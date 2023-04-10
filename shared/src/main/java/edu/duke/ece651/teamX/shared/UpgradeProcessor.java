package edu.duke.ece651.teamX.shared;

import java.util.ArrayList;

public class UpgradeProcessor {

  private ArrayList<UpgradeSender> allUpgrade;
  private Map map;

  public UpgradeProcessor(ArrayList<UpgradeSender> _allUpgrade, Map map) {
    this.allUpgrade = _allUpgrade;
    this.map = map;
  }

  public void resolveAllUpgrade() {
    for (UpgradeSender u : allUpgrade) {
      Territory source = map.getTerritoryByName(u.getSource().getName());
      Player p = map.getOwner(source);
      String result = p.upgradeUnit(source, u.getIndex(), u.getToLevel());
      if (result != null) {
        throw new IllegalArgumentException(result);
      }
    }
  }
}
