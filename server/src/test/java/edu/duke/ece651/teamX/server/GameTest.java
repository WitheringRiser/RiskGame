package edu.duke.ece651.teamX.server;

import static org.junit.jupiter.api.Assertions.*;

import edu.duke.ece651.teamX.shared.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
//import java.util.*;
import java.util.ArrayList;
import java.util.HashMap;
import org.junit.jupiter.api.Test;

public class GameTest {

  @Test
  public void test_GameBasic() {
    Game myGame = new Game(3, 20);
    Game equalGame = new Game(3, 20);
    assertEquals(myGame, equalGame);
    assertEquals(myGame.hashCode(), equalGame.hashCode());
    assertEquals(myGame, myGame);
    assertNotEquals(myGame, null);
    assertNotEquals(myGame, new Game(3, 19));
    assertEquals(3, myGame.getNumPlayer());
    ArrayList<Territory> terr_list1 = new ArrayList<Territory>();
    terr_list1.add(new Territory("aTerritory"));
    terr_list1.add(new Territory("bTerritory"));
    terr_list1.add(new Territory("cTerritory"));
    Player p1 = new Player("Red", 20);
    myGame.setGroupOwner(terr_list1, p1);
    assertNotEquals(myGame, equalGame);
    equalGame.setGroupOwner(terr_list1, p1);
    assertEquals(myGame, equalGame);

    assertEquals(3, myGame.getMap().getTerritoryNum());
    assertEquals(p1, myGame.getMap().getOwner(new Territory("aTerritory")));
    assertThrows(IllegalArgumentException.class,
        () -> myGame.setGroupOwner(terr_list1, p1));
  }

  @Test
  public void test_Group() {
    //Test parser for all number of players work corrctly
    for (int p_num = 2; p_num <= 4; p_num++) {
      Game myGame = new Game(p_num, 20);
      HashMap<Integer, ArrayList<Territory>> groups = myGame.setupGroup();
      assertEquals(p_num, groups.size());
      for (int i = 0; i < p_num; i++) {
        assertEquals(24 / p_num, groups.get(i).size());
      }
    }
  }

  @Test
  public void test_Create() throws IOException, ClassNotFoundException {
    Communicate communicate = new Communicate();
    ServerSocket ss = new ServerSocket(4445);
    Socket clientSocket = new Socket("localhost", 4445);
    Socket playerSocket = ss.accept();
    Game myGame = new Game(3, 20);
    myGame.createPlayer(playerSocket, "Red");
    Player recv_player = communicate.receivePlayer(clientSocket);

    int num = 0;
    communicate.sendInt(clientSocket, num);
    myGame.createMap();
    communicate.receiveObject(clientSocket);
    assertEquals(8, myGame.getMap().getTerritoryNum());
    //make sure the owner is set
    Player p1 = new Player("Red", 20);
    Player p2 = new Player("Blue", 20);
    assertEquals(true, myGame.getMap().getMap().containsValue(p1));
    assertEquals(false, myGame.getMap().getMap().containsValue(p2));

    myGame.playTurns();
    Map received_map = communicate.receiveMap(clientSocket);
    assertEquals(8, received_map.getTerritoryNum());
    GameResult gameRes = communicate.receiveGameResult(clientSocket);
    assertTrue(gameRes.isWin());
    assertEquals(p1, gameRes.getWinner());
    playerSocket.close();
    clientSocket.close();
    ss.close();

  }

  @Test
  public void Game_test_all() throws IOException {
    Game game = new Game(3, 20);
    Game equalGame = new Game(3, 20);
    ArrayList<Territory> terr_list1 = new ArrayList<Territory>();
    terr_list1.add(new Territory("aTerritory"));
    terr_list1.add(new Territory("bTerritory"));
    terr_list1.add(new Territory("cTerritory"));
    Player p1 = new Player("Red", 20);

    game.setGroupOwner(terr_list1, p1);
    equalGame.setGroupOwner(terr_list1, p1);
    ServerSocket ss = new ServerSocket(4484);
    Socket clientSocket = new Socket("localhost", 4484);
    Socket serverSocket = ss.accept();
    game.createPlayer(clientSocket, "Red");
    equalGame.createPlayer(clientSocket, "Red");
    assertEquals(game, equalGame);
    assertEquals(p1, game.getPlayerFromSocket(clientSocket));
    assertNull(game.getPlayerFromSocket(null));
    assertEquals(true, game.containsPlayer("Red"));
    assertEquals(false, game.containsPlayer("red"));
    assertEquals(true, game.hasWon());  //only one player->won directly
    assertEquals(true, game.checkIsEnd());
    assertEquals(false, game.checkHasBegin());
    assertNotEquals(game, equalGame);

    ArrayList<Territory> terr_list2 = new ArrayList<Territory>();
    terr_list2.add(new Territory("dTerritory"));
    terr_list2.add(new Territory("eTerritory"));
    terr_list2.add(new Territory("fTerritory"));
    assertFalse(game.setUnits(terr_list2));
    assertTrue(game.setUnits(terr_list1));
    clientSocket.close();
    ss.close();
    serverSocket.close();

  }

  @Test
  void checkRunError() {
    Game game = new Game(3, 20);
    game.run();
    assertTrue(game.checkIsEnd());
  }

  @Test
  public void getMoveSenders_test() {
    Game game = new Game(3, 20);
    ArrayList<ActionSender> allActions = new ArrayList<>();
    Territory source = new Territory("abc", 2);
    Territory dest = new Territory("def", 2);
    MoveSender move_sender = new MoveSender(source, dest, 1);
    AttackSender attack_sender = new AttackSender(source, dest, 1);
    allActions.add(move_sender);
    allActions.add(attack_sender);
    ArrayList<MoveSender> allMoves = new ArrayList<>();
    allMoves.add(move_sender);
    ArrayList<AttackSender> allAttacks = new ArrayList<>();
    allAttacks.add(attack_sender);
  }
}
