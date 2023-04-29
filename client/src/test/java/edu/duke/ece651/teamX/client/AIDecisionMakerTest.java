package edu.duke.ece651.teamX.client;


import static org.junit.jupiter.api.Assertions.*;

import edu.duke.ece651.teamX.shared.Map;
import edu.duke.ece651.teamX.shared.Player;
import edu.duke.ece651.teamX.shared.Territory;
import java.net.Socket;
import org.junit.jupiter.api.Test;

class AIDecisionMakerTest {


  @Test
  void make_decision() {
    Map map = new Map();

    Player p1 = new Player("A", 10);
    Player p2 = new Player("B", 10);

    Territory t1 = new Territory("Desert", 30);
    Territory t2 = new Territory("Mountains", 10);

    map.addTerritory(t1, p1);
    map.addTerritory(t2, p2);
    t1.addNeighbors(t2);
    t2.addNeighbors(t1);

    Socket socket = new Socket();
    ClientAttack clientAttack = new ClientAttack(socket, map, p1);
    ClientMove clientMove = new ClientMove(socket, map, p1);
    ClientResearch clientResearch = new ClientResearch(socket, p1);
    ClientUpgrade clientUpgrade = new ClientUpgrade(socket, map, p1);

    AIDecisionMaker aiDecisionMaker = new AIDecisionMaker(map, clientAttack, clientMove,
        clientResearch, clientUpgrade, p1);

    aiDecisionMaker.make_decision();

  }
}
