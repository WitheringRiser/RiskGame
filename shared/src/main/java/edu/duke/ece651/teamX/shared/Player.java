package edu.duke.ece651.teamX.shared;

import java.io.Serializable;

public class Player implements Serializable {

  private final String name;
  private final int unit_num;  // The initial number of units

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
  }

  public String getName() {
    return this.name;
  }

  public int getUnitNum() {
    return this.unit_num;
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
