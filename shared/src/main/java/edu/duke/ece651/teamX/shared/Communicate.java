package edu.duke.ece651.teamX.shared;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Communicate {

  public void sendObject(Socket socket, Object obj) throws IOException {
    OutputStream outputStream = socket.getOutputStream();
    ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
    objectOutputStream.writeObject(obj);
  }

  public void sendMap(Socket socket, Map map) throws IOException {
    sendObject(socket, map);
  }

  public Object receiveObject(Socket socket) throws IOException, ClassNotFoundException {
    InputStream inputStream = socket.getInputStream();
    ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
    return objectInputStream.readObject();
  }

  public Map receiveMap(Socket socket) throws IOException, ClassNotFoundException {
    return (Map) receiveObject(socket);
  }

}
