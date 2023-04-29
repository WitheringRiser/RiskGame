package edu.duke.ece651.teamX.client.controller;

import com.sun.tools.classfile.ConstantPool.CPRefInfo;
import edu.duke.ece651.teamX.client.AIDecisionMaker;
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
import java.nio.file.*;

import edu.duke.ece651.teamX.client.ClientAttack;
import edu.duke.ece651.teamX.client.ClientBreaker;
import edu.duke.ece651.teamX.client.ClientCloak;
import edu.duke.ece651.teamX.client.ClientMove;
import edu.duke.ece651.teamX.client.ClientResearch;
import edu.duke.ece651.teamX.client.ClientShield;
import edu.duke.ece651.teamX.client.ClientSpyMove;
import edu.duke.ece651.teamX.client.ClientUpgrade;
import edu.duke.ece651.teamX.client.view.GeneralScreen;
import edu.duke.ece651.teamX.shared.Communicate;
import edu.duke.ece651.teamX.shared.FogView;
import edu.duke.ece651.teamX.shared.GameResult;
import edu.duke.ece651.teamX.shared.Map;
import edu.duke.ece651.teamX.shared.Player;
import edu.duke.ece651.teamX.shared.Territory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;

import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.media.Media;  
import javafx.scene.media.MediaPlayer;  
import javafx.scene.media.MediaView;  
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color; 

import java.io.File;



public class PlayTurnController implements Controller {

  private enum GameMode {
    DEFAULT, ATTACK, MOVE, UPGRADE, SPYMOVE, CLOAK, SHIELD, BREAKER
  }

  private GameMode currentMode = GameMode.DEFAULT;
  private Territory sourceTerritory = null;
  private Territory destinationTerritory = null;
  private HashMap<String, Integer> unitSetting = new HashMap<>();
  private HashMap<String, ArrayList<Integer>> unitUpgradeDic = new HashMap<>();
  private int shieldLevel = 0;
  private int breakerLevel = 0;
  TextField textField;
  TextField textField_toLevel;

  private boolean isLost = false;

  @FXML
  Text resultText;
  

  @FXML
  private GridPane gridPane;

  @FXML
  private Button cloakButton;

  protected ArrayList<String> namePassword;
  protected Socket clientSocket;
  protected Stage stage;
  protected Scene scene;
  protected FogView fogView;

  private Map map;
  private ClientAttack clientAttack;
  private ClientMove clientMove;
  private ClientResearch clientResearch;
  private ClientUpgrade clientUpgrade;
  private ClientSpyMove clientSpyMove;
  private ClientCloak clientCloak;
  private ClientShield clientShield;
  private ClientBreaker clientBreaker;
  GameResult gameResult;

  public PlayTurnController(Stage stage, Socket socket, ArrayList<String> namepassword)
      throws IOException, ClassNotFoundException {

    this.namePassword = namepassword;
    this.stage = stage;
    this.clientSocket = socket;
    this.frogView = new FrogView(namepassword.get(0));
    //resultText.setFill(Color.IVORY); 
    refresh();
  }

