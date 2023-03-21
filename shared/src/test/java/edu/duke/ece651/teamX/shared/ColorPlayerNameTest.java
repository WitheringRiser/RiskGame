package edu.duke.ece651.teamX.shared;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class ColorPlayerNameTest {

  private void assert_name_equals(ColorPlayerName color_names, String... expect) {
    for (String s : expect) {
      assertEquals(s, color_names.getName());
    }
  }

  @Test
  public void test_getName() {
    ColorPlayerName color_names = new ColorPlayerName();
    assert_name_equals(
        color_names, "Red", "Green", "Blue", "Yellow", "Orange", "Pink", "Purple");
    assertThrows(IllegalArgumentException.class, () -> color_names.getName());
  }
}
