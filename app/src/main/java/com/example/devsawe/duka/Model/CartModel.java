package com.example.devsawe.duka.Model;

public class CartModel {

    private String cartdate;
    private String cartname;
    private String cartquantity;
    private String carttotal;

    public CartModel() {
    }

    public CartModel(String cartdate, String cartname, String cartquantity, String carttotal) {
        this.cartdate = cartdate;
        this.cartname = cartname;
        this.cartquantity = cartquantity;
        this.carttotal = carttotal;
    }

    public String getCartdate() {
        return cartdate;
    }

    public void setCartdate(String cartdate) {
        this.cartdate = cartdate;
    }

    public String getCartname() {
        return cartname;
    }

    public void setCartname(String cartname) {
        this.cartname = cartname;
    }

    public String getCartquantity() {
        return cartquantity;
    }

    public void setCartquantity(String cartquantity) {
        this.cartquantity = cartquantity;
    }

    public String getCarttotal() {
        return carttotal;
    }

    public void setCarttotal(String carttotal) {
        this.carttotal = carttotal;
    }
}





