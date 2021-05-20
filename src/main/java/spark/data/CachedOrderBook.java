package spark.data;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.NavigableMap;

public class CachedOrderBook {

    private Map<String, NavigableMap<BigDecimal, BigDecimal>> oBook;

    private static final String BIDS  = "BIDS";
    private static final String ASKS  = "ASKS";

    public CachedOrderBook() {
        this.oBook = new HashMap<>();
    }

    public NavigableMap<BigDecimal, BigDecimal> getAsks() {
        return oBook.get(ASKS);
    }

    public NavigableMap<BigDecimal, BigDecimal> getBids() {
        return oBook.get(BIDS);
    }

    /**
     * @return the best ask in the order book
     */
    public Map.Entry<BigDecimal, BigDecimal> getBestAsk() {
        return getAsks().lastEntry();
    }

    /**
     * @return the best bid in the order book
     */
    public Map.Entry<BigDecimal, BigDecimal> getBestBid() {
        return getBids().firstEntry();
    }

    /**
     * @return a depth cache, containing two keys (ASKs and BIDs), and for each, an ordered list of book entries.
     */
    public Map<String, NavigableMap<BigDecimal, BigDecimal>> getDepthCache() {
        return oBook;
    }

    public void put(String string, NavigableMap<BigDecimal, BigDecimal> map) {
        oBook.put(string, map);
    }

    /**
     * Prints the cached order book / depth of a symbol as well as the best ask and bid price in the book.
     */
    public void printDepthCache() {
        System.out.println(oBook);
        System.out.println("ASKS:");
        getAsks().entrySet().forEach(entry -> System.out.println(toDepthCacheEntryString(entry)));
        System.out.println("BIDS:");
        getBids().entrySet().forEach(entry -> System.out.println(toDepthCacheEntryString(entry)));
        System.out.println("BEST ASK: " + toDepthCacheEntryString(getBestAsk()));
        System.out.println("BEST BID: " + toDepthCacheEntryString(getBestBid()));
    }

    /**
     * Pretty prints an order book entry in the format "price / quantity".
     */
    private static String toDepthCacheEntryString(Map.Entry<BigDecimal, BigDecimal> depthCacheEntry) {
        return depthCacheEntry.getKey().toPlainString() + " / " + depthCacheEntry.getValue();
    }

}
