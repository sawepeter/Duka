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
import com.example.devsawe.duka.Activities.NewGoods;
import com.example.devsawe.duka.Controller;
import com.example.devsawe.duka.R;
import com.example.devsawe.duka.database.DBHelper;
import com.example.devsawe.duka.database.Database;

public class Goods extends Fragment  {
    Context context;
    ListView listview_goods;
    public SimpleCursorAdapter display;
    DBHelper dbhelper;
    private Controller controller;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragmentgoods, container, false);
        context = rootView.getContext();

        controller = new Controller();


        FloatingActionButton add_goods = rootView.findViewById(R.id.add_goods);
        add_goods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NewGoods.class);
                startActivity(intent);
            }
        });

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Add Good");
        getGoods();
    }

    public void getGoods(){
        try{
            dbhelper = new DBHelper(getActivity());
            SQLiteDatabase db = dbhelper.getReadableDatabase();
            Cursor goods = db.query(true, Database.GOODS_TABLE_NAME,null,null,null,null,null,null,null,null);
            if (goods.getCount()==0){
                Toast.makeText(context, "no records", Toast.LENGTH_SHORT).show();
            }else{
                //Toast.makeText(context, "records present", Toast.LENGTH_SHORT).show();
                controller.toast("records present in the database",getContext(),R.drawable.ic_error_outline_black_24dp);
            }

            String from [] = {Database.GOOD_IMAGE,Database.GOOD_NAME,Database.GOOD_BARCODE,Database.GOOD_STOCK,Database.GOOD_MINIMUM_STOCK,Database.GOOD_PURCHASE_COST,Database.GOOD_SALE_PRICE};
            int to [] = {R.id.db_image,R.id.good,R.id.barcode,R.id.available_stock,R.id.minimum_stock,R.id.purchase_cost,R.id.sale_price};

            display = new SimpleCursorAdapter(getActivity(),R.layout.custom_goods_layout,goods,from,to);
            listview_goods = getActivity().findViewById(R.id.listview_goods);
            listview_goods.setAdapter(display);
            dbhelper.close();
        }catch (Exception ex){
            Toast.makeText(getActivity(), "Error:"+ex.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

}
