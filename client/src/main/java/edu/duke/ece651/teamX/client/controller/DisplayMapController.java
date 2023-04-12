package edu.duke.ece651.teamX.client.controller;

import edu.duke.ece651.teamX.shared.Map;
import edu.duke.ece651.teamX.shared.Territory;
import java.io.IOException;
import java.net.Socket;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class DisplayMapController implements Controller {

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


  public DisplayMapController(Stage stage, Socket socket, Map map) {
    this.stage = stage;
    this.socket = socket;
    this.map = map;
  }

  private void displayTerritoryInfo(Territory territory) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Territory Information");
    alert.setHeaderText("Territory: " + territory.getName());
    String content = "Owner: " + map.getOwner(territory).getName() + "\n"
        + "Neighbors: " + StreamSupport.stream(
            Spliterators.spliteratorUnknownSize(territory.getNeighbours(), Spliterator.ORDERED), false)
        .map(Territory::getName)
        .collect(Collectors.joining(", "));
    // Add other information as needed
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
