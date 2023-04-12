package edu.duke.ece651.teamX.client.view;
import java.io.IOException;

import edu.duke.ece651.teamX.client.controller.Controller;
import javafx.scene.layout.GridPane;
import java.io.IOException;
public class GeneralScreen <T extends Controller> extends GridPane{

    public GeneralScreen(T controller) throws IOException {
        controller.setNewLayout();
      }
    
}
