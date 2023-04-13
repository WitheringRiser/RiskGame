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
}
