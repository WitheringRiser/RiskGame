package edu.duke.ece651.teamX.shared;

import java.util.ArrayList;

public class MultiAttack {
  private ArrayList<Attacker> enemies;
  private Territory defender;
  private Map map;
  private ArrayList<Attack> attackList = null;

  MultiAttack(ArrayList<Attacker> enemies, Territory defender, Map map) {
    this.enemies = enemies;
    this.defender = defender;
    this.map = map;
  }

  public void enemiesFight(Attacker enemy1, Attacker enemy2) {
    // assert (enemy1.getUnitsNumber() > 0 && enemy2.getUnitsNumber() > 0);
    enemy1.fight(enemy2, 20);
    if (enemy1.LosesAll()) {
      enemies.remove(enemy1);
    } else if (enemy2.LosesAll()) {
      enemies.remove(enemy2);
    }
  }

  public boolean onlyOneEnemy() {
    for (int i = 1; i < enemies.size(); ++i) {
      if (!map.getOwner(enemies.get(i).getTerritory()).equals(map.getOwner(enemies.get(0).getTerritory()))) {
        return false;
      }
    }
    return true;
  }

  public ArrayList<Attacker> getFinalAttacker() {
    int index1 = 0;
    int index2 = 1;
    while (!onlyOneEnemy()) {
      enemiesFight(enemies.get(index1), enemies.get(index2));
      index1 = (index1 + 1) % enemies.size();
      index2 = (index1 + 1) % enemies.size();
    }
    return enemies;
  }

  public void prepare() {
    attackList = new ArrayList<Attack>();
    ArrayList<Attacker> enemy = getFinalAttacker();
    for (int i = 0; i < enemy.size(); ++i) {
      Territory attackerTerritory = enemy.get(i).getTerritory();
      Player enemyPlayer = enemy.get(i).getOwner();
      Attack attack = new Attack(attackerTerritory, defender, enemy.get(i).getAttackNumber(), 0, enemyPlayer);
      attackList.add(attack);
    }
  }

  public boolean perform() {
    // assert (attackList != null && attackList.size() > 0);
    if (attackList == null || attackList.size() == 0) {
      throw new IllegalArgumentException("need to call prepare() before perform()");
    }
    for (int i = 0; i < attackList.size(); ++i) {
      Attack attack = attackList.get(i);
      Player player = attack.getEnemyPlayer();
      if (map.getOwner(defender).equals(player)) {
        ArrayList<Unit> move = attack.getAttackerUnits();
        defender.addUnits(move);
      } else {
        if (attack.perform()) {
          map.setOwner(defender, player);
        }
      }
    }
    return true;
  }
}
