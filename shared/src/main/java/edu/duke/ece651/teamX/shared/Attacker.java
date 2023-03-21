package edu.duke.ece651.teamX.shared;

import java.util.Random;

public class Attacker {

  private Territory attacker;
  final private Player player;
  private int num;
  private static Random rand = null;

  Attacker(Territory _attacker, int _num, Player player) {
    this(_attacker, _num, player, 0);
  }

  Attacker(Territory _attacker, int _num, Player _player, int seed) {
    attacker = _attacker;
    this.player = _player;
    num = _num;
    rand = new Random(seed);
  }

  public Territory getTerritory() {
    return attacker;
  }

  public int getAttackNumber() {
    return num;
  }

  public Player getOwner() {
    return player;
  }

  public boolean LosesAll() {
    return num == 0;
  }

  public void loseFight() {
    attacker.removeUnits(1);
    num -= 1;
  }

  public int rollDice(int max) {
    return rand.nextInt(max) + 1;
  }

  public void fight(Attacker another, int max) {
    int enemyDice1 = rollDice(max);
    int enemyDice2 = rollDice(max);
    if (enemyDice1 > enemyDice2) {
      another.loseFight();
    } else if (enemyDice1 < enemyDice2) {
      loseFight();
    }
  }
}
