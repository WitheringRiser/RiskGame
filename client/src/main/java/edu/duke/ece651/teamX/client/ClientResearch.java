package edu.duke.ece651.teamX.client;

import java.io.IOException;
import java.net.Socket;

import edu.duke.ece651.teamX.shared.CloakProcessor;
import edu.duke.ece651.teamX.shared.Communicate;
import edu.duke.ece651.teamX.shared.Player;
import edu.duke.ece651.teamX.shared.ResearchSender;

public class ClientResearch {

  private Socket socket;
  private Player player;
  private Communicate communicate;
  private ResearchSender sender;

  public ClientResearch(Socket socket, Player _player) {
    this.player = _player;
    this.sender = null;
    this.socket = socket;
    // isUnlockCloak=false;
  }

  // public ClientResearch(Socket socket, Player _player,boolean unlock) {
  //   this.player = _player;
  //   this.sender = null;
  //   this.socket = socket;
  //   // this.isUnlockCloak=unlock;
  // }

  public void perform(boolean isUnlockCloak) {

    if (sender != null) {
      throw new IllegalArgumentException("You can not do research any more!");
    } else {
      int cost;
      if (isUnlockCloak) {
        if (player.getTechLevel() < 3) {
          throw new IllegalArgumentException("You cannot unlock cloak now!");
        }
        cost = CloakProcessor.getUnlockCloakCost();
      } else {
        cost = player.getResearchNeedCost();
      }
      if (cost > player.getTechResource()) {
        throw new IllegalArgumentException("You don not have enough resource for upgrade!");
      }
      player.consumeTech(cost);
      sender = new ResearchSender(player, isUnlockCloak);
    }
  }

  public void commit() throws IOException {
    communicate.sendObject(socket, this.sender);
  }
}
