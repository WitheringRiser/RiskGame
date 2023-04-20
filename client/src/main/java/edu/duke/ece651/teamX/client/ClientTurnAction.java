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

  /**
   * get the index of units
   * 
   * @param source: source territory
   * @param maxNum: max number of units the user can select(food resource is
   *                limited)
   * @return: arraylist of index
   * @throws IOException
   */
  // public ArrayList<Integer> chooseIndex(Territory source, int maxNum) throws IOException {
  //   int max = source.getUnitsNumber();
  //   ArrayList<Integer> indexes = inputReader.readIndexList(0, max - 1, maxNum);
  //   return indexes;
  // }

  /**
   * @return an ActionSender object with action information
   * @throws IOException
   */
  // public ActionSender generateAction()
  //     throws IOException {
  //   Territory source = chooseOneTerritory(findOwnTerr(), true);
  //   if (source == null) {
  //     return null;
  //   }
  //   Territory dest = chooseOneTerritory(findDestTerrs(source), false);
  //   if (dest == null) {
  //     return null;
  //   }
    /*
     * int unit_num = inputReader.enterNum(source.getUnitsNumber(),
     * prompt.enterNumPrompt(),
     * prompt.enterAgainPrompt());
     * 
     * if (unit_num < 0) {
     * return null;
     * }
     */
  //   int distance = source.getTerritorySize() + dest.getTerritorySize();
  //   int canChooseMax = map.getOwner(source).getFoodResource() / distance;
  //   ArrayList<Integer> indexList = chooseIndex(source, canChooseMax);

  //   return new ActionSender(source, dest, indexList);
  // }

  // public void perform()throws IOException, ClassNotFoundException{

  // }
  public void commit() throws IOException {
    communicate.sendObject(socket, this.actions);
    this.actions.clear();
  }
}
