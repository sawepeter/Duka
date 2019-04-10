package com.example.devsawe.duka.Activities;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.devsawe.duka.Controller;
import com.example.devsawe.duka.Fragments.Cartfragment;
import com.example.devsawe.duka.R;
import com.example.devsawe.duka.database.DBHelper;
import com.example.devsawe.duka.database.Database;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.text.SimpleDateFormat;

public class Clients extends AppCompatActivity {

    Button sale_toast;
    int minteger = 0;
    TextView txtproductBP,displayInteger,product_sprice,grandtotal,quantity,txtproduct,txtprice,
            add_date,txtselected_product,orderedquantity,carttotal,txtproductselect_price,txtproduct_buying_price;
    CircularImageView products_image;
    String image_product;
    String grand_total,selected_product,productselect_price,productselected_buyingprice;
    ListView list_sell;
    public SimpleCursorAdapter clients_display;
    private Controller controller;
    SharedPreferences preferences_product;
    DBHelper dbhelper;
    SQLiteDatabase db;
    Boolean success = true;
    String cart_date,cart_item,cart_quantity,cart_total,cart_sellingprice,cart_buyingprice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        controller = new Controller();

        preferences_product = PreferenceManager.getDefaultSharedPreferences(this);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        getGoods();

        }
        public void showDialogSales(){
            final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

            LayoutInflater inflater = this.getLayoutInflater();
            final View dialogview = inflater.inflate(R.layout.custom_dialog, null);
            dialogBuilder.setTitle("Add Product");
           dialogBuilder.setView(dialogview);
            selected_product = preferences_product.getString("Selected_product","");
            productselect_price = preferences_product.getString("selectproduct_price","");
            productselected_buyingprice = preferences_product.getString("product_buyingprice","");
          //  image_product = preferences_product.getString("imagepath","");
             txtselected_product = dialogview.findViewById(R.id.product_selected);
            add_date = dialogview.findViewById(R.id.add_date);
            showcurrentdate();
             txtproductselect_price = dialogview.findViewById(R.id.product_price);
             txtproduct_buying_price = dialogview.findViewById(R.id.product_buying_price);
            CircularImageView imageview = dialogview.findViewById(R.id.imageview);
            orderedquantity = dialogview.findViewById(R.id.ordered_quantity);
            carttotal = dialogview.findViewById(R.id.grandtotal);
            //imageview.setImageDrawable(image_product);
            txtselected_product.setText(selected_product);
            txtproductselect_price.setText(productselect_price);
            txtproduct_buying_price.setText(productselected_buyingprice);
            Button add_cart = dialogview.findViewById(R.id.add_cart);
            add_cart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {

                        dbhelper = new DBHelper(getApplicationContext());
                        db= dbhelper.getReadableDatabase();

                        cart_date = add_date.getText().toString();
                        cart_item = txtselected_product.getText().toString();
                        cart_quantity = orderedquantity.getText().toString();
                        cart_total = carttotal.getText().toString();
                        cart_sellingprice = txtproductselect_price.getText().toString();
                        cart_buyingprice = txtproduct_buying_price.getText().toString();


                        dbhelper.AddCart(cart_date, cart_item, cart_quantity, cart_total,cart_sellingprice,cart_buyingprice);
                        if (!CartValidate()){
                            Toast.makeText(Clients.this, "Ensure the quantity has a value greater than 0", Toast.LENGTH_SHORT).show();
                        } else if(success){
                            Toast.makeText(getApplicationContext(), "Good successfully !!!!", Toast.LENGTH_SHORT).show();
                            controller.toast("Good successfully added to cart ",Clients.this,R.drawable.navicon);




                            finish();


                        }else {
                            Toast.makeText(getApplicationContext(), "Good failed to save !!!!", Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception ex){
                        success=false;
                    }
                }
            });
           dialogBuilder.setNegativeButton("CANCEL",null);
           Button decrease = dialogview.findViewById(R.id.decrease);
           decrease.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   displayInteger = dialogview.findViewById(R.id.ordered_quantity);
                   minteger = minteger - 1;
                   displayInteger.setText("" + minteger);
                   calc();
               }
           });
           Button increase = dialogview.findViewById(R.id.increase);
           increase.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   displayInteger = dialogview.findViewById(R.id.ordered_quantity);
                   minteger = minteger + 1;
                   displayInteger.setText("" + minteger);
                   calc();

               }
           });
            product_sprice = dialogview.findViewById(R.id.product_price);
            grandtotal = dialogview.findViewById(R.id.grandtotal);
            quantity = dialogview.findViewById(R.id.quantity);

           dialogBuilder.create();
           dialogBuilder.show();

        }

    private  boolean CartValidate() {
        boolean valid = true;

        String first = orderedquantity.getText().toString();
        Double quantity = Double.parseDouble(first);

        if (quantity <= 0){
            orderedquantity.setError("Quantity should be greater than zero");
            valid = false;
        }

        return valid;
    }
        private void calc() {
        displayInteger.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(displayInteger.length()==0){
                    displayInteger.setText("0");
                }if (displayInteger.length()>0){
                   String quantitychange =  displayInteger.getText().toString();
                   Double s_price = Double.parseDouble(quantitychange);

                    String  actualprice = product_sprice.getText().toString();
                    Double price_actual = Double.parseDouble(actualprice);

                    quantity.setText(quantitychange);

                    Double sub_total = price_actual * s_price;
                    grand_total = Double.toString(sub_total);

                    grandtotal.setText(grand_total);



                }
            }
        });
    }

    public void getGoods(){
        try{
            DBHelper dbhelper = new DBHelper(getApplicationContext());
            SQLiteDatabase db = dbhelper.getReadableDatabase();
            Cursor goods = db.query(true, Database.GOODS_TABLE_NAME,null,null,null,null,null,null,null,null);
            if (goods.getCount()==0){
                controller.toast("No records present in database",Clients.this,R.drawable.navicon);
            }else{
                controller.toast("records present in database !!!",Clients.this,R.drawable.navicon);
            }

            String from [] = {Database.GOOD_IMAGE,Database.GOOD_NAME,Database.GOOD_SALE_PRICE,Database.GOOD_BARCODE,Database.GOOD_STOCK,Database.GOOD_MINIMUM_STOCK,Database.GOOD_PURCHASE_COST};
            int to [] = {R.id.db_image,R.id.good,R.id.sale_price,R.id.barcode,R.id.available_stock,R.id.minimum_stock,R.id.purchase_cost};

            clients_display = new SimpleCursorAdapter(getApplicationContext(),R.layout.custom_goods_layout,goods,from,to);
            list_sell = findViewById(R.id.list_sell);
            list_sell.setAdapter(clients_display);
            list_sell.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    txtproduct = view.findViewById(R.id.good);
                    txtprice = view.findViewById(R.id.sale_price);
                    txtproductBP = view.findViewById(R.id.purchase_cost);
                    products_image = view.findViewById(R.id.db_image);
                    SharedPreferences.Editor edit = preferences_product.edit();
                    edit.putString("Selected_product",txtproduct.getText().toString());
                    edit.putString("selectproduct_price",txtprice.getText().toString());
                    edit.putString("product_buyingprice",txtproductBP.getText().toString());
                   // edit.putString("imagepath",products_image.getDrawable().toString());
                    edit.apply();
                    showDialogSales();
                    controller.toast("You clicked !!!" +txtproduct.getText().toString(),Clients.this,R.drawable.navicon);
                    //finish();
                }
            });
            dbhelper.close();
        }catch (Exception ex){
            controller.toast("Error"+ex.getMessage(),getApplicationContext(),R.drawable.navicon);
        }
    }

    public void showcurrentdate(){
        long date = System.currentTimeMillis();

        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy h:mm a");
        String dateString = sdf.format(date);
        add_date.setText(dateString);
    }


}

