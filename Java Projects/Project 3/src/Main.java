import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Main {

    // print help function
    public static void printHelp() {

        System.out.println("Usage: Logman LOGFILE | - h | --help");
    }

    // Main
    public static void main(String[] args) {
        // if the argument array is not enjoy to 1
        // print help
        if (args.length != 1) {
            printHelp();
            System.exit(1);
        }

        // print help
        if (args[0].equals("-h") || args[0].equals("--help")) {
            printHelp();
            System.exit(0);
        }

        Logfile lf = new Logfile(args[0]);

        System.out.println(lf.size() + " entries read");

        Scanner in = new Scanner(System.in);

        System.out.print("% ");

        // while loop
        while (in.hasNextLine()) {


            String line = in.nextLine();


            // check if the line is blank

            if (line.isBlank()) {
                break;
            }

            // get the char at zero
            char command = line.charAt(0);

            // NOTE: you can use this to extract the arguments for a command
            // String arguments = line.substring(2);

            // process each of the command types

            switch (command) {

                case '#':
                    // this is a comment, nothing happens
                    break;

                case 'q':
                    // just exit out
                    System.exit(0);
                    break;

                case 'k':
                    // get the string
                    String raw_keywords = line.substring(2).toLowerCase();
                    // split the string
                    String[] raw_split = raw_keywords.split("[^a-zA-Z0-9]+");
                    ArrayList<String> kwds = new ArrayList<>();
                    // add to kwds
                    for (String s : raw_split) {
                        if (!s.isBlank()) {
                            kwds.add(s);
                        }
                    }
                    // can call keyword search
                    int numResults = lf.keywordSearch(kwds);

                    System.out.println("Keyword search: " + numResults + " entries found");
                    break;

                // q # a p d b e s l c g r t m k
                case 'a':
                    String pos_str = line.substring(2);
                    // parse to string
                    int pos = Integer.parseInt(pos_str);

                    if (!(pos >= 0 & pos < lf.getMasterList().size())) {
                        System.err.println(pos_str + " is not a valid position within" +
                                " the associated list.");
                        break;
                    }

                    // get the index

                    LogEntry e = lf.getMasterList().get(lf.getIndexMap().get(pos));

                    // add to except list
                    lf.getExcerptList().add(e);

                    int x = lf.getExcerptList().indexOf(e);

                    lf.getExcerptList().remove(e);

                    lf.getExcerptList().add(new LogEntry(e.getMessage(), pos, x));

                    System.out.println("log entry " + pos + " appended");

                    break;
                case 'p':

                    // get the except list
                    ArrayList<LogEntry> arr = lf.getExcerptList();

                    // print everything out

                    for (int i = 0; i < arr.size(); i++) {
                        System.out.println(i + "|" + arr.get(i).getId() + "|" + arr.get(i).getMessage());
                    }
                    break;
                case 'd':
                    String pos_str_t = line.substring(2);
                    int pos_t = Integer.parseInt(pos_str_t);

                    if (!(pos_t >= 0 & pos_t < lf.getExcerptList().size())) {
                        System.err.println(pos_str_t + " is not a valid position within" +
                                " the associated list.");
                        break;
                    }

                    LogEntry e_t = lf.getExcerptList().get(pos_t);

                    lf.getExcerptList().remove(e_t);

                    System.out.println("Deleted excerpt list entry " + pos_t);

                    break;

                case 'b':

                    String pos_str_b = line.substring(2);
                    int pos_b = Integer.parseInt(pos_str_b);


                    if (!(pos_b >= 0 & pos_b < lf.getExcerptList().size())) {
                        System.err.println(pos_str_b + " is not a valid position within" +
                                " the associated list.");
                        break;
                    }

                    // shift

                    lf.getExcerptList().add(0, lf.getExcerptList().remove(pos_b));

                    System.out.println("Moved excerpt list entry " + pos_b);

                    break;

                case 'e':

                    String pos_str_e = line.substring(2);

                    int pos_e = Integer.parseInt(pos_str_e);

                    if (!(pos_e >= 0 & pos_e < lf.getExcerptList().size())) {
                        System.err.println(pos_str_e + " is not a valid position within" +
                                " the associated list.");
                        break;
                    }

                    // shift

                    lf.getExcerptList().add(lf.getExcerptList().size() - 1, lf.getExcerptList().remove(pos_e));

                    System.out.println("Moved excerpt list entry " + pos_e);

                    break;


                case 's':

                    System.out.println("excerpt list sorted");

                    if (lf.getExcerptList().isEmpty()) {
                        System.out.println("(previously empty)");
                    } else {
                        System.out.println("previous ordering:");

                        if (lf.getExcerptList().size() == 0) {
                            break;
                        }

                        System.out.println(0 + "|" +
                                lf.getExcerptList().get(0).getId() + "|" +
                                lf.getExcerptList().get(0).getMessage());

                        System.out.println("...");

                        System.out.println(lf.getExcerptList().size() - 1 + "|" +
                                lf.getExcerptList().get(lf.getExcerptList().size() - 1).getId() + "|" +
                                lf.getExcerptList().get(lf.getExcerptList().size() - 1).getMessage());

                        System.out.println("new ordering:");

                        SortingComparator sc = new SortingComparator();

                        lf.getExcerptList().sort(sc);

                        System.out.println(0 + "|" +
                                lf.getExcerptList().get(0).getId() + "|" +
                                lf.getExcerptList().get(0).getMessage());

                        System.out.println("...");

                        System.out.println(lf.getExcerptList().size() - 1 + "|" +
                                lf.getExcerptList().get(lf.getExcerptList().size() - 1).getId() + "|" +
                                lf.getExcerptList().get(lf.getExcerptList().size() - 1).getMessage());

                    }

                    break;
                case 'l':

                    if (lf.getExcerptList().size() != 0) {
                        System.out.println("excerpt list cleared");
                        System.out.println("previous contents:");
                        ArrayList<LogEntry> arr1 = lf.getExcerptList();

                        System.out.println(0 + "|" +
                                arr1.get(0).getId() + "|" + arr1.get(0).getMessage());

                        System.out.println("...");


                        System.out.println(arr1.size() - 1 + "|" +
                                arr1.get(arr1.size() - 1).getId() + "|" + arr1.get(arr1.size() - 1).getMessage());

                        lf.getExcerptList().clear();


                    } else {
                        System.out.println("excerpt list cleared");
                        System.out.println("(previously empty)");
                    }

                    break;

                case 'c':

                    String raw_keywords_c = line.substring(2).toLowerCase();

                    // can call
                    int numResults_c = lf.categorySearch(raw_keywords_c);

                    System.out.println("Category search: " + numResults_c + " entries found");

                    break;


                case 'g':

                    Logfile.LastSearch search_type = lf.getSearchKind();

                    if (search_type.equals(Logfile.LastSearch.Category) ||
                            search_type.equals(Logfile.LastSearch.Keyword)) {

                        ArrayList<Integer> arr1 = lf.getHashSearchResults();

                        Collections.sort(arr1);

                        for (Integer num : arr1) {
                            System.out.println(lf.getMasterList().get(num).getId() + "|" +
                                    lf.getMasterList().get(num).getMessage());
                        }
                    } else if (search_type.equals(Logfile.LastSearch.Timestamp)) {

                        int start = lf.getStartIdx();

                        int end = lf.getEndIdx();

                        for (int i = start; i < end; i++) {
                            System.out.println(lf.getMasterList().get(i).getId() + "|" +
                                    lf.getMasterList().get(i).getMessage());
                        }

                    } else {
                        System.err.println("No search has occurred");
                    }

                    break;

                case 'r':

                    Logfile.LastSearch search_type1 = lf.getSearchKind();

                    if (search_type1.equals(Logfile.LastSearch.Category) ||
                            search_type1.equals(Logfile.LastSearch.Keyword)) {
                        ArrayList<Integer> arr1 = lf.getHashSearchResults();

                        ArrayList<LogEntry> newAL = new ArrayList<>();

                        int f = 0;

                        for (Integer num : arr1) {
                            newAL.add(lf.getMasterList().get(num));
                        }

                        f = newAL.size();

                        SortingComparator comp = new SortingComparator();

                        newAL.sort(comp);


                        for (LogEntry lgE : newAL) {
                            lf.getExcerptList().add(new LogEntry(lgE.getMessage(),
                                    lgE.getId(),
                                    lf.getExcerptList().size()));
                        }

                        System.out.println(f + " log entries appended");

                    } else if (search_type1.equals(Logfile.LastSearch.Timestamp)) {

                        int start = lf.getStartIdx();

                        int end = lf.getEndIdx();

                        for (int i = start; i < end; i++) {
                            lf.getExcerptList().add(new
                                    LogEntry(lf.getMasterList().get(i).getMessage(),
                                    lf.getMasterList().get(i).getId(), lf.getExcerptList().size()));
                        }

                        System.out.println(end - start + " log entries appended");

                    } else {
                        System.err.println("No search has occurred");
                    }


                    break;

                case 't':

                    String[] time = line.substring(2).split("[|]");

                    String ts_1 = time[0];

                    int ts_1_len = ts_1.length();

                    String ts_2 = time[1];

                    // check if it has 14 characters
                    int ts_2_len = ts_2.length();

                    if (ts_1_len == 14 & ts_2_len == 14) {


                        long ts_1_num = LogEntry.convertTimeToLong(ts_1);

                        long ts_2_num = LogEntry.convertTimeToLong(ts_2);

                        int result = lf.timestampSearch(ts_1_num, ts_2_num);

                        System.out.println("Timestamps search: " + result + " entries found");

                        break;
                    } else {
                        System.err.println("Timestamps should have 14 characters");
                        break;

                    }


                case 'm':

                    String ts = line.substring(2);

                    int ts_len = ts.length();

                    if (ts_len != 14) {
                        System.err.println("Timestamps should have 14 characters");
                        break;
                    }

                    long ts_num = LogEntry.convertTimeToLong(ts);

                    int result_1 = lf.timestampSearch1(ts_num);

                    System.out.println("Timestamp search: " + result_1 + " entries found");

                    break;

                default:
                    System.err.println("Unexpected command " + command);
            }// switch

            System.out.print("% ");

        } // while
    }// main()
}// Class Main
