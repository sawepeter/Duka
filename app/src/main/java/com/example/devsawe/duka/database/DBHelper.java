package com.example.devsawe.duka.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import static com.example.devsawe.duka.Activities.GoodSales.product_name;

public class DBHelper extends SQLiteOpenHelper {

    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "Duka.db";

    public DBHelper(Context ctx) {
        super(ctx, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTables(db);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Database.GOODS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " +  Database.SUPPLIER_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Database.CUSTOMER_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Database.SALES_TABLE_NAME);

        onCreate(db);

    }

    private void createTables(SQLiteDatabase database) {
        //goods table
        String user_table_sql = "create table " + Database.GOODS_TABLE_NAME + "( " +
                Database.ROW_ID + " integer  primary key autoincrement," +
                Database.GOOD_ID + " INTEGER," +
                Database.GOOD_IMAGE  + " TEXT," +
                Database.GOOD_NAME  + " TEXT," +
                Database.GOOD_BARCODE  + " TEXT unique," +
                Database.GOOD_STOCK  + " DOUBLE," +
                Database.GOOD_MINIMUM_STOCK  + " DOUBLE," +
                Database.GOOD_PURCHASE_COST  + " DOUBLE," +
                Database.GOOD_SALE_PRICE + " DOUBLE)";

        String DefaultUser = "INSERT INTO " + Database.GOODS_TABLE_NAME + " ("
                + Database.ROW_ID + ", "
                + Database.GOOD_ID + ", "
                + Database.GOOD_IMAGE + ","
                + Database.GOOD_NAME + ", "
                + Database.GOOD_BARCODE + ", "
                + Database.GOOD_STOCK + ", "
                + Database.GOOD_MINIMUM_STOCK + ", "
                + Database.GOOD_PURCHASE_COST + ", "
                + Database.GOOD_SALE_PRICE + ") Values ('1', '1214', 'Image Path', 'KDF', '8500096','10','2','100','50')";

        //customer table
        String customer_table_sql = "create table " + Database.CUSTOMER_TABLE_NAME + "( " +
                Database.ROW_ID + " integer  primary key autoincrement," +
                Database.CUSTOMER_ID + " INTEGER," +
                Database.CUSTOMER_NAME  + " TEXT," +
                Database.CUSTOMER_PHONE  + " VARCHAR unique," +
                Database.CUSTOMER_LOCATION  + " TEXT," +
                Database.CUSTOMER_CREDIT  + " DOUBLE)";

        String DefaultCustomer = "INSERT INTO " + Database.CUSTOMER_TABLE_NAME + " ("
                + Database.ROW_ID + ", "
                + Database.CUSTOMER_ID + ", "
                + Database.CUSTOMER_NAME + ", "
                + Database.CUSTOMER_PHONE + ", "
                + Database.CUSTOMER_LOCATION + ", "
                + Database.CUSTOMER_CREDIT + ") Values ('1','2', 'DevSawe', '0725632415','juja','0.0')";

        //suppliers table
        String supplier_table_sql = "create table " + Database.SUPPLIER_TABLE_NAME + "( " +
                Database.ROW_ID + " integer  primary key autoincrement," +
                Database.SUPPLIER_ID + " INTEGER," +
                Database.SUPPLIER_NAME  + " TEXT," +
                Database.SUPPLIER_PRODUCT  + " TEXT," +
                Database.SUPPLIER_LOCATION  + " TEXT," +
                Database.SUPPLIER_EMAIL  + " TEXT," +
                Database.SUPPLIER_PHONE + " VARCHAR unique)";

        String DefaultSupplier = "INSERT INTO " + Database.SUPPLIER_TABLE_NAME + " ("
                + Database.ROW_ID + ", "
                + Database.SUPPLIER_ID + ", "
                + Database.SUPPLIER_NAME + ", "
                + Database.SUPPLIER_PRODUCT + ", "
                + Database.SUPPLIER_LOCATION + ", "
                + Database.SUPPLIER_EMAIL + ", "
                + Database.SUPPLIER_PHONE + ") Values ('1', '1214', 'Kimbo', 'Cooking oil','Clay works','kimbo@gmail.com','0725632415')";

        //sales  table
        String sale_table_sql = "create table " + Database.SALES_TABLE_NAME + "( " +
                Database.ROW_ID + " integer  primary key autoincrement," +
                Database.SALE_ID + " INTEGER," +
                Database.SALES_DATE  + " DATE," +
                Database.SALES_CUSTOMER  + " TEXT," +
                Database.SALE_TRANSACTION_ID  + " VARCHAR unique," +
                Database.SALE_PRODUCT  + " TEXT," +
                Database.SALE_QUANTITY  + " VARCHAR," +
                Database.SELLING_PRICE  + " VARCHAR," +
                Database.SALE_SUB_TOTAL  + " VARCHAR," +
                Database.SALE_DISCOUNT  + " VARCHAR," +
                Database.SALE_GRAND_TOTAL  + " DOUBLE," +
                Database.SALE_AMOUNT_RECEIVED  + " VARCHAR," +
                Database.SALE_BALANCE + " VARCHAR)";

        String DefaultSales = "INSERT INTO " + Database.SALES_TABLE_NAME + " ("
                + Database.ROW_ID + ", "
                + Database.SALE_ID + ", "
                + Database.SALES_DATE + ", "
                + Database.SALES_CUSTOMER + ", "
                + Database.SALE_TRANSACTION_ID + ", "
                + Database.SALE_PRODUCT + ", "
                + Database.SALE_QUANTITY + ", "
                + Database.SELLING_PRICE + ", "
                + Database.SALE_SUB_TOTAL + ", "
                + Database.SALE_DISCOUNT + ", "
                + Database.SALE_GRAND_TOTAL + ", "
                + Database.SALE_AMOUNT_RECEIVED + ", "
                + Database.SALE_BALANCE + ")  Values ('1', '1214', '18-02-2019', 'Devsawe','VFR7686','Kimbo','10Kgs','Ksh: 500','Ksh: 5000','Ksh: 100','Ksh : 5', 'Ksh: 4900','Ksh: 0.00')";

        try {
            database.execSQL(user_table_sql);
            database.execSQL(supplier_table_sql);
            database.execSQL(customer_table_sql);
            database.execSQL(sale_table_sql);

            //loading the default information
            database.execSQL(DefaultUser);
            database.execSQL(DefaultSupplier);
            database.execSQL(DefaultCustomer);
            //database.execSQL(DefaultSales);

        }catch (Exception e){
            Log.d("Duka.db", "Error in DBHelper.onCreate() : " + e.getMessage());
        }

    }

    public long AddSale(String saledate, String salecustomer, String transactionid, String product, String salequantity, String sellingprice, String subtotal, String discount, String grandtotal, String amount, String balance){

        SQLiteDatabase db = this.getReadableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Database.SALES_DATE,saledate);
        contentValues.put(Database.SALES_CUSTOMER,salecustomer);
        contentValues.put(Database.SALE_TRANSACTION_ID,transactionid);
        contentValues.put(Database.SALE_PRODUCT,product);
        contentValues.put(Database.SALE_QUANTITY,salequantity);
        contentValues.put(Database.SELLING_PRICE,sellingprice);
        contentValues.put(Database.SALE_SUB_TOTAL,subtotal);
        contentValues.put(Database.SALE_DISCOUNT,discount);
        contentValues.put(Database.SALE_GRAND_TOTAL,grandtotal);
        contentValues.put(Database.SALE_AMOUNT_RECEIVED,amount);
        contentValues.put(Database.SALE_BALANCE,balance);
        return db.insert(Database.SALES_TABLE_NAME,null,contentValues);

    }


