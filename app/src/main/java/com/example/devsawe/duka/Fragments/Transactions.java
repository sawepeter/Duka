package com.example.devsawe.duka.Fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.example.devsawe.duka.Activities.TransactionDetails;
import com.example.devsawe.duka.Controller;
import com.example.devsawe.duka.R;
import com.example.devsawe.duka.database.DBHelper;
import com.example.devsawe.duka.database.Database;

public class Transactions extends Fragment {

    Context context;
    ListView list_transaction;
    public SimpleCursorAdapter display;
    DBHelper dbhelper;
    private Controller controller;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_transactions, container, false);
        context = rootView.getContext();

        controller = new Controller();

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Transactions Analysis");
        getTransactions();
    }

    @Override
    public void onResume() {
        super.onResume();
        getTransactions();
    }

    public void getTransactions(){
        try{
            dbhelper = new DBHelper(getActivity());
            SQLiteDatabase db = dbhelper.getReadableDatabase();
            Cursor billingsdetails = db.query(true, Database.BILLING_TABLE_NAME,null,null,null,null,null,null,null,null);
            if (billingsdetails.getCount()==0){
                //Toast.makeText(context, "no records", Toast.LENGTH_SHORT).show();
                controller.toast("no records",getContext(),R.drawable.ic_error_outline_black_24dp);
            }else{
                //Toast.makeText(context, "records present", Toast.LENGTH_SHORT).show();
                controller.toast("records present",getContext(),R.drawable.ic_error_outline_black_24dp);
            }

            String from [] = {Database.ROW_ID,Database.BILLING_TIME,Database.BILLING_TOTAL,Database.BILLING_CASH_IN};
            int to [] = {R.id.transaction_id,R.id.transaction_date,R.id.transaction_items,R.id.transaction_total_price};

            display = new SimpleCursorAdapter(getActivity(),R.layout.transaction_item,billingsdetails,from,to);
            list_transaction = getActivity().findViewById(R.id.list_transaction);
            list_transaction.setAdapter(display);
            list_transaction.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //String selectedFromList = (list_view_customers.getItemAtPosition(position).toString());
                    Intent intent = new Intent(getActivity(), TransactionDetails.class);
                    startActivity(intent);
                    Toast.makeText(context, "You clicked" + position, Toast.LENGTH_SHORT).show();
                }
            });
            dbhelper.close();
        }catch (Exception ex){
            Toast.makeText(getActivity(), "Error:"+ex.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }


}


