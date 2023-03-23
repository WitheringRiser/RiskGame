package edu.duke.ece651.teamX.shared;

import java.util.ArrayList;
<<<<<<< HEAD
import java.util.HashMap;

public class MoveProcessor {

  private ArrayList<MoveSender> moveSender;
  private Map map;
  private MoveValidChecker checker;

  public MoveProcessor(ArrayList<MoveSender> moveSender, Map _map)
      throws IllegalArgumentException {
    map = _map;
    try {
      checker = new AttackValidChecker(moveSender, _map);
    } catch (IllegalArgumentException e) {
      throw e;
    }
//    checker = new AttackValidChecker(_allAttack, _map);
    moveSender = getAttackList(moveSender);
  }

  public ArrayList<MultiAttack> getAttackList(ArrayList<AttackSender> _allAttack) {
    HashMap<Territory, ArrayList<Attacker>> hashmap = getHashMap(_allAttack);
    ArrayList<MultiAttack> allAttack = new ArrayList<MultiAttack>();
    for (Territory defender : hashmap.keySet()) {
      allAttack.add(new MultiAttack(hashmap.get(defender), defender, map));
    }
    return allAttack;
  }

  public HashMap<Territory, ArrayList<Attacker>> getHashMap(ArrayList<AttackSender> allAttack) {
    HashMap<Territory, ArrayList<Attacker>> ans = new HashMap<Territory, ArrayList<Attacker>>();
    for (int i = 0; i < allAttack.size(); ++i) {
      AttackSender attack = allAttack.get(i);

      Territory defender = map.getTerritoryByName(attack.getDestination().getName());
      Territory enemy = map.getTerritoryByName(attack.getSource().getName());

      int num = attack.getUnitsNum();

      if (ans.containsKey(defender)) {
        ans.get(defender).add(new Attacker(enemy, num, map.getOwner(enemy)));
      } else {
        ArrayList<Attacker> attackers = new ArrayList<Attacker>();
        attackers.add(new Attacker(enemy, num, map.getOwner(enemy)));
        ans.put(defender, attackers);
      }
    }
    return ans;
  }

  public void resovleAllAttack() {
    for (MultiAttack m : moveSender) {
      m.prepare();
    }
    for (MultiAttack m : moveSender) {
      m.perform();
=======

public class MoveProcessor {

  private Map map;
  private ArrayList<MoveSender> allMove;
  private MoveValidChecker checker;

  public MoveProcessor(ArrayList<MoveSender> _allMove, Map _map) throws IllegalArgumentException {
    map = _map;
    allMove = _allMove;

    checker = new MoveValidChecker(allMove, map);
//    checker.checkValid();
  }

  public void resolveAllMove() {
    for (MoveSender m : allMove) {
      Territory source = map.getTerritoryByName(m.getSource().getName());
      Territory destination = map.getTerritoryByName(m.getDestination().getName());
      int num = m.getUnitsNum();
//      set the arraylist of units with certain number
      source.removeUnits(num);
      destination.addUnits(null, num);
>>>>>>> c717b9b6543c5e29b4d16da7833a60dafcc6f41f
    }
  }
}
