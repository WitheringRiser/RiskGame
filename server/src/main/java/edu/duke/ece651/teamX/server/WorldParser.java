package edu.duke.ece651.teamX.server;

import edu.duke.ece651.teamX.shared.*;
import java.util.*;
import java.io.*;

// Reads the json file and sends String representing file to the MyFormatter class
public class WorldParser {

  private HashMap<Integer, ArrayList<Territory>> myworld;
  //private ArrayList<ArrayList<Territory>> territoryGroups;

  public WorldParser(int playerNum, HashMap<Integer, ArrayList<Territory>> w) {
    myworld = w;
    createWorld(playerNum);
  }

  public WorldParser(int playerNum) {
    myworld = new HashMap<>();
    createWorld(playerNum);
  }


  public HashMap<Integer, ArrayList<Territory>> getWorld() {
    return myworld;
  }

  /*
  public ArrayList<ArrayList<Territory>> getTerritoryGroups() {
    return territoryGroups;
  }
  */

  /* input: playerNum representing the number of players in the game
   * modifies the map object in this class to include the world map based on the json file
   */
  private void createWorld(int playerNum) {
    StringBuilder file = new StringBuilder();
    file.append("/InterestingMap").append(playerNum).append(".json");
    InputStream input = getClass().getResourceAsStream(file.toString());

    StringBuilder worldInfo = new StringBuilder();
    Scanner scanner = new Scanner(input);

    while (scanner.hasNext()) {
      worldInfo.append(scanner.next());
    }

    MyFormatter m = new MyFormatter(playerNum);
    m.MapParse(myworld, worldInfo.toString());
  }
}