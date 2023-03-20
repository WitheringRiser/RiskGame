package edu.duke.ece651.teamX.client;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
public class UserInReader {
    private PrintStream out;
    private BufferedReader inputReader;
    
    /**
     * Construct a UserInReader object
     * The main job is to read from user and parse some input
     * @param input
     * @param out
     */
    public UserInReader(BufferedReader input, PrintStream out){
        this.out = out;
        this.inputReader = input;
    }

    /**
     * Read a line of string from user
     * @return a string of user input
     * @throws IOException
     */
    public String readString()throws IOException{
        String user_in = inputReader.readLine();
        return user_in;
    }

    /**
   * Convert user input to an integer
   * 
   * @param user_in is user input string
   * @return -1 if the number is not valid
   * @return int >= 0 if user input a valid number
   */
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

  /**
   * Directly read from user and return an int result
   * @return -1 if the number is not valid
   * @return int >= 0 if user input a valid number
   * @throws IOException
   */
  public int getUserInt()throws IOException{
    String user_in = inputReader.readLine();
    return getUserInt(user_in);    
  }

  /**
   *Get a valid unit number input by client
   *@param max_num is the maximum number of available units
   *@return -1 if user press B or b
   *@return a valid number of unit to use in later action
   */
  public int enterNum(int max_num, String enterAgainPromot) throws IOException {
    while (true) {      
      String user_in = inputReader.readLine();
      if (user_in.equals("B") || user_in.equals("b")) {
        return -1;
      }
      int num_units = getUserInt(user_in);
      if (num_units >= 0 && num_units <= max_num) {
        return num_units;
      }
      out.print(enterAgainPromot);
    }
  }

  /**
   * 
   * @param max_num is the maximum number of available units
   * @param startPromot is the start input promot
   * @param enterAgainPromot is the message that asks user enter again
   * @return a valid number of unit to use in later action
   * @throws IOException
   */
  public int enterNum(int max_num, String startPromot, String enterAgainPromot) throws IOException {
    out.print(startPromot);
    return enterNum(max_num,enterAgainPromot);
  }
}
