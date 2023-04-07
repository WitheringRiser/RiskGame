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
  @Test
  public void test_JoinActiveRoom() throws IOException, ClassNotFoundException {
    HashMap<String, String> namePasswordDic = new HashMap<String, String>();
    ArrayList<Game> GameList = new ArrayList<Game>();
    Communicate communicate = new Communicate();
    ServerSocket ss = new ServerSocket(4449);
    Socket clientSocket1 = new Socket("localhost", 4449);
    Socket playerSocket1 = ss.accept();
    Admin adm1 = new Admin(namePasswordDic, GameList, playerSocket1);
    communicate.sendInt(clientSocket1, 3);
    communicate.sendInt(clientSocket1, 2);
    communicate.sendInt(clientSocket1, 4);
    communicate.sendInt(clientSocket1, 2);
    adm1.createNewRoom("Red");
    adm1.createNewRoom("Red");
    adm1.createNewRoom("Red");
    adm1.createNewRoom("Blue");
    Socket clientSocket2 = new Socket("localhost", 4449);
    Socket playerSocket2 = ss.accept();
    Admin adm2 = new Admin(namePasswordDic, GameList, playerSocket2);
    communicate.sendInt(clientSocket2, 1);
    adm2.joinActiveRoom("Red");
    ArrayList<RoomSender> searchRes =
        (ArrayList<RoomSender>)communicate.receiveObject(clientSocket2);
    assertEquals(3, searchRes.size());
    Player p = new Player("Red", 20);
    assertEquals(playerSocket2, GameList.get(1).getPlayerSocket(p));
    GameList.get(3).hasWon();
    communicate.sendInt(clientSocket2, 0);
    adm2.joinActiveRoom("Blue");
    assertEquals(playerSocket1, GameList.get(3).getPlayerSocket(new Player("Blue", 20)));

    clientSocket1.close();
    clientSocket2.close();
    playerSocket1.close();
    playerSocket2.close();
    ss.close();
  }
  @Test
  public void test_joinNewRoo()
      throws IOException, ClassNotFoundException, InterruptedException {
    HashMap<String, String> namePasswordDic = new HashMap<String, String>();
    ArrayList<Game> GameList = new ArrayList<Game>();
    Communicate communicate = new Communicate();
    ServerSocket ss = new ServerSocket(4450);
    Socket clientSocket1 = new Socket("localhost", 4450);
    Socket playerSocket1 = ss.accept();
    Admin adm1 = new Admin(namePasswordDic, GameList, playerSocket1);
    communicate.sendInt(clientSocket1, 3);
    communicate.sendInt(clientSocket1, 2);
    communicate.sendInt(clientSocket1, 4);
    communicate.sendInt(clientSocket1, 2);
    adm1.createNewRoom("Red");
    adm1.createNewRoom("Red");
    adm1.createNewRoom("Red");
    adm1.createNewRoom("Blue");
    Socket clientSocket2 = new Socket("localhost", 4450);
    Socket playerSocket2 = ss.accept();
    Admin adm2 = new Admin(namePasswordDic, GameList, playerSocket2);
    communicate.sendInt(clientSocket2, 0);
    adm2.joinNewRoom("Blue");
    ArrayList<RoomSender> searchRes =
        (ArrayList<RoomSender>)communicate.receiveObject(clientSocket2);
    assertEquals(3, searchRes.size());
    assertEquals(2, GameList.get(0).getActualNumPlayer());
    communicate.sendInt(clientSocket2, 0);
    // communicate.sendInt(clientSocket2, 0);
    //communicate.sendInt(clientSocket1, 1);
    // communicate.sendInt(clientSocket1, 1);
    adm2.joinNewRoom("Blue");

    assertEquals(2, GameList.get(1).getActualNumPlayer());
    Thread.sleep(1000);  //wait 1 second for game to process
    assertTrue(GameList.get(1).checkHasBegin());
  }
}
