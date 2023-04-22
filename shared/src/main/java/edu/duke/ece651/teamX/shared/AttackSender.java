package edu.duke.ece651.teamX.shared;

import java.util.ArrayList;

public class AttackSender extends ActionSender {

  public AttackSender(Territory _source, Territory _destination, int _unitsNum) {
    super(_source, _destination, _unitsNum);

  }

  public AttackSender(Territory _source, Territory _destination, int _unitsNum, String name) {
    super(_source, _destination, _unitsNum, name);
  }

}
