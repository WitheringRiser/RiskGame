package edu.duke.ece651.teamX.shared;
import java.util.*;
import org.json.JSONObject;
import org.json.JSONArray;

public class ActionToJson {
  private JSONObject ans;
  private JSONObject ansAllAction;
  private ArrayList<ActionSender> actionList;
  private HashMap<Integer, ArrayList<ActionSender>> AllAction;
  private String ActionType;
  public ActionToJson(ArrayList<ActionSender> myactionList, String Type) {
    this.ans = new JSONObject();
    this.actionList = new ArrayList<>();
    this.actionList = myactionList;
    ActionType = Type;
    getActionListObj();
  }
  public ActionToJson(HashMap<Integer, ArrayList<ActionSender>> Actions) {
    this.ansAllAction = new JSONObject();
    this.AllAction = new HashMap<>();
    this.AllAction = Actions;
  }



  public void ComposeAllAction() {
    for (HashMap.Entry<Integer, ArrayList<ActionSender>> entry : AllAction.entrySet()) {
      JSONArray currActionArray = new JSONArray();
      for (int i = 0; i < entry.getValue().size(); i++) {
        JSONObject actionObj = new JSONObject();
        ActionSender action = entry.getValue().get(i);
        getActionObj(actionObj, action);
        currActionArray.put(actionObj);
      }
      String insertKey = "player_" + entry.getKey();
      ansAllAction.put(insertKey, currActionArray);
    }
  }
  public JSONObject getAllAction() {
    return this.ansAllAction;
  }

  public JSONObject getJSON() {
    return this.ans;
  }
  
  private void getActionListObj() {
    JSONArray actionArray = new JSONArray();
    getActionArray(actionArray);
    ans.put(ActionType, actionArray);
  }

  public void getActionArray(JSONArray actionArray) {
    for (int i = 0; i < actionList.size(); i++) {
      JSONObject actionObj = new JSONObject();
      ActionSender action = actionList.get(i);
      getActionObj(actionObj, action);
      actionArray.put(actionObj);
    }
  }

  public void getActionObj(JSONObject actionObj, ActionSender action) {
    MapToJson myMaptoJson = new MapToJson();
    JSONObject srcTerritoryObj = new JSONObject();
    myMaptoJson.getTerritoryObj(srcTerritoryObj, action.getSource());
    actionObj.put("src", srcTerritoryObj);

    JSONObject dstTerritoryObj = new JSONObject();
    myMaptoJson.getTerritoryObj(dstTerritoryObj, action.getDestination());
    actionObj.put("dst", dstTerritoryObj);


  }
}