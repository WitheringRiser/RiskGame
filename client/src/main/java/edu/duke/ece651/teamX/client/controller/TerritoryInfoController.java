package edu.duke.ece651.teamX.client.controller;

import edu.duke.ece651.teamX.shared.Map;
import edu.duke.ece651.teamX.shared.Player;
import edu.duke.ece651.teamX.shared.Territory;
import java.io.IOException;
import java.net.Socket;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class TerritoryInfoController implements Controller {

  private class TerritoryButton extends Circle {

    private Territory territory;
    private int x;
    private int y;
    private int radius;

    public TerritoryButton(Territory territory, int x, int y, int radius) {
      super(x, y, radius);
      setFill(Color.BLUE);
      setRadius(radius);
      this.territory = territory;
      this.x = x;
      this.y = y;
      this.radius = radius;
      setOnMouseClicked(event -> displayTerritoryInfo(territory));
    }


    public Territory getTerritory() {
      return territory;
    }

    public int getX() {
      return x;
    }

    public int getY() {
      return y;
    }

  }

  private Stage stage;
  private Socket socket;
  private Map map;


  public TerritoryInfoController(Stage stage, Socket socket, Map map) {
    this.stage = stage;
    this.socket = socket;
    this.map = map;
  }

  private void displayTerritoryInfo(Territory territory) {
    Dialog<Void> territoryDialog = new Dialog<>();
    territoryDialog.setTitle("Territory Information");

    VBox contentBox = new VBox();
    contentBox.setSpacing(10);

    Label territoryName = new Label("Territory: " + territory.getName());
    contentBox.getChildren().add(territoryName);

    Player owner = map.getOwner(territory);
    Label ownerName = new Label("Owner: " + owner.getName());
    contentBox.getChildren().add(ownerName);

    Iterator<Territory> iterator = territory.getNeighbours();
    String neighbors = StreamSupport.stream(
            Spliterators.spliteratorUnknownSize(iterator, Spliterator.ORDERED), false)
        .map(Territory::getName)
        .collect(Collectors.joining(", "));
    Label neighborList = new Label("Neighbors: " + neighbors);
    contentBox.getChildren().add(neighborList);

    territoryDialog.getDialogPane().setContent(contentBox);

    ButtonType moreInfoButtonType = new ButtonType("Player info", ButtonBar.ButtonData.LEFT);
    territoryDialog.getDialogPane().getButtonTypes().addAll(moreInfoButtonType, ButtonType.CLOSE);

    territoryDialog.setResultConverter(buttonType -> {
      if (buttonType == moreInfoButtonType) {
        displayPlayerInfo(owner);
      }
      return null;
    });

    territoryDialog.showAndWait();


  }

  private void displayPlayerInfo(Player player) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Player Information");
    alert.setHeaderText("Player: " + player.getName());

    String content = "Tech Resources: " + player.getTechResource() + "\n";
    content += "Tech Level: " + player.getTechLevel() + "\n";
    // Add other player information as needed

    alert.setContentText(content);
    alert.showAndWait();
  }

  @Override
  public void setNewLayout() throws IOException {
    Pane pane = new Pane();
    int i = 0;
    for (Territory terr : map.getAllTerritories()) {
      TerritoryButton territoryButton = new TerritoryButton(terr, 10 + i * 15, 10, 10);
      pane.getChildren().add(territoryButton);
      i++;
    }
    Scene scene = new Scene(pane, 640, 480);
    stage.setScene(scene);
    stage.setTitle("Territory Map");
    stage.show();
  }
}