package edu.duke.ece651.teamX.shared;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Random;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class MultiAttackTest {

  @Test
  public void test_AttackOneOwener() {
    Territory t1 = new Territory("duke", 10);
    Territory t2 = new Territory("cary", 8);
    Territory t3 = new Territory("rdu", 7);

    Territory defender = new Territory("nyu", 15);

    Player p1 = new Player("zhou", 20);
    Player p5 = new Player("defender", 15);

    Map map = new Map();
    map.addTerritory(t1, p1);
    map.addTerritory(t2, p1);
    map.addTerritory(t3, p1);
    map.addTerritory(defender, p5);

    TextDisplayer displayer = new TextDisplayer(map);
    System.out.println(displayer.display());

    ArrayList<Attacker> enemies = new ArrayList<Attacker>();
    enemies.add(new Attacker(t1.removeUnits(10), p1));
    enemies.add(new Attacker(t2.removeUnits(5), p1));
    enemies.add(new Attacker(t3.removeUnits(5), p1));
    Random rand = new Random(0);
    MultiAttack ma = new MultiAttack(enemies, defender, map, rand);
    ma.perform();
    assertEquals(2, defender.getUnitsNumber());
    System.out.println(displayer.display());

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
    enemies.add(new Attacker(t1.removeUnits(num - 3), p1));
    enemies.add(new Attacker(t2.removeUnits(5), p2));
    enemies.add(new Attacker(t3.removeUnits(5), p3));
    enemies.add(new Attacker(t4.removeUnits(2), p4));
    Random rand = new Random(0);
    MultiAttack ma = new MultiAttack(enemies, defender, map, rand);
    ma.perform();
    assertEquals(res, defender.getUnitsNumber());
    System.out.println(displayer.display());

  }
  @Test
  public void test_AttackWithBonus() {
    Territory t1 = new Territory("duke", 10);
    Territory t2 = new Territory("cary", 8);
    Territory t3 = new Territory("rdu", 7);

    Territory defender = new Territory("nyu", 15);

    Player p1 = new Player("zhou", 20);
    Player p5 = new Player("defender", 15);

    Map map = new Map();
    map.addTerritory(t1, p1);
    map.addTerritory(t2, p1);
    map.addTerritory(t3, p1);
    map.addTerritory(defender, p5);

    TextDisplayer displayer = new TextDisplayer(map);
    System.out.println(displayer.display());

    p1.upgradeLevel();
    p1.upgradeLevel();
    p1.upgradeUnit(t1, 0, 1);
    p1.upgradeUnit(t1, 2, 2);

    p5.upgradeLevel();
    p5.upgradeLevel();
    p5.upgradeUnit(defender, 0, 1);
    p5.upgradeUnit(defender, 2, 2);

    ArrayList<Attacker> enemies = new ArrayList<Attacker>();
    enemies.add(new Attacker(t1.removeUnits(10), p1));
    enemies.add(new Attacker(t2.removeUnits(5), p1));
    enemies.add(new Attacker(t3.removeUnits(5), p1));
    Random rand = new Random(0);
    MultiAttack ma = new MultiAttack(enemies, defender, map, rand);
    ma.perform();
    assertEquals(1, defender.getUnitsNumber());
    System.out.println(displayer.display());

  }
  @Test
  public void test_mutltiplayer_perform() {
    test_perform_helper(108, 75);
    test_perform_helper(8, 23);
  }
  

}
