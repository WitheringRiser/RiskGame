package edu.duke.ece651.teamX.server;

import static org.junit.jupiter.api.Assertions.*;

import edu.duke.ece651.teamX.shared.*;
import java.io.*;
import java.util.*;
import org.junit.jupiter.api.Test;

public class WorldParserTest {

  @Test
  public void test_world2initter() {
    WorldParser myWorldInit = new WorldParser(2);
    HashMap<Integer, ArrayList<Territory>> myworld;
    myworld = myWorldInit.getWorld();
    MapToJson myMaptoJson = new MapToJson(myworld);
    //myMaptoJson.getJSON();
    System.out.println(myMaptoJson.getJSON());
  }

  @Test
  public void test_world3initter() {
    HashMap<Integer, ArrayList<Territory>> myworld2 = new HashMap<>();
    WorldParser myworldinitter2 = new WorldParser(3, myworld2);

    WorldParser myWorldInit = new WorldParser(3);
    HashMap<Integer, ArrayList<Territory>> myworld;
    myworld = myWorldInit.getWorld();
    MapToJson myMaptoJson = new MapToJson(myworld);
    // myMaptoJson.getJSON();
    System.out.println(myMaptoJson.getJSON());
  }

  @Test
  public void test_world4initter() {
    WorldParser myWorldInit = new WorldParser(4);
    HashMap<Integer, ArrayList<Territory>> myworld;
    myworld = myWorldInit.getWorld();
    MapToJson myMaptoJson = new MapToJson(myworld);
    System.out.println(myMaptoJson.getJSON());
  }
}
