package edu.duke.ece651.teamX.shared;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Random;

import org.junit.jupiter.api.Test;

public class AttackTest {

  @Test
  public void test_perform() {
    Territory enemy5 = new Territory("enemy5", 100);
    Territory defender5 = new Territory("defender5", 3);

    Player p = new Player("a", 20);
    Map map = new Map();
    map.addTerritory(defender5, p);
    p.upgradeLevel();
    p.upgradeLevel();
    p.upgradeUnit(defender5, 0, 1);
    p.upgradeUnit(defender5, 2, 2);

    Random rand = new Random(0);
    Attack at1 = new Attack(enemy5.getUnits().get(0), defender5.getUnits().get(0));
    assertFalse(at1.unitAttack(rand, 20));

    Attack at2 = new Attack(enemy5.getUnits().get(0), defender5.getUnits().get(1));
    assertTrue(at2.unitAttack(rand, 20));

    Attack at3 = new Attack(enemy5.getUnits().get(0), defender5.getUnits().get(2));
    assertFalse(at3.unitAttack(rand, 20));

  }

}
