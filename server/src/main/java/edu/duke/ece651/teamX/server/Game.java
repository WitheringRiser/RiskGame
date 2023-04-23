package edu.duke.ece651.teamX.server;

import edu.duke.ece651.teamX.shared.*;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Game implements Runnable {

  private int num_player;
  private int init_units;
  private HashMap<Player, Socket> player_dict;
  private HashMap<Player, Socket> status_dict;
  private Map map;
  private boolean hasBegin;
  private boolean isEnd;

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
    status_dict = new HashMap<Player, Socket>();
    map = new Map();
    hasBegin = false;
    isEnd = false;
  }

  /**
   * Get the socket of a player
   *
   * @param p Player
   * @return the socket of the player
   */
  public Socket getPlayerSocket(Player p) {
    return player_dict.get(p);
  }

  private Iterable<Player> getAllPlayers() {
    return player_dict.keySet();
  }

  public boolean containsPlayer(String p_name) {
    for (Player p : player_dict.keySet()) {
      if (p.getName().equals(p_name)) {
        return true;
      }
    }
    return false;
  }

  public Player getPlayerFromSocket(Socket s) {
    for (Player p : player_dict.keySet()) {
      if (player_dict.get(p) == s) {
        return p;
      }
    }
    return null;
  }

  // Get the expected number of palyers
  public int getNumPlayer() {
    return num_player;
  }

  // Get current number of players
  public int getActualNumPlayer() {
    return player_dict.size();
  }

  public Map getMap() {
    return map;
  }

  /**
   * Check if the game is already begin --> cannot add new clients in it
   *
   * @return true if it has call the run() function false otherwise
   */
  public boolean checkHasBegin() {
    return hasBegin;
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
    Communicate.sendObject(getPlayerSocket(player), free_groups);
  }

  /**
   * Create a Player object for client and inform that client
   */
  public void createPlayer(Socket player_socket, String name) throws IOException {
    Player p = new Player(name, init_units);
    Communicate.sendPlayer(player_socket, p);
    player_dict.put(p, player_socket);
  }

  public boolean updateSocket(String name, Socket newSocket) {
    for (Player p : player_dict.keySet()) {
      if (p.getName().equals(name)) {
        try {
          Communicate.sendPlayer(newSocket, p);
          getPlayerSocket(p).close();
        } catch (IOException ioe) {
          return false;
        }
        player_dict.put(p, newSocket);
        return true;
      }
    }
    return false;
  }

  /**
   * Create the map by adding territories and their owners
   */
  public void createMap() throws IOException, ClassNotFoundException {
    HashMap<Integer, ArrayList<Territory>> terr_groups = setupGroup();

    for (Player p : player_dict.keySet()) {
      int choice;
      try {
        sendTerrGroup(terr_groups, p); // Let client make decision
        choice = Communicate.receiveInt(getPlayerSocket(p));
        status_dict.put(p, getPlayerSocket(p));

      } catch (Exception ioe) {// if the client loses connection -> choose the first
        choice = terr_groups.keySet().stream().findFirst().get();
        status_dict.put(p, null);
      }
      setGroupOwner(terr_groups.get(choice), p); // Set owner of the group
      terr_groups.remove(choice); // remove this option
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
    try {
      Socket s = getPlayerSocket(player);
      Communicate.sendMap(s, map);
      // If there are new valid connection, we update here
      status_dict.put(player, s);
    } catch (IOException ioe) {
      status_dict.put(player, null);
    }
  }

  private boolean hasConnection() {
    for (Socket s : status_dict.values()) {
      if (s != null) {
        return true;
      }
    }
    return false;
  }

  public void handleActionSenders(Iterable<ActionSender> actionSenders)
      throws IllegalArgumentException {
    Iterable<MoveSender> moveSenders = getMoveSenders(actionSenders);
    Iterable<AttackSender> attackSenders = getAttackSenders(actionSenders);

    // TODO: no move processor right now so cannot handle move senders
    handleMoveSenders(moveSenders);
    handleAttackSenders(attackSenders);
  }

  private void handleAttackSenders(Iterable<AttackSender> attackSenders)
      throws IllegalArgumentException {
    AttackProcessor attackProcessor = new AttackProcessor((ArrayList<AttackSender>) attackSenders,
        map);
    attackProcessor.resovleAllAttack();
  }

  private void handleMoveSenders(Iterable<MoveSender> moveSenders)
      throws IllegalArgumentException {
    MoveProcessor moveProcessor = new MoveProcessor((ArrayList<MoveSender>) moveSenders, map);
    moveProcessor.resolveAllMove();
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

  /**
   * Print out the master map
   */
  public void printMasterMap() {
    map.printMap();
  }

  // print all actionsenders content, only for testing and debugging
  public void printActions(Iterable<ActionSender> allActions, ArrayList<UpgradeSender> allUpgrades,
      ArrayList<ResearchSender> allResearch) {
    System.out.println("---------------------\n");
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
    System.out.println("Upgrade:");
    for (UpgradeSender a : allUpgrades) {
      System.out.println("Upgrade " + a.getSource().getName() + "'s" +
          Integer.toString(a.getNum()) +
          "units to level " + Integer.toString(a.getToLevel()));
    }
    System.out.println("Research:");
    // for (ResearchSender a : allResearch) {
    // System.out.println("Player " + a.getPlayer() +
    // "want to improve his technology level");
    // }
    System.out.println("---------------------\n");
  }

  public Player whoWons() {
    for (Player player : getAllPlayers()) {
      if (map.getTerritories(player).size() == map.getTerritoryNum()) {
        isEnd = true; // If a player win --> the game is end
        return player;
      }
    }
    return null;
  }

  public boolean hasWon() {
    return whoWons() != null;
  }

  public boolean checkIsEnd() {
    return isEnd;
  }

  public ArrayList<Player> whoLost() {
    ArrayList<Player> result = new ArrayList<>();
    for (Player player : getAllPlayers()) {
      if (map.getTerritories(player).size() == 0) {
        result.add(player);
      }
    }
    return result;
  }

  private GameResult getGameResult() {
    return new GameResult(hasWon(), whoWons(), whoLost());
  }

  private void sendGameResult(GameResult gameResult)
      throws IOException {
    for (Player p : player_dict.keySet()) {
      // Need to send to the socket we send map to
      Socket s = status_dict.get(p);
      if (s == null) {
        continue;
      }
      try {
        Communicate.sendGameResult(s, gameResult);
      } catch (IOException ioe) {
        continue;
      }
    }
  }

  public RoomSender getRoomSender() {
    if (isEnd) {
      return new RoomSender(num_player, player_dict.size(),
          new HashSet<Player>(player_dict.keySet()), map,
          hasBegin, isEnd, whoWons(), whoLost());
    }
    if (hasBegin) {
      return new RoomSender(num_player, player_dict.size(),
          new HashSet<Player>(player_dict.keySet()), map,
          hasBegin, isEnd, null, whoLost());
    }

    return new RoomSender(num_player, player_dict.size(), new HashSet<Player>(player_dict.keySet()),
        map,
        hasBegin, false, null, null);
  }

  private void incrementAllTerritoryByOneUnit() {
    for (Territory terr : map.getAllTerritories()) {
      terr.addUnits(1, map.getOwner(terr).getName());
      terr.turnReset();
    }
  }

  private void handleAllShields(ArrayList<ShieldSender> allShield) {
    ShieldProcessor p = new ShieldProcessor(allShield, map);
    p.resovleAllShield();
  }

  private void handleAdvancedActions(ArrayList<ResearchSender> allResearh, ArrayList<UpgradeSender> allUpgrades,
      ArrayList<SpyMoveSender> allSpymoves, ArrayList<CloakSender> allCloaks) {
    UpgradeProcessor up = new UpgradeProcessor(allUpgrades, map);
    SpyMoveProcessor smp = new SpyMoveProcessor(allSpymoves, map);
    CloakProcessor ckp = new CloakProcessor(allCloaks, map);
    ResearchProcessor rp = new ResearchProcessor(allResearh, map);
    up.resolveAllUpgrade();
    smp.resolveAllSpyMove();
    ckp.resolveAllCloaks();
    rp.resovleAllResearch();
  }

  private void playOneTurn(int try_num)
      throws IOException, ClassNotFoundException {
    GameResult gameResult = getGameResult();
    // collect all the moves and attacks from players
    ArrayList<ActionSender> allActions = new ArrayList<>();
    ArrayList<ResearchSender> allResearchs = new ArrayList<>();
    ArrayList<UpgradeSender> allUpgrades = new ArrayList<>();
    ArrayList<SpyMoveSender> allSpyMoves = new ArrayList<>();
    ArrayList<CloakSender> allCloaks = new ArrayList<>();
    ArrayList<ShieldSender> allshield = new ArrayList<>();
    for (Player player : getAllPlayers()) {
      // if this player has lost, skip
      if (gameResult.loserContains(player)) {
        continue;
      }
      // Need to receive from the socket that we send map to
      Socket s = status_dict.get(player);
      // if the client lose connection when sending map, skip
      if (s == null) {
        continue;
      }
      try {
        ArrayList<MoveSender> moves = (ArrayList<MoveSender>) Communicate.receiveObject(s);
        ArrayList<AttackSender> attacks = (ArrayList<AttackSender>) Communicate.receiveObject(s);
        ResearchSender research = (ResearchSender) Communicate.receiveObject(s);
        ArrayList<UpgradeSender> upgrades = (ArrayList<UpgradeSender>) Communicate.receiveObject(s);
        ArrayList<SpyMoveSender> spymoves = (ArrayList<SpyMoveSender>) Communicate.receiveObject(s);
        ArrayList<CloakSender> cloaks = (ArrayList<CloakSender>) Communicate.receiveObject(s);
        ArrayList<ShieldSender> shields = (ArrayList<ShieldSender>) Communicate.receiveObject(s);
        allActions.addAll(moves);
        allActions.addAll(attacks);
        allResearchs.add(research);
        allUpgrades.addAll(upgrades);
        allSpyMoves.addAll(spymoves);
        allCloaks.addAll(cloaks);
        allshield.addAll(shields);
      } catch (IOException ioe) { // if lose connection when receive --> skip
        continue;
      }
    }
    printActions(allActions, allUpgrades, allResearchs);
    try {
      handleAllShields(allshield);
      handleAdvancedActions(allResearchs, allUpgrades, allSpyMoves, allCloaks);
      handleActionSenders(allActions);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
    }
  }

  // public void increaseALlPlayerResources(int num) {
  // Set<Player> players = new HashSet<Player>(map.getMap().values());
  // for (Player player : players) {
  // player.increaseAllResource(num);
  // }
  // }

  // New version: increase resources based on num of territories
  public void increaseALlPlayerResources(int num) {
    for (Player player : map.getMap().values()) {
      player.increaseAllResource(num);
    }
  }

  /**
   * isolate for testing
   */
  public void playTurns() throws IOException, ClassNotFoundException {
    while (true) {
      sendMapAll();
      if (!hasConnection()) {
        continue;
      }
      // print out the master map
      printMasterMap();
      GameResult gameResult = getGameResult();
      sendGameResult(gameResult);
      if (gameResult.isWin()) {
        System.out.println("Game over");
        break;
      }
      playOneTurn(num_player);
      incrementAllTerritoryByOneUnit();
      increaseALlPlayerResources(5);
    }
  }

  public void run() {
    hasBegin = true;
    try {
      if (num_player != getActualNumPlayer()) {
        throw new RuntimeException("The actual player num is not as expected");
      }
      createMap();
      for (Player p : status_dict.keySet()) {
        Socket s = status_dict.get(p);
        try {
          if (s == null) {
            throw new IOException("no connection when send territory group");
          }
          // receive results from client and let game setUnits using this result
          ArrayList<Territory> res = (ArrayList<Territory>) Communicate.receiveObject(s);
          setUnits(res);
        } catch (Exception ioe) {
          ArrayList<Territory> allTerr = map.getTerritories(p);
          int num = init_units / allTerr.size();
          for (Territory t : allTerr) {
            t.addUnits(num);
          }
          if (init_units > num * allTerr.size()) {
            allTerr.get(0).addUnits(init_units - num * allTerr.size());
          }
        }
      }
      playTurns();
    } catch (Exception e) {
      System.out.println(e.getMessage());
      isEnd = true;
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Game og = (Game) o;
    if (!map.equals(og.map) || !player_dict.equals(og.player_dict)) {
      return false;
    }
    if (num_player != og.num_player || init_units != og.init_units) {
      return false;
    }
    if (hasBegin != og.hasBegin || isEnd != og.isEnd) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    return 17 * map.hashCode() + player_dict.hashCode();
  }
}
