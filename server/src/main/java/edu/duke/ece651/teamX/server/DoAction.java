package edu.duke.ece651.teamX.server;

import edu.duke.ece651.teamX.shared.*;
import java.util.*;
import java.io.*;

public class DoAction {

  private HashMap<Integer, ArrayList<Territory>> myworld;
  private String tempWorldStr;
  private HashMap<Integer, ArrayList<ActionSender>> myActionMap;
  private String tempActionMapStr;
  private HashSet<String> invalidPlayer;
  private MyFormatter myformatter;
  private ArrayList<ActionSender> mymoveList;
  private HashMap<Integer, Integer> myResource;
  private HashMap<Integer, Integer> rawResource;

  public DoAction(HashMap<Integer, ArrayList<Territory>> world,
      HashMap<Integer, ArrayList<ActionSender>> actionsMap, HashMap<Integer, Integer> resource) {
    init();
    myworld = world;
    myActionMap = actionsMap;
    myformatter = new MyFormatter(myworld.size());
    tempWorldStr = myformatter.MapCompose(myworld).toString();
    tempActionMapStr = myformatter.AllActionCompose(myActionMap).toString();
    myResource = resource;
  }

  public DoAction(HashMap<Integer, ArrayList<Territory>> world) {
    init();
    myworld = world;
    myformatter = new MyFormatter(myworld.size());
  }

  private void init() {
    myworld = new HashMap<>();
    myActionMap = new HashMap<>();
    invalidPlayer = new HashSet<>();
    mymoveList = new ArrayList<>();
    myResource = new HashMap<>();
    rawResource = new HashMap<>();
  }


  public void doMoveAction(ArrayList<ActionSender> moveList) {
    System.out.println("[DEBUG] moveList.size() is " + moveList.size());
    for (int k = 0; k < moveList.size(); k++) {
      System.out.println("[DEBUG] action's src is " + moveList.get(k).getSource().getName());
      System.out.println("[DEBUG] action's dst is " + moveList.get(k).getDestination().getName());
    }
    if (moveList.size() == 0) {
      return;
    }

    for (int i = 0; i < moveList.size(); i++) {
      ActionSender action = moveList.get(i);

      // check if action valid
      ServerChecker mychecker = new ServerChecker(myworld);

      moveHelper(action);
    }
    mymoveList = moveList;
  }

  // Do moveAction once
  private void moveHelper(ActionSender action) {
    System.out.println("[DEBUG] move action valid");
    HashMap<Integer, Integer> movedSoldierMap = null;
    for (HashMap.Entry<Integer, Integer> entry : movedSoldierMap.entrySet()) {
      int soldierLevel = entry.getKey();
      int numReduce = entry.getValue();
      if (numReduce == 0) {
        continue;
      }

      Territory srcTerritory = findTerritory(myworld, action.getSource().getName());
      Territory dstTerritory = findTerritory(myworld, action.getDestination().getName());
      System.out.println("[DEBUG] " + srcTerritory.getName() + " move " + numReduce
          + "level_" + soldierLevel + " soldier to " + dstTerritory.getName());


    }
  }


  // find a specific territory in worldmap
  public Territory findTerritory(
      HashMap<Integer, ArrayList<Territory>> myworld, String TerritoryName) {
    Territory ans = new Territory(null);
    for (HashMap.Entry<Integer, ArrayList<Territory>> entry : myworld.entrySet()) {
      ArrayList<Territory> territoryList = entry.getValue();
      for (int j = 0; j < territoryList.size(); j++) {
        Territory myterritory = territoryList.get(j);
        if (myterritory.getName().equals(TerritoryName)) {
          ans = myterritory;
          return ans;
        }
      }
    }
    return ans;
  }

  //return worldMap with territory details diff
  public HashMap<Integer, ArrayList<Territory>> getNewWorld() {
    return this.myworld;
  }


}