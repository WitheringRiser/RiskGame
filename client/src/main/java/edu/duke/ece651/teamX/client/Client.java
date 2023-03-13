package edu.duke.ece651.teamX.client;
import edu.duke.ece651.teamX.shared.*;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class Client {
  private Socket socket;
  private Communicate communicate;
  private Player player;
  private Map map;
  private ArrayList<Action> actions;

  public Client(Socket s) {
    socket = s;
    communicate = new Communicate();
  }
  public void init() throws IOException, ClassNotFoundException {
    player = communicate.receivePlayer(socket);
  }

  public boolean chooseTerritory() { return true; }

  public boolean setUnits(Territory terr, Unit unit, int num) { return true; }

  public void receiveMap() {}

  // public Player receivePlayer() {}

  public void addAction(Action action) {}

  public boolean sendActions() { return true; }

  public PlayError receiveError() { return null; }

  public void errorHandling(PlayError err) {}
}
