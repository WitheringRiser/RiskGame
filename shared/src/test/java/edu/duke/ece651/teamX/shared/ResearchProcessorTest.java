package edu.duke.ece651.teamX.shared;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

public class ResearchProcessorTest {
  @Test
  public void test_resolve() {
    ResearchSender r1 = new ResearchSender(new Territory("a"));
    ResearchSender r2 = new ResearchSender(new Territory("b"));
    ArrayList<ResearchSender> all = new ArrayList<>();
    all.add(r1);
    all.add(r2);

    Map map = new Map();
    map.addTerritory(new Territory("a"), null);
    map.addTerritory(new Territory("b"), null);

    ResearchProcessor p = new ResearchProcessor(all, map);
    p.resovleAllResearch();
    p.resovleAllResearch();
    assertEquals(map.getTerritoryByName("a").getTechLevel(), 3);
    assertEquals(map.getTerritoryByName("b").getTechLevel(), 3);
    assertThrows(IllegalArgumentException.class, () -> p.resovleAllResearch());
  }

}
