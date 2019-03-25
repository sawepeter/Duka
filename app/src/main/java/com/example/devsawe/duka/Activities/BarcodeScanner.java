package com.example.devsawe.duka.Activities;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.devsawe.duka.R;
import com.example.devsawe.duka.database.DBHelper;
import com.example.devsawe.duka.database.Database;
import com.google.zxing.Result;

import org.w3c.dom.Text;

import java.util.ArrayList;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class BarcodeScanner extends AppCompatActivity  {

    ListView list_product;
    public SimpleCursorAdapter suppliers_display;
    SharedPreferences preferences_product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_scanner);


        preferences_product = PreferenceManager.getDefaultSharedPreferences(this);
       // sendNotification();
        getGoods();
    }

    private void sendNotification() {

       // Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        Uri sound = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/raw/notify");
        //Get an instance of NotificationManager
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.inv1)
                        .setSound(sound)
                        .setContentTitle("Low Stock Notification")
                        .setContentText("Your Good Stock is running low");
        // Gets an instance of the NotificationManager service//
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(001, mBuilder.build());
    }


    public void getGoods(){
        try{
            DBHelper dbhelper = new DBHelper(getApplicationContext());
            SQLiteDatabase db = dbhelper.getReadableDatabase();
            Cursor goods = db.query(true, Database.GOODS_TABLE_NAME,null,null,null,null,null,null,null,null);
            if (goods.getCount()==0){
                Toast.makeText(getApplicationContext(), "no records", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getApplicationContext(), "records present", Toast.LENGTH_SHORT).show();
            }

            String from [] = {Database.GOOD_IMAGE,Database.GOOD_NAME,Database.GOOD_BARCODE,Database.GOOD_STOCK,Database.GOOD_MINIMUM_STOCK,Database.GOOD_PURCHASE_COST,Database.GOOD_SALE_PRICE};
            int to [] = {R.id.db_image,R.id.good,R.id.barcode,R.id.available_stock,R.id.minimum_stock,R.id.purchase_cost,R.id.sale_price};

            suppliers_display = new SimpleCursorAdapter(getApplicationContext(),R.layout.custom_goods_layout,goods,from,to);
            list_product = findViewById(R.id.list_product);
            list_product.setAdapter(suppliers_display);
            list_product.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    TextView txtgood = view.findViewById(R.id.good);
                    SharedPreferences.Editor edit = preferences_product.edit();
                    edit.putString("Suppliers",txtgood.getText().toString());
                    edit.apply();
                    Toast.makeText(BarcodeScanner.this, "You clicked" + txtgood.getText().toString(), Toast.LENGTH_SHORT).show();
                    finish();
                    Intent intent = new Intent(BarcodeScanner.this,Make_sales.class);
                    startActivity(intent);
                }
            });
            dbhelper.close();
        }catch (Exception ex){
            Toast.makeText(getApplicationContext(), "Error:"+ex.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }




}
