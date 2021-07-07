package com.tm.tarvemart.mainUI;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tm.tarvemart.Helper.NetworkConnectionHelper;
import com.tm.tarvemart.R;
import com.tm.tarvemart.inerface.GlobleInterfce;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DeliveryAddress extends AppCompatActivity implements GlobleInterfce {
    String City_Url = "city-list";
    ArrayList<String> CityName;
    ArrayList<String> CityId;
    Spinner sp_city;
    String cityid;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_address);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add Address");
        sp_city = findViewById(R.id.sp_city);
        CityName = new ArrayList<>();
        CityId = new ArrayList<>();
        if (NetworkConnectionHelper.isOnline(DeliveryAddress.this)) {
            loadSpinnerCity();
        } else {
            Toast.makeText(DeliveryAddress.this, "Please check your internet connection! try again...", Toast.LENGTH_SHORT).show();
        }


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
        //Dashboard.checkNetConnection(DeliveryAddress.this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Dashboard.checkNetConnection(DeliveryAddress.this);
    }

    public void loadSpinnerCity() {
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

                    sp_city.setAdapter(new ArrayAdapter<String>(DeliveryAddress.this, android.R.layout.simple_spinner_dropdown_item, CityName));


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) /*{
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> params = new HashMap<>();
                params.put("state_id", p);
                return params;

            }
        }*/;
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

}
