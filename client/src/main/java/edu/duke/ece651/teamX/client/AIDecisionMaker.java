package edu.duke.ece651.teamX.client;

import edu.duke.ece651.teamX.shared.Map;
import edu.duke.ece651.teamX.shared.Player;

public class AIDecisionMaker {

  private Map map;
  private ClientAttack clientAttack;
  private ClientMove clientMove;
  private ClientResearch clientResearch;
  private ClientUpgrade clientUpgrade;

  private Player this_player;

  public AIDecisionMaker(Map map, ClientAttack clientAttack, ClientMove clientMove,
      ClientResearch clientResearch, ClientUpgrade clientUpgrade, Player this_player) {
    this.map = map;
    this.clientAttack = clientAttack;
    this.clientMove = clientMove;
    this.clientResearch = clientResearch;
    this.clientUpgrade = clientUpgrade;
    this.this_player = this_player;
  }


  public void make_decision() {
    this.clientResearch.perform(false);
  }
}
