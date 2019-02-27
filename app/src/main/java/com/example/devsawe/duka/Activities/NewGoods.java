package com.example.devsawe.duka.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.devsawe.duka.R;
import com.example.devsawe.duka.database.DBHelper;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NewGoods extends AppCompatActivity implements View.OnClickListener {
    EditText edtgoodname, edt_stock, edt_minimum_stock, edt_purchase_cost, edt_sale_price;
    String s_goodname, s_edtbarcode, s_stock, s_minimum_stock, s_purchase_cost, s_sale_price;
    public static TextView edtbarcode;
    File file;
    DBHelper dbhelper;
    SQLiteDatabase db;
    Boolean success = true;
    private static final int CAMERA_REQUEST = 1888;
    private static final int SELECT_PHOTO = 100;
    public static Bitmap imageBitmap;
    CircularImageView circularImageView;
    Button Scan_Button;
    private BottomSheetDialog bottomSheetDialog;
    //qr code scanner object
    private IntentIntegrator qrScan;
    public static String date_save = "";
    public static String signpath = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_goods);

        //intializing scan object
        qrScan = new IntentIntegrator(this);

        bottomSheetDialog = new BottomSheetDialog(NewGoods.this);
        View bottomSheetDialogView = getLayoutInflater().inflate(R.layout.avatars_dialog, null);
        bottomSheetDialog.setContentView(bottomSheetDialogView);

        View takephotoView = bottomSheetDialogView.findViewById(R.id.take_photo);
        View choosefromGalleyView = bottomSheetDialogView.findViewById(R.id.Gallery_choose);

        Scan_Button = findViewById(R.id.quantity_units);
        Scan_Button.setOnClickListener(this);


        choosefromGalleyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(
                        NewGoods.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        SELECT_PHOTO);
            }
        });

        takephotoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                 startActivityForResult(cameraIntent, CAMERA_REQUEST);

            }
        });

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);

        circularImageView = findViewById(R.id.goods_image);
        edtgoodname = findViewById(R.id.edtgoodname);
        edtbarcode = findViewById(R.id.edtbarcode);
        edt_stock = findViewById(R.id.edt_stock);
        edt_minimum_stock = findViewById(R.id.edt_minimum_stock);
        edt_purchase_cost = findViewById(R.id.edt_purchase_cost);
        edt_sale_price = findViewById(R.id.edt_sale_price);


        circularImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.show();
            }
        });
    }

    public void SaveImage() {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date());
        date_save = timeStamp;
        Log.d("savee: ", date_save);
        String fileName = "IMG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File pictureFile = null;
        try {
            pictureFile = File.createTempFile(fileName, ".jpg", storageDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("tttt", pictureFile.toString());
        signpath = pictureFile.getAbsolutePath();
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(pictureFile);
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
            fos.flush();
            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_edit, menu);
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == SELECT_PHOTO){
            if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,SELECT_PHOTO);
            } else {
                Toast.makeText(getApplicationContext(), "You don't have permission to access file location!", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case CAMERA_REQUEST:
                if (resultCode == RESULT_OK){
                    //Bundle extras = data.getExtras();
                    imageBitmap = (Bitmap) data.getExtras().get("data");
                    circularImageView.setImageBitmap(imageBitmap);

                }
                break;
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK && data != null){
                    Uri uri = data.getData();

                    try {
                        InputStream inputStream = getContentResolver().openInputStream(uri);

                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                       circularImageView.setImageBitmap(bitmap);
                    }catch (FileNotFoundException e){
                        e.printStackTrace();
                    }
                }

                super.onActivityResult(requestCode, resultCode, data);
        }
        //Getting the scan results
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qr_code has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {

                edtbarcode.setText(result.getContents());
            }
        }
    }

    private  boolean validate() {
        boolean valid = true;

        String first = edtgoodname.getText().toString();
        String last = edtbarcode.getText().toString();
        String cell = edt_stock.getText().toString();
        String eaddress = edt_minimum_stock.getText().toString();

        if (first.isEmpty()){
            edtgoodname.setError("Empty good name field");
            valid = false;
        }else {
            edtgoodname.setError(null);
        }
        if (last.isEmpty()){
            edtbarcode.setError("Empty barcode field");
            valid = false;
        }else {
            edtbarcode.setError(null);
        }
        if (cell.isEmpty()){
            edt_stock.setError("Empty unit price field");
            valid = false;
        }else {
            edt_stock.setError(null);
        }
        if (eaddress.isEmpty()){
            edt_minimum_stock.setError("Empty quantity field");
            valid = false;
        }else {
            edt_minimum_stock.setError(null);
        }


        return valid;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            case R.id.save:
                try {

                    dbhelper = new DBHelper(getApplicationContext());
                    db= dbhelper.getReadableDatabase();


                    s_goodname = edtgoodname.getText().toString();
                    s_edtbarcode = edtbarcode.getText().toString();
                    s_stock = edt_stock.getText().toString();
                    s_minimum_stock = edt_minimum_stock.getText().toString();
                    s_purchase_cost = edt_purchase_cost.getText().toString();
                    s_sale_price = edt_sale_price.getText().toString();
                    SaveImage();

                    dbhelper.AddGood(signpath,s_goodname, s_edtbarcode, s_stock, s_minimum_stock, s_purchase_cost,s_sale_price);
                    if (!validate()){
                        Toast.makeText(NewGoods.this, "Fill all the required empty fields", Toast.LENGTH_SHORT).show();
                    } else if(success){
                        Toast.makeText(getApplicationContext(), "Good successfully & saved!!!!", Toast.LENGTH_SHORT).show();
                        Log.d("pathhh",signpath);
                        finish();
                    }else {
                        Toast.makeText(getApplicationContext(), "Good failed to save !!!!", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception ex){
                    success=false;
                }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {
        //initiating the qr code scan
        qrScan.initiateScan();
    }
}
