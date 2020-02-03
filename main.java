//-------|---------|---------|---------|---------|---------|---------|---------|
//
// UW CSS 527 - Assg2 - OTP Tokens
// main.java
//
//-------|---------|---------|---------|---------|---------|---------|---------|

//-----------------------------------------------------------------------------|
// Authorship
//-----------------------------------------------------------------------------|
//
// Tim Lum
// twhlum@gmail.com
//
// Matt Sell
//
// Christopher Ijams
// cocopelly255@gmail.com
//
// Created:  2020.01.23
// Modified: 2020.02.-- (TODO)
// For the University of Washington Bothell, CSS 527
// Winter 2020, Masters in Cybersecurity Engineering (MCSE)
//

//-----------------------------------------------------------------------------|
// File Description
//-----------------------------------------------------------------------------|
//
// Driver file for the OTP class

//-----------------------------------------------------------------------------|
// Package Files
//-----------------------------------------------------------------------------|
//
// See README.md

//-----------------------------------------------------------------------------|
// Useage
//-----------------------------------------------------------------------------|
//
// Compile with:
// javac Main.java && java Main
//
// Note: Requires Java SDK installed to the Linux environment. Install with:
// $ sudo apt-get update
// $ sudo apt-get install openjdk-8-jdk

//-------|---------|---------|---------|---------|---------|---------|---------|
//
//       INCLUDES
//
//-------|---------|---------|---------|---------|---------|---------|---------|

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.awt.Dimension;
import java.io.File;
import java.util.Scanner; // For user inputs
import java.util.HashSet; // For hashsets
import java.util.LinkedList; // For linked lists
import java.util.Date; // For timestamps
import java.text.SimpleDateFormat; // For formatting timestamp
import java.time.Instant;
import javax.swing.*; 
import java.awt.*;
import java.awt.event.*;

public class Main {

  // -------|---------|---------|---------|---------|---------|---------|---------|
  //
  // GLOBAL CONSTANTS
  //
  // -------|---------|---------|---------|---------|---------|---------|---------|

  static final boolean DEBUG = true;
  static final int NUM_TESTS = 1000000;
  static final int LOOKAHEAD = 100;

  // -------|---------|---------|---------|---------|---------|---------|---------|
  //
  // PROGRAM DRIVER
  //
  // -------|---------|---------|---------|---------|---------|---------|---------|

  // NOTE:
  // Initial driver version for testing out each of the methods. Will refactor
  // into usable form.

