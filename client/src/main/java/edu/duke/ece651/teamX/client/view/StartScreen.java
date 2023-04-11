package edu.duke.ece651.teamX.client.view;

import edu.duke.ece651.teamX.client.Util;
import edu.duke.ece651.teamX.client.controller.StartController;
import edu.duke.ece651.teamX.client.view.elements.ButtonGroup;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;

public class StartScreen extends GridPane {

  private static final int SCREEN_WIDTH = 450;
  private static final int SCREEN_HEIGHT = 150;
  private static final int SCREEN_SPACING = 20;
  private static final int START_BTN_WIDTH = 140;
  private static final int START_BTN_HEIGHT = 30;
  private static final String LAYOUT_ID = "Layout";

  public StartScreen(StartController controller) {
    setAlignment(Pos.CENTER);
    setWidth(SCREEN_WIDTH);
    setHeight(SCREEN_HEIGHT);
    setId(LAYOUT_ID);

    add(getStartButtonGroup(controller), 0, 0);
    controller.setNewLayout(this);
    Util.applyStyleSheet(this);
  }

  private VBox getStartButtonGroup(StartController controller) {
    VBox result = new VBox();
    result.setAlignment(Pos.CENTER);
    result.setSpacing(SCREEN_SPACING);

    ButtonGroup startButton = new ButtonGroup(result, START_BTN_WIDTH, START_BTN_HEIGHT);
    startButton.addButtons("Login");
    startButton.setButtonStyles(ButtonGroup.SUCCESS_STYLE);

    return result;
  }

}

