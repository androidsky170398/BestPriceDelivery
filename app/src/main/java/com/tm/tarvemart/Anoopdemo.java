//package com.tm.tarvemart;
//
//import android.app.ProgressDialog;
//import android.support.v4.app.FragmentActivity;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.support.v7.widget.GridLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.android.volley.Request;
//import com.android.volley.RequestQueue;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.StringRequest;
//import com.android.volley.toolbox.Volley;
//import com.tm.tarvemart.Adapter.AdapterForCategoryHome;
//import com.tm.tarvemart.Adapter.AdapterForProduct;
//import com.tm.tarvemart.entity.CategoryData;
//import com.tm.tarvemart.entity.ProductData;
//import com.tm.tarvemart.mainUI.ActivityProduct;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static com.tm.tarvemart.inerface.GlobleInterfce.Base_URl;
//
//public class Anoopdemo extends AppCompatActivity {
//    private LinearLayout dynamic_ll;
//    String url = "subcategory_mainpage";
//    private ArrayList<Data_List> anoop_dataArray;
//    private String category_name,sub_category_name,sub_id,sub_category_image;
//    private TextView cat_name_tv;
//    private RecyclerView rv_Item_category;
//    private List<CategoryData> categoryData;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_anoopdemo);
//        dynamic_ll=(LinearLayout)findViewById(R.id.dynamic_ll);
//
//        getData();
//
//    }
//
//    public void getData() {
//        anoop_dataArray = new ArrayList<Data_List>();
//        final ProgressDialog progressDialog = new ProgressDialog(Anoopdemo.this);
//        progressDialog.show();
//        progressDialog.setMessage("Loading");
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, Base_URl + url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                progressDialog.dismiss();
//                try {
//                    JSONObject jsonObject = new JSONObject(response);
//                    if (jsonObject.getBoolean("status")) {
//                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
//                        String listInString = jsonObject1.getString("category");
//                     //   Log.d("res","res"+listInString);
//                        if (jsonObject1.has("category")) {
//                            JSONArray my_noti_Array = jsonObject1.getJSONArray("category");
//                            for (int i = 0; i < my_noti_Array.length(); i++) {
//                                JSONObject jsonObject11 = my_noti_Array.getJSONObject(i);
//                                String id = jsonObject11.getString("id");
//                                category_name = jsonObject11.getString("category_name");
//                                String category_image = jsonObject11.getString("category_image");
//                                String subcat = jsonObject11.getString("subcategory");
//                                categoryData = CategoryData.createJsonInList(subcat);
//                                if (jsonObject11.has("subcategory")) {
//                                    JSONArray subcat_Array = jsonObject11.getJSONArray("subcategory");
//                                    for (int j = 0; j < subcat_Array.length(); j++) {
//                                        JSONObject subcat_jsonObject = subcat_Array.getJSONObject(j);
//                                        sub_id = subcat_jsonObject.getString("id");
//                                        sub_category_name = subcat_jsonObject.getString("category_name");
//                                        sub_category_image = subcat_jsonObject.getString("category_image");
//                                    }
//
//
//
//                                    Data_List noti_screen_lists = new Data_List(category_name, sub_id,sub_category_name, sub_category_image);
//                                    anoop_dataArray.add(noti_screen_lists);
//                                    getNotificationList(i);
//                                }
//                            }
//                        }
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                progressDialog.dismiss();
//                Toast.makeText(getApplicationContext(), "Please check your Internet Connection! try again...", Toast.LENGTH_SHORT).show();
//            }
//        });
//        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
//        requestQueue.add(stringRequest);
//    }
//    //Latest_add_Screen function to set data in data array(dataarray1)layout
//    private void getNotificationList(final int i) {
//        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
//        View convertView = inflater.inflate(R.layout.dynamic_dashboard, null);
//        dynamic_ll.addView(convertView);
//
//        cat_name_tv=(TextView)convertView.findViewById(R.id.cat_name_tv);
//        rv_Item_category=(RecyclerView)convertView.findViewById(R.id.rv_Item_category);
//
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(Anoopdemo.this, 3);
//        AdapterForCategoryHome myAdapter = new AdapterForCategoryHome(Anoopdemo.this, categoryData);
//        rv_Item_category.setLayoutManager(gridLayoutManager);
//        rv_Item_category.setAdapter(myAdapter);
//        rv_Item_category.setHasFixedSize(true);
//
//
//        cat_name_tv.setText(anoop_dataArray.get(i).getCategory_name());
//
//        View view = new View(Anoopdemo.this);
//        LinearLayout.LayoutParams params_view = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 10);
//        view.setLayoutParams(params_view);
//        dynamic_ll.addView(view);
//    }
//}
