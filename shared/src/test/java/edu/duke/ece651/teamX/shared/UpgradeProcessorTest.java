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

}
