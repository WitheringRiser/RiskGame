package edu.duke.ece651.teamX.client;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

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

  public void perform_res(String name, int num, int toLevel, Territory source) {
    ArrayList<Integer> indexList = source.getUnitsDit().get(name);
    if (indexList.size() < num) {
      throw new IllegalArgumentException("no enough units to upgrade");
    }
    for (int i = 0; i < num; ++i) {
      String mes = this.player.upgradeUnit(source, indexList.get(i), toLevel);
      if (mes != null) {
        throw new IllegalArgumentException(mes);
      }
    }

    // this.player.consumeTech(source.getUnits().get(unitIndex).getCost(toLevel));
    // source.getUnits().get(unitIndex).upgradeLevel(toLevel);
    this.actions.add(new UpgradeSender(source, name, num, toLevel));
  }

  public void commit() throws IOException {
    communicate.sendObject(socket, this.actions);
    this.actions.clear();
  }

}
