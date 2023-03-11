package edu.duke.ece651.teamX.shared;

import java.util.ArrayList;

public class Client {
  private Map map;
  private Player player;
  private ArrayList<Action> actions;

  public Client() { actions = new ArrayList<Action>(); }
  public void init() {}

  public boolean chooseTerritory() { return true; }

  public boolean setUnits(Territory terr, Unit unit, int num) { return true; }

  public void receiveMap() {}

  public void receivePlayer() {}

  public void addAction(Action action) {}

  public boolean sendActions() { return true; }

  public PlayError receiveError() { return null; }

  public void errorHandling(PlayError err) {}
}
