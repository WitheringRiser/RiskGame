package edu.duke.ece651.teamX.client;

import java.net.Socket;

import edu.duke.ece651.teamX.shared.Communicate;
import edu.duke.ece651.teamX.shared.Player;
import edu.duke.ece651.teamX.shared.ResearchSender;

public class ClientResearch {
  private Socket socket;
  private Player player;
  private Communicate communicate;

  public ClientResearch(Player _player) {
    this.player = _player;
  }

  public ResearchSender perform() {
    int cost = player.getResearchNeedCost();
    if (cost > player.getTechResource()) {
      return null;
    } else {
      player.consumeTech(cost);
      return new ResearchSender(player);
    }
  }
}
