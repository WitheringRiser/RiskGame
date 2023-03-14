package edu.duke.ece651.teamX.server;
import edu.duke.ece651.teamX.shared.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
public class Game {
  private int num_player;
  private int init_units;
  private HashMap<Player, Socket> player_dict;
  private Map map;
  private Communicate communicate;

  /**
   *Create the Game object 
   *@param p_num is the number of players in this game
   *@param u_num is the number of initial units for each player
   */
  public Game(int p_num, int u_num) {
    num_player = p_num;
    init_units = u_num;
    player_dict = new HashMap<Player, Socket>();
    map = new Map();
    communicate = new Communicate();
  }

  //Getter mainly for testing
  public int getNumPlayer() { return num_player; }
  public Map getMap() { return map; }

  /**
   *Get all territory groups from parser
   *@return a hashmap of territory groups
   */
  public HashMap<Integer, ArrayList<Territory> > setupGroup() {
    WorldParser parser = new WorldParser(num_player);
    HashMap<Integer, ArrayList<Territory> > res = parser.getWorld();
    return res;
  }

  /**
   *Assign a player to a territory group
   *@param terrGroup is the territory group chose by player
   *@param p is the player to assign
   *@throws IllegalArgumentException if there is any invlaid territory
   */
  public void setGroupOwner(ArrayList<Territory> terrGroup, Player p) {
    for (Territory t : terrGroup) {
      if (!map.addTerritory(t, p)) {
        throw new IllegalArgumentException("Invalid territory to add: " + t.getName());
      }
    }
  }

  public void sendTerrGroup(HashMap<Integer, ArrayList<Territory> > free_groups,
                            Player player) throws IOException {
    communicate.sendObject(player_dict.get(player), free_groups);
  }

  public void createPlayer(Socket player_socket, String name) throws IOException {
    Player p = new Player(name, init_units);
    player_dict.put(p, player_socket);
    communicate.sendPlayer(player_socket, p);
  }
  /**
   *Create the map by adding territories and their owners
   */
  public void createMap() throws IOException, ClassNotFoundException {
    HashMap<Integer, ArrayList<Territory> > terr_groups = setupGroup();
    for (Player p : player_dict.keySet()) {
      sendTerrGroup(terr_groups, p);  //Let client make decision
      int choice = communicate.receiveInt(player_dict.get(p));
      setGroupOwner(terr_groups.get(choice), p);  //Set owner of the group
      terr_groups.remove(choice);                 //remove this option
    }
  }

  public void setUnits() {}

  public void sendMap(Player player) {}

  public void receiveActions() {}

  public void checkAction() {}

  public void handleActions(Iterable<Action> actions) {}
}
