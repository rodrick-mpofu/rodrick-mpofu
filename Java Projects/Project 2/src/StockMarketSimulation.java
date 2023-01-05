import java.util.ArrayList;
import java.util.Scanner;

public class StockMarketSimulation {

    // variable to store stocks
    private final ArrayList<Stock> stocks;
    // variable to store traders
    private final ArrayList<Trader> traders;

    private final Config config;
    // Scanner
    private Scanner in;
    // keep track of the current index
    private long curr_idx;
    // keep track of number of traders
    private final int numTraders;
    // keep track of the number of stocks
    private final int numStocks;


    // constructor
    public StockMarketSimulation(Config c) {
        config = c;

        // read in an initial configuration
        in = new Scanner(System.in);

        // COMMENT: <COMMENT>
        // MODE: <INPUT_MODE>
        // NUM_TRADERS: <NUM_TRADERS>
        // NUM_STOCKS: <NUM_STOCKS>

        // skip over the comment
        in.nextLine();

        // throw away header
        in.next();
        String mode = in.next();

        // throw away header
        in.next();
        numTraders = in.nextInt();

        // throw away header
        in.next();
        numStocks = in.nextInt();

        // construct ALs with the correct capacity
        traders = new ArrayList<>(numTraders);

        // add to traders arraylist
        for (int j = 0; j < numTraders; j++) {
            traders.add(new Trader(j));
        }

        stocks = new ArrayList<>(numStocks);

        // populate stocks
        for (int i = 0; i < numStocks; i++) {
            stocks.add(new Stock(config));
        }

        // check for PR mode
        if (mode.equals("PR")) {
            //RANDOM_SEED: <SEED>
            //NUMBER_OF_ORDERS: <NUM_ORDERS>
            //ARRIVAL_RATE: <ARRIVAL_RATE>

            in.next();
            int seed = in.nextInt();

            in.next();
            int numOrders = in.nextInt();

            in.next();
            int arrivalRate = in.nextInt();

            // allows us to read orders one at a time
            in = P2Random.PRInit(seed, numTraders, numStocks, numOrders, arrivalRate);
        }


    }

    public void simulate() {
        // implement the main processing loop for simulation

        long currentTime = 0;


        // how should I check if I am done?
        // what error will show up if we use in.hasNextline
        // I think I fixed this
        while (in.hasNextLong()) {
            Order nextOrder = getNextOrder();


            if (nextOrder.getTimestamp() > currentTime) {
                // the timestamp changes

                // if median flag is set
                med(currentTime);

                currentTime = nextOrder.getTimestamp(); // get the current time


            // error check
            } else if (nextOrder.getTimestamp() < currentTime) {

                System.err.println("Timestamp should be non-decreasing");
                System.exit(1);
            }


            Stock s = stocks.get(nextOrder.getStockId());

            // add the order to the stock
            s.addOrder(nextOrder);


            // perform matches
            s.performMatches(traders);


        } // end of while loop

        med(currentTime);

    }

    private void med(long currentTime) {
        // if median flag is set
        if (config.median) {

            int i = 0; // initialize i
            while (i < stocks.size()) {

                // get the stock i in stocks
                Stock s = stocks.get(i);

                // check if transaction has occurred
                if (s.isHasTransOccurred()) {
                    // get the median
                    int med = s.getMedian();

                    // print the median information
                    printMedian(i, currentTime, med);
                }

                i++; // increment
            }
        }
    }

    /**
     * Read and return the next order from im
     *
     * @return Order object with the next order to process
     */

    private Order getNextOrder() {
        // <TIMESTAMP> <BUY/SELL> T<TRADER_ID> S<STOCK_NUM> $<PRICE> #<QUANTITY>

        long ts = in.nextLong();

        // error check
        if (ts < 0) {
            System.err.println("The timestamp should be a non-negative integer");
            System.exit(1);
        }

        // keep track of the intent either buy or sell
        String intent = in.next();

        // get trader id
        int traderId = Integer.parseInt(in.next().substring(1));

        // error check
        if (traderId < 0 || traderId >= numTraders) {
            System.err.println("The trader Id should be in the range [0, Number of traders)");
            System.exit(1);
        }

        // get the stock Id
        int stockId = Integer.parseInt(in.next().substring(1));

        // error check
        if (stockId < 0 || stockId >= numStocks) {
            System.err.println("The stock Id should be in the range [0, Number of stocks)");
            System.exit(1);
        }

        // get the price
        int price = Integer.parseInt(in.next().substring(1));

        // error check
        if (price <= 0) {
            System.err.println("The price should be a positive integer");
            System.exit(1);
        }

        // get the quantity
        int qty = Integer.parseInt(in.next().substring(1));

        // error check
        if (qty <= 0) {
            System.err.println("The quantity should be a positive integer");
            System.exit(1);
        }

        // do comparisons
        if (intent.equals("SELL")) {
            return new SellOrder(curr_idx++, ts, traderId, stockId, price, qty);
        } else {
            return new BuyOrder(curr_idx++, ts, traderId, stockId, price, qty);
        }
    }

    public void printMedian(int s, long ts, int p) {
        // print median information
        System.out.println("Median match price of Stock " + s + " at time " + ts + " is $" + p);
    }

    public void printTraderInfo(int id, int numBought, int numSold, int netValue) {
        // Trader  <TRADER_ID> bought  <NUMBER_BOUGHT> and sold  <NUMBER_SOLD> for a net
        //transfer of   $ <NET_VALUE_TRADED><NEWLINE>

        System.out.println("Trader " + id + " bought " + numBought +
                " and sold " + numSold + " for a net transfer of $" + netValue);
    }

    public void getTraderInfo() {
        // trader info flag set
        if (config.traderInfo) {
            System.out.println("---Trader Info---");

            int i = 0; // initialize
            while (i < numTraders) {

                // get the trader
                Trader trd = traders.get(i);
                printTraderInfo(trd.getTraderId(), trd.getStocksBought(), trd.getStocksSold(), trd.getNetSales());

                i++; // increment
            }
        }

    }

}
