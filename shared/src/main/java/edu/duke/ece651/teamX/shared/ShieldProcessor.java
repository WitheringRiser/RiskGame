package edu.duke.ece651.teamX.shared;

import java.util.ArrayList;

public class ShieldProcessor {
  private ArrayList<ShieldSender> allShield;
  private Map map;

  public ShieldProcessor(ArrayList<ShieldSender> _allShield, Map _map) {
    this.allShield = _allShield;
    this.map = _map;
  }

  public void resovleAllShield() {
    for (ShieldSender s : allShield) {
      if (s != null) {
        Territory source = map.getTerritoryByName(s.getSource().getName());
        Player p = map.getOwner(source);
        if (p.consumeGold(source.getShieldCost(s.getLevel()))) {
          source.shieldTerritory(s.getLevel());
        } else {
          throw new IllegalArgumentException("gold resources are not enough to buy the shield");
        }
      }
    }
  }
}
