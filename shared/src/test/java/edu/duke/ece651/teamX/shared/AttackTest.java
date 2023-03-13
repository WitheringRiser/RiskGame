package edu.duke.ece651.teamX.shared;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class AttackTest {

  @Test
  public void test_perform() {
    Territory enemy1 = new Territory("enemy1");
    Territory enemy2 = new Territory("enemy2");
    Territory enemy3 = new Territory("enemy3");
    Territory defender1 = new Territory("defender1");
    Territory defender2 = new Territory("defender2");
    Territory defender3 = new Territory("defender3");
    enemy1.addUnits(null, 10);
    defender1.addUnits(null, 5);
    enemy2.addUnits(null, 5);
    defender2.addUnits(null, 10);
    enemy3.addUnits(null, 5);
    defender3.addUnits(null, 5);
    Attack at1 = new Attack(enemy1, defender1, 100);
    assertEquals(at1.perform(), true);
    // System.out.println(enemy1.getUnitsNumber());
    Attack at2 = new Attack(enemy2, defender2);
    assertEquals(at2.perform(), false);
    // System.out.println(enemy2.getUnitsNumber());
    Attack at3 = new Attack(enemy3, defender3, 10);
    assertEquals(at3.perform(), true);
    // System.out.println(enemy3.getUnitsNumber());
  }

}
