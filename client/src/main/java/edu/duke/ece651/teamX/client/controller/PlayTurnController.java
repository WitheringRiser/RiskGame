package edu.duke.ece651.teamX.client.controller;

import edu.duke.ece651.teamX.client.ClientAction;
import edu.duke.ece651.teamX.client.ClientAttack;
import edu.duke.ece651.teamX.client.ClientMove;
import edu.duke.ece651.teamX.client.ClientResearch;
import edu.duke.ece651.teamX.client.ClientUpgrade;
import edu.duke.ece651.teamX.shared.ActionSender;
import edu.duke.ece651.teamX.shared.Communicate;
import edu.duke.ece651.teamX.shared.GameResult;
import edu.duke.ece651.teamX.shared.Map;
import edu.duke.ece651.teamX.shared.Territory;
import edu.duke.ece651.teamX.shared.Unit;
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
      indexList.add(Integer.parseInt(index));
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
      units_info.append(unit.getName()).append(" ");
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
    gridPane.add(textField, 5, 7);

    for (Territory t : map.getAllTerritories()) {
      Button button = (Button) scene.lookup("#" + t.getName());
      if (map.getTerritoriesByPlayerName(namePassword.get(0)).contains(t)) {
        button.setStyle("-fx-background-color: blue ;");
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
    stage.setTitle("Set Units");
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
          filterClickableButtons(clientMove.findDestTerrs(sourceTerritory));
        } else {
          destinationTerritory = territory;
          System.out.println("destinationTerritory is " + destinationTerritory.getName());
          if (currentMode == GameMode.ATTACK) {
            System.out.println("attack");
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
  private void doneButtonClick(ActionEvent event) throws IOException, ClassNotFoundException {
    currentMode = GameMode.DEFAULT;
    clientMove.commit();
    clientAttack.commit();
    clientResearch.commit();
    clientUpgrade.commit();
    refresh();
  }

}
