package edu.duke.ece651.teamX.client;

import static org.junit.jupiter.api.Assertions.*;

import edu.duke.ece651.teamX.shared.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import org.junit.jupiter.api.Test;

public class ClientShieldTest {
  @Test
  public void test_ClientShield() throws IOException, ClassNotFoundException {
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
    ServerSocket ss = new ServerSocket(4594);
    Socket clientSocket = new Socket("localhost", 4594);
    Socket serverSocket = ss.accept();
    ClientShield cs = new ClientShield(clientSocket, my_map, p1);
    cs.perform(t1, 1);
    cs.commit();
    assertThrows(IllegalArgumentException.class, () -> cs.perform(t2, 2));
  }
}
