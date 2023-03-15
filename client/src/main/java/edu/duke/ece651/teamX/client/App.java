/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package edu.duke.ece651.teamX.client;

import com.sun.org.apache.xerces.internal.impl.io.UCSReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import org.checkerframework.checker.units.qual.C;

public class App {
  public static void main(String[] args) throws IOException, ClassNotFoundException {
    Socket clientSocket = new Socket("localhost", 4444);
    BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
    PrintStream out = System.out;

    Client client = new Client(clientSocket, input, out);
    client.init();
    // client.receiveMap();
    //client.displayMap();
  }
}
