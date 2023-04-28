package edu.duke.ece651.teamX.client;

import static org.junit.jupiter.api.Assertions.*;

import edu.duke.ece651.teamX.shared.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import org.junit.jupiter.api.Test;
public class ClientMoveTest {
  @Test
  public void test_ClientMove() throws IOException, ClassNotFoundException {
    Map my_map = new Map();
    Territory t1 = new Territory("Desert", 30);
    Territory t2 = new Territory("Swamp", 3);
    Territory t3 = new Territory("Beach", 3);
    Territory t4 = new Territory("Plains", 3);
    Player p1 = new Player("Red", 10);
    Player p2 = new Player("Green", 10);
    t1.addNeighbors(t2);
    t2.addNeighbors(t3);
    t3.addNeighbors(t4);
    my_map.addTerritory(t1, p1);
    my_map.addTerritory(t2, p1);
    my_map.addTerritory(t3, p1);
    my_map.addTerritory(t4, p2);
    Communicate communicate = new Communicate();
    ServerSocket ss = new ServerSocket(4494);
    Socket clientSocket = new Socket("localhost", 4494);
    Socket serverSocket = ss.accept();
    ClientMove cm = new ClientMove(clientSocket, my_map, p1);
    ArrayList<Territory> ts = cm.findDestTerrs(t1);
    assertEquals(ts.size(), 2);
    assertThrows(IllegalArgumentException.class, () -> cm.findDestTerrs(t4));
    ts = cm.findOwnTerr();
    assertEquals(ts.size(), 3);
    int init_food = p1.getFoodResource();
    HashMap<String, Integer> unit_dict = new HashMap<>();
    unit_dict.put("level_0_unit", 2);
    cm.perform(t1, t3, unit_dict);
    unit_dict.put("level_0_unit", 0);
    cm.perform(t1, t3, unit_dict);
    assertEquals(p1.getFoodResource(), init_food - 18 * 2);
    unit_dict.put("level_0_unit", 28);
    assertThrows(IllegalArgumentException.class, () -> cm.perform(t1, t3, unit_dict));
    cm.commit();
    assertThrows(IllegalArgumentException.class,
                 () -> cm.perform_res(t1, t3, "level_0_unit", 28));
  }
}
