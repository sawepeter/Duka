package com.example.devsawe.duka.Fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.example.devsawe.duka.Activities.NewCustomers;
import com.example.devsawe.duka.Activities.NewGoods;
import com.example.devsawe.duka.R;
import com.example.devsawe.duka.database.DBHelper;
import com.example.devsawe.duka.database.Database;

import java.util.List;

public class Customers extends Fragment {

    Context context;
   ListView list_view_customers;
    public SimpleCursorAdapter display;
    DBHelper dbhelper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_customers, container, false);
        context = rootView.getContext();


        FloatingActionButton add_customer = rootView.findViewById(R.id.add_customer);
        add_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NewCustomers.class);
                startActivity(intent);
            }
        });
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Duka Customers");
        getCustomers();
    }

    public void getCustomers(){
        try{
            dbhelper = new DBHelper(getActivity());
            SQLiteDatabase db = dbhelper.getReadableDatabase();
            Cursor customers = db.query(true, Database.CUSTOMER_TABLE_NAME,null,null,null,null,null,null,null,null);
            if (customers.getCount()==0){
                Toast.makeText(context, "no records", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(context, "records present", Toast.LENGTH_SHORT).show();
            }

            String from [] = {Database.CUSTOMER_NAME,Database.CUSTOMER_PHONE};
            int to [] = {R.id.customer_name,R.id.customer_phone};

            display = new SimpleCursorAdapter(getActivity(),R.layout.custom_customers,customers,from,to);
            list_view_customers = getActivity().findViewById(R.id.list_view_customers);
            list_view_customers.setAdapter(display);
            dbhelper.close();
        }catch (Exception ex){
            Toast.makeText(getActivity(), "Error:"+ex.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }
}
