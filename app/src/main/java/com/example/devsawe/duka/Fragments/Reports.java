package com.example.devsawe.duka.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.devsawe.duka.Activities.BarcodeScanner;
import com.example.devsawe.duka.Activities.Clients;
import com.example.devsawe.duka.Activities.FingerPrint;
import com.example.devsawe.duka.Activities.Make_sales;
import com.example.devsawe.duka.R;

public class Reports extends Fragment {
    Context context;
    private static final int CAMERA_REQUEST = 1888;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View reportsview = inflater.inflate(R.layout.fragment_reports, container, false);
        context = reportsview.getContext();

        Button btn_list = reportsview.findViewById(R.id.btn_list);
        btn_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,Make_sales.class);
                startActivity(intent);
            }
        });

        Button btn_reports = reportsview.findViewById(R.id.btn_reports);
        btn_reports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Intent intent = new Intent(context,FingerPrint.class);//fails to pick the fingerprint class will have to rename it.
                startActivity(intent);
            }
        });

        Button btn_barcode = reportsview.findViewById(R.id.btn_barcode);
        btn_barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,BarcodeScanner.class);
                startActivity(intent);
            }
        });

        Button btn_clients = reportsview.findViewById(R.id.btn_clients);
        btn_clients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,Clients.class);
                startActivity(intent);
            }
        });
        return reportsview;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Duka Reports");
    }

}
