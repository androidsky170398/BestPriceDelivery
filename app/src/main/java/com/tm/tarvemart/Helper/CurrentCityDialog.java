package com.tm.tarvemart.Helper;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tm.tarvemart.R;
import com.tm.tarvemart.Util.Constants;
import com.tm.tarvemart.inerface.GlobleInterfce;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.tm.tarvemart.Util.Constants.CITY_ID;
import static com.tm.tarvemart.Util.Constants.CITY_NAME;
import static com.tm.tarvemart.Util.Constants.JUSTLAUNCHED;


/**
 * Created by Rai Shahab on 28-10-2018.
 */

public class CurrentCityDialog implements GlobleInterfce {
    String cityList = Base_URl + "city-list";
    List<JSONObject> cityObjects = new ArrayList<>();
    CityListAdapter cityListAdapter;
    Activity activity;
    TextView norecordTv;
    Dialog dialog;
    FragmentManager fragmentManager;
    TextView tv_Location;

    public CurrentCityDialog(@NonNull Activity activity, FragmentManager fragmentManager, TextView tv_Location) {
        this.activity = activity;
        this.fragmentManager = fragmentManager;
        this.tv_Location = tv_Location;
        showDialog();
    }


    private void getCityList() {
        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(activity);
        progressDoalog.setCancelable(false);
        progressDoalog.setMessage("Please wait....");
        progressDoalog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, cityList,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String result) {
                        progressDoalog.dismiss();
                        norecordTv = dialog.findViewById(R.id.norecordTv);
                        try {
                            JSONObject response = new JSONObject(result);
                            cityObjects.clear();
                            if (response.getString("status").equalsIgnoreCase("true")) {
                                JSONArray jsonObject = response.getJSONArray("data");
                                if (jsonObject != null && jsonObject.length() > 0) {
                                    for (int i = 0; i < jsonObject.length(); i++) {
                                        cityObjects.add(jsonObject.getJSONObject(i));
                                    }
                                    norecordTv.setVisibility(View.GONE);

                                }
                                cityListAdapter.notifyDataSetChanged();
                            } else
                                norecordTv.setVisibility(View.VISIBLE);
                            dialog.show();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDoalog.dismiss();
                        Toast.makeText(activity, "Please check your internet connection! try again...", Toast.LENGTH_LONG).show();
                    }
                }) {


        };
        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
    }

    public void showDialog() {
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.city_list);
        assert dialog.getWindow() != null;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        RecyclerView cityRv = dialog.findViewById(R.id.cityRv);
        norecordTv = dialog.findViewById(R.id.norecordTv);
        cityRv.setLayoutManager(new LinearLayoutManager(activity));
        cityListAdapter = new CityListAdapter(cityObjects);
        cityRv.setAdapter(cityListAdapter);
        getCityList();
    }

    public class CityListAdapter extends RecyclerView.Adapter<CityListAdapter.ViewHolder> {
        List<JSONObject> cityObjects;

        CityListAdapter(List<JSONObject> cityObjects) {
            this.cityObjects = cityObjects;
        }

        @NonNull
        @Override
        public CityListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_row, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
            try {
                holder.titleTxt.setText(cityObjects.get(position).getString("city"));
                if (cityObjects.size() == 1) {
                    holder.vw.setVisibility(View.GONE);
                }
                if (cityObjects.size() == 5 || cityObjects.size() > 5) {
                    holder.fram.setMinimumHeight(300);
                    holder.fram.setMinimumWidth(300);
                }
                holder.conatinerLl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            Constants.savePreferences(activity, CITY_ID, cityObjects.get(position).getString("id"));
                            Constants.savePreferences(activity, JUSTLAUNCHED, cityObjects.get(position).getString("id"));
                            Constants.savePreferences(activity, CITY_NAME, cityObjects.get(position).getString("city"));
                            tv_Location.setText(cityObjects.get(position).getString("city"));
                            dialog.dismiss();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        public int getItemCount() {
            return cityObjects.size();
        }


        class ViewHolder extends RecyclerView.ViewHolder {
            TextView titleTxt;
            LinearLayout conatinerLl;
            View vw;
            FrameLayout fram;

            ViewHolder(View v) {
                super(v);
                v.findViewById(R.id.iconsIv).setVisibility(View.GONE);
                titleTxt = v.findViewById(R.id.titleTxt);
                fram = v.findViewById(R.id.fram);
                conatinerLl = v.findViewById(R.id.conatinerLl);
                vw = v.findViewById(R.id.vw);
                titleTxt.setGravity(Gravity.CENTER_HORIZONTAL);
                titleTxt.setTextColor(activity.getResources().getColor(R.color.white));
            }

        }


    }


}
