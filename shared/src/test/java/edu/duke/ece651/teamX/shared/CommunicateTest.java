package edu.duke.ece651.teamX.shared;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.Test;

class CommunicateTest {

  private Map createMap() {
    Map my_map = new Map();
    Territory t1 = new Territory("Territory_1");
    Territory t2 = new Territory("Terrotory_2");
    my_map.addTerritory(t1);
    my_map.addTerritory(t2);
    return my_map;
  }

  @Test
  void send_receive_Map() throws IOException, ClassNotFoundException {
//    Map myMap = createMap();
//
//    Communicate communicate = new Communicate();
//    ServerSocket ss = new ServerSocket(4444);
//    Socket clientSocket = new Socket("localhost", 4444);
//    Socket serverSocket = ss.accept();
//    communicate.sendMap(clientSocket, myMap);
//    Map recv_map = communicate.receiveMap(serverSocket);
//    assertEquals(myMap, recv_map);
  }

}