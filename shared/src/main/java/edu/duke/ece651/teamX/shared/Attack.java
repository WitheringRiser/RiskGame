package edu.duke.ece651.teamX.shared;

import java.util.ArrayList;
import java.util.Random;

public class Attack extends BasicAction {

  private static Random rand = null;
  private ArrayList<Unit> attacker;
  private Player enemyPlayer;

  Attack(Territory _source, Territory _destination) {
    this(_source, _destination, _source.getUnitsNumber(), 0, null);
  }

  Attack(Territory _source, Territory _destination, int num) {
    this(_source, _destination, num, 0, null);
  }

  Attack(Territory _source, Territory _destination, int num, int seed) {
    this(_source, _destination, num, seed, null);
  }

  Attack(Territory _source, Territory _destination, int num, int seed, Player player) {
    super(_source, _destination);

    rand = new Random(seed);

    attacker = source.removeUnits(num);
    enemyPlayer = player;
  }

  public ArrayList<Unit> getAttackerUnits() {
    return attacker;
  }

  public Player getEnemyPlayer() {
    return enemyPlayer;
  }

  public int rollDice(int max) {
    return rand.nextInt(max) + 1;
  }

  /**
   * perform one unit attck
   */
  public void unitAttack(int max) {
    int enemyDice = rollDice(max);
    int defenderDice = rollDice(max);
    if (enemyDice > defenderDice) {
      destination.removeUnits(1);
    } else {
      attacker.remove(0);
    }
  }

  /**
   * @return true if attacker wins
   */
  @Override
  public boolean perform() {
    int attackerSize = attacker.size();
    int destinationOriginalSize = destination.getUnitsNumber();

    while (attacker.size() > 0 && destination.getUnitsNumber() > 0) {
      unitAttack(20);
    }
    TextDisplayer.displayAttack(attackerSize, destinationOriginalSize, attacker.size(),
        destination.getUnitsNumber(),
        enemyPlayer);
    if (attacker.size() == 0) {
      return false;
    } else {
      destination.setUnits(attacker);
      return true;
    }
  }

}
