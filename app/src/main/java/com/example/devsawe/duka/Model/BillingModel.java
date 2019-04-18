package com.example.devsawe.duka.Model;

import java.io.Serializable;

public class BillingModel implements Serializable {
    private String billingdate;
    private String billingitems;
    private String billingtotal;
    private String billingBptotal;
    private String billingSellingprice;
    private String billingcashin;
    private String billingbalance;

    public BillingModel() {
    }

    public BillingModel(String billingdate, String billingitems, String billingtotal, String billingBptotal, String billingSellingprice, String billingcashin, String billingbalance) {
        this.billingdate = billingdate;
        this.billingitems = billingitems;
        this.billingtotal = billingtotal;
        this.billingBptotal = billingBptotal;
        this.billingSellingprice = billingSellingprice;
        this.billingcashin = billingcashin;
        this.billingbalance = billingbalance;
    }

    public String getBillingdate() {
        return billingdate;
    }

    public void setBillingdate(String billingdate) {
        this.billingdate = billingdate;
    }

    public String getBillingitems() {
        return billingitems;
    }

    public void setBillingitems(String billingitems) {
        this.billingitems = billingitems;
    }

    public String getBillingtotal() {
        return billingtotal;
    }

    public void setBillingtotal(String billingtotal) {
        this.billingtotal = billingtotal;
    }

    public String getBillingBptotal() {
        return billingBptotal;
    }

    public void setBillingBptotal(String billingBptotal) {
        this.billingBptotal = billingBptotal;
    }

    public String getBillingSellingprice() {
        return billingSellingprice;
    }

    public void setBillingSellingprice(String billingSellingprice) {
        this.billingSellingprice = billingSellingprice;
    }

    public String getBillingcashin() {
        return billingcashin;
    }

    public void setBillingcashin(String billingcashin) {
        this.billingcashin = billingcashin;
    }

    public String getBillingbalance() {
        return billingbalance;
    }

    public void setBillingbalance(String billingbalance) {
        this.billingbalance = billingbalance;
    }
}
