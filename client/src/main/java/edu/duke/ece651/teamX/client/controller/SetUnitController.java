package edu.duke.ece651.teamX.client.controller;

import java.io.IOException;

import edu.duke.ece651.teamX.client.view.GeneralScreen;
import edu.duke.ece651.teamX.shared.*;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.net.URL;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.control.Label;
import javafx.scene.Parent;

import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundSize;

public class SetUnitController implements Controller {
    // @FXML
    TextField textField;
    @FXML
    Text resultText;
    @FXML
    private GridPane gridPane;

    protected ArrayList<String> namePassword;
    protected Socket clientSocket;
    protected Stage stage;
    protected Scene scene;
    private ArrayList<Territory> territories;
    private int remainUnit;

    public SetUnitController(Stage st, Socket cs, ArrayList<String> np, ArrayList<Territory> ts, int r) {
        namePassword = np;
        stage = st;
        clientSocket = cs;
        territories = ts;
        remainUnit = r;
    }

    @FXML
    public void setUnit(ActionEvent ae) throws IOException, ClassNotFoundException {
        Object source = ae.getSource();
        Button btn = (Button) source;
        String terrName = btn.getId();
        String uerIn = textField.getText();
        try {
            int number = Integer.parseInt(uerIn);
            if (number < 0 || number > remainUnit) {
                throw new IllegalArgumentException("");
            }
            for (Territory t : territories) {
                if (t.getName().equals(terrName)) {
                    t.addUnits(number,namePassword.get(0));
                    btn.setText(t.getName()+"   #Units: "+String.valueOf(t.getUnitsNumber()));
                    break;
                }
            }

            remainUnit -= number;
            resultText.setText("Success");
            if (remainUnit == 0) {
                resultText.setText("Set units done! Please wait...");
                Communicate.sendObject(clientSocket, territories);
                PlayTurnController playTurnController = new PlayTurnController(stage, clientSocket, namePassword);
                GeneralScreen<PlayTurnController> generalScreen = new GeneralScreen<>(playTurnController);
            }
        } catch (Exception e) {
            resultText.setText(" Please input a valid positive number <= " + remainUnit);
        }

    }

    public void displayTerr() {
        textField = new TextField();
        textField.setId("numText");
        gridPane.add(textField, 5, 7);
        for (Territory t : territories) {
            Button button = (Button) scene.lookup("#" + t.getName());
            button.setStyle("-fx-background-color: darkslateblue ;");
            button.setOnAction(event -> {
                try {
                    setUnit(event);
                } catch (Exception e) {
                }
            });
        }
    }

    public void setNewLayout() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/displaymap.fxml"));
        loader.setController(this);
        Parent root = loader.load();
        resultText.setText("Please input units in text box and click on a colored Territory");
        URL cssResource = getClass().getResource("/style/mapButton.css");
        scene = new Scene(root, 900, 600);
        scene.getStylesheets().add(cssResource.toString());
        stage.setTitle("Set Units");
        stage.setScene(scene);
        displayTerr();

        Image image = new Image("photos/MenuBackground.png");
        gridPane.setBackground(new Background(new BackgroundImage(image,BackgroundRepeat.NO_REPEAT,
                                                              BackgroundRepeat.NO_REPEAT,
                                                              BackgroundPosition.DEFAULT,
                                                              new BackgroundSize(1.0, 1.0, true, true, false, false))));

        stage.show();

    }

}
