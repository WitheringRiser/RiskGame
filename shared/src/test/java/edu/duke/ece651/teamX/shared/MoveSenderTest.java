package edu.duke.ece651.teamX.shared;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class MoveSenderTest {
  @Test
  public void test_moveSender() {
    Territory source = new Territory("A", 10);
    Territory dest = new Territory("B", 0);
    MoveSender ms = new MoveSender(source, dest, 5);
    assertEquals(new Territory("A"), ms.getSource());
    assertEquals(new Territory("B"), ms.getDestination());
    assertEquals(5, ms.getUnitsNum());
  }
}
