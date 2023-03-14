package edu.duke.ece651.teamX.shared;

import java.util.ArrayList;

public class MultiAttack {
  private ArrayList<Territory> enemies;
  private Territory defender;
  private Map map;

  MultiAttack(ArrayList<Territory> enemies, Territory defender, Map map) {
    this.enemies = enemies;
    this.defender = defender;
    this.map = map;
  }

  public void enemiesFight(Territory enemy1, Territory enemy2) {
    // assert (enemy1.getUnitsNumber() > 0 && enemy2.getUnitsNumber() > 0);
    Attack attack = new Attack(enemy1, enemy2);
    attack.enemyFight(20);
    if (enemy1.getUnitsNumber() == 0) {
      enemies.remove(enemy1);
    } else if (enemy2.getUnitsNumber() == 0) {
      enemies.remove(enemy2);
    }
  }

  public boolean onlyOneEnemy() {
    for (int i = 1; i < enemies.size(); ++i) {
      if (!map.getOwner(enemies.get(i)).equals(map.getOwner(enemies.get(0)))) {
        return false;
      }
    }
    return true;
  }

  public ArrayList<Territory> getFinalAttacker() {
    int index1 = 0;
    int index2 = 1;
    while (!onlyOneEnemy()) {
      enemiesFight(enemies.get(index1), enemies.get(index2));
      index1 = (index1 + 1) % enemies.size();
      index2 = (index1 + 1) % enemies.size();
    }
    return enemies;
  }

  public boolean perform() {
    ArrayList<Territory> enemy = getFinalAttacker();
    for (int i = 0; i < enemy.size(); ++i) {
      if (map.getOwner(defender).equals(map.getOwner(enemy.get(i)))) {
        ArrayList<Unit> move = enemy.get(i).removeUnits(enemy.get(i).getUnitsNumber());
        defender.addUnits(move);
      } else {
        Attack attack = new Attack(enemy.get(i), defender);
        if (attack.perform()) {
          Player p = map.getOwner(enemy.get(i));
          map.setOwner(defender, p);
        }
      }
    }
    return true;
  }
}
