package edu.duke.ece651.teamX.shared;

import java.util.ArrayList;

public class CloakProcessor {

  private Map map;
  private ArrayList<CloakSender> allCloaks;

  public CloakProcessor(ArrayList<CloakSender> _allCloaks, Map m) {
    map = m;
    allCloaks = _allCloaks;
  }

  /**
   * Set the cloak cost
   *
   * @return
   */
  public static int getCloakCost() {
    return 10;
  }

  public static int getUnlockCloakCost() {
    return 50;
  }

  public void resolveAllCloaks() {
    //Reduce 1 cloak in the beginning
    for (Territory t : map.getAllTerritories()) {
      t.reduceOneCloak();
    }
    for (CloakSender cs : allCloaks) {
      Territory source = map.getTerritoryByName(cs.getSource().getName());
      Player player = map.getOwner(source);
      if (!player.getCanCloak()) {
        throw new IllegalArgumentException("This player cannot use cloak");
      }
      if (player.getTechResource() < getCloakCost()) {
        throw new IllegalArgumentException("No enough resource for cloak");
      }
      source.addCloak();
      player.consumeTech(getCloakCost());
    }
  }
}
