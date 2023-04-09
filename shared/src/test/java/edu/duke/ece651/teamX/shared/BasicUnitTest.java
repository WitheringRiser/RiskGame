package edu.duke.ece651.teamX.shared;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class BasicUnitTest {
  @Test
  public void test_getter() {
    Unit u = new BasicUnit();
    assertEquals(u.getBonus(), 0);
    assertEquals(u.getName(), "level_0_unit");
  }

}
