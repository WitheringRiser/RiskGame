package edu.duke.ece651.teamX.shared;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.io.IOException;

public class TerritorySizeTest {

  @Test
  public void test_size() throws IOException {
    TerritorySize test = new TerritorySize();
    int size = test.getTerritorySize("Jincheng Taiga");
    assertEquals(size, 6);
  }
}