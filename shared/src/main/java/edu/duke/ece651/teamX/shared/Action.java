package edu.duke.ece651.teamX.shared;

public interface Action {

  /**
   * perform the action return true if succeed(like win in attack or move successfully return false
   * if fail
   */
  public boolean perform();

}
