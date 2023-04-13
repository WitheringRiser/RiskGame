package edu.duke.ece651.teamX.client.controller;

import java.io.IOException;

import edu.duke.ece651.teamX.client.view.GeneralScreen;
import edu.duke.ece651.teamX.shared.*;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.net.URL;

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

public class SelectGroupController implements Controller {
    protected ArrayList<String> namePassword;
    protected Socket clientSocket;
    protected Stage stage;
    protected Scene scene;
    private HashMap<Integer, ArrayList<Territory>> territories;
    @FXML
    Text resultText;

    public SelectGroupController(Stage st, Socket cs, ArrayList<String> np,
            HashMap<Integer, ArrayList<Territory>> tsm) {
        namePassword = np;
        clientSocket = cs;
        stage = st;
        territories = tsm;
    }

    @FXML
    public void chooseGroup(ActionEvent ae) throws IOException, ClassNotFoundException {
        Object source = ae.getSource();

        Button btn = (Button) source;
        String terrName = btn.getId();
        ArrayList<Territory> ts ;
        for (int i : territories.keySet()) {
            ts= territories.get(i);
            for (Territory t : ts) {
                if (t.getName().equals(terrName)) {
                    Communicate.sendInt(clientSocket, i);
                    SetUnitController suc = new SetUnitController(stage, clientSocket, namePassword, ts, 20);
                    GeneralScreen<SetUnitController> rs = new GeneralScreen<>(suc);
                    break;
                }
            }
        }
        
    }

    public void displayGroups() {
        String colors[] = { "red", "blue", "yellow", "green" };
        for (int i : territories.keySet()) {
            ArrayList<Territory> ts = territories.get(i);
            for (Territory t : ts) {
                Button button = (Button) scene.lookup("#" + t.getName());
                button.setStyle("-fx-background-color: " + colors[i] + ";");
                button.setOnAction(event -> {
                    try {
                        chooseGroup(event);
                    } catch (Exception e) {
                    }
                });
            }
        }

    }

    public void setNewLayout() throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/displaymap.fxml"));
            loader.setController(this);
            Parent root = loader.load();
            resultText.setText("Please Choose a Colored Territory Group by Clicking");
            URL cssResource = getClass().getResource("/style/mapButton.css");
            scene = new Scene(root, 900, 600);
            scene.getStylesheets().add(cssResource.toString());
            stage.setTitle("Choose Territory");
            stage.setScene(scene);
            displayGroups();
            stage.show();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}
