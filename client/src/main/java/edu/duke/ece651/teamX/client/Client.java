package edu.duke.ece651.teamX.client;

import edu.duke.ece651.teamX.shared.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;

public class Client {

  private Socket socket;
  private Communicate communicate;
  private Player player; // init
  private TextPrompt prompt; // init
  private Map map;
  private PrintStream out;
  private UserInReader inputReader;
  private ArrayList<Player> losers;

  /**
   * Construct a Client object
   *
   * @param s is socket to communicate with server
   */
  public Client(Socket s, BufferedReader input, PrintStream out) {
    socket = s;
    communicate = new Communicate();
    map = new Map();
    this.out = out;
    this.inputReader = new UserInReader(input, out);
  }

  /**
   * Initialze the game settings for client Receive player to get player properties (name, num of
   * units) Choose a territory group to sart Set Units
   */
  public void init() throws IOException, ClassNotFoundException {
    player = communicate.receivePlayer(socket);
    prompt = new TextPrompt(player);
    out.print(prompt.startPrompt());
    ArrayList<Territory> territories = chooseTerrGroup();
    setAllUnits(territories); // place units in territories
    // attacks = new ArrayList<AttackSender>();
    // moves = new ArrayList<MoveSender>();
  }

  /**
   * Select a group to start
   *
   * @return ArrayList<Territory> of the chosen territoy group
   * @throws IOException
   * @throws ClassNotFoundException
   */
  public ArrayList<Territory> chooseTerrGroup() throws IOException, ClassNotFoundException {
    ClientGroupSetting grouSetting = new ClientGroupSetting(socket, out, inputReader, prompt);
    grouSetting.perform();
    grouSetting.commit();
    return grouSetting.getGroup();
  }

  /**
   * Place all available units to the territories
   *
   * @param territories is the group from chooseTerrGroup function
   * @throws IOException
   */
  public void setAllUnits(ArrayList<Territory> territories) throws IOException {
    ClientUnitSetting unitSetting = new ClientUnitSetting(socket, out, inputReader, prompt,
        territories,
        player.getUnitNum());
    unitSetting.perform();
    unitSetting.commit();
  }

  /**
   * Receive map from server
   */
  public void receiveMap() throws IOException, ClassNotFoundException {
    map = communicate.receiveMap(socket);
  }

  /**
   * Display Map received from server
   */
  public void displayMap() throws IOException {
    TextDisplayer dis = new TextDisplayer(map);
    out.print(dis.display());
  }

  /**
   * Perform the commit action for all ClientTurnActions
   *
   * @param actions
   * @throws IOException
   */
  public void performCommit(ClientAction... actions) throws IOException {
    for (ClientAction act : actions) {
      act.commit();
    }
  }


  private GameResult receiveGameResult() throws IOException, ClassNotFoundException {
    return communicate.receiveGameResult(socket);
  }


  /**
   * Let client to player one turn
   *
   * @throws IOException
   */
  public void playOneTurn() throws IOException {
    ClientAttack attack = new ClientAttack(socket, out, inputReader, prompt, map, player);
    ClientMove move = new ClientMove(socket, out, inputReader, prompt, map, player);
    while (true) {
      displayMap();
      out.print(prompt.oneTurnPrompt());
      String user_in = inputReader.readString();
      user_in = user_in.toUpperCase();
      if (user_in.equals("M")) {
        move.perform();
      } else if (user_in.equals("A")) {
        attack.perform();
      } else if (user_in.equals("D")) {
        performCommit(move, attack);
        out.print(prompt.commitMessage());
        return;
      } else {
        out.print(prompt.enterAgainPrompt());
      }
    }
  }


  public void playTurns() throws IOException, ClassNotFoundException {
    while (true) {
      receiveMap();
      GameResult gameResult = receiveGameResult();
      if (!gameResult.isWin() && !gameResult.loserContains(player)) {
        playOneTurn();
      } else if (gameResult.isWin()) {
        out.println("Game finished!");
        out.println("Winner is " + gameResult.getWinner().getName());
        return;
      } else if (gameResult.loserContains(player)) {
        out.println("You lose!");
        return;
      }
    }
  }
}
