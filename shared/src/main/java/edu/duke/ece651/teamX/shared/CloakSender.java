package edu.duke.ece651.teamX.shared;

import java.io.Serializable;

public class CloakSender implements Serializable {

  private Territory source;

  public CloakSender(Territory s) {
    source = s;
  }

  public Territory getSource() {
    return source;
  }
}
