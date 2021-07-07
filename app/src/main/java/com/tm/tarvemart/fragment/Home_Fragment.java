package com.tm.tarvemart.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tm.tarvemart.Adapter.AdapterForBanner;
import com.tm.tarvemart.Adapter.AdapterForCategoryHome;
import com.tm.tarvemart.Data_List;
import com.tm.tarvemart.Helper.NetworkConnectionHelper;
import com.tm.tarvemart.R;
import com.tm.tarvemart.entity.BannerData;
import com.tm.tarvemart.entity.CategoryData;
import com.tm.tarvemart.inerface.GlobleInterfce;
import com.tm.tarvemart.mainUI.ActivityProduct;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import static android.content.Context.LAYOUT_INFLATER_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 * */


public class Home_Fragment extends Fragment implements GlobleInterfce {
    RecyclerView rv_Item_category;
    GridLayoutManager gridLayoutManager;
    List<String> list = new ArrayList<>();
    private LinearLayout dynamic_ll;

    String url = "subcategory_mainpage";
    private ArrayList<Data_List> anoop_dataArray;
    private String cat_id,category_name,sub_category_name,sub_id,sub_category_image;
    private TextView cat_name_tv;

    public Home_Fragment() {
        // Required empty public constructor
    }
    List<CategoryData> categoryData = new ArrayList<>();
    List<BannerData> bannerData = new ArrayList<>();
    String bannerUrl = "bannerlist";
    ViewPager viewPager_banner;
    private static int currentPage = 0;
    CirclePageIndicator indicator;
    SwipeRefreshLayout srl_Refresh;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_, container, false);
        rv_Item_category = (RecyclerView) view.findViewById(R.id.rv_Item_category);
        dynamic_ll=(LinearLayout)view.findViewById(R.id.dynamic_ll);
        viewPager_banner = (ViewPager) view.findViewById(R.id.viewPager_banner);
        indicator = (CirclePageIndicator) view.findViewById(R.id.indicator);

        srl_Refresh = view.findViewById(R.id.srl_Refresh);
        srl_Refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        srl_Refresh.setRefreshing(false);
                    }
                }, 2000);
            }
        });
        if (NetworkConnectionHelper.isOnline(getActivity())) {
            getBanner();
            getData();
        } else {
            Toast.makeText(getActivity(), "Please check your internet connection! try again...", Toast.LENGTH_SHORT).show();
        }
        return view;
    }


    public void getBanner() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
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
                    viewPager_banner.setAdapter(new AdapterForBanner(getActivity(), bannerData));
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
                Toast.makeText(getActivity(), "Please check your Internet Connection! try again...", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    public void getData() {
        anoop_dataArray = new ArrayList<Data_List>();
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();
        progressDialog.setMessage("Loading");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Base_URl + url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean("status")) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        String listInString = jsonObject1.getString("category");
                        //   Log.d("res","res"+listInString);
                        if (jsonObject1.has("category")) {
                            JSONArray my_noti_Array = jsonObject1.getJSONArray("category");
                            for (int i = 0; i < my_noti_Array.length(); i++) {
                                JSONObject jsonObject11 = my_noti_Array.getJSONObject(i);
                                cat_id = jsonObject11.getString("id");
                                category_name = jsonObject11.getString("category_name");
                                String category_image = jsonObject11.getString("category_image");
                                String subcat = jsonObject11.getString("subcategory");
                                categoryData = CategoryData.createJsonInList(subcat);
                                if (jsonObject11.has("subcategory")) {
                                    JSONArray subcat_Array = jsonObject11.getJSONArray("subcategory");
                                    for (int j = 0; j < subcat_Array.length(); j++) {
                                        JSONObject subcat_jsonObject = subcat_Array.getJSONObject(j);
                                        sub_id = subcat_jsonObject.getString("id");
                                        sub_category_name = subcat_jsonObject.getString("category_name");
                                        sub_category_image = subcat_jsonObject.getString("category_image");
                                    }

                                    Data_List noti_screen_lists = new Data_List(cat_id,category_name, sub_id,sub_category_name, sub_category_image);
                                    anoop_dataArray.add(noti_screen_lists);
                                    getDataList(i);
                                }
                            }
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
                Toast.makeText(getActivity(), "Please check your Internet Connection! try again...", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

//     Latest_add_Screen function to set data in data array(dataarray1)layout

    private void getDataList(final int i) {

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
        View convertView = inflater.inflate(R.layout.dynamic_dashboard, null);
        dynamic_ll.addView(convertView);
        cat_name_tv=(TextView)convertView.findViewById(R.id.cat_name_tv);
        RelativeLayout add_rl=(RelativeLayout)convertView.findViewById(R.id.add_rl);
        rv_Item_category=(RecyclerView)convertView.findViewById(R.id.rv_Item_category);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),3);
        //  GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),1,GridLayoutManager.HORIZONTAL,false);
        AdapterForCategoryHome myAdapter = new AdapterForCategoryHome(getActivity(), categoryData);
        rv_Item_category.setLayoutManager(gridLayoutManager);
        rv_Item_category.setAdapter(myAdapter);
        rv_Item_category.setHasFixedSize(true);



        cat_name_tv.setText(anoop_dataArray.get(i).getCategory_name());
        cat_name_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (anoop_dataArray.get(i).getCategory_name().equalsIgnoreCase("Garments")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("This option Coming soon...");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.setCancelable(false);
                    builder.show();
                } else {
                    Intent intent = new Intent(getActivity(), ActivityProduct.class);
                    intent.putExtra("id", anoop_dataArray.get(i).getCat_id());
                    //   intent.putExtra("fromWhere", anoop_dataArray.get(i).getCategory_name());
                    startActivity(intent);
                }
            }
        });

//        if (anoop_dataArray.get(i).getCategory_name().equalsIgnoreCase("Fruits")){
//            add_rl.setVisibility(View.VISIBLE);
//        }

        View view = new View(getActivity());
        LinearLayout.LayoutParams params_view = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 10);
        view.setLayoutParams(params_view);
        dynamic_ll.addView(view);
    }



}
