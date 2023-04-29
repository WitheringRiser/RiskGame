package edu.duke.ece651.teamX.shared;

import java.util.ArrayList;

public class BreakerProcessor {

  private ArrayList<BreakerSender> allBreaker;
  private Map map;

  public BreakerProcessor(ArrayList<BreakerSender> _allBreaker, Map _map) {
    this.allBreaker = _allBreaker;
    this.map = _map;
  }

  public void resovleAllBreak() {
    for (BreakerSender s : allBreaker) {
      if (s != null) {
        Territory source = map.getTerritoryByName(s.getSource().getName());
        Player p = map.getOwner(source);
        if (p.consumeGold(source.getBreakerCost(s.getLevel()))) {
          source.addBreaker(s.getLevel());
        } else {
          throw new IllegalArgumentException("gold resources are not enough to buy the breaker");
        }
      }
    }
  }
}
