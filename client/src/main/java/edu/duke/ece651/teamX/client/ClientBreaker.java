package edu.duke.ece651.teamX.client;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import edu.duke.ece651.teamX.shared.*;

public class ClientBreaker {

  private Socket socket;
  private Communicate communicate;
  protected Map map;
  protected Player player;
  protected ArrayList<BreakerSender> actions;

  public ClientBreaker(Socket s, Map m, Player ply) {
    this.socket = s;
    this.communicate = new Communicate();
    this.map = m;
    this.player = ply;
    this.actions = new ArrayList<BreakerSender>();
  }

  public void perform_res(Territory source, int toLevel, int cost) {
    player.consumeGold(cost);
    source.addBreaker(toLevel);
    this.actions.add(new BreakerSender(source, toLevel));
  }

  /**
   * @param source
   * @param toLevel: shield level
   */
  public void perform(Territory source, int toLevel) {
    int cost = source.getBreakerCost(toLevel);

    if (cost > player.getGoldResource()) {
      throw new IllegalArgumentException("No enough gold to buy breaker: " + cost);
    }
    // Perform
    perform_res(source, toLevel, cost);

  }

  public void commit() throws IOException {
    communicate.sendObject(socket, this.actions);
    this.actions.clear();
  }

}