  private void refresh() throws IOException, ClassNotFoundException {
    map = Communicate.receiveMap(clientSocket);
    gameResult = Communicate.receiveGameResult(clientSocket);

    if (gameResult.loserContainsByPlayerNmae(namePassword.get(0))) {
      isLost = true;
      resultText.setText("You Lost!");
    }
    if (gameResult.isWin()) {
      openWinWindow(gameResult);
    }

    System.out.println("player name = " + namePassword.get(0));
    fogView.refreshMap(map);
    this.clientAttack = new ClientAttack(clientSocket, map,
        map.getPlayerByName(namePassword.get(0)));
    this.clientMove = new ClientMove(clientSocket, map, map.getPlayerByName(namePassword.get(0)));
    this.clientResearch = new ClientResearch(clientSocket,
        map.getPlayerByName(namePassword.get(0)));
    this.clientUpgrade = new ClientUpgrade(clientSocket, map,
        map.getPlayerByName(namePassword.get(0)));
    this.clientSpyMove = new ClientSpyMove(clientSocket, map,
        map.getPlayerByName(namePassword.get(0)));
    this.clientCloak = new ClientCloak(clientSocket, map, map.getPlayerByName(namePassword.get(0)));
    this.clientShield = new ClientShield(clientSocket, map,
        map.getPlayerByName(namePassword.get(0)));
    this.clientBreaker = new ClientBreaker(clientSocket, map,
        map.getPlayerByName(namePassword.get(0)));
    setNewLayout();
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
    content += fogView.getTerrInfo(territory.getName());
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
        if (t.getShieldLevel() == 0 && t.getBreakerLevel() == 0) {
          button.setStyle("-fx-background-color: darkslateblue ;");
        } else if (t.getShieldLevel() == 0) {
          button.setStyle("-fx-background-color: indianred ;");
        } else if (t.getBreakerLevel() == 0) {
          button.setStyle("-fx-background-color: gold ;");
        } else {
          button.setStyle("-fx-background-color: orange ;");
        }
        // Change color when pressed
        button.setOnMousePressed(e -> button.setStyle("-fx-background-color: darkblue;"));
        button.setOnAction(event -> {
          try {
            handleTerritoryClick(button, t);
          } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
          }
        });
      } else {
        button.setOnAction(event -> {
        });
        button.setStyle("-fx-background-color: grey ;");
      }
      if (!isReset) {
        displayTerritoryInfo(button, t);
      }
    }

    //AudioClip sound = new AudioClip("sounds/background_music_game.mp3");
    //sound.play();

    
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

    Image image = new Image("photos/MenuBackground.png");
    gridPane.setBackground(new Background(new BackgroundImage(image,BackgroundRepeat.NO_REPEAT,
                                                              BackgroundRepeat.NO_REPEAT,
                                                              BackgroundPosition.DEFAULT,
                                                              new BackgroundSize(1.0, 1.0, true, true, false, false))));

    

    
    

    frogView.refreshMap(map);
    setTerrButtons(false);
    if (!isLost) {
      setCloakButtons();
    }
    stage.show();

    if (isLost) {
      refresh();
    }
  }

  private void setCloakButtons() {
    Player player = map.getPlayerByName(namePassword.get(0));
    if (player.getCanCloak()) {
      cloakButton.setText("Cloak");
      cloakButton.setOnAction(event -> {
        currentMode = GameMode.CLOAK;
        filterClickableButtons(clientCloak.getSourcTerritories());
        sourceTerritory = null;
      });
    } else {
      cloakButton.setText("Unlock Cloak");
      cloakButton.setOnAction(event -> {
        try {
          clientResearch.perform(true);
        } catch (IllegalArgumentException iae) {
          resultText.setText(iae.getMessage());
        }
      });

    }
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
        case CLOAK:
          sourceTerritory = territory;
          clientCloak.perform(sourceTerritory);
          sourceTerritory = null;
          setNewLayout();
          break;
        case UPGRADE:
          sourceTerritory = territory;
          openUnitsWindow();
          clientUpgrade.perform(territory, unitUpgradeDic);
          unitUpgradeDic.clear();
          sourceTerritory = null;
          setNewLayout();
          break;
        case SHIELD:
          sourceTerritory = territory;
          openLevelWindow();
          clientShield.perform(territory, this.shieldLevel);
          this.shieldLevel = 0;
          sourceTerritory = null;
          setNewLayout();
          break;
        case BREAKER:
          sourceTerritory = territory;
          openLevelWindow();
          clientBreaker.perform(territory, this.breakerLevel);
          this.breakerLevel = 0;
          sourceTerritory = null;
          setNewLayout();
          break;
        case ATTACK:
        case MOVE:
        case SPYMOVE:
          if (sourceTerritory == null) {
            sourceTerritory = territory;
            System.out.println("sourceTerritory is " + sourceTerritory.getName());
            button.setStyle("-fx-background-color: yellow;");
            openUnitsWindow();
            if (currentMode == GameMode.ATTACK) {
              filterClickableButtons(clientAttack.findDestTerrs(sourceTerritory));
            } else if (currentMode == GameMode.MOVE) {
              filterClickableButtons(clientMove.findDestTerrs(sourceTerritory));
            } else if (currentMode == GameMode.SPYMOVE) {
              filterClickableButtons(clientSpyMove.findDestTerrs(territory));
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
            } else if (currentMode == GameMode.SPYMOVE) {
              clientSpyMove.perform(sourceTerritory, destinationTerritory, unitSetting);
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
  private void onShieldButtonClick(ActionEvent event) {
    if (isLost) {
      resultText.setText("You have lost the game, please wait for others to finish");
      return;
    }
    resultText.setText("Please select a source Territory to add a shield on");
    currentMode = GameMode.SHIELD;
    sourceTerritory = null;
  }

  @FXML
  private void onBreakerButtonClick(ActionEvent event) {
    if (isLost) {
      resultText.setText("You have lost the game, please wait for others to finish");
      return;
    }
    resultText.setText("Please select a source Territory to add a breaker on");
    currentMode = GameMode.BREAKER;
    sourceTerritory = null;
  }

  @FXML
  private void onAttackButtonClick(ActionEvent event) {
    if (isLost) {
      resultText.setText("You have lost the game, please wait for others to finish");
      return;
    }
    resultText.setText("Please select a source Territory to start attack");
    currentMode = GameMode.ATTACK;
    sourceTerritory = null;
  }

  @FXML
  private void onMoveButtonClick(ActionEvent event) {
    if (isLost) {
      resultText.setText("You have lost the game, please wait for others to finish");
      return;
    }
    resultText.setText("Please select a source Territory to start move");
    currentMode = GameMode.MOVE;
    sourceTerritory = null;
  }

  @FXML
  private void defaultButtonClick(ActionEvent event) {
    if (isLost) {
      resultText.setText("You have lost the game, please wait for others to finish");
      return;
    }
    currentMode = GameMode.DEFAULT;
    sourceTerritory = null;
    setTerrButtons(true);
  }

  @FXML
  private void onResearchButtonClick(ActionEvent event) {
    if (isLost) {
      resultText.setText("You have lost the game, please wait for others to finish");
      return;
    }
    currentMode = GameMode.DEFAULT;
    sourceTerritory = null;
    setTerrButtons(true);
    try {
      clientResearch.perform(false);
    } catch (IllegalArgumentException iae) {
      resultText.setText(iae.getMessage());
    }

  }

  @FXML
  private void onUpgradeButtonClick(ActionEvent event) {
    if (isLost) {
      resultText.setText("You have lost the game, please wait for others to finish");
      return;
    }
    resultText.setText("Please select a source Territory to start upgrade");
    currentMode = GameMode.UPGRADE;
    sourceTerritory = null;
  }

  @FXML
  private void onSpyMove(ActionEvent event) {
    if (isLost) {
      resultText.setText("You have lost the game, please wait for others to finish");
      return;
    }
    currentMode = GameMode.SPYMOVE;
    ArrayList<Territory> findRes = clientSpyMove.findSourcTerritories();
    if (findRes.size() > 0) {
      resultText.setText("Please select a source Territory that has your spies");
    } else {
      resultText.setText("You do not have spies available, please use upgrade to raise spies");
    }
    filterClickableButtons(findRes);
    sourceTerritory = null;
  }

  @FXML
  private void doneButtonClick(ActionEvent event) throws IOException, ClassNotFoundException {
    if (isLost) {
      resultText.setText("You have lost the game, please wait for others to finish");
      return;
    }
    currentMode = GameMode.DEFAULT;
    endOneTurn();
    refresh();
  }

  private void endOneTurn() throws IOException {
    clientMove.commit();
    clientAttack.commit();
    clientResearch.commit();
    clientUpgrade.commit();
    clientSpyMove.commit();
    clientCloak.commit();
    clientShield.commit();
    clientBreaker.commit();
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
    content += "Gold: " + player.getGoldResource() + "\n";

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

  private void addLevelSetter(GridPane gridPane) {
    gridPane.setAlignment(Pos.CENTER);
    gridPane.add(new Label("Shield description:"), 0, 0, GridPane.REMAINING, 1);
    gridPane.add(new Label("Level"), 0, 1);
    gridPane.add(new Label("Cost of Gold"), 1, 1);
    gridPane.add(new Label("Enemy it Kills"), 2, 1);

    int cost = 50;
    for (int i = 1; i <= 4; ++i) {
      gridPane.add(new Label(String.valueOf(i)), 0, i + 1);
      gridPane.add(new Label(String.valueOf(cost)), 1, i + 1);
      gridPane.add(new Label(String.valueOf(i * 25) + "%"), 2, i + 1);
      cost = cost * 2;
    }

    ComboBox<Integer> comboBox = getComboBox(1, 4, 1);
    comboBox.setId("ShiledLevel");
    gridPane.add(new Label("Level you want"), 0, 6);
    gridPane.add(comboBox, 1, 6, GridPane.REMAINING, 1);
  }

  private void addBreakerSetter(GridPane gridPane) {
    gridPane.setAlignment(Pos.CENTER);
    gridPane.add(new Label("Breaker description:"), 0, 0, GridPane.REMAINING, 1);
    gridPane.add(new Label("Level"), 0, 1);
    gridPane.add(new Label("Cost of Gold"), 1, 1);
    gridPane.add(new Label("Shield it breaks"), 2, 1);

    int cost = 25;
    String shieldString = "";
    String split = "";
    for (int i = 1; i <= 4; ++i) {
      shieldString += (split + String.valueOf(i));
      gridPane.add(new Label(String.valueOf(i)), 0, i + 1);
      gridPane.add(new Label(String.valueOf(cost)), 1, i + 1);
      gridPane.add(new Label(shieldString), 2, i + 1);
      cost = cost * 2;
      split = ", ";
    }

    ComboBox<Integer> comboBox = getComboBox(1, 4, 1);
    comboBox.setId("BreakerLevel");
    gridPane.add(new Label("Level you want"), 0, 6);
    gridPane.add(comboBox, 1, 6, GridPane.REMAINING, 1);
  }

  private void addSpySetter(GridPane gridPane) {
    Label nameTitle = new Label("Type");
    Label amountTitle = new Label("Amount");
    gridPane.add(nameTitle, 0, 0);
    gridPane.add(amountTitle, 1, 0);
    Label levelLabel = new Label("Spy:");
    ComboBox<Integer> comboBox = getComboBox(0,
        sourceTerritory.getSpyMoveIndsFromPlayer(namePassword.get(0)).size(),
        0);
    comboBox.setId("Spies");
    gridPane.add(levelLabel, 0, 1);
    gridPane.add(comboBox, 1, 1);

  }

  private void addToLevelSetter(Stage unitsStage, GridPane gridPane) {
    Label levelTitle = new Label("To Level");
    gridPane.add(levelTitle, 2, 0);
    for (int level = 0; level <= 6; level++) {
      ComboBox<Integer> comboBox = getComboBox(level, 6, level);
      comboBox.setId("toLevel" + level);
      gridPane.add(comboBox, 2, level + 1);
      if (level > 0) {
        Button SpyButton = new Button("spy_" + level);
        gridPane.add(SpyButton, 3, level + 1);
        SpyButton.setText("To Spy");
        SpyButton.setOnAction(event -> {
          unitSetting.clear();
          for (int l = 0; l <= 6; l++) {
            ComboBox<Integer> comboBox2 = (ComboBox<Integer>) gridPane.lookup(
                "#unitLevel" + l);
            int selectedUnits = comboBox2.getValue();
            ArrayList<Integer> upInfo = new ArrayList<>();
            upInfo.add(selectedUnits);
            upInfo.add(-1);
            unitUpgradeDic.put("level_" + l + "_unit", upInfo);
          }
          unitsStage.close();
        });
      }
    }
  }

  private void LevelSaveButton(Stage unitsStage, GridPane gridPane, String name, boolean isShield) {
    Button saveButton = new Button("Save");
    gridPane.add(saveButton, 1, 9);
    saveButton.setOnAction(event -> {
      ComboBox<Integer> comboBox = (ComboBox<Integer>) gridPane.lookup(name);
      if (isShield) {
        this.shieldLevel = comboBox.getValue();
      } else {
        this.breakerLevel = comboBox.getValue();
      }
      unitsStage.close();
    });
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

  private void addSpySaveButton(Stage unitsStage, GridPane gridPane) {
    Button saveButton = new Button("Save");
    gridPane.add(saveButton, 2, 4);
    saveButton.setOnAction(event -> {
      unitSetting.clear();
      ComboBox<Integer> comboBox = (ComboBox<Integer>) gridPane.lookup("#Spies");
      int selectedUnits = comboBox.getValue();
      unitSetting.put("Spy", selectedUnits);
      unitsStage.close();
    });
  }

  private void openLevelWindow() {
    Stage unitsStage = new Stage();
    unitsStage.initModality(Modality.APPLICATION_MODAL);
    unitsStage.setTitle("Select level of tool you want to buy");
    GridPane gridPane = new GridPane();
    gridPane.setVgap(10);
    gridPane.setHgap(10);
    gridPane.setPadding(new Insets(10, 10, 10, 10));
    if (currentMode == GameMode.SHIELD) {
      addLevelSetter(gridPane);
      LevelSaveButton(unitsStage, gridPane, "#ShiledLevel", true);
    } else {
      addBreakerSetter(gridPane);
      LevelSaveButton(unitsStage, gridPane, "#BreakerLevel", false);
    }

    Scene unitsScene = new Scene(gridPane);
    unitsStage.setScene(unitsScene);
    unitsStage.showAndWait();
  }

  private void openWinWindow(GameResult gameResult) {
    Stage winStage = new Stage();
    winStage.initModality(Modality.APPLICATION_MODAL);
    winStage.setTitle("Game Result");
    Label label = new Label(gameResult.getWinner().getName() + " wins!");
    GridPane gridPane = new GridPane();
    gridPane.setVgap(10);
    gridPane.setHgap(10);
    gridPane.setPadding(new Insets(10, 10, 10, 10));
    gridPane.add(label, 0, 0);

    Button exitButton = new Button("Exit Game");
    exitButton.setOnAction(event -> {
      System.exit(0);
    });
    gridPane.add(exitButton, 0, 1);

    Scene scene = new Scene(gridPane);
    winStage.setScene(scene);
    winStage.showAndWait();
  }

  private void openUnitsWindow() {
    Stage unitsStage = new Stage();
    unitsStage.initModality(Modality.APPLICATION_MODAL);
    unitsStage.setTitle("Set Units");
    GridPane gridPane = new GridPane();
    gridPane.setVgap(10);
    gridPane.setHgap(10);
    gridPane.setPadding(new Insets(10, 10, 10, 10));
    if (currentMode == GameMode.SPYMOVE) {
      addSpySetter(gridPane);
    } else {
      addUnitSetter(gridPane);
    }

    if (currentMode == GameMode.UPGRADE) {
      addToLevelSetter(unitsStage, gridPane);
    }
    if (currentMode == GameMode.SPYMOVE) {
      addSpySaveButton(unitsStage, gridPane);
    } else {
      addSaveButton(unitsStage, gridPane);
    }

    Scene unitsScene = new Scene(gridPane);
    unitsStage.setScene(unitsScene);
    unitsStage.showAndWait();
  }

  @FXML
  private void makeAIDecision() throws IOException, ClassNotFoundException {
    if (isLost) {
      resultText.setText("You have lost the game, please wait for others to finish");
      return;
    }
    AIDecisionMaker aiDecisionMaker = new AIDecisionMaker(this.map, this.clientAttack,
        this.clientMove, this.clientResearch, this.clientUpgrade,
        map.getPlayerByName(namePassword.get(0)));
    aiDecisionMaker.make_decision();
    endOneTurn();
    refresh();
  }
}
