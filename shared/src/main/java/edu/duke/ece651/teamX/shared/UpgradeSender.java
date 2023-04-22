package edu.duke.ece651.teamX.shared;

import java.io.Serializable;

public class UpgradeSender implements Serializable {

  private Territory source;
  private String name;
  private int num;
  private int toLevel;

  public UpgradeSender(Territory _source, String _name, int _num, int _toLevel) {
    this.source = _source;
    this.name = _name;
    this.num = _num;
    this.toLevel = _toLevel;
  }

  public Territory getSource() {
    return source;
  }

  public String getName() {
    return name;
  }

  public int getNum() {
    return num;
  }

  public int getToLevel() {
    return toLevel;
  }
}
