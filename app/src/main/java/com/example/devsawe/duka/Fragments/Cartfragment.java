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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.example.devsawe.duka.Activities.Clients;
import com.example.devsawe.duka.Activities.NewCustomers;
import com.example.devsawe.duka.Controller;
import com.example.devsawe.duka.R;
import com.example.devsawe.duka.database.DBHelper;
import com.example.devsawe.duka.database.Database;

import q.rorbin.badgeview.QBadgeView;

public class Cartfragment extends Fragment {

    Context context;
    private Controller controller;
    Button buttonClear,buttonCharge;
    DBHelper dbhelper;
    SimpleCursorAdapter cartAdadapter;
    ListView list_cart;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cart, container, false);
        context = rootView.getContext();

        controller = new Controller();


        FloatingActionButton fab_cart = rootView.findViewById(R.id.fab_cart);
        fab_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Clients.class);
                startActivity(intent);
            }
        });

        buttonClear = rootView.findViewById(R.id.button_clear);
        buttonCharge = rootView.findViewById(R.id.button_charge);
        try {
            buttonClear.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_delete_black_24dp, 0, 0, 0);
            buttonCharge.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_check_black_24dp, 0);
        } catch (Exception nm) {

        }
        buttonCharge.setText("0.0");
        return rootView;
    }

    public void setCharge() {
        buttonCharge.setText("");
        buttonCharge.setText("CHARGE " + String.valueOf(dbhelper.sumOfTotalPricesOfItemsInCart()));
        int itemsCountInCart = dbhelper.getNoOfItemsInCart();
        try {

            View view = getActivity().findViewById(R.id.action_receipt);
            if (itemsCountInCart > 0) {
                new QBadgeView(getActivity()).bindTarget(view).setBadgeNumber(itemsCountInCart);
            } else if (itemsCountInCart == 0) {
                new QBadgeView(getActivity()).bindTarget(view).setBadgeText(String.valueOf(itemsCountInCart)).setBadgeBackgroundColor(R.color.colorAccent);
                // Toast.makeText(getContext(), "bade", Toast.LENGTH_SHORT).show();

            }
        } catch (Exception m) {
            m.printStackTrace();
            // Toast.makeText(getContext(), "error"+m, Toast.LENGTH_SHORT).show();
        }

        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // controller.toast("We will clear the cart",getActivity(),R.drawable.navicon);
                dbhelper.clearCart();
            }
        });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Duka Cart");
        getCart();

    }


    public void getCart(){
        try{
            dbhelper = new DBHelper(getActivity());
            SQLiteDatabase db = dbhelper.getReadableDatabase();
            Cursor cart = db.query(true, Database.CART_TABLE_NAME,null,null,null,null,null,null,null,null);
            if (cart.getCount()==0){
                //Toast.makeText(context, "no records", Toast.LENGTH_SHORT).show();
                controller.toast("no records",getContext(),R.drawable.ic_error_outline_black_24dp);
            }else{
                //Toast.makeText(context, "records present", Toast.LENGTH_SHORT).show();
                controller.toast("records present",getContext(),R.drawable.navicon);
                setCharge();
                //buttonCharge.setText("CHARGE " + String.valueOf(dbhelper.sumOfTotalPricesOfItemsInCart()));
            }

            String from [] = {Database.CART_ITEM_NAME,Database.CART_ITEM_QUANTITY,Database.CART_ITEM_TOTAL};
            int to [] = {R.id.item_name,R.id.cart_quantity,R.id.cart_total};

            cartAdadapter = new SimpleCursorAdapter(getActivity(),R.layout.cart_item,cart,from,to);
            list_cart = getActivity().findViewById(R.id.list_cart);
            list_cart.setAdapter(cartAdadapter);
            list_cart.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //String selectedFromList = (list_view_customers.getItemAtPosition(position).toString());
                    Toast.makeText(context, "You clicked" + position, Toast.LENGTH_SHORT).show();
                }
            });
            dbhelper.close();
        }catch (Exception ex){
            Toast.makeText(getActivity(), "Error:"+ex.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }
}
