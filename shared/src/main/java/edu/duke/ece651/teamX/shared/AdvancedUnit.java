package edu.duke.ece651.teamX.shared;

public class AdvancedUnit extends BasicUnit {

  protected String owner;

  public AdvancedUnit(String player_name) {
    this.owner = player_name;
  }

  public String getPlayerName() {
    return owner;
  }

}
