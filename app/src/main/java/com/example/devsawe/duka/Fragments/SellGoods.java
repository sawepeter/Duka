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

import com.example.devsawe.duka.Activities.GoodSales;
import com.example.devsawe.duka.R;
import com.example.devsawe.duka.database.DBHelper;
import com.example.devsawe.duka.database.Database;

public class SellGoods extends Fragment {

    Context context;
    public SimpleCursorAdapter suppliers_display;
    DBHelper dbhelper;
    ListView list_view_sales;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        View rootview = inflater.inflate(R.layout.fragment_sell_goods, container, false);
        context = getContext();

        FloatingActionButton sell_goods = rootview.findViewById(R.id.sell_goods);
        sell_goods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,GoodSales.class);
                startActivity(intent);
            }
        });

        return rootview;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Sell Good");
        getSales();
    }

    public void getSales(){
        try{
            dbhelper = new DBHelper(getActivity());
            SQLiteDatabase db = dbhelper.getReadableDatabase();
            Cursor goods = db.query(true, Database.SALES_TABLE_NAME,null,null,null,null,null,null,null,null);
            if (goods.getCount()==0){
                Toast.makeText(context, "no records", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(context, "records present", Toast.LENGTH_SHORT).show();
            }

            String from [] = {Database.SALE_TRANSACTION_ID,Database.SALES_CUSTOMER,Database.SALES_DATE,Database.SALE_PRODUCT,Database.SALE_QUANTITY,Database.SALE_AMOUNT_RECEIVED};
            int to [] = {R.id.sale_transaction_id,R.id.sale_customer,R.id.sale_date,R.id.sale_product,R.id.quantity_bought,R.id.paid_amount};

            suppliers_display = new SimpleCursorAdapter(getActivity(),R.layout.sales_row_item,goods,from,to);
            list_view_sales = getActivity().findViewById(R.id.list_view_sales);
            list_view_sales.setAdapter(suppliers_display);
            dbhelper.close();
        }catch (Exception ex){
            Toast.makeText(getActivity(), "Error:"+ex.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }
}
