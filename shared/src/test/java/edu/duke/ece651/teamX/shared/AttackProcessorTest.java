package edu.duke.ece651.teamX.shared;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class AttackProcessorTest {
  @Disabled
  @Test
  public void test_invalid_totalSum() {
    ArrayList<AttackSender> allAttack = new ArrayList<AttackSender>();
    Territory t1 = new Territory("duke", 100);
    Territory t2 = new Territory("cary", 80);
    Territory t3 = new Territory("rdu", 7);
    Territory t4 = new Territory("bos", 11);
    Player p1 = new Player("zhou", 20);
    Player p2 = new Player("andre", 15);
    t1.addNeighbors(t2);
    t1.addNeighbors(t3);
    t1.addNeighbors(t4);
    Map map = new Map();
    map.addTerritory(t1, p1);
    map.addTerritory(t2, p2);
    map.addTerritory(t3, p2);
    map.addTerritory(t4, p2);

    TextDisplayer displayer = new TextDisplayer(map);
    System.out.println(displayer.display());

    allAttack.add(new AttackSender(t1, t2, 30));
    allAttack.add(new AttackSender(t1, t3, 40));
    allAttack.add(new AttackSender(t1, t4, 31));

    assertThrows(IllegalArgumentException.class, () -> new AttackProcessor(allAttack, map));
  }

  @Disabled
  @Test
  public void test_invalid_notNeigbor() {
    ArrayList<AttackSender> allAttack = new ArrayList<AttackSender>();
    Territory t1 = new Territory("duke", 100);
    Territory t2 = new Territory("cary", 80);
    Player p1 = new Player("zhou", 20);
    Player p2 = new Player("z", 20);
    Map map = new Map();
    map.addTerritory(t1, p1);
    map.addTerritory(t2, p2);

    allAttack.add(new AttackSender(t1, t2, 11));
    allAttack.add(new AttackSender(t2, t1, 50));

    assertThrows(IllegalArgumentException.class, () -> new AttackProcessor(allAttack, map));
  }

  @Disabled
  @Test
  public void test_invalid_sameOwener() {
    ArrayList<AttackSender> allAttack = new ArrayList<AttackSender>();
    Territory t1 = new Territory("duke", 100);
    Territory t2 = new Territory("cary", 80);
    Player p1 = new Player("zhou", 20);
    t1.addNeighbors(t2);
    Map map = new Map();
    map.addTerritory(t1, p1);
    map.addTerritory(t2, p1);

    allAttack.add(new AttackSender(t1, t2, 11));
    allAttack.add(new AttackSender(t2, t1, 50));

    assertThrows(IllegalArgumentException.class, () -> new AttackProcessor(allAttack, map));
  }

  @Disabled
  @Test
  public void test_invalid_sameTerritory() {
    ArrayList<AttackSender> allAttack = new ArrayList<AttackSender>();
    Territory t1 = new Territory("duke", 100);
    Territory t2 = new Territory("cary", 80);
    Player p1 = new Player("zhou", 20);
    Player p2 = new Player("andre", 15);
    t1.addNeighbors(t2);
    Map map = new Map();
    map.addTerritory(t1, p1);
    map.addTerritory(t2, p2);

    allAttack.add(new AttackSender(t1, t1, 11));
    allAttack.add(new AttackSender(t2, t1, 50));

    assertThrows(IllegalArgumentException.class, () -> new AttackProcessor(allAttack, map));
  }

  @Disabled
  @Test
  public void test_invalid_num() {
    ArrayList<AttackSender> allAttack = new ArrayList<AttackSender>();
    Territory t1 = new Territory("duke", 100);
    Territory t2 = new Territory("cary", 80);
    Player p1 = new Player("zhou", 20);
    Player p2 = new Player("andre", 15);
    t1.addNeighbors(t2);
    Map map = new Map();
    map.addTerritory(t1, p1);
    map.addTerritory(t2, p2);

    allAttack.add(new AttackSender(t1, t2, 101));
    allAttack.add(new AttackSender(t2, t1, 50));

    assertThrows(IllegalArgumentException.class, () -> new AttackProcessor(allAttack, map));
  }

  @Disabled
  @Test
  public void test_resovle() {
    ArrayList<AttackSender> allAttack = new ArrayList<AttackSender>();
    Territory t1 = new Territory("duke", 100);
    Territory t2 = new Territory("cary", 80);
    Territory t3 = new Territory("rdu", 7);
    Territory t4 = new Territory("bos", 11);
    Player p1 = new Player("zhou", 20);
    Player p2 = new Player("andre", 15);
    Player p3 = new Player("an", 15);
    t1.addNeighbors(t2);
    t2.addNeighbors(t3);
    t1.addNeighbors(t3);
    t2.addNeighbors(t4);
    Map map = new Map();
    map.addTerritory(t1, p1);
    map.addTerritory(t2, p2);
    map.addTerritory(t3, p3);
    map.addTerritory(t4, p3);

    TextDisplayer displayer = new TextDisplayer(map);
    System.out.println(displayer.display());

    allAttack.add(new AttackSender(t1, t2, 30));
    allAttack.add(new AttackSender(t2, t3, 50));
    allAttack.add(new AttackSender(t3, t1, 5));
    allAttack.add(new AttackSender(t4, t2, 6));

    AttackProcessor ap = new AttackProcessor(allAttack, map);
    ap.resovleAllAttack();
    System.out.println(displayer.display());
    assertEquals(49, t3.getUnitsNumber());
    assertEquals(7, t2.getUnitsNumber());
    assertEquals(62, t1.getUnitsNumber());
    assertEquals(5, t4.getUnitsNumber());

  }

}
