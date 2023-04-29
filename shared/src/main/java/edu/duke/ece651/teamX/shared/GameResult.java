package edu.duke.ece651.teamX.shared;

import java.io.Serializable;
import java.util.ArrayList;

public class GameResult implements Serializable {

  private final boolean isWin;
  private final Player winner;
  private final ArrayList<Player> losers;

  public GameResult(boolean isWin, Player winner, ArrayList<Player> losers) {
    this.isWin = isWin;
    this.winner = winner;
    this.losers = losers;
  }

  public boolean isWin() {
    return isWin;
  }

  public Player getWinner() {
    return winner;
  }

  public ArrayList<Player> getLosers() {
    return losers;
  }

  public boolean loserContains(Player p) {
    return losers.contains(p);
  }

  public boolean loserContainsByPlayerNmae(String name) {
    for (Player p : losers) {
      if (p.getName().equals(name)) {
        return true;
      }
    }
    return false;
  }

}
