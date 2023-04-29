package edu.duke.ece651.teamX.client.controller;

import edu.duke.ece651.teamX.shared.Communicate;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import edu.duke.ece651.teamX.client.view.GeneralScreen;
import java.net.URL;

import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundSize;
/*
 * For client to choose from:
 * 0 Start a new game
 * 1 Join a new game
 * 2 Enter a active game
 */
public class RoomController implements Controller {

  private ArrayList<String> namePassword;
  private Socket clientSocket;
  private Stage stage;
  @FXML
  Text resultText;

    /**
     * 
     * @param st the stage
     * @param cs the client socket created outside
     * @param np the name password Arraylist
     */
    public RoomController(Stage st, Socket cs, ArrayList<String> np) {
        stage = st;
        clientSocket = cs;
        namePassword = np;
        
    }

  /**
   * Reconnect to the server to indicate choose game room again
   *
   * @throws IOException
   * @throws ClassNotFoundException
   */
  public void resetConnection() throws IOException, ClassNotFoundException {
    clientSocket.close();
    clientSocket = new Socket("localhost", 4444);
    Communicate.sendInt(clientSocket, 1);
    Communicate.sendObject(clientSocket, namePassword);
    String mes = (String) Communicate.receiveObject(clientSocket);
    if (mes.length() != 0) {
      throw new IllegalArgumentException("Invalid username and password: " + mes);
    }
  }

  public void setNewLayout() throws IOException {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/roomcontrol.fxml"));
    loader.setController(this);
    Parent root = loader.load();
    URL cssResource = getClass().getResource("/style/roomButton.css");
    Scene scene = new Scene(root, 640, 480);
    scene.getStylesheets().add(cssResource.toString());
    stage.setTitle("Entering the Game");
    stage.setScene(scene);
    stage.show();
  }

  @FXML
  public void createNewGame(ActionEvent ae) throws IOException, ClassNotFoundException {
    Communicate.sendInt(clientSocket, 0);
    CreateGameController cc = new CreateGameController(stage, clientSocket, namePassword);
    GeneralScreen<CreateGameController> rs = new GeneralScreen<>(cc);
    // resultText.setText("Create a new Game");

  }

  @FXML
  public void JoinNewGame(ActionEvent ae) throws IOException, ClassNotFoundException {
    Communicate.sendInt(clientSocket, 1);
    EnterGameController ec = new EnterGameController(stage, clientSocket, namePassword);
    GeneralScreen<EnterGameController> gs = new GeneralScreen<EnterGameController>(ec);

  }

  @FXML
  public void EnterMyGame(ActionEvent ae) throws IOException, ClassNotFoundException {
    Communicate.sendInt(clientSocket, 2);
    EnterGameController ec = new EnterGameController(stage, clientSocket, namePassword);
    GeneralScreen<EnterGameController> gs = new GeneralScreen<EnterGameController>(ec);

  }

}
