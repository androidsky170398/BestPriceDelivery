package com.tm.tarvemart.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tm.tarvemart.Adapter.AdapterForCategory;
import com.tm.tarvemart.Helper.NetworkConnectionHelper;
import com.tm.tarvemart.R;
import com.tm.tarvemart.entity.CategoryData;
import com.tm.tarvemart.inerface.GlobleInterfce;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends Fragment implements GlobleInterfce {
    RecyclerView rv_productCategory;
    List<CategoryData> list_Category = new ArrayList<>();
    String url = "category";
    SwipeRefreshLayout srl_Refresh;

    public CategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        rv_productCategory = (RecyclerView) view.findViewById(R.id.rv_productCategory);
        srl_Refresh = view.findViewById(R.id.srl_Refresh);
        srl_Refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetworkConnectionHelper.isOnline(getActivity())) {
                    getCategoryData();
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

        if (NetworkConnectionHelper.isOnline(getActivity())) {
            getCategoryData();
        } else {
            Toast.makeText(getActivity(), "Please check your internet connection! try again...", Toast.LENGTH_SHORT).show();
        }


        return view;
    }

    void getCategoryData() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();
        progressDialog.setMessage("Loading");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Base_URl + url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                //  Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean("status")) {
                        String jsonInString = jsonObject.getString("data");
                        list_Category = CategoryData.createJsonInList(jsonInString);
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);
                        AdapterForCategory myAdapter = new AdapterForCategory(getActivity(), list_Category);
                        rv_productCategory.setLayoutManager(gridLayoutManager);
                        rv_productCategory.setAdapter(myAdapter);
                        rv_productCategory.setHasFixedSize(true);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

}
