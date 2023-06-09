package edu.duke.ece651.teamX.client;

import static org.junit.jupiter.api.Assertions.*;

import edu.duke.ece651.teamX.shared.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import org.junit.jupiter.api.Test;
public class ClientAttackTest {
  @Test
  public void test_ClientAttack() throws IOException, ClassNotFoundException {
    Map my_map = new Map();
    Territory t1 = new Territory("Desert", 30);
    Territory t2 = new Territory("Swamp", 3);
    Territory t3 = new Territory("Beach", 30);
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
    ServerSocket ss = new ServerSocket(4495);
    Socket clientSocket = new Socket("localhost", 4495);
    Socket serverSocket = ss.accept();
    ClientAttack ca = new ClientAttack(clientSocket, my_map, p1);
    ArrayList<Territory> ts = ca.findDestTerrs(t3);
    assertEquals(ts.size(), 1);
    assertThrows(IllegalArgumentException.class, () -> ca.findDestTerrs(t4));
    int init_food = p1.getFoodResource();
    HashMap<String, Integer> unit_dict = new HashMap<>();
    unit_dict.put("level_0_unit", 2);
    ca.perform(t3, t4, unit_dict);
    ca.commit();
    assertEquals(28, t3.getUnitsNumber());
    unit_dict.put("level_0_unit", 28);
    assertThrows(IllegalArgumentException.class,
                 () -> ca.perform_res(t3, t4, "level_0_unit", 28));
  }
}
