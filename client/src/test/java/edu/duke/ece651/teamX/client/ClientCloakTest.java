package edu.duke.ece651.teamX.client;

import static org.junit.jupiter.api.Assertions.*;

import edu.duke.ece651.teamX.shared.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import org.junit.jupiter.api.Test;

public class ClientCloakTest {
  @Test
  public void test_ClientCloak() throws IOException, ClassNotFoundException {
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
    ServerSocket ss = new ServerSocket(4590);
    Socket clientSocket = new Socket("localhost", 4590);
    Socket serverSocket = ss.accept();
    ClientCloak cc = new ClientCloak(clientSocket, my_map, p1);
    ArrayList<Territory> ts = cc.getSourcTerritories();
    assertEquals(3, ts.size());
    t2.addCloak();
    ts = cc.getSourcTerritories();
    assertEquals(2, ts.size());
    assertThrows(IllegalArgumentException.class, () -> cc.perform(t1));
    p1.upgradeLevel();
    p1.upgradeLevel();
    p1.increaseAllResource(CloakProcessor.getUnlockCloakCost() - p1.getTechResource());
    p1.unlockCloak();
    assertThrows(IllegalArgumentException.class, () -> cc.perform(t1));
    p1.increaseAllResource(CloakProcessor.getCloakCost());
    cc.perform(t1);
    cc.commit();
    assertThrows(IllegalArgumentException.class, () -> cc.perform(t1));
  }
}
