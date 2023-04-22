package edu.duke.ece651.teamX.shared;

import java.util.ArrayList;
import java.util.HashMap;

public class AttackValidChecker {

  private ArrayList<AttackSender> allAttack;
  private Map map;

  AttackValidChecker(ArrayList<AttackSender> _allAttack, Map _map) throws IllegalArgumentException {
    map = _map;
    allAttack = _allAttack;
    checkUnitAndAdjacent();
    checkTotalAttackNum();
    checkIndex();
  }

  public void checkIndex() {
    for (AttackSender attack : allAttack) {
      Territory source = map.getTerritoryByName(attack.getSource().getName());
      ArrayList<Integer> indexList = source.getUnitsDit().get(attack.getName());
      if (indexList == null || indexList.size() < attack.getUnitsNum()) {
        throw new IllegalArgumentException("units are not enough to attack");
      }
    }
  }

  public void checkUnitAndAdjacent() {
    for (AttackSender attack : allAttack) {
      Territory source = map.getTerritoryByName(attack.getSource().getName());
      Territory destination = map.getTerritoryByName(attack.getDestination().getName());

      int num = attack.getUnitsNum();
      if (num > source.getUnitsNumber()) {
        throw new IllegalArgumentException(
            "attack units number is bigger than the territory actually has!");
      }
      if (source.equals(destination)) {
        throw new IllegalArgumentException("can not attack self");
      }
      if (map.getOwner(destination).equals(map.getOwner(source))) {
        throw new IllegalArgumentException("can not attack owned territory");
      }
      if (!source.hasNeighbor(destination)) {
        throw new IllegalArgumentException("can not attack non-adjacent territory");
      }
    }
  }

  public void checkTotalAttackNum() {
    HashMap<Territory, Integer> ans = new HashMap<Territory, Integer>();
    for (int i = 0; i < allAttack.size(); ++i) {
      AttackSender attack = allAttack.get(i);
      Territory enemy = map.getTerritoryByName(attack.getSource().getName());
      int num = attack.getUnitsNum();
      if (ans.containsKey(enemy)) {
        int sum = ans.get(enemy);
        ans.put(enemy, sum + num);
      } else {
        ans.put(enemy, num);
      }
      if (ans.get(enemy) > enemy.getUnitsNumber()) {
        throw new IllegalArgumentException(
            "total number of attack units are bigger than the units number the territory actually has!");
      }
    }
  }
}
