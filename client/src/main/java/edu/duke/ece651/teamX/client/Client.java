package edu.duke.ece651.teamX.client;
import edu.duke.ece651.teamX.shared.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
public class Client {
  private Socket socket;
  private Communicate communicate;
  private Player player;
  private TextPromot promot;
  private Map map;
  private ArrayList<Action> actions;
  private PrintStream out;
  private BufferedReader inputReader;

  /**
   *Convert user input to an integer
   *@param user_in is user input string
   *@return -1 if the number is not valid
   *@return int >= 0 if user input a valid number 
   */
  private int getUserInt(String user_in) {
    try {
      int choice = Integer.parseInt(user_in);
      return choice;
    }
    catch (NumberFormatException ex) {
      out.println("Expect a valid number, but got \"" + user_in + "\"");
    }
    return -1;
  }

  /**
   *Construct a Client object
   *@param s is socket to communicate with server
   */
  public Client(Socket s, BufferedReader input, PrintStream out) {
    socket = s;
    communicate = new Communicate();
    this.out = out;
    this.inputReader = input;
  }

  private HashMap<Integer, ArrayList<Territory> > receiveTerrGroup()
      throws IOException, ClassNotFoundException {
    return (HashMap<Integer, ArrayList<Territory> >)communicate.receiveObject(socket);
  }
  /**
   *Initialze the game settings for client
   *Receive player to get player properties (name, num of units)
   *Choose a territory group to sart
   *Set Units
   */
  public void init() throws IOException, ClassNotFoundException {
    player = communicate.receivePlayer(socket);
    promot = new TextPromot(player);
    out.print(promot.startPromot());
    HashMap<Integer, ArrayList<Territory> > free_groups = receiveTerrGroup();
    int terr_ind = chooseTerrGroup(free_groups);
    communicate.sendInt(socket, terr_ind);  //notify server about the choice
  }

  /**
   *Choose a territory group as initial territories
   *@param free_groups is the available group options
   *@return int the choice of user
   *@print invalid option to promot user to choose again
   */
  public int chooseTerrGroup(HashMap<Integer, ArrayList<Territory> > free_groups)
      throws IOException {
    out.print(promot.displayTerrGroup(free_groups));
    while (true) {
      String user_in = inputReader.readLine();
      int choice = getUserInt(user_in);
      if (choice >= 0 && free_groups.containsKey(choice)) {
        return choice;
      }
      else {
        out.println("The input is not a valid option, please choose again.");
      }
    }
  }

  public boolean setUnits(ArrayList<Territory> territories) { return true; }

  /**
   *Receive map from server
   */
  public void receiveMap() throws IOException, ClassNotFoundException {
    map = communicate.receiveMap(socket);
  }
  /**
   *Display Map
   */
  public void displayMap() throws IOException {
    TextDisplayer dis = new TextDisplayer(map);
    out.print(dis.display());
  }

  // public Player receivePlayer() {}

  public void addAction(Action action) {}

  public boolean sendActions() { return true; }

  public PlayError receiveError() { return null; }

  public void errorHandling(PlayError err) {}
}
