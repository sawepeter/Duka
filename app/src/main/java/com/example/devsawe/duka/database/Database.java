package com.example.devsawe.duka.database;

public class Database {

    //constants
    public static final String ROW_ID = "_id";
    public static final String GOOD_ID = "goodid";
    public static final String SUPPLIER_ID = "supplierid";
    public static final String CUSTOMER_ID = "custommerid";
    public static final String SALE_ID= "saleid";
    public static final String CART_ITEM_ID = "cartitemid";

    //table goods
    public static final String GOODS_TABLE_NAME = "table_goods";

    public static final String GOOD_IMAGE = "image";
    public static final String GOOD_NAME = "goodname";
    public static final String GOOD_BARCODE = "goodbarcode";
    public static final String GOOD_STOCK = "goodstock";
    public static final String GOOD_MINIMUM_STOCK = "goodminimumstock";
    public static final String GOOD_PURCHASE_COST = "goodpurchasecost";
    public static final String GOOD_SALE_PRICE = "goodsaleprice";

    //table suppliers
    public static final String SUPPLIER_TABLE_NAME = "table_suppliers";

    public static final String SUPPLIER_NAME = "supplier_name";
    public static final String SUPPLIER_PRODUCT = "supplier_product";
    public static final String SUPPLIER_LOCATION = "supplier_location";
    public static final String SUPPLIER_EMAIL = "supplier_email";
    public static final String SUPPLIER_PHONE = "supplier_phone";

    //table customers
    public static final String CUSTOMER_TABLE_NAME = "table_customers";

    public static final String CUSTOMER_NAME = "customername";
    public static final String CUSTOMER_PHONE = "customerphone";
    public static final String CUSTOMER_LOCATION = "customerlocation";
    public static final String CUSTOMER_CREDIT = "customer_credit";


    //table sales
    public static final String SALES_TABLE_NAME = "table_sales";

    public static final String SALES_DATE = "salesdate";
    public static final String SALES_CUSTOMER = "salecustomer";
    public static final String SALE_TRANSACTION_ID = "saletransactionid";
    public static final String SALE_PRODUCT = "saleproduct";
    public static final String SALE_QUANTITY = "salequantity";
    public static final String SELLING_PRICE = "sellingprice";
    public static final String SALE_SUB_TOTAL = "salesubtotal";
    public static final String SALE_DISCOUNT = "salediscount";
    public static final String SALE_GRAND_TOTAL = "salegrandtotal";
    public static final String SALE_AMOUNT_RECEIVED = "saleamountreceived";
    public static final String SALE_BALANCE = "salebalance";
    public static final String SALE_PAYMENT_METHOD = "salepaymentmethod";

    //table goods sales
    public static final  String CART_TABLE_NAME = "table_cart";

    public static final String CART_ITEM_DATE = "cartitemdate";
    public static final String CART_ITEM_NAME = "itemname";
    public static final String CART_ITEM_QUANTITY = "cartitemquantity";
    public static final String CART_ITEM_TOTAL =  "cartitemtotal";
    




}
