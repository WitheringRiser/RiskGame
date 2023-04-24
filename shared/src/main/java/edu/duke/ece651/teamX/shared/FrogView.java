package edu.duke.ece651.teamX.shared;

import java.util.ArrayList;
import java.util.HashMap;

public class FrogView {
  private HashMap<String, String> oldInfoMap;
  private HashMap<String, String> currentInfoMap;
  // private Map map;
  private String playerName;

  public FrogView(String name) {
    playerName = name;
    oldInfoMap = new HashMap<>();
    currentInfoMap = new HashMap<>();
  }

  private String getFullView(Map map, Territory t) {
    String content = "";
    content += "Owner: " + map.getOwner(t).getName() + "\n";
    content += "Units:\n";
    HashMap<String, ArrayList<Integer> > unitDic = t.getUnitsDit();
    for (String typeName : unitDic.keySet()) {
      content += "  - " + typeName + ": " + unitDic.get(typeName).size() + "\n";
    }
    if (t.isCloaked()) {
      content += "Cloaked\n";
    }
    ArrayList<Integer> spies = t.getSpyIndsFromPlayer(playerName);
    if (spies.size() > 0) {
      content += "Your Spies: " + spies.size() + "\n";
    }
    return content;
  }

  public void refreshMap(Map map) {
    oldInfoMap = currentInfoMap;
    currentInfoMap = new HashMap<>();
    for (Territory t : map.getAllTerritories()) {
      if (map.getOwner(t).getName().equals(playerName)) {
        currentInfoMap.put(t.getName(), getFullView(map, t));
        continue;
      }
      if (t.getSpyIndsFromPlayer(playerName).size() > 0) {
        currentInfoMap.put(t.getName(), getFullView(map, t));
        continue;
      }
      for (Territory ownT : map.getTerritoriesByPlayerName(playerName)) {
        if (t.hasNeighbor(ownT)) {
          if (!t.isCloaked()) {
            currentInfoMap.put(t.getName(), getFullView(map, t));
          }
          break;
        }
      }
      if (!currentInfoMap.containsKey(t.getName()) &&
          oldInfoMap.containsKey(t.getName())) {
        String content = oldInfoMap.get(t.getName());
        if (content.startsWith("==========================")) {
          currentInfoMap.put(t.getName(), content);
        }
        else {
          content = "==========================\n"
                    + "Outdated Infomation:\n" + content + "==========================\n";
          currentInfoMap.put(t.getName(), content);
        }
      }
    }
  }

  public String getTerrInfo(String terr) {
    if (currentInfoMap.containsKey(terr)) {
      return currentInfoMap.get(terr);
    }
    else {
      return "";
    }
  }
}
