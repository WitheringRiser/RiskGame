package edu.duke.ece651.teamX.shared;

import java.io.Serializable;

public abstract class Unit implements Serializable {

  protected int level;

  abstract public int getBonus();

  abstract public String getName();

  abstract public int getCost(int to);

  abstract public boolean upgradeLevel(int to);
}
