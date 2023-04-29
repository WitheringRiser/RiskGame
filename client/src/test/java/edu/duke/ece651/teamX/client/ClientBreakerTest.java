package edu.duke.ece651.teamX.client;

import static org.junit.jupiter.api.Assertions.*;

import edu.duke.ece651.teamX.shared.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import org.junit.jupiter.api.Test;

public class ClientBreakerTest {
  @Test
  public void test_ClientBreaker() throws IOException, ClassNotFoundException {
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
    ServerSocket ss = new ServerSocket(4694);
    Socket clientSocket = new Socket("localhost", 4694);
    Socket serverSocket = ss.accept();
    ClientBreaker cb = new ClientBreaker(clientSocket, my_map, p1);
    cb.perform(t1, 1);
    cb.commit();
    assertThrows(IllegalArgumentException.class, () -> cb.perform(t2, 3));
  }
}
