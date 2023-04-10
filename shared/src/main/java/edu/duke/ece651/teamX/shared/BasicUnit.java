package edu.duke.ece651.teamX.shared;

import java.util.HashMap;

public class BasicUnit extends Unit {
  private HashMap<Integer, String> nameMap;
  private HashMap<Integer, Integer> bonusMap;
  private HashMap<Integer, Integer> costMap;

  public BasicUnit() {
    level = 0;
    nameMap = new HashMap<>();
    bonusMap = new HashMap<>();
    costMap = new HashMap<>();
    getNameMap();
    getBonusMap();
    getCostMap();
  }

  private void getNameMap() {
    nameMap.put(0, "level_0_unit");
    nameMap.put(1, "level_1_unit");
    nameMap.put(2, "level_2_unit");
    nameMap.put(3, "level_3_unit");
    nameMap.put(4, "level_4_unit");
    nameMap.put(5, "level_5_unit");
    nameMap.put(6, "level_6_unit");
  }

  private void getBonusMap() {
    bonusMap.put(0, 0);
    bonusMap.put(1, 1);
    bonusMap.put(2, 3);
    bonusMap.put(3, 5);
    bonusMap.put(4, 8);
    bonusMap.put(5, 11);
    bonusMap.put(6, 15);
  }

  private void getCostMap() {
    costMap.put(0, 0);
    costMap.put(1, 3);
    costMap.put(2, 11);
    costMap.put(3, 30);
    costMap.put(4, 55);
    costMap.put(5, 90);
    costMap.put(6, 140);
  }

  @Override
  public int getBonus() {
    return bonusMap.get(level);
  }

  @Override
  public String getName() {
    return nameMap.get(level);
  }

  @Override
  public int getCost(int to) {
    if (to < level || to > 6) {
      return -1;
    }
    return costMap.get(to) - costMap.get(level);
  }

  @Override
  public boolean upgradeLevel(int to) {
    if (level >= 6 || to < level || to > 6) {
      return false;
    }
    level = to;
    return true;
  }

}
