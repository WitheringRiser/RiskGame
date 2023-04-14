package edu.duke.ece651.teamX.client;

import edu.duke.ece651.teamX.shared.*;
import edu.duke.ece651.teamX.client.controller.*;
import edu.duke.ece651.teamX.client.view.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import javafx.util.Pair;
import java.util.HashMap;
import javafx.stage.Stage;

public class Client {

  private Socket socket;
  private Communicate communicate;
  private Player player; // init
  // private TextPrompt prompt; // init
  private Map map;
  private Stage stage;
  // private PrintStream out;
  // private UserInReader inputReader;
  private ArrayList<Player> losers;
  private int foodResource;
  private int techResource;
  private ArrayList<String> namePassword;

  /**
   * Construct a Client object
   *
   * @param s is socket to communicate with server
   */
  public Client(Socket s, Stage st, ArrayList<String> np) {
    socket = s;
    stage = st;
    communicate = new Communicate();
    map = new Map();
    namePassword = np;
  }

  /**
   * Initialze the game settings for client Receive player to get player
   * properties (name, num of
   * units) Choose a territory group to sart Set Units
   */
  public void init() throws IOException, ClassNotFoundException {
    player = communicate.receivePlayer(socket);
    HashMap<Integer, ArrayList<Territory>> ts = (HashMap<Integer, ArrayList<Territory>>) Communicate
        .receiveObject(socket);  
        SelectGroupController sgc = new SelectGroupController(stage, socket, namePassword, ts);
        GeneralScreen<SelectGroupController> sgs = new GeneralScreen<>(sgc);
        
    // prompt = new TextPrompt(player);
    // out.print(prompt.startPrompt());

    // ArrayList<Territory> territories = chooseTerrGroup();
    // setAllUnits(territories); // place units in territories
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
  // public ArrayList<Territory> chooseTerrGroup() throws IOException, ClassNotFoundException {
  //   // ClientGroupSetting groupSetting = new ClientGroupSetting(socket, out,
  //   // inputReader, prompt);
  //   // groupSetting.perform();
  //   // groupSetting.commit();
  //   // return groupSetting.getGroup();

  // }

  /**
   * Place all available units to the territories
   *
   * @param territories is the group from chooseTerrGroup function
   * @throws IOException
   */
  // public void setAllUnits(ArrayList<Territory> territories) throws IOException {
  //   ClientUnitSetting unitSetting = new ClientUnitSetting(socket, out, inputReader, prompt,
  //       territories,
  //       player.getUnitNum());
  //   unitSetting.perform();
  //   unitSetting.commit();
  // }

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
    // out.print(dis.display());
  }

  /**
   * Perform the commit action for all ClientTurnActions
   *
   * @param actions
   * @throws IOException
   */
  public void performCommit(ClientResearch research, ClientUpgrade upgrade, ClientAction... actions)
      throws IOException {
    for (ClientAction act : actions) {
      act.commit();
    }
    research.commit();
    upgrade.commit();
  }

  private GameResult receiveGameResult() throws IOException, ClassNotFoundException {
    return communicate.receiveGameResult(socket);
  }

  public int getFoodResource() {
    return foodResource;
  }

  public int getTechResource() {
    return techResource;
  }

  public void setTechResource(int t) {
    techResource = t;
  }

  public void setFoodResource(int f) {
    foodResource = f;
  }

  // ability to increment tech resources at the end of each turn
  public void AddTechResource(Map TerrMap, Pair<Integer, String> PlayerInfo) {

    TerritoryProduce AddResource = new TerritoryProduce();
    HashMap<Territory, Player> HM_Terr = new HashMap<Territory, Player>();
    HM_Terr = TerrMap.getMap();
    ArrayList<Territory> MyTerr = new ArrayList<>();

    for (int i = 0; i < HM_Terr.size(); i++) {
      int CurrAdd = AddResource.getTech(HM_Terr.get(i).getName());
      this.techResource += CurrAdd;
    }
  }

  // ability to increment food resources at the end of each turn
  public void AddFoodResource(Map TerrMap, Pair<Integer, String> PlayerInfo) {

    TerritoryProduce AddResource = new TerritoryProduce();
    HashMap<Territory, Player> HM_Terr = new HashMap<Territory, Player>();
    HM_Terr = TerrMap.getMap();
    ArrayList<Territory> MyTerr = new ArrayList<>();

    for (int i = 0; i < HM_Terr.size(); i++) {
      int CurrAdd = AddResource.getFood(HM_Terr.get(i).getName());
      this.foodResource += CurrAdd;
    }
  }

  /**
   * Let client to player one turn
   *
   * @throws IOException
   */
  public void playOneTurn() throws IOException {
    // ClientAttack attack = new ClientAttack(socket, out, inputReader, prompt, map, player);
    // ClientMove move = new ClientMove(socket, out, inputReader, prompt, map, player);
    // ClientResearch research = new ClientResearch(player);
    // ClientUpgrade upgrade = new ClientUpgrade(socket, out, inputReader, prompt, map, player);
    // while (true) {
    //   displayMap();
    //   out.print(prompt.oneTurnPrompt());
    //   String user_in = inputReader.readString();
    //   user_in = user_in.toUpperCase();
    //   switch (user_in) {
    //     case "M":
    //       move.perform();
    //       break;
    //     case "A":
    //       attack.perform();
    //       break;
    //     case "R":
    //       research.perform();
    //       break;
    //     case "U":
    //       upgrade.perform();
    //       break;
    //     case "D":
    //       performCommit(research, upgrade, move, attack);
    //       out.print(prompt.commitMessage());
    //       return;
    //     default:
    //       out.print(prompt.enterAgainPrompt());
    //       break;
    //   }
    // }
  }

  public void playTurns() throws IOException, ClassNotFoundException {
    while (true) {
      receiveMap();
      GameResult gameResult = receiveGameResult();
      if (!gameResult.isWin() && !gameResult.loserContains(player)) {
        playOneTurn();
      } else if (gameResult.isWin()) {
        // out.println("Game finished!");
        // out.println("Winner is " + gameResult.getWinner().getName());
        return;
      } else if (gameResult.loserContains(player)) {
        displayMap();
        // out.println("You lose! Right now you are watching the game!");
      }
    }
  }

  /**
   * When enter a new game, begin from choosing group
   *
   * @throws IOException
   * @throws ClassNotFoundException
   */
  public void start() throws IOException, ClassNotFoundException {
    init();
    playTurns();
  }

  /**
   * When switch to a game that is palying Receive palyer again to initialize
   *
   * @throws IOException
   * @throws ClassNotFoundException
   */
  // public void continuePlay() throws IOException, ClassNotFoundException {
  //   player = communicate.receivePlayer(socket);
  //   // prompt = new TextPrompt(player);
  //   // out.println("Welcome back, player " + player.getName());
  //   playTurns();
  // }
  public void receivePlayer() throws IOException, ClassNotFoundException{
    player = communicate.receivePlayer(socket);
  }
}
