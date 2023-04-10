package edu.duke.ece651.teamX.shared;

import java.util.ArrayList;
import java.util.Random;

public class Attack extends BasicAction {

  private Random rand = null;
  private ArrayList<Unit> attacker;
  private ArrayList<Unit> defender;
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
    defender = destination.removeUnits(destination.getUnitsNumber());
    enemyPlayer = player;
  }

  Attack(Territory _source, Territory _destination, ArrayList<Integer> indexList, int seed, Player player) {
    super(_source, _destination);

    rand = new Random(seed);

    attacker = source.removeUnitsFromList(indexList);
    defender = destination.removeUnits(destination.getUnitsNumber());
    enemyPlayer = player;
  }

  public ArrayList<Unit> getAttackerUnits() {
    return attacker;
  }

  public Player getEnemyPlayer() {
    return enemyPlayer;
  }

  public int rollDice(int max, int bonus) {
    return rand.nextInt(max) + 1 + bonus;
  }

  public int getHighestUnitIndex(ArrayList<Unit> units) {
    int res = 0;
    for (int i = 0; i < units.size(); ++i) {
      if (units.get(i).getBonus() > units.get(res).getBonus()) {
        res = i;
      }
    }
    return res;
  }

  public int getLowestUnitIndex(ArrayList<Unit> units) {
    int res = 0;
    for (int i = 0; i < units.size(); ++i) {
      if (units.get(i).getBonus() < units.get(res).getBonus()) {
        res = i;
      }
    }
    return res;
  }

  /**
   * perform one unit attck
   */
  public void unitAttack(Unit enemy, Unit def, int max) {
    int enemyDice = rollDice(max, enemy.getBonus());
    int defenderDice = rollDice(max, def.getBonus());
    if (enemyDice > defenderDice) {
      defender.remove(def);
    } else {
      attacker.remove(enemy);
    }
  }

  /**
   * @return true if attacker wins
   */
  @Override
  public boolean perform() {
    int attackerSize = attacker.size();
    int destinationOriginalSize = defender.size();
    int i = 0;
    while (attacker.size() > 0 && defender.size() > 0) {
      if (i % 2 == 0) {
        unitAttack(attacker.get(getHighestUnitIndex(attacker)), defender.get(getLowestUnitIndex(defender)), 20);
        i = 1;
      } else {
        unitAttack(attacker.get(getLowestUnitIndex(attacker)), defender.get(getHighestUnitIndex(defender)), 20);
        i = 0;
      }
    }
    TextDisplayer.displayAttack(attackerSize, destinationOriginalSize, attacker.size(),
        defender.size(), enemyPlayer);
    if (attacker.size() == 0) {
      destination.setUnits(defender);
      return false;
    } else {
      destination.setUnits(attacker);
      return true;
    }
  }

}
