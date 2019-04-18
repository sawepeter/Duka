package com.example.devsawe.duka.Fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.devsawe.duka.Activities.Clients;
import com.example.devsawe.duka.Activities.Receipt;
import com.example.devsawe.duka.Controller;
import com.example.devsawe.duka.Model.BillingModel;
import com.example.devsawe.duka.Model.CartModel;
import com.example.devsawe.duka.R;
import com.example.devsawe.duka.database.DBHelper;
import com.example.devsawe.duka.database.Database;
import com.google.gson.Gson;
import com.itextpdf.text.DocumentException;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import q.rorbin.badgeview.QBadgeView;

import static com.github.mikephil.charting.charts.Chart.LOG_TAG;

public class Cartfragment extends Fragment {

    Context context;
    private Controller controller;
    Button buttonClear,buttonCharge;
    DBHelper dbhelper;
    SimpleCursorAdapter cartAdadapter;
    ListView list_cart;
    FloatingActionButton fab_cart;
    Dialog dialog;
    EditText edtCharge;
    TextView txtTransNo,change;
    TextView txtBalance;
    RadioButton radioSend;
    RadioButton radioView;
    RadioGroup r;
    View rootView;
    ProgressDialog progressDialog;
    DecimalFormat formatter;
    ArrayList<CartModel> cartItemsmodel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         rootView = inflater.inflate(R.layout.fragment_cart, container, false);
        context = rootView.getContext();
        setHasOptionsMenu(true);

        formatter=new DecimalFormat("0000");
        txtTransNo = rootView.findViewById(R.id.transno);

       // txtTransNo.setText(formatter.format(TransNo));
        controller = new Controller();

