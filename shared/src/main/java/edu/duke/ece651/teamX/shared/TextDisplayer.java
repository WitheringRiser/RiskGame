package edu.duke.ece651.teamX.shared;

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

  static public void displayAttack(int attackerSize, int defenderSize, int attackerAfter, int defenderAfter,
      Player enemyPlayer) {
    System.out.println("----------------AttackLog----------------");
    if (enemyPlayer != null) {
      System.out.println(enemyPlayer.getName() + " player launch the attack!");
    }

    System.out.println("Original attacker number: " + attackerSize);
    System.out.println("Original defender number: " + defenderSize);

    System.out.println("After attack, attacker number: " + attackerAfter);
    System.out.println("After attack, defender number: " + defenderAfter);
    if (attackerAfter == 0) {
      System.out.println("Attacker loses all units");
    } else {
      System.out.println("Attacker wins!");
    }
    System.out.println("-----------------------------------------");
  }
}
