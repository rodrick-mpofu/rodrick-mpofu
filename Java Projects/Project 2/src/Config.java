import gnu.getopt.Getopt;
import gnu.getopt.LongOpt;

import java.util.Scanner;

public class Config {

    // create boolean values for verbose, traderinfo and median
    // verbose
    boolean verbose;
    // traderInfo
    boolean traderInfo;
    // median
    boolean median;

    public Config(String[] args) {

        // Getopt processing
        LongOpt[] longOptions = {
                new LongOpt("verbose", LongOpt.OPTIONAL_ARGUMENT, null, 'v'),
                new LongOpt("median", LongOpt.OPTIONAL_ARGUMENT, null, 'm'),
                new LongOpt("trader-info", LongOpt.OPTIONAL_ARGUMENT, null, 'i')
        };


        // Create a scanner to read inn our data
        Scanner s = new Scanner(System.in);

        // construct the Get-opt object to process the args variable
        /**
         * To construct:
         * - name of the program
         * - args array
         * - short option string
         *   + list alphabetically or in order from log option array
         *   + for short options with required additional argument, a ':' after the letter
         * - long options array
         **/
        Getopt g = new Getopt("Stock Market Simulator", args, "vmi", longOptions);

        g.setOpterr(true);

        int choice;

        // loop through all the arguments
        while ((choice = g.getopt()) != -1) {


            switch (choice) {

                case 'v':
                    verbose = true;
                    break;
                case 'm':
                    median = true;
                    break;
                case 'i':
                    traderInfo = true;
                    break;
                default:
                    // unknown argument
                    System.err.println("Unknown command line argument option: " + (char) choice);
                    System.exit(1);
            }


        }
    }


    // is median flag set
    public boolean isMedian() {
        return median;
    }


}
