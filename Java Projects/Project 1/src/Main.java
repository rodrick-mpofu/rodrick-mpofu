public class Main {

    public static void main(String[] args) {
        Config c = new Config(args);
        // construct the letterman puzzle solver
        Letterman l = new Letterman(c);


        // process input
        l.readDictionary();


        if (c.isCheckpoint1()) {
            l.printDictionary();
            System.exit(0);
        }
        l.search();
        if (c.isCheckpoint2()) {
            System.exit(0);
        }
        if(c.isWordOutput()) {
            l.backtrack();
        }

    }
}
