package edu.duke.ece651.teamX.server;

import static org.junit.jupiter.api.Assertions.*;

import edu.duke.ece651.teamX.shared.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import org.junit.jupiter.api.Test;

public class GameTest {

  @Test
  public void test_GameBasic() {
    Game myGame = new Game(3, 20);
    assertEquals(3, myGame.getNumPlayer());
    ArrayList<Territory> terr_list1 = new ArrayList<Territory>();
    terr_list1.add(new Territory("aTerritory"));
    terr_list1.add(new Territory("bTerritory"));
    terr_list1.add(new Territory("cTerritory"));
    Player p1 = new Player("Red", 20);
    myGame.setGroupOwner(terr_list1, p1);
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
    int num = 0;
    communicate.sendInt(clientSocket, num);
    myGame.createMap();
    assertEquals(8, myGame.getMap().getTerritoryNum());
    //make sure the owner is set
    Player p1 = new Player("Red", 20);
    Player p2 = new Player("Blue", 20);
    assertEquals(true, myGame.getMap().getMap().containsValue(p1));
    assertEquals(false, myGame.getMap().getMap().containsValue(p2));
  }
}
