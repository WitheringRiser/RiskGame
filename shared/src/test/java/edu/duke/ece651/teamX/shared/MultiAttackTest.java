package edu.duke.ece651.teamX.shared;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class MultiAttackTest {
  @Test
  public void test_getFinalAttacker() {
    Territory t1 = new Territory("duke", 100);
    Territory t2 = new Territory("cary", 100);
    Territory t3 = new Territory("rdu", 7);
    Territory t4 = new Territory("boston", 3);
    ArrayList<Territory> enemies = new ArrayList<Territory>();
    enemies.add(t1);
    enemies.add(t2);
    enemies.add(t3);
    enemies.add(t4);

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

    MultiAttack ma = new MultiAttack(enemies, defender, map);
    assertEquals(true, ma.perform());
    System.out.println(displayer.display());
    assertEquals(163, defender.getUnitsNumber());

  }

  public void test_perform_helper(int num, int res) {
    Territory t1 = new Territory("duke", num);
    Territory t2 = new Territory("cary", 8);
    Territory t3 = new Territory("rdu", 7);
    Territory t4 = new Territory("boston", 3);
    ArrayList<Territory> enemies = new ArrayList<Territory>();
    enemies.add(t1);
    enemies.add(t2);
    enemies.add(t3);
    enemies.add(t4);

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
    // System.out.println(displayer.display());

    MultiAttack ma = new MultiAttack(enemies, defender, map);
    assertEquals(true, ma.perform());
    assertEquals(res, defender.getUnitsNumber());
    // System.out.println(displayer.display());

  }

  @Test
  public void test_perform() {
    test_perform_helper(108, 83);
    test_perform_helper(8, 21);
  }

}
