package edu.duke.ece651.teamX.shared;

import java.io.Serializable;

public class ResearchSender implements Serializable {

  private Player player;
  private boolean unlockCloak;

  public ResearchSender(Player _player) {
    this.player = _player;
    unlockCloak = false;
  }

  public ResearchSender(Player _player, boolean unlock) {
    this.player = _player;
    unlockCloak = unlock;
  }

  public Player getPlayer() {
    return player;
  }

  public boolean isUnlockCloak(){
    return unlockCloak;
  }
}
