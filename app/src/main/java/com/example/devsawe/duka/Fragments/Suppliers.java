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

import com.example.devsawe.duka.Activities.NewSuppliers;
import com.example.devsawe.duka.R;
import com.example.devsawe.duka.database.DBHelper;
import com.example.devsawe.duka.database.Database;

public class Suppliers extends Fragment {

    Context context;
    public SimpleCursorAdapter suppliers_display;
    DBHelper dbhelper;
    ListView list_view_suppliers;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        View rootView = inflater.inflate(R.layout.fragment_suppliers, container, false);
        context = rootView.getContext();

        FloatingActionButton add_supplier = rootView.findViewById(R.id.add_supplier);
        add_supplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NewSuppliers.class);
                startActivity(intent);
            }
        });
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Duka Suppliers");
        getSuppliers();
    }

    public void getSuppliers(){
        try{
            dbhelper = new DBHelper(getActivity());
            SQLiteDatabase db = dbhelper.getReadableDatabase();
            Cursor suppliers = db.query(true, Database.SUPPLIER_TABLE_NAME,null,null,null,null,null,null,null,null);
            if (suppliers.getCount()==0){
                Toast.makeText(context, "no records", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(context, "records present", Toast.LENGTH_SHORT).show();
            }

            String source [] = {Database.SUPPLIER_NAME,Database.SUPPLIER_PRODUCT,Database.SUPPLIER_LOCATION,Database.SUPPLIER_EMAIL,Database.SUPPLIER_PHONE};
            int results [] = {R.id.name_of_supplier,R.id.product_of_supplier,R.id.location_of_supplier,R.id.email_of_supplier,R.id.phone_of_supplier};

            suppliers_display = new SimpleCursorAdapter(getActivity(),R.layout.custom_supplier_layout,suppliers,source,results);
            list_view_suppliers = getActivity().findViewById(R.id.list_view_suppliers);
            list_view_suppliers.setAdapter(suppliers_display);
            dbhelper.close();
        }catch (Exception ex){
            Toast.makeText(getActivity(), "Error:"+ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
