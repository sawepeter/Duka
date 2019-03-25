package com.example.devsawe.duka.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.devsawe.duka.R;
import com.example.devsawe.duka.database.DBHelper;
import com.example.devsawe.duka.database.Database;

import java.util.ArrayList;
import java.util.List;

public class Display extends AppCompatActivity {

    ListView list_customer;
    String nameinfo,emailinfo;
    public SimpleCursorAdapter display;
    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        preferences=PreferenceManager.getDefaultSharedPreferences(this);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);

        //   loadcustomers();
        //loadsaleamount();
        //users();

        list_customer = findViewById(R.id.list_customer);
        getCustomers();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                Toast.makeText(this, "Setting up!!!", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void users() {
        Bundle extras = getIntent().getExtras();
        nameinfo = extras.getString("Names");
        emailinfo = extras.getString("Email");

      //  list_customer = findViewById(R.id.list_customer);
        ArrayList<String> listItems = new ArrayList<String>();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,listItems);
        list_customer.setAdapter(adapter);
        listItems.add(nameinfo);
        listItems.add(emailinfo);
        adapter.notifyDataSetChanged();
    }

    public void loadcustomers(){
        DBHelper db = new DBHelper(getApplicationContext());

        // Spinner Drop down elements
        List<String> lables = db.getsaleCustomer();

        // Creating adapter for spinner
       // ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_list_item_1, lables);

        // Drop down layout style - list view with radio button
  //      dataAdapter
    //            .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
       // list_customer.setAdapter(dataAdapter);
    }

    public void loadsaleamount(){

        DBHelper db = new DBHelper(getApplicationContext());

        // Spinner Drop down elements
        List<String> sales = db.getCustomer();

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, sales);

         list_customer.setAdapter(dataAdapter);

    }

    public void getCustomers(){
        try{
            DBHelper dbhelper = new DBHelper(getApplicationContext());
            SQLiteDatabase db = dbhelper.getReadableDatabase();
            Cursor customers = db.query(true, Database.CUSTOMER_TABLE_NAME,null,null,null,null,null,null,null,null);
            if (customers.getCount()==0){
                Toast.makeText(getApplicationContext(), "no records", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getApplicationContext(), "records present", Toast.LENGTH_SHORT).show();
            }

            String from [] = {Database.CUSTOMER_NAME,Database.CUSTOMER_PHONE};
            int to [] = {R.id.customer_name,R.id.customer_phone};

            display = new SimpleCursorAdapter(getApplicationContext(),R.layout.custom_customers,customers,from,to);
           list_customer = findViewById(R.id.list_customer);
            list_customer.setAdapter(display);
            list_customer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String selectedFromList = (list_customer.getItemAtPosition(position).toString());

                    TextView txtName=view.findViewById(R.id.customer_name);
                    SharedPreferences.Editor edit=preferences.edit();
                    edit.putString("Name",txtName.getText().toString());
                    edit.apply();
                    Toast.makeText(Display.this, "You clicked" + txtName.getText().toString(), Toast.LENGTH_SHORT).show();
                    finish();
                    //Intent intent = new Intent(Display.this,Make_sales.class);
                 //   startActivity(intent);
                }
            });

            dbhelper.close();
        }catch (Exception ex){
            Toast.makeText(getApplicationContext(), "Error:"+ex.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }




}
