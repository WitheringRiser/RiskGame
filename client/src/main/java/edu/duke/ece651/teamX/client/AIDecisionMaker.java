package edu.duke.ece651.teamX.client;

import edu.duke.ece651.teamX.shared.BasicUnit;
import edu.duke.ece651.teamX.shared.Map;
import edu.duke.ece651.teamX.shared.Player;
import edu.duke.ece651.teamX.shared.Territory;
import edu.duke.ece651.teamX.shared.Unit;
import java.util.ArrayList;
import java.util.HashMap;

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

  /**
   * Evaluation how "strong" a territory is based on the number and level of units
   *
   * @param terr the territory to be evaluated
   * @return the evaluation score
   */
  private int getTerritoryEvaluation(Territory terr) {
    int score = 0;
    for (Unit unit : terr.getUnits()) {
      if (unit instanceof BasicUnit) {
        score += unit.getBonus();
      }
    }
    return score;
  }

  /**
   * Get the boarder territories. Units are better put in the boarder for defending and attacking.
   *
   * @return the boarder territories
   */
  private ArrayList<Territory> getBoarderTerritories() {
    ArrayList<Territory> boarderTerritories = new ArrayList<>();
    for (Territory terr : map.getTerritories(this_player)) {
      if (map.getOwner(terr).equals(this_player)) {
        if (this.clientAttack.findDestTerrs(terr).size() != 0) {
          boarderTerritories.add(terr);
        }
      }
    }
    return boarderTerritories;
  }

  /**
   * Find the best source territory to attack the enemy
   *
   * @return the best source territory
   */
  private Territory findBestAttackingSourceTerritory() {
    int enemyLowestTerrEval = Integer.MAX_VALUE;
    Territory bestAttackSource = null;
    for (Territory terr : getBoarderTerritories()) {
      for (Territory enemy : clientAttack.findDestTerrs(terr)) {
        if (getTerritoryEvaluation(enemy) < enemyLowestTerrEval) {
          bestAttackSource = terr;
        }
      }
    }
    return bestAttackSource;
  }

  /**
   * Get the statistics of all basic units owned by this player in the map
   *
   * @return a hashmap of the statistics, key is level, value is number of units with this level
   */
  private HashMap<Integer, Integer> getBasicUnitStats() {
    HashMap<Integer, Integer> basicUnitStats = new HashMap<>();
    for (Territory terr : map.getTerritories(this_player)) {
      for (Unit unit : terr.getUnits()) {
        if (unit instanceof BasicUnit) {
          if (basicUnitStats.containsKey(unit.getLevel())) {
            basicUnitStats.put(unit.getLevel(), basicUnitStats.get(unit.getLevel()) + 1);
          } else {
            basicUnitStats.put(unit.getLevel(), 1);
          }
        }
      }
    }
    return basicUnitStats;
  }


  /**
   * Get the number of basic units owned by this player in the map
   *
   * @return the number of basic units
   */
  private int getBasicUnitNum() {
    int num = 0;
    for (Territory terr : map.getTerritories(this_player)) {
      for (Unit unit : terr.getUnits()) {
        if (unit instanceof BasicUnit) {
          num++;
        }
      }
    }
    return num;
  }

  /**
   * Get the number of basic units with certain level owned by this player in the map
   *
   * @param level the level of the basic unit
   * @return the number of basic units with this level
   */
  private int getUnitNumByLevel(int level) {
    HashMap<Integer, Integer> basicUnitStats = getBasicUnitStats();
    return basicUnitStats.getOrDefault(level, 0);
  }

  private void upgradeOneTerritory(Territory terr, int current_level) {
    for (int count_level = terr.getUnitCountByLevel(current_level); count_level >= 0;
        count_level--) {
      try {
        HashMap<String, ArrayList<Integer>> upgradeDic = new HashMap<>();

        ArrayList<Integer> upInfo = new ArrayList<>();
        upInfo.add(count_level);
        upInfo.add(this_player.getTechLevel());

        upgradeDic.put("level_" + current_level + "_unit", upInfo);

        clientUpgrade.perform(terr, upgradeDic);
        break;
      } catch (Exception ignored) {
      }
    }
  }

  private void doUpgrade() {
    // prioritize to upgrade the boarder territories
    for (Territory terr : getBoarderTerritories()) {
      for (int level = this_player.getTechLevel() - 1; level >= 0; level--) {
        upgradeOneTerritory(terr, level);
      }
    }

    // upgrade the non-boarder territories
    for (Territory terr : map.getTerritories(this_player)) {
      if (!getBoarderTerritories().contains(terr)) {
        for (int level = this_player.getTechLevel() - 1; level >= 0; level--) {
          upgradeOneTerritory(terr, level);
        }
      }
    }

  }

  /**
   * Decide to upgrade or do research based on the current tech resources
   *
   * @param controlConstant the control constant, must be between 0 and 1
   */
  private void decideUpgradeOrResearch(double controlConstant) {
    if (getUnitNumByLevel(this_player.getTechLevel()) < getBasicUnitNum() * controlConstant) {
      System.out.println("doing upgrade!");
      doUpgrade();
    } else {
      System.out.println("doing research!");
      this.clientResearch.perform(false);
    }
  }


  public void make_decision() {
    decideUpgradeOrResearch(0.1);
    getBoarderTerritories();
  }
}
