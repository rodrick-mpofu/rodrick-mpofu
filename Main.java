import gnu.getopt.Getopt;
import gnu.getopt.LongOpt;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        // Create a scanner to read inn our data
        Scanner s = new Scanner(System.in);

        LongOpt[] longOptions = {
                // either specify --help or -h
                new LongOpt("help", LongOpt.NO_ARGUMENT, null, 'h'),
                new LongOpt("verbose", LongOpt.NO_ARGUMENT, null, 'v'),
                new LongOpt("mode", LongOpt.REQUIRED_ARGUMENT, null, 'm')

        };

        // construct the Get-opt object to process the args variable
        /*
         * To construct:
         * - name of the program
         * - args array
         * - short option string
         *   + list alphabetically or in order from log option array
         *   + for short options with required additional argument, a ':' after the letter
         * - long options array
         */
        Getopt g = new Getopt("Project0", args, "hvm:", longOptions);

        g.setOpterr(true);

        // store the most recent argument to process
        int choice;

        String num = s.next();

        String mode = "";

        int m_pos = Arrays.binarySearch(args, "-m");
        int m_pos1 = Arrays.binarySearch(args, "--mode");

        // look for --mode too

        if (m_pos < 0 && m_pos1 < 0) {

            System.err.println("Missing --mode/-m flag on the command line");

            System.exit(1);
        }


        while ((choice = g.getopt()) != -1) {

            switch (choice) {

                case 'h' -> {

                    printHelp();

                    System.exit(0);

                }
                case 'm' -> {

                    mode = g.getOptarg();

                    if (!mode.equals("average") && !mode.equals("median")) {

                        // error: invalid
                        System.err.println("Error: invalid mode: " + mode);

                        System.exit(1);

                    }

                }
                // boolean for verbose
                case 'v' -> {
                    printVerbose(num);
                }


                default -> {
                    // this will run if none of the other cases match
                    // it should
                    System.err.println("Error: Invalid mode: " + mode);

                    System.exit(1);

                }

            } // switch


        } // while


        // TODO Make sure the required arguments were set (mode)


        //if verbose stuff
        // array in verbose

        calc_stats(mode, s, num);


    } // main


    public static void printHelp() {

        System.out.println("Usage: java [options] Main [-m average|median]|[-h][-v]}");

        System.out.println("This program is an example of processing command");

        System.out.println("line commands with Get-opt.");
    }

    public static void printVerbose(String num) {

        System.out.println("Reading " + num + " numbers.");
        System.out.println("Read " + num + " numbers.");

    }

    public static void calc_stats(String mode, Scanner s, String num) {

        double size = Double.parseDouble(num);

        ArrayList<Double> arr = new ArrayList<>();

        int count = 0;

        while (s.hasNextDouble()) {
            count += 1;
            arr.add(s.nextDouble());
        }

        if (count > Integer.parseInt(num)) {

            // More numbers provided in the input than were specified
            System.err.println("Error: expected input was" + size);

            System.exit(1);
        }


        if (size == 0) {

            System.out.println("No data => no statistics!");

            System.exit(1);
        }
        if (mode.equals("average")) {

            double total = 0;

            for (double d : arr) {

                total += d;

            }
            double avg = total / arr.size();

            double avg1 = Math.round(avg * 100.0) / 100.0;

            System.out.println("Average: " + avg1);
        }
        if (mode.equals("median")) {

            Collections.sort(arr);

            int pos;

            pos = (int) (size / 2);

            String med;

            if (size % 2 != 0) {

                med = String.valueOf(arr.get(pos));

            } else {

                double center = (arr.get(pos) + arr.get(pos - 1)) / 2;

                med = String.valueOf(center);
            }
            System.out.println("Median: " + med);

        }

    }


}





