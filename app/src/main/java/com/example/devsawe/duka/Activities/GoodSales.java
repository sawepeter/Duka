package com.example.devsawe.duka.Activities;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.devsawe.duka.R;
import com.example.devsawe.duka.database.DBHelper;
import com.example.devsawe.duka.database.Database;

import java.util.List;
import java.util.Random;

public class GoodSales extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Button make_sale,new_customer;
    Double inputQuantity,current_stock;
    Spinner spinner_customers,spinner_products;
    TextView txt_transaction_id,txt_selling_price,subtotal,cash_balance,transaction_total,txt_date;
    DBHelper dbhelper;
    EditText edt_quantity,discount_received,cash_received;
    SQLiteDatabase db;
    Boolean success = true;
    RadioButton radiompesa,radiocash;
    String s_spinner_customers,s_spinner_products,s_txt_transaction_id,s_cash_received,
            s_edt_quantity,s_discount_received,s_txt_selling_price,s_subtotal,s_cash_balance,s_transaction_total,s_txt_date;
    String available_stock,minimum_stock;
    public static String Stock_update,product_name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_good_sales);
        showNotification();


        new_customer = findViewById(R.id.new_customer);
        new_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GoodSales.this,NewCustomers.class);
                startActivity(intent);
            }
        });

        txt_date = findViewById(R.id.txt_date);
        discount_received = findViewById(R.id.discount_received);
        discount_received.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (discount_received.length()>0){
                    discount_received.hasFocus();
                    Discount();
                }if (discount_received.length() == 0){
                    discount_received.setText("0.0");
                }

            }
        });

        edt_quantity = findViewById(R.id.edt_quantity);
        edt_quantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
               /* if (edt_quantity.length()>0){
                    edt_quantity.hasFocus();
                    calc();
                }if (edt_quantity.length() == 0){
                    edt_quantity.setText("0.0");
                }*/
               if (edt_quantity.length() < 0){
                   Toast.makeText(GoodSales.this, "Input some quantity greater than 0.0", Toast.LENGTH_SHORT).show();

               }else if (edt_quantity.length() >0){
                   edt_quantity.hasFocus();
                   calc();

               }else {
                   Toast.makeText(GoodSales.this, "Invalid Quantity !!!!", Toast.LENGTH_SHORT).show();
               }

            }
        });

        cash_received = findViewById(R.id.cash_received);
        cash_received.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                if (cash_received.length()>0){
                    cash_received.hasFocus();
                    Balance();
                }if (cash_received.length() == 0){
                    cash_received.setText("0.0");
                }


            }
        });



        spinner_customers = findViewById(R.id.spinner_customers);
        spinner_customers.setOnItemSelectedListener(this);
        spinner_products = findViewById(R.id.spinner_products);
        spinner_products.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                product_name = parent.getItemAtPosition(position).toString();
                String quantity_change = parent.getItemAtPosition(position).toString();

                dbhelper = new DBHelper(getApplicationContext());
                SQLiteDatabase db = dbhelper.getReadableDatabase();

                txt_selling_price = findViewById(R.id.txt_selling_price);
                txt_selling_price.setText(dbhelper.ProdPrice(product_name));

                //this is the string that will be used to do the necessary calculations
                String sellingprice = dbhelper.ProdPrice(product_name);

                //operations to update database stock
                available_stock = dbhelper.GetQuantity(quantity_change);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        loadSpinnerData();
        TransactionID();
        loadProducts();




        //button that makes the sales,stores the sales in a database,deducts the sold out quantity and updates stock
        make_sale = findViewById(R.id.make_sale);
        make_sale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    try{

                        dbhelper = new DBHelper(getApplicationContext());
                        db= dbhelper.getReadableDatabase();

                        s_spinner_customers = spinner_customers.getSelectedItem().toString();
                        s_spinner_products = spinner_products.getSelectedItem().toString();
                        s_txt_transaction_id = txt_transaction_id.getText().toString();
                        s_cash_received = cash_received.getText().toString();
                        s_edt_quantity = edt_quantity.getText().toString();
                        s_discount_received = discount_received.getText().toString();
                        s_txt_selling_price = txt_selling_price.getText().toString();
                        s_subtotal = subtotal.getText().toString();
                        s_cash_balance = cash_balance.getText().toString();
                        s_transaction_total = transaction_total.getText().toString();
                        s_txt_date = txt_date.getText().toString();
                        deductions();



                        if (s_edt_quantity==available_stock){
                            Toast.makeText(GoodSales.this, "We dont sell quantitiy 2 here!!!", Toast.LENGTH_SHORT).show();
                        }else{
                            dbhelper.AddSale(s_txt_date, s_spinner_customers, s_txt_transaction_id,
                                    s_spinner_products, s_edt_quantity, s_txt_selling_price, s_subtotal,
                                    s_discount_received, s_transaction_total, s_cash_received,s_cash_balance);
                            if (inputQuantity>current_stock && current_stock<=0 && inputQuantity == 0) {
                                Toast.makeText(getApplicationContext(), "Sorry quantity demanded is invalid !!!", Toast.LENGTH_SHORT).show();
                            }else {
                                showCustomToast();
                                UpdateStock();
                                Notifier();
                                finish();
                            }
                        }


                    }catch (Exception ex){
                        success=false;
                    }



            }
        });

    }

    private void showCustomToast() {
        View view = getLayoutInflater().inflate(R.layout.custom_toast,null);
        ImageView image = view.findViewById(R.id.image);
        image.setImageResource(R.drawable.ic_done_light_green_a100_24dp);
        TextView txt_toast = view.findViewById(R.id.txt_toast);
        txt_toast.setText("Sale Captured successfully!!!");

        Toast mToast = new Toast(getApplicationContext());
        mToast.setView(view);
        mToast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER, 0, 0);
        mToast.setDuration(Toast.LENGTH_LONG);
        mToast.show();
    }

    public void UpdateStock(){
        dbhelper = new DBHelper(getApplicationContext());
        db= dbhelper.getReadableDatabase();

       dbhelper.UpdateGoodStock(Stock_update,product_name);
       if (success){
           Toast.makeText(getApplicationContext(), "Database updated successfully", Toast.LENGTH_SHORT).show();
       }else {
           Toast.makeText(getApplicationContext(), "Database update failed !!!", Toast.LENGTH_SHORT).show();
       }
    }

    public void deductions(){
         current_stock = Double.valueOf(available_stock);
         inputQuantity = Double.valueOf(s_edt_quantity);

            Double resultQuantity = current_stock - inputQuantity;

            Stock_update = String.valueOf(resultQuantity);

    }

    public void calc(){
        txt_selling_price = findViewById(R.id.txt_selling_price);
       String selling_price = txt_selling_price.getText().toString();
       Double s_price = Double.parseDouble(selling_price);

        edt_quantity = findViewById(R.id.edt_quantity);
        String product_quantity = edt_quantity.getText().toString();
        Double s_quantity = Double.parseDouble(product_quantity);

        Double sub_total = (s_price * s_quantity);
        String totalprice = Double.toString(sub_total);


            subtotal = findViewById(R.id.subtotal);
            subtotal.setText(totalprice);

    }

    public void Discount(){
        subtotal = findViewById(R.id.subtotal);
        String selling_price_total = subtotal.getText().toString();
        Double totalsub = Double.parseDouble(selling_price_total);

        discount_received = findViewById(R.id.discount_received);
        String discountamount = discount_received.getText().toString();
        Double amount = Double.parseDouble(discountamount);

        Double grandtotal = (totalsub) - (amount);
        String total = Double.toString(grandtotal);

        transaction_total = findViewById(R.id.transaction_total);
        transaction_total.setText(total);

    }

    public void Balance(){
        transaction_total = findViewById(R.id.transaction_total);
        String totaltransaction = transaction_total.getText().toString();
        Double transaction = Double.parseDouble(totaltransaction);

        cash_received = findViewById(R.id.cash_received);
        String amountpayed = cash_received.getText().toString();
        Double cash = Double.parseDouble(amountpayed);

        Double balance = (cash) - (transaction);
        String balancetotal = Double.toString(balance);

        cash_balance = findViewById(R.id.cash_balance);
        cash_balance.setText(balancetotal);
    }

    public void TransactionID() {
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        int length = 8;


        Random random = new Random();
        StringBuilder builder = new StringBuilder(length);
        alphabet.charAt(random.nextInt(alphabet.length()));

        for (int i = 0; i < length; i++) {
            builder.append(alphabet.charAt(random.nextInt(alphabet.length())));
        }

        String sawe = builder.toString();
        txt_transaction_id = findViewById(R.id.txt_transaction_id);
        txt_transaction_id.setText(sawe);
    }

    private void loadSpinnerData(){
        // database handler
        DBHelper db = new DBHelper(getApplicationContext());

        // Spinner Drop down elements
        List<String> lables = db.getCustomer();

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, lables);

        // Drop down layout style - list view with radio button
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner_customers.setAdapter(dataAdapter);
    }

    public void loadProducts(){
        DBHelper db = new DBHelper(getApplicationContext());

        List<String> product_names = db.getProducts();

        ArrayAdapter<String> productsAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,product_names);

        productsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner_products.setAdapter(productsAdapter);
    }

    public void Notifier(){
        minimum_stock = dbhelper.LowestStock();
        if (minimum_stock == s_edt_quantity){
            showNotification();
        }
    }

    private void showNotification() {

        // Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        Uri sound = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/raw/notify");
        //Get an instance of NotificationManager
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.inv1)
                        .setSound(sound)
                        .setContentTitle(product_name)
                        .setContentText("Your Good Stock is running low");
        // Gets an instance of the NotificationManager service//
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(001, mBuilder.build());
    }







    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String customer_name = parent.getItemAtPosition(position).toString();
        //database operations to display specific selling price of the spinner selected item
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
