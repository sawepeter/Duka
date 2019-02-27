package com.example.devsawe.duka.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.devsawe.duka.R;
import com.example.devsawe.duka.database.DBHelper;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class NewCustomers extends AppCompatActivity {

    EditText edt_customer_name,edt_customer_phone,edt_customer_location,edt_credit_amount;
    String customer_name,customer_phone,customer_location,credit_amount;
    private static final int CAMERA_REQUEST = 1888;
    private static final int SELECT_PHOTO = 100;
    Bitmap imageBitmap;
    CircularImageView circularImageView;
    DBHelper dbhelper;
    SQLiteDatabase db;
    Boolean success = true;
    Button take_photo,choose_photo;
    private BottomSheetDialog bottomSheetDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_customers);

        bottomSheetDialog = new BottomSheetDialog(NewCustomers.this);
        View bottomSheetDialogView = getLayoutInflater().inflate(R.layout.avatars_dialog, null);
        bottomSheetDialog.setContentView(bottomSheetDialogView);

        View takephotoView = bottomSheetDialogView.findViewById(R.id.take_photo);
        View choosefromGalleyView = bottomSheetDialogView.findViewById(R.id.Gallery_choose);

        choosefromGalleyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(
                        NewCustomers.this,
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

        edt_customer_name = findViewById(R.id.edt_customer_name);
        edt_customer_phone = findViewById(R.id.edt_customer_phone);
        edt_customer_location = findViewById(R.id.edt_customer_location);
        edt_credit_amount = findViewById(R.id.edt_credit_amount);
        circularImageView = findViewById(R.id.goods_image);

        circularImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.show();
            }
        });

    }

    public static byte[] CircularImageViewToByte(CircularImageView image) {
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
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
    }

    private  boolean validate() {
        boolean valid = true;

        String first = edt_customer_name.getText().toString();
        String last = edt_customer_phone.getText().toString();
        String cell = edt_customer_location.getText().toString();
        String eaddress = edt_credit_amount.getText().toString();

        if (first.isEmpty()){
            edt_customer_name.setError("Empty good name field");
            valid = false;
        }else {
            edt_customer_name.setError(null);
        }
        if (last.isEmpty()){
            edt_customer_phone.setError("Empty barcode field");
            valid = false;
        }else {
            edt_customer_phone.setError(null);
        }
        if (cell.isEmpty()){
            edt_customer_location.setError("Empty unit price field");
            valid = false;
        }else {
            edt_customer_location.setError(null);
        }
        if (eaddress.isEmpty()){
            edt_credit_amount.setError("Empty quantity field");
            valid = false;
        }else {
            edt_credit_amount.setError(null);
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

                    customer_name = edt_customer_name.getText().toString();
                    customer_phone = edt_customer_phone.getText().toString();
                    customer_location = edt_customer_location.getText().toString();
                    credit_amount = edt_credit_amount.getText().toString();


                    dbhelper.AddCustomer(customer_name, customer_phone, customer_location, credit_amount);
                    if (!validate()){
                        Toast.makeText(NewCustomers.this, "Fill all the required empty fields", Toast.LENGTH_SHORT).show();
                    } else if(success){
                        Toast.makeText(getApplicationContext(), "Good successfully saved!!!!", Toast.LENGTH_SHORT).show();
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
}
