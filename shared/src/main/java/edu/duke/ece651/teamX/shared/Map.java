package edu.duke.ece651.teamX.shared;

import java.util.HashMap;

public class Map {

  private HashMap<Territory, Player> map_dict;

  public Map() {

  }

  public boolean addTerritory(Territory terr) {
    return true;
  }

  public boolean setOwner(Territory terr, Player player) {
    return true;
  }

  public Player getOwner(Territory terr) {
    return map_dict.get(terr);
  }

}
