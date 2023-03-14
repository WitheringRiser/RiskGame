package edu.duke.ece651.teamX.shared;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

public class AttackProcessorTest {
  @Test
  public void test_resovle() {
    ArrayList<AttackSender> allAttack = new ArrayList<AttackSender>();
    Territory t1 = new Territory("duke", 100);
    Territory t2 = new Territory("cary", 80);
    Territory t3 = new Territory("rdu", 7);
    Territory t4 = new Territory("bos", 11);
    Player p1 = new Player("zhou", 20);
    Player p2 = new Player("andre", 15);
    Player p3 = new Player("an", 15);

    Map map = new Map();
    map.addTerritory(t1, p1);
    map.addTerritory(t2, p2);
    map.addTerritory(t3, p3);
    map.addTerritory(t4, p3);

    TextDisplayer displayer = new TextDisplayer(map);
    System.out.println(displayer.display());

    allAttack.add(new AttackSender(t1, t2, 30));
    allAttack.add(new AttackSender(t2, t3, 50));
    allAttack.add(new AttackSender(t3, t1, 5));
    allAttack.add(new AttackSender(t4, t2, 6));

    AttackProcessor ap = new AttackProcessor(allAttack, map);
    ap.resovleAllAttack();
    System.out.println(displayer.display());
    

  }

}
