package edu.duke.ece651.teamX.client;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.StringReader;
import org.junit.jupiter.api.Test;

public class UserInReaderTest {
  @Test
  public void test_readString() throws IOException {
    String input_data = "a\n";
    BufferedReader input = new BufferedReader(new StringReader(input_data));
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    PrintStream output = new PrintStream(bytes, true);
    UserInReader uir = new UserInReader(input, output);
    String res = uir.readString();
    assertEquals("a", res);
  }
}
