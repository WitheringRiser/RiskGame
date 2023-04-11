package edu.duke.ece651.teamX.client.view;

import edu.duke.ece651.teamX.client.controller.GameController;
import java.io.IOException;
import javafx.scene.layout.GridPane;

public class GameScreen extends GridPane {

  public GameScreen(GameController controller) throws IOException {
    controller.setNewLayout();
  }

}
