import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

public class Stock {

    // two Priority Queues to store the buy orders and sell orders
    private PriorityQueue<Order> buyOrders;
    private PriorityQueue<Order> sellOrders;
    private Config config;

    // keep track whether a transaction has occurred or not
    private boolean hasTransOccurred;

    // keep track of a count
    public static int count;
    //  min PQ (Priority Queue)
    PriorityQueue<Integer> topHalf = new PriorityQueue<>();


    // max PQ (Priority Queue)
    PriorityQueue<Integer> bottomHalf =
            new PriorityQueue<>(Collections.reverseOrder());

    // get the median
    public int getMedian() {
        return median;
    }

    // track our median
    private int median;

    // constructor
    public Stock(Config c) {
        config = c;
        sellOrders = new PriorityQueue<>();
        buyOrders = new PriorityQueue<>();

    }

    public void addOrder(Order o) {
        // adding an order
        if (o.isSell()) {
            sellOrders.add(o);
        } else {
            buyOrders.add(o);
        }
    }

    public void performMatches(List<Trader> traders) {

        // checking if there are orders that can be matched and perform those transactions
        while (canMatch()) {
            // keep track of the top of PQs
            Order buy = buyOrders.peek();
            Order sell = sellOrders.peek();

            // keep track of the price
            int price;


            if (buy.getId() < sell.getId()) {
                price = buy.getPrice();

            } else {
                price = sell.getPrice();
            }

            // keep track of the shares
            int shares = Math.min(buy.getQuantity(), sell.getQuantity());

            // perform the transaction

            buy.removeShares(shares);
            sell.removeShares(shares);

            // if buy quantity is zero remove from the PQs
            if (buy.getQuantity() == 0) {
                buyOrders.remove();
            }

            // if sell quantity is zero remove from the PQs
            if (sell.getQuantity() == 0) {
                sellOrders.remove();
            }


            // if verbose flag is set print out the transtactions
            if (config.verbose) {
                System.out.println("Trader " + buy.getTraderId() + " purchased " +
                        shares + " shares of Stock " + buy.getStockId() +
                        " from Trader " + sell.getTraderId() + " for $" + price + "/share");
            }

            count++; // increment the count

            trackMedian(price); // keep track of the median price

            hasTransOccurred = true; // transactions has occurred

            // update trader information
            traders.get(buy.getTraderId()).buy(shares, price);
            traders.get(sell.getTraderId()).sell(shares, price);

        }

    }

    public boolean canMatch() {
        // check if two orders can be matched at the head of the PQs
        if (buyOrders.isEmpty() || sellOrders.isEmpty()) {
            // we cannot match
            return false;
        }
        return sellOrders.peek().getPrice() <= buyOrders.peek().getPrice();
    }


    public void summaryOutput() {
        // print out the summray output
        System.out.println("---End of Day---");
        System.out.println("Orders Processed: " + count);
    }

    // function to keep track if transaction has occurred or not
    public boolean isHasTransOccurred() {

        return hasTransOccurred;
    }

    public void trackMedian(int price) {

        // process i and update the median

        if (topHalf.isEmpty() && bottomHalf.isEmpty()) {
            // first item
            topHalf.add(price);
            median = price;
        } else {
            // decide if wwe are inserting in the top or bottom half
            if (price < median) {
                bottomHalf.add(price);
            } else {
                topHalf.add(price);
            }

            // check for balanced sizes
            if (bottomHalf.size() - topHalf.size() == 2) {
                // shift an item from the bottom to the top
                topHalf.add(bottomHalf.remove());
            } else if (topHalf.size() - bottomHalf.size() == 2) {
                // shift an item from the top to the bottom
                bottomHalf.add(topHalf.remove());
            }
            // update the median value
            // size differ by at most 1
            if (bottomHalf.size() > topHalf.size()) {
                median = bottomHalf.element();
            } else if (topHalf.size() > bottomHalf.size()) {
                median = topHalf.element();
            } else {
                // integer division
                median = (topHalf.element() + bottomHalf.element()) / 2;
            }
        }
    }

}

