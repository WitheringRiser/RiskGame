package edu.duke.ece651.teamX.client;

import static org.junit.jupiter.api.Assertions.*;

import edu.duke.ece651.teamX.shared.*;
import java.util.ArrayList;
import java.util.HashMap;
import org.junit.jupiter.api.Test;

public class TextPromotTest {
  private HashMap<Integer, ArrayList<Territory> > formGroup() {
    ArrayList<Territory> terr_list1 = new ArrayList<Territory>();
    ArrayList<Territory> terr_list2 = new ArrayList<Territory>();
    Territory a = new Territory("A");
    Territory b = new Territory("B");
    Territory c = new Territory("C");
    Territory d = new Territory("D");
    assertEquals(true, a.addNeighbors(b));
    assertEquals(true, a.addNeighbors(c));
    assertEquals(true, b.addNeighbors(d));
    assertEquals(true, c.addNeighbors(d));
    terr_list1.add(a);
    terr_list1.add(b);
    terr_list2.add(c);
    terr_list2.add(d);
    HashMap<Integer, ArrayList<Territory> > res =
        new HashMap<Integer, ArrayList<Territory> >();
    res.put(0, terr_list1);
    res.put(1, terr_list2);
    return res;
  }
  @Test
  public void test_Begin() {
    Player p1 = new Player("A", 20);
    TextPromot promot = new TextPromot(p1);
    String expected =
        "Welcome to the RISK game, Player A!\n"
        + "Please wait a few moments while we gather all players before we start...\n";
    assertEquals(expected, promot.startPromot());
  }

  @Test
  public void test_displayGroup() {
    Player p1 = new Player("A", 20);
    TextPromot promot = new TextPromot(p1);
    HashMap<Integer, ArrayList<Territory> > groups = formGroup();
    String expected = "Please choose one territory group as your initial territories:\n"
                      +
                      "(type the corresponding number to indicate your choice, e.g. 1)\n"
                      + "\n"
                      + "(0) Group 0:\n"
                      + "   - A (next to: B, C)\n"
                      + "   - B (next to: A, D)\n"
                      + "(1) Group 1:\n"
                      + "   - C (next to: A, D)\n"
                      + "   - D (next to: B, C)\n";
    assertEquals(expected, promot.displayTerrGroup(groups));
  }
  @Test
  public void test_setUnitPromot() {
    Player p1 = new Player("A", 20);
    TextPromot promot = new TextPromot(p1);
    HashMap<Integer, ArrayList<Territory> > groups = formGroup();
    String expected =
        "A player: you are going to place the units in your territories.\n\n"
        + "You have 20 available units to place.\n"
        + "Please choose a territory to place units:\n"
        + "   (0) A (next to: B, C) with 0 units\n"
        + "   (1) B (next to: A, D) with 0 units\n";
    assertEquals(expected, promot.setUnitPromot(groups.get(0), 20));
  }
}
