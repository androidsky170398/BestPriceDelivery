package com.tm.tarvemart.VolleyMultiUse;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tm.tarvemart.R;
import com.tm.tarvemart.entity.CityData;
import com.tm.tarvemart.entity.StateData;
import com.tm.tarvemart.inerface.GlobleInterfce;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VolleyForStateAndCity implements GlobleInterfce {
    Context context;
    String stateId, url;
    Spinner spinner;
    static String selectedValue;
    public static HashMap<String, String> hashMapForState = new HashMap<>();
    public static HashMap<String, String> hashMapForCity = new HashMap<>();

    public VolleyForStateAndCity(Context context, String stateId, String url, Spinner spinner, String selectedValue) {
        this.context = context;
        this.stateId = stateId;
        this.url = url;
        this.spinner = spinner;
        this.selectedValue = selectedValue;
        getStateAndCity();
    }

    public void getStateAndCity() {
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Base_URl + url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                //Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean("status") == true) {
                        String data = jsonObject.getString("data");
                        if (data != null && !data.equalsIgnoreCase("{}")) {
                            if (url.equalsIgnoreCase("state-list")) {
                                List<StateData> List_State = StateData.createJsonInList(data);
                                hashMapForState.clear();
                                String[] s = new String[(List_State.size() + 1)];
                                s[0] = "--Select--";
                                if (List_State != null && List_State.size() > 0) {
                                    for (int i = 0; i < List_State.size(); i++) {
                                        s[(i + 1)] = List_State.get(i).getState_name();
                                        hashMapForState.put(List_State.get(i).getState_name(), List_State.get(i).getId());
                                        if (selectedValue != null && selectedValue.equalsIgnoreCase("") && List_State.get(i).getState_name().equalsIgnoreCase(selectedValue)) {
                                            spinner.setSelection(i);
                                        }
                                    }
                                }
                                spinner.setAdapter(new ArrayAdapter(context, R.layout.support_simple_spinner_dropdown_item, s));
                            } else if (url.equalsIgnoreCase("city-list")) {
                                List<CityData> City_List = CityData.createJsonInList(data);
                                String[] s = new String[(City_List.size() + 1)];
                                s[0] = "--Select--";
                                int position = 0;
                                if (City_List != null && City_List.size() > 0) {
                                    for (int i = 0; i < City_List.size(); i++) {
                                        s[(i + 1)] = City_List.get(i).getCity();
                                        hashMapForCity.put(City_List.get(i).getCity(), City_List.get(i).getId());
                                        if (selectedValue != null && !selectedValue.equalsIgnoreCase("") && City_List.get(i).getCity().equalsIgnoreCase(selectedValue)) {
                                            position = (i + 1);
                                        }
                                    }
                                }
                                spinner.setAdapter(new ArrayAdapter(context, R.layout.support_simple_spinner_dropdown_item, s));
                                spinner.setSelection(position);
                            }
                        } else {
                            Toast.makeText(context, "Data not found!!!", Toast.LENGTH_SHORT).show();
                        }
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
                if (stateId != null && !stateId.equalsIgnoreCase("")) {
                    params.put("state_id", stateId);
                }
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
}
