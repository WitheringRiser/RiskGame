package edu.duke.ece651.teamX.shared;

import java.util.ArrayList;
import java.util.Random;

public class Attack {
  private Unit attacker;
  private Unit defender;

  Attack(Unit _attacker, Unit _defender) {
    this.attacker = _attacker;
    this.defender = _defender;
  }

  public int rollDice(Random rand, int max, int bonus) {
    return rand.nextInt(max) + 1 + bonus;
  }

  /**
   * perform one unit attck
   */
  public boolean unitAttack(Random rand, int max) {
    if (attacker == null) {
      return false;
    }
    if (defender == null) {
      return true;
    }
    int enemyDice = rollDice(rand, max, attacker.getBonus());
    int defenderDice = rollDice(rand, max, defender.getBonus());
    if (enemyDice > defenderDice) {
      return true;
    } else {
      return false;
    }
  }
}
