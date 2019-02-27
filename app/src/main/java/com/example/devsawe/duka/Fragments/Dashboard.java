package com.example.devsawe.duka.Fragments;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.devsawe.duka.R;
import com.example.devsawe.duka.database.DBHelper;
import com.example.devsawe.duka.database.Database;

public class Dashboard extends Fragment {
    Context context;
    DBHelper dbhelper;
    TextView stock_count,customer_count,supplier_count,sales_count;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        View rootview = inflater.inflate(R.layout.fragment_dashboard, container, false);
        context = rootview.getContext();
         stock_count = rootview.findViewById(R.id.stock_count);
        customer_count = rootview.findViewById(R.id.customer_count);
        supplier_count = rootview.findViewById(R.id.supplier_count);
        sales_count = rootview.findViewById(R.id.sales_count);
        return rootview;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("DashBoard");
        getGoodsCount();
        getCustomerCount();
        getSalesCount();
        getSupplierCount();

    }

    public String getGoodsCount(){
        String countquery = "SELECT * FROM "+ Database.GOODS_TABLE_NAME;
        dbhelper = new DBHelper(getActivity());
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(countquery, null);

        int count = cursor.getCount();
        String goods_count = String.valueOf(count);
        cursor.close();
        stock_count.setText(goods_count);

        return goods_count;
    }

    public String getCustomerCount(){
        String customercount = "SELECT * FROM "+Database.CUSTOMER_TABLE_NAME;
        dbhelper = new DBHelper(getActivity());
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(customercount, null);

        int customernumber = cursor.getCount();
        String number_of_customers = String.valueOf(customernumber);
        cursor.close();
        customer_count.setText(number_of_customers);


        return number_of_customers;

    }

    public String getSupplierCount(){
        String suppliercount = "SELECT * FROM "+Database.SUPPLIER_TABLE_NAME;
        dbhelper = new DBHelper(getActivity());
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(suppliercount, null);

        int suppliernumber = cursor.getCount();
        String number_of_supplier = String.valueOf(suppliernumber);
        cursor.close();
        supplier_count.setText(number_of_supplier);


        return number_of_supplier;

    }

    public String getSalesCount(){
        String salescount = "SELECT * FROM "+Database.CUSTOMER_TABLE_NAME;
        dbhelper = new DBHelper(getActivity());
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(salescount, null);

        int salesnumber = cursor.getCount();
        String number_of_sales = String.valueOf(salesnumber);
        cursor.close();
        sales_count.setText(number_of_sales);


        return number_of_sales;

    }

}
