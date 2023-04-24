package edu.duke.ece651.teamX.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

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

  public abstract int calculateCost(Territory source, Territory dest, int num);

  public abstract void perform_res(Territory source, Territory dest, String name, int num);

  public void perform(Territory source, Territory dest, HashMap<String, Integer> unitSetting) {
    int totalCost = 0;
    for (String typeName : unitSetting.keySet()) {
      totalCost += calculateCost(source, dest, unitSetting.get(typeName));
    }
    if (totalCost > player.getFoodResource()) {
      throw new IllegalArgumentException(
          "The food resource is not enough for the total cost " + totalCost);
    }
    for (String typeName : unitSetting.keySet()) {
      int num = unitSetting.get(typeName);
      if (num > 0) {
        perform_res(source, dest, typeName, num);
      }
    }
  }

  public void commit() throws IOException {
    communicate.sendObject(socket, this.actions);
    this.actions.clear();
  }
}
