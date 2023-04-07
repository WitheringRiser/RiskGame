package edu.duke.ece651.teamX.server;
import static org.junit.jupiter.api.Assertions.*;

import edu.duke.ece651.teamX.shared.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import org.junit.jupiter.api.Test;

public class AdminTest {
  @Test
  public void test_createAccountLogin() throws IOException, ClassNotFoundException {
    HashMap<String, String> namePasswordDic = new HashMap<String, String>();
    ArrayList<Game> GameList = new ArrayList<Game>();
    Communicate communicate = new Communicate();
    ServerSocket ss = new ServerSocket(4447);
    Socket clientSocket = new Socket("localhost", 4447);
    Socket playerSocket = ss.accept();
    Admin adm = new Admin(namePasswordDic, GameList, playerSocket);
    ArrayList<String> newUP1 = new ArrayList<String>();
    newUP1.add("Red");
    newUP1.add("12345");
    ArrayList<String> newUP2 = new ArrayList<String>();
    newUP2.add("Blue");
    newUP2.add("12345");
    ArrayList<String> newUP3 = new ArrayList<String>();
    newUP3.add("Red");
    newUP3.add("1234");
    ArrayList<String> newUP4 = new ArrayList<String>();
    newUP4.add("Green");
    newUP4.add("1234");
    communicate.sendObject(clientSocket, newUP1);
    adm.createAccount();
    assertEquals("", (String)communicate.receiveObject(clientSocket));

    communicate.sendObject(clientSocket, newUP3);
    communicate.sendObject(clientSocket, newUP2);
    adm.createAccount();
    assertEquals("Username already exist",
                 (String)communicate.receiveObject(clientSocket));
    assertEquals("", (String)communicate.receiveObject(clientSocket));
    communicate.sendObject(clientSocket, newUP3);
    communicate.sendObject(clientSocket, newUP4);
    communicate.sendObject(clientSocket, newUP1);
    adm.login();
    assertEquals("Password incorrect", (String)communicate.receiveObject(clientSocket));
    assertEquals("Invalid username", (String)communicate.receiveObject(clientSocket));
    assertEquals("", (String)communicate.receiveObject(clientSocket));
    playerSocket.close();
    clientSocket.close();
    ss.close();
  }
  @Test
  public void test_createRoom() throws IOException, ClassNotFoundException {
    HashMap<String, String> namePasswordDic = new HashMap<String, String>();
    ArrayList<Game> GameList = new ArrayList<Game>();
    Communicate communicate = new Communicate();
    ServerSocket ss = new ServerSocket(4448);
    Socket clientSocket = new Socket("localhost", 4448);
    Socket playerSocket = ss.accept();
    Admin adm = new Admin(namePasswordDic, GameList, playerSocket);
    communicate.sendInt(clientSocket, 3);
    adm.createNewRoom("Red0");
    Player p = communicate.receivePlayer(clientSocket);
    assertEquals("Red0", p.getName());
    assertEquals(1, GameList.size());
    assertTrue(GameList.get(0).containsPlayer("Red0"));
    assertEquals(1, GameList.get(0).getActualNumPlayer());
    assertEquals(3, GameList.get(0).getNumPlayer());
    clientSocket.close();
    playerSocket.close();
    ss.close();
  }
}