  public static void main(String[] args) {
    // args = new String[1];
    // args[0] = "CLIENT";

    String role = args[0];
    if (!role.equals("SERVER") && !role.equals("CLIENT") && !role.equals("KEYGEN")) {
      System.out.println( "Improper argument passed (" + args[0] + "). Halting..." );
      System.exit(0);
    }
    else {
      if (DEBUG) {
        System.out.println( "My role is: " + role );
        System.out.println();
      }
    }

// -------|---------|---------|---------|
// SERVER
// -------|---------|---------|---------|
    if( role.equals( "SERVER" ) ) {
      // Do SERVER things here
      LinkedList<String> OTPbank = new LinkedList<String>();
      int currIteration = 0;
      String initVector = getIV();
      initServer( OTPbank, LOOKAHEAD, initVector );
      if( DEBUG ) {
        System.out.println( "Contents of server OTP bank: " );
        for( int i = 0 ; i < OTPbank.size() ; i++ ) {
          System.out.print( "(" + i + "):" + OTPbank.get( i ) + " " );
          if( i % 10 == 9 ) {
            System.out.println();
          }
        }
        System.out.println();
      }

      
      Scanner userInput = new Scanner(System.in);
      while( true ) {
        System.out.println( "Please provide an OTP or 'X' to eXit: " );
        String userOTP = userInput.next();

        System.out.println( "User input received: " + userOTP );
        if( userOTP.equals( "X" ) ) {
          if( DEBUG ) {
            System.out.println( "Exiting..." );
            System.out.println();
          }
          // Exit
          System.exit(1);
        }

        int syncNumber = 0;
        boolean isFound = false;
        for( int i = 0 ; i < OTPbank.size() ; i++ ) {
          if( OTPbank.get(i).equals( userOTP ) ) {
            // Match found!
            syncNumber = i;
            isFound = true;
            break;
          }
        }
        if( DEBUG ) {
          System.out.println( "Synchronization off expected by: (" + syncNumber + ") positions. Adjusting..." );
        }
      }




    } // Closing SERVER behavior

// -------|---------|---------|---------|
// CLIENT
// -------|---------|---------|---------|
    else if( role.equals( "CLIENT" ) ) {
      // Do CLIENT things here
      // Note: Client appears to be the same as the KEYGEN set of behaviors (sans collision tests)
    }

// -------|---------|---------|---------|
// KEYGEN
// -------|---------|---------|---------|
    else if( role.equals( "KEYGEN" ) ) {
      // Do KEYGEN things here
      Scanner userInput = new Scanner(System.in);
      while( true ) {
        System.out.println( "Enter 'OTP' to generate a One-Time Pass");
        System.out.println( "  'T' to enter Test mode" );
        System.out.println( "  'X' to eXit" );
        String command = userInput.next();
        if( command.equals("OTP") ) {
          if( DEBUG ) {
            System.out.println( "Generating an OTP..." );
            System.out.println();
          }
          // Generate an OTP
        }
        else if( command.equals("T") ) {
          if( DEBUG ) {
            System.out.println( "Running a bajillion test OTPs..." );
            System.out.println();
          }
          // Run a bajillion test OTPs and do metric stuff here
          // Must test:
          //   - CR1 - The number of similar OTPs in bajillion tests
          //   - CR2 - The number of -consecutive- similar OTPs in bajillion tests
          double hitRate = testBattery( NUM_TESTS );
          System.out.println( "Test battery results: " + (hitRate*100) + "%" );
        }
        else if( command.equals( "X" ) ) {
          if( DEBUG ) {
            System.out.println( "Exiting..." );
            System.out.println();
          }
          // Exit
          System.exit(1);
        }
        else {
          System.out.println( "Unrecognized input. Trying again..." );
          System.out.println();
        }
      } // Closing while loop (should never be reached if entered)
    } // Closing KEYGEN behavior

    if (DEBUG) {
      System.out.println("Retrieving initialization vector..." + "\n");
    }
    int iterationCounter = 0; // will increase with each method call.

    String key = "";
    StringBuilder hexString = null;
    byte[] sha2 = new byte[0];
    String OTP = "";

    try {
      key = getIV();
      if (DEBUG) {
        System.out.println("The initialization vector is:\t" + key + "\n");
      }

      sha2 = generateSha2(key);
      if (DEBUG) {
        System.out.println("The sha2 hash in UTF-8 is:\t" + sha2 + "\n");
      }

      hexString = generateHex(sha2);
      if (DEBUG) {
        System.out.println("The sha2 hash in Hex is:\t" + hexString + "\n");
      }

      OTP = generateOtp(hexString.toString());
      if (DEBUG) {
        System.out.println("The one time password is:\t" + OTP + "\n");
      }

    } catch (Exception e) {
      e.printStackTrace(System.out);
    } finally {

    }

  // initGUI();
  } // Closing main()



  /**
   * Run a set of tests and calculate collision metrics
   * 
   * @param input The number of tests to execute when searching for a collision
   * @return A double representing the collision rate for the number of tests
   */
  public static String OTPfromSeed( String seed ) {
    boolean localDebug = false;
    if( DEBUG && localDebug ) {
      System.out.println( "Generating OTP from (" + seed + ")" );
    }
    byte[] sha2 = new byte[0];
    StringBuilder hexString = null;
    String retString = "";
    try {
      // SHA-2 the seed string
      sha2 = generateSha2( seed );
      // Convert SHA-2 to hexadecimal
      hexString = generateHex(sha2);
      // Truncate the hexadecimal string to make an OTP
      retString = generateOtp(hexString.toString());
    }
    catch (Exception e) {
      e.printStackTrace(System.out);
    }
    if( DEBUG && localDebug ) {
      System.out.println( "OTP generated: " + retString );
    }
    // Send it back
    return retString;
  }

  /**
   * Initialize the server OTP bank with a number of in-sequence OTPs
   * 
   * @param input The OTP bank to add the OTPs to
   * @return None
   */
  public static void initServer( LinkedList<String> otpbank, int capacity, String seed ) {
    if( DEBUG ) {
      System.out.println( "Initializing server OTP bank." );
      System.out.println( "Capacity: " + capacity );
      System.out.println( "Seed    : " + seed );
    }
    String currOTP = OTPfromSeed( seed );
    String prevOTP = "";
    for( int i = 0 ; i < capacity ; i++ ) {
      otpbank.addLast( currOTP );
      prevOTP = currOTP;
      currOTP = OTPfromSeed( prevOTP );
    }
  } // Closing initServer()


