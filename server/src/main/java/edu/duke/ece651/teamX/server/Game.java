package edu.duke.ece651.teamX.server;
import edu.duke.ece651.teamX.shared.*;
import java.util.ArrayList;

public class Game {
  private int num_player;
  private int init_units;
  private ArrayList<Player> players;
  private Map map;
  private Communicate communicate;
  public Game(int p_num, int u_num) {
    num_player = p_num;
    init_units = u_num;
    players = new ArrayList<Player>();
    map = new Map();
    communicate = new Communicate();
  }

  //Getter mainly for testing
  public int getNumPlayer() { return num_player; }
  public Map getMap() { return map; }

  public void createMap() {}

  public void setGroupOwner(ArrayList<Territory> terrGroup, Player p) {
    for (Territory t : terrGroup) {
      if (!map.addTerritory(t, p)) {
        throw new IllegalArgumentException("Invalid territory to add " + t.getName());
      }
    }
  }

  public void setUnits() {}

  public void sendMap(Player player) {}

  public void receiveActions() {}

  public void checkAction() {}

  public void handleActions(Iterable<Action> actions) {}
}
