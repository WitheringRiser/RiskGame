package edu.duke.ece651.teamX.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


public class Map implements Serializable {

  private HashMap<Territory, Player> map_dict;

  /**
   * Construct a map by initializing an empty hashmap
   */
  public Map() {
    this.map_dict = new HashMap<>();
  }

  public int getTerritoryNum() {
    return this.map_dict.size();
  }

  public HashMap<Territory, Player> getMap() {
    return map_dict;
  }

  /**
   * Check if the territory is already in the hashmap Note: We do not allow have multiple
   * territories with the same name
   *
   * @param terr is the territory to check
   * @return true is terr is in the hashmap and false otherwise
   */
  private boolean alreadyExist(Territory terr) {
    return map_dict.containsKey(terr);
  }

  /**
   * Add a new territory to the map if the territory is ok to add
   *
   * @param terr is the territory to add
   * @return true if the terr is ok to add and indicate the success of adding
   */
  public boolean addTerritory(Territory terr, Player player) {
    if (terr == null) {
      return false;
    }
    if (alreadyExist(terr)) {
      return false;
    }
    map_dict.put(terr, player);
    return true;
  }

  /**
   * Check the existence of the input terr and throw if not exists
   *
   * @param terr is the territory to check
   * @throw IllegalArgumentException if the input is null
   * @throw IllegalArgumentException if the input is not exist in the map
   */
  private void checkAndThrow(Territory terr) {
    if (terr == null) {
      throw new IllegalArgumentException("The input territory should not be null");
    }
    if (!alreadyExist(terr)) {
      throw new IllegalArgumentException("The input territory" + terr.getName() +
          " does not exist");
    }
  }

  /**
   * Change the owner player of the territory
   *
   * @param terr   is the territory to change ownership
   * @param player is the new owner of this territory
   * @return true to indicate success
   */
  public boolean setOwner(Territory terr, Player player) {
    if (terr == null) {
      return false;
    }
    if (!alreadyExist(terr)) {
      return false;
    }
    map_dict.put(terr, player);
    terr.resetCloak();//reset cloaking status for new owner
    return true;
  }

  /**
   * Get the owner of the input terr Will check the existence of the territory first
   *
   * @param terr is the territory we want to get the owner of
   * @return the owner of the territory. null indicates no owner
   * @throw IllegalArgumentException if the input is null
   * @throw IllegalArgumentException if the input is not exist in the map
   */
  public Player getOwner(Territory terr) {
    checkAndThrow(terr);
    return map_dict.get(terr);
  }

  /**
   * Get a territories of one player
   *
   * @param player is the target player
   * @return a list of Territory belongs to that player
   */
  public ArrayList<Territory> getTerritories(Player player) {
    ArrayList<Territory> territories = new ArrayList<Territory>();
    for (Territory t : map_dict.keySet()) {
      if (map_dict.get(t).equals(player)) {
        territories.add(t);
      }
    }
    return territories;
  }

  public ArrayList<Territory> getTerritoriesByPlayerName(String playerName) {
    ArrayList<Territory> territories = new ArrayList<Territory>();
    for (Territory t : map_dict.keySet()) {
      if (map_dict.get(t).getName().equals(playerName)) {
        territories.add(t);
      }
    }
    return territories;
  }

  /**
   * Set the units of a territory
   *
   * @param terr  is the territory to set units
   * @param units is the units to set
   * @return
   */
  public boolean setTerritoryUnits(Territory terr, ArrayList<Unit> units) {
    if (terr == null) {
      return false;
    }
    if (!alreadyExist(terr)) {
      return false;
    }
    for (Territory t : map_dict.keySet()) {
      if (t.equals(terr)) {
        t.setUnits(units);
      }
    }
    return true;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Map map = (Map) o;
    return Objects.equals(map_dict, map.map_dict);
  }

  @Override
  public int hashCode() {
    return Objects.hash(map_dict);
  }

  /**
   * Print out the map grouped by players, only for testing and debugging
   */
  public void printMap() {
    // get the set of players
    Set<Player> players = new HashSet<>();
    for (Territory t : map_dict.keySet()) {
      players.add(map_dict.get(t));
    }
    for (Player p : players) {
      System.out.println("Player " + p.getName() + " has territories: ");
      for (Territory t : getTerritories(p)) {
        System.out.println(t.getName() + ": " + t.getUnitsNumber() + " units");
      }
      System.out.println("------------------------\n");
    }
  }

  public Territory getTerritoryByName(String name) {
    for (Territory t : map_dict.keySet()) {
      if (t.getName().equals(name)) {
        return t;
      }
    }
    return null;
  }

  public Player getPlayerByName(String name) {
    for (Player p : map_dict.values()) {
      if (p.getName().equals(name)) {
        return p;
      }
    }
    return null;
  }

  public Iterable<Territory> getAllTerritories() {
    return map_dict.keySet();
  }

}
