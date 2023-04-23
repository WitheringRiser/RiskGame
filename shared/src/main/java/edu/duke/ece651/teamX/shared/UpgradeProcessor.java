package edu.duke.ece651.teamX.shared;

import java.util.ArrayList;

public class UpgradeProcessor {
  private ArrayList<UpgradeSender> allUpgrade;
  private Map map;

  public UpgradeProcessor(ArrayList<UpgradeSender> _allUpgrade, Map map) {
    this.allUpgrade = _allUpgrade;
    this.map = map;
  }

  public void resolveAllSpy() {
    // Handle raise Spy
    for (UpgradeSender u : allUpgrade) {
      if (u != null && u.getToLevel() == -1) {
        Territory source = map.getTerritoryByName(u.getSource().getName());
        Player p = map.getOwner(source);
        ArrayList<Integer> indexList = source.getUnitsDit().get(u.getName());
        if (indexList.size() < u.getNum()) {
          throw new IllegalArgumentException(
              "Territory has no enough units to upgrade to Spy!");
        }
        int cost = Spy.getSpyCost() * u.getNum();
        if (cost > p.getTechResource()) {
          throw new IllegalArgumentException("No enough technology resource");
        }
        ArrayList<Unit> removedUnits = source.removeLevelUnits(u.getName(), u.getNum());
        if (removedUnits.get(0).getLevel() < 1) {
          source.addUnits(removedUnits);  //added invlaid delete back
          throw new IllegalArgumentException("The units are too low to upgrade to Spy");
        }
        ArrayList<Spy> newSpies = new ArrayList<>();
        for (int i = 0; i < u.getNum(); i++) {
          newSpies.add(new Spy(p.getName()));
        }
        source.addSpies(newSpies);
        p.consumeTech(cost);
      }
    }
  }

  public void resolveAllUpgrade() {
    for (UpgradeSender u : allUpgrade) {
      if (u != null && u.getToLevel() >= 0) {
        Territory source = map.getTerritoryByName(u.getSource().getName());
        Player p = map.getOwner(source);
        ArrayList<Integer> indexList = source.getUnitsDit().get(u.getName());
        if (indexList.size() < u.getNum()) {
          throw new IllegalArgumentException(
              "Territory has no enough units to complete upgrade!");
        }
        for (int i = 0; i < u.getNum(); ++i) {
          String result = p.upgradeUnit(source, indexList.get(i), u.getToLevel());
          if (result != null) {
            throw new IllegalArgumentException(result);
          }
        }
      }
    }
    resolveAllSpy();
  }
}
