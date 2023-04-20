package edu.duke.ece651.teamX.client;

import java.io.IOException;

public interface ClientAction {

  /**
   * Perfom an action of this action type
   */
  // public void perform() throws IOException, ClassNotFoundException;

  /**
   * Complete a sequence of action and send to server
   */
  public void commit() throws IOException;

}
