package edu.duke.ece651.teamX.shared;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;
import org.junit.jupiter.api.Test;

public class TerritoryTest {

  @Test
  public void test_remove() {
    Territory t1 = new Territory("t1");
    assertThrows(IllegalArgumentException.class, () -> t1.removeUnits(1));
  }

  @Test
  public void test_addNeigbors() {
    Territory t1 = new Territory("t1");
    Territory t2 = new Territory("t2");
    assertEquals(true, t1.addNeighbors(t2));
    assertEquals(false, t1.addNeighbors(t2));
  }

  @Test
  public void test_Territory() {
    // Test constructor and getName function
    Territory t1 = new Territory("aTerritory");
    Territory t2 = new Territory("aTerritory");
    assertEquals(t1.getName(), "aTerritory");
    // Test equals function
    assertEquals(t1, t2);
    assertEquals(t1, t1);
    Territory t3 = new Territory("bTerritory");
    assertNotEquals(t1, t3);
    assertNotEquals(t2, t3);
    assertNotEquals(t1, null);
    // Test adding to a dictionary for later map class
    HashMap<Territory, String> terrDic = new HashMap<Territory, String>();
    terrDic.put(t1, "owner1");
    assertEquals(terrDic.get(t1), "owner1");
    assertEquals(terrDic.get(t2), "owner1");
  }

  @Test
  void testRemoveFromList() {
    Territory t1 = new Territory("a", 3);
    ArrayList<Integer> a1 = new ArrayList<>();
    ArrayList<Integer> a2 = new ArrayList<>();
    a1.add(0);
    a1.add(1);
    t1.removeUnitsFromList(a1);
    assertEquals(t1.getUnitsNumber(), 1);
    a2.add(1);
    assertThrows(IllegalArgumentException.class, () -> t1.removeUnitsFromList(a2));
  }

  @Test
  void getTerritorySize() {
    Territory t1 = new Territory("RockyCliffs", 3);
    Territory t2 = new Territory("invalid name", 3);
    assertEquals(t1.getTerritorySize(), 7);
    assertEquals(t2.getTerritorySize(), -1);
  }

  @Test
  void testUnitDit() {
    Territory t1 = new Territory("RockyCliffs", 3);
    BasicUnit bu = new BasicUnit();
    t1.addUnits(5, "Red");
    bu.upgradeLevel(2);
    t1.addUnits(bu, 4);
    HashMap<String, ArrayList<Integer>> unitDict = t1.getUnitsDit();
    assertEquals(2, unitDict.size());
    assertEquals(8, unitDict.get("level_0_unit").size());
    assertEquals(4, unitDict.get("level_2_unit").size());
    assertEquals(8, t1.getUnitCountByLevel(0));
    assertEquals(4, t1.getUnitCountByLevel(2));
    assertThrows(IllegalArgumentException.class,
        () -> t1.removeLevelUnits("level_1_unit", 1));
    assertThrows(IllegalArgumentException.class,
        () -> t1.removeLevelUnits("level_0_unit", 9));
  }

  @Test
  void testCloak() {
    Territory t1 = new Territory("RockyCliffs", 3);
    t1.addCloak();
    assertThrows(IllegalArgumentException.class, () -> t1.addCloak());
    assertTrue(t1.isCloaked());
    t1.resetCloak();
    assertFalse(t1.isCloaked());
    t1.addCloak();
    t1.reduceOneCloak();
    t1.reduceOneCloak();
    assertTrue(t1.isCloaked());
    t1.reduceOneCloak();
    assertFalse(t1.isCloaked());
    t1.reduceOneCloak();
    assertFalse(t1.isCloaked());
  }

  @Test
  void testSpy() {
    Territory t1 = new Territory("RockyCliffs", 3);
    ArrayList<Spy> redSpies = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      redSpies.add(new Spy("Red"));
    }
    t1.addSpies(redSpies);
    ArrayList<Spy> greenSpies = new ArrayList<>();
    for (int i = 0; i < 3; i++) {
      greenSpies.add(new Spy("Green"));
    }
    t1.addSpies(greenSpies);
    assertEquals(greenSpies.size(), t1.getSpyIndsFromPlayer("Green").size());
    assertEquals(0, t1.getSpyIndsFromPlayer("A").size());
    ArrayList<Spy> res = t1.removeSpies("Red", 4);
    assertEquals(1, t1.getSpyIndsFromPlayer("Red").size());
    assertEquals(4, res.size());
    assertThrows(IllegalArgumentException.class, () -> t1.removeSpies("Red", 2));

    Spy s = greenSpies.get(0);
    greenSpies.get(0).recordMove();
    assertTrue(s.checkMove());
    t1.turnReset();
    assertFalse(s.checkMove());
  }
}
