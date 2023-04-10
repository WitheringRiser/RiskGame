package edu.duke.ece651.teamX.shared;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class AttackProcessor {

  private ArrayList<MultiAttack> allAttack;
  private Map map;
  private Random rand;

  public AttackProcessor(ArrayList<AttackSender> _allAttack, Map _map)
      throws IllegalArgumentException {
    map = _map;
    try {
      AttackValidChecker checker = new AttackValidChecker(_allAttack, _map);
    } catch (IllegalArgumentException e) {
      throw e;
    }
    rand = new Random();
    this.allAttack = getAttackList(_allAttack);
  }

  public AttackProcessor(ArrayList<AttackSender> _allAttack, Map _map, int seed)
      throws IllegalArgumentException {
    map = _map;
    try {
      AttackValidChecker checker = new AttackValidChecker(_allAttack, _map);
    } catch (IllegalArgumentException e) {
      throw e;
    }
    rand = new Random(seed);
    this.allAttack = getAttackList(_allAttack);
  }

  private ArrayList<MultiAttack> getAttackList(ArrayList<AttackSender> _allAttack) {
    HashMap<Territory, ArrayList<Attacker>> hashmap = getHashMap(_allAttack);
    ArrayList<MultiAttack> allAttack = new ArrayList<MultiAttack>();
    for (Territory defender : hashmap.keySet()) {
      allAttack.add(
          new MultiAttack(hashmap.get(defender), map.getTerritoryByName(defender.getName()), map,
              rand));
    }
    return allAttack;
  }

  private HashMap<Territory, ArrayList<Attacker>> getHashMap(ArrayList<AttackSender> allAttack) {
    HashMap<Territory, ArrayList<Attacker>> ans = new HashMap<Territory, ArrayList<Attacker>>();
    for (int i = 0; i < allAttack.size(); ++i) {
      AttackSender attack = allAttack.get(i);
      Territory defender = map.getTerritoryByName(attack.getDestination().getName());
      Territory enemy = map.getTerritoryByName(attack.getSource().getName());
      ArrayList<Unit> attacker = enemy.removeUnitsFromList(attack.getIndexList());

      if (ans.containsKey(defender)) {
        ans.get(defender).add(new Attacker(attacker, map.getOwner(enemy)));
      } else {
        ArrayList<Attacker> attackers = new ArrayList<Attacker>();
        attackers.add(new Attacker(attacker, map.getOwner(enemy)));
        ans.put(defender, attackers);
      }
    }
    return ans;
  }

  public void resovleAllAttack() {
    for (MultiAttack m : allAttack) {
      m.perform();
    }
  }
}
