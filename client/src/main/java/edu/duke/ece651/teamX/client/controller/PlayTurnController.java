package edu.duke.ece651.teamX.client.controller;

import edu.duke.ece651.teamX.client.*;
import edu.duke.ece651.teamX.client.view.*;
import edu.duke.ece651.teamX.shared.*;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class PlayTurnController implements Controller {

  private enum GameMode {
    DEFAULT, ATTACK, MOVE
  }

  private GameMode currentMode = GameMode.DEFAULT;
  private Territory sourceTerritory = null;
  private Territory destinationTerritory = null;


  TextField textField;

  @FXML
  Text resultText;

  @FXML
  private GridPane gridPane;

  protected ArrayList<String> namePassword;
  protected Socket clientSocket;
  protected Stage stage;
  protected Scene scene;

  private Map map;
  private ClientAttack clientAttack;
  private ClientMove clientMove;
  private ClientResearch clientResearch;
  private ClientUpgrade clientUpgrade;
  GameResult gameResult;

  public PlayTurnController(Stage stage, Socket socket, ArrayList<String> namepassword)
      throws IOException, ClassNotFoundException {

    this.namePassword = namepassword;
    this.stage = stage;
    this.clientSocket = socket;
    refresh();
  }

  private void refresh() throws IOException, ClassNotFoundException {
    map = Communicate.receiveMap(clientSocket);
    gameResult = Communicate.receiveGameResult(clientSocket);

    System.out.println("player name = " + namePassword.get(0));

    this.clientAttack = new ClientAttack(clientSocket, null, null, null, map,
        map.getPlayerByName(namePassword.get(0)));
    this.clientMove = new ClientMove(clientSocket, null, null, null, map,
        map.getPlayerByName(namePassword.get(0)));
    this.clientResearch = new ClientResearch(clientSocket,
        map.getPlayerByName(namePassword.get(0)));
    this.clientUpgrade = new ClientUpgrade(clientSocket, null, null, null, map,
        map.getPlayerByName(namePassword.get(0)));
    setNewLayout();
  }

  private ArrayList<Integer> getIndexList() {
    String indexString = textField.getText();
    String[] indexArray = indexString.split(" ");
    ArrayList<Integer> indexList = new ArrayList<>();
    for (String index : indexArray) {
      try{
        indexList.add(Integer.parseInt(index));
        resultText.setText("Added");
      }
      catch(Exception e){
        resultText.setText("Invalid input number: "+index);
      }
      
    }
    return indexList;
  }

  @FXML
  private void displayTerritoryInfo(Button button, Territory territory) {
    Iterator<Territory> iterator = territory.getNeighbours();
    String neighbors = StreamSupport.stream(
            Spliterators.spliteratorUnknownSize(iterator, Spliterator.ORDERED), false)
        .map(Territory::getName)
        .collect(Collectors.joining(", "));
    String content = "Territory: " + territory.getName() + "\n"
        + "Owner: " + map.getOwner(territory).getName() + "\n"
        + "Neighbors: " + neighbors + "\n";

//    Get units information
    StringBuilder units_info = new StringBuilder();
    Iterable<Unit> allUnits = territory.getUnits();
    for (Unit unit : allUnits) {
      if (unit == null) {
        units_info.append("null ");
      } else {
        units_info.append(unit.getName()).append(" ");
      }
    }
    content += "Units: " + units_info.toString() + "\n";

    Tooltip territoryTooltip = new Tooltip(content);
    territoryTooltip.setStyle("-fx-font-size: 14;");

    button.setOnMouseEntered(event -> {
      territoryTooltip.show(button, event.getScreenX(), event.getScreenY() + 15);
    });

    button.setOnMouseExited(event -> {
      territoryTooltip.hide();
    });
  }


  public void setTerrButtons(boolean isReset) {
    textField = new TextField();
    textField.setId("numText");
    gridPane.add(textField, 6, 7);

    for (Territory t : map.getAllTerritories()) {
      Button button = (Button) scene.lookup("#" + t.getName());
      if (map.getTerritoriesByPlayerName(namePassword.get(0)).contains(t)) {
        button.setStyle("-fx-background-color: blue ;");
      } else {
        button.setStyle("-fx-background-color: grey ;");
      }
      if (!isReset) {
        displayTerritoryInfo(button, t);
      }
      button.setOnAction(event -> handleTerritoryClick(button, t));
    }

  }

  @Override
  public void setNewLayout() throws IOException, ClassNotFoundException {
    System.out.println("maps num is " + map.getTerritoryNum());

    FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/playOneTurn.fxml"));
    loader.setController(this);
    Parent root = loader.load();
    URL cssResource = getClass().getResource("/style/mapButton.css");
    scene = new Scene(root, 900, 600);
    scene.getStylesheets().add(cssResource.toString());
    stage.setTitle("Play Game");
    stage.setScene(scene);
    setTerrButtons(false);
    stage.show();
  }

  private void filterClickableButtons(ArrayList<Territory> territories) {
    for (Territory t : map.getAllTerritories()) {
      Button button = (Button) scene.lookup("#" + t.getName());
      if (territories.contains(t)) {
        button.setStyle("-fx-background-color: green;");
        button.setOnAction(event -> handleTerritoryClick(button, t));
      } else {
        button.setStyle("-fx-background-color: grey;");
        button.setOnAction(event -> {
        });
      }
    }
  }

  private void handleTerritoryClick(Button button, Territory territory) {
    switch (currentMode) {
      case ATTACK:
      case MOVE:
        if (sourceTerritory == null) {
          sourceTerritory = territory;
          System.out.println("sourceTerritory is " + sourceTerritory.getName());
          button.setStyle("-fx-background-color: yellow;");
          if (currentMode == GameMode.ATTACK) {
            filterClickableButtons(clientAttack.findDestTerrs(sourceTerritory));
          } else if (currentMode == GameMode.MOVE) {
            filterClickableButtons(clientMove.findDestTerrs(sourceTerritory));
          }
        } else {
          destinationTerritory = territory;
          System.out.println("destinationTerritory is " + destinationTerritory.getName());
          if (currentMode == GameMode.ATTACK) {
            System.out.println("attack");
            clientAttack.perform_res(
                new ActionSender(sourceTerritory, destinationTerritory, getIndexList()));
          } else if (currentMode == GameMode.MOVE) {
            System.out.println("move");
            clientMove.perform_res(
                new ActionSender(sourceTerritory, destinationTerritory, getIndexList()));
          }
//          reset
          currentMode = GameMode.DEFAULT;
          sourceTerritory = null;
          setTerrButtons(true);
        }
        break;
      default:
        setTerrButtons(true);
        break;
    }
  }

  @FXML
  private void onAttackButtonClick(ActionEvent event) {
    currentMode = GameMode.ATTACK;
    sourceTerritory = null;
  }

  @FXML
  private void onMoveButtonClick(ActionEvent event) {
    currentMode = GameMode.MOVE;
    sourceTerritory = null;
  }

  @FXML
  private void defaultButtonClick(ActionEvent event) {
    currentMode = GameMode.DEFAULT;
    sourceTerritory = null;
    setTerrButtons(true);
  }

  @FXML
  private void onResearchButtonClick(ActionEvent event) {
    currentMode = GameMode.DEFAULT;
    sourceTerritory = null;
    setTerrButtons(true);
    clientResearch.perform();
  }

  @FXML
  private void doneButtonClick(ActionEvent event) throws IOException, ClassNotFoundException {
    currentMode = GameMode.DEFAULT;
    clientMove.commit();
    clientAttack.commit();
    clientResearch.commit();
    clientUpgrade.commit();
    refresh();
  }

  @FXML
  private void displayPlayerInfo() {
    Player player = map.getPlayerByName(namePassword.get(0));
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Player Information");
    alert.setHeaderText("Player: " + player.getName());

    String content = "Tech Resources: " + player.getTechResource() + "\n";
    content += "Tech Level: " + player.getTechLevel() + "\n";
    content += "Food Resources: " + player.getFoodResource() + "\n";

    alert.setContentText(content);
    alert.showAndWait();
  }

  @FXML
  public void back() throws IOException, ClassNotFoundException {
    RoomController rc = new RoomController(stage, clientSocket, namePassword);
    rc.resetConnection();
    GeneralScreen<RoomController> rs = new GeneralScreen<>(rc);
  }

}
