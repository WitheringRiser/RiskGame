package edu.duke.ece651.teamX.server;

import edu.duke.ece651.teamX.shared.*;
import java.util.*;
import java.io.*;

public class ActionHelper {

  private HashMap<Integer, ArrayList<ActionSender>> playersActions;
  private HashMap<Integer, ArrayList<Territory>> worldMap;
  private ArrayList<ActionSender> moveList;
  // ArrayList to record if the players committed the actions
  private ArrayList<Boolean> playerComplete;
  private String actionsStr;
  private int playerNum;

  public ActionHelper(int number, HashMap<Integer, ArrayList<Territory>> m) {
    reset(number, m);
  }

  public void reset() {
    reset(playerNum, worldMap);
  }

  public void reset(int number, HashMap<Integer, ArrayList<Territory>> m) {
    this.playersActions = new HashMap<>();
    this.worldMap = m;
    this.moveList = new ArrayList<>();
    this.playerNum = number;
    this.playerComplete = new ArrayList<>(playerNum);
    for (int i = 0; i < number; ++i) {
      playerComplete.add(false);
    }
  }

  public boolean checkActionValid(int id) {
    return playersActions.containsKey(id);
  }

  public synchronized void addActions(
      int playerId, ArrayList<ActionSender> ml, ArrayList<ActionSender> al) {
    ArrayList<ActionSender> curActions = new ArrayList<>();
    curActions.addAll(ml);
    curActions.addAll(al);
    playersActions.put(playerId, curActions);
    moveList.addAll(ml);
  }

  public synchronized void actionsCompleted(int playerId) {
    playerComplete.set(playerId, true);
  }

  public synchronized void executeActions(HashMap<Integer, Integer> food) {
    // Wait until all players have committed the actions
    while (playerComplete.contains(false)) {
      try {
        this.wait();
      } catch (InterruptedException e) {
        System.out.println("Error: wait failed in ActionHelper!");
      }
    }

    DoAction d = new DoAction(worldMap, playersActions, food);
    d.doMoveAction(moveList);
    worldMap = d.getNewWorld();
    MyFormatter formatter = new MyFormatter(playerNum);
    actionsStr = formatter.AllActionCompose(playersActions).toString();

  }

  public String getActionString() {
    return actionsStr;
  }
}