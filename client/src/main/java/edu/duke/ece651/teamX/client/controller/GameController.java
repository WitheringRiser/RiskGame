package edu.duke.ece651.teamX.client.controller;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GameController {

  private Stage stage;

  public GameController(Stage stage) {
    this.stage = stage;
  }

  public void setNewLayout() throws IOException {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/test.fxml"));
    loader.setController(this);
    Parent root = loader.load();
    Scene scene = new Scene(root, 640, 480);

    stage.setTitle("Login or Create Account");
    stage.setScene(scene);
    stage.show();

  }

}
