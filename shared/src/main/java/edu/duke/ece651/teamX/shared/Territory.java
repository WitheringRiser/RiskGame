package edu.duke.ece651.teamX.shared;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

public class Territory {
  private ArrayList<Unit> units;
  private ArrayList<Territory> neighbours;
  private final String name;  // a territory is uniquely defined by name

  /**
   *Constructs a Territory object with the specified name
   *@param in_name is the name of the Territory
   */
  public Territory(String in_name) {
    this.units = new ArrayList<Unit>();
    this.neighbours = new ArrayList<Territory>();
    this.name = in_name;
  }

  /**
   *Get the name of a territory
   */
  public String getName() { return name; }
  public int getUnitsNumber() { return units.size(); }
  public Iterator<Territory> getNeighbours() { return neighbours.iterator(); }
  public Iterator<Unit> getUnits() { return units.iterator(); }

  public void addUnits(Unit unit, int number) {}

  public boolean removeUnits(Unit unit) { return true; }

  /**
   *Check if two territory are equals
   *Currently only use name to compare
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o != null && o.getClass().equals(getClass())) {
      Territory otherTerr = (Territory)o;
      return name.equals(otherTerr.getName());
    }
    return false;
  }
  /**
   *Use name to generate hashcode since name is unique and fixed
   */
  @Override
  public int hashCode() {
    return name.hashCode();
  }
}