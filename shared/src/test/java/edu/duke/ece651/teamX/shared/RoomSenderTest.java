package edu.duke.ece651.teamX.shared;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import org.junit.jupiter.api.Test;

public class RoomSenderTest {
  @Test
  public void test_RoomSender() throws IOException, ClassNotFoundException {
    RoomSender aSender = new RoomSender(2, 2, null, null, true, false, null, null);
    ServerSocket ss = new ServerSocket(4451);
    Socket clientSocket = new Socket("localhost", 4451);
    Socket playerSocket = ss.accept();
    Communicate.sendObject(playerSocket, aSender);
    RoomSender rSender = (RoomSender)Communicate.receiveObject(clientSocket);
    assertEquals(2, rSender.getTotalNum());
    assertEquals(2, rSender.getJointedNum());
    assertEquals(null, rSender.getPlayerSet());
    assertEquals(null, rSender.getMap());
    assertEquals(true, rSender.getIsBegin());
    assertEquals(false, rSender.getIsEnd());
    assertEquals(null, rSender.getWinner());
    assertEquals(null, rSender.getLosers());
  }
}
