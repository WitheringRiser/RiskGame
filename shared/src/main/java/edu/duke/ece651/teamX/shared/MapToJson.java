package edu.duke.ece651.teamX.shared;

import java.util.*;
import org.json.JSONObject;
import org.json.JSONArray;

// Converts a map object into a JSONObject
public class MapToJson {

  private JSONObject ans;
  private HashMap<Integer, ArrayList<Territory>> myTerrMap;

  public MapToJson() {
  }

  public MapToJson(HashMap<Integer, ArrayList<Territory>> rhsTerrMap) {
    this.ans = new JSONObject();
    this.myTerrMap = new HashMap<>();
    this.myTerrMap = rhsTerrMap;
    getPlayerObj();
  }


  public JSONObject getJSON() {
    return this.ans;
  }

  /* modifies JSONObject ans to have player IDs
   */
  public void getPlayerObj() {
    for (HashMap.Entry<Integer, ArrayList<Territory>> entry : myTerrMap.entrySet()) {
      int key = entry.getKey();
      StringBuilder playerID = new StringBuilder();
      playerID.append("player_").append(key);

      JSONArray territoryArray = new JSONArray();
      getTerritoryArray(territoryArray, entry.getValue());
      ans.put(playerID.toString(), territoryArray);
    }
  }

  /* input: JSONArray of Territory objects, ArrayList of Territory objects
   *  takes a JSONObject representing the Territory and putting it into a JSONArray representing Territory's
   */
  public void getTerritoryArray(JSONArray territoryArray, ArrayList<Territory> TerritoryList) {
    for (int i = 0; i < TerritoryList.size(); i++) {
      JSONObject terrObj = new JSONObject();
      Territory myTerr = TerritoryList.get(i);
      getTerritoryObj(terrObj, myTerr);
      territoryArray.put(terrObj);
    }
  }

  /* input: JSONObject representing a Territory, and a Territory
   * reads from the fields of a Territory object to build a JSONObject representing the Territory
   */
  public void getTerritoryObj(JSONObject terrObj, Territory myTerr) {

    int numSoldiers = myTerr.getUnitsNumber();
    terrObj.put("soldiers", numSoldiers);

    Iterator<Territory> neighborList = myTerr.getNeighbours();
    JSONArray neighborArray = new JSONArray();

    // Iterator converted to ArrayList to match parameter type of function call to getNeighborArray
    ArrayList<Territory> list = new ArrayList<>();
    neighborList.forEachRemaining(list::add);

    getNeighborArray(neighborArray, list);
    terrObj.put("neighbor", neighborArray);
    String name = myTerr.getName();
    terrObj.put("territoryName", name);
  }

  /* input: JSONArray of neighbor JSONObjects, ArrayList of Territory objects representing the neighbors
   * creates a JSONObject representing neighbor objects using neighborList and places it into a JSONArray
   */
  public void getNeighborArray(JSONArray neighborArray, ArrayList<Territory> neighborList) {
    for (int i = 0; i < neighborList.size(); i++) {
      JSONObject neighborObj = new JSONObject();
      StringBuilder neighborID = new StringBuilder();
      neighborID.append("neighbor_").append(i);
      neighborObj.put(neighborID.toString(), neighborList.get(i));
      neighborArray.put(neighborObj);
    }
  }


}
