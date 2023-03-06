package edu.duke.ece651.teamX.shared;

import java.util.HashMap;

public class Map {
  private HashMap<Territory, Player> map_dict;

  /**
   * Construct a map by initialzing an empty hashmap
   */
  public Map() {
    this.map_dict = new HashMap<Territory, Player>();
  }

  public int getTerritoryNum() {
    return this.map_dict.size();
  }

  public HashMap<Territory, Player> getMap() {
    return map_dict;
  }

  /**
   * Check if the territory is already in the hashmap
   * Note: We do not allow have multiple territories with the same name
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
   * @return false if the terr is not ok to add (i.e. is null or already exist)
   * @return true if the terr is ok to add and indicate the success of adding
   */
  public boolean addTerritory(Territory terr) {
    if (terr == null) {
      return false;
    }
    if (alreadyExist(terr)) {
      return false;
    }
    map_dict.put(terr, null);
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
   * @return false if the territory does not exist
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
    return true;
  }

  /**
   * Get the owner of the input terr
   * Will check the existence of the territory first
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
}
