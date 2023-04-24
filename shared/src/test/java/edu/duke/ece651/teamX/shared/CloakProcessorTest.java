package edu.duke.ece651.teamX.shared;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import org.junit.jupiter.api.Test;

public class CloakProcessorTest {

  @Test
  public void test_CloakProcessor() {
    Territory t1 = new Territory("A", 4);
    Player p1 = new Player("Red", 10);
    Map m = new Map();
    m.addTerritory(t1, p1);
    ArrayList<CloakSender> all = new ArrayList<>();
    all.add(new CloakSender(new Territory("A", 4)));
    CloakProcessor cp1 = new CloakProcessor(all, m);
    assertThrows(IllegalArgumentException.class, () -> cp1.resolveAllCloaks());
    p1.upgradeLevel();
    p1.upgradeLevel();
    p1.increaseAllResource(10);
    p1.unlockCloak();
    assertThrows(IllegalArgumentException.class, () -> cp1.resolveAllCloaks());
    p1.increaseAllResource(100);
    cp1.resolveAllCloaks();
    assertTrue(t1.isCloaked());
  }
}