    public long AddGood(String imagepath, String goodname,String barcodegood,String goodavailablestock,String goodminstock,String goodpurchasesale,String goodsaleprice){

    SQLiteDatabase db = this.getReadableDatabase();

    ContentValues contentValues = new ContentValues();
    contentValues.put(Database.GOOD_IMAGE,imagepath);
    contentValues.put(Database.GOOD_NAME,goodname);
    contentValues.put(Database.GOOD_BARCODE,barcodegood);
    contentValues.put(Database.GOOD_STOCK,goodavailablestock);
    contentValues.put(Database.GOOD_MINIMUM_STOCK,goodminstock);
    contentValues.put(Database.GOOD_PURCHASE_COST,goodpurchasesale);
    contentValues.put(Database.GOOD_SALE_PRICE,goodsaleprice);
    return db.insert(Database.GOODS_TABLE_NAME,null,contentValues);

    }

    public void UpdateGoodStock(String goodavailablestock,String goodname) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values =new ContentValues();
        values.put(Database.GOOD_STOCK,goodavailablestock);


        long row =db.update(Database.GOODS_TABLE_NAME,values,Database.GOOD_NAME+ "=?",new String[]{goodname});

        if (row>0){


        }



    }


    public long AddSupplier(String suppliername,String supplierproduct, String supplierlocation, String supplieremail, String supplierphone){

        SQLiteDatabase db = this.getReadableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Database.SUPPLIER_NAME,suppliername);
        contentValues.put(Database.SUPPLIER_PRODUCT,supplierproduct);
        contentValues.put(Database.SUPPLIER_LOCATION,supplierlocation);
        contentValues.put(Database.SUPPLIER_EMAIL,supplieremail);
        contentValues.put(Database.SUPPLIER_PHONE,supplierphone);
        return db.insert(Database.SUPPLIER_TABLE_NAME,null,contentValues);
    }

    public long AddCustomer(String customername,String customerphone,String customerlocation,String creditamount){

        SQLiteDatabase db = this.getReadableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Database.CUSTOMER_NAME,customername);
        contentValues.put(Database.CUSTOMER_PHONE,customerphone);
        contentValues.put(Database.CUSTOMER_LOCATION,customerlocation);
        contentValues.put(Database.CUSTOMER_CREDIT,creditamount);
        return db.insert(Database.CUSTOMER_TABLE_NAME,null,contentValues);
    }

    //this function gets the customer names and displays them to spinner widget
    public List<String> getCustomer(){
        List<String> customer_names = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT customername,customerlocation FROM " + Database.CUSTOMER_TABLE_NAME, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                customer_names.add(cursor.getString(0));
               // customer_names.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }
        // returning customer names
        return customer_names;
    }

    public List<String> getsaleCustomer(){
        List<String> name_of_salecustomer = new ArrayList<String>();


        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT salecustomer FROM " +Database.SALES_TABLE_NAME,null);

        if (cursor.moveToFirst()){
            do {
                name_of_salecustomer.add(cursor.getString(0));
            }while (cursor.moveToNext());
        }

        return name_of_salecustomer;
    }

    public ArrayList<String> queryXData(){
        ArrayList<String> xNewData = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + Database.SALE_PRODUCT + " FROM " + Database.SALES_TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            xNewData.add(cursor.getString(cursor.getColumnIndex("saleproduct")));
        }
        cursor.close();
        return xNewData;
    }

    public ArrayList<String> CustomersXdata(){
        ArrayList<String> xNewData = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + Database.SALES_CUSTOMER + " FROM " + Database.SALES_TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            xNewData.add(cursor.getString(cursor.getColumnIndex("salecustomer")));
        }
        cursor.close();
        return xNewData;

    }
    //y axis code

    public ArrayList<Float> queryYData(){
        ArrayList<Float> yNewData = new ArrayList<Float>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + Database.SALE_GRAND_TOTAL + " FROM " + Database.SALES_TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            yNewData.add(cursor.getFloat(cursor.getColumnIndex("salegrandtotal")));
        }
        cursor.close();
        return yNewData;
    }

    public List<String> getSaleAmount(){
        List<String> sale_customer_amount = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT salegrandtotal FROM "+Database.SALES_TABLE_NAME,null);

        if (cursor.moveToFirst()){
            do {
                sale_customer_amount.add(cursor.getString(0));
            }while (cursor.moveToNext());
        }

        return sale_customer_amount;
    }

    //this function gets the customer names and displays them to spinner widget
    public List<String> getProducts(){
        List<String> product_names = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT goodname FROM " + Database.GOODS_TABLE_NAME, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                product_names.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        // returning customer names
        return product_names;
    }
    public String ProdPrice(String product_name){
        SQLiteDatabase db = this.getReadableDatabase();
        String sellprice_Query = "SELECT goodsaleprice FROM table_goods WHERE goodname = '" + product_name + "'";

        String Price=null;
        Cursor cursor = db.rawQuery(sellprice_Query, null);
        int columnprice = cursor.getColumnIndex("goodsaleprice");
        if (cursor.moveToFirst()) {
            do {
                String selling_price = cursor.getString(columnprice);
              Price=selling_price;
            } while (cursor.moveToNext());
        }
      return Price;
    }

    public String HighestBuyer(){
        String top_customer=null;
        SQLiteDatabase db = this.getReadableDatabase();
        String alltimebuyer = "SELECT salecustomer, max(salegrandtotal) as maxgrand FROM (SELECT salecustomer, sum(salegrandtotal) as salegrandtotal FROM table_sales group by salecustomer)";

        Cursor cursor = db.rawQuery(alltimebuyer, null);
        if (cursor.moveToFirst())
        {
            do {
                int columncustomer = cursor.getColumnIndex("salecustomer");
                top_customer = cursor.getString(columncustomer);
            }while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();
       return top_customer;
    }

    public String LowestStock(){
        String stock_minimum=null;
        //available_stock=== the string  that represent the current stock in the database
        //product_name == the string that displays the good that was selected on the spinner
        SQLiteDatabase db = this.getReadableDatabase();
        String minimumstock = "SELECT goodminimumstock FROM table_goods WHERE goodname = '" + product_name + "'";//the product name has an issue

        Cursor cursor = db.rawQuery(minimumstock,null);
        if (cursor.moveToFirst()) {
            do {
                int columnminimumstock = cursor.getColumnIndex("goodminimumstock");
                stock_minimum = cursor.getString(columnminimumstock);
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        return stock_minimum;
    }

    //code segment for the most sold good
    public String HighestSoldProduct(){
        String top_product=null;
        SQLiteDatabase db = this.getReadableDatabase();
        String alltimeproduct = "SELECT saleproduct, max(salegrandtotal) as maxgrand FROM (SELECT saleproduct, sum(salegrandtotal) as salegrandtotal FROM table_sales group by saleproduct)";

        Cursor cursor = db.rawQuery(alltimeproduct, null);
        if (cursor.moveToFirst())
        {
            do {
                int columnproduct = cursor.getColumnIndex("saleproduct");
                top_product = cursor.getString(columnproduct);
            }while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();
        return top_product;
    }
    //code segment for retrieving the sum of all transactions
    public String TotalSales(){
        String sales_total = null;
        SQLiteDatabase db = this.getReadableDatabase();
        String sum_of_sales = "SELECT SUM(salegrandtotal) as grandtotal FROM table_sales";
        Cursor cursor = db.rawQuery(sum_of_sales,null);
        if (cursor.moveToFirst()){
            do {
                int columngrandtotal = cursor.getColumnIndex("grandtotal");
                    sales_total = cursor.getString(columngrandtotal);
            }   while (cursor.moveToNext());
        }
        return sales_total;
    }

    public String GetQuantity(String quantity_change){
        SQLiteDatabase db = this.getReadableDatabase();
        String Quantity_Query = "SELECT goodstock FROM table_goods WHERE goodname = '" + quantity_change + "'";
        String CurrentQuantity = null;
        Cursor cursor = db.rawQuery(Quantity_Query, null);
        int columnQuanity  = cursor.getColumnIndex("goodstock");
        if (cursor.moveToFirst())
        {
            do {
                String InStock = cursor.getString(columnQuanity);
                CurrentQuantity = InStock;
            }while (cursor.moveToNext());
        }
        return CurrentQuantity;
    }
}
