package edu.duke.ece651.teamX.shared;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class PlayerTest {
  @Test
  public void test_PlayerBasic() {
    Player p1 = new Player("A", 20, 4);
    Player p2 = new Player("A", 20, 4);
    Player p3 = new Player("B", 20, 4);
    assertEquals("A", p1.getName());
    assertEquals(20, p1.getUnitNum());
    assertEquals(4, p1.getTerritoryNum());
    assertThrows(IllegalArgumentException.class, () -> new Player("A", -1, 4));
    assertThrows(IllegalArgumentException.class, () -> new Player("A", 20, -1));
    assertThrows(IllegalArgumentException.class, () -> new Player(null, 20, 4));

    assertEquals(p1, p2);
    assertEquals(p1, p1);
    assertNotEquals(p1, p3);
    assertNotEquals(p1, null);
  }
}
