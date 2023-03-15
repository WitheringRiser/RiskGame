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
    map = new Map();

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
    ArrayList<Territory> territories = free_groups.get(terr_ind);
    setAllUnits(territories);  //place units in territories
    sendUnitPlacement(territories);
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
        out.print(promot.enterAgainPromot());
      }
    }
  }
  /**
   *Place a number of units in a chosen territory
   *@param t is the chosen territory
   *@param num_units is the number of units to place  
   */
  public void setUnits(Territory t, int num_units) { t.addUnits(null, num_units); }

  /**
   *Place all available units to the territories
   *To minimize potential error for server, 
   *this function will make sure client can only choose the 
   *territories belongs to him/her and cannot place more than 
   *the inital unit number in the Player object
   *@param territories is a list of territories of the client
   */
  public void setAllUnits(ArrayList<Territory> territories) throws IOException {
    int remain_units = player.getUnitNum();
    boolean is_start = true;
    while (remain_units > 0) {
      out.println(promot.setUnitPromot(territories, remain_units, is_start));
      is_start = false;
      String user_in = inputReader.readLine();
      int choice = getUserInt(user_in);
      if (choice >= 0 && choice < territories.size()) {
        while (true) {
          out.println(promot.enterNumPromot());
          user_in = inputReader.readLine();
          if (user_in.equals("b") || user_in.equals("B")) {
            break;
          }
          int num_units = getUserInt(user_in);
          if (num_units >= 0 && num_units <= remain_units) {
            setUnits(territories.get(choice), num_units);
            remain_units -= num_units;
            break;
          }
          out.print(promot.enterAgainPromot());
        }
      }
      else {
        out.print(promot.enterAgainPromot());
      }
    }
  }

  /**
   *Send unit placement information to the server
   */
  public void sendUnitPlacement(ArrayList<Territory> territories) throws IOException {
    communicate.sendObject(socket, territories);
  }

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

  //public ArrayList<Territory> findAllTerr() {}

  public void addAction(Action action) {}

  public boolean sendActions() { return true; }

  public PlayError receiveError() { return null; }

  public void errorHandling(PlayError err) {}
}
