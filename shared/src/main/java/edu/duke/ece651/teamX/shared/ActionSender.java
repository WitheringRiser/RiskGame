package edu.duke.ece651.teamX.shared;

import java.io.Serializable;
import java.util.ArrayList;

public class ActionSender implements Serializable {
  protected Territory source;
  protected Territory destination;
  protected int unitsNum;
  protected String name;

  //Constructor for Testing
  public ActionSender(Territory _source, Territory _destination, int _unitsNum) {
    source = _source;
    destination = _destination;
    unitsNum = _unitsNum;
    name = source.getUnits().get(0).getName();
  }

  //Constructor for real use
  public ActionSender(Territory _source,
                      Territory _destination,
                      int _unitsNum,
                      String name) {
    //this(_source, _destination, _unitsNum);
    source = _source;
    destination = _destination;
    unitsNum = _unitsNum;

    this.name = name;
  }

  public Territory getSource() { return source; }

  public Territory getDestination() { return destination; }

  public int getUnitsNum() { return unitsNum; }

  public String getName() { return name; }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o != null && o.getClass().equals(getClass())) {
      ActionSender otherSender = (ActionSender)o;
      return source.equals(otherSender.getSource()) &&
          destination.equals(otherSender.getDestination()) &&
          unitsNum == otherSender.getUnitsNum() && name == otherSender.getName();
    }
    return false;
  }

  @Override
  public int hashCode() {
    String s = source.getName() + ", " + destination.getName() + ", " +
               Integer.toString(unitsNum) + ", " + name;
    return s.hashCode();
  }
}
