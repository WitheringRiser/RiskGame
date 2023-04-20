package edu.duke.ece651.teamX.shared;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.PriorityQueue;

public class MoveProcessor {

  private Map map;
  private ArrayList<MoveSender> allMove;
  private MoveValidChecker checker;

  public MoveProcessor(ArrayList<MoveSender> _allMove, Map _map) throws IllegalArgumentException {
    map = _map;
    allMove = _allMove;

    checker = new MoveValidChecker(allMove, map);
    // checker.checkValid();
  }

  public void resolveAllMove() {
    for (MoveSender m : allMove) {
      Territory source = map.getTerritoryByName(m.getSource().getName());
      Territory destination = map.getTerritoryByName(m.getDestination().getName());
      ArrayList<Integer> indexList = m.getIndexList();
      // set the arraylist of units with certain number
      destination.addUnits(source.removeUnitsFromList(indexList));
      TextDisplayer.displayMove(source, destination, indexList.size(), map.getOwner(source));
      Player p = map.getOwner(source);
      p.consumeFood(consumeFood(source, destination, map, indexList.size(), 1));
    }
  }

  public static int getMinCostPathBetweenSourceDest(Territory source, Territory destination, Map map) {
    Player owner = map.getOwner(source);

    PriorityQueue<TerritoryWrapper> queue = new PriorityQueue<>(
        Comparator.comparingInt(TerritoryWrapper::getCost));
    HashMap<Territory, TerritoryWrapper> visited = new HashMap<>();

    TerritoryWrapper sourceWrapper = new TerritoryWrapper(source, 0, null);
    queue.add(sourceWrapper);
    visited.put(source, sourceWrapper);

    while (!queue.isEmpty()) {
      TerritoryWrapper currWrapper = queue.poll();
      Territory curr = currWrapper.getTerritory();

      if (curr.equals(destination)) {
        return currWrapper.getCost();
      }

      Iterator<Territory> iter = curr.getNeighbours();
      while (iter.hasNext()) {
        Territory t = iter.next();
        if (map.getOwner(t).equals(owner)) {
          int edgeCost = curr.getTerritorySize() + t.getTerritorySize();
          int newCost = currWrapper.getCost() + edgeCost;

          if (!visited.containsKey(t) || visited.get(t).getCost() > newCost) {
            TerritoryWrapper tWrapper = new TerritoryWrapper(t, newCost, currWrapper);
            queue.add(tWrapper);
            visited.put(t, tWrapper);
          }
        }
      }
    }
    return -1;
  }

  public int consumeFood(Territory source, Territory destination, Map map, int unitsNum, int coef) {
    int cost = getMinCostPathBetweenSourceDest(source, destination, map);
    return cost * coef * unitsNum == 0 ? 1 : cost * coef * unitsNum; // if cost is 0, then it's 1 (at least cost one)
  }

  private static class TerritoryWrapper {

    private Territory territory;
    private int cost;
    private TerritoryWrapper previous;

    public TerritoryWrapper(Territory territory, int cost, TerritoryWrapper previous) {
      this.territory = territory;
      this.cost = cost;
      this.previous = previous;
    }

    public Territory getTerritory() {
      return territory;
    }

    public int getCost() {
      return cost;
    }

  }

}
