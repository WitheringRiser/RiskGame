package edu.duke.ece651.teamX.client.controller;

import edu.duke.ece651.teamX.client.*;
import edu.duke.ece651.teamX.client.view.*;
import edu.duke.ece651.teamX.shared.*;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.swing.text.LabelView;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PlayTurnController implements Controller {

  private enum GameMode {
    DEFAULT, ATTACK, MOVE, UPGRADE
  }

  private GameMode currentMode = GameMode.DEFAULT;
  private Territory sourceTerritory = null;
  private Territory destinationTerritory = null;
  private HashMap<String, Integer> unitSetting = new HashMap<>();
  private HashMap<String, ArrayList<Integer>> unitUpgradeDic = new HashMap<>();

  TextField textField;
  TextField textField_toLevel;

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

    this.clientAttack = new ClientAttack(clientSocket, map,
        map.getPlayerByName(namePassword.get(0)));
    this.clientMove = new ClientMove(clientSocket, map, map.getPlayerByName(namePassword.get(0)));
    this.clientResearch = new ClientResearch(clientSocket,
        map.getPlayerByName(namePassword.get(0)));
    this.clientUpgrade = new ClientUpgrade(clientSocket, map,
        map.getPlayerByName(namePassword.get(0)));
    setNewLayout();
  }

  private ArrayList<Integer> getIndexList() {
    String indexString = textField.getText();
    String[] indexArray = indexString.split(" ");
    ArrayList<Integer> indexList = new ArrayList<>();
    for (String index : indexArray) {
      try {
        indexList.add(Integer.parseInt(index));
        resultText.setText("Added");
      } catch (Exception e) {
        resultText.setText("Invalid input number: " + index);
      }
    }
    return indexList;
  }

  // TODO:just stub function
  private String getUnitName() {
    String name = "level_0_unit";
    return name;
  }

  // TODO:just stub function
  private int getUnitNum() {
    int num = 1;
    return num;
  }

  @FXML
  private void displayTerritoryInfo(Button button, Territory territory) {
    Iterator<Territory> iterator = territory.getNeighbours();
    String neighbors = StreamSupport.stream(
        Spliterators.spliteratorUnknownSize(iterator, Spliterator.ORDERED), false)
        .map(Territory::getName)
        .collect(Collectors.joining(", "));
    String content = "Territory: " + territory.getName() + "\n"
        + "Neighbors: " + neighbors + "\n"
        + "Size: " + territory.getTerritorySize() + "\n";

    // TODO: change to frogView
    content += "Owner: " + map.getOwner(territory).getName() + "\n";
    content += "Units:\n";
    HashMap<String, ArrayList<Integer>> unitDic = territory.getUnitsDit();
    for (String typeName : unitDic.keySet()) {
      content += "  - " + typeName + ": " + unitDic.get(typeName).size() + "\n";
    }
    Tooltip territoryTooltip = new Tooltip(content);
    territoryTooltip.setStyle("-fx-font-size: 14;");
    territoryTooltip.setStyle("-fx-wrap-text: true;");

    button.setOnMouseEntered(event -> {
      territoryTooltip.show(button, event.getScreenX(), event.getScreenY() + 15);
    });

    button.setOnMouseExited(event -> {
      territoryTooltip.hide();
    });
  }

  public void setTerrButtons(boolean isReset) {
    for (Territory t : map.getAllTerritories()) {
      Button button = (Button) scene.lookup("#" + t.getName());
      if (map.getTerritoriesByPlayerName(namePassword.get(0)).contains(t)) {
        button.setStyle("-fx-background-color: blue ;");
        // Change color when pressed
        button.setOnMousePressed(e -> button.setStyle("-fx-background-color: darkblue;"));

      } else {
        button.setStyle("-fx-background-color: grey ;");
      }
      if (!isReset) {
        displayTerritoryInfo(button, t);
      }
      button.setOnAction(event -> {
        try {
          handleTerritoryClick(button, t);
        } catch (IOException | ClassNotFoundException e) {
          throw new RuntimeException(e);
        }
      });
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
        button.setOnAction(event -> {
          try {
            handleTerritoryClick(button, t);
          } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
          }
        });
      } else {
        button.setStyle("-fx-background-color: grey;");
        button.setOnAction(event -> {
        });
      }
    }
  }

  private void handleTerritoryClick(Button button, Territory territory)
      throws IOException, ClassNotFoundException {
    try {
      switch (currentMode) {
        case UPGRADE:
        sourceTerritory=territory;
          openUnitsWindow();
          clientUpgrade.perform(territory, unitUpgradeDic);
          unitUpgradeDic.clear();
          sourceTerritory=null;
          setNewLayout();
          break;
        case ATTACK:
        case MOVE:
          if (sourceTerritory == null) {
            sourceTerritory = territory;
            System.out.println("sourceTerritory is " + sourceTerritory.getName());
            button.setStyle("-fx-background-color: yellow;");
            openUnitsWindow();
            if (currentMode == GameMode.ATTACK) {
              filterClickableButtons(clientAttack.findDestTerrs(sourceTerritory));
            } else if (currentMode == GameMode.MOVE) {
              filterClickableButtons(clientMove.findDestTerrs(sourceTerritory));
            }
          } else {
            destinationTerritory = territory;
            System.out.println("destinationTerritory is " + destinationTerritory.getName());
            if (currentMode == GameMode.ATTACK) {
              clientAttack.perform(sourceTerritory, destinationTerritory, unitSetting);
              unitSetting.clear();
            } else if (currentMode == GameMode.MOVE) {
              clientMove.perform(sourceTerritory, destinationTerritory, unitSetting);
              unitSetting.clear();
            }
            // reset and get new temporary map
            currentMode = GameMode.DEFAULT;
            sourceTerritory = null;
            // setTerrButtons(true);
            setNewLayout();
          }
          break;
        default:
          setTerrButtons(true);
          break;
      }

    } catch (IllegalArgumentException iae) {
      resultText.setText(iae.getMessage());
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
    try {
      clientResearch.perform();
    } catch (IllegalArgumentException iae) {
      resultText.setText(iae.getMessage());
    }

  }

  @FXML
  private void onUpgradeButtonClick(ActionEvent event) {
    currentMode = GameMode.UPGRADE;
    sourceTerritory = null;
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

  private ComboBox<Integer> getComboBox(int minV, int maxV, int defaultV) {
    ComboBox<Integer> comboBox = new ComboBox<>();
    ObservableList<Integer> options = FXCollections.observableArrayList();
    if (sourceTerritory == null) {
      options.add(0);
    } else {
      for (int i = minV; i <= maxV; i++) {
        options.add(i);
      }
    }
    comboBox.setItems(options);
    comboBox.setValue(defaultV);
    return comboBox;
  }

  private void addUnitSetter(GridPane gridPane) {
    Label nameTitle = new Label("Type");
    Label amountTitle = new Label("Amount");
    gridPane.add(nameTitle, 0, 0);
    gridPane.add(amountTitle, 1, 0);
    for (int level = 0; level <= 6; level++) {
      Label levelLabel = new Label("Level " + level + ":");
      ComboBox<Integer> comboBox = getComboBox(0, sourceTerritory.getUnitCountByLevel(level), 0);
      comboBox.setId("unitLevel" + level);
      gridPane.add(levelLabel, 0, level + 1);
      gridPane.add(comboBox, 1, level + 1);
    }
  }

  private void addToLevelSetter(GridPane gridPane) {
    Label levelTitle = new Label("To Level");
    gridPane.add(levelTitle, 2, 0);
    for (int level = 0; level <= 6; level++) {
      ComboBox<Integer> comboBox = getComboBox(level, 6, level);
      comboBox.setId("toLevel" + level);
      gridPane.add(comboBox, 2, level + 1);
    }
  }

  private void addSaveButton(Stage unitsStage, GridPane gridPane) {
    Button saveButton = new Button("Save");
    gridPane.add(saveButton, 1, 9);
    saveButton.setOnAction(event -> {
      unitSetting.clear();
      for (int level = 0; level <= 6; level++) {

        ComboBox<Integer> comboBox = (ComboBox<Integer>) gridPane.lookup(
            "#unitLevel" + level);
        int selectedUnits = comboBox.getValue();
        if (currentMode == GameMode.ATTACK || currentMode == GameMode.MOVE) {
          unitSetting.put("level_" + level + "_unit", selectedUnits);
        } else if (currentMode == GameMode.UPGRADE) {
          ComboBox<Integer> comboBox2 = (ComboBox<Integer>) gridPane.lookup(
              "#toLevel" + level);
          int toLevel = comboBox2.getValue();
          ArrayList<Integer> upInfo = new ArrayList<>();
          upInfo.add(selectedUnits);
          upInfo.add(toLevel);
          unitUpgradeDic.put("level_" + level + "_unit", upInfo);
        }
      }
      unitsStage.close();
    });
  }

  private void openUnitsWindow() {
    Stage unitsStage = new Stage();
    unitsStage.initModality(Modality.APPLICATION_MODAL);
    unitsStage.setTitle("Set Units");
    GridPane gridPane = new GridPane();
    gridPane.setVgap(10);
    gridPane.setHgap(10);
    gridPane.setPadding(new Insets(10, 10, 10, 10));
    addUnitSetter(gridPane);
    if (currentMode == GameMode.UPGRADE) {
      addToLevelSetter(gridPane);
    }
    addSaveButton(unitsStage, gridPane);

    Scene unitsScene = new Scene(gridPane);
    unitsStage.setScene(unitsScene);
    unitsStage.showAndWait();
  }
}
