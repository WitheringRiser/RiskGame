package edu.duke.ece651.teamX.shared;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Communicate {

  /**
   * Send object through socket
   *
   * @param socket
   * @param obj
   * @throws IOException
   */
  public void sendObject(Socket socket, Object obj) throws IOException {
    OutputStream outputStream = socket.getOutputStream();
    ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
    objectOutputStream.writeObject(obj);
  }

  /**
   * Send map through socket
   *
   * @param socket
   * @param map
   * @throws IOException
   */
  public void sendMap(Socket socket, Map map) throws IOException {
    sendObject(socket, map);
  }

  /**
   * Send integer (representing choice or decision)
   *
   * @param socket
   * @param num    is the choice or decision to send
   * @throws IOException
   */
  public void sendInt(Socket socket, Integer num) throws IOException {
    sendObject(socket, num);
  }

  public void sendPlayer(Socket socket, Player player) throws IOException {
    sendObject(socket, player);
  }

  /**
   * Receive object through socket
   *
   * @param socket
   * @return
   * @throws IOException
   * @throws ClassNotFoundException
   */
  public Object receiveObject(Socket socket) throws IOException, ClassNotFoundException {
    InputStream inputStream = socket.getInputStream();
    ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
    return objectInputStream.readObject();
  }

  /**
   * Receive map through socket
   *
   * @param socket
   * @return
   * @throws IOException
   * @throws ClassNotFoundException
   */
  public Map receiveMap(Socket socket) throws IOException, ClassNotFoundException {
    return (Map) receiveObject(socket);
  }

  public Integer receiveInt(Socket socket) throws IOException, ClassNotFoundException {
    return (Integer) receiveObject(socket);
  }

  public Player receivePlayer(Socket socket) throws IOException, ClassNotFoundException {
    return (Player) receiveObject(socket);
  }
}
