package edu.duke.ece651.teamX.shared;

import java.util.ArrayList;

public class ResearchProcessor {
  private ArrayList<ResearchSender> allResearch;
  private Map map;

  public ResearchProcessor(ArrayList<ResearchSender> _allRearch, Map map) {
    this.allResearch = _allRearch;
    this.map = map;
  }

  public void resovleAllResearch() {
    for (ResearchSender r : allResearch) {
      Player p = map.getPlayerByName(r.getPlayer().getName());
      if (!p.upgradeLevel()) {
        throw new IllegalArgumentException("technology resources are not enough to upgrade technology level");
      }
    }
  }
}
