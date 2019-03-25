package com.example.devsawe.duka.Activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.devsawe.duka.Common;
import com.example.devsawe.duka.R;
import com.example.devsawe.duka.database.DBHelper;
import com.example.devsawe.duka.database.Database;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

public class FingerPrint extends AppCompatActivity  {

    TextView biggest_buyer_name,expenses_amount,Sales_Amount;
    DBHelper dbhelper;
    SQLiteDatabase db;
    BarChart barChart,barchart2;
    PieChart pieChart;
    LineChart lineChart;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finger_print);

        // pieChart = findViewById(R.id.piechart);
        barchart2 = findViewById(R.id.barchart2);

        dbhelper = new DBHelper(getApplicationContext());
        db = dbhelper.getReadableDatabase();

        biggest_buyer_name = findViewById(R.id.biggest_buyer_name);
        biggest_buyer_name.setText(dbhelper.HighestBuyer());
        //code to display the product that was bought the highest
        expenses_amount = findViewById(R.id.expenses_amount);
        expenses_amount.setText(dbhelper.HighestSoldProduct());

        //code to display the total sales earned
        Sales_Amount = findViewById(R.id.Sales_Amount);
        Sales_Amount.setText(dbhelper.TotalSales());

        barChart = findViewById(R.id.chart);

        addData();
        addLineChart();

       /* BarData bardata = new BarData(getXAxisValues(), getDataSet());
        chart.setData(bardata);
        chart.setDescription("");
        chart.animateXY(3000, 3000);
        chart.invalidate();

        pieChart.setUsePercentValues(true);

     /*   ArrayList<Entry> yvalues = new ArrayList<Entry>();
        yvalues.add(new Entry(10, 0));
        yvalues.add(new Entry(1, 1));
        yvalues.add(new Entry(12, 2));
        //yvalues.add(new Entry(25f, 3));
        //yvalues.add(new Entry(23f, 4));
        //yvalues.add(new Entry(17f, 5));

        PieDataSet dataSet = new PieDataSet(yvalues,"");

        ArrayList<String> xVals = new ArrayList<String>();

        xVals.add("Sawe");
        xVals.add("Job");
       xVals.add("Peter");
        //xVals.add("April");
        //xVals.add("May");
        //xVals.add("June");


        PieData data = new PieData(xVals, dataSet);
        // In Percentage term
        data.setValueFormatter(new PercentFormatter());
        // Default value
        //data.setValueFormatter(new DefaultValueFormatter(0));
        pieChart.setData(data);
        pieChart.setDescription("This is Pie Chart");

        pieChart.setDrawHoleEnabled(true);
        pieChart.setTransparentCircleRadius(25f);
        pieChart.setHoleRadius(25f);

        dataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
        data.setValueTextSize(13f);
        data.setValueTextColor(Color.DKGRAY);
        pieChart.setOnChartValueSelectedListener(this);

        pieChart.animateXY(1400, 1400);*/
    }
    private void addData(){

        ArrayList<BarEntry> yVals = new ArrayList<BarEntry>();

        for (int i = 0; i < dbhelper.queryYData().size(); i++)
            yVals.add(new BarEntry(dbhelper.queryYData().get(i), i));

        ArrayList<String> xVals = new ArrayList<String>();
        for(int i = 0; i < dbhelper.queryXData().size(); i++)
            xVals.add(dbhelper.queryXData().get(i));

        BarDataSet dataSet = new BarDataSet(yVals, "Goods");
        dataSet.setColors(Common.COLORFUL_COLOR);

       BarData data = new BarData(xVals, dataSet);
       // PieData pieData = new PieData(xVals,dataSet);



        LimitLine line = new LimitLine(0, "");
        line.setTextSize(12f);
        line.setLineWidth(4f);
        YAxis leftAxis = barchart2.getAxisLeft();

        leftAxis.addLimitLine(line);

        barchart2.setData(data);
        barchart2.setDescription("Good sales");
        barchart2.animateY(2000);

    }

    private void addLineChart(){

        ArrayList<BarEntry> yVals = new ArrayList<BarEntry>();

        for (int i = 0; i < dbhelper.queryYData().size(); i++)
            yVals.add(new BarEntry(dbhelper.queryYData().get(i), i));

        ArrayList<String> xVals = new ArrayList<String>();
        for(int i = 0; i < dbhelper.queryXData().size(); i++)
            xVals.add(dbhelper.CustomersXdata().get(i));

        BarDataSet dataSet = new BarDataSet(yVals, "customers names");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        BarData data = new BarData(xVals, dataSet);
        // PieData pieData = new PieData(xVals,dataSet);



        LimitLine line = new LimitLine(8, "");
        line.setTextSize(12f);
        line.setLineWidth(4f);
        YAxis leftAxis = barChart.getAxisLeft();

        leftAxis.addLimitLine(line);

        barChart.setData(data);
        barChart.setDescription("The expenses chart.");
        barChart.animateY(2000);

    }




   /* private ArrayList<BarDataSet> getDataSet() {
        ArrayList<BarDataSet> dataSets = null;

        ArrayList<BarEntry> valueSet1 = new ArrayList<>();
        BarEntry v1e1 = new BarEntry(140, 0);
        valueSet1.add(v1e1);
        BarEntry v1e2 = new BarEntry(30, 1);
        valueSet1.add(v1e2);
        BarEntry v1e3 = new BarEntry(20, 2);
        valueSet1.add(v1e3);
        BarEntry v1e4 = new BarEntry(10, 3);
        valueSet1.add(v1e4);
        BarEntry vle5 = new BarEntry(10,4);
        valueSet1.add(vle5);

        BarDataSet barDataSet1 = new BarDataSet(valueSet1, "Brand 1");
        barDataSet1.setColors(Common.COLORFUL_COLOR);

        dataSets = new ArrayList<>();
        dataSets.add(barDataSet1);
        return dataSets;
    }*/

    //newly fetched code from stackoverflow deemed to be working
    //x axis values code


/*
    private ArrayList<String> getXAxisValues() {
        ArrayList<String> xAxis = new ArrayList<>();
        xAxis.add("KDF");//1
        xAxis.add("Telkom");//2
        xAxis.add("Bread");//3
        xAxis.add("Milk");//4
        xAxis.add("Tea leaves");//5
        return xAxis;
    }
     private ArrayList<String> getsalescustomers(){
         DBHelper db = new DBHelper(getApplicationContext());

         return (ArrayList<String>) db.getsaleCustomer();
     }

     private ArrayList<String> getsaleamount(){

         DBHelper db = new DBHelper(getApplicationContext());

         return (ArrayList<String>) db.getSaleAmount();

     }





    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
        if (e == null)
            return;
        Log.i("VAL SELECTED",
                "Value: " + e.getVal() + ", xIndex: " + e.getXIndex()
                        + ", DataSet index: " + dataSetIndex);
    }

    @Override
    public void onNothingSelected() {
        Log.i("PieChart", "nothing selected");
    }*/



    }



