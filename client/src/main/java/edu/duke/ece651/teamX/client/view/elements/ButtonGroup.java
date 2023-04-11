package edu.duke.ece651.teamX.client.view.elements;

import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;

public class ButtonGroup {

  public static final String SUCCESS_STYLE = "success";
  private static final String BTN_STRING = "Btn";
  private static final String SPACE_STRING = "\\s+";
  private static final String EMPTY_STRING = "";

  private static final int DEFAULT_BTN_WIDTH = 130;
  private static final int DEFAULT_BTN_HEIGHT = 20;
  private static final int DEFAULT_TEXT_SIZE = 14;

  private List<ButtonBase> buttons;
  private Pane pane;
  private int width, height, textSize;


  public ButtonGroup(Pane pane) {
    this(pane, DEFAULT_BTN_WIDTH, DEFAULT_BTN_HEIGHT, DEFAULT_TEXT_SIZE);
  }

  public ButtonGroup(Pane pane, int width, int height) {
    this(pane, width, height, DEFAULT_TEXT_SIZE);
  }

  public ButtonGroup(Pane pane, int width, int height, int textSize) {
    this.pane = pane;
    this.width = width;
    this.height = height;
    this.textSize = textSize;
    buttons = new ArrayList<>();
  }

  /**
   * Method to add buttons to the button group
   *
   * @param labels is an array of String keys to create the buttons with. Keys must be in resource
   *               file of the ButtonGroup
   */
  public void addButtons(String... labels) {
    for (String label : labels) {
      ButtonBase btn = new Button();
      addButton(btn, label);
    }
    addButtonsToPane();
  }

  /**
   * Method to update the Pane associated with this ButtonGroup to have the new buttons
   */
  public void addButtonsToPane() {
    pane.getChildren().addAll(buttons);
  }

  /**
   * Method to set the button styles for all buttons contained within the button group. Default CSS
   * classes are listed as public variables for the ButtonGroup class
   *
   * @param styleClass is the string CSS class to set all the buttons to
   */
  public void setButtonStyles(String styleClass) {
    for (ButtonBase btn : buttons) {
      btn.getStyleClass().add(styleClass);
    }
  }

  public void setOnButtonPushed(EventHandler<ActionEvent> event) {
    setOnButtonPushed(event, 0);
  }

  /**
   * Method to assign an action to multiple buttons in the ButtonGroup
   */
  public void setOnButtonsPushed(EventHandler<ActionEvent> event, int... buttonIndices) {
    for (int i : buttonIndices) {
      setOnButtonPushed(event, i);
    }
  }

  /**
   * Method to assign an action to a specific button in the ButtonGroup
   */
  public void setOnButtonPushed(EventHandler<ActionEvent> event, int buttonIndex) {
    ButtonBase btn = buttons.get(buttonIndex);
    btn.setOnAction(event);
  }

  /**
   * Button configuration method to set the characteristics of every button and add them to the list
   * of buttons in the group
   */
  public void addButton(ButtonBase button, String label) {
    button.setAlignment(Pos.CENTER);
    button.setText(label);
    button.setId(label.replaceAll(SPACE_STRING, EMPTY_STRING) + BTN_STRING);
    button.setMinHeight(height);
    button.setPrefWidth(width);
    button.setFont(new Font(textSize));
    buttons.add(button);
  }

}
