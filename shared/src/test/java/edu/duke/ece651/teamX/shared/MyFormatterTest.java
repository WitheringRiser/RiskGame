package edu.duke.ece651.teamX.shared;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import org.junit.jupiter.api.Test;

public class MyFormatterTest {
  @Test
  public void test_Formater() {
    System.out.println("--------Test Map--------");
    String Input =
        "{'player_0':[{'neighbor':[{'neighbor_0':'a_neigh1'},{'neighbor_1':'a_neigh2'}],'territoryName':'aTerr1'},{'neighbor':[{'neighbor_0': 'a_neigh3'},{'neighbor_1': 'a_neigh4'}],'territoryName':'aTerr2'}],'player_1':[{'neighbor':[{'neighbor_0':'b_neigh1'},{'neighbor_1':'b_neigh2'}],'territoryName':'bTerr1'},{'owner':'bbb','neighbor':[{'neighbor_0':'b_neigh3'},{'neighbor_1':'b_neigh4'}],'territoryName':'bTerr2'}]}";
    MyFormatter Map = new MyFormatter(2);
    HashMap<Integer, ArrayList<Territory>> TerritoryMap = new HashMap<>();
    Map.MapParse(TerritoryMap, Input);

    MyFormatter Map2 = new MyFormatter(1);
    HashMap<Integer, ArrayList<Territory>> TerritoryMap2 = new HashMap<>();
    Map2.MapParse(TerritoryMap2, Input);
  }
  //This test function has an exact copy in MapToJsonTest.java. The only reason I 
  // include it here as well is to get 100% test coverage as the above code in 
  // test_Formatter does not test the MapCompose function
  @Test
  public void test_mapCompose(){
    HashMap<Integer, ArrayList<Territory>> myterritoryMap =
        new HashMap<Integer, ArrayList<Territory>>();

    ArrayList<Territory> myTerritoryList = new ArrayList<Territory>();
    Territory terrA = new Territory("A");
    Territory terrB = new Territory("B");
    Territory terrD = new Territory("D");
    terrA.addNeighbors(terrB);
    terrA.addNeighbors(terrD);
    terrA.addUnits(new BasicUnit(), 3);
    myTerritoryList.add(terrA);
    ArrayList<Territory> EmptyTerr = new ArrayList<>();

    myterritoryMap.put(0, myTerritoryList);
    myterritoryMap.put(1, EmptyTerr);

    MyFormatter myformatter = new MyFormatter(0);
    System.out.println(myformatter.MapCompose(myterritoryMap));

  }
}
