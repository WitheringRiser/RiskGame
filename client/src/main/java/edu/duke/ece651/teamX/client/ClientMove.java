package edu.duke.ece651.teamX.client;

import edu.duke.ece651.teamX.shared.MoveSender;
import java.util.ArrayList;
import edu.duke.ece651.teamX.shared.*;
import java.net.Socket;
import java.util.Iterator;

public class ClientMove extends ClientTurnAction<MoveSender> {

  public ClientMove(Socket s, Map m, Player ply) {
    super(s, m, ply);
  }

  /**
   * Find destination the units can move to
   */
  public ArrayList<Territory> findDestTerrs(Territory source) {
    if (!map.getOwner(source).equals(this.player)) {
      throw new IllegalArgumentException("The territory does not belong to this player");
    }
    ArrayList<Territory> dests = new ArrayList<Territory>();
    ArrayList<Territory> visited = new ArrayList<Territory>();
    ArrayList<Territory> todo = new ArrayList<Territory>();
    todo.add(source);
    while (!todo.isEmpty()) {
      Territory curr = todo.get(0);
      todo.remove(0);
      if (!visited.contains(curr)) {
        Iterator<Territory> iter = curr.getNeighbours();
        while (iter.hasNext()) {
          Territory t = iter.next();
          if (map.getOwner(t).equals(this.player)) {
            if (!dests.contains(t) && (!t.equals(source))) {
              dests.add(t);
            }
            todo.add(t);
          }
        }
        visited.add(curr);
      }
    }
    return dests;
  }


  public int calculateCost(Territory source, Territory dest, int num) {
    int distance = MoveProcessor.getMinCostPathBetweenSourceDest(source, dest, map);
    return num * distance;
  }

  /**
   * @param source
   * @param dest
   * @param indList
   * @throws IllegalArgumentException to indicate invalid movement
   */
  public void perform_res(Territory source, Territory dest, String name, int num) {

    int cost = calculateCost(source, dest, num);
    if (cost > player.getFoodResource()) {
      throw new IllegalArgumentException("Food resource is not enough for the moving cost " + cost);
    }
    System.out.println(name);
    dest.addUnits(source.removeLevelUnits(name, num));
    player.consumeFood(cost);
    MoveSender ms = new MoveSender(source, dest, num, name);
    actions.add(ms);
  }
}
