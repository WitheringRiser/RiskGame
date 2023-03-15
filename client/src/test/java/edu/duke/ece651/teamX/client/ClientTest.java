package edu.duke.ece651.teamX.client;
import static org.junit.jupiter.api.Assertions.*;

import edu.duke.ece651.teamX.shared.*;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import org.junit.jupiter.api.Test;
public class ClientTest {
  @Test
  public void test_DisplayMap() throws IOException, ClassNotFoundException {
    Map my_map = new Map();
    Territory t1 = new Territory("A");
    Territory t2 = new Territory("B");
    Territory t3 = new Territory("C");
    Territory t4 = new Territory("D");
    t1.addNeighbors(t2);
    t1.addNeighbors(t3);
    t2.addNeighbors(t4);
    t3.addNeighbors(t4);
    Player p1 = new Player("Red", 20);
    Player p2 = new Player("Blue", 20);
    my_map.addTerritory(t1, p1);
    my_map.addTerritory(t2, p1);
    my_map.addTerritory(t3, p2);
    my_map.addTerritory(t4, p2);
    t1.addUnits(null, 2);
    t2.addUnits(null, 3);
    t3.addUnits(null, 1);

    Communicate communicate = new Communicate();
    ServerSocket ss = new ServerSocket(4444);
    Socket clientSocket = new Socket("localhost", 4444);
    Socket serverSocket = ss.accept();
    BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    PrintStream output = new PrintStream(bytes, true);

    Client client = new Client(clientSocket, input, output);
    communicate.sendMap(serverSocket, my_map);
    client.receiveMap();
    client.displayMap();
    String expected = "Red player:\n"
                      + "-----------\n"
                      + "2 units in A (next to: B, C)\n"
                      + "3 units in B (next to: A, D)\n\n"
                      + "Blue player:\n"
                      + "-----------\n"
                      + "1 units in C (next to: A, D)\n"
                      + "0 units in D (next to: B, C)\n\n";
    assertEquals(expected, bytes.toString());
  }
  /**
  @Test
  public void test_init() {
ArrayList<Territory> terr_list = new ArrayList<Territory>();
Territory a = new Territory("A");
    Territory b = new Territory("B");
    Territory c = new Territory("C");
    Territory d = new Territory("D");
    

  }
  */
}
