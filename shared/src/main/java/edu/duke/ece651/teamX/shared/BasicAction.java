package edu.duke.ece651.teamX.shared;

public abstract class BasicAction implements Action {

  @Override
  public boolean checkValid() {
    return false;
  }

  @Override
  public void perform() {

  }
}
