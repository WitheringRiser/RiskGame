package edu.duke.ece651.teamX.client;

import edu.duke.ece651.teamX.shared.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

public class UserInReader {
  private PrintStream out;
  private BufferedReader inputReader;

  /**
   * Construct a UserInReader object The main job is to read from user and parse some input
   *
   * @param input
   * @param out
   */
  public UserInReader(BufferedReader input, PrintStream out) {
    this.out = out;
    this.inputReader = input;
  }

  /**
   * Read a line of string from user
   *
   * @return a string of user input
   * @throws IOException
   */
  public String readString() throws IOException {
    String user_in = inputReader.readLine();
    return user_in;
  }
  /*
  public ArrayList<Integer> readIndexList(int min, int max, int max_num) throws IOException {
    ArrayList<Integer> indexList = new ArrayList<>();
    boolean isValidInput = false;
    do {
      System.out
          .print(
              "To select units, please enter a list of unit indexes(integers) separated by commas or spaces: ");
      String input = readString();
      String[] tokens = input.split("[,\\s]+");
      isValidInput = true;
      for (String token : tokens) {
        try {
          int index = Integer.parseInt(token);
          if (index < min || index > max) {
            System.out.println(
                "Invalid input. All integers must be between " + min + " and " + max + ".");
            indexList.clear();
            isValidInput = false;
            break;
          }
          if (indexList.contains(index)) {
            System.out.println("Invalid input. The integer " + index + " appears multiple times.");
            indexList.clear();
            isValidInput = false;
            break;
          }
          indexList.add(index);
          if (indexList.size() > max_num) {
            System.out.println("Invalid input. food resource is not enough");
            indexList.clear();
            isValidInput = false;
            break;
          }
        } catch (NumberFormatException e) {
          System.out.println("Invalid input. Please enter a valid list of integers.");
          indexList.clear();
          isValidInput = false;
          break;
        }
      }
    } while (!isValidInput);
    return indexList;

  }
  */
  /**
   * Convert user input to an integer
   *
   * @param user_in is user input string
   * @return -1 if the number is not valid
   * @return int >= 0 if user input a valid number
   */
  /*
  public int getUserInt(String user_in) {
    try {
      int choice = Integer.parseInt(user_in);
      return choice;
    }
    catch (NumberFormatException ex) {
      out.println("Expect a valid number, but got \"" + user_in + "\"");
    }
    return -1;
  }
  */
  /**
   * Directly read from user and return an int result
   *
   * @return -1 if the number is not valid
   * @return int >= 0 if user input a valid number
   * @throws IOException
   */
  /*
  public int getUserInt() throws IOException {
    String user_in = inputReader.readLine();
    return getUserInt(user_in);
  }
  */
  /**
   * Get a valid unit number input by client
   *
   * @param max_num is the maximum number of available units
   * @return -1 if user press B or b
   * @return a valid number of unit to use in later action
   */
  /*
  public int enterNum(int max_num, String enterAgainPrompt) throws IOException {
    while (true) {
      String user_in = inputReader.readLine();
      if (user_in.equals("B") || user_in.equals("b")) {
        return -1;
      }
      int num_units = getUserInt(user_in);
      if (num_units >= 0 && num_units <= max_num) {
        return num_units;
      }
      out.print(enterAgainPrompt);
    }
  }
  */
  /*
  public int enterNum(int min_num, int max_num, String enterAgainPrompt)
      throws IOException {
    while (true) {
      int num = enterNum(max_num, enterAgainPrompt);
      if (num >= min_num) {
        return num;
      }
      out.print(enterAgainPrompt);
    }
  }
  */
  /**
   * @param max_num          is the maximum number of available units
   * @param startPrompt      is the start input prompt
   * @param enterAgainPrompt is the message that asks user enter again
   * @return a valid number of unit to use in later action
   * @throws IOException
   */
  /*
  public int enterNum(int max_num, String startPrompt, String enterAgainPrompt)
      throws IOException {
    out.print(startPrompt);
    return enterNum(max_num, enterAgainPrompt);
  }
  */
  /*
  public int enterLevel(int tech_level, int resource, Unit unit) throws IOException {
    out.print("please input the level you want the unit to upgrade to");
    while (true) {
      String user_in = inputReader.readLine();
      if (user_in.equals("B") || user_in.equals("b")) {
        return -1;
      }
      int level = getUserInt(user_in);

      if (level > tech_level || unit.getCost(level) > resource ||
          !unit.upgradeLevel(level)) {
        out.print("level is invalid, please input again");
      }
      else {
        return level;
      }
    }
  }
  */
}
