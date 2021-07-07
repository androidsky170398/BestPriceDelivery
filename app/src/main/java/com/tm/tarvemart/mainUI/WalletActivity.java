package com.tm.tarvemart.mainUI;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tm.tarvemart.Helper.NetworkConnectionHelper;
import com.tm.tarvemart.R;
import com.tm.tarvemart.Util.Constants;
import com.tm.tarvemart.entity.WalletData;
import com.tm.tarvemart.inerface.GlobleInterfce;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.tm.tarvemart.Util.Constants.ID;

public class WalletActivity extends AppCompatActivity implements GlobleInterfce {
    TextView tv_TotalBalance, tv_history;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        findViewByIds();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("My Wallet");
        if (NetworkConnectionHelper.isOnline(WalletActivity.this
        )) {
            final ProgressDialog progressDialog = new ProgressDialog(WalletActivity.this);
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
                                    tv_TotalBalance.setText(getResources().getString(R.string.sym_rs) + " " + walletData.getWallet_amount());
                                } else {
                                    Toast.makeText(WalletActivity.this, "Some error ouccerred...", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else {
                            Toast.makeText(WalletActivity.this, "Some error ouccerred...", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Toast.makeText(WalletActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> params = new HashMap<>();
                    params.put("user_id", Constants.getSavedPreferences(WalletActivity.this, ID, ""));
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(WalletActivity.this);
            requestQueue.add(stringRequest);
        } else {
            Toast.makeText(WalletActivity.this, "Please check your internet connection! try again...", Toast.LENGTH_SHORT).show();
        }
        tv_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(WalletActivity.this, "Comming soon...", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(WalletActivity.this,WalletHistoryActivity.class);
                startActivity(intent);
            }
        });
    }

    private void findViewByIds() {
        tv_TotalBalance = findViewById(R.id.tv_TotalBalance);
        tv_history = findViewById(R.id.tv_history);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
