package edu.duke.ece651.teamX.client.controller;

import edu.duke.ece651.teamX.shared.*;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import edu.duke.ece651.teamX.client.view.GeneralScreen;
import edu.duke.ece651.teamX.client.*;
import java.net.URL;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class WaitController implements Controller {

  private ArrayList<String> namePassword;
  private Socket clientSocket;
  private Stage stage;

  public WaitController(Stage st, Socket cs, ArrayList<String> np) {
    stage = st;
    clientSocket = cs;
    namePassword = np;
  }

  public void setNewLayout() throws IOException, ClassNotFoundException {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/waitingpage.fxml"));
    loader.setController(this);
    Parent root = loader.load();
    Scene scene = new Scene(root, 640, 480);

    stage.setTitle("Waiting");
    stage.setScene(scene);
    stage.show();

    // A seperate thread to receive information from socket
    new Thread(() -> {
      try {
        Player player = Communicate.receivePlayer(clientSocket);
        HashMap<Integer, ArrayList<Territory>> ts = (HashMap<Integer, ArrayList<Territory>>) Communicate
            .receiveObject(clientSocket);
        Platform.runLater(() -> {// to update the UI in event thread
          try {
            SelectGroupController sgc = new SelectGroupController(stage, clientSocket, namePassword,
                ts);
            GeneralScreen<SelectGroupController> sgs = new GeneralScreen<>(sgc);
          } catch (Exception e) {
          }
        });

      } catch (Exception e) {
      }
    }).start();

  }

  @FXML
  public void back() throws IOException, ClassNotFoundException {
    RoomController rc = new RoomController(stage, clientSocket, namePassword);
    rc.resetConnection();
    GeneralScreen<RoomController> rs = new GeneralScreen<>(rc);
  }
}
