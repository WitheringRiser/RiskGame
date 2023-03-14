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

  public Territory getFinalAttacker() {
    int index1 = 0;
    int index2 = 1;
    while (enemies.size() > 1) {
      enemiesFight(enemies.get(index1), enemies.get(index2));
      index1 = (index1 + 1) % enemies.size();
      index2 = (index1 + 1) % enemies.size();
    }
    return enemies.get(0);
  }

  public Map perform() {
    Territory enemy = getFinalAttacker();
    Attack attack = new Attack(enemy, defender);
    if (attack.perform()) {
      Player p = map.getOwner(enemy);
      map.setOwner(defender, p);
    }
    return map;
  }
}
