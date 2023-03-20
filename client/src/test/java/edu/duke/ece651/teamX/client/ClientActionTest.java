package edu.duke.ece651.teamX.client;

import static org.junit.jupiter.api.Assertions.*;

import edu.duke.ece651.teamX.shared.*;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import org.junit.jupiter.api.Test;
public class ClientActionTest {
  @Test
  public void test_FindTerr() throws IOException, ClassNotFoundException {
    String input_data = "0\n0\n30\n20\n"     //for move1 normal
                        + "0\n0\n1\nb\n"     //for move1 quit when input number
                        + "0\n0\n0\nb\n"     //for move2
                        + "0\nb\n0\n0\nb\n"  //for move2
                        + "0\n0\nb\n"        //for move3
                        + "0\n0\n10\nb\n"    //for att1
                        + "0\n0\n10\nb\n"    //for att2
                        + "";
    BufferedReader input = new BufferedReader(new StringReader(input_data));
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    PrintStream output = new PrintStream(bytes, true);
    Territory a = new Territory("A", 20);
    Territory b = new Territory("B", 20);
    Territory c = new Territory("C", 20);
    Territory d = new Territory("D", 20);
    Territory e = new Territory("E", 20);
    Territory f = new Territory("F", 20);
    Territory g = new Territory("G", 20);
    a.addNeighbors(b);
    a.addNeighbors(c);
    b.addNeighbors(c);
    b.addNeighbors(d);
    b.addNeighbors(f);
    c.addNeighbors(e);
    d.addNeighbors(g);
    f.addNeighbors(g);
    Player p1 = new Player("Yellow", 20);
    Player p2 = new Player("Red", 20);
    Map map = new Map();
    map.addTerritory(a, p1);
    map.addTerritory(b, p1);
    map.addTerritory(c, p1);
    map.addTerritory(f, p1);
    map.addTerritory(g, p1);
    map.addTerritory(d, p2);
    map.addTerritory(e, p2);
    TextPromot tp1 = new TextPromot(p1);
    TextPromot tp2 = new TextPromot(p2);
    UserInReader uir = new UserInReader(input, output);
    //Test for the move
    ClientMove move1 =
        new ClientMove(null, output, uir, tp1, map, new Player("Yellow", 20));
    ClientMove move2 = new ClientMove(null, output, uir, tp2, map, new Player("Red", 20));
    ArrayList<Territory> mres1 = move1.findDestTerrs(a);
    ArrayList<Territory> mres2 = move2.findDestTerrs(d);
    assertThrows(IllegalArgumentException.class, () -> move1.findDestTerrs(d));
    assertEquals(4, mres1.size());
    assertEquals(0, mres2.size());
    ClientMove move3 =
        new ClientMove(null, output, uir, tp1, map, new Player("Blue", 20));

    //Test for the attack
    ClientAttack att1 =
        new ClientAttack(null, output, uir, tp1, map, new Player("Yellow", 20));
    ClientAttack att2 = new ClientAttack(null, output, uir, tp1, map, p2);
    ArrayList<Territory> ares1 = att1.findDestTerrs(a);

    assertThrows(IllegalArgumentException.class, () -> att1.findDestTerrs(d));
    ArrayList<Territory> ares2 = att2.findDestTerrs(d);
    assertEquals(0, ares1.size());
    assertEquals(2, ares2.size());
    move1.perform();
    move1.perform();
    assertEquals(0, a.getUnitsNumber());
    assertEquals(40, b.getUnitsNumber());
    move2.perform();
    move2.perform();
    move2.perform();
    move3.perform();

    att1.perform();
    att2.perform();
    assertEquals(10, d.getUnitsNumber());
  }
}
