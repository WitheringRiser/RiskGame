package edu.duke.ece651.teamX.client;
import static org.junit.jupiter.api.Assertions.*;

import edu.duke.ece651.teamX.shared.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import org.junit.jupiter.api.Test;

public class ClientSpyMoveTest {
  @Test
  public void test_ClientSpyMove() throws IOException, ClassNotFoundException {
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
    ServerSocket ss = new ServerSocket(4499);
    Socket clientSocket = new Socket("localhost", 4499);
    Socket serverSocket = ss.accept();
    ClientSpyMove csm = new ClientSpyMove(clientSocket, my_map, p1);
    ArrayList<Spy> spies = new ArrayList<>();
    for (int i = 0; i < 100; i++) {
      spies.add(new Spy("Red"));
    }
    t4.addSpies(spies);
    ArrayList<Territory> ts = csm.findSourcTerritories();
    assertEquals(1, ts.size());
    ts = csm.findDestTerrs(t3);
    assertEquals(2, ts.size());
    csm.perform_res(t4, t3, "Spy", 1);
    assertThrows(IllegalArgumentException.class,
                 () -> csm.perform_res(t4, t3, "Spy", 100));
    assertThrows(IllegalArgumentException.class, () -> csm.perform_res(t3, t4, "Spy", 1));
  }
}
