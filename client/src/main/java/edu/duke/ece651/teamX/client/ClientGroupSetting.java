package edu.duke.ece651.teamX.client;

import java.io.PrintStream;
import java.net.Socket;
import edu.duke.ece651.teamX.shared.*;
import java.util.HashMap;
import java.util.ArrayList;
import java.io.IOException;

public class ClientGroupSetting implements ClientAction {

  private Socket socket;
  private PrintStream out;
  private UserInReader inputReader;
  private TextPromot promot;
  private Communicate communicate;
  private int terr_choice;
  private ArrayList<Territory> territories;

  public ClientGroupSetting(Socket s, PrintStream o, UserInReader uir, TextPromot p) {
    this.socket = s;
    this.out = o;
    this.inputReader = uir;
    this.promot = p;
    this.communicate = new Communicate();
    this.terr_choice = -1;
    territories = new ArrayList<Territory>();
  }

  /**
   * Receive territory groups from server
   *
   * @return is a hashmap representing available territory groups
   */
  public HashMap<Integer, ArrayList<Territory>> receiveTerrGroup()
      throws IOException, ClassNotFoundException {
    return (HashMap<Integer, ArrayList<Territory>>) communicate.receiveObject(socket);
  }

  /**
   * Choose a territory group as initial territories
   *
   * @param free_groups is the available group options
   * @return int the choice of user
   * @print invalid option to promot user to choose again
   */
  public int chooseTerrGroup(HashMap<Integer, ArrayList<Territory>> free_groups)
      throws IOException {
    out.print(promot.displayTerrGroup(free_groups));
    while (true) {

      int choice = inputReader.getUserInt();
      if (choice >= 0 && free_groups.containsKey(choice)) {
        return choice;
      } else {
        out.print(promot.enterAgainPromot());
      }
    }
  }


  /**
   * Let client to choose territory group
   *
   * @throws IOException
   * @throws ClassNotFoundException
   */
  public void perform() throws IOException, ClassNotFoundException {
    HashMap<Integer, ArrayList<Territory>> free_groups = receiveTerrGroup();
    terr_choice = chooseTerrGroup(free_groups);
    territories = free_groups.get(terr_choice);
  }

  /**
   * Send the choice to the server
   *
   * @throws IOException
   */
  public void commit() throws IOException {
    communicate.sendInt(socket, terr_choice);// notify server about the choice
  }

  /**
   * Return the chosen territory group from previous operation
   *
   * @return the chosen territory group
   */
  public ArrayList<Territory> getGroup() {
    return this.territories;
  }
}
