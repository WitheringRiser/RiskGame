package edu.duke.ece651.teamX.client;

import static org.junit.jupiter.api.Assertions.*;

import edu.duke.ece651.teamX.shared.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import org.junit.jupiter.api.Test;

public class ClientUpgradeTest {
  @Test
  public void test_ClientUpgrade() throws IOException, ClassNotFoundException {
    Map my_map = new Map();
    Territory t1 = new Territory("Desert", 60);
    Territory t2 = new Territory("Swamp", 1);
    Territory t3 = new Territory("Beach", 30);
    Territory t4 = new Territory("Plains", 3);
    Player p1 = new Player("Red", 10);
    Player p2 = new Player("Green", 10);
    t1.addNeighbors(t2);
    t2.addNeighbors(t3);
    t3.addNeighbors(t4);
    my_map.addTerritory(t1, p1);
    my_map.addTerritory(t2, p1);
    my_map.addTerritory(t3, p1);
    my_map.addTerritory(t4, p2);
    Communicate communicate = new Communicate();
    ServerSocket ss = new ServerSocket(4496);
    Socket clientSocket = new Socket("localhost", 4496);
    Socket serverSocket = ss.accept();
    ClientUpgrade cu = new ClientUpgrade(clientSocket, my_map, p1);
    HashMap<String, ArrayList<Integer> > unit_dict = new HashMap<>();
    ArrayList<Integer> nums = new ArrayList<Integer>();
    nums.add(2);
    nums.add(1);
    cu.commit();
    unit_dict.put("level_0_unit", nums);
    cu.perform(t1, unit_dict);
    assertThrows(IllegalArgumentException.class, () -> cu.perform(t2, unit_dict));
    assertThrows(IllegalArgumentException.class,
                 () -> cu.perform_res("level_0_unit", 2, 2, t1));
    nums.clear();
    nums.add(0);
    nums.add(1);
    cu.perform(t1, unit_dict);
    nums.clear();
    nums.add(2);
    nums.add(2);
    assertThrows(IllegalArgumentException.class, () -> cu.perform(t1, unit_dict));
    nums.clear();
    nums.add(58);
    nums.add(1);
    assertThrows(IllegalArgumentException.class, () -> cu.perform(t1, unit_dict));
    nums.clear();
    nums.add(2);
    nums.add(-1);
    assertThrows(IllegalArgumentException.class, () -> cu.perform(t1, unit_dict));
    p1.increaseAllResource(1000);
    p1.upgradeLevel();
    p1.upgradeLevel();
    assertThrows(IllegalArgumentException.class, () -> cu.perform(t1, unit_dict));
    unit_dict.clear();
    unit_dict.put("level_1_unit", nums);
    cu.perform(t1, unit_dict);
    //cu.perform(t1, unit_dict);
  }
}
