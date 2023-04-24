package edu.duke.ece651.teamX.shared;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

public class ShieldProcessorTest {
  @Test
  public void test_resolve() {
    Territory t1 = new Territory("a", 3);
    Territory t2 = new Territory("b", 4);
    ShieldSender s1 = new ShieldSender(t1, 1);
    ShieldSender s2 = new ShieldSender(t2, 3);
    ArrayList<ShieldSender> all = new ArrayList<>();
    all.add(s2);
    Player play1 = new Player("p1", 10);
    Player play2 = new Player("p2", 20);
    Map map = new Map();
    map.addTerritory(t1, play1);
    map.addTerritory(t2, play2);
    ShieldProcessor p = new ShieldProcessor(all, map);
    assertThrows(IllegalArgumentException.class, () -> p.resovleAllShield());
    all.remove(s2);
    all.add(s1);
    all.add(null);
    ShieldProcessor p2 = new ShieldProcessor(all, map);
    p2.resovleAllShield();
    assertEquals(map.getTerritoryByName("a").getShieldLevel(), 1);
    assertEquals(map.getTerritoryByName("b").getShieldLevel(), 0);
    map.getTerritoryByName("a").releaseShield();
    assertEquals(map.getTerritoryByName("a").getShieldLevel(), 0);

  }

}
