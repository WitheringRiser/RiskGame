package edu.duke.ece651.teamX.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Territory implements Serializable {

  private ArrayList<Unit> units;
  private ArrayList<Territory> neighbours;
  private final String name; // a territory is uniquely defined by name
  private int cloaking;
  private ArrayList<Spy> spies;
  private int shield_level;
  private HashMap<Integer, Integer> shieldRule;

  /**
   * Constructs a Territory object with the specified name
   *
   * @param in_name is the name of the Territory
   */
  public Territory(String in_name) {
    this.units = new ArrayList<Unit>();
    this.neighbours = new ArrayList<Territory>();
    this.name = in_name;
    this.cloaking = 0;
    this.spies = new ArrayList<>();
    this.shield_level = 0;
    this.shieldRule = new HashMap<>();
    setShieldRule();
  }

  public Territory(String in_name, int num) {
    this(in_name);
    addUnits(num);
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

  public void setShieldRule() {
    shieldRule.put(1, 50);
    shieldRule.put(2, 100);
    shieldRule.put(3, 200);
    shieldRule.put(4, 400);
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

  public void addUnits(int number, String player_name) {
    for (int i = 0; i < number; ++i) {
      units.add(new AdvancedUnit(player_name));
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

  public ArrayList<Unit> removeUnitsFromList(ArrayList<Integer> indexList) {
    ArrayList<Unit> unitList = new ArrayList<Unit>();
    for (Integer i : indexList) {
      if (i < 0 || i >= units.size()) {
        throw new IllegalArgumentException("index is invalid");
      }
      unitList.add(units.get(i));
    }
    units.removeAll(unitList);
    return unitList;
  }

  public ArrayList<Unit> removeLevelUnits(String unitTypeName, int num) {
    HashMap<String, ArrayList<Integer>> unitDic = getUnitsDit();
    if (!unitDic.containsKey(unitTypeName)) {
      throw new IllegalArgumentException(name + " does not have " + unitTypeName);
    }
    ArrayList<Integer> indexs = unitDic.get(unitTypeName);
    if (indexs.size() < num) {
      throw new IllegalArgumentException(name + " does not have enough " + unitTypeName);
    }
    ArrayList<Integer> indexList = new ArrayList<Integer>();
    for (int i = 0; i < num; i++) {
      indexList.add(indexs.get(i));
    }
    return removeUnitsFromList(indexList);
  }

  public ArrayList<Unit> getUnits() {
    return units;
  }

  /**
   * Map from unit name to index Will be used in client side to display unit info
   *
   * @return the resulted map from name to index
   */
  public HashMap<String, ArrayList<Integer>> getUnitsDit() {
    HashMap<String, ArrayList<Integer>> resDic = new HashMap<>();
    for (int i = 0; i < units.size(); i++) {
      Unit u = units.get(i);
      ArrayList<Integer> inds;
      if (resDic.containsKey(u.getName())) {
        inds = resDic.get(u.getName());
      } else {
        inds = new ArrayList<>();
      }
      inds.add(i);
      resDic.put(u.getName(), inds);
    }
    return resDic;
  }

  public int getTerritorySize() {
    TerritorySize ts = new TerritorySize();
    try {
      return ts.getTerritorySize(name);
    } catch (NullPointerException e) {
      System.out.println("Territory name is not in the size map");
      return -1;
    }
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

  /**
   * Add cloaking cloaking times for this territory
   *
   * @throws IllegalArgumentException to prevent duplicates
   */
  public void addCloak() {
    if (cloaking > 0) {
      throw new IllegalArgumentException("This territory is already cloaked");
    }
    cloaking = 3;
  }

  /**
   * If cloaked --> reduce 1 time per turn
   */
  public void reduceOneCloak() {
    if (cloaking > 0) {
      cloaking -= 1;
    }
  }

  /**
   * If the territory change owner, it needs to be reseted
   */
  public void resetCloak() {
    cloaking = 0;
  }

  /**
   * Identify if this territory is cloaked for displaying
   *
   * @return boolean to indicate whether it is cloaked
   */
  public boolean isCloaked() {
    return cloaking > 0;
  }

  public int getUnitCountByLevel(int level) {
    int count = 0;
    for (Unit u : units) {
      if (u.getLevel() == level) {
        count++;
      }
    }
    return count;
  }

  /**
   * For display purpose
   * 
   * @param playerName
   * @return
   */
  public ArrayList<Integer> getSpyIndsFromPlayer(String playerName) {
    ArrayList<Integer> res = new ArrayList<>();
    for (int i = 0; i < spies.size(); i++) {
      if (spies.get(i).getOwner().equals(playerName)) {
        res.add(i);
      }
    }
    return res;
  }

  /**
   * For move spy
   * 
   * @param playerName
   * @return
   */
  public ArrayList<Integer> getSpyMoveIndsFromPlayer(String playerName) {
    ArrayList<Integer> inds = getSpyIndsFromPlayer(playerName);
    ArrayList<Integer> res = new ArrayList<>();
    for (int i : inds) {
      if (!spies.get(i).checkMove()) {
        res.add(i);
      }
    }
    return res;
  }

  public void addSpies(ArrayList<Spy> inputSpies) {
    for (Spy s : inputSpies) {
      this.spies.add(s);
    }
  }

  public ArrayList<Spy> removeSpies(String owner, int num) {
    ArrayList<Integer> inds = getSpyMoveIndsFromPlayer(owner);
    if (inds.size() < num) {
      throw new IllegalArgumentException("The Spy is not enough: have " + inds.size() + " but requested " + num);
    }
    ArrayList<Spy> resSpies = new ArrayList<>();
    for (int i = 0; i < num; i++) {
      Spy currS = spies.get(inds.get(i));
      currS.recordMove();
      resSpies.add(currS);
    }
    spies.removeAll(resSpies);
    return resSpies;
  }

  /**
   * Reset the movement info for spies
   */
  public void turnReset() {
    for (Spy s : spies) {
      s.turnReset();
    }
  }

  public int getShieldLevel() {
    return shield_level;
  }

  public int getShieldCost(int toLevel) {
    return shieldRule.get(toLevel);
  }

  public void shieldTerritory(int toLevel) {
    this.shield_level = toLevel;
  }

  public void releaseShield() {
    this.shield_level = 0;
  }

}
