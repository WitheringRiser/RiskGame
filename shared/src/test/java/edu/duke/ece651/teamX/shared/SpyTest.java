package edu.duke.ece651.teamX.shared;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class SpyTest {

  @Test
  public void test_Spy() {
    Spy s = new Spy("Red");
    assertFalse(s.checkMove());
    assertEquals("Red", s.getOwner());
    s.recordMove();
    assertThrows(IllegalArgumentException.class, () -> s.recordMove());
    s.turnReset();
    s.recordMove();
  }
}
