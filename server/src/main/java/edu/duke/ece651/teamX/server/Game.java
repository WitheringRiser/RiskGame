package edu.duke.ece651.teamX.server;

import edu.duke.ece651.teamX.shared.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Game {

  private int num_player;
  private int init_units;
  private HashMap<Player, Socket> player_dict;
  private Map map;
  private Communicate communicate;

  /**
   * Create the Game object
   *
   * @param p_num is the number of players in this game
   * @param u_num is the number of initial units for each player
   */
  public Game(int p_num, int u_num) {
    num_player = p_num;
    init_units = u_num;
    player_dict = new HashMap<Player, Socket>();
    map = new Map();
    communicate = new Communicate();
  }

  /**
   * Get the socket of a player
   *
   * @param p Player
   * @return the socket of the player
   */
  private Socket getPlayerSocket(Player p) {
    return player_dict.get(p);
  }

  private Iterable<Player> getAllPlayers() {
    return player_dict.keySet();
  }

  public Player getPlayerFromSocket(Socket s) {
    for (Player p : player_dict.keySet()) {
      if (player_dict.get(p) == s) {
        return p;
      }
    }
    return null;
  }

  //Getter mainly for testing
  public int getNumPlayer() {
    return num_player;
  }

  public Map getMap() {
    return map;
  }

  /**
   * Get all territory groups from parser
   *
   * @return a hashmap of territory groups
   */
  public HashMap<Integer, ArrayList<Territory>> setupGroup() {
    WorldParser parser = new WorldParser(num_player);
    HashMap<Integer, ArrayList<Territory>> res = parser.getWorld();
    return res;
  }

  /**
   * Assign a player to a territory group
   *
   * @param terrGroup is the territory group chose by player
   * @param p         is the player to assign
   * @throws IllegalArgumentException if there is any invlaid territory
   */
  public void setGroupOwner(ArrayList<Territory> terrGroup, Player p) {
    for (Territory t : terrGroup) {
      if (!map.addTerritory(t, p)) {
        throw new IllegalArgumentException("Invalid territory to add: " + t.getName());
      }
    }
  }


  /**
   * Send available territory groups to a client
   *
   * @param free_groups us current available territory groups
   * @param player      is the client to receive the groups
   */
  public void sendTerrGroup(HashMap<Integer, ArrayList<Territory>> free_groups,
      Player player) throws IOException {
    communicate.sendObject(getPlayerSocket(player), free_groups);
  }

  /**
   * Create a Player object for client and inform that client
   */
  public void createPlayer(Socket player_socket, String name) throws IOException {
    Player p = new Player(name, init_units);
    player_dict.put(p, player_socket);
    communicate.sendPlayer(player_socket, p);
  }

  /**
   * Create the map by adding territories and their owners
   */
  public void createMap() throws IOException, ClassNotFoundException {
    HashMap<Integer, ArrayList<Territory>> terr_groups = setupGroup();
    for (Player p : getAllPlayers()) {
      sendTerrGroup(terr_groups, p);  //Let client make decision
      int choice = communicate.receiveInt(getPlayerSocket(p));
      setGroupOwner(terr_groups.get(choice), p);  //Set owner of the group
      terr_groups.remove(choice);                 //remove this option
    }
  }

  /**
   * Using the received results to set units on the map
   *
   * @param res is the results received from client
   * @return true if all units are set successfully, false otherwise
   */
  public boolean setUnits(ArrayList<Territory> res) {
    for (Territory terr : res) {
      if (!getMap().setTerritoryUnits(terr, terr.getUnits())) {
        return false;
      }
    }
    return true;
  }

  public void sendMapAll() throws IOException {
    for (Player p : getAllPlayers()) {
      sendMap(p);
    }
  }

  public void sendMap(Player player) throws IOException {
    communicate.sendMap(getPlayerSocket(player), map);
  }

  public void handleActionSenders(Iterable<ActionSender> actionSenders)
      throws IllegalArgumentException {

    Iterable<MoveSender> moveSenders = getMoveSenders(actionSenders);
    Iterable<AttackSender> attackSenders = getAttackSenders(actionSenders);

    // TODO: no move processor right now so cannot handle move senders
    handleAttackSenders(attackSenders);
  }

  private void handleAttackSenders(Iterable<AttackSender> attackSenders)
      throws IllegalArgumentException {
    AttackProcessor attackProcessor = new AttackProcessor((ArrayList<AttackSender>) attackSenders,
        map);
    attackProcessor.resovleAllAttack();
  }

  private Iterable<MoveSender> getMoveSenders(Iterable<ActionSender> actionSenders) {
    ArrayList<MoveSender> moveSenders = new ArrayList<>();
    for (ActionSender actionSender : actionSenders) {
      if (actionSender instanceof MoveSender) {
        moveSenders.add((MoveSender) actionSender);
      }
    }
    return moveSenders;
  }

  private Iterable<AttackSender> getAttackSenders(Iterable<ActionSender> actionSenders) {
    ArrayList<AttackSender> attackSenders = new ArrayList<>();
    for (ActionSender actionSender : actionSenders) {
      if (actionSender instanceof AttackSender) {
        attackSenders.add((AttackSender) actionSender);
      }
    }
    return attackSenders;
  }

  // TODO: don't make checker constructor directly check and throw exception
  //  otherwise it's hard to call the check function from outside
  public void checkAction() {
  }

  public void handleActions(Iterable<Action> actions) {
  }

  /**
   * Print out the master map
   */
  public void printMasterMap() {
    map.printMap();
  }

  //  print all actionsenders content, only for testing and debugging
  public void printActions(Iterable<ActionSender> allActions) {
    System.out.println("Attack:");
    for (AttackSender a : getAttackSenders(allActions)) {
      System.out.println("From " + a.getSource().getName() + " to " +
          a.getDestination().getName() +
          " units: " + Integer.toString(a.getUnitsNum()));
    }
    System.out.println("Move:");
    for (MoveSender a : getMoveSenders(allActions)) {
      System.out.println("From " + a.getSource().getName() + " to " +
          a.getDestination().getName() +
          " units: " + Integer.toString(a.getUnitsNum()));
    }
    System.out.println("---------------------\n");
  }

}
