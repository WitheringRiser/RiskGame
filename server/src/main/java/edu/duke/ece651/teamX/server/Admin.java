package edu.duke.ece651.teamX.server;

import edu.duke.ece651.teamX.shared.*;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
public class Admin implements Runnable {
  private HashMap<String, String> namePasswordDic;
  private ArrayList<Game> GameList;

  private Socket socket;
  private Communicate communicate;

  /**
     * Construct an Admin object for each connection
     * 
     * @param np_ptr is the pointer to player_dict saved in App
     * @param ng_ptr is the pointer to status dict save in App
     * @param cs     is the client socket for Admin to communicate
     */
  public Admin(HashMap<String, String> np_ptr, ArrayList<Game> gl_ptr, Socket cs) {
    namePasswordDic = np_ptr;
    GameList = gl_ptr;
    socket = cs;
    communicate = new Communicate();
  }

  /**
     * Client authentication
     * Receive username and password, compare them with data in namePasswordDic
     * If the username and password is not match, or username not exist,
     * send error message to user
     * If the client is authenticated, send empty message to client,
     * and move to next step
     * Need read lock for namePasswordDic
     * @return the user name
     */
  public String login() throws IOException, ClassNotFoundException {
    while (true) {
      ArrayList<String> namePassWord =
          (ArrayList<String>)communicate.receiveObject(socket);
      if (namePasswordDic.containsKey(namePassWord.get(0))) {
        if (namePasswordDic.get(namePassWord.get(0)).equals(namePassWord.get(1))) {
          communicate.sendObject(socket, "");
          return namePassWord.get(0);
        }
        else {
          communicate.sendObject(socket, "Password incorrect");
        }
      }
      else {
        communicate.sendObject(socket, "Invalid username");
      }
    }
  }

  /**
     * Create a new client account and save it into namePasswordDic
     * If the username already exist, send error message to client
     * If the creation is successful, send empty message to client
     * After creation, the user is default as authenticated
     * Need write lock for namePasswordDic
     * @return the user name
     */
  public String createAccount() throws IOException, ClassNotFoundException {
    while (true) {
      ArrayList<String> newInfo = (ArrayList<String>)communicate.receiveObject(socket);
      if (!namePasswordDic.containsKey(newInfo.get(0))) {
        namePasswordDic.put(newInfo.get(0), newInfo.get(1));
        communicate.sendObject(socket, "");
        return newInfo.get(0);
      }
      else {
        communicate.sendObject(socket, "Username already exist");
      }
    }
  }

  /**
     * Create a new Game object according to user's requirement
     * User will send the playerNum to Admin
     * After create the room, the client is added to the game
     * Add the newly created game into
     * 
     * @param name is the username of the client
     * 
     * Need write lock for nameGameDic
     */
  public void createNewRoom(String name) throws IOException, ClassNotFoundException {
    int playerNum = communicate.receiveInt(socket);
    Game newGame = new Game(playerNum, 20);
    newGame.createPlayer(socket, name);
    GameList.add(newGame);
  }

  /**
     * Let the user join a room that is not active
     * The client should send back player+index of the room in nameGameDic
     * Need to append the Game in the arraylist of that username
     * If the room is already begin, send new options to user
     * @param name is the username of the client
     * 
     * Need write lock for nameGameDic
     */
  public void joinNewRoom(String name) throws IOException, ClassNotFoundException {}

  /**
     * Let user go back to a still active room
     * The client should send back the index of the room they want to go
     * If the game already end, need to send error message to user
     * If suceesfully join, send empty message to user
     * @param name is the username of the client
     * 
     * Need write lock for nameGameDic
     */
  private void joinActiveRoom(String name) throws IOException, ClassNotFoundException {}

  /**
     * Client authentication
     * Assign the client to their chosen room
     * Need to handle IOException, ClassNotFoundException errors
     */
  public void run() {}
}
