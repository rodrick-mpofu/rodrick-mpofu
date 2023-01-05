public class Trader {
    // store traderId
    private final int traderId;
    // store number of stocks bought
    private int stocksBought;
    // store number of stocks sold
    private int stocksSold;
    // store net sales
    private int netSales;

    // get traderId
    public int getTraderId() {
        return traderId;
    }

    // get number of stocks bought
    public int getStocksBought() {
        return stocksBought;
    }

    // get number of stocks sold
    public int getStocksSold() {
        return stocksSold;
    }

    // get net sales
    public int getNetSales() {
        return netSales;
    }


    public Trader(int traderId) {
        this.traderId = traderId;
        stocksBought = 0;
        stocksSold = 0;
        netSales = 0;
    }

    public void buy(int shares, int price) {

        // update stocks bought
        stocksBought = stocksBought + shares;
        // update netSales
        netSales = netSales - (price * shares);
    }

    public void sell(int shares, int price) {

        // update stocksSold
        stocksSold = stocksSold + shares;

        // update netSales
        netSales = netSales + (price * shares);
    }
}
