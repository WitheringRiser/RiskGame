package edu.duke.ece651.teamX.shared;

public class ResearchSender {
  private Territory source;

  public ResearchSender(Territory _source) {
    this.source = _source;
  }

  public Territory getSource() {
    return source;
  }
}
