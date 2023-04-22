package edu.duke.ece651.teamX.shared;

import java.util.ArrayList;

public class MoveSender extends ActionSender {

  public MoveSender(Territory _source, Territory _destination, int _unitsNum) {
    super(_source, _destination, _unitsNum);
  }

  public MoveSender(Territory _source, Territory _destination, int _unitsNum, String name) {
    super(_source, _destination, _unitsNum, name);
  }
}
