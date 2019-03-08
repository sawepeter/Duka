package com.example.devsawe.duka.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.devsawe.duka.R;
import com.example.devsawe.duka.database.DBHelper;

import java.util.List;

public class Display extends AppCompatActivity {

    ListView list_customer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        list_customer = findViewById(R.id.list_customer);
     //   loadcustomers();
        loadsaleamount();
    }

    public void loadcustomers(){
        DBHelper db = new DBHelper(getApplicationContext());

        // Spinner Drop down elements
        List<String> lables = db.getsaleCustomer();

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, lables);

        // Drop down layout style - list view with radio button
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
       // list_customer.setAdapter(dataAdapter);
    }

    public void loadsaleamount(){

        DBHelper db = new DBHelper(getApplicationContext());

        // Spinner Drop down elements
        List<String> sales = db.getSaleAmount();

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, sales);

         list_customer.setAdapter(dataAdapter);

    }
}
