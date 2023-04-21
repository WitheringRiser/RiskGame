package edu.duke.ece651.teamX.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Player implements Serializable {

  public final String name;
  private final int unit_num; // The initial number of units

  private int food_resources;
  private int tech_resources;

  private int tech_level;
  private HashMap<Integer, Integer> researchRule;

  /**
   * Construct a Player Object for passing player related information
   *
   * @param n is the name of the Player assigned by server
   * @param u is the initial number of units set by server
   */
  public Player(String n, int u) {
    if (n == null) {
      throw new IllegalArgumentException("The input name should not be null");
    }
    if (u < 0) {
      throw new IllegalArgumentException(
          "The initial number of unit should not less than 0, but is " + u);
    }
    this.name = n;
    this.unit_num = u;

    this.tech_level = 1;

    this.food_resources = 100; // Temporarily set at 100
    this.tech_resources = 100; // Temporarily set at 100

    this.researchRule = new HashMap<Integer, Integer>();
    getResearchRule();
  }

  private void getResearchRule() {
    this.researchRule.put(1, 20);
    this.researchRule.put(2, 40);
    this.researchRule.put(3, 80);
    this.researchRule.put(4, 160);
    this.researchRule.put(5, 320);
  }

  public int getTechLevel() {
    return tech_level;
  }

  public String getName() {
    return this.name;
  }

  public int getUnitNum() {
    return this.unit_num;
  }

  public int getFoodResource() {
    return this.food_resources;
  }

  public int getTechResource() {
    return this.tech_resources;
  }

  public int getResearchNeedCost() {
    if(!this.researchRule.containsKey(tech_level)){
      throw new IllegalArgumentException("You are already in the hightest level "+tech_level);
    }
    return this.researchRule.get(tech_level);
  }

  /**
   * consume technology resources
   *
   * @param num: the number of technology resources need to be consumed
   * @return true if consume successfully, false otherwise
   */
  public boolean consumeTech(int num) {
    if (tech_resources < num) {
      return false;
    }
    tech_resources -= num;
    return true;
  }

  /**
   * consume food resources
   *
   * @param num: the number of food resources need to be consumed
   * @return true if consume successfully, false otherwise
   */
  public boolean consumeFood(int num) {
    if (food_resources < num) {
      return false;
    }
    food_resources -= num;
    return true;
  }

  /**
   * research to upgrade technology level
   *
   * @return true if upgrade successfully, false otherwise
   */
  public boolean upgradeLevel() {
    if (consumeTech(researchRule.get(tech_level))) {
      tech_level += 1;
      return true;
    }
    return false;
  }

  /**
   * upgrade one unit
   *
   * @param index:   the index of the unit
   * @param toLevel: the level the unit upgrade to
   * @return null if upgrade successfully, otherwise the string describe the
   *         reason for failure
   */
  public String upgradeUnit(Territory source, int index, int toLevel) {
    ArrayList<Unit> units = source.getUnits();
    if (tech_level < toLevel) {
      return "the maximum technology level doesn't permit this upgrade";
    }
    if (index < 0 || index >= units.size()) {
      return "the unit doesn't exist, please check the index";
    }
    Unit u = units.get(index);
    if (!consumeTech(u.getCost(toLevel))) {
      return "the technology resource is not enough to upgrade this unit";
    }
    if (!u.upgradeLevel(toLevel)) {
      return "the level is not legal or the unit has reached the highest level";
    }
    return null;
  }

  public void increaseAllResource(int num){
    this.food_resources += num;
    this.tech_resources += num;
  }
  
  /**
   * Check if two Player equals Compare name, number of units, and number of
   * territories
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o != null && o.getClass().equals(getClass())) {
      Player otherPlayer = (Player) o;
      return name.equals(otherPlayer.getName()) && unit_num == otherPlayer.getUnitNum();
    }
    return false;
  }

  @Override
  public int hashCode() {
    int ans = 0;
    for (int i = 0; i < name.length(); ++i) {
      ans = ans * 17 + name.charAt(i);
    }
    return ans;
  }
}
