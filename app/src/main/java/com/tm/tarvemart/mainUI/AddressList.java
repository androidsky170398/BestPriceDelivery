package com.tm.tarvemart.mainUI;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.razorpay.Checkout;
import com.tm.tarvemart.Adapter.AdapterForAddress;
import com.tm.tarvemart.Helper.NetworkConnectionHelper;
import com.tm.tarvemart.Map.GpsTrackerForGoogleMap;
import com.tm.tarvemart.PayMentGateWay;
import com.tm.tarvemart.R;
import com.tm.tarvemart.Util.Constants;
import com.tm.tarvemart.VolleyMultiUse.VolleyForStateAndCity;
import com.tm.tarvemart.entity.AddressData;
import com.tm.tarvemart.entity.BillingData;
import com.tm.tarvemart.entity.CouponData;
import com.tm.tarvemart.entity.CreateOrder;
import com.tm.tarvemart.entity.ProductData;
import com.tm.tarvemart.entity.WalletData;
import com.tm.tarvemart.inerface.GlobleInterfce;
import com.tm.tarvemart.sqliteDatabase.DataSource;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.tm.tarvemart.Util.Constants.CITY_ID;
import static com.tm.tarvemart.Util.Constants.ID;

public class AddressList extends AppCompatActivity implements GlobleInterfce {
    private static final String TAG = AddressList.class.getSimpleName();

    RecyclerView rv_address;
    TextView tv_NoAddress;
    List<AddressData> addressDataList;
    AdapterForAddress adapterForAddress;
    GridLayoutManager gridLayoutManager;

    Button btn_AddAddress;
    LinearLayout ll_ifAddressIsAvailable;
    RadioButton rb_COD, rb_payNow;
    Button btn_CreateOrder;
    List<CouponData> list_AddedCouponData = new ArrayList<>();
    String totalPrice = "0", walletAmount = "0", CouponAmount = "0", paybleAmount = "0", sgst = "0", cgst = "0", totalWalletAmount = "0";
    RequestQueue queue;

    TextView txt_TotalAmount,
            txt_walletAmount,
            txt_CouponAmount,
            txt_payableAmount,
            txt_GST;
    String cgstper;
    String sgstper;

    TextView txt_TotalWalletAmount;
    CheckBox ch_wallet;
    LinearLayout ll_useWalletAmt;
    EditText et_useWalletAmount;
    Button btn_walletAmtAdd;

