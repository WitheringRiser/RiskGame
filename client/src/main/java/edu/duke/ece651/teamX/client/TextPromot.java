package edu.duke.ece651.teamX.client;
import edu.duke.ece651.teamX.shared.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
public class TextPromot {
  private final Player player;
  /**
   *Display territory information
   *@param t is the territory to display
   */
  private String displayTerrtory(Territory t) {
    StringBuilder ans = new StringBuilder("");
    ans.append(t.getName());
    Iterator<Territory> iter = t.getNeighbours();
    ans.append(" (next to: ");
    ans.append(iter.next().getName());
    while (iter.hasNext()) {
      ans.append(", ");
      ans.append(iter.next().getName());
    }
    ans.append(")");
    return ans.toString();
  }

  /**
   *Display territory with its units
   */
  private String displayTerrWithUnit(Territory t) {
    StringBuilder ans = new StringBuilder("");
    ans.append(displayTerrtory(t));
    ans.append(" with ");
    ans.append(t.getUnitsNumber());
    ans.append(" units");
    return ans.toString();
  }

  /**
   *Construct a text-based promot wrtiter
   *@param p is the player to provide infromation
   */
  public TextPromot(Player p) { player = p; }

  /**
   *Showing the welcome promot at the beginning
   */
  public String startPromot() {
    StringBuilder ans = new StringBuilder("");
    ans.append("Welcome to the RISK game, Player " + player.getName() + "!\n");
    ans.append(
        "Please wait a few moments while we gather all players before we start...\n");
    return ans.toString();
  }

  /**
   *Display the promot and options for client to set units
   *@param territories is the list of territories of client
   *@param num_units is the number of units waiting to be placed
   *@param is_start is whether to show the head message of promot
   */
  public String setUnitPromot(ArrayList<Territory> territories,
                              int num_units,
                              boolean is_start) {
    StringBuilder ans = new StringBuilder("");
    if (is_start) {
      ans.append(player.getName() +
                 " player: you are going to place the units in your territories.\n\n");
    }
    ans.append("You have " + num_units + " available units to place.\n");
    ans.append("Please choose a territory to place units:\n");
    for (int i = 0; i < territories.size(); i++) {
      ans.append("(" + i + ") ");
      ans.append(displayTerrWithUnit(territories.get(i)));
      ans.append("\n");
    }
    return ans.toString();
  }
  /**
   *Display the territory group for client to choose
   *@param free_groups is the available groups get from user
   */
  public String displayTerrGroup(HashMap<Integer, ArrayList<Territory> > free_groups) {
    StringBuilder ans = new StringBuilder("");
    ans.append("Please choose one territory group as your initial territories:\n");
    ans.append("(type the corresponding number to indicate your choice, e.g. 1)\n");
    ArrayList<Integer> sorted_group = new ArrayList<>(free_groups.keySet());
    Collections.sort(sorted_group);
    for (int i : sorted_group) {
      ans.append("\n");
      ans.append("(" + i + ") Group " + i + ":\n");
      for (Territory t : free_groups.get(i)) {
        ans.append("   - ");
        ans.append(displayTerrtory(t));
        ans.append("\n");
      }
    }
    return ans.toString();
  }
}
