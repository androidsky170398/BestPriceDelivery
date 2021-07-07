package com.tm.tarvemart.fragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tm.tarvemart.Adapter.AdapterForCoupon;
import com.tm.tarvemart.Helper.NetworkConnectionHelper;
import com.tm.tarvemart.R;
import com.tm.tarvemart.Util.Constants;
import com.tm.tarvemart.entity.CouponData;
import com.tm.tarvemart.inerface.GlobleInterfce;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tm.tarvemart.Util.Constants.ID;

/**
 * A placeholder fragment containing a simple view.
 */
public class CouponAdapterFragment extends Fragment implements GlobleInterfce {
    private static final String ARG_SECTION_NUMBER = "section_number";
    TextView tv_NoData;
    RecyclerView rv_Coupon;
    SwipeRefreshLayout srl_Refresh;

    public static CouponAdapterFragment newInstance(int index) {
        CouponAdapterFragment fragment = new CouponAdapterFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_coupon_adapter, container, false);

        tv_NoData = root.findViewById(R.id.tv_NoData);
        rv_Coupon = root.findViewById(R.id.rv_Coupon);
        rv_Coupon.setHasFixedSize(true);
        rv_Coupon.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        if (NetworkConnectionHelper.isOnline(getActivity())) {
            getCouponData();
        } else {
            Toast.makeText(getActivity(), "Please check your internet connection! try again...", Toast.LENGTH_SHORT).show();
        }
        srl_Refresh = root.findViewById(R.id.srl_Refresh);
        srl_Refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetworkConnectionHelper.isOnline(getActivity())) {
                    getCouponData();
                } else {
                    Toast.makeText(getActivity(), "Please check your internet connection! try again...", Toast.LENGTH_SHORT).show();
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        srl_Refresh.setRefreshing(false);
                    }
                }, 2000);
            }
        });

        return root;
    }

    void getCouponData() {
       /* final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();
        progressDialog.setMessage("Loading");*/
        String url = "coupon-list";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Base_URl + url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
              //  progressDialog.dismiss();
                //  Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean("status")) {
                        String jsonInString = jsonObject.getString("data");
                        List<CouponData> list_CouponData = new ArrayList<>();
                        CouponData couponData = CouponData.createJsonInObject(jsonInString);
                        if (getArguments().getInt(ARG_SECTION_NUMBER) == 1) {
                            if (couponData.getAvailable() != null && couponData.getAvailable().size() > 0) {
                                for (CouponData couponData1 : couponData.getAvailable()) {
                                    list_CouponData.add(couponData1);
                                }
                            }
                        } else {
                            if (couponData.getExpired() != null && couponData.getExpired().size() > 0) {
                                for (CouponData couponData1 : couponData.getExpired()) {
                                    list_CouponData.add(couponData1);
                                }
                            }
                        }
                        if (list_CouponData.size() > 0) {
                            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);
                            AdapterForCoupon myAdapter = new AdapterForCoupon(getActivity(), list_CouponData);
                            rv_Coupon.setAdapter(myAdapter);
                            rv_Coupon.setHasFixedSize(true);
                            tv_NoData.setVisibility(View.GONE);
                            rv_Coupon.setVisibility(View.VISIBLE);
                        } else {
                            rv_Coupon.setVisibility(View.GONE);
                            tv_NoData.setVisibility(View.VISIBLE);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> params = new HashMap<>();
                params.put("user_id", Constants.getSavedPreferences(getContext(), ID, null));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }
}




