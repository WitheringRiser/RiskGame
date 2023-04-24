package edu.duke.ece651.teamX.shared;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class ActionSenderTest {

  @Test
  public void test_equal() {
    ActionSender a1 = new ActionSender(new Territory("a", 2), new Territory("b", 2), 2);
    ActionSender a2 = new ActionSender(new Territory("a", 2), new Territory("b", 2), 2);
    MoveSender a3 = new MoveSender(new Territory("a", 2), new Territory("b", 2), 2);
    MoveSender a4 = new MoveSender(new Territory("a", 2), new Territory("b", 2), 2, "a");

    assertTrue(a1.equals(a1));
    assertTrue(a1.equals(a2));
    assertFalse(a1.equals(a3));
    assertEquals(a4.hashCode(), new String("a, b, 2, a").hashCode());
  }
}
