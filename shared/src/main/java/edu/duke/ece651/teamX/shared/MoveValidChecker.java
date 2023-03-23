package edu.duke.ece651.teamX.shared;

import java.util.ArrayList;

public class MoveValidChecker {

  private ArrayList<MoveSender> allMove;
  private Map map;

  MoveValidChecker(ArrayList<MoveSender> _allMove, Map _map) {
    allMove = _allMove;
    map = _map;
  }

  public void checkValid() throws IllegalArgumentException {
    for (MoveSender m : allMove) {
      Territory source = map.getTerritoryByName(m.getSource().getName());
      Territory destination = map.getTerritoryByName(m.getDestination().getName());
      int num = m.getUnitsNum();
      if (source.getUnitsNumber() < num) {
        throw new IllegalArgumentException("Not enough units in source territory");
      }
      if (!map.getOwner(source).equals(map.getOwner(destination))) {
        throw new IllegalArgumentException(
            "Source and destination are not owned by the same player");
      }
    }
  }

}
