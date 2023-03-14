package edu.duke.ece651.teamX.shared;

import java.util.ArrayList;

public class MultiAttack {
  private ArrayList<Territory> enemies;
  private Territory defender;

  MultiAttack(ArrayList<Territory> enemies, Territory defender) {
    this.enemies = enemies;
    this.defender = defender;
  }

  public void enemiesFight(Territory enemy1, Territory enemy2) {
    Attack attack = new Attack(enemy1, enemy2);
    // attack.unitAttack(20);
  }

  public boolean perform() {
    return true;
  }

}
