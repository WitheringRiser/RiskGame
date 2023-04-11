package edu.duke.ece651.teamX.client;

import javafx.scene.layout.Pane;

public class Util {

  private static final String STYLE_SHEET = "resources/style/default.css";

  public static void applyStyleSheet(Pane pane) {
    pane.getStylesheets().add(STYLE_SHEET);
  }

}
