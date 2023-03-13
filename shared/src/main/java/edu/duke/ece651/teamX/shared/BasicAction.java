package edu.duke.ece651.teamX.shared;

import java.util.ArrayList;

public abstract class BasicAction implements Action {
  protected Territory source;
  protected Territory destination;
  BasicAction(Territory _source, Territory _destination){
    source = _source;
    destination = _destination;
  }
}
