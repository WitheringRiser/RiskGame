package edu.duke.ece651.teamX.client;

import static org.junit.jupiter.api.Assertions.*;

import edu.duke.ece651.teamX.shared.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import org.junit.jupiter.api.Test;

public class ClientResearchTest {
  @Test
  public void test_ClientResearch() throws IOException, ClassNotFoundException {
    Player p1 = new Player("Red", 10);
    Communicate communicate = new Communicate();
    ServerSocket ss = new ServerSocket(4497);
    Socket clientSocket = new Socket("localhost", 4497);
    Socket serverSocket = ss.accept();
    ClientResearch cr = new ClientResearch(clientSocket, p1);
    cr.perform(false);
    assertThrows(IllegalArgumentException.class, () -> cr.perform(false));
    cr.commit();

    ClientResearch cr2 = new ClientResearch(clientSocket, p1);
    assertThrows(IllegalArgumentException.class, () -> cr2.perform(true));
    p1.upgradeLevel();
    p1.upgradeLevel();
    assertThrows(IllegalArgumentException.class, () -> cr2.perform(true));
    p1.increaseAllResource(1000);
    cr2.perform(true);
    //p1.unlockCloak();
    //ClientResearch cr3 = new ClientResearch(clientSocket, p1);
    //assertThrows(IllegalArgumentException.class, () -> cr3.perform(true));
  }
}
