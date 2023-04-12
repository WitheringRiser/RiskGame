package edu.duke.ece651.teamX.client.controller;

import edu.duke.ece651.teamX.shared.BasicUnit;
import edu.duke.ece651.teamX.shared.Map;
import edu.duke.ece651.teamX.shared.Player;
import edu.duke.ece651.teamX.shared.Territory;
import edu.duke.ece651.teamX.shared.Unit;
import java.io.IOException;

import edu.duke.ece651.teamX.client.view.GeneralScreen;
import edu.duke.ece651.teamX.shared.Communicate;
import java.net.Socket;
import java.util.ArrayList;

import java.net.URL;

import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class CreateGameController implements Controller {

  protected ArrayList<String> namePassword;
  protected Socket clientSocket;
  protected Stage stage;

  public CreateGameController(Stage st, Socket cs, ArrayList<String> np) {
    namePassword = np;
    clientSocket = cs;
    stage = st;
  }

  public void setNewLayout() throws IOException {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/creategame.fxml"));
    loader.setController(this);
    Parent root = loader.load();
    URL cssResource = getClass().getResource("/style/roomButton.css");
    Scene scene = new Scene(root, 640, 480);
    scene.getStylesheets().add(cssResource.toString());
    stage.setTitle("Create a New Game");
    stage.setScene(scene);
    stage.show();
  }

  @FXML
  public void back() throws IOException, ClassNotFoundException {
    RoomController rc = new RoomController(stage, clientSocket, namePassword);
    rc.resetConnection();
    // RoomScreen rs = new RoomScreen(rc);
    GeneralScreen<RoomController> rs = new GeneralScreen<>(rc);
  }

  protected int getSendNumber(ActionEvent ae) {
    try {
      Object source = ae.getSource();
      if (source instanceof Button) {
        Button btn = (Button) source;
        int playerNum = Integer.parseInt(btn.getText());
        Communicate.sendInt(clientSocket, playerNum);
        return playerNum;
      } else {
        throw new IllegalArgumentException("Invalid source " + source + " for ActionEvent");
      }
    } catch (Exception e) {
      throw new IllegalArgumentException("Error input event");
    }
  }

  @FXML
  public void onNumberButton(ActionEvent ae) throws IOException {
    getSendNumber(ae);

    // TODO: only for test
    Unit unit1 = new BasicUnit();
    Unit unit2 = new BasicUnit();
    unit1.upgradeLevel(1);
    unit2.upgradeLevel(2);

    ArrayList<Unit> units = new ArrayList<>();
    units.add(unit1);
    units.add(unit2);

    Territory desert = new Territory("Desert");
    desert.addUnits(units);
    Territory mountains = new Territory("Mountains");
    Territory swamp = new Territory("Swamp");
    Territory beach = new Territory("Beach");
    desert.addNeighbors(mountains);
    desert.addNeighbors(swamp);
    mountains.addNeighbors(desert);
    mountains.addNeighbors(beach);
    swamp.addNeighbors(desert);
    swamp.addNeighbors(beach);
    Player p1 = new Player("p1", 10);
    Map map = new Map();
    map.addTerritory(desert, p1);
    map.addTerritory(mountains, p1);
    map.addTerritory(swamp, p1);
    map.addTerritory(beach, p1);
    TerritoryInfoController territoryInfoController = new TerritoryInfoController(stage,
        clientSocket, map);
    GeneralScreen<TerritoryInfoController> displayMapScreen = new GeneralScreen<>(
        territoryInfoController);
  }

}
