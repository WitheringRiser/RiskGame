/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package edu.duke.ece651.teamX.server;

import edu.duke.ece651.teamX.shared.Communicate;
import edu.duke.ece651.teamX.shared.Map;
import edu.duke.ece651.teamX.shared.MyName;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class App {

  public static void main(String[] args) throws IOException, ClassNotFoundException {
    Communicate communicate = new Communicate();
    ServerSocket ss = new ServerSocket(4444);
    Socket socket = ss.accept();
    Map recv_map = (Map) communicate.receiveMap(socket);
    System.out.println(recv_map.getTerritoryNum());
  }

}
