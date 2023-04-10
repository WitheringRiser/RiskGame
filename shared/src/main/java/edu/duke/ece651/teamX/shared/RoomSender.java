package edu.duke.ece651.teamX.shared;

import java.io.Serializable;
import java.util.HashSet;
import java.util.ArrayList;

public class RoomSender implements Serializable {

  private int totalNum;
  private int jointedNum;
  private HashSet<Player> playerSet;
  private Map map;
  private boolean isBegin;
  private boolean isEnd;
  private Player winner;
  private ArrayList<Player> losers;

  public RoomSender(int tN, int jN, HashSet<Player> pS, Map m, boolean iB, boolean iE, Player w,
      ArrayList<Player> l) {
    totalNum = tN;
    jointedNum = jN;
    playerSet = pS;
    map = m;
    isBegin = iB;
    isEnd = iE;
    winner = w;
    losers = l;
  }

  public int getTotalNum() {
    return totalNum;
  }

  public int getJointedNum() {
    return jointedNum;
  }

  public HashSet<Player> getPlayerSet() {
    return playerSet;
  }

  public Map getMap() {
    return map;
  }

  public boolean getIsBegin() {
    return isBegin;
  }

  public boolean getIsEnd() {
    return isEnd;
  }

  public Player getWinner() {
    return winner;
  }

  public ArrayList<Player> getLosers() {
    return losers;
  }
}
