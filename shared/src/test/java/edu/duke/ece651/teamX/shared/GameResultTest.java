package edu.duke.ece651.teamX.shared;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;

class GameResultTest {

  @Test
  void isWin() {
    GameResult gameResult = new GameResult(true, null, null);
    assertTrue(gameResult.isWin());
  }

  @Test
  void getWinner() {
    Player winner = new Player("hello", 20);
    ArrayList<Player> losers = new ArrayList<>();
    losers.add(new Player("hello", 20));
    GameResult gameResult = new GameResult(true, winner, losers);
    assertTrue(gameResult.isWin());
    assertEquals(winner, gameResult.getWinner());
  }

  @Test
  void getLosers() {
    Player winner = new Player("hello", 20);
    ArrayList<Player> losers = new ArrayList<>();
    losers.add(new Player("hello", 20));
    GameResult gameResult = new GameResult(true, winner, losers);
    assertEquals(losers, gameResult.getLosers());
  }

  @Test
  void loserContains() {
    Player winner = new Player("hello", 20);
    ArrayList<Player> losers = new ArrayList<>();
    losers.add(new Player("hello", 20));
    GameResult gameResult = new GameResult(true, winner, losers);
    assertTrue(gameResult.loserContains(new Player("hello", 20)));
  }
}