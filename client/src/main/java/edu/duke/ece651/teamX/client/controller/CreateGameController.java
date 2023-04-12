package edu.duke.ece651.teamX.client.controller;

import java.io.IOException;

import edu.duke.ece651.teamX.client.view.GeneralScreen;
import edu.duke.ece651.teamX.shared.Communicate;
import java.net.Socket;
import java.util.ArrayList;

import java.net.URL;

import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class CreateGameController implements Controller {

    protected ArrayList<String> namePassword;
    protected Socket clientSocket;
    protected Stage stage;

    public CreateGameController(Stage st, Socket cs, ArrayList<String> np) {
        namePassword = np;
        clientSocket = cs;
        stage = st;
    }

    public void setNewLayout() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/creategame.fxml"));
        loader.setController(this);
        Parent root = loader.load();
        URL cssResource = getClass().getResource("/style/roomButton.css");
        Scene scene = new Scene(root, 640, 480);        
        scene.getStylesheets().add(cssResource.toString());
        stage.setTitle("Create a New Game");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void back() throws IOException, ClassNotFoundException{
        RoomController rc = new RoomController(stage,clientSocket,namePassword);
        rc.resetConnection();
        // RoomScreen rs = new RoomScreen(rc);
        GeneralScreen<RoomController> rs = new GeneralScreen<>(rc);
    } 

    @FXML
    public void onNumberButton(ActionEvent ae) throws IOException, ClassNotFoundException, NumberFormatException {
      Object source = ae.getSource();
      if (source instanceof Button) {
        Button btn = (Button)source;
        int playerNum = Integer.parseInt(btn.getText());
        Communicate.sendInt(clientSocket, playerNum);
      }
      else {
        throw new IllegalArgumentException("Invalid source " + source + " for ActionEvent");
      }
    }

}