  /**
   * Run a set of tests and calculate collision metrics
   * 
   * @param input The number of tests to execute when searching for a collision
   * @return A double representing the collision rate for the number of tests
   */
  public static double testBattery( int numTests ) {
    // Initialize variables
    int numConsecutive = 0;
    int numCollisions = 0;
    HashSet<String> hashTable = new HashSet<String>();
    String prevOTP = "";
    String currOTP = "";

    // Run the tests
    // Start by initializing the "previous" OTP
    Instant instant = Instant.now();
    String timeStamp = instant.toString();
    prevOTP = OTPfromSeed( timeStamp + Math.random() );

    for( int i = 0 ; i < numTests ; i++ ) {
      // Make a new OTP - Random string salted with timestamp
      currOTP = OTPfromSeed( prevOTP );
      // Check if this OTP has been seen before...
      if( hashTable.contains( currOTP ) ) {
        // Collision detected! Count it
        numCollisions++;
        if( currOTP.equals( prevOTP ) ) {
          // Is it also a consecutive collision? If so, count that
          numConsecutive++;
        }
      }
      // This OTP has not been observed yet
      else { 
        hashTable.add( currOTP );
      }
      // Done, get ready for next round
      prevOTP = currOTP;
    }
    double collisionRate   = (double)numCollisions  / (double)numTests;
    double consecutiveRate = (double)numConsecutive / (double)numTests;

    if( DEBUG ) {
      System.out.println( "Collision rate   : " + collisionRate   );
      System.out.println( "Consecutive rate : " + consecutiveRate );
      System.out.println( "Consecutive count: " + numConsecutive  );
    }

    return collisionRate;
  } // Closing testBattery()
  
  /**
   * Retrieve the initialization vector for SHA-256 key generation.
   * 
   * @return The seed.
   */
  public static String getIV() {
    Scanner sc = null;
    File file;
    String key = "";
    try {
      file = new File("seed.txt");
      sc = new Scanner(file);
      key = sc.nextLine();
    } catch (Exception e) {
      e.printStackTrace(System.out);
    } finally {
      sc.close();
    }
    return key;
  }

  /**
   * Generate the SHA-256 value used for OTP creation from an initialization
   * vector.
   * 
   * @param input A string representing the seed value
   * @return A byte array represending the hash in UTF-8 format.
   * @throws NoSuchAlgorithmException
   */
  public static byte[] generateSha2(String input) throws NoSuchAlgorithmException {
    MessageDigest md = MessageDigest.getInstance("SHA-256");
    return md.digest(input.getBytes(StandardCharsets.UTF_8));
  }

  /**
   * Returns the hexidecimal format of the incoming byte array.
   * 
   * @param bytes A byte array representation of a SHA-256 hash.
   * @return The hexidecimal representation of a SHA-256 hash.
   */
  public static StringBuilder generateHex(byte[] bytes) {
    StringBuilder hexString = new StringBuilder();
    for (byte b : bytes) {
      hexString.append(String.format("%02X", b));
    }
    return hexString;
  }

  /**
   * Returns the one time password and increments the truncation counter.
   * 
   * @param hexString The hexidecimal string to be truncated.
   * @return The one time password.
   */
  public static String generateOtp(String hexString) {
    byte length = 6;
    if (hexString.length() >= length) {
      hexString = hexString.substring(0, 6);
    }
    return hexString;
  }

   /**
   * Initialize the graphical user interface.
   */
  private static void initGUI() {
    javax.swing.SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        createAndShowGUI();
      }
    });
  }

  // -------|---------|---------|---------|---------|---------|---------|---------|
  //
  // Graphical User Interface
  //
  // -------|---------|---------|---------|---------|---------|---------|---------|

  /**
   * Create and initialize the graphical user interface.
   */
  private static void createAndShowGUI() {
    // JButtons
    JButton btnOtp = new JButton("Generate OTP");
    JButton btnTest = new JButton("Test OTP");

    // JLabel
    JLabel label = new JLabel("OTP");

    // JPanel
    JPanel panel = new JPanel();
    panel.add(label);
    panel.add(btnOtp);
    panel.add(btnTest);

    // JFrame
    JFrame frame = new JFrame("Crypto OTP");
    frame.getContentPane().add(panel);

    // Action Listeners
    btnOtp.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent evt) {
        label.setText("Generate OTP Pressed");
      }
    });

    btnTest.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent evt) {
        label.setText("Test Pressed");
      }
    });

    // Format GUI.
    frame.setSize(500,500);
    frame.setPreferredSize(new Dimension(400, 300));
    frame.setLocationRelativeTo(null);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    // Display window.
    frame.pack();
    frame.setVisible(true);
}

} // Closing class Main
