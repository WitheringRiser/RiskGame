package edu.duke.ece651.teamX.shared;

import java.io.Serializable;

public class ActionSender implements Serializable {

  protected Territory source;
  protected Territory destination;
  protected int unitsNum;

  public ActionSender(Territory _source, Territory _destination, int _unitsNum) {
    source = _source;
    destination = _destination;
    unitsNum = _unitsNum;
  }


  public Territory getSource() {
    return source;
  }

  public Territory getDestination() {
    return destination;
  }

  public int getUnitsNum() {
    return unitsNum;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o != null && o.getClass().equals(getClass())) {
      ActionSender otherSender = (ActionSender) o;
      return source.equals(otherSender.getSource()) &&
          destination.equals(otherSender.getDestination()) &&
          unitsNum == otherSender.getUnitsNum();
    }
    return false;
  }

  @Override
  public int hashCode() {
    String s = source.getName() + ", " + destination.getName() + ", " +
        Integer.toString(unitsNum);
    return s.hashCode();
  }
}
