package edu.duke.ece651.teamX.shared;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;
import org.junit.jupiter.api.Test;
public class MapTest {
  @Test
  public void test_MapBasics() {
    //Set up the map
    Map my_map = new Map();
    assertEquals(my_map, my_map);
    assertNotEquals(my_map, null);
    Territory t1 = new Territory("aTerritory");
    Territory t2 = new Territory("aTerritory");
    assertEquals(0, my_map.getTerritoryNum());
    HashMap<Map, String> try_dict = new HashMap<Map, String>();
    Map try_map = new Map();
    try_dict.put(my_map, "0");
    assertEquals(true, try_dict.containsKey(my_map));
    assertEquals(true, try_dict.containsKey(try_map));
    //Test addTerritory
    assertEquals(true, my_map.addTerritory(t1, null));
    assertEquals(false, try_dict.containsKey(my_map));
    //Should not add the same territory more than once
    assertEquals(1, my_map.getTerritoryNum());
    assertEquals(false, my_map.addTerritory(t1, null));
    assertEquals(1, my_map.getTerritoryNum());
    assertEquals(false, my_map.addTerritory(t2, null));
    assertEquals(1, my_map.getTerritoryNum());
    //Should not add the null
    assertEquals(false, my_map.addTerritory(null, null));
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
    assertEquals(true, my_map.addTerritory(new Territory("bTerritory"), null));
    assertEquals(2, my_map.getTerritoryNum());
    assertEquals(null, my_map.getOwner(new Territory("bTerritory")));
    assertEquals(true, my_map.addTerritory(new Territory("cTerritory"), null));
    assertEquals(true, my_map.addTerritory(new Territory("dTerritory"), null));
    assertEquals(true, my_map.addTerritory(new Territory("eTerritory"), null));
    assertEquals(5, my_map.getTerritoryNum());
  }

  @Test
  public void test_setOwner() {
    Map my_map = new Map();
    Territory t1 = new Territory("aTerritory");
    Territory t2 = new Territory("aTerritory");
    Territory t3 = new Territory("bTerritory");
    Territory t4 = new Territory("cTerritory");
    assertEquals(true, my_map.addTerritory(t1, null));
    assertEquals(true, my_map.addTerritory(t3, null));
    assertEquals(true, my_map.addTerritory(t4, null));
    Player p1 = new Player("Red", 20);
    Player p2 = new Player("Blue", 20);
    //Test normal function of setOwner
    assertEquals(true, my_map.setOwner(t1, p1));
    assertEquals(p1, my_map.getOwner(t1));
    assertEquals(true, my_map.setOwner(t2, p2));
    assertEquals(p2, my_map.getOwner(t1));
    //Set null or non-exist territory
    assertEquals(false, my_map.setOwner(null, p2));
    assertEquals(false, my_map.setOwner(new Territory("dTerritory"), p2));

    assertEquals(true, my_map.setOwner(t3, p1));
    assertEquals(true, my_map.setOwner(t4, p1));
    ArrayList<Territory> terrs = my_map.getTerritories(p1);
    assertEquals(2, terrs.size());
    assertEquals(true, terrs.contains(new Territory("bTerritory")));
    assertEquals(true, terrs.contains(new Territory("cTerritory")));
    assertEquals(false, terrs.contains(new Territory("aTerritory")));
  }
}
