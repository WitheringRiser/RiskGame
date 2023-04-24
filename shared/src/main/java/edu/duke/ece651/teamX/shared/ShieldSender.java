package edu.duke.ece651.teamX.shared;

import java.io.Serializable;

public class ShieldSender implements Serializable {
  private Territory source;
  private int toLevel;

  public ShieldSender(Territory _source, int toLevel) {
    this.source = _source;
    this.toLevel = toLevel;
  }

  public Territory getSource() {
    return source;
  }

  public int getLevel() {
    return toLevel;
  }
}
