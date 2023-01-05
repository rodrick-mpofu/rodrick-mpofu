public class Main {
    public static void main(String[] args) {
        Config c = new Config(args);

        // construct the stock simulator
        StockMarketSimulation s = new StockMarketSimulation(c);
        // Print out the first line
        System.out.println("Processing orders...");

        // run through the necessary steps
        s.simulate();

        // construct new stock to get the summary output
        Stock t = new Stock(c);
        t.summaryOutput();
        // this will get the trader info and print it out
        s.getTraderInfo();
    }
}