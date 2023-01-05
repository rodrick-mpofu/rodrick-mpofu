import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Logfile {

    public ArrayList<LogEntry> getMasterList() {
        return masterList;
    }

    private final ArrayList<LogEntry> masterList;

    public ArrayList<Integer> getIndexMap() {
        return indexMap;
    }

    private ArrayList<Integer> indexMap;

    // helper DS for indices
    private HashMap<String, ArrayList<Integer>> categoryMap;

    private HashMap<String, ArrayList<Integer>> keywordMap;

    public int getStartIdx() {
        return startIdx;
    }

    public int getEndIdx() {
        return endIdx;
    }

    // timestamp search results
    private int startIdx, endIdx;

    public ArrayList<Integer> getHashSearchResults() {
        return hashSearchResults;
    }

    // category/keyword search result
    private ArrayList<Integer> hashSearchResults;

    // excerpt List
    private final ArrayList<LogEntry> excerptList;

    private int loc = 0;

    public int loc2 = -1;

    public LastSearch getSearchKind() {
        return searchKind;
    }

    private LastSearch searchKind;

    public Logfile(String fn) {

        searchKind = LastSearch.None;
        startIdx = endIdx = -1;
        masterList = new ArrayList<>();
        excerptList = new ArrayList<>();

        try {
            Scanner in = new Scanner(new File(fn));

            loc = 0;
            while (in.hasNextLine()) {

                String line = in.nextLine();

                if (line.isBlank()) {
                    // empty line
                    break;
                }


                // do something with the log
                masterList.add(new LogEntry(line, loc, loc2));
                loc++;
            }
        } catch (FileNotFoundException e) {
            System.err.println(fn + " not found");
            System.exit(1);
        }

        // post-process the log entries
        postProcess();

    }

    /**
     * This method does all the of the processing of the log entries
     * to prepare us for all the user commands
     */
    private void postProcess() {
        SortingComparator comp = new SortingComparator();

        Collections.sort(masterList, comp);

        // Collections.sort(masterList, new SortingComparator());

        // next we will need to regenerate the mapping of the
        // original  index locations to support the append command
        indexMap = new ArrayList<>(masterList.size());
        for (int i = 0; i < masterList.size(); i++) {
            // insert a dummy value
            indexMap.add(0);
        }

        // set the mapping for the original locations in the master log file
        for (int i = 0; i < masterList.size(); i++) {
            LogEntry e = masterList.get(i);
            indexMap.set(e.getId(), i);
        }

        // prepare for Category search
        hashSearchResults = new ArrayList<>();
        prepCategorySearch();
        prepKeywordSearch();


    }

    private void prepKeywordSearch() {
        keywordMap = new HashMap<>();
        //keywordMap = new TreeMap<>();
        for (int i = 0; i < masterList.size(); i++) {
            LogEntry e = masterList.get(i);


            // split up the message into keywords
            String[] kwds = e.getMessage().substring(15).toLowerCase().split("[^a-zA-Z0-9]+");

            Set<String> set = new HashSet<String>();
            for (String kwd : kwds) {
                if (kwd.isBlank()) {
                    continue;
                }

                // add the current index to the entry for the keyword

                if (!keywordMap.containsKey(kwd)) {
                    keywordMap.put(kwd, new ArrayList<>());
                }
                // what about duplicate keywords?????
                // I fixed this with a hash set because it takes unique elements

                // add element to Set/HashSet
                boolean flagForDuplicate = set.add(kwd);
                if (flagForDuplicate) {
                    keywordMap.get(kwd).add(i);
                }
            }
        }
    }

    private void prepCategorySearch() {

        categoryMap = new HashMap<>();
        //categoryMap = new TreeMap<>();

        for (int i = 0; i < masterList.size(); i++) {
            LogEntry e = masterList.get(i);

            // if category is in mapping

            if (!categoryMap.containsKey(e.getCategory())) {
                categoryMap.put(e.getCategory(), new ArrayList<>());
            }
            categoryMap.get(e.getCategory()).add(i);
        }
    }

    public int size() {
        return masterList.size();
    }

    /**
     * Conduct a timestamp search for entries in the range [start, end]
     *
     * @param start the starting timestamp
     * @param end   the ending timestamp
     * @return number of elements found by this search
     */
    public int timestampSearch(long start, long end) {
        searchKind = LastSearch.Timestamp;
        hashSearchResults.clear();

        // find the index of the starting index and save it to startIdx

        startIdx = (Collections.binarySearch(masterList, new LogEntry(start), new LowerBound()) + 1) * -1;
        endIdx = (Collections.binarySearch(masterList, new LogEntry(end), new UpperBound()) + 1) * -1;


        //  return the value
        return endIdx - startIdx;
    }

    /**
     * Conduct a timestamp search for entries in the range [start, end]
     *
     * @param start the starting timestamp
     * @return number of elements found by this search
     */
    public int timestampSearch1(long start) {
        searchKind = LastSearch.Timestamp;
        hashSearchResults.clear();

        // find the index of the starting index and save it to startIdx

        startIdx = (Collections.binarySearch(masterList, new LogEntry(start), new LowerBound()) + 1) * -1;
        endIdx = (Collections.binarySearch(masterList, new LogEntry(start), new UpperBound()) + 1) * -1;

        // actually return the value
        return endIdx - startIdx;
    }

    public int categorySearch(String cat) {
        searchKind = LastSearch.Category;
        hashSearchResults.clear();
        // we cannot addAll of "null"
        // we need to check that the category exists before doing this

        if (categoryMap.get(cat.toLowerCase()) != null) {
            hashSearchResults.addAll(categoryMap.get(cat.toLowerCase()));
        }

        return hashSearchResults.size();

    }

    public int keywordSearch(ArrayList<String> kwds) {
        searchKind = LastSearch.Keyword;
        hashSearchResults.clear();
        // take the set intersection of all the kwds

        // won't work as shown with the spec
        // k & ui
        // split -> ["", "ui"]


        HashSet<Integer> set = new HashSet<>();

        if (keywordMap.get(kwds.get(0)) != null) {
            set.addAll(keywordMap.get(kwds.get(0)));
        }


        for (int i = 1; i < kwds.size(); i++) {
            if(keywordMap.containsKey(kwds.get(i))) {
                if (keywordMap.get(kwds.get(i)) != null) {
                    set.retainAll(keywordMap.get(kwds.get(i)));
                }
            } else {
                return 0;
            }
        }



        hashSearchResults.addAll(set);

        // loop from index 1 -- end of kwds
        // find the list of entries

        // hashSearchResults.retainAll(kwd search result for that next index)

        // retainAll on ArrayLists is O(n^2) --- TLE
        // HashSets 0(1) look up  --- O(n) for retain all
        // --- > this needs to be sorted at the end 0(n log n )
        // ALs are sorted, set intersection in O(n) with one pass over both lists
        return hashSearchResults.size();
    }

    public ArrayList<LogEntry> getExcerptList() {
        return excerptList;
    }

    private static class LowerBound implements Comparator<LogEntry> {

        @Override
        public int compare(LogEntry o1, LogEntry o2) {
            if (o1.getTimestamp() < o2.getTimestamp()) {
                return -1;
            } else {
                // return "greater than" for both == and >
                return 1;
            }
        }
    }

    private static class UpperBound implements Comparator<LogEntry> {

        @Override
        public int compare(LogEntry o1, LogEntry o2) {
            if (o1.getTimestamp() <= o2.getTimestamp()) {
                // return "less than" for both == and <
                return -1;
            } else {
                // return "greater than" for both == and >
                return 1;
            }
        }
    }


    public enum LastSearch {
        None,
        Timestamp,
        Category,
        Keyword
    }
}
