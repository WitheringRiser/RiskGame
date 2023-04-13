package edu.duke.ece651.teamX.client.controller;

import edu.duke.ece651.teamX.shared.Communicate;
import edu.duke.ece651.teamX.shared.Map;
import edu.duke.ece651.teamX.shared.Territory;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class PlayTurnController implements Controller {

  private enum GameMode {
    DEFAULT, ATTACK, MOVE
  }


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

  public PlayTurnController(Stage stage, Socket socket, ArrayList<String> namepassword) {
    this.namePassword = namepassword;
    this.stage = stage;
    this.clientSocket = socket;
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
        + "Neighbors: " + neighbors;

    Tooltip territoryTooltip = new Tooltip(content);
    territoryTooltip.setStyle("-fx-font-size: 14;");

    button.setOnMouseEntered(event -> {
      territoryTooltip.show(button, event.getScreenX(), event.getScreenY() + 15);
    });

    button.setOnMouseExited(event -> {
      territoryTooltip.hide();
    });
  }


  public void displayTerr() {
    textField = new TextField();
    textField.setId("numText");
    gridPane.add(textField, 5, 7);
    for (Territory t : map.getTerritoriesByPlayerName(namePassword.get(0))) {
      Button button = (Button) scene.lookup("#" + t.getName());
      button.setStyle("-fx-background-color: blue ;");
      displayTerritoryInfo(button, t);
    }
  }

  @Override
  public void setNewLayout() throws IOException, ClassNotFoundException {
    map = Communicate.receiveMap(clientSocket);
    System.out.println("maps num is " + map.getTerritoryNum());

    FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/playOneTurn.fxml"));
    loader.setController(this);
    Parent root = loader.load();
    URL cssResource = getClass().getResource("/style/mapButton.css");
    scene = new Scene(root, 900, 600);
    scene.getStylesheets().add(cssResource.toString());
    stage.setTitle("Set Units");
    stage.setScene(scene);
    displayTerr();
    stage.show();

  }
}
