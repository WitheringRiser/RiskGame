package edu.duke.ece651.teamX.shared;

public class UpgradeSender {
  private Territory source;
  private int index;
  private int toLevel;

  public UpgradeSender(Territory _source, int _index, int _toLevel) {
    this.source = _source;
    this.index = _index;
    this.toLevel = _toLevel;
  }

  public Territory getSource() {
    return source;
  }

  public int getIndex() {
    return index;
  }

  public int getToLevel() {
    return toLevel;
  }
}
