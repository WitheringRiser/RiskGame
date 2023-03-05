package edu.duke.ece651.teamX.shared;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

public class Territory {

  private ArrayList<Unit> units;
  private ArrayList<Territory> neighbours;
  private String name;

  public Territory() {

  }

  public void addUnits(Unit unit, int number) {

  }

  public Iterator<Territory> getNeighbours() {
    return neighbours.iterator();
  }

  public int GetUnitsNumber() {
    return 0;
  }

  public Iterator<Unit> getUnits() {
    return units.iterator();
  }

  public boolean removeUnits(Unit unit) {
    return true;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Territory territory = (Territory) o;
    return Objects.equals(getUnits(), territory.getUnits()) && Objects
        .equals(getNeighbours(), territory.getNeighbours()) && Objects
        .equals(name, territory.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(getUnits(), getNeighbours(), name);
  }
}