    Button btn_AddCoupon;
    LinearLayout ll_couponDetails;
    TextView tv_couponNo, tv_CouponAmountView;
    TextView tv_other;
    private String Transaction_id;
    private String succ_fail_response;
    Calendar calander;
    SimpleDateFormat simpledateformat;
    private String show_date_time;
    private String Spinner_percentage;
    private SharedPreferences price_preference;
    private String cart_price_total,cart_price_payable;
    SharedPreferences offer_preference=null;
    private static String off_per,result;
    private String send_amount;
    private String rz_send_amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_list);
        tv_other = findViewById(R.id.tv_other);
        Intent intent = getIntent();
        totalPrice = intent.getStringExtra("totalPrice");
        Spinner_percentage = intent.getStringExtra("spin_per");

        price_preference = getApplicationContext().getSharedPreferences("price_preference", MODE_PRIVATE);
        cart_price_total = price_preference.getString("cart_p1", "");
        cart_price_payable = price_preference.getString("cart_p2", "");

        offer_preference = getApplicationContext().getSharedPreferences("offer_preference", MODE_PRIVATE);
        off_per = offer_preference.getString("offer_percentage", "");


        findViewByIds();
        GpsTrackerForGoogleMap gpsTrackerForGoogleMap = new GpsTrackerForGoogleMap(AddressList.this);
        gridLayoutManager = new GridLayoutManager(AddressList.this, 1);
        rv_address.setHasFixedSize(true);
        rv_address.setLayoutManager(gridLayoutManager);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        calander = Calendar.getInstance();
        simpledateformat = new SimpleDateFormat("dd:MM:yyyy HH:mm:ss");
        show_date_time = simpledateformat.format(calander.getTime());
     //   Toast.makeText(getApplicationContext(),""+show_date_time,Toast.LENGTH_LONG).show();

        if (NetworkConnectionHelper.isOnline(AddressList.this)) {
            getAddressData();
            getWalletAmount();
            queue = Volley.newRequestQueue(this);
        } else {
            Toast.makeText(AddressList.this, "Please check your internet connection! try again...", Toast.LENGTH_SHORT).show();
        }


        ll_couponDetails.setVisibility(View.GONE);
        rb_COD.setChecked(true);
        txt_TotalAmount.setText(getResources().getString(R.string.sym_rs) + " " + totalPrice);


        btn_AddAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkConnectionHelper.isOnline(AddressList.this)) {
                    startActivity(new Intent(AddressList.this, AddAndEditAddress.class).putExtra("totalPrice", totalPrice));
                    finish();//alertAdd_And_DeleteAddress(AddressList.this, null, totalPrice);
                } else {
                    Toast.makeText(AddressList.this, "Please check your internet connection! try again...", Toast.LENGTH_SHORT).show();
                }

            }
        });


        rb_payNow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    rb_payNow.setChecked(false);
                    rb_COD.setChecked(true);
                    startPayment();
                }
            }
        });




        tv_other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkConnectionHelper.isOnline(AddressList.this)) {
                    Intent intent1 = new Intent(AddressList.this, AddAndEditAddress.class);
                    intent1.putExtra("totalPrice", totalPrice);
                    startActivity(intent1);
                    finish();
                } else {
                    Toast.makeText(AddressList.this, "Please check your Internet connection...", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_CreateOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddressList.this);
                builder.setMessage("Are you sure you want to confirm your order?");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(AddressList.this, "Work in Progress", Toast.LENGTH_SHORT).show();
                        final CreateOrder createOrder = new CreateOrder();
                        AddressData addressData = new AddressData();
                        addressData.setAddress_id(addressDataList.get(0).getId());
                        //addressDataList.get(0).setAddress_id();
                        createOrder.setDelivery_address(addressData);

                        if (NetworkConnectionHelper.isOnline(AddressList.this)) {
                            final DataSource dataSource = new DataSource(AddressList.this);
                            dataSource.open();
                            List<ProductData> listProduct = dataSource.selectInto_tbl_Products("", "");
                            dataSource.close();
                            if (listProduct != null && listProduct.size() > 0) {
                                for (int i = 0; i < listProduct.size(); i++) {
                                    ProductData productData = new ProductData();
                                    productData.setProduct_id(listProduct.get(i).getId());
                                    productData.setProduct_quantity_id(listProduct.get(i).getQuantity_id());
                                    productData.setProduct_selling_price(listProduct.get(i).getSelling_price());
                                    productData.setProduct_discount(listProduct.get(i).getDiscount());
                                    productData.setSelected_quantity(listProduct.get(i).getSelected_quantity());
                                    createOrder.getProducts().add(productData);
                                }
                            }
                            BillingData billingData = new BillingData();
                            if (rb_COD.isChecked()) {
                                billingData.setPay_mode("COD");
                            }
                            billingData.setActual_price(cart_price_total);
                            if (ch_wallet.isChecked()) {
                                billingData.setWallet_used("yes");
                                billingData.setWallet_money(walletAmount);
                            } else {
                                billingData.setWallet_used("no");
                                billingData.setWallet_money("0");
                            }
                            billingData.setCashback_applied("0");
                            billingData.setCashback_percent("0");
                            billingData.setCashback_amount("0");
                            billingData.setTotal_amount_payable(cart_price_payable);
                            billingData.setDelivery_type("standard");
                            billingData.setDelivery_time(new SimpleDateFormat("hh:mm:ss", Locale.ENGLISH).format(new Date()));
                            billingData.setDelivery_date(new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(new Date()));
                            billingData.setCgst(cgstper);
                            billingData.setSgst(sgstper);
                            billingData.setSpinner_percent(Spinner_percentage);

                            if (list_AddedCouponData.size() == 0) {
                                billingData.setCoupan_used("no");
                                billingData.setCoupan_id("");
                                billingData.setCoupan_code("");
                                billingData.setCoupan_price("");
                            } else {
                                billingData.setCoupan_used("yes");
                                billingData.setCoupan_id(list_AddedCouponData.get(0).getId());
                                billingData.setCoupan_code(list_AddedCouponData.get(0).getCoupon_number());
                                billingData.setCoupan_price(list_AddedCouponData.get(0).getAmount());
                            }
                            createOrder.setBilling_detail(billingData);
                            createOrder.setCustomer_id(Constants.getSavedPreferences(AddressList.this, ID, null));
                            createOrder.setCity_id(Constants.getSavedPreferences(AddressList.this, CITY_ID, null));
                            final ProgressDialog progressDialog = new ProgressDialog(AddressList.this);
                            progressDialog.setMessage("Loading...");
                            progressDialog.show();
                            String url = "create-order";
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, Base_URl + url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    progressDialog.dismiss();
                                    //Toast.makeText(AddressList.this, response, Toast.LENGTH_SHORT).show();
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        if (jsonObject.getBoolean("status")) {
                                            int i = 0;
                                            ProductData data = ProductData.createJsonInObject(jsonObject.getString("data"));
                                            if (data != null) {
                                                /*for (ProductData productData : dataList) {*/
                                                DataSource dataSource1 = new DataSource(AddressList.this);
                                                dataSource1.open();
                                                i = dataSource1.deleteInto_tbl_Products("", "");
                                                dataSource1.close();
                                                /*}*/
                                                Toast.makeText(AddressList.this, "Product Order Created SuccessFully!!", Toast.LENGTH_SHORT).show();
                                                AlertDialog.Builder builder = new AlertDialog.Builder(AddressList.this);
                                                builder.setMessage("Your order created successfully...\n" +
                                                        "invoice_id :     " + data.getInvoice_id());
                                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                        offer_preference.edit().clear().commit();
                                                        startActivity(new Intent(AddressList.this, Dashboard.class)
                                                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                                    }
                                                });
                                                builder.show();

                                            } else {
                                                Toast.makeText(AddressList.this, "Some error occurred!Please try again.", Toast.LENGTH_SHORT).show();
                                            }

                                        } else {
                                            Toast.makeText(AddressList.this, "Some error occurred!!!", Toast.LENGTH_SHORT).show();

                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    progressDialog.dismiss();
                                    Toast.makeText(AddressList.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }) {
                                @Override
                                public byte[] getBody() throws com.android.volley.AuthFailureError {
                                    String str = new Gson().toJson(createOrder);
                                    return str.getBytes();
                                }

                                public String getBodyContentType() {
                                    return "application/json; charset=utf-8";
                                }
                            };
                            RequestQueue requestQueue = Volley.newRequestQueue(AddressList.this);
                            requestQueue.add(stringRequest);
                        } else {
                            Toast.makeText(AddressList.this, "Please check your internet connection! try again...", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setCancelable(false);
                builder.show();
                        }
        });

        if (ch_wallet.isChecked()) {
            ll_useWalletAmt.setVisibility(View.VISIBLE);
        } else {
            ll_useWalletAmt.setVisibility(View.GONE);
        }
        ch_wallet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (totalWalletAmount != null && totalWalletAmount.equalsIgnoreCase("0")) {
                    Toast.makeText(AddressList.this, "Your wallet Amount is 0.", Toast.LENGTH_SHORT).show();
                    ch_wallet.setChecked(false);
                } else {
                    if (ch_wallet.isChecked()) {
                        ll_useWalletAmt.setVisibility(View.VISIBLE);
                    } else {
                        ll_useWalletAmt.setVisibility(View.GONE);
                        et_useWalletAmount.setText("0");
                        walletAmount = "0";
                        txt_TotalWalletAmount.setText(getResources().getString(R.string.sym_rs) + " " + totalWalletAmount + "");
                        txt_walletAmount.setText(getResources().getString(R.string.sym_rs) +"0");
                      //  txt_walletAmount.setText(getResources().getString(R.string.sym_rs) + " " + (Double.parseDouble(totalWalletAmount) - Double.parseDouble(walletAmount)));
                        paybleAmount = ((Double.parseDouble(totalPrice) - Double.parseDouble(walletAmount) - Double.parseDouble(CouponAmount)) + (Double.parseDouble(cgst) + Double.parseDouble(sgst))) + "";
                        txt_payableAmount.setText(paybleAmount);
                    }
                }
            }
        });
        btn_walletAmtAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_useWalletAmount.getText().toString().trim().equalsIgnoreCase("")) {
                    Toast.makeText(AddressList.this, "Please enter wallet amount", Toast.LENGTH_SHORT).show();
                } else if (Double.parseDouble(totalWalletAmount) == 0) {
                    Toast.makeText(AddressList.this, "Your wallet Amount is 0.", Toast.LENGTH_SHORT).show();
                } else if (Double.parseDouble(et_useWalletAmount.getText().toString()) > Double.parseDouble(totalWalletAmount)) {
                    Toast.makeText(AddressList.this, "You are entered wrong amount", Toast.LENGTH_SHORT).show();
                } else if (Double.parseDouble(et_useWalletAmount.getText().toString()) > Double.parseDouble(totalPrice)) {
                    Toast.makeText(AddressList.this, "You are entered wrong amount", Toast.LENGTH_SHORT).show();
                } else if (Double.parseDouble(et_useWalletAmount.getText().toString()) > ((Double.parseDouble(totalPrice) * 25) / 100)) {
                    Toast.makeText(AddressList.this, "you can use 25% of total amount in your wallet amount", Toast.LENGTH_SHORT).show();
                } else {
                    walletAmount = et_useWalletAmount.getText().toString().trim();
                    txt_walletAmount.setText(getResources().getString(R.string.sym_rs)+" "+walletAmount);
                    txt_TotalWalletAmount.setText(getResources().getString(R.string.sym_rs) + " " + (Double.parseDouble(totalWalletAmount) - Double.parseDouble(walletAmount)));
                    paybleAmount = ((Double.parseDouble(totalPrice) - Double.parseDouble(walletAmount) - Double.parseDouble(CouponAmount)) + (Double.parseDouble(cgst) + Double.parseDouble(sgst))) + "";
                    txt_payableAmount.setText(paybleAmount);
                }
            }
        });

        btn_AddCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (btn_AddCoupon.getText().toString().equalsIgnoreCase("Remove")) {
                    btn_AddCoupon.setText("Add");
                    list_AddedCouponData.remove(0);
                    CouponAmount = "0";
                    ll_couponDetails.setVisibility(View.GONE);
                    txt_CouponAmount.setText(getResources().getString(R.string.sym_rs) + " 0");
                    paybleAmount = ((Double.parseDouble(totalPrice) - Double.parseDouble(walletAmount) - Double.parseDouble(CouponAmount)) + (Double.parseDouble(cgst) + Double.parseDouble(sgst))) + "";
                    txt_payableAmount.setText(paybleAmount);
                } else {
                    if (list_AddedCouponData != null && list_AddedCouponData.size() == 0) {
                        final ProgressDialog progressDialog = new ProgressDialog(AddressList.this);
                        progressDialog.show();
                        progressDialog.setMessage("Loading...");
                        String url = "coupon-list";
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, Base_URl + url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                progressDialog.dismiss();
                                //  Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if (jsonObject.getBoolean("status")) {
                                        String jsonInString = jsonObject.getString("data");
                                        final List<CouponData> list_CouponData = new ArrayList<>();
                                        CouponData couponData = CouponData.createJsonInObject(jsonInString);
                                        if (couponData.getAvailable() != null && couponData.getAvailable().size() > 0) {
                                            for (CouponData couponData1 : couponData.getAvailable()) {
                                                list_CouponData.add(couponData1);
                                            }
                                        } else {
                                            Toast.makeText(AddressList.this, "Coupon not Available...", Toast.LENGTH_SHORT).show();
                                        }
                                        /*if (couponData.getExpired() != null && couponData.getExpired().size() > 0) {
                                            for (CouponData couponData1 : couponData.getExpired()) {
                                                list_CouponData.add(couponData1);
                                            }
                                        }*/
                                        if (list_CouponData.size() > 0) {
                                            GridLayoutManager gridLayoutManager = new GridLayoutManager(AddressList.this, 1);
                                            View view = LayoutInflater.from(AddressList.this).inflate(R.layout.dialog_for_coupon, null, false);
                                            ImageView iv_cancel = view.findViewById(R.id.iv_cancel);
                                            RecyclerView rv_Coupon = view.findViewById(R.id.rv_Coupon);
                                            rv_Coupon.setHasFixedSize(true);
                                            rv_Coupon.setLayoutManager(gridLayoutManager);
                                            AlertDialog.Builder builder = new AlertDialog.Builder(AddressList.this);
                                            builder.setView(view);
                                            final AlertDialog alertDialog = builder.show();
                                            RecyclerView.Adapter adapter = new RecyclerView.Adapter<AddressList.ViewHolder>() {
                                                @NonNull
                                                @Override
                                                public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                                                    return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_for_coupon, viewGroup, false));
                                                }
                                                @Override
                                                public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
                                                    viewHolder.tv_couponNo.setText(list_CouponData.get(i).getCoupon_number());
                                                    viewHolder.tv_Amount.setText(list_CouponData.get(i).getAmount());
                                                    viewHolder.tv_valid.setText(list_CouponData.get(i).getValidity_start() + " To " + list_CouponData.get(i).getValidity_end());
                                                    viewHolder.tv_Availability.setText(list_CouponData.get(i).getAvailability());
                                                    if (list_CouponData.get(i).getUsable() != null && !list_CouponData.get(i).getUsable().equalsIgnoreCase("")) {
                                                        viewHolder.tv_Usable.setText(list_CouponData.get(i).getUsable());
                                                    } else {
                                                        viewHolder.ll_uses.setVisibility(View.GONE);
                                                    }
                                                    if (list_AddedCouponData != null && list_AddedCouponData.size() == 0) {
                                                        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                list_AddedCouponData.add(list_CouponData.get(i));
                                                                notifyDataSetChanged();
                                                                if (list_AddedCouponData != null && list_AddedCouponData.size() > 0) {
                                                                    final ProgressDialog progressDialog = new ProgressDialog(AddressList.this);
                                                                    progressDialog.show();
                                                                    progressDialog.setMessage("Loading...");
                                                                    String url = "coupon-list";
                                                                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Base_URl + url, new Response.Listener<String>() {
                                                                        @Override
                                                                        public void onResponse(String response) {
                                                                            progressDialog.dismiss();
                                                                            //  Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
                                                                            try {
                                                                                JSONObject jsonObject = new JSONObject(response);
                                                                                if (jsonObject.getBoolean("status")) {
                                                                                    if ((Double.parseDouble(totalPrice) - Double.parseDouble(walletAmount) - Double.parseDouble("0")) + (Double.parseDouble(cgst) + Double.parseDouble(sgst)) >= Double.parseDouble(list_AddedCouponData.get(0).getAmount())) {
                                                                                        ll_couponDetails.setVisibility(View.VISIBLE);
                                                                                        CouponAmount = list_AddedCouponData.get(0).getAmount();
                                                                                        tv_couponNo.setText(list_AddedCouponData.get(0).getCoupon_number());
                                                                                        tv_CouponAmountView.setText(getResources().getString(R.string.sym_rs) + " " + list_AddedCouponData.get(0).getAmount());
                                                                                        txt_CouponAmount.setText(getResources().getString(R.string.sym_rs) + " " + list_AddedCouponData.get(0).getAmount());
                                                                                        paybleAmount = ((Double.parseDouble(totalPrice) - Double.parseDouble(walletAmount) - Double.parseDouble(CouponAmount)) + (Double.parseDouble(cgst) + Double.parseDouble(sgst))) + "";
                                                                                        txt_payableAmount.setText(paybleAmount);
                                                                                        alertDialog.dismiss();
                                                                                        btn_AddCoupon.setText("Remove");
                                                                                    } else {
                                                                                        list_AddedCouponData.clear();
                                                                                        Toast.makeText(AddressList.this, "Your coupon amount is greater than your Payable Amount.\nYou can not use this Coupon.", Toast.LENGTH_LONG).show();
                                                                                        alertDialog.dismiss();
                                                                                    }
                                                                                } else {
                                                                                    Toast.makeText(AddressList.this, jsonObject.getString("message") + "", Toast.LENGTH_SHORT).show();
                                                                                }
                                                                            } catch (JSONException e) {
                                                                                e.printStackTrace();
                                                                            }
                                                                        }
                                                                    }, new Response.ErrorListener() {
                                                                        @Override
                                                                        public void onErrorResponse(VolleyError error) {
                                                                            progressDialog.dismiss();
                                                                            Toast.makeText(AddressList.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    }) {
                                                                        @Override
                                                                        protected Map<String, String> getParams() throws AuthFailureError {
                                                                            HashMap<String, String> params = new HashMap<>();
                                                                            params.put("coupon_code", list_AddedCouponData.get(0).getCoupon_number());
                                                                            params.put("user_id", Constants.getSavedPreferences(AddressList.this, ID, null));
                                                                            return params;
                                                                        }
                                                                    };
                                                                    RequestQueue requestQueue = Volley.newRequestQueue(AddressList.this);
                                                                    requestQueue.add(stringRequest);
                                                                }
                                                            }
                                                        });
                                                    }
                                                }

                                                @Override
                                                public int getItemCount() {
                                                    return list_CouponData.size();
                                                }
                                            };
                                            rv_Coupon.setAdapter(adapter);

                                            iv_cancel.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    alertDialog.dismiss();
                                                }
                                            });

                                        } else {

                                        }
                                    }

                                } catch (JSONException e1) {
                                    e1.printStackTrace();
                                }
                            }
                        },new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progressDialog.dismiss();
                                Toast.makeText(AddressList.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                HashMap<String, String> params = new HashMap<>();
                                params.put("user_id", Constants.getSavedPreferences(AddressList.this, ID, null));
                                return params;
                            }
                        };
                        RequestQueue requestQueue = Volley.newRequestQueue(AddressList.this);
                        requestQueue.add(stringRequest);
                    } else {
                        Toast.makeText(AddressList.this, "Coupon Already Added...", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    private void findViewByIds() {
        rv_address = findViewById(R.id.rv_address);
        tv_NoAddress = findViewById(R.id.tv_NoAddress);
        btn_AddAddress = findViewById(R.id.btn_AddAddress);
        ll_ifAddressIsAvailable = findViewById(R.id.ll_ifAddressIsAvailable);
        rb_COD = findViewById(R.id.rb_COD);
        rb_payNow = findViewById(R.id.rb_payNow);
        btn_CreateOrder = findViewById(R.id.btn_CreateOrder);

        txt_TotalAmount = findViewById(R.id.txt_TotalAmount);
        txt_walletAmount = findViewById(R.id.txt_walletAmount);
        txt_CouponAmount = findViewById(R.id.txt_CouponAmount);
        txt_payableAmount = findViewById(R.id.txt_payableAmount);
        txt_GST = findViewById(R.id.txt_GST);

        txt_TotalWalletAmount = findViewById(R.id.txt_TotalWalletAmount);
        ch_wallet = findViewById(R.id.ch_wallet);
        ll_useWalletAmt = findViewById(R.id.ll_useWalletAmt);
        et_useWalletAmount = findViewById(R.id.et_useWalletAmount);
        btn_walletAmtAdd = findViewById(R.id.btn_walletAmtAdd);

        btn_AddCoupon = findViewById(R.id.btn_AddCoupon);
        ll_couponDetails = findViewById(R.id.ll_couponDetails);
        tv_couponNo = findViewById(R.id.tv_couponNo);
        tv_CouponAmountView = findViewById(R.id.tv_CouponAmountView);
    }

    public void getAddressData() {
        final ProgressDialog progressDialog = new ProgressDialog(AddressList.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        String url = "view-address";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Base_URl + url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                //Toast.makeText(AddressList.this, response, Toast.LENGTH_SHORT).show();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean("status")) {
                        String cityList = jsonObject.getString("data");
                        if (cityList != null && !cityList.equalsIgnoreCase("{}")) {
                            addressDataList = AddressData.createJsonInList(cityList);
                            if (addressDataList != null && addressDataList.size() > 0) {
                                rv_address.setVisibility(View.VISIBLE);
                                tv_NoAddress.setVisibility(View.GONE);
                                adapterForAddress = new AdapterForAddress(AddressList.this, addressDataList, totalPrice);
                                rv_address.setAdapter(adapterForAddress);
                                ll_ifAddressIsAvailable.setVisibility(View.VISIBLE);
                                btn_AddAddress.setVisibility(View.GONE);
                            } else {
                                btn_AddAddress.setVisibility(View.VISIBLE);
                                ll_ifAddressIsAvailable.setVisibility(View.GONE);
                                rv_address.setVisibility(View.GONE);
                                tv_NoAddress.setVisibility(View.VISIBLE);
                            }
                        } else {
                            btn_AddAddress.setVisibility(View.VISIBLE);
                            ll_ifAddressIsAvailable.setVisibility(View.GONE);
                            rv_address.setVisibility(View.GONE);
                            tv_NoAddress.setVisibility(View.VISIBLE);
                            Toast.makeText(AddressList.this, "Data not found!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        btn_AddAddress.setVisibility(View.VISIBLE);
                        ll_ifAddressIsAvailable.setVisibility(View.GONE);
                        Toast.makeText(AddressList.this, "Some error occurred!!!", Toast.LENGTH_SHORT).show();
                        rv_address.setVisibility(View.GONE);
                        tv_NoAddress.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    rv_address.setVisibility(View.GONE);
                    tv_NoAddress.setVisibility(View.VISIBLE);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(AddressList.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
//                params.put("customer_id", Constants.getSavedPreferences(AddressList.this, ID, ""));
                params.put("customer_id", "1");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(AddressList.this);
        requestQueue.add(stringRequest);
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_address, menu);
        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        } else if (id == R.id.opt_add_address) {
            //alertAdd_And_DeleteAddress(AddressList.this, null);
        }
        return super.onOptionsItemSelected(item);
    }

    public static void alertAdd_And_DeleteAddress1(final Context context, AddressData addressData, final String totalPrice) {
        GpsTrackerForGoogleMap gpsTrackerForGoogleMap = new GpsTrackerForGoogleMap(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_add_edit_address, null, false);
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context, R.style.Theme_AppCompat_Light);
        alertDialog.setView(view);

        // AlertDialog dialog1 = alertDialog.create();

        final AlertDialog dialog = alertDialog.create();
        ColorDrawable back = new ColorDrawable(Color.WHITE);
        InsetDrawable inset = new InsetDrawable(back, 20);
        dialog.getWindow().setBackgroundDrawable(inset);

        dialog.show();
        //final AlertDialog dialog = alertDialog.show();
        TextView tv_header = view.findViewById(R.id.tv_header);
        final EditText et_firstname = view.findViewById(R.id.et_firstname);
        final EditText et_lastname = view.findViewById(R.id.et_lastname);
        final EditText et_houseNo = view.findViewById(R.id.et_houseNo);
        final EditText et_apartname = view.findViewById(R.id.et_apartname);
        final EditText et_street = view.findViewById(R.id.et_street);
        final EditText et_landmark = view.findViewById(R.id.et_landmark);
        final Spinner sp_state = view.findViewById(R.id.sp_state);
        final Spinner sp_city = view.findViewById(R.id.sp_city);
        final EditText et_pincode = view.findViewById(R.id.et_pincode);
        final EditText et_contact = view.findViewById(R.id.et_contact);
        final RadioButton rb_home = view.findViewById(R.id.rb_home);
        final RadioButton rb_office = view.findViewById(R.id.rb_office);
        final RadioButton rb_other = view.findViewById(R.id.rb_other);
        final ImageView iv_cancel = view.findViewById(R.id.iv_cancel);
        TextView txt_updateAndAdd = view.findViewById(R.id.txt_updateAndAdd);

        iv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        if (addressData != null) {
            tv_header.setText("Update Your Address");
            et_firstname.setText(addressData.getFirst_name() + "");
            et_lastname.setText(addressData.getLast_name() + "");
            et_houseNo.setText(addressData.getHouse_number() + "");
            et_apartname.setText(addressData.getApartment() + "");
            et_street.setText(addressData.getStreet_detail() + "");
            et_landmark.setText(addressData.getLandmark() + "");
            //et_city.setText(addressData.getCity_name() + "");
            et_pincode.setText(addressData.getPincode() + "");
            et_contact.setText(addressData.getMobile() + "");
            new VolleyForStateAndCity(context, "34", "city-list", sp_city, addressData.getCity_name());
            /*
             * for State
             *
             * */
           /* new VolleyForStateAndCity(context, "", "state-list", sp_state);
            sp_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (!sp_state.getSelectedItem().toString().trim().equalsIgnoreCase("--Select--")) {
                        new VolleyForStateAndCity(context, VolleyForStateAndCity.hashMapForState.get(sp_state.getSelectedItem().toString().trim()), "city-list", sp_city);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });*/
            if (addressData.getType() != null && addressData.getType().equalsIgnoreCase("home")) {
                rb_home.setChecked(true);
                rb_office.setEnabled(false);
                rb_other.setEnabled(false);
                addressData.setType("home");
            } else if (addressData.getType() != null && addressData.getType().equalsIgnoreCase("office")) {
                rb_home.setEnabled(false);
                rb_office.setChecked(true);
                rb_other.setEnabled(false);
                addressData.setType("office");
            } else if (addressData.getType() != null && addressData.getType().equalsIgnoreCase("other")) {
                rb_home.setEnabled(false);
                rb_office.setEnabled(false);
                rb_other.setChecked(true);
                addressData.setType("other");
            }
            txt_updateAndAdd.setText("Update Address");
            addressData.setAction("edit");
        } else {
            tv_header.setText("Add Address");
            txt_updateAndAdd.setText("Add Address");
            addressData = new AddressData();
            addressData.setAction("add");
            new VolleyForStateAndCity(context, "34", "city-list", sp_city, "");
        }
        final AddressData finalAddressData = addressData;
        txt_updateAndAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_firstname.getText().toString().trim().equalsIgnoreCase("")) {
                    et_firstname.setError("Empty! Fill This.");
                    et_firstname.requestFocus();
                } else if (et_lastname.getText().toString().trim().equalsIgnoreCase("")) {
                    et_lastname.setError("Empty! Fill This.");
                    et_lastname.requestFocus();
                } else if (et_houseNo.getText().toString().trim().equalsIgnoreCase("")) {
                    et_houseNo.setError("Empty! Fill This.");
                    et_houseNo.requestFocus();
                } else if (et_apartname.getText().toString().trim().equalsIgnoreCase("")) {
                    et_apartname.setError("Empty! Fill This.");
                    et_apartname.requestFocus();
                } else if (et_street.getText().toString().trim().equalsIgnoreCase("")) {
                    et_street.setError("Empty! Fill This.");
                    et_street.requestFocus();
                } else if (et_landmark.getText().toString().trim().equalsIgnoreCase("")) {
                    et_landmark.setError("Empty! Fill This.");
                    et_landmark.requestFocus();
                }/* else if (sp_state.getSelectedItem().toString().trim().equalsIgnoreCase("--select--")) {
                    Toast.makeText(context, "Please Select State!!!", Toast.LENGTH_SHORT).show();
                }*/ else if (sp_city.getSelectedItem().toString().trim().equalsIgnoreCase("--select--")) {
                    Toast.makeText(context, "Please Select City!!!", Toast.LENGTH_SHORT).show();
                } else if (et_pincode.getText().toString().trim().equalsIgnoreCase("")) {
                    et_pincode.setError("Empty! Fill This.");
                    et_pincode.requestFocus();
                } else if (et_contact.getText().toString().trim().equalsIgnoreCase("")) {
                    et_contact.setError("Empty! Fill This.");
                    et_contact.requestFocus();
                } else if (rb_home.isChecked() == false && rb_office.isChecked() == false && rb_other.isChecked() == false) {
                    Toast.makeText(context, "Please select address type!!!", Toast.LENGTH_SHORT).show();
                } else {
                    SharedPreferences sharedLocation = context.getSharedPreferences("AppLocation", 0);
                    //sharedLocation.getString("latitude", "0.0");
                    //sharedLocation.getString("longitude", "0.0");
                    finalAddressData.setFirst_name(et_firstname.getText().toString().trim());
                    finalAddressData.setLast_name(et_lastname.getText().toString().trim());
                    finalAddressData.setApartment(et_apartname.getText().toString().trim());
                    finalAddressData.setHouse_number(et_houseNo.getText().toString().trim());
                    finalAddressData.setStreet_detail(et_street.getText().toString().trim());
                    finalAddressData.setCity_name(sp_city.getSelectedItem().toString().trim());
                    finalAddressData.setCity_id(VolleyForStateAndCity.hashMapForCity.get(sp_city.getSelectedItem().toString().trim()));
                    finalAddressData.setLandmark(et_landmark.getText().toString().trim());
                    finalAddressData.setPincode(et_pincode.getText().toString().trim());
                    finalAddressData.setMobile(et_contact.getText().toString().trim());
                    finalAddressData.setLatitude(sharedLocation.getString("latitude", "0.0"));
                    finalAddressData.setLongitude(sharedLocation.getString("longitude", "0.0"));
                    finalAddressData.setCustomer_id(Constants.getSavedPreferences(context, ID, null));

                    if (finalAddressData.getId() == null) {
                        finalAddressData.setId("");
                    }
                    if (rb_home.isChecked()) {
                        finalAddressData.setType("home");
                    } else if (rb_office.isChecked()) {
                        finalAddressData.setType("office");
                    } else if (rb_other.isChecked()) {
                        finalAddressData.setType("other");
                    }

                    final ProgressDialog progressDialog = new ProgressDialog(context);
                    progressDialog.setMessage("Loading");
                    progressDialog.show();
                    String url = "create-address";
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Base_URl + url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            //Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getBoolean("status") == true) {
                                    String message = jsonObject.getString("message");
                                    Toast.makeText(context, message + "", Toast.LENGTH_SHORT).show();
                                    context.startActivity(new Intent(context, context.getClass()).putExtra("totalPrice", totalPrice));
                                    ((Activity) context).finish();
                                    dialog.dismiss();
                                } else {
                                    String message = jsonObject.getString("message");
                                    Toast.makeText(context, message + "", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            HashMap<String, String> params = new HashMap<>();
                            params.put("address_id", finalAddressData.getId());
                            params.put("firstname", finalAddressData.getFirst_name());
                            params.put("lastname", finalAddressData.getLast_name());
                            params.put("mobile", finalAddressData.getMobile());
                            params.put("house_number", finalAddressData.getHouse_number());
                            params.put("apartment", finalAddressData.getApartment());
                            params.put("street_detail", finalAddressData.getStreet_detail());
                            params.put("landmark", finalAddressData.getLandmark());
                            params.put("city_id", finalAddressData.getCity_id());
                            params.put("pincode", finalAddressData.getPincode());
                            params.put("customer_id", finalAddressData.getCustomer_id());
                            params.put("latitude", finalAddressData.getLatitude());
                            params.put("longitude", finalAddressData.getLongitude());
                            params.put("address_type", finalAddressData.getType());
                            params.put("action", finalAddressData.getAction());
                            return params;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(context);
                    requestQueue.add(stringRequest);
                }
            }
        });

    }

    void getWalletAmount() {
        final ProgressDialog progressDialog = new ProgressDialog(AddressList.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        String url = "general-setting";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Base_URl + url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                //Toast.makeText(AddressList.this, response, Toast.LENGTH_SHORT).show();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean("status")) {
                        String data = jsonObject.getString("data");
                        if (data != null && !data.equalsIgnoreCase("{}")) {
                            WalletData walletData = WalletData.createJsonInList(data);
                            if (walletData != null) {
                                totalWalletAmount = walletData.getWallet_amount();
                                txt_TotalWalletAmount.setText(getResources().getString(R.string.sym_rs) + " " + totalWalletAmount + "");
                                cgstper = "0";
                                sgstper = "0";
                                if (walletData.getOrder_setting().getSgst() != null && !walletData.getOrder_setting().getSgst().equalsIgnoreCase("")) {
                                    sgst = ((Double.parseDouble(totalPrice) * Double.parseDouble(walletData.getOrder_setting().getSgst())) / 100) + "";
                                    sgstper = walletData.getOrder_setting().getSgst();
                                } else {
                                    sgst = "0";
                                }
                                if (walletData.getOrder_setting().getCgst() != null && !walletData.getOrder_setting().getCgst().equalsIgnoreCase("")) {
                                    cgst = ((Double.parseDouble(totalPrice) * Double.parseDouble(walletData.getOrder_setting().getCgst())) / 100) + "";
                                    cgstper = walletData.getOrder_setting().getCgst();
                                } else {
                                    cgst = "0";
                                }
                                txt_GST.setText(getResources().getString(R.string.sym_rs) + " " + (Double.parseDouble(cgst) + Double.parseDouble(sgst)) + "  (" + (Double.parseDouble(cgstper) + Double.parseDouble(sgstper)) + "%)");
                                txt_walletAmount.setText(getResources().getString(R.string.sym_rs) + " " + walletAmount);

                                paybleAmount = ((Double.parseDouble(totalPrice) - Double.parseDouble("0") - Double.parseDouble(CouponAmount)) + (Double.parseDouble(cgst) + Double.parseDouble(sgst))) + "";
                                txt_payableAmount.setText(paybleAmount);
                            } else {
                                Toast.makeText(AddressList.this, "Some error ouccerred...", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        Toast.makeText(AddressList.this, "Some error ouccerred...", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(AddressList.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("user_id", Constants.getSavedPreferences(AddressList.this, ID, ""));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(AddressList.this);
        requestQueue.add(stringRequest);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_couponNo,
                tv_Amount,
                tv_valid,
                tv_Availability,
                tv_Usable;
        LinearLayout ll_uses;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_couponNo = itemView.findViewById(R.id.tv_couponNo);
            tv_Amount = itemView.findViewById(R.id.tv_Amount);
            tv_valid = itemView.findViewById(R.id.tv_valid);
            tv_Availability = itemView.findViewById(R.id.tv_Availability);
            tv_Usable = itemView.findViewById(R.id.tv_Usable);
            ll_uses = itemView.findViewById(R.id.ll_uses);
        }
    }



    private void getPaymentApi() {
        String getFname = "anoop";
        String getPhone = "9696381023";
        String getEmail = "anoop@sdhinfotech.com";
        String getAmt   = totalPrice;//rechargeAmt.getText().toString().trim();

        Intent intent = new Intent(getApplicationContext(), PayMentGateWay.class);
        intent.putExtra("FIRST_NAME",getFname);
        intent.putExtra("PHONE_NUMBER",getPhone);
        intent.putExtra("EMAIL_ADDRESS",getEmail);
        intent.putExtra("RECHARGE_AMT",getAmt);
        startActivity(intent);
    }



    //    *******************************Razorpay Integration code***********************************************

    public void startPayment() {
        /*
          You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        final Activity activity = this;
        final Checkout co = new Checkout();
        try {
            JSONObject options = new JSONObject();
            options.put("name", "Tarve Mart Payment");
            options.put("description", "Tarve Mart Charge");
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
//            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("currency", "INR");
            send_amount=txt_payableAmount.getText().toString();
            long lon = Math.round(Float.parseFloat(send_amount));
            options.put("amount",(lon*100));
//            new BigDecimal(send_amount);
//            Integer result = BigDecimal.ROUND_HALF_UP;
            JSONObject preFill = new JSONObject();
            preFill.put("email", "tarvemart2020@gmail.com");
            preFill.put("contact", "7390904736");
            options.put("prefill", preFill);

            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
    /**
     * The name of the function has to be
     * onPaymentSuccess
     * Wrap your code in try catch, as shown, to ensure that this method runs correctly
     */
    @SuppressWarnings("unused")
    public void onPaymentSuccess(String razorpayPaymentID) {
        try {
            AfterPayment();
            Transaction_id=razorpayPaymentID;
           // BookingConfirmServiceImple();
            Toast.makeText(this, "Payment Successful: " + razorpayPaymentID, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentSuccess", e);
        }
    }
    /**
     * The name of the function has to be
     * onPaymentError
     * Wrap your code in try catch, as shown, to ensure that this method runs correctly
     */
    @SuppressWarnings("unused")
    public void onPaymentError(int code, String res_response) {
        try {
            Intent intent=new Intent(getApplicationContext(),Dashboard.class);
            startActivity(intent);
            succ_fail_response= res_response;
            Toast.makeText(this, "Payment failed: " + code + " " + res_response, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentError", e);
        }
    }
//    Razorpay Integration code



    public void AfterPayment(){
            AlertDialog.Builder builder = new AlertDialog.Builder(AddressList.this);
            builder.setMessage("Are you sure you want to confirm your order?");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //Toast.makeText(AddressList.this, "Work in Progress", Toast.LENGTH_SHORT).show();
                    final CreateOrder createOrder = new CreateOrder();
                    AddressData addressData = new AddressData();
                    addressData.setAddress_id(addressDataList.get(0).getId());
                    //addressDataList.get(0).setAddress_id();
                    createOrder.setDelivery_address(addressData);

                    if (NetworkConnectionHelper.isOnline(AddressList.this)) {
                        final DataSource dataSource = new DataSource(AddressList.this);
                        dataSource.open();
                        List<ProductData> listProduct = dataSource.selectInto_tbl_Products("", "");
                        dataSource.close();
                        if (listProduct != null && listProduct.size() > 0) {
                            for (int i = 0; i < listProduct.size(); i++) {
                                ProductData productData = new ProductData();
                                productData.setProduct_id(listProduct.get(i).getId());
                                productData.setProduct_quantity_id(listProduct.get(i).getQuantity_id());
                                productData.setProduct_selling_price(listProduct.get(i).getSelling_price());
                                productData.setProduct_discount(listProduct.get(i).getDiscount());
                                productData.setSelected_quantity(listProduct.get(i).getSelected_quantity());
                                createOrder.getProducts().add(productData);
                            }
                        }
                        BillingData billingData = new BillingData();
//                        if (rb_payNow.isChecked()) {
                            billingData.setPay_mode("card_payment");
//                        }
                        billingData.setActual_price(cart_price_total);
                        if (ch_wallet.isChecked()) {
                            billingData.setWallet_used("yes");
                            billingData.setWallet_money(walletAmount);
                        } else {
                            billingData.setWallet_used("no");
                            billingData.setWallet_money("0");
                        }
                        billingData.setCashback_applied("0");
                        billingData.setCashback_percent("0");
                        billingData.setCashback_amount("0");
                        billingData.setTotal_amount_payable(cart_price_payable);
                        billingData.setDelivery_type("standard");
                        billingData.setDelivery_time(new SimpleDateFormat("hh:mm:ss", Locale.ENGLISH).format(new Date()));
                        billingData.setDelivery_date(new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(new Date()));
                        billingData.setCgst(cgstper);
                        billingData.setSgst(sgstper);
                        billingData.setTxtId(Transaction_id);
                        billingData.setPayment_datetime(show_date_time);
                        billingData.setPayment_status("success");
                        billingData.setSpinner_percent(Spinner_percentage);
                       // billingData.setPayment_status(succ_fail_response);


                        if (list_AddedCouponData.size() == 0) {
                            billingData.setCoupan_used("no");
                            billingData.setCoupan_id("");
                            billingData.setCoupan_code("");
                            billingData.setCoupan_price("");
                        } else {
                            billingData.setCoupan_used("yes");
                            billingData.setCoupan_id(list_AddedCouponData.get(0).getId());
                            billingData.setCoupan_code(list_AddedCouponData.get(0).getCoupon_number());
                            billingData.setCoupan_price(list_AddedCouponData.get(0).getAmount());
                        }
                        createOrder.setBilling_detail(billingData);
                        createOrder.setCustomer_id(Constants.getSavedPreferences(AddressList.this, ID, null));
                        createOrder.setCity_id(Constants.getSavedPreferences(AddressList.this, CITY_ID, null));
                        final ProgressDialog progressDialog = new ProgressDialog(AddressList.this);
                        progressDialog.setMessage("Loading...");
                        progressDialog.show();
                        String url = "create-order";
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, Base_URl + url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                progressDialog.dismiss();
                                //Toast.makeText(AddressList.this, response, Toast.LENGTH_SHORT).show();
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if (jsonObject.getBoolean("status")) {
                                        int i = 0;
                                        ProductData data = ProductData.createJsonInObject(jsonObject.getString("data"));
                                        if (data != null) {
                                            /*for (ProductData productData : dataList) {*/
                                            DataSource dataSource1 = new DataSource(AddressList.this);
                                            dataSource1.open();
                                            i = dataSource1.deleteInto_tbl_Products("", "");
                                            dataSource1.close();
                                            /*}*/
                                            Toast.makeText(AddressList.this, "Product Order Created SuccessFully!!", Toast.LENGTH_SHORT).show();
                                            AlertDialog.Builder builder = new AlertDialog.Builder(AddressList.this);
                                            builder.setMessage("Your order created successfully...\n" +
                                                    "invoice_id :     " + data.getInvoice_id());
                                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                    offer_preference.edit().clear().commit();
                                                    startActivity(new Intent(AddressList.this, Dashboard.class)
                                                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                                }
                                            });
                                            builder.show();

                                        } else {
                                            Toast.makeText(AddressList.this, "Some error occurred!Please try again.", Toast.LENGTH_SHORT).show();
                                        }

                                    } else {
                                        Toast.makeText(AddressList.this, "Some error occurred!!!", Toast.LENGTH_SHORT).show();

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progressDialog.dismiss();
                                Toast.makeText(AddressList.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        })
                        {
                            @Override
                            public byte[] getBody() throws com.android.volley.AuthFailureError {
                                String str = new Gson().toJson(createOrder);
                                return str.getBytes();
                            }

                            public String getBodyContentType() {
                                return "application/json; charset=utf-8";
                            }
                        };
                        RequestQueue requestQueue = Volley.newRequestQueue(AddressList.this);
                        requestQueue.add(stringRequest);
                    } else {
                        Toast.makeText(AddressList.this, "Please check your internet connection! try again...", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setCancelable(false);
            builder.show();
        }
}