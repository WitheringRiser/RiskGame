
package edu.duke.ece651.teamX.shared;

import java.io.*;
import java.util.*;

//create food and tech resources in a territory
public class TerritoryProduce {

  private HashMap<String, Integer> techProduct;
  private HashMap<String, Integer> foodProduct;

  public TerritoryProduce() {
    String fileName = "/Product.txt";
    foodProduct = new HashMap<>();
    techProduct = new HashMap<>();

    String[] Split;
    InputStream input = getClass().getResourceAsStream(fileName);
    Scanner scanner = new Scanner(input);

    while (scanner.hasNext()) {
      String str = scanner.nextLine();
      Split = str.split("-");
      this.foodProduct.put(Split[0], Integer.parseInt(Split[1]));
      this.techProduct.put(Split[0], Integer.parseInt(Split[2]));
    }
  }

  public int getTech(String Terr) {
    return this.techProduct.get(Terr);
  }

  public int getFood(String Terr) {
    return this.foodProduct.get(Terr);
  }


}
