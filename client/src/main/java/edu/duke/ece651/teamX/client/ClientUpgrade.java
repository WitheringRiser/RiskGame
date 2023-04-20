package edu.duke.ece651.teamX.client;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;

import edu.duke.ece651.teamX.shared.Communicate;
import edu.duke.ece651.teamX.shared.Map;
import edu.duke.ece651.teamX.shared.Player;
import edu.duke.ece651.teamX.shared.Territory;
import edu.duke.ece651.teamX.shared.UpgradeSender;

public class ClientUpgrade {

  private Socket socket;
  private PrintStream out;
  private UserInReader inputReader;
  private TextPrompt prompt;
  private Communicate communicate;
  protected Map map;
  protected Player player;
  protected ArrayList<UpgradeSender> actions;

  public ClientUpgrade(Socket s, PrintStream o, UserInReader uir, TextPrompt tp, Map m,
      Player ply) {
    this.socket = s;
    this.out = o;
    this.inputReader = uir;
    this.prompt = tp;
    this.communicate = new Communicate();
    this.map = m;
    this.player = ply;
    this.actions = new ArrayList<UpgradeSender>();
  }

  /**
   * @param terrs     a list of territory to choose from
   * @param is_source whether it is to choose the source destination
   * @return a result territory
   * @throws IOException
   */
  private Territory chooseOneTerritory(ArrayList<Territory> terrs, boolean is_source)
      throws IOException {
    out.print(prompt.chooseTerrPrompt(terrs, is_source));
    int choice = inputReader.enterNum(terrs.size() - 1, prompt.enterAgainPrompt());
    if (choice < 0) {
      return null;
    }
    return terrs.get(choice);
  }

  public void perform() throws IOException {
    Territory source = chooseOneTerritory(this.map.getTerritories(this.player), true);
    if (source == null) {
      return;
    }
    int unitIndex = inputReader.enterNum(source.getUnitsNumber(),
        "Please input the index of the unit you want upgrade",
        "index invalid, please input again");
    int toLevel = inputReader.enterLevel(this.player.getTechLevel(), this.player.getTechResource(),
        source.getUnits().get(unitIndex));
    if (toLevel < 0) {
      return;
    }
    perform_res(unitIndex, toLevel, source);
  }

  public void perform_res(int unitIndex, int toLevel, Territory source) {
    this.player.consumeTech(source.getUnits().get(unitIndex).getCost(toLevel));
    source.getUnits().get(unitIndex).upgradeLevel(toLevel);
    this.actions.add(new UpgradeSender(source, unitIndex, toLevel));
  }

  public void commit() throws IOException {
    communicate.sendObject(socket, this.actions);
    this.actions.clear();
  }

}
