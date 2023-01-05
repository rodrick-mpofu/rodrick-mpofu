public class LogEntry {

    private String raw;

    private int entryId;

    private int entryId_2;

    private String category;

    private final long timestamp;

    private String message;

    public LogEntry(String line, int entryID, int entryID_2) {

        raw = line;

        timestamp = convertTimeToLong(line);

        entryId_2 = entryID_2;

        entryId = entryID;

        // set the category... substring from a specific pos 15  -- find("|", 16) --> toLowerCase()

        category = cat(line);

        message = msg(line);

    }

    public LogEntry(long start) {
        timestamp = start;
    }

    public int getId() {
        return entryId;
    }

    public static long convertTimeToLong(String time) {
        // M M : D D : h h : m m  :  s  s
        // 0 1 2 3 4 5 6 7 8 9 10 11 12 13

        return (long) (time.charAt(13) - '0') +
                (time.charAt(12) - '0') * 10L +
                (time.charAt(10) - '0') * 100L +
                (time.charAt(9) - '0') * 1000L +
                (time.charAt(7) - '0') * 10000L +
                (time.charAt(6) - '0') * 100000L +
                (time.charAt(4) - '0') * 1000000L +
                (time.charAt(3) - '0') * 10000000L +
                (time.charAt(1) - '0') * 100000000L +
                (time.charAt(0) - '0') * 1000000000L;

    }

    public static String cat(String raw_cat) {

        return raw_cat.substring(15, raw_cat.indexOf("|", 15)).toLowerCase();
    }

    public static String msg(String raw) {

        String str = cat(raw);
       int str_len = str.length();

        return raw.substring(15 + str_len + 1).toLowerCase();
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getCategory() {
        return category;
    }

    public String getMessage() {
        return raw;
    }
}
