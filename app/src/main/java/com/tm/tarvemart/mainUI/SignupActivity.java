package com.tm.tarvemart.mainUI;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tm.tarvemart.Helper.NetworkConnectionHelper;
import com.tm.tarvemart.LocationTrack.GPSTracker;
import com.tm.tarvemart.R;
import com.tm.tarvemart.Util.CommonUtill;
import com.tm.tarvemart.Util.Constants;
import com.tm.tarvemart.inerface.GlobleInterfce;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.tm.tarvemart.Util.Constants.FCM_KEY;

public class SignupActivity extends AppCompatActivity implements GlobleInterfce {

    TextInputEditText edt_name, edt_email, edt_mobile, edt_referral;
    Spinner sp_state, sp_city;
    ArrayList<String> StateName;
    ArrayList<String> StateId;
    ArrayList<String> CityName;
    ArrayList<String> CityName1;
    ArrayList<String> CityId;
    String State_Url = "state-list";
    String City_Url = "city-list";
    String stateid, cityid;
    String url = "signup";
    Button btn_signUp;
    String m_androidId;
    double latitude, longitude;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("SignUp");
        StateName = new ArrayList<>();
        CityName = new ArrayList<>();
        CityId = new ArrayList<>();
        StateId = new ArrayList<>();
        CityName1 = new ArrayList<>();
        sp_state = findViewById(R.id.sp_state);
        sp_city = findViewById(R.id.sp_city);
        edt_name = findViewById(R.id.edt_name);
        edt_email = findViewById(R.id.edt_email);
        edt_mobile = findViewById(R.id.edt_mobile);
        edt_referral = findViewById(R.id.edt_referral);
        btn_signUp = findViewById(R.id.btn_signUp);
        edt_mobile.setText(getIntent().getStringExtra("mobileNo"));
        edt_mobile.setKeyListener(null);
        //   m_androidId = Settings.Secure.getString(SignupActivity.this.getContentResolver(), Settings.Secure.ANDROID_ID);
        GPSTracker gpsTracker = new GPSTracker(SignupActivity.this);
        if (gpsTracker.canGetLocation()) {
            latitude = gpsTracker.getLatitude();
            longitude = gpsTracker.getLongitude();

        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gpsTracker.showSettingsAlert();
        }
        btn_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkConnectionHelper.isOnline(SignupActivity.this)) {
                    signUp();
                } else {
                    Toast.makeText(SignupActivity.this, "Please check your internet connection! try again...", Toast.LENGTH_SHORT).show();
                }
            }
        });
        if (NetworkConnectionHelper.isOnline(SignupActivity.this)) {
            loadSpinnerState();
        } else {
            Toast.makeText(SignupActivity.this, "Please check your internet connection! try again...", Toast.LENGTH_SHORT).show();
        }

        sp_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {

                    for (int j = 0; j <= StateId.size(); j++) {
                        if (sp_state.getSelectedItem().toString().equalsIgnoreCase(StateName.get(j))) {
                            // position = i;
                            stateid = StateId.get(j - 1);
                            break;
                        }
                    }
                    CityName1.clear();
                    CityName.clear();
                    loadSpinnerCity(stateid);
                } else if (position == 0) {
                    CityName.clear();
                    loadSpinnerCity1();
                    CityName1.clear();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                CityName1.clear();

                //  sp_city.setAdapter(new ArrayAdapter<String>(UploadPeronalDetails.this, android.R.layout.simple_spinner_dropdown_item, CityName));*/

            }
        });
        sp_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    for (int j = 0; j <= CityId.size(); j++) {
                        if (sp_city.getSelectedItem().toString().equalsIgnoreCase(CityName.get(j))) {
                            // position = i;
                            cityid = CityId.get(j - 1);
                            break;
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //Dashboard.checkNetConnection(SignupActivity.this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Dashboard.checkNetConnection(SignupActivity.this);
    }

    private void loadSpinnerState() {
        RequestQueue requestQueue = Volley.newRequestQueue(SignupActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Base_URl + State_Url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    StateName.add("Select State");
                    JSONObject jsonObject = new JSONObject(response);
                    //  String sta = jsonObject.getString("status");
                    if (jsonObject.getBoolean("status")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            StateId.add(jsonObject1.getString("id"));
                            String statename = jsonObject1.getString("state_name");
                            StateName.add(statename);
                        }
                    }

                    sp_state.setAdapter(new ArrayAdapter<String>(SignupActivity.this, android.R.layout.simple_spinner_dropdown_item, StateName));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SignupActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });
        requestQueue.add(stringRequest);
    }

    public void loadSpinnerCity(final String p) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Base_URl + City_Url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    //  CityName.clear();
                    JSONObject jsonObject = new JSONObject(response);
                    CityName.add("Select City");
                    //  String sta = jsonObject.getString("status");
                    if (jsonObject.getBoolean("status")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            CityId.add(jsonObject1.getString("id"));
                            // String statename = jsonObject1.getString("state_name");
                            String Cityname = jsonObject1.getString("city");
                            CityName.add(Cityname);
                        }
                    }

                    sp_city.setAdapter(new ArrayAdapter<String>(SignupActivity.this, android.R.layout.simple_spinner_dropdown_item, CityName));


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> params = new HashMap<>();
                params.put("state_id", p);
                return params;

            }
        };
        requestQueue.add(stringRequest);
    }

    public void loadSpinnerCity1() {
        RequestQueue requestQueue = Volley.newRequestQueue(SignupActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Base_URl + City_Url /*+ "=" + StateId.get(p - 1)*/, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    CityName1.add("Select City");
                    JSONObject jsonObject = new JSONObject(response);

                    sp_city.setAdapter(new ArrayAdapter<String>(SignupActivity.this, android.R.layout.simple_spinner_dropdown_item, CityName1));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(stringRequest);
    }

    public void signUp() {
        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Base_URl + url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                // Toast.makeText(SignupActivity.this, response, Toast.LENGTH_SHORT).show();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean("status")) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        if (jsonObject1.has("mobile")) {
                            Toast.makeText(SignupActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SignupActivity.this, ValidateOtp.class).putExtra("mobileNo", jsonObject1.getString("mobile")));
                            finish();
                        }
                    } else {
                        JSONObject jsonObject2 = jsonObject.getJSONObject("error_code");
                        if (jsonObject2.has("state"))
                            Toast.makeText(SignupActivity.this, jsonObject2.getString("state"), Toast.LENGTH_SHORT).show();
                        if (jsonObject2.has("city"))
                            Toast.makeText(SignupActivity.this, jsonObject2.getString("city"), Toast.LENGTH_SHORT).show();
                        if (jsonObject2.has("name"))
                            Toast.makeText(SignupActivity.this, jsonObject2.getString("name"), Toast.LENGTH_SHORT).show();
                        if (jsonObject2.has("email"))
                            Toast.makeText(SignupActivity.this, jsonObject2.getString("email"), Toast.LENGTH_SHORT).show();
                        if (jsonObject2.has("mobile"))
                            Toast.makeText(SignupActivity.this, jsonObject2.getString("mobile"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(SignupActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("name", edt_name.getText().toString().trim());
                params.put("mobile", edt_mobile.getText().toString().trim());
                params.put("email", edt_email.getText().toString().trim());
                if (stateid != null)
                    params.put("state", stateid);
                if (cityid != null)
                    params.put("city", cityid);
                params.put("fcm_token", Constants.getSavedPreferences(SignupActivity.this, FCM_KEY, ""));
                params.put("referal_code", edt_referral.getText().toString().trim());
                params.put("latitude", latitude + "");
                params.put("longitude", longitude + "");
                params.put("device_id", CommonUtill.getDeviceId(SignupActivity.this));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(SignupActivity.this);
        requestQueue.add(stringRequest);
    }

}
