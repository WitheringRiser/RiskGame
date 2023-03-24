package edu.duke.ece651.teamX.client;

import java.io.IOException;
import java.util.ArrayList;
import edu.duke.ece651.teamX.shared.*;
import java.net.Socket;
import java.io.PrintStream;

public class ClientUnitSetting implements ClientAction {

  private Socket socket;
  private PrintStream out;
  private UserInReader inputReader;
  private TextPrompt prompt;
  private Communicate communicate;
  private ArrayList<Territory> territories;
  private int remain_units;

  public ClientUnitSetting(Socket s, PrintStream o, UserInReader uir, TextPrompt p,
      ArrayList<Territory> ts, int r) {
    this.socket = s;
    this.out = o;
    this.inputReader = uir;
    this.prompt = p;
    this.communicate = new Communicate();
    this.territories = ts;
    this.remain_units = r;

  }

  /**
   * Place a number of units in a chosen territory
   *
   * @param t         is the chosen territory
   * @param num_units is the number of units to place
   */
  public void setUnits(Territory t, int num_units) {
    t.addUnits(null, num_units);
  }

  /**
   * Place all available units to the territories To minimize potential error for server, this
   * function will make sure client can only choose the territories belongs to him/her and cannot
   * place more than the inital unit number in the Player object
   *
   * @param territories is a list of territories of the client
   */
  public void perform() throws IOException {
    // int remain_units = player.getUnitNum();
    boolean is_start = true;
    while (remain_units > 0) {
      out.println(prompt.setUnitPrompt(territories, remain_units, is_start));
      is_start = false;

      int choice = inputReader.getUserInt();
      if (choice >= 0 && choice < territories.size()) {
        int num_units = inputReader.enterNum(remain_units, prompt.enterNumPrompt(),
            prompt.enterAgainPrompt());
        if (num_units >= 0) {
          setUnits(territories.get(choice), num_units);
          remain_units -= num_units;
        }
      } else {
        out.print(prompt.enterAgainPrompt());
      }
    }
  }


  /**
   * Send unit placement information to the server
   */
  public void commit() throws IOException {
    communicate.sendObject(socket, territories);
    out.print(prompt.commitMessage());
  }
}
