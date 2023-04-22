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
      if (u != null) {
        Territory source = map.getTerritoryByName(u.getSource().getName());
        Player p = map.getOwner(source);
        ArrayList<Integer> indexList = source.getUnitsDit().get(u.getName());
        if (indexList.size() < u.getNum()) {
          throw new IllegalArgumentException("Territory has no enough units to complete upgrade!");
        }
        for (int i = 0; i < u.getNum(); ++i) {
          String result = p.upgradeUnit(source, indexList.get(i), u.getToLevel());
          if (result != null) {
            throw new IllegalArgumentException(result);
          }
        }
      }
    }
  }
}
