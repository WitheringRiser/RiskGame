package edu.duke.ece651.teamX.client.view;

import edu.duke.ece651.teamX.client.controller.RoomController;
import java.io.IOException;
import javafx.scene.layout.GridPane;

public class RoomScreen extends GridPane {

  public RoomScreen(RoomController controller) throws IOException {
    controller.setNewLayout();

  }

}
