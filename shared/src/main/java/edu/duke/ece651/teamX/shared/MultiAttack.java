package edu.duke.ece651.teamX.shared;

import java.util.ArrayList;
import java.util.Random;

public class MultiAttack {

  private ArrayList<Attacker> enemies;
  private Territory destination;
  private Map map;
  private Random rand;

  MultiAttack(ArrayList<Attacker> enemies, Territory defender, Map map, Random rand) {
    this.enemies = enemies;
    this.enemies.add(new Attacker(defender.removeUnits(defender.getUnitsNumber()), map.getOwner(defender)));
    this.destination = defender;
    this.map = map;
    this.rand = rand;
  }

  private Unit getHighestUnit(ArrayList<Unit> units) {
    int res = 0;
    for (int i = 0; i < units.size(); ++i) {
      if (units.get(i).getBonus() > units.get(res).getBonus()) {
        res = i;
      }
    }
    return units.get(res);
  }

  private Unit getLowestUnit(ArrayList<Unit> units) {
    int res = 0;
    for (int i = 0; i < units.size(); ++i) {
      if (units.get(i).getBonus() < units.get(res).getBonus()) {
        res = i;
      }
    }
    return units.get(res);
  }

  private boolean onlyOneEnemy() {
    for (int i = 1; i < enemies.size(); ++i) {
      if (!enemies.get(i).getOwner().equals(enemies.get(0).getOwner())) {
        return false;
      }
    }
    return true;
  }

  private void attackOneTurn(int index1, int index2) {
    Attacker a1 = enemies.get(index1);
    Attacker a2 = enemies.get(index2);
    if (a1.getOwner().equals(a2.getOwner())) {
      return;
    }
    Unit attacker = getHighestUnit(a1.getAttacker());
    Unit defender = getLowestUnit(a2.getAttacker());
    Attack attack = new Attack(attacker, defender);
    if (attack.unitAttack(rand, 20)) {
      a2.remove(defender);
      if (a2.LosesAll()) {
        enemies.remove(a2);
      }
    } else {
      a1.remove(attacker);
      if (a1.LosesAll()) {
        enemies.remove(a1);
      }
    }
  }

  public ArrayList<Attacker> getFinalAttacker() {
    int index1 = 0;
    int index2 = 1;
    while (!onlyOneEnemy()) {
      attackOneTurn(index1, index2);
      index1 = (index1 + 1) % enemies.size();
      index2 = (index1 + 1) % enemies.size();
    }
    return enemies;
  }

  public void perform() {
    TextDisplayer.displayAttackOriginal(destination, enemies, map);
    ArrayList<Attacker> res = getFinalAttacker();
    for (Attacker a : res) {
      destination.addUnits(a.getAttacker());
      map.setOwner(destination, a.getOwner());
    }
    TextDisplayer.displayAttackAfter(destination, map);
  }
}
