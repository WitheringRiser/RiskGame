package edu.duke.ece651.teamX.server;

import static org.junit.jupiter.api.Assertions.*;

import edu.duke.ece651.teamX.shared.*;
import java.util.*;
import org.junit.jupiter.api.Test;
public class GameTest {
  @Test
  public void test_GameBasic() {
    Game myGame = new Game(3, 20);
    assertEquals(3, myGame.getNumPlayer());
    ArrayList<Territory> terr_list1 = new ArrayList<Territory>();
    terr_list1.add(new Territory("aTerritory"));
    terr_list1.add(new Territory("bTerritory"));
    terr_list1.add(new Territory("cTerritory"));
    Player p1 = new Player("Red", 20, 3);
    myGame.setGroupOwner(terr_list1, p1);
    assertEquals(3, myGame.getMap().getTerritoryNum());
    assertEquals(p1, myGame.getMap().getOwner(new Territory("aTerritory")));
    assertThrows(IllegalArgumentException.class,
                 () -> myGame.setGroupOwner(terr_list1, p1));
  }
}
