package edu.duke.ece651.teamX.shared;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.*;

public class ActionToJsonTest {
  @Test
  public void test_ActiontoJson() {
    ArrayList<ActionSender> actionList = new ArrayList<>();
    Territory territory_A = new Territory("A");
    territory_A.addNeighbors(new Territory("B"));
    territory_A.addNeighbors(new Territory("D"));


    ActionSender action_A = new ActionSender(territory_A, territory_A, 3);


    ActionToJson myActiontoJson = new ActionToJson(actionList, "Move");
    System.out.println(myActiontoJson.getJSON());

    MyFormatter myformatter = new MyFormatter(0);

  }
}