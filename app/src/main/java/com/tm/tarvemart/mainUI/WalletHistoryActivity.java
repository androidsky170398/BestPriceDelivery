package com.tm.tarvemart.mainUI;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.tm.tarvemart.Adapter.AdapterForWalletHistory;
import com.tm.tarvemart.Helper.NetworkConnectionHelper;
import com.tm.tarvemart.R;
import com.tm.tarvemart.Util.Constants;
import com.tm.tarvemart.entity.WalletData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tm.tarvemart.Util.Constants.ID;
import static com.tm.tarvemart.inerface.GlobleInterfce.Base_URl;

public class WalletHistoryActivity extends AppCompatActivity {
    TextView tv_noData;
    RecyclerView rv_walletHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_history);
        findViewByIds();
        getSupportActionBar().setTitle("Wallet History");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        rv_walletHistory.setHasFixedSize(true);
        rv_walletHistory.setLayoutManager(new GridLayoutManager(WalletHistoryActivity.this,1));
        getwalletHistoryData();
    }

    private void findViewByIds() {
        rv_walletHistory = findViewById(R.id.rv_walletHistory);
        tv_noData = findViewById(R.id.tv_noData);
    }

    void getwalletHistoryData() {
        if (NetworkConnectionHelper.isOnline(WalletHistoryActivity.this)) {
            final ProgressDialog progressDialog = new ProgressDialog(WalletHistoryActivity.this);
            progressDialog.setMessage("Loading");
            progressDialog.show();
            String url = "wallet_history";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Base_URl + url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    progressDialog.dismiss();
                    //Toast.makeText(AddressList.this, response, Toast.LENGTH_SHORT).show();
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getBoolean("status")) {
                            List<WalletData> dataList = WalletData.createJsonInList1(jsonObject.getString("data"));
                            if (dataList != null && dataList.size() > 0) {
                                AdapterForWalletHistory adapter= new AdapterForWalletHistory(WalletHistoryActivity.this, dataList);
                                rv_walletHistory.setAdapter(adapter);
                                rv_walletHistory.setVisibility(View.VISIBLE);
                                tv_noData.setVisibility(View.GONE);
                            }
                        } else {
                            rv_walletHistory.setVisibility(View.GONE);
                            tv_noData.setVisibility(View.VISIBLE);
                            Toast.makeText(WalletHistoryActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Toast.makeText(WalletHistoryActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> params = new HashMap<>();
                    params.put("customer_id", Constants.getSavedPreferences(WalletHistoryActivity.this, ID, ""));
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(WalletHistoryActivity.this);
            requestQueue.add(stringRequest);
        } else {
            Toast.makeText(WalletHistoryActivity.this, "Please check your internet connection! try again...", Toast.LENGTH_SHORT).show();
        }
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
