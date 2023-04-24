package edu.duke.ece651.teamX.shared;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class PlayerTest {
  @Test
  public void test_PlayerBasic() {
    Player p1 = new Player("A", 20);
    Player p2 = new Player("A", 20);
    Player p3 = new Player("B", 20);
    assertEquals("A", p1.getName());
    assertEquals(20, p1.getUnitNum());

    assertThrows(IllegalArgumentException.class, () -> new Player("A", -1));

    assertThrows(IllegalArgumentException.class, () -> new Player(null, 20));

    assertEquals(p1, p2);
    assertEquals(p1, p1);
    assertNotEquals(p1, p3);
    assertNotEquals(p1, null);
  }

  @Test
  void testResearch() {
    Player t1 = new Player("a", 10);
    assertEquals(t1.getTechLevel(), 1);
    assertTrue(t1.upgradeLevel());
    assertEquals(t1.getTechLevel(), 2);
    assertThrows(IllegalArgumentException.class, () -> t1.unlockCloak());
    assertTrue(t1.upgradeLevel());
    assertEquals(t1.getTechLevel(), 3);
    assertFalse(t1.upgradeLevel());
    assertEquals(t1.getTechLevel(), 3);
    assertThrows(IllegalArgumentException.class, () -> t1.unlockCloak());
    t1.increaseAllResource(10000);
    assertTrue(t1.upgradeLevel());
    assertTrue(t1.upgradeLevel());
    assertTrue(t1.upgradeLevel());
    assertEquals(t1.getTechLevel(), 6);
    assertThrows(IllegalArgumentException.class, () -> t1.getResearchNeedCost());
  }

  @Test
  void testUpgradeUnits() {
    Player t1 = new Player("a", 10);
    Territory terr = new Territory("aTerritory", 3);
    assertEquals(t1.upgradeUnit(terr, 0, 2),
                 "the maximum technology level doesn't permit this upgrade");
    assertEquals(t1.upgradeUnit(terr, 3, 1),
                 "the unit doesn't exist, please check the index");
    assertEquals(t1.upgradeUnit(terr, 0, 1), null);
    assertEquals(t1.upgradeUnit(terr, 0, 0),
                 "the level is not legal or the unit has reached the highest level");
    t1.upgradeLevel();
    t1.upgradeLevel();

    assertEquals(null, t1.upgradeUnit(terr, 1, 3));
    assertEquals(t1.upgradeUnit(terr, 2, 2),
                 "the technology resource is not enough to upgrade this unit");
  }

  @Test
  void testConsumeFood() {
    Player p1 = new Player("a", 20);
    assertTrue(p1.consumeFood(50));
    assertTrue(p1.consumeFood(50));
    assertEquals(p1.getFoodResource(), 0);
    assertEquals(p1.getTechResource(), 100);
    assertEquals(p1.getGoldResource(), 100);
    assertEquals(p1.getResearchNeedCost(), 20);
    assertFalse(p1.consumeFood(50));
    p1.increaseAllResource(40);
    assertEquals(p1.getFoodResource(), 40);
    assertEquals(p1.getTechResource(), 140);
  }
}
