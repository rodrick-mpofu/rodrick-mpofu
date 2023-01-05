public abstract class Order implements Comparable<Order> {

    // keep track of the id
    private final long id;
    // keep track of the timestamp
    private final long timestamp;
    // keep track of the traderId
    private final int traderId;
    // keep track of the stock Id
    private final int stockId;
    // keep track of the price
    private final int price;
    // keep track of the quantity
    private int quantity;

    public Order(long id, long ts, int tId, int sId, int p, int q) {
        // unique number to describe an order
        this.id = id;
        timestamp = ts;
        traderId = tId;
        stockId = sId;
        price = p;
        quantity = q;
    }

    public long getId() {
        return id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public int getTraderId() {
        return traderId;
    }

    public int getStockId() {
        return stockId;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public abstract boolean isSell();

    @Override
    public abstract int compareTo(Order o);

    /**
     * reduce the quantity of shares in this order
     *
     * @param shares quantity to reduce by
     */
    public void removeShares(int shares) {
        quantity -= shares;
    }

}
