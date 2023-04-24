package edu.duke.ece651.teamX.shared;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import org.junit.jupiter.api.Test;
public class FrogViewTest {
  @Test
  public void test_FrogView() {
    Territory t1 = new Territory("A", 3);
    Territory t2 = new Territory("B", 3);
    Territory t3 = new Territory("C", 3);
    Player p1 = new Player("Red", 10);
    Player p2 = new Player("Green", 10);
    t2.addNeighbors(t1);
    t2.addNeighbors(t3);
    Map m = new Map();
    m.addTerritory(t1, p1);
    m.addTerritory(t2, p1);
    m.addTerritory(t3, p2);
    FrogView fv = new FrogView("Green");
    assertEquals(fv.getTerrInfo("C").length(), 0);
    fv.refreshMap(m);
    assertTrue(fv.getTerrInfo("C").length() > 0);
    assertTrue(fv.getTerrInfo("B").length() > 0);
    assertTrue(fv.getTerrInfo("A").length() == 0);
    t2.addCloak();
    fv.refreshMap(m);
    fv.refreshMap(m);
    assertTrue(fv.getTerrInfo("B").startsWith("=="));
    ArrayList<Spy> spies = new ArrayList<>();
    spies.add(new Spy("Green"));
    t2.addSpies(spies);
    fv.refreshMap(m);
    assertFalse(fv.getTerrInfo("B").startsWith("=="));
  }
}
