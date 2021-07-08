package com.tm.tarvemart.mainUI;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tm.tarvemart.Adapter.AdapterForCart;
import com.tm.tarvemart.Helper.NetworkConnectionHelper;
import com.tm.tarvemart.R;
import com.tm.tarvemart.Util.Constants;
import com.tm.tarvemart.entity.ProductData;
import com.tm.tarvemart.sqliteDatabase.DataSource;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {
    private static TextView total;
    private static int offer_total_payment=0,total_payment=0;
    private static SharedPreferences price_preference;
    private static SharedPreferences.Editor price_editor;
    RecyclerView rv_cartList;
    TextView tv_noData;
    GridLayoutManager gridLayoutManager;
    List<ProductData> list_Products = new ArrayList<>();
    TextView tv_totalRs, tv_totalsave;
    Button btn_checkout;
    LinearLayout ll_main;
    private static TextView get_offer;
    SharedPreferences offer_preference=null;
    private static String off_per,result;
    private static TextView tv_subtotalRs;
    private SharedPreferences.Editor editor;
    SharedPreferences prefs=null;
    private String From_where;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("My Cart");
        ll_main = findViewById(R.id.ll_main);
        tv_totalRs = findViewById(R.id.tv_totalRs);
        tv_subtotalRs=findViewById(R.id.tv_subtotalRs);
        total=findViewById(R.id.total);

        get_offer=findViewById(R.id.get_offer);


        tv_totalsave = findViewById(R.id.tv_totalsave);
        btn_checkout = findViewById(R.id.btn_checkout);
        rv_cartList = (RecyclerView) findViewById(R.id.rv_cartList);
        tv_noData = (TextView) findViewById(R.id.tv_noData);
        gridLayoutManager = new GridLayoutManager(CartActivity.this, 1);


        offer_preference = getApplicationContext().getSharedPreferences("offer_preference", MODE_PRIVATE);
        off_per = offer_preference.getString("offer_percentage", "");

        result = off_per.replaceAll("%","");
        get_offer.setText(off_per + " ");

      /*  prefs = getSharedPreferences("com.tm.tarvemart.mainUI", MODE_PRIVATE);
//        result = off_per.replaceAll("%","");
//        get_offer.setText(off_per + " ");

        if (prefs.getBoolean("offfer_preference", true)) {
            result = off_per.replaceAll("%","");
            get_offer.setText(off_per + " ");
            //You can perform anything over here. This will call only first time
            editor = prefs.edit();
            editor.putBoolean("offfer_preference", false);
            editor.commit();
        }
        else {
            result = "";
            get_offer.setText("0");
        }*/



        rv_cartList.setLayoutManager(gridLayoutManager);
        DataSource dataSource = new DataSource(CartActivity.this);
        dataSource.open();
        list_Products = dataSource.selectInto_tbl_Products("", "");
        dataSource.close();
        if (list_Products != null && list_Products.size() > 0) {
            ll_main.setVisibility(View.VISIBLE);
            rv_cartList.setVisibility(View.VISIBLE);
            tv_noData.setVisibility(View.GONE);
            AdapterForCart adapterForCart = new AdapterForCart(CartActivity.this, list_Products, tv_totalRs, tv_totalsave);
            rv_cartList.setAdapter(adapterForCart);
            CartActivity.totalRsforCart(CartActivity.this, tv_totalRs,tv_totalsave);
            /*for (int i = 0; i < list_Products.size(); i++) {
                totalPrice += Integer.parseInt(list_Products.get(i).getSelling_price()) * Integer.parseInt(list_Products.get(i).getSelected_quantity());
                TotalSave += Integer.parseInt(list_Products.get(i).getPrice()) * Integer.parseInt(list_Products.get(i).getSelected_quantity());
            }
            tv_totalRs.setText("Rs " + totalPrice);
            tv_totalsave.setText("You save " + (TotalSave - totalPrice));*/
        } else {
            ll_main.setVisibility(View.GONE);
            rv_cartList.setVisibility(View.GONE);
            tv_noData.setVisibility(View.VISIBLE);
        }
        btn_checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkConnectionHelper.isOnline(CartActivity.this)) {
                    if (Constants.getSavedPreferences(CartActivity.this, Constants.ID, null) != null) {
                        startActivity(new Intent(CartActivity.this, AddressList.class).putExtra("totalPrice", ((total.getText().toString().trim()).split(" ")[1])).putExtra("spin_per",off_per));
                    } else {
                        startActivity(new Intent(CartActivity.this, SendOtpActivity.class));
                    }
                } else {
                    Toast.makeText(CartActivity.this, "Please check your internet connection! try again...", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(CartActivity.this, Dashboard.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public static void totalRsforCart(Context context, TextView tv_totalRs, TextView tv_totalsave) {
        int totalPrice = 0, TotalSave = 0;
        DataSource dataSource = new DataSource(context);
        dataSource.open();
        List<ProductData> list_Products = dataSource.selectInto_tbl_Products("", "");
        for (int i = 0; i < list_Products.size(); i++) {
            totalPrice += Double.parseDouble(list_Products.get(i).getSelling_price()) * Double.parseDouble(list_Products.get(i).getSelected_quantity());
            TotalSave += Double.parseDouble(list_Products.get(i).getPrice()) * Double.parseDouble(list_Products.get(i).getSelected_quantity());

        }
        tv_totalRs.setText("Rs " + TotalSave);
        tv_subtotalRs.setText("Rs " + totalPrice);


        //login perference
        price_preference = context.getSharedPreferences("price_preference", MODE_PRIVATE);
        price_editor = price_preference.edit();
        price_editor.putString("cart_p1", String.valueOf(totalPrice));
        price_editor.putString("cart_p2", String.valueOf(TotalSave));
        price_editor.commit();

        if (result.equals("")){
            get_offer.setText("00");
            total.setText("Rs " + totalPrice);
        }
        else
        {
            offer_total_payment= ((totalPrice*Integer.parseInt(result))/100);
            total_payment = totalPrice - offer_total_payment;
         //   Toast.makeText(context,""+total_payment,Toast.LENGTH_LONG).show();
            total.setText("Rs " + total_payment);
        }
        tv_totalsave.setText("You save Rs " + (TotalSave - totalPrice));
    }
}
