package edu.duke.ece651.teamX.client;

import java.io.IOException;
import java.util.ArrayList;
import edu.duke.ece651.teamX.shared.*;
import java.net.Socket;
import java.io.PrintStream;

public abstract class ClientTurnAction<T extends ActionSender> implements ClientAction {

  private Socket socket;
  private PrintStream out;
  private UserInReader inputReader;
  private TextPromot promot;
  private Communicate communicate;
  protected Map map;
  protected Player player;
  protected ArrayList<T> actions;

  public ClientTurnAction(Socket s, PrintStream o, UserInReader uir, TextPromot tp, Map m,
      Player ply) {
    this.socket = s;
    this.out = o;
    this.inputReader = uir;
    this.promot = tp;
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
   * @param terrs     a list of territory to choose from
   * @param is_source whether it is to choose the source destination
   * @return a result territory
   * @throws IOException
   */
  public Territory chooseOneTerritory(ArrayList<Territory> terrs, boolean is_source)
      throws IOException {
    out.print(promot.chooseTerrPromot(terrs, is_source));
    int choice = inputReader.enterNum(terrs.size() - 1, promot.enterAgainPromot());
    if (choice < 0) {
      return null;
    }
    return terrs.get(choice);
  }

  /**
   * A function to find destination territorys
   *
   * @param source
   * @return
   */
  public abstract ArrayList<Territory> findDestTerrs(Territory source);

  /**
   * @return an ActionSender object with action information
   * @throws IOException
   */
  public ActionSender generateAction()
      throws IOException {
    Territory source = chooseOneTerritory(findOwnTerr(), true);
    if (source == null) {
      return null;
    }
    Territory dest = chooseOneTerritory(findDestTerrs(source), false);
    if (dest == null) {
      return null;
    }
    int unit_num = inputReader.enterNum(source.getUnitsNumber(), promot.enterNumPromot(),
        promot.enterAgainPromot());

    if (unit_num < 0) {
      return null;
    }

    return new ActionSender(source, dest, unit_num);
  }

  // public void perform()throws IOException, ClassNotFoundException{

  // }
  public void commit() throws IOException {
    communicate.sendObject(socket, this.actions);
    this.actions.clear();
  }
  // }
}