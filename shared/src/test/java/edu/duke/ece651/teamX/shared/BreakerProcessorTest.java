package edu.duke.ece651.teamX.shared;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

public class BreakerProcessorTest {

  @Test
  public void test_resolve() {
    Territory t1 = new Territory("a", 3);
    Territory t2 = new Territory("b", 4);
    BreakerSender s1 = new BreakerSender(t1, 1);
    BreakerSender s2 = new BreakerSender(t2, 4);
    ArrayList<BreakerSender> all = new ArrayList<>();
    all.add(s2);
    Player play1 = new Player("p1", 10);
    Player play2 = new Player("p2", 20);
    Map map = new Map();
    map.addTerritory(t1, play1);
    map.addTerritory(t2, play2);
    BreakerProcessor p = new BreakerProcessor(all, map);
    assertThrows(IllegalArgumentException.class, () -> p.resovleAllBreak());
    all.remove(s2);
    all.add(s1);
    all.add(null);
    BreakerProcessor p2 = new BreakerProcessor(all, map);
    p2.resovleAllBreak();
    assertEquals(map.getTerritoryByName("a").getBreakerLevel(), 1);
    assertEquals(map.getTerritoryByName("b").getBreakerLevel(), 0);
    map.getTerritoryByName("a").releaseBreaker();
    assertEquals(map.getTerritoryByName("a").getBreakerLevel(), 0);

  }

}
