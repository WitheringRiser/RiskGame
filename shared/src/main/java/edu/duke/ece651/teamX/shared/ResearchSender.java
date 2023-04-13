package edu.duke.ece651.teamX.shared;

import java.io.Serializable;

public class ResearchSender implements Serializable {

  private Player player;

  public ResearchSender(Player _player) {
    this.player = _player;
  }

  public Player getPlayer() {
    return player;
  }
}
