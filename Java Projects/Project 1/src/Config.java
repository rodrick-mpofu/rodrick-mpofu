import gnu.getopt.Getopt;
import gnu.getopt.LongOpt;
import java.util.Scanner;

/**
 * Store and process all configuration options for this project
 */
public class Config {

    // member variables to store settings

    // Check if in stack or queue mode
    boolean stackMode;

    // morph modes
    private boolean changeMode;
    private boolean swapMode;
    private boolean lengthMode;

    // output mode
    private boolean wordOutput = true;

    // begin word
    private String beginWord = "";

    // end word
    private String endWord = "";

    // checkpoints
    private boolean checkpoint1;
    private boolean checkpoint2;

    /**
     * Construct our configuration object and process the command line arguments
     *
     * @param args string of command line arguments
     */

    public Config(String[] args) {
        // Getopt processing
        LongOpt[] longOptions = {
                new LongOpt("stack", LongOpt.NO_ARGUMENT, null, 's'),
                new LongOpt("queue", LongOpt.NO_ARGUMENT, null, 'q'),
                new LongOpt("change", LongOpt.NO_ARGUMENT, null, 'c'),
                new LongOpt("swap", LongOpt.NO_ARGUMENT, null, 'p'),
                new LongOpt("length", LongOpt.NO_ARGUMENT, null, 'l'),
                new LongOpt("output", LongOpt.REQUIRED_ARGUMENT, null, 'o'),
                new LongOpt("begin", LongOpt.REQUIRED_ARGUMENT, null, 'b'),
                new LongOpt("end", LongOpt.REQUIRED_ARGUMENT, null, 'e'),
                new LongOpt("help", LongOpt.NO_ARGUMENT, null, 'h'),
                new LongOpt("checkpoint1", LongOpt.NO_ARGUMENT, null, 'x'),
                new LongOpt("checkpoint2", LongOpt.NO_ARGUMENT, null, 'y')

        };

        // Create a scanner to read inn our data
        Scanner s = new Scanner(System.in);

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
        Getopt g = new Getopt("Letterman", args, "sqcplo:b:e:hxy", longOptions);

        g.setOpterr(true);

        int choice;

        // ERROR Checking variables
        boolean routingModeSet = false;

        routeFreq(args);

//        String beginWord = "";
//        String endWord = "";


        // loop through all the arguments
        while ((choice = g.getopt()) != -1) {

            switch (choice) {

                case 's':
                    stackMode = true;
                    routingModeSet = true;
                    break;

                case 'q':
                    stackMode = false;
                    routingModeSet = true;

                    break;

                case 'c':

                    changeMode = true;

                    break;
                case 'p':

                    swapMode = true;

                    break;
                case 'l':
                    lengthMode = true;

                    break;
                case 'o':
                    // read the required string argument
                    String mode = g.getOptarg();

                    if (!mode.equals("M") && (!mode.equals("W"))) {
                        // we have an error
                        System.err.println("Only W and M are supported for modes");
                        System.exit(1);
                    }

                    wordOutput = mode.equals("W");

                    break;
                case 'b':

                    beginWord = g.getOptarg();

                    break;
                case 'e':

                    endWord = g.getOptarg();

                    break;
                case 'h':

                    printhelp();
                    break;
                case 'x':
                    checkpoint1 = true;
                    break;
                case 'y':
                    checkpoint2 = true;
                    break;

                default:
                    // TODO change choice from int to character
                    System.err.println("Unknown command line argument option: " + (char) choice);
                    System.exit(1);
            }
        } // while loop

        // TODO check that all required arguments are provided


        if (!routingModeSet) {
            System.err.println("One of stack or queue mode must be specified");
            System.exit(1);
        }


        if(!(swapMode || changeMode || lengthMode)) {
            System.err.println("At least one morph argument is required");
                System.exit(1);
        } else if(!lengthMode && (beginWord.length() != endWord.length())) {
            System.err.println("Length argument is required");
                System.exit(1);
        }


        }


    private void printhelp() {
        System.out.println("Usage: java [options] Main [-o W|M]|[-s][-q][-c][-p][-l][-b][-e][-h]}");
        System.out.println("The program will find a path to an end word through steps of converting letters \n " +
                "starting from an beginning word");
        System.out.println("--stack, -s: \n \t\t If this switch is set, use the stack-based routing scheme. ");
        System.out.println("--queue, -q: \n \t\t If this switch is set, use the queue-based routing scheme");
        System.out.println("--swap, -p: \n \t\t If this switch is set, Letterman is allowed to swap any two adjacent characters.");
        System.out.println("--length, -l: \n \t\t If this switch is set, Letterman is allowed to modify the length of a word, by inserting or deleting a single letter.");
        System.out.println("--output (W|M),  -o (W|M): \n \t\t Indicates the output file format by following the flag with a \n" +
                "\t\t W (word format) or M (modification format). " +
                "\n \t\t If the --output option is not specified, default to word output format (W)");
        System.out.println("--begin <word>, -b <word>: \n \t\t This specifies the word that Letterman starts with. \n" +
                "\t\t This flag must be specified on the command line, and when it is specified a word must follow it.");
        System.out.println("--end <word>, -e <word>: \n \t\t This specifies the word that Letterman must reach. \n" +
                "\t\t This flag must be specified on the command line, and when it is specified a word must follow it.");
        System.out.println("--help, -h: \n \t\t If this switch is set, the program should print a brief help message " +
                "\n \t\t which describes what the program does and what each of the flags are.");
        System.exit(0);
    }

    public static void routeFreq(String[] args) {

        int count_s = 0;
        int count_q = 0;

        for (String str : args) {

            if (str.equals("-s") || str.equals("--stack")) {
                count_s += 1;
            } else if (str.equals("-q") || str.equals("--queue")) {
                count_q += 1;
            }
        }
        if (count_q + count_s > 1) {
            System.err.println("Only 1 Stack argument or queue argument is required");
            System.exit(1);
        }
    }

    public boolean isChangeMode() {
        return changeMode;
    }

    public boolean isSwapMode() {
        return swapMode;
    }

    public boolean isLengthMode() {
        return lengthMode;
    }

    public boolean isCheckpoint1() {

        return checkpoint1;
    }

    public boolean isCheckpoint2() {
        return checkpoint2;
    }


    public boolean isStackMode() {
        return stackMode;
    }


    public boolean isWordOutput() {
        return wordOutput;
    }

    public String getBeginWord() {
        return beginWord;
    }

    public String getEndWord() {
        return endWord;
    }


}
