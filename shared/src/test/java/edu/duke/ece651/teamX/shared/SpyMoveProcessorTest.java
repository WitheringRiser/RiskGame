package edu.duke.ece651.teamX.shared;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import org.junit.jupiter.api.Test;

public class SpyMoveProcessorTest {

  @Test
  public void test_SpyMove() {
    Territory t1 = new Territory("Beach", 3);
    Territory t2 = new Territory("Swamp", 3);
    Territory t3 = new Territory("Desert", 3);
    Player p1 = new Player("Red", 10);
    Player p2 = new Player("Green", 10);
    Map m = new Map();
    t2.addNeighbors(t3);
    m.addTerritory(t1, p1);
    m.addTerritory(t2, p2);
    m.addTerritory(t3, p2);
    ArrayList<Spy> spies = new ArrayList<>();
    for (int i = 0; i < 11; i++) {
      spies.add(new Spy("Red"));
    }
    t2.addSpies(spies);
    ArrayList<SpyMoveSender> all = new ArrayList<>();
    all.add(new SpyMoveSender(t2, t3, 5, "Red"));
    SpyMoveProcessor smp = new SpyMoveProcessor(all, m);
    smp.resolveAllSpyMove();
    assertEquals(5, t3.getSpyIndsFromPlayer("Red").size());
    assertEquals(6, t2.getSpyIndsFromPlayer("Red").size());
    assertEquals(0, t3.getSpyMoveIndsFromPlayer("Red").size());
    assertEquals(50, p1.getFoodResource());
    //Not enough spies
    all.clear();
    all.add(new SpyMoveSender(t2, t3, 7, "Red"));
    SpyMoveProcessor smp2 = new SpyMoveProcessor(all, m);
    assertThrows(IllegalArgumentException.class, () -> smp2.resolveAllSpyMove());
    //Not enough food
    all.clear();
    all.add(new SpyMoveSender(t2, t3, 6, "Red"));
    SpyMoveProcessor smp3 = new SpyMoveProcessor(all, m);
    assertThrows(IllegalArgumentException.class, () -> smp3.resolveAllSpyMove());
    //Not connected
    all.clear();
    all.add(new SpyMoveSender(t2, t1, 1, "Red"));
    SpyMoveProcessor smp4 = new SpyMoveProcessor(all, m);
    assertThrows(IllegalArgumentException.class, () -> smp4.resolveAllSpyMove());
  }
}
