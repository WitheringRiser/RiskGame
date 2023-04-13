package edu.duke.ece651.teamX.client;

import static org.junit.jupiter.api.Assertions.*;

import edu.duke.ece651.teamX.shared.*;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.util.Pair;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class ClientTest {
  private void test_displayMap(Client client,
                               Communicate communicate,
                               Socket serverSocket,
                               Map my_map,
                               ByteArrayOutputStream bytes)
      throws IOException, ClassNotFoundException {
    communicate.sendMap(serverSocket, my_map);
    bytes.reset();
    client.receiveMap();
    client.displayMap();
    String expected = "Red player:\n"
                      + "-----------\n"
                      + "2 units in A (next to: B, C)\n"
                      + "3 units in B (next to: A, D)\n\n"
                      + "Blue player:\n"
                      + "-----------\n"
                      + "1 units in C (next to: A, D)\n"
                      + "0 units in D (next to: B, C)\n\n";
    assertEquals(expected, bytes.toString());
  }

  private void test_init(Client client,
                         Communicate communicate,
                         Socket serverSocket,
                         Player player,
                         HashMap<Integer, ArrayList<Territory> > free_groups,
                         ByteArrayOutputStream bytes)
      throws IOException, ClassNotFoundException {
    communicate.sendObject(serverSocket, player);
    communicate.sendObject(serverSocket, free_groups);
    client.init();

    int res = communicate.receiveInt(serverSocket);
    assertEquals(0, res);
    ArrayList<Territory> territories =
        (ArrayList<Territory>)communicate.receiveObject(serverSocket);
    assertEquals(new Territory("A"), territories.get(0));
    assertEquals(new Territory("B"), territories.get(1));
    assertEquals(0, territories.get(0).getUnitsNumber());
    assertEquals(20, territories.get(1).getUnitsNumber());
  }

  // private void test_findTerr(Client client, Territory t1) {
  // assertThrows(IllegalArgumentException.class,
  // () -> client.findAttackTerr(new Territory("C")));
  // assertThrows(IllegalArgumentException.class,
  // () -> client.findMoveTerr(new Territory("C")));
  // ArrayList<Territory> terrs = client.findOwnTerr();
  // assertEquals(2, terrs.size());
  // assertEquals(true, terrs.contains(new Territory("A")));
  // assertEquals(true, terrs.contains(new Territory("B")));
  // ArrayList<Territory> att_neigs = client.findAttackTerr(t1);
  // assertEquals(1, att_neigs.size());
  // assertEquals(new Territory("C"), att_neigs.get(0));
  // ArrayList<Territory> mv_neigs = client.findMoveTerr(t1);
  // assertEquals(1, mv_neigs.size());
  // assertEquals(new Territory("B"), mv_neigs.get(0));
  // }
  // private void test_action(Client client) {}
  /*
  @Disabled
  @Test
  public void test_Client() throws IOException, ClassNotFoundException {
    Communicate communicate = new Communicate();
    ServerSocket ss = new ServerSocket(4433);
    Socket clientSocket = new Socket("localhost", 4433);
    Socket serverSocket = ss.accept();
    String input_data = "a\n0\n1\nb\n3\n1\n30\n20\n" // for init
        + "m\n0\n0\n3\n2\n" // for move
        + "a\n1\n0\n6\na\nm\n5\nb\n" // for attack
        + "c\n1\n" // for invalid choice
        + "D\n";
    BufferedReader input = new BufferedReader(new StringReader(input_data));
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    PrintStream output = new PrintStream(bytes, true);

    Client client = new Client(clientSocket, input, output);

    Map my_map = new Map();
    Territory t1 = new Territory("A");
    Territory t2 = new Territory("B");
    Territory t3 = new Territory("C");
    Territory t4 = new Territory("D");
    t1.addNeighbors(t2);
    t1.addNeighbors(t3);
    t2.addNeighbors(t4);
    t3.addNeighbors(t4);
    ArrayList<Territory> terr_list1 = new ArrayList<Territory>();
    ArrayList<Territory> terr_list2 = new ArrayList<Territory>();
    terr_list1.add(t1);
    terr_list1.add(t2);
    terr_list2.add(t3);
    terr_list2.add(t4);
    HashMap<Integer, ArrayList<Territory>> my_groups = new HashMap<Integer, ArrayList<Territory>>();
    my_groups.put(0, terr_list1);
    my_groups.put(1, terr_list2);
    Player p1 = new Player("Red", 20);
    test_init(client, communicate, serverSocket, p1, my_groups, bytes);
    Player p2 = new Player("Blue", 20);
    my_map.addTerritory(t1, p1);
    my_map.addTerritory(t2, p1);
    my_map.addTerritory(t3, p2);
    my_map.addTerritory(t4, p2);
    t1.addUnits(null, 2);
    t2.addUnits(null, 3);
    t3.addUnits(null, 1);

    test_displayMap(client, communicate, serverSocket, my_map, bytes);
    // test_findTerr(client, t1);
    communicate.sendObject(serverSocket, false);
    client.playOneTurn();
    ArrayList<MoveSender> moves = (ArrayList<MoveSender>) communicate.receiveObject(serverSocket);
    ArrayList<AttackSender> attacks = (ArrayList<AttackSender>) communicate.receiveObject(serverSocket);
    assertEquals(1, moves.size());
    MoveSender m = moves.get(0);
    assertEquals(t1, m.getSource());
    assertEquals(t2, m.getDestination());
    assertEquals(2, m.getUnitsNum());
    assertEquals(1, attacks.size());
    AttackSender a = attacks.get(0);
    assertEquals(t2, a.getSource());
    assertEquals(t4, a.getDestination());
    assertEquals(5, a.getUnitsNum());

    // to test the AddTechResource and AddFoodResource functions
    Pair<Integer, String> playerInfo = new Pair<Integer, String>(0, "Green");
    // String Input = "{'player_0':[ {'owner':'player_0',
    // 'soldiers':[{'level_0':'3'},{'level_1':'0'},{'level_2':'0'},{'level_3':'0'},{'level_4':'0'},{'level_5':'0'},{'level_6':'0'}],
    // 'neighbor':[{'neighbor_0':'B'},{'neighbor_1':'C'},{'neighbor_2':'D'}],
    // 'territoryName':'A'},{'owner':'player_0',
    // 'soldiers':[{'level_0':'3'},{'level_1':'0'},{'level_2':'0'},{'level_3':'0'},{'level_4':'0'},{'level_5':'0'},{'level_6':'0'}],
    // 'neighbor':[{'neighbor_0':'A'},{'neighbor_1':'C'},{'neighbor_2':'L'}],
    // 'territoryName':'B'},{'owner':'player_0',
    // 'soldiers':[{'level_0':'3'},{'level_1':'0'},{'level_2':'0'},{'level_3':'0'},{'level_4':'0'},{'level_5':'0'},{'level_6':'0'}],
    // 'neighbor':[{'neighbor_0':'A'},{'neighbor_1':'B'},{'neighbor_2':'L'},{'neighbor_3':'J'},{'neighbor_4':'F'},{'neighbor_5':'D'}],
    // 'territoryName':'C'},{'owner':'player_0',
    // 'soldiers':[{'level_0':'3'},{'level_1':'0'},{'level_2':'0'},{'level_3':'0'},{'level_4':'0'},{'level_5':'0'},{'level_6':'0'}],
    // 'neighbor':[{'neighbor_0':'A'},{'neighbor_1':'C'},{'neighbor_2':'F'},
    // {'neighbor_3':'E'}], 'territoryName':'D'},{'owner':'player_0',
    // 'soldiers':[{'level_0':'3'},{'level_1':'0'},{'level_2':'0'},{'level_3':'0'},{'level_4':'0'},{'level_5':'0'},{'level_6':'0'}],
    // 'neighbor':[{'neighbor_0':'D'},{'neighbor_1':'F'}, {'neighbor_2':'G'}],
    // 'territoryName':'E'},{'owner':'player_0',
    // 'soldiers':[{'level_0':'3'},{'level_1':'0'},{'level_2':'0'},{'level_3':'0'},{'level_4':'0'},{'level_5':'0'},{'level_6':'0'}],
    // 'neighbor':[{'neighbor_0':'D'},{'neighbor_1':'C'},{'neighbor_2':'J'},{'neighbor_3':'I'},{'neighbor_4':'G'},{'neighbor_5':'E'}],
    // 'territoryName':'F'}], 'player_1':[ {'owner':'player_1',
    // 'soldiers':[{'level_0':'3'},{'level_1':'0'},{'level_2':'0'},{'level_3':'0'},{'level_4':'0'},{'level_5':'0'},{'level_6':'0'}],
    // 'neighbor':[{'neighbor_0':'E'},{'neighbor_1':'F'},{'neighbor_2':'I'},{'neighbor_3':'H'}],
    // 'territoryName':'G'}, {'owner':'player_1',
    // 'soldiers':[{'level_0':'3'},{'level_1':'0'},{'level_2':'0'},{'level_3':'0'},{'level_4':'0'},{'level_5':'0'},{'level_6':'0'}],
    // 'neighbor':[{'neighbor_0':'G'},{'neighbor_1':'I'}], 'territoryName':'H'},
    // {'owner':'player_1',
    // 'soldiers':[{'level_0':'3'},{'level_1':'0'},{'level_2':'0'},{'level_3':'0'},{'level_4':'0'},{'level_5':'0'},{'level_6':'0'}],
    // 'neighbor':[{'neighbor_0':'F'},{'neighbor_1':'J'},{'neighbor_2':'K'},{'neighbor_3':'H'},{'neighbor_4':'G'}],
    // 'territoryName':'I'}, {'owner':'player_1',
    // 'soldiers':[{'level_0':'3'},{'level_1':'0'},{'level_2':'0'},{'level_3':'0'},{'level_4':'0'},{'level_5':'0'},{'level_6':'0'}],
    // 'neighbor':[{'neighbor_0':'C'},{'neighbor_1':'L'},{'neighbor_2':'K'},{'neighbor_3':'I'},{'neighbor_4':'F'}],
    // 'territoryName':'J'},{'owner':'player_1',
    // 'soldiers':[{'level_0':'3'},{'level_1':'0'},{'level_2':'0'},{'level_3':'0'},{'level_4':'0'},{'level_5':'0'},{'level_6':'0'}],
    // 'neighbor':[{'neighbor_0':'I'},{'neighbor_1':'J'},{'neighbor_2':'L'}],
    // 'territoryName':'K'},{'owner':'player_1',
    // 'soldiers':[{'level_0':'3'},{'level_1':'0'},{'level_2':'0'},{'level_3':'0'},{'level_4':'0'},{'level_5':'0'},{'level_6':'0'}],
    // 'neighbor':[{'neighbor_0':'B'},{'neighbor_1':'C'},{'neighbor_2':'J'},{'neighbor_3':'K'}],
    // 'territoryName':'L'}] }";
    // MyFormatter Map = new MyFormatter(2);
    Map TerritoryMap = new Map();
    // Map.MapParse(TerritoryMap, Input);
    client.AddTechResource(TerritoryMap, playerInfo);
    client.setTechResource(10);
    assert (10 == client.getTechResource());
    client.AddFoodResource(TerritoryMap, playerInfo);
    client.setFoodResource(15);
    assert (15 == client.getFoodResource());
    }*/
}
