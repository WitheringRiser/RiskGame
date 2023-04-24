package edu.duke.ece651.teamX.shared;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class AdvancedUnitTest {

  @Test
  public void test_AdvancedUnit() {
    AdvancedUnit u1 = new AdvancedUnit("Red");
    assertEquals("Red", u1.getPlayerName());
  }
}
