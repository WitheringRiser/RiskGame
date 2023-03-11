package edu.duke.ece651.teamX.shared;

import java.io.Serializable;

public class Player implements Serializable {

  private final String name;
  private final int unit_num; // The initial number of units
  private final int territory_num; // The initial number of territory

  /**
   * Construct a Player Object for passing player related information
   *
   * @param n is the name of the Player assigned by server
   * @param u is the initial number of units set by server
   * @param t is the initial number of territories set by server
   */
  public Player(String n, int u, int t) {
    if (n == null) {
      throw new IllegalArgumentException("The input name should not be null");
    }
    if (u < 0) {
      throw new IllegalArgumentException(
          "The initial number of unit should not less than 0, but is " + u);
    }
    if (t < 0) {
      throw new IllegalArgumentException(
          "The initial number of territories should not less than 0, but is " + t);
    }
    this.name = n;
    this.unit_num = u;
    this.territory_num = t;
  }

  public String getName() {
    return this.name;
  }

  public int getUnitNum() {
    return this.unit_num;
  }

  public int getTerritoryNum() {
    return this.territory_num;
  }

  /**
   * Check if two Player equals Compare name, number of units, and number of territories
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o != null && o.getClass().equals(getClass())) {
      Player otherPlayer = (Player) o;
      return name.equals(otherPlayer.getName()) && unit_num == otherPlayer.getUnitNum() &&
          territory_num == otherPlayer.getTerritoryNum();
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
