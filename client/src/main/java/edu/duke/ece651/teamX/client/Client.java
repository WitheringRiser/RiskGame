package edu.duke.ece651.teamX.client;

import edu.duke.ece651.teamX.shared.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.Function;

public class Client {
  private Socket socket;
  private Communicate communicate;
  private Player player;      // init
  private TextPromot promot;  // init
  private Map map;
  private ArrayList<AttackSender> attacks;
  private ArrayList<MoveSender> moves;
  private PrintStream out;
  private UserInReader inputReader;
  // private BufferedReader inputReader;

  private void sendMove() throws IOException {
    communicate.sendObject(socket, this.moves);
    this.moves.clear();
  }
  private void sendAttact() throws IOException {
    communicate.sendObject(socket, this.attacks);
  }

  /**
   * Construct a Client object
   * 
   * @param s is socket to communicate with server
   */
  public Client(Socket s, BufferedReader input, PrintStream out) {
    socket = s;
    communicate = new Communicate();
    map = new Map();
    attacks = new ArrayList<AttackSender>();
    moves = new ArrayList<MoveSender>();
    this.out = out;
    this.inputReader = new UserInReader(input,out);
  }

  /**
   * Initialze the game settings for client
   * Receive player to get player properties (name, num of units)
   * Choose a territory group to sart
   * Set Units
   */
  public void init() throws IOException, ClassNotFoundException {
    player = communicate.receivePlayer(socket);
    promot = new TextPromot(player);
    out.print(promot.startPromot());
    ArrayList<Territory> territories = chooseTerrGroup();

    setAllUnits(territories);  // place units in territories
  }

  /**
   * Select a group to start
   * @return ArrayList<Territory> of the chosen territoy group
   * @throws IOException
   * @throws ClassNotFoundException
   */
  public ArrayList<Territory> chooseTerrGroup() throws IOException, ClassNotFoundException{
    ClientGroupSetting grouSetting = new ClientGroupSetting(socket, out, inputReader, promot);
    grouSetting.perform();
    grouSetting.commit();
    return grouSetting.getGroup();
  }

  /**
   * Place all available units to the territories
   * @param territories is the group from chooseTerrGroup function
   * @throws IOException
   */
  public void setAllUnits(ArrayList<Territory> territories) throws IOException{
    ClientUnitSetting unitSetting = new ClientUnitSetting(socket, out, inputReader, promot,territories,player.getUnitNum());
    unitSetting.perform();
    unitSetting.commit();
  }

  /**
   * Receive map from server
   */
  public void receiveMap() throws IOException, ClassNotFoundException {
    map = communicate.receiveMap(socket);
  }

  /**
   * Display Map received from server
   */
  public void displayMap() throws IOException {
    TextDisplayer dis = new TextDisplayer(map);
    out.print(dis.display());
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
   * Find all neighbors of a territory that are not belongs to this
   * player
   * 
   * @param source is the source territory to start attack
   * @return is the list of direct neighbor territories of other players
   */
  public ArrayList<Territory> findAttackTerr(Territory source) {
    if (!map.getOwner(source).equals(this.player)) {
      throw new IllegalArgumentException("The territory does not belong to this player");
    }
    Iterator<Territory> iter = source.getNeighbours();
    ArrayList<Territory> dests = new ArrayList<Territory>();
    while (iter.hasNext()) {
      Territory curr = iter.next();
      if (!map.getOwner(curr).equals(this.player)) {
        dests.add(curr);
      }
    }
    return dests;
  }

  public ArrayList<Territory> findMoveTerr(Territory source) {
    if (!map.getOwner(source).equals(this.player)) {
      throw new IllegalArgumentException("The territory does not belong to this player");
    }
    ArrayList<Territory> dests = new ArrayList<Territory>();
    ArrayList<Territory> visited = new ArrayList<Territory>();
    ArrayList<Territory> todo = new ArrayList<Territory>();
    todo.add(source);
    while (!todo.isEmpty()) {
      Territory curr = todo.get(0);
      todo.remove(0);
      if (!visited.contains(curr)) {
        Iterator<Territory> iter = curr.getNeighbours();
        while (iter.hasNext()) {
          Territory t = iter.next();
          if (map.getOwner(t).equals(this.player)) {
            if (!dests.contains(t) && (!t.equals(source))) {
              dests.add(t);
            }
            todo.add(t);
          }
        }
        visited.add(curr);
      }
    }
    return dests;
  }

  public Territory chooseOneTerritory(ArrayList<Territory> terrs, boolean is_source)
      throws IOException {
    out.print(promot.chooseTerrPromot(terrs, is_source));
    // int choice = enterUnitNum(terrs.size() - 1);
    int choice=  inputReader.enterNum(terrs.size() - 1, promot.enterAgainPromot());
    if (choice < 0) {
      return null;
    }
    return terrs.get(choice);
  }

  public ActionSender generateAction(Function<Territory, ArrayList<Territory> > findFunc)
      throws IOException {
    Territory source = chooseOneTerritory(findOwnTerr(), true);
    if (source == null) {
      return null;
    }
    Territory dest = chooseOneTerritory(findFunc.apply(source), false);
    if (dest == null) {
      return null;
    }
  
    int unit_num = inputReader.enterNum(source.getUnitsNumber(), promot.enterNumPromot(), promot.enterAgainPromot());

    if (unit_num < 0) {
      return null;
    }
    return new ActionSender(source, dest, unit_num);
  }

  public void performAttack() throws IOException {
    Function<Territory, ArrayList<Territory> > findfunc = (t) -> findAttackTerr(t);
    ActionSender res = generateAction(findfunc);
    if (res != null) {
      AttackSender atts =
          new AttackSender(res.getSource(), res.getDestination(), res.getUnitsNum());
      this.attacks.add(atts);
      res.getSource().removeUnits(res.getUnitsNum());
    }
  }

  public void performMove() throws IOException {
    Function<Territory, ArrayList<Territory> > findfunc = (t) -> findMoveTerr(t);
    ActionSender res = generateAction(findfunc);
    if (res != null) {
      MoveSender mvs =
          new MoveSender(res.getSource(), res.getDestination(), res.getUnitsNum());
      this.moves.add(mvs);
      res.getSource().removeUnits(res.getUnitsNum());
      res.getDestination().addUnits(null, res.getUnitsNum());
    }
  }
  public void performCommit() throws IOException {
    sendMove();
    sendAttact();
    out.print(promot.commitMessage());
  }

  public void playeOneTurn() throws IOException {
    while (true) {
      displayMap();
      out.print(promot.oneTurnPromot());
      String user_in = inputReader.readString();
      user_in = user_in.toUpperCase();
      if (user_in.equals("M")) {
        performMove();
      }
      else if (user_in.equals("A")) {
        performAttack();
      }
      else if (user_in.equals("D")) {
        performCommit();
        return;
      }
      else {
        out.print(promot.enterNumPromot());
      }
    }
  }
  // public void addAction(Action action) {}

  // public boolean sendActions() { return true; }

  // public PlayError receiveError() { return null; }

  // public void errorHandling(PlayError err) {}
}
