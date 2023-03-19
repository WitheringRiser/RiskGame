package edu.duke.ece651.teamX.shared;

import java.util.*;
import org.json.*;

// Parses world json files into useable objects
public class MyFormatter {
  private int NumPlayers;
  private HashMap<String, Territory> terr_name_dict;
  private HashMap<String, JSONArray> terr_neighbor_dict;

  public MyFormatter(int num) {
    NumPlayers = num;
    terr_name_dict = new HashMap<String, Territory>();
    terr_neighbor_dict = new HashMap<String, JSONArray>();
  }

  /*
   * input: a map object to write to, and the string representing the contents of
   * a json file
   * converts a json string object into a map
   */
  public void MapParse(HashMap<Integer, ArrayList<Territory> > Input, String MapJson) {
    if (NumPlayers < 2) {
      return;
    }
    // receive the json string and parse to a territoryMap
    JSONObject InputMap = new JSONObject(MapJson);
    // go through all players to set their territories

    for (int i = 0; i < NumPlayers; i++) {
      JSONArray PlayerTemp = new JSONArray();
      PlayerTemp = InputMap.optJSONArray("player_" + Integer.toString(i));
      // System.out.println(PlayerTemp);

      ArrayList<Territory> InnerTerr = new ArrayList<Territory>();
      for (int j = 0; j < PlayerTemp.length(); j++) {
        JSONObject TerrTemp = PlayerTemp.optJSONObject(j);
        Territory Inner = JsonToTerritory(TerrTemp);
        InnerTerr.add(Inner);
      }
      Input.put(i, InnerTerr);
    }
    formRelations();
  }

  /*
   * input: JSONObject
   * output: Territory
   * takes a JSONObject, creates a Territory object, and sets the Territory values
   * to the fields from the JSONObject
   */
  public Territory JsonToTerritory(JSONObject TerrTemp) {
    // parse a JsonObject which is a territory
    // "<Default name>" is a placeholder for territory name. Will need to
    // find a way to set up territory names from a .txt file.
    String TerritoryName = TerrTemp.optString("territoryName");
    Territory Inner = new Territory(TerritoryName);
    terr_name_dict.put(TerritoryName, Inner);
    // String owner = TerrTemp.optString("owner");

    // Integer numSoldiers = 0;
    // BasicUnit u = new BasicUnit();
    // Inner.addUnits(u, numSoldiers);

    // neighbor stored in JsonArray Format
    JSONArray Neighbors = TerrTemp.optJSONArray("neighbor");
    terr_neighbor_dict.put(TerritoryName, Neighbors);
    return Inner;
  }

  public void formRelations() {
    for (String name : terr_neighbor_dict.keySet()) {
      Territory curr = terr_name_dict.get(name);
      JSONArray Neighbors = terr_neighbor_dict.get(name);
      for (int j = 0; j < Neighbors.length(); j++) {
        JSONObject InnerNbor = Neighbors.optJSONObject(j);
        String NeighName = InnerNbor.optString("neighbor_" + Integer.toString(j));
        if (terr_name_dict.containsKey(NeighName)) {
          Territory neighbor = terr_name_dict.get(NeighName);
          curr.addNeighbors(neighbor);
        }
        else {
          System.out.println(NeighName);
        }
      }
    }
  }

  /*
   * input: map object
   * output: JSONObject
   * converts a map into a JSONObject
   */
  public JSONObject MapCompose(HashMap<Integer, ArrayList<Territory> > territoryMap) {
    MapToJson myMaptoJson = new MapToJson(territoryMap);
    return myMaptoJson.getJSON();
  }
}
