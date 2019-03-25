package com.example.devsawe.duka.Activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.devsawe.duka.Fragments.Customers;
import com.example.devsawe.duka.R;
import com.example.devsawe.duka.database.DBHelper;
import com.example.devsawe.duka.database.Database;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;

public class Make_sales extends AppCompatActivity {
    EditText add_date,add_customer,add_product;
    SharedPreferences preferences,preferences_product;
    ArrayList<String> listItems=new ArrayList<String>();
    ArrayAdapter<String> adapter;
    String product_chosen;
    ListView selected_product;
    ListView list_product;
    SimpleCursorAdapter suppliers_display;

    AlertDialog prod;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_sales);

        preferences=PreferenceManager.getDefaultSharedPreferences(this);
        preferences_product = PreferenceManager.getDefaultSharedPreferences(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);

        selected_product = findViewById(R.id.selected_product);
        add_date = findViewById(R.id.add_date);
        add_customer = findViewById(R.id.add_customer);
        add_customer.setText(preferences.getString("Name",""));
        add_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(Make_sales.this,Display.class);
                startActivity(intent);
            }
        });
        add_product = findViewById(R.id.add_product);
        //add_product.setText(preferences_product.getString("Suppliers",""));
        product_chosen = preferences_product.getString("Suppliers","");
        add_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* finish();
                Intent intent = new Intent(Make_sales.this,BarcodeScanner.class);
                startActivity(intent);*/
                goods();
            }
        });
        showcurrentdate();



    }
    public void goods(){
        final AlertDialog.Builder goods=new AlertDialog.Builder(this);
        LayoutInflater inflater=this.getLayoutInflater();
        final View product =inflater.inflate(R.layout.activity_barcode_scanner, null);
        goods.setView(product);
        list_product = product.findViewById(R.id.list_product);
        getGoods();
        prod=goods.create();
        prod.show();

    }
    public void getGoods(){
        try{
            DBHelper dbhelper = new DBHelper(this);
            SQLiteDatabase db = dbhelper.getReadableDatabase();
            Cursor goods = db.query(true, Database.GOODS_TABLE_NAME,null,null,null,null,null,null,null,null);
            if (goods.getCount()==0){
                Toast.makeText(getApplicationContext(), "no records", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getApplicationContext(), "records present", Toast.LENGTH_SHORT).show();
            }

            String from [] = {Database.GOOD_IMAGE,Database.GOOD_NAME,Database.GOOD_BARCODE,Database.GOOD_STOCK,Database.GOOD_MINIMUM_STOCK,Database.GOOD_PURCHASE_COST,Database.GOOD_SALE_PRICE};
            int to [] = {R.id.db_image,R.id.good,R.id.barcode,R.id.available_stock,R.id.minimum_stock,R.id.purchase_cost,R.id.sale_price};

            suppliers_display = new SimpleCursorAdapter(this,R.layout.custom_goods_layout,goods,from,to);

            list_product.setAdapter(suppliers_display);
            list_product.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    TextView txtgood = view.findViewById(R.id.good);
                    SharedPreferences.Editor edit = preferences_product.edit();
                    edit.putString("Suppliers",txtgood.getText().toString());
                    edit.apply();
                    Toast.makeText(Make_sales.this, "You clicked" + txtgood.getText().toString(), Toast.LENGTH_SHORT).show();
                    listItems.add(txtgood.getText().toString());

                    adapter= new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_list_item_1,listItems);

                    selected_product.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    prod.dismiss();

                }
            });
            dbhelper.close();
        }catch (Exception ex){
            Toast.makeText(getApplicationContext(), "Error:"+ex.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_edit, menu);
        return true;
    }

    public void showcurrentdate(){
        long date = System.currentTimeMillis();

        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy h:mm a");
        String dateString = sdf.format(date);
        add_date.setText(dateString);
    }
}
