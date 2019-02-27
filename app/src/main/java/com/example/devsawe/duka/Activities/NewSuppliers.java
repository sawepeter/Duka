package com.example.devsawe.duka.Activities;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.devsawe.duka.R;
import com.example.devsawe.duka.database.DBHelper;

public class NewSuppliers extends AppCompatActivity {

    ImageButton btn_email,img_btn_sms,img_btn_call;
    Boolean success = true;
    DBHelper dbhelper;
    SQLiteDatabase db;
    EditText edt_supplier_name,adt_supplier_product,edt_location,edt_email,edt_phone_supplier;
    String s_supplier_name,s_supplier_product,s_location,s_email,s_phone_supplier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_suppliers);


        edt_supplier_name = findViewById(R.id.edt_supplier_name);
        adt_supplier_product = findViewById(R.id.adt_supplier_product);
        edt_location = findViewById(R.id.edt_location);
        edt_email = findViewById(R.id.edt_email);
        edt_phone_supplier = findViewById(R.id.edt_phone_supplier);




        btn_email = findViewById(R.id.btn_email);
        btn_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(NewSuppliers.this, "Email button clicked !!!", Toast.LENGTH_SHORT).show();
            }
        });

        img_btn_sms = findViewById(R.id.img_btn_sms);
        img_btn_sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(NewSuppliers.this, "Sms button clicked !!!", Toast.LENGTH_SHORT).show();
            }
        });

        img_btn_call = findViewById(R.id.img_btn_call);
        img_btn_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(NewSuppliers.this, "Call button clicked !!!", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private  boolean FieldsChecker() {
        boolean valid = true;

        String first = edt_supplier_name.getText().toString();
        String last = adt_supplier_product.getText().toString();
        String cell = edt_location.getText().toString();
        String phone = edt_phone_supplier.getText().toString();

        if (first.isEmpty()){
            edt_supplier_name.setError("Empty good name field");
            valid = false;
        }else {
            edt_supplier_name.setError(null);
        }
        if (last.isEmpty()){
            edt_location.setError("Empty barcode field");
            valid = false;
        }else {
            edt_location.setError(null);
        }
        if (cell.isEmpty()){
            adt_supplier_product.setError("Empty unit price field");
            valid = false;
        }else {
            adt_supplier_product.setError(null);
        }
        if (phone.isEmpty()){
            edt_phone_supplier.setError("Empty unit price field");
            valid = false;
        }else {
            edt_phone_supplier.setError(null);
        }


        return valid;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.item_suppliers, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.home_btn:
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;

            case R.id.add_supplier:
               try{

                   dbhelper = new DBHelper(getApplicationContext());
                   db= dbhelper.getReadableDatabase();



                   s_supplier_name = edt_supplier_name.getText().toString();
                   s_supplier_product = adt_supplier_product.getText().toString();
                   s_location = edt_location.getText().toString();
                   s_email = edt_email.getText().toString();
                   s_phone_supplier = edt_phone_supplier.getText().toString();



                   dbhelper.AddSupplier(s_supplier_name,s_supplier_product,s_location,s_email,s_phone_supplier);
                   if (!FieldsChecker()){
                       Toast.makeText(getApplicationContext(), "Fill all the required empty fields", Toast.LENGTH_SHORT).show();
                   } else if(success){
                       Toast.makeText(getApplicationContext(), "Good successfully saved!!!!", Toast.LENGTH_SHORT).show();
                       finish();
                   }else {
                       Toast.makeText(getApplicationContext(), "Good failed to save !!!!", Toast.LENGTH_SHORT).show();
                   }

               }catch (Exception ex){
                   success=false;

               }
        }
        return super.onOptionsItemSelected(item);
    }
}
