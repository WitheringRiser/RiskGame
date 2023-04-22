package edu.duke.ece651.teamX.client;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import edu.duke.ece651.teamX.shared.Communicate;
import edu.duke.ece651.teamX.shared.Map;
import edu.duke.ece651.teamX.shared.Player;
import edu.duke.ece651.teamX.shared.Territory;
import edu.duke.ece651.teamX.shared.UpgradeSender;

public class ClientUpgrade {

  private Socket socket;
  private Communicate communicate;
  protected Map map;
  protected Player player;
  protected ArrayList<UpgradeSender> actions;

  public ClientUpgrade(Socket s, Map m, Player ply) {
    this.socket = s;
    this.communicate = new Communicate();
    this.map = m;
    this.player = ply;
    this.actions = new ArrayList<UpgradeSender>();
  }

  /**
   * 
   * @param name    the type name of units
   * @param num     number of units to upgrade
   * @param toLevel to which level
   * @param source
   */
  public void perform_res(String name, int num, int toLevel, Territory source) {
    ArrayList<Integer> indexList = source.getUnitsDit().get(name);
    for (int i = 0; i < num; ++i) {
      String mes = this.player.upgradeUnit(source, indexList.get(i), toLevel);
      if (mes != null) {
        throw new IllegalArgumentException(mes);
      }
    }
    this.actions.add(new UpgradeSender(source, name, num, toLevel));
  }

  /**
   * 
   * @param source
   * @param upgradeDic <Name : <num, toLevel>>
   */
  public void perform(Territory source, HashMap<String, ArrayList<Integer>> upgradeDic) {
    int cost = 0;
    // Check if the resource is enough
    for (String typeName : upgradeDic.keySet()) {
      ArrayList<Integer> indexList = source.getUnitsDit().get(typeName);
      int num = upgradeDic.get(typeName).get(0);
      int toLevel = upgradeDic.get(typeName).get(1);
      if (num > 0) {
        if (indexList.size() < num) {
          throw new IllegalArgumentException("No enough " + typeName + " to upgrade");
        }
        if (toLevel > player.getTechLevel()) {
          throw new IllegalArgumentException(
              "Cannot upgrade to level " + toLevel + " as player is level " + player.getTechLevel());
        }
        cost += num * source.getUnits().get(indexList.get(0)).getCost(toLevel);
      }
    }
    if (cost > player.getTechResource()) {
      throw new IllegalArgumentException("No enough technology resource to upgrade! cost: " + cost);
    }
    // Perform
    for (String typeName : upgradeDic.keySet()) {
      int num = upgradeDic.get(typeName).get(0);
      int toLevel = upgradeDic.get(typeName).get(1);
      if (num > 0) {
        perform_res(typeName, num, toLevel, source);
      }
    }
  }

  public void commit() throws IOException {
    communicate.sendObject(socket, this.actions);
    this.actions.clear();
  }

}
