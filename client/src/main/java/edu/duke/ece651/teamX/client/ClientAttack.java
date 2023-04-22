package edu.duke.ece651.teamX.client;

import java.util.ArrayList;
import edu.duke.ece651.teamX.shared.*;
import java.net.Socket;
import java.util.Iterator;

public class ClientAttack extends ClientTurnAction<AttackSender> {

  public ClientAttack(Socket s, Map m, Player ply) {
    super(s, m, ply);
  }

  /**
   * Find all neighbors of a territory that are not belongs to this player
   *
   * @param source is the source territory to start attack
   * @return is the list of direct neighbor territories of other players
   */
  public ArrayList<Territory> findDestTerrs(Territory source) {
    if (!map.getOwner(source).equals(this.player)) {
      throw new IllegalArgumentException("The territory does not belong to this player");
    }
    Iterator<Territory> iter = source.getNeighbours();
    ArrayList<Territory> dests = new ArrayList<Territory>();
    while (iter.hasNext()) {
      Territory curr = iter.next();
      if (!map.getOwner(curr).equals(this.player)) {
        dests.add(curr);
      }
    }
    return dests;
  }

  /**
   * 
   * @param source
   * @param dest
   * @param indList
   * @throw IllegalArgumentException to indicate the invalid action
   */
  public void perform_res(Territory source, Territory dest, String name, int num) {
    int cost = num * (source.getTerritorySize() + dest.getTerritorySize());
    if (cost > player.getFoodResource()) {
      throw new IllegalArgumentException("The food resource is not enough for the attacking cost " + cost);
    }
    source.removeLevelUnits(name, num);
    player.consumeFood(cost);
    AttackSender atts = new AttackSender(source, dest, num, name);
    this.actions.add(atts);

  }

}
