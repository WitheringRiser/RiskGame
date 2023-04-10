package edu.duke.ece651.teamX.shared;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import java.io.IOException;

public class TerritoryProduceTest {

  @Test
  public void test_produce() throws IOException {
    TerritoryProduce test = new TerritoryProduce();
    int foodNum = test.getFood("Desert");
    int techNum = test.getTech("Desert");
    assertEquals(10, foodNum);
    System.out.println(
        "Territory Desert: " + "Food " + test.getFood("Desert") + ", Technology " + test.getTech(
            "Desert"));
    ;
    assertEquals(7, techNum);
  }
}
