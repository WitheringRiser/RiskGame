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
  private TextPrompt prompt;
  private Communicate communicate;
  protected Map map;
  protected Player player;
  protected ArrayList<T> actions;

  public ClientTurnAction(Socket s, PrintStream o, UserInReader uir, TextPrompt tp, Map m,
      Player ply) {
    this.socket = s;
    this.out = o;
    this.inputReader = uir;
    this.prompt = tp;
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
    out.print(prompt.chooseTerrPrompt(terrs, is_source));
    int choice = inputReader.enterNum(terrs.size() - 1, prompt.enterAgainPrompt());
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
   * get the index of units
   * 
   * @param source: source territory
   * @param maxNum: max number of units the user can select(food resource is
   *                limited)
   * @return: arraylist of index
   * @throws IOException
   */
  public ArrayList<Integer> chooseIndex(Territory source, int maxNum) throws IOException {
    int max = source.getUnitsNumber();
    ArrayList<Integer> indexes = inputReader.readIndexList(0, max - 1, maxNum);
    return indexes;
  }

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

    int unit_num = inputReader.enterNum(source.getUnitsNumber(),
        prompt.enterNumPrompt(),
        prompt.enterAgainPrompt());

    if (unit_num < 0) {
      return null;
    }

    // ArrayList<Integer> indexList = chooseIndex(source,
    // map.getOwner(source).getFoodResource());

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
