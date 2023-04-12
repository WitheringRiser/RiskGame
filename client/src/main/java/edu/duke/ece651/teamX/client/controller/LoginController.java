package edu.duke.ece651.teamX.client.controller;

import edu.duke.ece651.teamX.client.view.GeneralScreen;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import edu.duke.ece651.teamX.shared.Communicate;
import javafx.stage.Stage;

public class LoginController implements Controller {

  @FXML
  TextField usernameField;
  @FXML
  PasswordField passwordField;
  @FXML
  Text resultText;
  private ArrayList<String> namePassword;
  private Socket clientSocket;
  private Stage stage;

  public LoginController(Socket socket, Stage stage) throws IOException {
    clientSocket = socket;
    this.stage = stage;
  }

  private void formNamePassword() {
    namePassword = new ArrayList<String>();
    String un = usernameField.getText();
    String ps = passwordField.getText();
    namePassword.add(un);
    namePassword.add(ps);
  }

  private void authentication(int choice) throws IOException, ClassNotFoundException {
    // String mes = "button pressed";
    formNamePassword();
    Communicate.sendInt(clientSocket, choice);
    Communicate.sendObject(clientSocket, namePassword);
    String mes = (String) Communicate.receiveObject(clientSocket);
    // System.out.println(mes);
    if (mes.length() == 0) {
      resultText.setText("Login success");
      RoomController rc = new RoomController(stage,clientSocket,namePassword);
      GeneralScreen<RoomController> rs = new GeneralScreen<>(rc);
      // GameController gc = new GameController(stage);
      // GameScreen gs = new GameScreen(gc);
    } else {
      resultText.setText(mes);
    }

  }

  public void setNewLayout() throws IOException {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/Login.fxml"));
    loader.setController(this);
    Parent root = loader.load();
    Scene scene = new Scene(root, 640, 480);

    stage.setTitle("Login or Create Account");
    stage.setScene(scene);
    stage.show();
  }

  @FXML
  public void onLogin(ActionEvent ae) throws IOException, ClassNotFoundException {
    authentication(1);
  }

  @FXML
  public void onCreateAccount(ActionEvent ae) throws IOException, ClassNotFoundException {
    authentication(0);
  }

  public ArrayList<String> getNamePassword() {
    return namePassword;
  }
}
