import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Scanner;

public class Letterman {

    private final Config c;

    // variable to store our dictionary
    private ArrayList<WordInfo> dictionary;
    // Initialize the begin index and end index to -1
    private int beginIndex = -1;
    private int endIndex = -1;

    // Constructor
    public Letterman(Config c) {
        this.c = c;

    }


    /**
     * read the dictionary from standard input
     */
    public void readDictionary() {
        // Scanner object
        Scanner in = new Scanner(System.in);

        // get number of words in dictionary
        int count = in.nextInt();

        // read to the end of the line
        in.nextLine();

        // construct our ArrayList
        dictionary = new ArrayList<>(count);

        // read in all the words
        while (in.hasNextLine()) {
            String line = in.nextLine();

            // check for blank line
            if (line.length() == 0) {
                // if there is a blank line break
                break;
            }

            // either  have a word or a comment
            // comment begins with two slashes

            if (line.charAt(0) == '/' && line.charAt(1) == '/') {
                // This means that there is a comment and so continue
                continue;
            }
            // check if this is the begin word
            if (line.equals(c.getBeginWord())) {
                // Set the beginIndex to the index of the word as word are being read in
                beginIndex = dictionary.size();
            } else if (line.equals(c.getEndWord())) {
                // Set the endIndex to the index of the word as word are being read in
                endIndex = dictionary.size();
            }

            // we have a word
            addWord(line, c.getBeginWord(), dictionary);
        }

        // Check to see if the start word is in the dictionary
        if (beginIndex == -1) {
            // This will print out an error message when there word is not found
            System.err.println("Start word does not exist in dictionary");
            System.exit(1);
        }

        // Check to see if the end word is in the dictionary
        if (endIndex == -1) {
            // This will print out an error message when there word is not found
            System.err.println("End word does not exist in dictionary");
            System.exit(1);
        }

        // print the size of the dictionary
        System.out.println("Words in dictionary: " + count);

    }

    /**
     * Search from a beginning word to an end word
     */

    public void search() {

        // deque to keep track of our reachable collection
        // store index of the word we're processing from our dictionary AL
        ArrayDeque<Integer> processing = new ArrayDeque<>();

        // Initialize the count for counting the processed and added words
        // numoftimes is keeping tack of the number of processed words
        int numoftimes = 0;
        int count = 0;
        // Initially populate with the starting word
        // mark as visited and add to the deque
        dictionary.get(beginIndex).visited = true;

        // If in stack mode add the begin word to head
        // If in queue mode insertion is from the tail
        // increment count
        if (c.isStackMode()) {
            processing.addFirst(beginIndex);
        } else {
            processing.addLast(beginIndex);
        }
        if(c.isCheckpoint2()) {
            printOutProcess("add", dictionary.get(beginIndex).text, 0);
        }
        count++; // increment count

        // while not empty and end word is not visited
        while (!processing.isEmpty() && !dictionary.get(endIndex).visited) {

            // remove next element
            int currIdx = processing.removeFirst();
            WordInfo curr = dictionary.get(currIdx);
            // loop through dictionary and look for sufficiently similar items
            numoftimes++; // increment numoftimes

            // If curr is equal to begin word
            //set the previous to be -1
            if (curr.text.equals(c.getBeginWord())) {
                curr.previous = -1;
            }
            // This will print out processed words if checkpoint2 flag is enabled
            if(c.isCheckpoint2()) {
                printOutProcess("processing", curr.text, numoftimes);
            }
            // loop through the dictionary and compare the words
            for (int i = 0; i < dictionary.size(); i++) {


                // skip ourselves
                if (currIdx == i) {
                    continue;
                }
                // Set other to be a word in the dictionary
                WordInfo other = dictionary.get(i);

                // If a word is not marked as visited and is sufficiently similar
                if (!other.visited && sufficientlySimilar(curr.text, other.text)) {

                    // visit and add
                    other.visited = true;
                    // set the previous index
                    other.previous = currIdx;

                    if(c.isStackMode()) {
                        // check if the processing arrayDeque
                        // contains
                        processing.addFirst(dictionary.indexOf(other));
                        // if checkpoint2 flag is enabled
                        // print out the word being added

                    } else {

                        processing.addLast(dictionary.indexOf(other));
                        // if checkpoint2 flag is enabled
                        // print out the word being added
                    }
                    if(c.isCheckpoint2()) {
                        printOutProcess("add", other.text, 0);
                    }

                    // check if the end word has been added
                    // and if not continue
                    if (!processing.contains(endIndex)) {
                        count++; // increment count
                    } else {
                        count++; // increment count and break
                        break;
                    }

                }
            }

        }
        // Check if end word if marked or is visited
        // if not print that no solution was found otherwise
        // print out the solution with the words checked
        if (!dictionary.get(endIndex).visited) {
            System.out.println("No solution, " + count + " words checked.");
            System.exit(0);
        } else {
            System.out.println("Solution, " + count + " words checked.");
            backtrack();
            System.exit(0);
        }

    }

