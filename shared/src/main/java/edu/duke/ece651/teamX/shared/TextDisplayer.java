package edu.duke.ece651.teamX.shared;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class TextDisplayer extends Displayer<String> {

  public TextDisplayer(Map _toDisplay) {
    super(_toDisplay);
  }

  /**
   * display one territory
   *
   * @param t: territory needs to be displayed
   * @return: e.g. x units in xx (next to: xx, xx, xx)
   */
  public String displayTerritory(Territory t) {
    StringBuilder ans = new StringBuilder("");
    ans.append(t.getUnitsNumber());
    ans.append(" units in ");
    ans.append(t.getName());
    ans.append(" (next to:");

    Iterator<Territory> iter = t.getNeighbours();
    String sep = " ";
    while (iter.hasNext()) {
      ans.append(sep);
      ans.append(iter.next().getName());
      sep = ", ";
    }
    ans.append(")\n");
    return ans.toString();
  }

  /**
   * display the whole map to player
   */
  @Override
  public String display() {
    StringBuilder ans = new StringBuilder("");
    HashMap<Territory, Player> toDisplay = map.getMap();
    Set<Player> playerSet = new HashSet<>(toDisplay.values());
    for (Player player : playerSet) {
      ans.append(makeHeader(player.getName()));
      for (HashMap.Entry<Territory, Player> entry : toDisplay.entrySet()) {
        if (entry.getValue().equals(player)) {
          Territory t = entry.getKey();
          ans.append(displayTerritory(t));
        }
      }
      ans.append("\n");
    }
    return ans.toString();
  }

  /**
   * This makes the header line, e.g. Green player
   *
   * @param playerName: the name of the player
   * @return the String that is the header line for the given player
   */
  String makeHeader(String playerName) {
    StringBuilder ans = new StringBuilder("");
    ans.append(playerName);
    ans.append(" player:\n");
    ans.append("-----------\n");
    return ans.toString();
  }

  public static void displayAttackOriginal(Territory t, ArrayList<Attacker> attackers, Map map) {
    StringBuilder ans = new StringBuilder();
    ans.append("----------------AttackLog----------------\n");
    ans.append("In " + t.getName() + "(belongs to " + map.getOwner(t).getName() + "), ");
    ans.append("fight between:\n");
    for (Attacker attacker : attackers) {
      ans.append(attacker.getOwner().getName() + " with " + attacker.getAttacker().size() + " units\n");
    }
    System.out.print(ans.toString());
  }

  public static void displayAttackAfter(Territory t, Map map) {
    StringBuilder ans = new StringBuilder();
    ans.append("After attack, \n" + t.getName() + " belongs to " + map.getOwner(t).getName() + " with "
        + t.getUnitsNumber() + " units\n");
    ans.append("-----------------------------------------\n");
    System.out.print(ans.toString());
  }

  public static void displayMove(Territory source, Territory destination, int num, Player player) {
    System.out.println("----------------MoveLog----------------");
    System.out.println(player.getName() + " player launch the move!");
    System.out.println("Move " + num + " units from " + source.getName() + " to " + destination
        .getName());
    System.out.println("---------------------------------------");
  }

}
