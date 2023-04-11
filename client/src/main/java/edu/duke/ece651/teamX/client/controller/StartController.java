package edu.duke.ece651.teamX.client.controller;

import edu.duke.ece651.teamX.client.view.StartScreen;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;

@Deprecated
public class StartController {

  private Stage stage;

  public StartController(Stage stage) {
    this.stage = stage;
  }

  public void setNewLayout(Pane layout) {
    Scene scene = new Scene(layout, layout.getWidth(), layout.getHeight());
    stage.setScene(scene);
    stage.hide();
    Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
    stage.setX((screenBounds.getWidth() - layout.getWidth()) / 2);
    stage.setY((screenBounds.getHeight() - layout.getHeight()) / 2);
    stage.show();
  }

}
