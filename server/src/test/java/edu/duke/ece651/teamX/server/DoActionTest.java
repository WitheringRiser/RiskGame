package edu.duke.ece651.teamX.server;

import edu.duke.ece651.teamX.shared.*;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.*;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

public class DoActionTest {
  /*
  private HashMap<Integer, Integer> resource;
  private HashMap<Integer, ArrayList<Territory>> myworld;
  private Territory territoryA;
  private Territory territoryB;
  private Territory territoryC;
  private Territory territoryD;
  private Territory territoryE;
  private Territory territoryF;

  public DoActionTest() {
    resource = new HashMap<>();
    resource.put(0, 100);
    resource.put(1, 100);
    resource.put(2, 100);
    resource.put(3, 100);

    myworld = new HashMap<>();
    StringBuilder fileName = new StringBuilder();
    fileName.append("/old/world2.json");
    InputStream input = getClass().getResourceAsStream(fileName.toString());

    Scanner scanner = new Scanner(input);
    StringBuilder worldInfo = new StringBuilder();
    while (scanner.hasNext()) {
      worldInfo.append(scanner.next());
    }
    MyFormatter tempformatter = new MyFormatter(2);
    tempformatter.MapParse(myworld, worldInfo.toString());

    MyFormatter formatter = new MyFormatter(2);
    String Astr =
        "{'owner':'player_0', 'soldiers':[{'level_0':'3'},{'level_1':'0'},{'level_2':'0'},{'level_3':'0'},{'level_4':'0'},{'level_5':'0'},{'level_6':'0'}], 'neighbor':[{'neighbor_0':'B'},{'neighbor_1':'D'},{'neighbor_2':'E'}], 'territoryName':'A'}";
    territoryA = new Territory(null);
    JSONObject tempA = new JSONObject(Astr);
    territoryA = formatter.JsonToTerritory(tempA);

    String Bstr =
        "{'owner':'player_1', 'soldiers':[{'level_0':'3'},{'level_1':'0'},{'level_2':'0'},{'level_3':'0'},{'level_4':'0'},{'level_5':'0'},{'level_6':'0'}], 'neighbor':[{'neighbor_0':'A'},{'neighbor_1':'C'},{'neighbor_2':'E'}], 'territoryName':'B'}";
    territoryB = new Territory(null);
    JSONObject tempB = new JSONObject(Bstr);
    territoryB = formatter.JsonToTerritory(tempB);

    String Cstr =
        "{'owner':'player_1', 'soldiers':[{'level_0':'3'},{'level_1':'0'},{'level_2':'0'},{'level_3':'0'},{'level_4':'0'},{'level_5':'0'},{'level_6':'0'}], 'neighbor':[{'neighbor_0':'B'},{'neighbor_1':'E'},{'neighbor_2':'F'},{'neighbor_3':'G'}], 'territoryName':'C'}";
    territoryC = new Territory(null);
    JSONObject tempC = new JSONObject(Cstr);
    territoryC = formatter.JsonToTerritory(tempC);

    String Dstr =
        "{'owner':'player_0', 'soldiers':[{'level_0':'3'},{'level_1':'0'},{'level_2':'0'},{'level_3':'0'},{'level_4':'0'},{'level_5':'0'},{'level_6':'0'}], 'neighbor':[{'neighbor_0':'A'},{'neighbor_1':'E'},{'neighbor_2':'H'}], 'territoryName':'D'}";
    territoryD = new Territory(null);
    JSONObject tempD = new JSONObject(Dstr);
    territoryD = formatter.JsonToTerritory(tempD);

    String Estr =
        "{'owner':'player_0', 'soldiers':[{'level_0':'3'},{'level_1':'0'},{'level_2':'0'},{'level_3':'0'},{'level_4':'0'},{'level_5':'0'},{'level_6':'0'}], 'neighbor':[{'neighbor_0':'A'},{'neighbor_1':'B'},{'neighbor_2':'C'},{'neighbor_3':'D'},{'neighbor_4':'F'},{'neighbor_5':'H'},{'neighbor_6':'J'}], 'territoryName':'E'}";
    territoryE = new Territory(null);
    JSONObject tempE = new JSONObject(Estr);
    territoryE = formatter.JsonToTerritory(tempE);

    String Fstr =
        "{'owner':'player_1', 'soldiers':[{'level_0':'3'},{'level_1':'0'},{'level_2':'0'},{'level_3':'0'},{'level_4':'0'},{'level_5':'0'},{'level_6':'0'}], 'neighbor':[{'neighbor_0':'C'},{'neighbor_1':'E'},{'neighbor_2':'G'},{'neighbor_3':'K'}], 'territoryName':'F'}";
    territoryF = new Territory(null);
    JSONObject tempF = new JSONObject(Fstr);
    territoryF = formatter.JsonToTerritory(tempF);
  }

 
  @Test
  public void test_moveattack() {
    ArrayList<ActionSender> actionList = new ArrayList<>();

    ActionSender action = new ActionSender(territoryA, territoryE, 3);
    actionList.add(action); // invalid action

    ActionSender action2 = new ActionSender(territoryD, territoryE, 3);
    actionList.add(action2); // invalid action

    ActionSender action3 = new ActionSender(territoryE, territoryF, 3);
    actionList.add(action3); // invalid action

    HashMap<Integer, ArrayList<ActionSender>> myactionMap = new HashMap<>();
    myactionMap.put(0, actionList);

    ArrayList<ActionSender> moveList = new ArrayList<>();
    moveList.add(action);
    moveList.add(action2);

    ArrayList<ActionSender> attackList = new ArrayList<>();
    attackList.add(action3);

    HashMap<Integer, Integer> resource = new HashMap<>();
    resource.put(0, 100);
    resource.put(1, 100);
    resource.put(2, 100);
    resource.put(3, 100);
    DoAction actor = new DoAction(myworld, myactionMap, resource);
    actor.doMoveAction(moveList);
    myworld = actor.getNewWorld();

  }

  @Test
  public void test_invalidMoveAction() {
    ArrayList<ActionSender> actionList = new ArrayList<>();

    ActionSender action2 = new ActionSender(territoryA, territoryD, 3);
    actionList.add(action2); // valid action, but is deleted

    ActionSender action = new ActionSender(territoryA, territoryB, 3);
    actionList.add(action); // invalid action

    ActionSender action3 = new ActionSender(territoryB, territoryC, 3);
    ArrayList<ActionSender> actionList2 = new ArrayList<>();
    actionList2.add(action3); // valid action

    HashMap<Integer, ArrayList<ActionSender>> myactionMap = new HashMap<>();
    myactionMap.put(0, actionList);
    myactionMap.put(1, actionList2);

    DoAction actor = new DoAction(myworld, myactionMap, resource);
    ArrayList<ActionSender> allActionList = new ArrayList<>();
    allActionList.add(action2);
    allActionList.add(action);
    allActionList.add(action3);

    actor.doMoveAction(allActionList);
    myworld = actor.getNewWorld();
    assertEquals(myactionMap.containsKey(0), false);
    MapToJson myMaptoJson = new MapToJson(myworld);
    System.out.println("[DEBUG] outside doactionclass, new world is:" + myMaptoJson.getJSON());
    MyFormatter formatter2 = new MyFormatter(2);
    System.out.println("[DEBUG] outside doactionclass, new actionmap is:"
        + formatter2.AllActionCompose(myactionMap));
  }

  

  @Test
  public void test_DoMoveAction() {

    ActionSender action = new ActionSender(territoryA, territoryD, 3);

    ArrayList<ActionSender> actionList = new ArrayList<>();
    actionList.add(action);
    HashMap<Integer, ArrayList<ActionSender>> myactionMap = new HashMap<>();
    myactionMap.put(0, actionList);

    DoAction actor = new DoAction(myworld, myactionMap, resource);
    actor.doMoveAction(actionList);
    myworld = actor.getNewWorld();

    MyFormatter formatter2 = new MyFormatter(2);
    System.out.println("[DEBUG] outside doactionclass, new actionmap is:"
        + formatter2.AllActionCompose(myactionMap));

    ActionSender action2 = new ActionSender(territoryB, territoryC, 3);
    ArrayList<ActionSender> actionList2 = new ArrayList<>();
    actionList2.add(action2);
    HashMap<Integer, ArrayList<ActionSender>> myactionMap2 = new HashMap<>();
    myactionMap2.put(1, actionList2);

    DoAction actor2 = new DoAction(myworld, myactionMap2, resource);
    actor2.doMoveAction(actionList2);
    myworld = actor2.getNewWorld();

    MyFormatter formatter3 = new MyFormatter(2);
    System.out.println("[DEBUG] outside doactionclass, new actionmap is:"
        + formatter3.AllActionCompose(myactionMap));
  }

  */
}
