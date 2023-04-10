package edu.duke.ece651.teamX.shared;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

public class AttackTest {

  @Test
  public void test_perform() {
    Territory enemy1 = new Territory("enemy1", 10);
    Territory enemy2 = new Territory("enemy2", 5);
    Territory enemy3 = new Territory("enemy3", 5);
    Territory enemy4 = new Territory("enemy4", 100);
    Territory enemy5 = new Territory("enemy5", 100);
    Territory defender1 = new Territory("defender1", 5);
    Territory defender2 = new Territory("defender2", 10);
    Territory defender3 = new Territory("defender3", 5);
    Territory defender4 = new Territory("defender4", 5);
    Territory defender5 = new Territory("defender5", 3);

    Attack at1 = new Attack(enemy1, defender1, enemy1.getUnitsNumber(), 100);
    assertEquals(at1.perform(), true);
    // System.out.println(enemy1.getUnitsNumber());
    Attack at2 = new Attack(enemy2, defender2);
    assertEquals(at2.perform(), false);
    // System.out.println(enemy2.getUnitsNumber());
    Attack at3 = new Attack(enemy3, defender3, enemy3.getUnitsNumber(), 10);
    assertEquals(at3.perform(), true);
    // System.out.println(enemy3.getUnitsNumber());
    Attack at4 = new Attack(enemy4, defender4, 1);
    assertEquals(at4.perform(), false);
    // System.out.println(enemy4.getUnitsNumber());
    Player p = new Player("a", 20);
    Map map = new Map();
    map.addTerritory(defender5, p);
    p.upgradeLevel();
    p.upgradeLevel();
    p.upgradeUnit(defender5, 0, 1);
    p.upgradeUnit(defender5, 2, 2);
    ArrayList<Integer> l = new ArrayList<>();
    l.add(0);
    l.add(3);
    l.add(8);
    Attack at5 = new Attack(enemy5, defender5, l, 0, null);
    assertTrue(at5.perform());
    assertEquals(defender5.getUnitsNumber(), 1);
    assertEquals(enemy5.getUnitsNumber(), 97);
  }

}
