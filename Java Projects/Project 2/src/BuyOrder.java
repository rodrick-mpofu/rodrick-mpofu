public class BuyOrder extends Order {
    public BuyOrder(long l, long ts, int traderId, int stockId, int price, int qty) {
        super(l, ts, traderId, stockId, price, qty);
    }
    // This is shows that the order is a buy
    // if it not a sell
    @Override
    public boolean isSell() {
        return false;
    }

    @Override
    public int compareTo(Order o) {
        // this
        // o --- other object
        // return a negative int when "this" is higher priority than o

        // break ties for same price
        if(this.getPrice() == o.getPrice()) {
            // the order that came first gets priority
            // smaller id means it came earlier
            // same for buy order
            return (int) (this.getId() - o.getId());
        }
        // price comparison will have to be different for a buy order
        return o.getPrice() - this.getPrice();
    }
}
