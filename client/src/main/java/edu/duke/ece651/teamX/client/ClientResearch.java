package edu.duke.ece651.teamX.client;

import java.io.IOException;
import java.net.Socket;

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
  }

  public void perform() {
    int cost = player.getResearchNeedCost();
    if (cost > player.getTechResource() || sender != null) {
      System.out.println("you can not do research any more!");
    } else {
      player.consumeTech(cost);
      sender = new ResearchSender(player);
    }
  }

  public void commit() throws IOException {
    communicate.sendObject(socket, this.sender);
  }
}
