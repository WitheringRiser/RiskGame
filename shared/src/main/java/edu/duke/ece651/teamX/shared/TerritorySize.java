package edu.duke.ece651.teamX.shared;

import java.io.*;
import java.util.*;

//have a size for a territory to determine distance to other territories
//"distance" determined by the sum of the sizes of all territories between source and destination
public class TerritorySize {
  private HashMap<String, Integer> terrSize;

  public TerritorySize() {
    String file = "/SizeMap.txt";
    this.terrSize = new HashMap<>();
    InputStream input = getClass().getResourceAsStream(file);
    
    String[] Split;
    Scanner scanner = new Scanner(input);

    while (scanner.hasNext()) {
      String str = scanner.nextLine();
      Split = str.split("-");
      this.terrSize.put(Split[0], Integer.parseInt(Split[1]));
    }
  }
  public int getTerritorySize(String Name) {
    return this.terrSize.get(Name);
  }
}