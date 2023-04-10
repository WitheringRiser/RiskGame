package edu.duke.ece651.teamX.shared;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

public class ResearchProcessorTest {

  @Test
  public void test_resolve() {
    ResearchSender r1 = new ResearchSender(new Player("a", 10));
    ResearchSender r2 = new ResearchSender(new Player("b", 10));
    ArrayList<ResearchSender> all = new ArrayList<>();
    all.add(r1);
    all.add(r2);

    Map map = new Map();
    map.addTerritory(new Territory("t1"), new Player("a", 10));
    map.addTerritory(new Territory("t2"), new Player("b", 10));

    ResearchProcessor p = new ResearchProcessor(all, map);
    p.resovleAllResearch();
    p.resovleAllResearch();
    assertEquals(map.getPlayerByName("a").getTechLevel(), 3);
    assertEquals(map.getPlayerByName("b").getTechLevel(), 3);
    assertThrows(IllegalArgumentException.class, () -> p.resovleAllResearch());
  }

}
