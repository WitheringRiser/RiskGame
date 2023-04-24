package edu.duke.ece651.teamX.shared;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import org.junit.jupiter.api.Test;

public class UpgradeProcessorTest {

  @Test
  public void test_resolve() {
    Territory t1 = new Territory("a", 3);
    Territory t2 = new Territory("b", 4);
    UpgradeSender u1 = new UpgradeSender(t1, t1.getUnits().get(0).getName(), 2, 1);
    UpgradeSender u2 = new UpgradeSender(t2, t2.getUnits().get(0).getName(), 2, 2);
    UpgradeSender u3 = new UpgradeSender(t2, t2.getUnits().get(0).getName(), 5, 2);
    UpgradeSender u4 = new UpgradeSender(t1, t1.getUnits().get(0).getName(), 1, 1);
    ArrayList<UpgradeSender> all = new ArrayList<>();
    all.add(u2);
    Player play1 = new Player("p1", 10);
    Player play2 = new Player("p2", 20);
    Map map = new Map();
    map.addTerritory(t1, play1);
    map.addTerritory(t2, play2);
    UpgradeProcessor p = new UpgradeProcessor(all, map);
    assertThrows(IllegalArgumentException.class, () -> p.resolveAllUpgrade());
    all.remove(u2);
    all.add(u1);
    all.add(u4);
    all.add(null);
    UpgradeProcessor p2 = new UpgradeProcessor(all, map);
    p2.resolveAllUpgrade();
    assertEquals(map.getTerritoryByName("a").getUnits().get(0).getName(), "level_1_unit");
    assertEquals(map.getTerritoryByName("a").getUnits().get(1).getName(), "level_1_unit");
    assertEquals(map.getTerritoryByName("a").getUnits().get(2).getName(), "level_1_unit");

    all.remove(u1);
    all.remove(u4);
    all.add(u3);
    UpgradeProcessor p3 = new UpgradeProcessor(all, map);
    assertThrows(IllegalArgumentException.class, () -> p3.resolveAllUpgrade());
  }

  @Test
  void test_resolveSpy() {
    Territory t1 = new Territory("A", 3);
    Player play1 = new Player("p1", 10);
    Map map = new Map();
    map.addTerritory(t1, play1);
    ArrayList<Unit> level1U = new ArrayList<>();
    for (int i = 0; i < 4; i++) {
      Unit u = new BasicUnit();
      u.upgradeLevel(1);
      level1U.add(u);
    }
    t1.addUnits(level1U);
    ArrayList<UpgradeSender> all = new ArrayList<>();
    UpgradeSender u1 =
        new UpgradeSender(t1, t1.getUnits().get(0).getName(), 2, -1);  //Level not enough
    UpgradeSender u2 =
        new UpgradeSender(t1, level1U.get(0).getName(), 5, -1);  //not enough units
    UpgradeSender u3 = new UpgradeSender(t1, level1U.get(0).getName(), 2, -1);
    all.add(u1);
    UpgradeProcessor p1 = new UpgradeProcessor(all, map);
    assertThrows(IllegalArgumentException.class, () -> p1.resolveAllUpgrade());
    assertEquals(t1.getUnitCountByLevel(0), 3);  //make sure added back
    all.clear();
    all.add(u2);
    UpgradeProcessor p2 = new UpgradeProcessor(all, map);
    assertThrows(IllegalArgumentException.class, () -> p2.resolveAllUpgrade());

    all.clear();
    all.add(u3);
    UpgradeProcessor p3 = new UpgradeProcessor(all, map);
    p3.resolveAllUpgrade();
    assertEquals(t1.getUnitCountByLevel(1), 2);
    assertEquals(t1.getSpyIndsFromPlayer("p1").size(), 2);
    assertEquals(play1.getTechResource(), 80);
    play1.consumeTech(61);
    assertThrows(IllegalArgumentException.class, () -> p3.resolveAllUpgrade());
  }
}
