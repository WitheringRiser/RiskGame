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

  public ClientUpgrade(Socket s, Map m,Player ply) {
    this.socket = s;
    this.communicate = new Communicate();
    this.map = m;
    this.player = ply;
    this.actions = new ArrayList<UpgradeSender>();
  }
  
  public void perform_res(int unitIndex, int toLevel, Territory source) {
    String mes = this.player.upgradeUnit(source, unitIndex, toLevel);
    if(mes!=null){
      throw new IllegalArgumentException(mes);
    }
    
    // this.player.consumeTech(source.getUnits().get(unitIndex).getCost(toLevel));
    // source.getUnits().get(unitIndex).upgradeLevel(toLevel);
    this.actions.add(new UpgradeSender(source, unitIndex, toLevel));
  }

  public void commit() throws IOException {
    communicate.sendObject(socket, this.actions);
    this.actions.clear();
  }

}
