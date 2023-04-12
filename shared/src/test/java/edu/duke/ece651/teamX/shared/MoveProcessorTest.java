package edu.duke.ece651.teamX.shared;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class MoveProcessorTest {

  @Test
  void getMinCostPathBetweenSourceDest() {
    Territory desert = new Territory("Desert");
    Territory mountains = new Territory("Mountains");
    Territory swamp = new Territory("Swamp");
    Territory beach = new Territory("Beach");

    desert.addNeighbors(mountains);
    desert.addNeighbors(swamp);
    mountains.addNeighbors(desert);
    mountains.addNeighbors(beach);
    swamp.addNeighbors(desert);
    swamp.addNeighbors(beach);
    beach.addNeighbors(mountains);
    beach.addNeighbors(swamp);

    Player p1 = new Player("p1", 10);
    Player p2 = new Player("p2", 10);
    Map map = new Map();
    map.addTerritory(desert, p1);
    map.addTerritory(mountains, p1);
    map.addTerritory(swamp, p1);
    map.addTerritory(beach, p1);

    MoveProcessor mp = new MoveProcessor(null, map);
    assertEquals(18, mp.getMinCostPathBetweenSourceDest(desert, beach, map));

    map.setOwner(swamp, p2);
    assertEquals(22, mp.getMinCostPathBetweenSourceDest(desert, beach, map));
  }
}
