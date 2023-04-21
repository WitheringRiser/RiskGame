package edu.duke.ece651.teamX.shared;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class TextDisplayerTest {
  @Test
  public void test_makeHeader() {
    TextDisplayer dis = new TextDisplayer(null);
    String name = "red";
    String ans = name + " player:\n";
    ans += "-----------\n";
    assertEquals(ans, dis.makeHeader(name));
  }

  @Test
  public void test_display() {
    Territory t1 = new Territory("duke");
    Territory t2 = new Territory("cary");
    Territory t3 = new Territory("durham");
    t1.addNeighbors(t2);
    t2.addNeighbors(t3);
    t1.addUnits(null, 2);
    t2.addUnits(null, 3);
    t3.addUnits(null, 1);
    Player p1 = new Player("red", 0);
    Player p2 = new Player("green", 0);
    Map m = new Map();
    m.addTerritory(t1, null);
    m.addTerritory(t2, null);
    m.addTerritory(t3, null);
    m.setOwner(t1, p1);
    m.setOwner(t2, p1);
    m.setOwner(t3, p2);

    TextDisplayer dis = new TextDisplayer(m);
    String expected = "red player:\n"
                      + "-----------\n"
                      + "2 units in duke (next to: cary)\n"
                      + "3 units in cary (next to: duke, durham)\n\n"
                      + "green player:\n"
                      + "-----------\n"
                      + "1 units in durham (next to: cary)\n\n";
    assertEquals(expected, dis.display());

    dis.displayMove(t1, t2, 3, p1);
  }
}
