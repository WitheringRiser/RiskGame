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
                      + "   - B (next to: A, D)\n\n"
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
    String expected = "You have 20 available units to place.\n"
                      + "Please choose a territory to place units:\n"
                      + "(0) A (next to: B, C) with 0 units\n"
                      + "(1) B (next to: A, D) with 0 units\n";
    assertEquals(expected, promot.setUnitPromot(groups.get(0), 20, false));
    expected =
        "A player: you are going to place the units in your territories.\n\n" + expected;
    assertEquals(expected, promot.setUnitPromot(groups.get(0), 20, true));

    expected = "Please indicate how many units you want to use.\n"
               + "(type B to go back)\n";
    assertEquals(expected, promot.enterNumPromot());
    expected = "The input is not a valid option, please choose again.\n";
    assertEquals(expected, promot.enterAgainPromot());
  }
  @Test
  public void test_choose_Promot() {
    Player p1 = new Player("A", 100);
    TextPromot promot = new TextPromot(p1);
    ArrayList<Territory> options = new ArrayList<Territory>();
    Territory t1 = new Territory("A", 20);
    Territory t2 = new Territory("B", 10);
    Territory t3 = new Territory("C", 5);
    t1.addNeighbors(t2);
    t2.addNeighbors(t3);
    options.add(t1);
    options.add(t2);
    options.add(t3);
    String expected_ = "(type B to go back)\n\n"
                       + "(0) A (next to: B) with 20 units\n"
                       + "(1) B (next to: A, C) with 10 units\n"
                       + "(2) C (next to: B) with 5 units\n";
    String expected1 =
        "Please choose one of your territories to start the action:\n" + expected_;
    String expected2 =
        "Please choose one of the below options as your destination:\n" + expected_;
    assertEquals(expected1, promot.chooseTerrPromot(options, true));
    assertEquals(expected2, promot.chooseTerrPromot(options, false));
    String expected3 = "Please choose one of your territories to start the action:\n"
                       + "(type B to go back)\n\n"
                       + "No option available, please go back (B) and choose again\n";
    assertEquals(expected3, promot.chooseTerrPromot(null, true));
    options.clear();
    assertEquals(expected3, promot.chooseTerrPromot(options, true));
  }
  @Test
  public void test_commitMessage() {
    Player p = new Player("A", 20);
    TextPromot promot = new TextPromot(p);
    String expected = "-------------------------------------------------------\n"
                      + "Actions received!\n"
                      + "Waiting for the server to process the actions...\n"
                      + "-------------------------------------------------------\n";
    assertEquals(expected, promot.commitMessage());
  }
}
