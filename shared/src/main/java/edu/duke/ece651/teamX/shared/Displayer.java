package edu.duke.ece651.teamX.shared;

public abstract class Displayer<T> {

  final protected Map map;

  Displayer(Map _toDisplay) {
    map = _toDisplay;
  }

  public abstract T display();

}
