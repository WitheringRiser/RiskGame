package edu.duke.ece651.teamX.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Territory implements Serializable {

  private ArrayList<Unit> units;
  private ArrayList<Territory> neighbours;
  private final String name; // a territory is uniquely defined by name

  private int food_reources;
  private int tech_resources;

  private int tech_level;
  private HashMap<Integer, Integer> researchRule;

  /**
   * Constructs a Territory object with the specified name
   *
   * @param in_name is the name of the Territory
   */
  public Territory(String in_name) {
    this.units = new ArrayList<Unit>();
    this.neighbours = new ArrayList<Territory>();
    this.name = in_name;
    this.tech_level = 1;

    this.food_reources = 100; // Temporarily set at 100
    this.tech_resources = 100; // Temporarily set at 100

    this.researchRule = new HashMap<Integer, Integer>();
    getResearchRule();
  }

  public Territory(String in_name, int num) {
    this(in_name);
    addUnits(num);
  }

  private void getResearchRule() {
    this.researchRule.put(1, 20);
    this.researchRule.put(2, 40);
    this.researchRule.put(3, 80);
    this.researchRule.put(4, 160);
    this.researchRule.put(5, 320);
  }

  /**
   * Get the name of a territory
   */
  public String getName() {
    return name;
  }

  public int getUnitsNumber() {
    return units.size();
  }

  public Iterator<Territory> getNeighbours() {
    return neighbours.iterator();
  }

  public int getTechLevel() {
    return tech_level;
  }
  /*
   * public Iterator<Unit> getUnits() {
   * return units.iterator();
   * }
   */

  public boolean hasNeighbor(Territory t) {
    return neighbours.contains(t);
  }

  public void addUnits(Unit unit, int number) {
    for (int i = 0; i < number; ++i) {
      units.add(unit);
    }
  }

  public void addUnits(int number) {
    for (int i = 0; i < number; ++i) {
      units.add(new BasicUnit());
    }
  }

  public void addUnits(ArrayList<Unit> list) {
    units.addAll(list);
  }

  public void setUnits(ArrayList<Unit> units) {
    this.units = units;
  }

  public boolean addNeighbors(Territory t) {
    if (!hasNeighbor(t)) {
      neighbours.add(t);
      if (!t.hasNeighbor(this)) {
        t.addNeighbors(this);
      }
      return true;
    }
    return false;
  }

  public ArrayList<Unit> removeUnits(int number) {
    ArrayList<Unit> unitList = new ArrayList<Unit>();
    while (unitList.size() < number) {
      if (getUnitsNumber() < 1) {
        throw new IllegalArgumentException("can not remove unit from empty units");
      }
      unitList.add(units.remove(0));
    }
    return unitList;
  }

  public ArrayList<Unit> getUnits() {
    return units;
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
  public String upgradeUnit(int index, int toLevel) {
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

  /**
   * Check if two territory are equals Currently only use name to compare
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o != null && o.getClass().equals(getClass())) {
      Territory otherTerr = (Territory) o;
      return name.equals(otherTerr.getName());
    }
    return false;
  }

  /**
   * Use name to generate hashcode since name is unique and fixed
   */
  @Override
  public int hashCode() {
    return name.hashCode();
  }
}
