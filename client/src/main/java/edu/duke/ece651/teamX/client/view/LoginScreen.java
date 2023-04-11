package edu.duke.ece651.teamX.client.view;

import edu.duke.ece651.teamX.client.controller.LoginController;
import java.io.IOException;
import javafx.scene.layout.GridPane;

public class LoginScreen extends GridPane {

  public LoginScreen(LoginController controller) throws IOException {
    controller.setNewLayout();

  }

}