    private boolean sufficientlySimilar(String a, String b) {
        // change: 1 character difference
        // swap: 2 character difference with the two characters adjacent and swapped
        // length: 1 character difference and 1 character length difference

        int charDifference = 0; // keep track of the character difference
        boolean change_on = false; // check if change mode can be applied
        boolean swap_on = false; // check if swap mode is on

        // if the difference between the two words
        // is greater than 1 return false
        if(Math.abs(a.length() - b.length()) > 1) {
            return false;
        }

        if (a.length() == b.length()) {
            // only swap and change will apply
            // go character by character
            // to check for equivalence
            for (int i = 0; i < a.length(); i++) {
                if (a.charAt(i) != b.charAt(i)) {
                    // we have a character difference
                    charDifference++; // increment chardifference
                }
            }
            // if in change mode and character difference is less than 1
            // and return true if true
            if (c.isChangeMode()) {
                if (charDifference <= 1) {
                    change_on = true;
                }
            }
            if (c.isSwapMode()) {

                // check if difference is two
                // check if both 1st and 2nd are true
                // and then return true
                // initialize the index of the curr letter and previous letter if there is a character difference
                int prev = -1;
                int curr = -1;

                for (int i = 0; i < a.length(); i++) {

                    if (a.charAt(i) != b.charAt(i)) {
                        prev = curr;
                        curr = i;
                    }

                }
                // if the character difference is 2 and the two words in the two different positions
                // return true
                if (charDifference == 2 &&
                        a.charAt(prev) == b.charAt(curr) &&
                        a.charAt(curr) == b.charAt(prev) && (curr - prev == 1)) {
                    swap_on = true;
                }

            }
            return swap_on || change_on; // either swap mode or change mode is turned on

        } else if (c.isLengthMode()) {
            // check which word is longer
            String longStr = a;
            String shortStr = b;

            // b is longer switch them around
            if (b.length() > a.length()) {
                longStr = b;
                shortStr = a;
            }
            if (longStr.length() - shortStr.length() == 1) {
                int k = 0; // initialize a count for comparing the index for in the long string
                int charDiff_len = 0; // initialize a count for the difference in characters
                for (int i = 0; i < shortStr.length(); i++) {
                    if (shortStr.charAt(i) != longStr.charAt(k)) {
                        charDiff_len++; // increment the difference in character
                        i--; // decrement the index for the short string if there is character difference
                    }
                    k++; // increment the index of the longer string

                    // If charDifference is more than 1 return false
                    if (charDiff_len > 1) {
                        return false;

                    }

                }
                // Check if the character difference is 0 and if the character at the last index at the short string
                // is not the same as the character at the last index at the longer string return true
                // OR if the character difference is 0 and if the character at the last index at the short string
                // is the same as the character at the last index at the longer string return true
                if (charDiff_len == 0 && (shortStr.charAt(shortStr.length() - 1) != longStr.charAt(longStr.length() - 1))) {
                    return true;
                } else if (charDiff_len == 0 && (shortStr.charAt(shortStr.length() - 1) == longStr.charAt(longStr.length() - 1))) {
                    return true;
                } else {
                    return charDiff_len == 1;
                }
            }

        }
        return false;
    }

