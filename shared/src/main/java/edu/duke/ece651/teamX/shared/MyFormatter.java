package edu.duke.ece651.teamX.shared;
import java.util.*;
import org.json.*;

public class MyFormatter {
  private int NumPlayers;

  public MyFormatter(int num) {
    NumPlayers = num;
  }


  public void MapParse(HashMap<Integer, ArrayList<Territory>> Input, String MapJson) {
    // receive the json string and parse to a territoryMap
    JSONObject InputMap = new JSONObject(MapJson);
    // go through all players to set their territories
    for (int i = 1; i <= NumPlayers; i++) {
      JSONArray PlayerTemp = new JSONArray();

      PlayerTemp = InputMap.optJSONArray("player_" + Integer.toString(i));

      if (PlayerTemp != null) {
        ArrayList<Territory> InnerTerr = new ArrayList<Territory>();
        for (int j = 0; j < PlayerTemp.length(); j++) {
          JSONObject TerrTemp = PlayerTemp.optJSONObject(j);
          Territory Inner = JsonToTerritory(TerrTemp);
          InnerTerr.add(Inner);
        }
        if (InnerTerr.size() == 0) {
          int tell = 0;
        }
        else{
          Input.put(i, InnerTerr);
        }
      }
    }
  }

  public Territory JsonToTerritory(JSONObject TerrTemp) {
    // parse a JsonObject which is a territory
    // "<Default name>" is a placeholder for territory name. Will need to
    // find a way to set up territory names from a .txt file.
    String TerritoryName = TerrTemp.optString("territoryName");
    Territory Inner = new Territory(TerritoryName);
    String owner = TerrTemp.optString("owner");

    JSONArray Soldiers = TerrTemp.optJSONArray("soldiers");
    for (int i = 0; i < Soldiers.length(); i++) {
      JSONObject InnerSoldier = Soldiers.optJSONObject(i);
      int num = InnerSoldier.optInt("level_" + Integer.toString(i));
      BasicUnit u = new BasicUnit(num);
      Inner.addUnits(u, num);
    }
    // neighbor stored in JsonArray Format
    JSONArray Neighbors = TerrTemp.optJSONArray("neighbor");
    for (int j = 0; j < Neighbors.length(); j++) {
      JSONObject InnerNbor = Neighbors.optJSONObject(j);
      String NeighName = InnerNbor.optString("neighbor_" + Integer.toString(j));
      Territory tempTerr = new Territory(NeighName);
      Inner.addNeighbors(tempTerr);
    }
    return Inner;
  }
}

  