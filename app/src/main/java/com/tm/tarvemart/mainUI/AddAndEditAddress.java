package com.tm.tarvemart.mainUI;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
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
import com.tm.tarvemart.R;
import com.tm.tarvemart.Util.Constants;
import com.tm.tarvemart.VolleyMultiUse.VolleyForStateAndCity;
import com.tm.tarvemart.entity.AddressData;
import com.tm.tarvemart.inerface.GlobleInterfce;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.tm.tarvemart.Util.Constants.ID;

public class AddAndEditAddress extends AppCompatActivity implements GlobleInterfce {
    EditText et_firstname, et_lastname,
            et_houseNo,
            et_apartname,
            et_street,
            et_landmark,
            et_pincode,
            et_contact;
    Spinner sp_state,
            sp_city;

    RadioButton rb_home, rb_office, rb_other;

    TextView txt_updateAndAdd;
    AddressData addressData;
    String totalPrice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_and_edit_address);
        findViewByIds();
        Intent intent = getIntent();
        totalPrice = intent.getStringExtra("totalPrice");
        addressData = (AddressData) intent.getSerializableExtra("addressData");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (addressData != null) {
            getSupportActionBar().setTitle("Update Your Address");
            et_firstname.setText(addressData.getFirst_name() + "");
            et_lastname.setText(addressData.getLast_name() + "");
            et_houseNo.setText(addressData.getHouse_number() + "");
            et_apartname.setText(addressData.getApartment() + "");
            et_street.setText(addressData.getStreet_detail() + "");
            et_landmark.setText(addressData.getLandmark() + "");
            //et_city.setText(addressData.getCity_name() + "");
            et_pincode.setText(addressData.getPincode() + "");
            et_contact.setText(addressData.getMobile() + "");
            new VolleyForStateAndCity(AddAndEditAddress.this, "34", "city-list", sp_city, addressData.getCity_name());
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
            getSupportActionBar().setTitle("Add Address");
            txt_updateAndAdd.setText("Add Address");
            addressData = new AddressData();
            addressData.setAction("add");
            new VolleyForStateAndCity(AddAndEditAddress.this, "34", "city-list", sp_city, "");
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
                    Toast.makeText(AddAndEditAddress.this, "Please Select City!!!", Toast.LENGTH_SHORT).show();
                } else if (et_pincode.getText().toString().trim().equalsIgnoreCase("")) {
                    et_pincode.setError("Empty! Fill This.");
                    et_pincode.requestFocus();
                } else if (et_contact.getText().toString().trim().equalsIgnoreCase("")) {
                    et_contact.setError("Empty! Fill This.");
                    et_contact.requestFocus();
                } else if (rb_home.isChecked() == false && rb_office.isChecked() == false && rb_other.isChecked() == false) {
                    Toast.makeText(AddAndEditAddress.this, "Please select address type!!!", Toast.LENGTH_SHORT).show();
                } else {
                    SharedPreferences sharedLocation = getSharedPreferences("AppLocation", 0);
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
                    finalAddressData.setCustomer_id(Constants.getSavedPreferences(AddAndEditAddress.this, ID, null));

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

                    final ProgressDialog progressDialog = new ProgressDialog(AddAndEditAddress.this);
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
                                    Toast.makeText(AddAndEditAddress.this, message + "", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(AddAndEditAddress.this, AddressList.class).putExtra("totalPrice", totalPrice));
                                    finish();
                                } else {
                                    String message = jsonObject.getString("message");
                                    Toast.makeText(AddAndEditAddress.this, message + "", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            Toast.makeText(AddAndEditAddress.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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
                    RequestQueue requestQueue = Volley.newRequestQueue(AddAndEditAddress.this);
                    requestQueue.add(stringRequest);
                }
            }
        });

    }

    private void findViewByIds() {
        et_firstname = findViewById(R.id.et_firstname);
        et_lastname = findViewById(R.id.et_lastname);
        et_houseNo = findViewById(R.id.et_houseNo);
        et_apartname = findViewById(R.id.et_apartname);
        et_street = findViewById(R.id.et_street);
        et_landmark = findViewById(R.id.et_landmark);
        sp_state = findViewById(R.id.sp_state);
        sp_city = findViewById(R.id.sp_city);
        et_pincode = findViewById(R.id.et_pincode);
        et_contact = findViewById(R.id.et_contact);
        rb_home = findViewById(R.id.rb_home);
        rb_office = findViewById(R.id.rb_office);
        rb_other = findViewById(R.id.rb_other);
        txt_updateAndAdd = findViewById(R.id.txt_updateAndAdd);
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
        super.onBackPressed();
        startActivity(new Intent(AddAndEditAddress.this, AddressList.class).putExtra("totalPrice", totalPrice));
        finish();
    }
}