    public void backtrack() {

        // Create an Arraylist to store the words
        ArrayList<String> path = new ArrayList<>();

        // Initialize the curr to start at the end word
        int curr = endIndex;

        // loop while we haven't reached the beginword
        while (curr != -1) {

            path.add(0, dictionary.get(curr).text); // add the words to the ArrayList
            curr = dictionary.get(curr).previous; // Get the previous word
        }

        System.out.println("Words in morph: " + path.size()); // print out the number of words in the ArrayList

        if (c.isWordOutput()) {
            for (String s : path) {
                System.out.println(s); // Retrieve the words in the path ArrayList
            }
        } else {
            System.out.println(path.get(0)); // Print out the start word
            int k = 1; // initialize count
            // loop through path Arraylist
            for (int i = 0; i < path.size(); i++) {
                if (k >= path.size()) {
                    break;
                }
                modificationOutput(path.get(i), path.get(k));
                k++; // increment the count
            }
        }
    }

    /**
     * Output the modification required to go from string a to string b
     * At this point, we already know one of the morphs applies, we just have to
     * determine which on
     *
     * @param a starting word for the morph
     * @param b ending word for the morph
     */
    private void modificationOutput(String a, String b) {
        // need to find the first difference between String a and b
        int pos = 0;
        // length of the shorter string - 1
        int maxPosition = Math.min(a.length(), b.length());

        while (pos < maxPosition) {
            // check for a difference
            if (a.charAt(pos) != b.charAt(pos)) {
                // we have found the position of the change
                break;
            }
            pos++;
        }

        // pos is either (1) the position of the change or (2) the index of the last character in the longer string
        // change, swap, insert or delete


        if (a.length() == b.length()) {
            int charDifference = 0;
            for (int i = 0; i < a.length(); i++) {
                if (a.charAt(i) != b.charAt(i)) {
                    charDifference++; // increment the count for the character difference
                }
            }
            // If there is one character difference
            // means that either a swap or change was made
            if (charDifference == 1) {
                // change was made
                System.out.println("c," + pos + "," + b.charAt(pos));
            } else {
                // swap was made
                System.out.println("s," + pos);
            }


        } else if (a.length() < b.length()) {
            // insert
            // i, <position>, <letter>
            // String b will be longer
            // so need character from b at this position

            // Ex: 0123
            // a: let
            // b: leet
            System.out.println("i," + pos + "," + b.charAt(pos));
        } else {
            // delete
            // string b will be shorter
            System.out.println("d," + pos);
        }
    }

    /**
     * output all words in the dictionary
     */

    public void printDictionary() {
        // enhanced for loop (for-each/ for-in loop)
        for (WordInfo w : dictionary) {
            System.out.println(w.text);
        }
    }

    /**
     * This function will reduce the runtime in checking and adding words to the dictionary if lenghtmode flag is not
     * given. The assumption will be that there is not need to delete or insert words thus the length will always be the
     * same. Words with great length then will not be considered
     * @param s is the String representation of the line being read
     * @param b is the begin word
     * @param arr is the arr in which words are being added
     */
    public void addWord (String s, String b, ArrayList <WordInfo> arr) {
        if(!c.isLengthMode()) {
            if(s.length() == b.length() ) {
                arr.add(new WordInfo(s));
            }
        }else {
            arr.add(new WordInfo(s));
        }
    }

    /**
     * This method prints out the added and processed words
     * @param a Is either add or processing to specify which operation is being done
     * @param wrd this is the added or processed words
     * @param b the count for the processed word
     */
    public static void printOutProcess(String a, Object wrd, int b) {

        if (a.equals("add") && b == 0) {
            System.out.println("  adding " + wrd);
        } else if (a.equals("processing")) {
            System.out.println(b + ": processing " + wrd);
        }
    }


    //inner class (helper class) to store dictionary words
    private static class WordInfo {
        String text;
        boolean visited;
        int previous; // keep track of the index of the previous word


        public WordInfo(String text) {

            this.text = text;
            this.visited = false;
        }

    }
}
