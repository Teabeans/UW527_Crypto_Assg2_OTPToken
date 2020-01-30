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
import java.io.File;
import java.util.Scanner; // For user inputs

public class Main {

  // -------|---------|---------|---------|---------|---------|---------|---------|
  //
  // GLOBAL CONSTANTS
  //
  // -------|---------|---------|---------|---------|---------|---------|---------|

  static final boolean DEBUG = true;

  // -------|---------|---------|---------|---------|---------|---------|---------|
  //
  // PROGRAM DRIVER
  //
  // -------|---------|---------|---------|---------|---------|---------|---------|

  // NOTE:
  // Initial driver version for testing out each of the methods. Will refactor
  // into usable form.

  public static void main(String[] args) {

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
    }

// -------|---------|---------|---------|
// CLIENT
// -------|---------|---------|---------|
    else if( role.equals( "CLIENT" ) ) {
      // Do CLIENT things here
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

  } // Closing main()

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

} // Closing class Main
