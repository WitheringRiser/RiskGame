package edu.duke.ece651.teamX.shared;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class MapTest {
  @Test
  public void test_MapBasics() {
    //Set up the map
    Map my_map = new Map();
    Territory t1 = new Territory("aTerritory");
    Territory t2 = new Territory("aTerritory");
    assertEquals(0, my_map.getTerritoryNum());
    //Test addTerritory
    assertEquals(true, my_map.addTerritory(t1));
    //Should not add the same territory more than once
    assertEquals(1, my_map.getTerritoryNum());
    assertEquals(false, my_map.addTerritory(t1));
    assertEquals(1, my_map.getTerritoryNum());
    assertEquals(false, my_map.addTerritory(t2));
    assertEquals(1, my_map.getTerritoryNum());
    //Should not add the null
    assertEquals(false, my_map.addTerritory(null));
    assertEquals(1, my_map.getTerritoryNum());

    //Test getowner
    assertEquals(null, my_map.getOwner(t1));
    assertEquals(null, my_map.getOwner(t2));
    assertEquals(null, my_map.getOwner(new Territory("aTerritory")));
    //Should not get null
    assertThrows(IllegalArgumentException.class, () -> my_map.getOwner(null));
    //Should not get non-exist territory's owner
    assertThrows(IllegalArgumentException.class,
                 () -> my_map.getOwner(new Territory("bTerritory")));
    //Test add more territories
    assertEquals(true, my_map.addTerritory(new Territory("bTerritory")));
    assertEquals(2, my_map.getTerritoryNum());
    assertEquals(null, my_map.getOwner(new Territory("bTerritory")));
    assertEquals(true, my_map.addTerritory(new Territory("cTerritory")));
    assertEquals(true, my_map.addTerritory(new Territory("dTerritory")));
    assertEquals(true, my_map.addTerritory(new Territory("eTerritory")));
    assertEquals(5, my_map.getTerritoryNum());
  }
}
