package edu.duke.ece651.teamX.shared;

import java.util.ArrayList;

public class Player {

  private Map map;
  private String name;
  private ArrayList<Action> actions;

  public void init() {

  }

  public void setName(String name) {

  }

  public boolean chooseTerritory() {
    return true;
  }

  public boolean setUnits(Territory terr, Unit unit, int num) {
    return true;
  }

  public void receiveMap() {

  }

  public void addAction(Action action) {

  }

  public boolean sendActions() {
    return true;
  }

  public PlayError receiveError() {
    return null;
  }

  public void errorHandling(PlayError err) {

  }


}
