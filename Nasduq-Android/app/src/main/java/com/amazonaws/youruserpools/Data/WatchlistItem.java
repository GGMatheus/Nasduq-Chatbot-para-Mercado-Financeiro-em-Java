package com.amazonaws.youruserpools.Data;

/**
 * Created by Leandro on 3/19/2017.
 */

public class WatchlistItem {

    public int itemType;
    private String ticker;
    private String companyName;
    private double price;
    private double change;
    private String currency;
    private double abs_change;

    public WatchlistItem(String ticker, String companyName, double price, double change, String currency, double abs_change, int itemType) {
        this.ticker = ticker;
        this.companyName = companyName;
        this.price = price;
        this.change = change;
        this.currency = currency;
        this.abs_change = abs_change;
        this.itemType = itemType;
    }

    public String getTicker() {
        return ticker;
    }

    public String getCompanyName() {
        return companyName;
    }

    public double getPrice() {
        return price;
    }

    public double getChange() {
        return change;
    }

    public String getCurrency() {
        return currency;
    }

    public double getAbs_change() {
        return abs_change;
    }
}
