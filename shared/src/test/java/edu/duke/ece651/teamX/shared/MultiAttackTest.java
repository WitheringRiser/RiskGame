package edu.duke.ece651.teamX.shared;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

public class MultiAttackTest {
  @Test
  public void test_getFinalAttacker() {
    ArrayList<Territory> enemies = new ArrayList<Territory>();
    enemies.add(new Territory("duke",8));
    enemies.add(new Territory("cary",8));
    enemies.add(new Territory("rdu",7));
    enemies.add(new Territory("boston",3));
    MultiAttack ma = new MultiAttack(enemies);
    Territory ans = ma.getFinalAttacker();
    assertEquals("duke", ans.getName());
  }

}
