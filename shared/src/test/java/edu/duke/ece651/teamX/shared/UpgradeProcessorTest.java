package edu.duke.ece651.teamX.shared;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

public class UpgradeProcessorTest {

  @Test
  public void test_resolve() {
    UpgradeSender u1 = new UpgradeSender(new Territory("a"), 0, 1);
    UpgradeSender u2 = new UpgradeSender(new Territory("b"), 5, 1);
    ArrayList<UpgradeSender> all = new ArrayList<>();
    all.add(u2);
    Player play1 = new Player("p1", 10);
    Player play2 = new Player("p2", 20);
    Map map = new Map();
    map.addTerritory(new Territory("a", 3), play1);
    map.addTerritory(new Territory("b", 3), play2);
    UpgradeProcessor p = new UpgradeProcessor(all, map);
    assertThrows(IllegalArgumentException.class, () -> p.resolveAllUpgrade());
    all.remove(u2);
    all.add(u1);
    all.add(null);
    UpgradeProcessor p2 = new UpgradeProcessor(all, map);
    p2.resolveAllUpgrade();
    assertEquals(map.getTerritoryByName("a").getUnits().get(0).getName(), "level_1_unit");
  }

}
