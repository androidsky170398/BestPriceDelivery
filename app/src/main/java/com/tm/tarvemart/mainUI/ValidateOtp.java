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
import android.widget.TextView;
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
import com.tm.tarvemart.Util.Constants;
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

import static com.tm.tarvemart.Util.Constants.ID;
import static com.tm.tarvemart.Util.Constants.User_Email;
import static com.tm.tarvemart.Util.Constants.User_Mobile;
import static com.tm.tarvemart.Util.Constants.User_Name;


public class ValidateOtp extends AppCompatActivity implements GlobleInterfce {

    TextInputEditText edt_otp;
    TextView txt_resendotp;
    Button btn_validateOtp;
    String validate_url = "validate-otp";
    String resend_url = "send-otp";
    Double latitude, longitude;
    TextView txt_timer;
    int time = 30;

    ViewPager viewPager_banner;
    private static int currentPage = 0;
    CirclePageIndicator indicator;
    List<BannerData> bannerData = new ArrayList<>();
    String bannerUrl = "bannerlist";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validate_otp);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Validate Otp");
        edt_otp = findViewById(R.id.edt_otp);
        txt_resendotp = findViewById(R.id.txt_resendotp);
        btn_validateOtp = findViewById(R.id.btn_validateOtp);
        txt_timer = findViewById(R.id.txt_timer);
        viewPager_banner = (ViewPager) findViewById(R.id.viewPager_banner);
        indicator = (CirclePageIndicator) findViewById(R.id.indicator);

        GPSTracker gpsTracker = new GPSTracker(ValidateOtp.this);
        if (gpsTracker.canGetLocation()) {
            latitude = gpsTracker.getLatitude();
            longitude = gpsTracker.getLongitude();
        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gpsTracker.showSettingsAlert();
        }
        btn_validateOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkConnectionHelper.isOnline(ValidateOtp.this))
                    validateOtp();
                else
                    Toast.makeText(ValidateOtp.this, "Please Check InterNet Connection", Toast.LENGTH_SHORT).show();

            }
        });

        txt_resendotp.setVisibility(View.GONE);
        txt_resendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkConnectionHelper.isOnline(ValidateOtp.this)) {
                    resendOtp();
                    getTimer();
                } else
                    Toast.makeText(ValidateOtp.this, "Please Check InterNet Connection", Toast.LENGTH_SHORT).show();

            }
        });
        getTimer();
        getBanner();
    }

    public void validateOtp() {
        final ProgressDialog progressDialog = new ProgressDialog(ValidateOtp.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Base_URl + validate_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean("status")) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        Constants.savePreferences(ValidateOtp.this, ID, jsonObject1.getString("id"));
                        Constants.savePreferences(ValidateOtp.this, User_Name, jsonObject1.getString("name"));
                        Constants.savePreferences(ValidateOtp.this, User_Email, jsonObject1.getString("email"));
                        Constants.savePreferences(ValidateOtp.this, User_Mobile, jsonObject1.getString("mobile"));
                        Toast.makeText(ValidateOtp.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ValidateOtp.this, Dashboard.class));
                        finish();
                    } else {
                        Toast.makeText(ValidateOtp.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //  Toast.makeText(ValidateOtp.this, response, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(ValidateOtp.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("mobile_no", getIntent().getStringExtra("mobileNo"));
                params.put("otp_text", edt_otp.getText().toString().trim());
                params.put("latitude", latitude + "");
                params.put("longitude", longitude + "");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(ValidateOtp.this);
        requestQueue.add(stringRequest);
        //Dashboard.checkNetConnection(ValidateOtp.this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Dashboard.checkNetConnection(ValidateOtp.this);
    }

    public void resendOtp() {
        final ProgressDialog progressDialog = new ProgressDialog(ValidateOtp.this);
        progressDialog.setMessage("Sending Otp");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Base_URl + resend_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean("status")) {
                        Toast.makeText(ValidateOtp.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(ValidateOtp.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("mobile_no", getIntent().getStringExtra("mobileNo"));
                params.put("latitude", latitude + "");
                params.put("longitude", longitude + "");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(ValidateOtp.this);
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

    void getTimer() {
       final Handler handler=new Handler();
       handler.postDelayed(new Runnable() {
           @Override
           public void run() {
               if (time == 0) {
                   txt_timer.setVisibility(View.GONE);
                   txt_resendotp.setVisibility(View.VISIBLE);
               } else {
                   txt_timer.setText("00:" + String.valueOf(time));
                   time--;
                   handler.postDelayed(this,1000);
               }
           }
       },1000);


    }

    public void getBanner() {
        final ProgressDialog progressDialog = new ProgressDialog(ValidateOtp.this);
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

                    viewPager_banner.setAdapter(new AdapterForBanner(ValidateOtp.this, bannerData));
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
