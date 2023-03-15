package edu.duke.ece651.teamX.shared;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class MultiAttackTest {
  @Test
  public void test_threeAttackFightTogether() {
    Territory t1 = new Territory("duke", 100);
    Territory t2 = new Territory("cary", 80);
    Territory t3 = new Territory("rdu", 7);
    Player p1 = new Player("zhou", 20);
    Player p2 = new Player("andre", 15);
    Player p3 = new Player("an", 15);

    Map map = new Map();
    map.addTerritory(t1, p1);
    map.addTerritory(t2, p2);
    map.addTerritory(t3, p3);

    TextDisplayer displayer = new TextDisplayer(map);
    System.out.println(displayer.display());

    ArrayList<Attacker> enemies1 = new ArrayList<Attacker>();
    enemies1.add(new Attacker(t1, 80, p1, 1));
    ArrayList<Attacker> enemies2 = new ArrayList<Attacker>();
    enemies2.add(new Attacker(t2, 78, p2, 1));
    ArrayList<Attacker> enemies3 = new ArrayList<Attacker>();
    enemies3.add(new Attacker(t3, 7, p3, 1));

    MultiAttack ma1 = new MultiAttack(enemies1, t2, map);
    MultiAttack ma2 = new MultiAttack(enemies2, t3, map);
    MultiAttack ma3 = new MultiAttack(enemies3, t1, map);
    ma1.prepare();
    ma2.prepare();
    ma3.prepare();

    assertEquals(true, ma1.perform());
    assertEquals(true, ma2.perform());
    assertEquals(true, ma3.perform());
    System.out.println(displayer.display());
    assertEquals(11, t1.getUnitsNumber());
    assertEquals(79, t2.getUnitsNumber());
    assertEquals(78, t3.getUnitsNumber());
    
    assertEquals(p1, map.getOwner(t1));
    assertEquals(p1, map.getOwner(t2));
    assertEquals(p2, map.getOwner(t3));

  }
  @Test
  public void test_twoAttackFightTogether() {
    Territory t1 = new Territory("duke", 100);
    Territory t2 = new Territory("cary", 80);

    Player p1 = new Player("zhou", 20);
    Player p2 = new Player("andre", 15);

    Map map = new Map();
    map.addTerritory(t1, p1);
    map.addTerritory(t2, p2);

    TextDisplayer displayer = new TextDisplayer(map);
    System.out.println(displayer.display());

    ArrayList<Attacker> enemies1 = new ArrayList<Attacker>();
    enemies1.add(new Attacker(t1, 100, p1, 1));
    ArrayList<Attacker> enemies2 = new ArrayList<Attacker>();
    enemies2.add(new Attacker(t2, 80, p2, 1));

    MultiAttack ma1 = new MultiAttack(enemies1, t2, map);
    MultiAttack ma2 = new MultiAttack(enemies2, t1, map);
    ma1.prepare();
    ma2.prepare();

    assertEquals(true, ma1.perform());
    assertEquals(true, ma2.perform());
    System.out.println(displayer.display());
    assertEquals(100, t2.getUnitsNumber());
    assertEquals(80, t1.getUnitsNumber());
    assertEquals(p1, map.getOwner(t2));
    assertEquals(p2, map.getOwner(t1));

  }

  @Test
  public void test_twoAttackOneOwener() {
    Territory t1 = new Territory("duke", 100);
    Territory t2 = new Territory("cary", 100);
    Territory t3 = new Territory("rdu", 7);
    Territory t4 = new Territory("boston", 3);

    Territory defender = new Territory("nyu", 25);

    Player p1 = new Player("zhou", 20);
    Player p3 = new Player("andre", 15);
    Player p4 = new Player("andrew", 15);
    Player p5 = new Player("defender", 15);

    Map map = new Map();
    map.addTerritory(t1, p1);
    map.addTerritory(t2, p1);
    map.addTerritory(t3, p3);
    map.addTerritory(t4, p4);
    map.addTerritory(defender, p5);

    TextDisplayer displayer = new TextDisplayer(map);
    System.out.println(displayer.display());

    ArrayList<Attacker> enemies = new ArrayList<Attacker>();
    enemies.add(new Attacker(t1, 95, p1, 1));
    enemies.add(new Attacker(t2, 95, p1, 1));
    enemies.add(new Attacker(t3, 7, p3, 1));
    enemies.add(new Attacker(t4, 3, p4, 1));

    MultiAttack ma = new MultiAttack(enemies, defender, map);
    ma.prepare();
    assertEquals(true, ma.perform());
    System.out.println(displayer.display());
    assertEquals(153, defender.getUnitsNumber());

  }

  public void test_perform_helper(int num, int res) {
    Territory t1 = new Territory("duke", num);
    Territory t2 = new Territory("cary", 8);
    Territory t3 = new Territory("rdu", 7);
    Territory t4 = new Territory("boston", 3);

    Territory defender = new Territory("nyu", 25);

    Player p1 = new Player("zhou", 20);
    Player p2 = new Player("an", 15);
    Player p3 = new Player("andre", 15);
    Player p4 = new Player("andrew", 15);
    Player p5 = new Player("defender", 15);

    Map map = new Map();
    map.addTerritory(t1, p1);
    map.addTerritory(t2, p2);
    map.addTerritory(t3, p3);
    map.addTerritory(t4, p4);
    map.addTerritory(defender, p5);

    TextDisplayer displayer = new TextDisplayer(map);
    System.out.println(displayer.display());

    ArrayList<Attacker> enemies = new ArrayList<Attacker>();
    enemies.add(new Attacker(t1, num, p1, 1));
    enemies.add(new Attacker(t2, 8, p2, 1));
    enemies.add(new Attacker(t3, 7, p3, 1));
    enemies.add(new Attacker(t4, 3, p4));
    MultiAttack ma = new MultiAttack(enemies, defender, map);
    assertThrows(IllegalArgumentException.class, () -> ma.perform());
    ma.prepare();
    assertEquals(true, ma.perform());
    assertEquals(res, defender.getUnitsNumber());
    System.out.println(displayer.display());

  }

  @Test
  public void test_perform() {
    test_perform_helper(108, 74);
    test_perform_helper(8, 18);
  }

}
