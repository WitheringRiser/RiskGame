package edu.duke.ece651.teamX.shared;
import java.util.ArrayList;
import java.util.Arrays;
public class ColorPlayerName implements PlayerName {
  private ArrayList<String> name_list;  //A list of player names
  private int name_count;               //Use to give a unique name

  public ColorPlayerName() {
    name_list = new ArrayList<>(Arrays.asList(
        "Red", "Green", "Blue", "Yellow", "Orange", "Pink", "Purple"));  // 7 names to use
    name_count = 0;
  }

  /**
   *Return a unique name from the nameset for player to use
   *@throws IllegalArgumentException when the number of player is out of the range
   *@return a name for the player
   */
  public String getName() {
    if (name_count >= name_list.size()) {
      throw new IllegalArgumentException(
          "The number of players is more than the number of available names: " +
          name_list.size());
    }
    String new_name = name_list.get(name_count);
    name_count++;  //update name_count to use next
    return new_name;
  }
}
