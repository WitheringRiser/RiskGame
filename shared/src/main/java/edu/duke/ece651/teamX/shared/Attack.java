package edu.duke.ece651.teamX.shared;

import java.util.Random;

public class Attack extends BasicAction {
  private static Random rand = null;

  Attack(Territory _source, Territory _destination) {
    this(_source,_destination,0);
  }

  Attack(Territory _source, Territory _destination, int seed) {
    super(_source, _destination);
    if (rand == null) {
      rand = new Random(seed);
    }
  }

  public int rollDice() {
    return rand.nextInt(20) + 1;
  }

  /**
   * return true if attacker wins
   */
  @Override
  public boolean perform() {
    while (source.getUnitsNumber() > 0 && destination.getUnitsNumber() > 0) {
      int enemyDice = rollDice();
      int defenderDice = rollDice();
      if (enemyDice > defenderDice) {
        destination.removeUnits(1);
      } else {
        source.removeUnits(1);
      }
    }
    if (source.getUnitsNumber() == 0) {
      return false;
    } else {
      return true;
    }

  }

  @Override
  public boolean checkValid() {
    // TODO Auto-generated method stub
    return false;
  }

}
