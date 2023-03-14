package edu.duke.ece651.teamX.shared;

import java.util.ArrayList;
import java.util.Random;

public class Attack extends BasicAction {
  private static Random rand = null;
  final private int attackNum;

  Attack(Territory _source, Territory _destination) {
    this(_source, _destination, _source.getUnitsNumber(), 0);
  }

  Attack(Territory _source, Territory _destination, int num) {
    this(_source, _destination, num, 0);
  }

  Attack(Territory _source, Territory _destination, int num, int seed) {
    super(_source, _destination);
    if (rand == null) {
      rand = new Random(seed);
    }
    attackNum = num;
  }

  public int rollDice(int max) {
    return rand.nextInt(max) + 1;
  }

  /**
   * perform one unit attck
   */
  public void unitAttack(int max, ArrayList<Unit> attacker) {
    int enemyDice = rollDice(max);
    int defenderDice = rollDice(max);
    if (enemyDice > defenderDice) {
      destination.removeUnits(1);
    } else {
      attacker.remove(0);
    }
  }

  /**
   * enemyFight with each other
   * if ties, both enemy will not lose a unit
   * 
   * @param max: sides number of dice.
   */
  public void enemyFight(int max) {
    int enemyDice1 = rollDice(max);
    int enemyDice2 = rollDice(max);
    if (enemyDice1 > enemyDice2) {
      destination.removeUnits(1);
    } else if (enemyDice1 < enemyDice2) {
      source.removeUnits(1);
    }

  }

  /**
   * @return true if attacker wins
   */
  @Override
  public boolean perform() {
    ArrayList<Unit> attacker = source.removeUnits(attackNum);
    while (attacker.size() > 0 && destination.getUnitsNumber() > 0) {
      unitAttack(20, attacker);
    }
    if (attacker.size() == 0) {
      return false;
    } else {
      destination.setUnits(attacker);
      return true;
    }
  }

  @Override
  public boolean checkValid() {
    // TODO Auto-generated method stub
    return false;
  }

}
