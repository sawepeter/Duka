package com.example.devsawe.duka.Model;

public class TransactionModel {
    private String transactiondate;
    private String transactionitems;
    private String transactiontotal;
    private String transactionBptotal;
    private String transactionSellingprice;
    private String transactioncashin;
    private String transactionbalance;

    public TransactionModel() {
    }

    public TransactionModel(String transactiondate, String transactionitems, String transactiontotal, String transactionBptotal, String transactionSellingprice, String transactioncashin, String transactionbalance) {
        this.transactiondate = transactiondate;
        this.transactionitems = transactionitems;
        this.transactiontotal = transactiontotal;
        this.transactionBptotal = transactionBptotal;
        this.transactionSellingprice = transactionSellingprice;
        this.transactioncashin = transactioncashin;
        this.transactionbalance = transactionbalance;
    }

    public String getTransactiondate() {
        return transactiondate;
    }

    public void setTransactiondate(String transactiondate) {
        this.transactiondate = transactiondate;
    }

    public String getTransactionitems() {
        return transactionitems;
    }

    public void setTransactionitems(String transactionitems) {
        this.transactionitems = transactionitems;
    }

    public String getTransactiontotal() {
        return transactiontotal;
    }

    public void setTransactiontotal(String transactiontotal) {
        this.transactiontotal = transactiontotal;
    }

    public String getTransactionBptotal() {
        return transactionBptotal;
    }

    public void setTransactionBptotal(String transactionBptotal) {
        this.transactionBptotal = transactionBptotal;
    }

    public String getTransactionSellingprice() {
        return transactionSellingprice;
    }

    public void setTransactionSellingprice(String transactionSellingprice) {
        this.transactionSellingprice = transactionSellingprice;
    }

    public String getTransactioncashin() {
        return transactioncashin;
    }

    public void setTransactioncashin(String transactioncashin) {
        this.transactioncashin = transactioncashin;
    }

    public String getTransactionbalance() {
        return transactionbalance;
    }

    public void setTransactionbalance(String transactionbalance) {
        this.transactionbalance = transactionbalance;
    }
}
