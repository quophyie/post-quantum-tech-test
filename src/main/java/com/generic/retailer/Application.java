package com.generic.retailer;

import com.generic.retailer.cli.Cli;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class Application {

  public static void main(String[] args) {

    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));
    try(Cli cli = Cli.create(reader, writer)) {
      cli.run();
    } catch (Exception e) {
      System.exit(1);
    }
  }

}
