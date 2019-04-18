package com.example.devsawe.duka.Activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.TextView;

import com.example.devsawe.duka.Model.BillingModel;
import com.example.devsawe.duka.Model.CartModel;
import com.example.devsawe.duka.R;
import com.example.devsawe.duka.database.DBHelper;

import java.io.File;
import java.util.ArrayList;

public class TransactionDetails extends AppCompatActivity {

    ArrayList<CartModel> p;
    File myFile = null;
    String fle;
    ProgressDialog progressDialog;
    private ArrayList<BillingModel> transactionsPojos;
    private int position;
    private RecyclerView recyclerView;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;
    private TextView textViewDate, textViewTotal;
    private DBHelper dbOperations;
    private Dialog dialog;
    private Button reproduce, reverse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
