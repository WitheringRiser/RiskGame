package edu.duke.ece651.teamX.client;

import static org.junit.jupiter.api.Assertions.*;

import edu.duke.ece651.teamX.shared.*;
import java.util.ArrayList;
import java.util.HashMap;
import org.junit.jupiter.api.Test;

public class TextPromptTest {

  private HashMap<Integer, ArrayList<Territory>> formGroup() {
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
    HashMap<Integer, ArrayList<Territory>> res =
        new HashMap<Integer, ArrayList<Territory>>();
    res.put(0, terr_list1);
    res.put(1, terr_list2);
    return res;
  }

  @Test
  public void test_Begin() {
    Player p1 = new Player("A", 20);
    TextPrompt prompt = new TextPrompt(p1);
    String expected =
        "Welcome to the RISK game, Player A!\n"
            + "Please wait a few moments while we gather all players before we start...\n";
    assertEquals(expected, prompt.startPrompt());
  }

  @Test
  public void test_displayGroup() {
    Player p1 = new Player("A", 20);
    TextPrompt prompt = new TextPrompt(p1);
    HashMap<Integer, ArrayList<Territory>> groups = formGroup();
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
    assertEquals(expected, prompt.displayTerrGroup(groups));
  }

  @Test
  public void test_setUnitPrompt() {
    Player p1 = new Player("A", 20);
    TextPrompt prompt = new TextPrompt(p1);
    HashMap<Integer, ArrayList<Territory>> groups = formGroup();
    String expected = "You have 20 available units to place.\n"
        + "Please choose a territory to place units:\n"
        + "(0) A (next to: B, C) with 0 units\n"
        + "(1) B (next to: A, D) with 0 units\n";
    assertEquals(expected, prompt.setUnitPrompt(groups.get(0), 20, false));
    expected =
        "A player: you are going to place the units in your territories.\n\n" + expected;
    assertEquals(expected, prompt.setUnitPrompt(groups.get(0), 20, true));

    expected = "Please indicate how many units you want to use.\n"
        + "(type B to go back)\n";
    assertEquals(expected, prompt.enterNumPrompt());
    expected = "The input is not a valid option, please choose again.\n";
    assertEquals(expected, prompt.enterAgainPrompt());
  }

  @Test
  public void test_choose_Prompt() {
    Player p1 = new Player("A", 100);
    TextPrompt prompt = new TextPrompt(p1);
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
    assertEquals(expected1, prompt.chooseTerrPrompt(options, true));
    assertEquals(expected2, prompt.chooseTerrPrompt(options, false));
    String expected3 = "Please choose one of your territories to start the action:\n"
        + "(type B to go back)\n\n"
        + "No option available, please go back (B) and choose again\n";
    assertEquals(expected3, prompt.chooseTerrPrompt(null, true));
    options.clear();
    assertEquals(expected3, prompt.chooseTerrPrompt(options, true));
  }

  @Test
  public void test_commitMessage() {
    Player p = new Player("A", 20);
    TextPrompt prompt = new TextPrompt(p);
    String expected = "-------------------------------------------------------\n"
        + "Actions received!\n"
        + "Waiting for the server to process the actions...\n"
        + "-------------------------------------------------------\n";
    assertEquals(expected, prompt.commitMessage());
  }

  @Test
  public void test_oneTurnPrompt() {
    Player p = new Player("A", 0);
    TextPrompt prompt = new TextPrompt(p);
    String expected = "You are the A player, what would you like to do?\n"
        + "   (M)ove\n"
        + "   (A)ttack\n"
        + "   (D)one\n";
    assertEquals(expected, prompt.oneTurnPrompt());
  }
}
