package com.example.devsawe.duka.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import com.example.devsawe.duka.R;
import com.example.devsawe.duka.database.DBHelper;
import com.example.devsawe.duka.database.Database;

public class Clients extends AppCompatActivity {

    Button sale_toast;
    int minteger = 0;
    TextView displayInteger,product_sprice,grandtotal,quantity;
    String grand_total;
    ListView list_customer;
    public SimpleCursorAdapter clients_display;
    private Controller controller;
    SharedPreferences preferences_product;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        controller = new Controller();



        preferences_product = PreferenceManager.getDefaultSharedPreferences(this);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
       getGoods();

       // sale_toast = findViewById(R.id.sale_toast);
       /* sale_toast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    //showDialogSales();
            }
        });*/

        }
        public void showDialogSales(){
            final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

            LayoutInflater inflater = this.getLayoutInflater();
            final View dialogview = inflater.inflate(R.layout.custom_dialog, null);
            dialogBuilder.setTitle("Add Product");
           dialogBuilder.setView(dialogview);
           dialogBuilder.setNegativeButton("CANCEL",null);
           dialogBuilder.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialog, int which) {
                   Toast.makeText(Clients.this, "Thanks you!!!", Toast.LENGTH_SHORT).show();
               }
           });
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
            product_sprice = dialogview.findViewById(R.id.product_sprice);
            grandtotal = dialogview.findViewById(R.id.grandtotal);
            quantity = dialogview.findViewById(R.id.quantity);

           dialogBuilder.create();
           dialogBuilder.show();

        }
        private void calc() {
        //Toast.makeText(this, "Its working!!", Toast.LENGTH_SHORT).show();
            controller.toast("Its working",Clients.this,R.drawable.ic_done_light_green_a100_24dp);
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
            list_customer = findViewById(R.id.list_customer);
            list_customer.setAdapter(clients_display);
            list_customer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    TextView txtproduct = view.findViewById(R.id.good);
                    SharedPreferences.Editor edit = preferences_product.edit();
                    edit.putString("Selected_product",txtproduct.getText().toString());
                    edit.apply();
                    showDialogSales();
                    controller.toast("You clicked !!!" +txtproduct.getText().toString(),Clients.this,R.drawable.navicon);
                    //finish();
                }
            });
            dbhelper.close();
        }catch (Exception ex){
            Toast.makeText(getApplicationContext(), "Error:"+ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


}

