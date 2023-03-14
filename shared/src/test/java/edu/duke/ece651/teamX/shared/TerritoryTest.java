package edu.duke.ece651.teamX.shared;

import static org.junit.jupiter.api.Assertions.*;

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
}
