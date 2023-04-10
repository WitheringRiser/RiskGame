package edu.duke.ece651.teamX.shared;

import java.util.ArrayList;

public class Attacker {

  private ArrayList<Unit> attacker;
  final private Player player;

  Attacker(ArrayList<Unit> _attacker, Player _player) {
    this.attacker = _attacker;
    this.player = _player;
  }

  public ArrayList<Unit> getAttacker() {
    return attacker;
  }

  public Player getOwner() {
    return player;
  }

  public boolean LosesAll() {
    return attacker.size() == 0;
  }

  public boolean remove(Unit unit) {
    return attacker.remove(unit);
  }
}
