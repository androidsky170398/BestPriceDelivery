package com.tm.tarvemart.mainUI;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.TextInputEditText;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tm.tarvemart.Adapter.AdapterForBanner;
import com.tm.tarvemart.Helper.NetworkConnectionHelper;
import com.tm.tarvemart.LocationTrack.GPSTracker;
import com.tm.tarvemart.R;
import com.tm.tarvemart.entity.BannerData;
import com.tm.tarvemart.inerface.GlobleInterfce;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class SendOtpActivity extends AppCompatActivity implements GlobleInterfce {

    String url = "send-otp";
    TextInputEditText edt_mobileNo;
    Button btn_sendOtp;
    ViewPager viewPager_banner;
    private static int currentPage = 0;
    CirclePageIndicator indicator;
    List<BannerData> bannerData = new ArrayList<>();
    String bannerUrl = "bannerlist";
    double latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_otp);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Login");
        edt_mobileNo = findViewById(R.id.edt_mobileNo);
        btn_sendOtp = findViewById(R.id.btn_sendOtp);

        viewPager_banner = (ViewPager) findViewById(R.id.viewPager_banner);
        indicator = (CirclePageIndicator) findViewById(R.id.indicator);

        GPSTracker gpsTracker = new GPSTracker(SendOtpActivity.this);
        if (gpsTracker.canGetLocation()) {
            latitude = gpsTracker.getLatitude();
            longitude = gpsTracker.getLongitude();
        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gpsTracker.showSettingsAlert();
        }
        btn_sendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkConnectionHelper.isOnline(SendOtpActivity.this))
                    sendOtp();
                else
                    Toast.makeText(SendOtpActivity.this, "Please Check InterNet Connection", Toast.LENGTH_SHORT).show();
            }
        });
        getBanner();
        //Dashboard.checkNetConnection(SendOtpActivity.this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Dashboard.checkNetConnection(SendOtpActivity.this);
    }

    public void sendOtp() {
        final ProgressDialog progressDialog = new ProgressDialog(SendOtpActivity.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Base_URl + url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                //    Toast.makeText(SendOtpActivity.this, response, Toast.LENGTH_SHORT).show();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean("status")) {
                        Toast.makeText(SendOtpActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        if (jsonObject1.has("mobile")) {
                            //  JSONObject jsonObject1=jsonObject.getJSONObject("data");
                            startActivity(new Intent(SendOtpActivity.this, ValidateOtp.class).putExtra("mobileNo", jsonObject1.getString("mobile")));
                            finish();
                        }
                    } else {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        if (jsonObject1.has("mobile")) {
                            Toast.makeText(SendOtpActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SendOtpActivity.this, SignupActivity.class).putExtra("mobileNo", jsonObject1.getString("mobile")));
                        } else {
                            JSONObject jsonObject2 = jsonObject.getJSONObject("error_code");
                            if (jsonObject2.has("mobile_no"))
                            Toast.makeText(SendOtpActivity.this, jsonObject2.getString("mobile_no"), Toast.LENGTH_LONG).show();

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(SendOtpActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("mobile_no", edt_mobileNo.getText().toString().trim());
                params.put("latitude", latitude + "");
                params.put("longitude", longitude + "");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(SendOtpActivity.this);
        requestQueue.add(stringRequest);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public void getBanner() {
        final ProgressDialog progressDialog = new ProgressDialog(SendOtpActivity.this);
        progressDialog.show();
        progressDialog.setMessage("Loading");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Base_URl + bannerUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equalsIgnoreCase("True")) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        String JsonInString = jsonObject1.getString("banner");
                        bannerData = BannerData.createJsonInList(JsonInString);
                    }

                    viewPager_banner.setAdapter(new AdapterForBanner(SendOtpActivity.this, bannerData));
                    indicator.setViewPager(viewPager_banner);
                    final float density = getResources().getDisplayMetrics().density;

//Set circle indicator radius
                    indicator.setRadius(2 * density);

                    // Auto start of viewpager
                    final Handler handler = new Handler();
                    final Runnable Update = new Runnable() {
                        public void run() {
                            if (currentPage == bannerData.size()) {
                                currentPage = 0;
                            }
                            viewPager_banner.setCurrentItem(currentPage++, true);
                        }
                    };
                    Timer swipeTimer = new Timer();
                    swipeTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            handler.post(Update);
                        }
                    }, 5000, 3000);
// Pager listener over indicator
                    indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                        @Override
                        public void onPageSelected(int position) {
                            currentPage = position;
                        }
                        @Override
                        public void onPageScrolled(int pos, float arg1, int arg2) {
                        }

                        @Override
                        public void onPageScrollStateChanged(int pos) {

                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //  Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Please check your Internet Connection! try again...", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

}