       fab_cart = rootView.findViewById(R.id.fab_cart);
        fab_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Clients.class);
                startActivity(intent);
            }
        });
        //fabviews();

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

    @Override
    public void onResume() {
        super.onResume();
        getCart();
    }

    public void fabviews(){
        fab_cart = controller.fab_cart(getActivity(), true, R.drawable.ic_receipt_black_24dp);
        fab_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (dbhelper.sumOfTotalPricesOfItemsInCart() < 1) {
                        controller.toast("Transaction can't be completed !!",getActivity(),R.drawable.navicon);
                    } else {
                        controller.toast("we will checkout soon!!!",getActivity(),R.drawable.navicon);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    public void setCharge() {
        buttonCharge.setText("");
        buttonCharge.setText("CHARGE " + String.valueOf(dbhelper.sumOfTotalPricesOfItemsInCart()));
        int itemsCountInCart = dbhelper.getNoOfItemsInCart();
        try {

            rootView.findViewById(R.id.action_receipt);
            if (itemsCountInCart > 0) {
                new QBadgeView(getActivity()).bindTarget(rootView).setBadgeNumber(itemsCountInCart);
            } else if (itemsCountInCart == 0) {
                new QBadgeView(getActivity()).bindTarget(rootView).setBadgeText(String.valueOf(itemsCountInCart)).setBadgeBackgroundColor(R.color.colorAccent);
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

        buttonCharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //controller.toast("Lets do the maths !!!",getActivity(),R.drawable.navicon);
                charge();
            }
        });
    }

    public void charge(){
        //controller.toast("we will checkout soon!!!",getActivity(),R.drawable.navicon);
        dialog = new Dialog(context);
        dialog.setTitle(String.valueOf(dbhelper.sumOfTotalPricesOfItemsInCart()));
        dialog.setContentView(R.layout.dialog_sell);
        dialog.setCanceledOnTouchOutside(false);
        txtBalance = dialog.findViewById(R.id.cashback);
        edtCharge = dialog.findViewById(R.id.cashin);
        radioSend = dialog.findViewById(R.id.radioSend);
        radioView = dialog.findViewById(R.id.radioView);
        change = dialog.findViewById(R.id.change);

        r = dialog.findViewById(R.id.radio);
        r.clearCheck();
        Button btnDismiss = dialog.findViewById(R.id.btn_dismiss);
        Button btnFinish = dialog.findViewById(R.id.btn_finish);
        final double total = dbhelper.sumOfTotalPricesOfItemsInCart();

        txtBalance.setText(String.valueOf(total));

        cartItemsmodel = new ArrayList<>();
        cartItemsmodel = dbhelper.getAllItemsInCart();
        edtCharge.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                double newTotal = total - Double.valueOf(s.toString());
               // txtBalance.setText(String.valueOf(newTotal));
                    change.setText(String.valueOf(newTotal));
            } catch (Exception m) {
            }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (!edtCharge.getText().toString().equals("")) {
                    double paidAmount = Double.valueOf(edtCharge.getText().toString());
                    if (paidAmount >= total && dbhelper.getNoOfItemsInCart() >= 1) {


                        Gson gson = new Gson();
                        String data = gson.toJson(dbhelper.getAllItemsInCart());

                        Date date = new Date();
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd ,yyyy  HH : mm : ss");
                        String timeStamp = simpleDateFormat.format(date);

                        //inserting into transactions table
                        BillingModel billing_data = new BillingModel();
                        billing_data.setBillingdate(timeStamp);
                        billing_data.setBillingitems(data);
                        billing_data.setBillingtotal(String.valueOf(dbhelper.sumOfTotalPricesOfItemsInCart()));
                        billing_data.setBillingBptotal(String.valueOf(dbhelper.sumOfTotalBpPricesOfItemsInCart()));
                        billing_data.setBillingSellingprice(String.valueOf(dbhelper.sumofTotalSpOfItemsInCart()));
                        billing_data.setBillingcashin(String.valueOf(paidAmount));

                        if (dbhelper.InsertBilling(billing_data)) {

                            controller.toast("Transaction complete", getContext(), R.drawable.navicon);

                            dbhelper.clearCart();
                            Log.d(LOG_TAG, data);
                            if (radioSend.isChecked()) {
                                //shareReciept(myFile);
                                progressDialog = new ProgressDialog(getActivity());
                                progressDialog.setMessage("Generating Receipt ...");
                                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                progressDialog.setIndeterminate(true);
                                progressDialog.setCancelable(false);
                                progressDialog.show();
                                //Back b = new Back(0, stockItemsPojos, dbhelper.sumOfTotalPricesOfItemsInCart(), paidAmount, "My Shop\n0725632415\nsawepeter6@@gmail.com", "Sawe Peter", "8845");
                               // b.execute();
                                //dialog.dismiss();

                               Receiptgenerator receiptgenerator = new Receiptgenerator(0,cartItemsmodel, dbhelper.sumOfTotalPricesOfItemsInCart(), paidAmount, "Sawe Duka\n0725632415\nsawepeter6@gmail.com", "Sawe Peter", "C026-01-1177/2016");
                               receiptgenerator.execute();
                            } else if (radioView.isChecked()) {
                                //dialog.dismiss();
                                progressDialog = new ProgressDialog(getActivity());
                                progressDialog.setMessage("Generating Receipt ...");
                                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                progressDialog.setIndeterminate(true);
                                progressDialog.setCancelable(false);
                                progressDialog.show();
                                //viewPdf(myFile);

                                //Back b = new Back(1, stockItemsPojos, dbhelper.sumOfTotalPricesOfItemsInCart(), paidAmount, "My Shop\n0725632415\nsawepeter6@@gmail.com", "Sawe Peter", "8845");
                                //b.execute();

                                Receiptgenerator receiptgenerator = new Receiptgenerator(0,cartItemsmodel, dbhelper.sumOfTotalPricesOfItemsInCart(), paidAmount, "Sawe Duka\n0725632415\nsawepeter6@gmail.com", "Sawe Peter", "C026-01-1177/2016");
                                receiptgenerator.execute();
                                //dialog.dismiss();
                            } else {
                                dialog.dismiss();
                            }
                            if (dbhelper.clearCart()) {
                                setCharge();
                                //setView(true, getContext());
                            }
                        } else {
                            controller.toast("Transaction Failed ", getContext(), R.drawable.ic_error_outline_black_24dp);
                        }
                    } else {
                       // vi.vibrate(200);
                        controller.toast("Invalid",getActivity(),R.drawable.navicon);
                    }
                } else {
                   // vi.vibrate(200);
                   controller.toast("Invalid",getActivity(),R.drawable.navicon);
                }
            }
        });
        dialog.show();
    }

    public void viewPdf(File myFile) {
        // dialog.dismiss();
        try {
            progressDialog.dismiss();
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(myFile), "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
        } catch (Exception nm) {
            Toast.makeText(getContext(), "You dont have any pdf viewer", Toast.LENGTH_LONG).show();
        }
    }

    public void shareReciept(File f) {
        try {
            dialog.dismiss();
            progressDialog.dismiss();
            Uri uri = Uri.fromFile(f);
            Intent share = new Intent();
            share.setAction(Intent.ACTION_SEND);
            share.setType("application/pdf");
            share.putExtra(Intent.EXTRA_STREAM, uri);

            getContext().startActivity(share);
        } catch (Exception nm) {

        }
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

    public class Receiptgenerator extends AsyncTask<String, Void, File>{

        private int wht;
        private ArrayList<CartModel> cartitems;
        private double itemsInCart;
        private double paidAmount;
        private String shopDetails;
        private String serverName;
        private String recNo;

        public Receiptgenerator(int wht, ArrayList<CartModel> cartitems, double itemsInCart, double paidAmount, String shopDetails, String serverName, String recNo) {
            this.wht = wht;
            this.cartitems = cartitems;
            this.itemsInCart = itemsInCart;
            this.paidAmount = paidAmount;
            this.shopDetails = shopDetails;
            this.serverName = serverName;
            this.recNo = recNo;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(File file) {
            super.onPostExecute(file);
            if (wht == 0){
                shareReciept(file);
            }else if (wht == 1){
                viewPdf(file);
            }
        }

        @Override
        protected File doInBackground(String... params) {
            Receipt r = new Receipt(getContext());
            File myFilea = null;
            try {
                myFilea = r.re(cartitems,itemsInCart,paidAmount,shopDetails,serverName,recNo);
            } catch (DocumentException e){
                e.printStackTrace();
            }catch (FileNotFoundException e){
                e.printStackTrace();
            }
            return myFilea;
        }
    }
}
