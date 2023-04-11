package edu.duke.ece651.teamX.client;

import edu.duke.ece651.teamX.client.controller.StartController;
import edu.duke.ece651.teamX.client.view.StartScreen;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

  private static final boolean RESIZABLE_WINDOW = false;
  private static final String WINDOW_NAME = "RISC Game";

  @Override
  public void start(Stage primaryStage) throws Exception {
    setUpStage(primaryStage);
    startGame(primaryStage);
  }

  private void startGame(Stage stage) {
    StartController controller = new StartController(stage);
    StartScreen startScreen = new StartScreen(controller);
  }

  private void setUpStage(Stage stage) {
    stage.setResizable(RESIZABLE_WINDOW);
    stage.setTitle(WINDOW_NAME);
  }

  public static void main(String[] args) {
    launch(args);
  }
}
