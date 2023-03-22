package edu.duke.ece651.teamX.shared;

import java.util.ArrayList;
import java.util.HashMap;

public class AttackProcessor {

  private ArrayList<MultiAttack> allAttack;
  private Map map;
  private AttackValidChecker checker;

  public AttackProcessor(ArrayList<AttackSender> _allAttack, Map _map)
      throws IllegalArgumentException {
    map = _map;
    try {
      checker = new AttackValidChecker(_allAttack, _map);
    } catch (IllegalArgumentException e) {
      throw e;
    }
//    checker = new AttackValidChecker(_allAttack, _map);
    allAttack = getAttackList(_allAttack);
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
      Territory defender = attack.getDestination();
      Territory enemy = attack.getSource();
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
    for (MultiAttack m : allAttack) {
      m.prepare();
    }
    for (MultiAttack m : allAttack) {
      m.perform();
    }
  }
}
