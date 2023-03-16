package edu.duke.ece651.teamX.shared;

public class ActionSender {
  Territory source;
  Territory destination;
  int unitsNum;

  public ActionSender(Territory _source, Territory _destination, int _unitsNum) {
    source = _source;
    destination = _destination;
    unitsNum = _unitsNum;
  }

  public Territory getSource() { return source; }

  public Territory getDestination() { return destination; }

  public int getUnitsNum() { return unitsNum; }
}
