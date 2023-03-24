package edu.duke.ece651.teamX.shared;

import java.util.ArrayList;

public class MoveProcessor {

  private Map map;
  private ArrayList<MoveSender> allMove;
  private MoveValidChecker checker;

  public MoveProcessor(ArrayList<MoveSender> _allMove, Map _map) throws IllegalArgumentException {
    map = _map;
    allMove = _allMove;

    checker = new MoveValidChecker(allMove, map);
//    checker.checkValid();
  }

  public void resolveAllMove() {
    for (MoveSender m : allMove) {
      Territory source = map.getTerritoryByName(m.getSource().getName());
      Territory destination = map.getTerritoryByName(m.getDestination().getName());
      int num = m.getUnitsNum();
//      set the arraylist of units with certain number
      source.removeUnits(num);
      destination.addUnits(null, num);
      TextDisplayer.displayMove(source, destination, num, map.getOwner(source));
    }
  }
}

