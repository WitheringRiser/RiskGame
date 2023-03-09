package edu.duke.ece651.teamX.server;
import edu.duke.ece651.teamX.shared.*;
import java.util.*;
import java.io.*;


public class WorldInit {
  private HashMap<Integer, ArrayList<Territory>> myworld;

  public WorldInit(int playerNum, HashMap<Integer, ArrayList<Territory>> w) {
    myworld = w;
    createWorld(playerNum);
  }

  public WorldInit(int playerNum) {
    myworld = new HashMap<>();
    createWorld(playerNum);
  }


  public HashMap<Integer, ArrayList<Territory>> getWorld() {
    return myworld;
  }

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