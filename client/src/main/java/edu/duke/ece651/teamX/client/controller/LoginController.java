package edu.duke.ece651.teamX.client.controller;

import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import edu.duke.ece651.teamX.shared.Communicate;

public class LoginController {
  @FXML
  TextField usernameField;
  @FXML
  PasswordField passwordField;
  @FXML
  Text resultText;
  private ArrayList<String> namePassword;
  private Socket clientSocket;

  public LoginController(Socket socket) {
    clientSocket = socket;
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
    } else {
      resultText.setText(mes);
    }

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
