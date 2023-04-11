
package edu.duke.ece651.teamX.server;

import edu.duke.ece651.teamX.shared.*;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.locks.*;

public class Admin implements Runnable {

  private HashMap<String, String> namePasswordDic;
  private ArrayList<Game> GameList;

  private Socket socket;
  private Communicate communicate;
  private ReadWriteLock gameLock;
  private ReadWriteLock nameLock;

  /**
   * Construct an Admin object for each connection
   *
   * @param np_ptr is the pointer to player_dict saved in App
   * @param ng_ptr is the pointer to status dict save in App
   * @param cs     is the client socket for Admin to communicate
   */
  public Admin(HashMap<String, String> np_ptr,
      ArrayList<Game> gl_ptr,
      Socket cs,
      ReadWriteLock gLock,
      ReadWriteLock nLock) {
    namePasswordDic = np_ptr;
    GameList = gl_ptr;
    socket = cs;
    communicate = new Communicate();
    gameLock = gLock;
    nameLock = nLock;
  }

  /**
   * Client authentication Receive username and password, compare them with data
   * in namePasswordDic
   * If the username and password is not match, or username not exist, send error
   * message to user If
   * the client is authenticated, send empty message to client, and move to next
   * step Need read lock
   * for namePasswordDic
   *
   * @return the user name
   */
  public String login() throws IOException, ClassNotFoundException {
    // while (true) {
    ArrayList<String> namePassWord = (ArrayList<String>) communicate.receiveObject(socket);
    boolean containUser = false;
    boolean passwordMatch = false;
    nameLock.readLock().lock();
    try {
      containUser = namePasswordDic.containsKey(namePassWord.get(0));
      if (containUser) {
        passwordMatch = namePasswordDic.get(namePassWord.get(0)).equals(namePassWord.get(1));
      }
    } finally {
      nameLock.readLock().unlock();
    }
    if (containUser) {
      if (passwordMatch) {
        communicate.sendObject(socket, "");
        return namePassWord.get(0);
      } else {
        communicate.sendObject(socket, "Password incorrect");
        return null;
      }
    } else {
      communicate.sendObject(socket, "Invalid username");
      return null;
    }
    // }
  }

  /**
   * Create a new client account and save it into namePasswordDic If the username
   * already exist,
   * send error message to client If the creation is successful, send empty
   * message to client After
   * creation, the user is default as authenticated Need write lock for
   * namePasswordDic
   *
   * @return the user name
   */
  public String createAccount() throws IOException, ClassNotFoundException {
    ArrayList<String> newInfo;
    // while (true) {
    newInfo = (ArrayList<String>) communicate.receiveObject(socket);
    nameLock.writeLock().lock();
    try {
      if (!namePasswordDic.containsKey(newInfo.get(0))) {
        namePasswordDic.put(newInfo.get(0), newInfo.get(1));
        communicate.sendObject(socket, "");
        return newInfo.get(0);
        // break;
      }
    } finally {
      nameLock.writeLock().unlock();
    }
    communicate.sendObject(socket, "Username already exist");
    // }
    // communicate.sendObject(socket, "");
    // return newInfo.get(0);
    return null;
  }

  /**
   * Create a new Game object according to user's requirement User will send the
   * playerNum to Admin
   * After create the room, the client is added to the game Add the newly created
   * game into
   *
   * @param name is the username of the client
   *             <p>
   *             Need write lock for nameGameDic
   */
  public void createNewRoom(String name) throws IOException, ClassNotFoundException {
    int playerNum = communicate.receiveInt(socket);
    Game newGame = new Game(playerNum, 20);
    newGame.createPlayer(socket, name);
    gameLock.writeLock().lock();
    try {
      GameList.add(newGame);
    } finally {
      gameLock.writeLock().unlock();
    }
  }

  /**
   * Let the user join a room that is not active The client should send back
   * player+index of the
   * room in nameGameDic Need to append the Game in the arraylist of that username
   * If the room is
   * already begin, send new options to user
   *
   * @param name is the username of the client
   *             <p>
   *             Need write lock for nameGameDic
   */
  public void joinNewRoom(String name) throws IOException, ClassNotFoundException {
    ArrayList<Game> searchRes = new ArrayList<Game>();
    ArrayList<RoomSender> sendList = new ArrayList<>();
    gameLock.readLock().lock();
    try {
      for (Game g : GameList) {
        if (!g.containsPlayer(name) && !g.checkHasBegin()) {
          searchRes.add(g);
          sendList.add(g.getRoomSender());
        }
      }
    } finally {
      gameLock.readLock().unlock();
    }
    communicate.sendObject(socket, sendList);
    int choice = communicate.receiveInt(socket);
    Game cGame = searchRes.get(choice);
    String msg;
    gameLock.writeLock().lock();
    try {
      if (cGame.checkHasBegin() || cGame.getActualNumPlayer() == cGame.getNumPlayer()) {
        communicate.sendObject(socket, "The room has already full");
      } else {
        communicate.sendObject(socket, "");
        cGame.createPlayer(socket, name);
        // If room full --> start the game by making a new thread
        if (cGame.getActualNumPlayer() == cGame.getNumPlayer()) {
          Thread gameThread = new Thread(cGame);
          gameThread.start();
        }
      }
    } finally {
      gameLock.writeLock().unlock();
    }

  }

  /**
   * Let user go back to a still active room The client should send back the index
   * of the room they
   * want to go If the game already end, need to send error message to user If
   * suceesfully join,
   * send empty message to user
   *
   * @param name is the username of the client
   *             <p>
   *             Need write lock for nameGameDic
   */
  public void joinActiveRoom(String name) throws IOException, ClassNotFoundException {
    ArrayList<Game> searchRes = new ArrayList<Game>();
    ArrayList<RoomSender> sendList = new ArrayList<>();
    gameLock.readLock().lock();
    try {
      for (Game g : GameList) {
        if (g.containsPlayer(name)) {
          searchRes.add(g);
          sendList.add(g.getRoomSender());
        }
      }
    } finally {
      gameLock.readLock().unlock();
    }
    communicate.sendObject(socket, sendList);
    int choice = communicate.receiveInt(socket);
    Game cGame = searchRes.get(choice);
    String msg;
    gameLock.writeLock().lock();
    try {
      if (cGame.checkIsEnd()) {
        communicate.sendObject(socket, "The game is already end");
      } else {
        communicate.sendObject(socket, "");
        cGame.updateSocket(name, socket);
      }
    } finally {
      gameLock.writeLock().unlock();
    }

  }

  /**
   * Client authentication Assign the client to their chosen room Need to handle
   * IOException,
   * ClassNotFoundException errors
   */
  public void run() {
    try {
      String userName;
      while (true) {
        // first number to indicate login option 0: create account, 1: login
        int choice = communicate.receiveInt(socket);

        if (choice == 0) {
          userName = createAccount();
        } else if (choice == 1) {
          userName = login();
        } else {
          socket.close();
          throw new IllegalArgumentException("Invalid option for log in");
        }
        if (userName != null) {
          break;
        }

      }

      // Second number to indicate join game option
      // 0: create a new game
      // 1: join a new room
      // 2: join a existing active room of this user
      int choice = communicate.receiveInt(socket);
      if (choice == 0) {
        createNewRoom(userName);
      } else if (choice == 1) {
        joinNewRoom(userName);
      } else if (choice == 2) {
        joinActiveRoom(userName);
      } else {
        socket.close();
        throw new IllegalArgumentException("Invalid option for enter game");
      }
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }

  }
}
