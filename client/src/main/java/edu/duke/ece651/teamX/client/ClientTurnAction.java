package edu.duke.ece651.teamX.client;

import java.io.IOException;
import java.util.ArrayList;
import edu.duke.ece651.teamX.shared.*;
import java.net.Socket;

public abstract class ClientTurnAction<T extends ActionSender> implements ClientAction {

  private Socket socket;
  private Communicate communicate;
  protected Map map;
  protected Player player;
  protected ArrayList<T> actions;

  public ClientTurnAction(Socket s, Map m, Player ply) {
    this.socket = s;
    this.communicate = new Communicate();
    this.map = m;
    this.player = ply;
    this.actions = new ArrayList<T>();
  }

  /**
   * Get all territories belong to this player
   *
   * @return a list of territories that belongs to this player
   */
  public ArrayList<Territory> findOwnTerr() {
    return this.map.getTerritories(this.player);
  }

  /**
   * A function to find destination territorys
   *
   * @param source
   * @return
   */
  public abstract ArrayList<Territory> findDestTerrs(Territory source);

  public void commit() throws IOException {
    communicate.sendObject(socket, this.actions);
    this.actions.clear();
  }
}
