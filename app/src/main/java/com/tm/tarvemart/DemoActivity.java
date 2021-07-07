//package com.tm.tarvemart;
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Build;
//import android.os.Handler;
//import android.support.annotation.NonNull;
//import android.support.annotation.RequiresApi;
//import android.support.v4.view.GravityCompat;
//import android.support.v4.widget.DrawerLayout;
//import android.support.v4.widget.SwipeRefreshLayout;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.support.v7.widget.GridLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.support.v7.widget.Toolbar;
//import android.view.LayoutInflater;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.WindowManager;
//import android.widget.AbsListView;
//import android.widget.CheckBox;
//import android.widget.EditText;
//import android.widget.ImageButton;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RadioButton;
//import android.widget.RadioGroup;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.android.volley.AuthFailureError;
//import com.android.volley.Request;
//import com.android.volley.RequestQueue;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.StringRequest;
//import com.android.volley.toolbox.Volley;
//import com.tm.tarvemart.Adapter.AdapterForBrand;
//import com.tm.tarvemart.Adapter.AdapterForProduct;
//import com.tm.tarvemart.Helper.NetworkConnectionHelper;
//import com.tm.tarvemart.R;
//import com.tm.tarvemart.Util.Constants;
//import com.tm.tarvemart.Util.PaginationScrollListener;
//import com.tm.tarvemart.entity.BrandData;
//import com.tm.tarvemart.entity.CategoryData;
//import com.tm.tarvemart.entity.ProductData;
//import com.tm.tarvemart.inerface.GlobleInterfce;
//import com.tm.tarvemart.mainUI.CartActivity;
//import com.tm.tarvemart.sqliteDatabase.DataSource;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class DemoActivity extends AppCompatActivity implements GlobleInterfce {
//    String url = "product-list";
//    List<ProductData> lst_productData = new ArrayList<>();
//    RecyclerView rec_prodeuctData;
//    int pageNo = 1;
//    boolean isLast = false;
//    boolean isLoad = false;
//    GridLayoutManager gridLayoutManager;
//    TextView txt_nodata;
//    SwipeRefreshLayout srl_refresh;
//    AdapterForDemo adapterForProduct;
//    String fromWhere, key;
//    boolean isScrolling = false;
//    private int currentitem, totalItem, scrolloutItems;
//    EditText et_search;
//    ImageButton btn_search;
//    boolean btn_search_click = false, sortBy = false, btn_Filter_click = false;
//    LinearLayout ll_sort;
//    TextView tv_sort, tv_filter;
//    List<CategoryData> list_Category = new ArrayList<>();
//    String sort_ByValue = "", byDiscount_Value = "", by_minPriceValue = "", by_maxPriceValue = "", by_BrandValue = "";
//    Toolbar toolbar;
//    DrawerLayout drawer;
//
//    EditText et_minPrice, et_maxPrice;
//    CheckBox ch_5, ch_10, ch_15, ch_20;
//    TextView tv_clearFilter, tv_ApplyFilter;
//    LinearLayout ll_discount, ll_discountView, ll_search;
//    ImageView iv_discount;
//    ImageView iv_backSearch;
//
//    LinearLayout ll_Brand;
//    ImageView iv_Brand;
//    RecyclerView rv_Brand;
//    List<BrandData> brandDataList = new ArrayList<>();
//    private AdapterForBrand adapterForBrand;
//
////    TextView tv_productCount,
////            tv_total_Price,
////            tv_View_Cart;
//
//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_product);
//        findViewByIds();
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setTitle("Products");
//        //getWindow().setTitleColor();
//        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
//        gridLayoutManager = new GridLayoutManager(DemoActivity.this, 2);
//        implementPagination(gridLayoutManager);
//        Intent intent = getIntent();
//        fromWhere = intent.getStringExtra("fromWhere");
//        if (fromWhere != null && fromWhere.equalsIgnoreCase("Search")) {
//            key = intent.getStringExtra("key");
//            ll_search.setVisibility(View.VISIBLE);
//            et_search.requestFocus();
//        } else {
//            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//            ll_search.setVisibility(View.GONE);
//        }
//
//        if (NetworkConnectionHelper.isOnline(DemoActivity.this)) {
//            getData();
//        } else {
//            Toast.makeText(DemoActivity.this, "Please check your internet connection! try again...", Toast.LENGTH_SHORT).show();
//        }
//
//        btn_search.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (et_search.getText().toString().trim().equalsIgnoreCase("")) {
//                    Toast.makeText(DemoActivity.this, "Please enter product name...", Toast.LENGTH_SHORT).show();
//                } else {
//                    key = et_search.getText().toString().trim();
//                    getData();
//                    btn_search_click = true;
//                    pageNo = 1;
//                }
//            }
//        });
//
//        srl_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                if (NetworkConnectionHelper.isOnline(DemoActivity.this)) {
//
//
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                       /* if (lst_productData.size() > 0) {
//                            adapterForProduct.notifyDataSetChanged();
//                        } else {
//                            getData();
//                        }*/
//                            pageNo = 1;
//                            btn_Filter_click = true;
//                            getData();
//                            srl_refresh.setRefreshing(false);
//                        }
//                    }, 2000);
//                } else {
//                    Toast.makeText(DemoActivity.this, "Please check your internet connection! try again...", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
//        rec_prodeuctData.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
//                    isScrolling = true;
//                }
//            }
//
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                currentitem = gridLayoutManager.getChildCount();
//                totalItem = gridLayoutManager.getItemCount();
//                scrolloutItems = gridLayoutManager.findFirstVisibleItemPosition();
//                if (isScrolling && (currentitem + totalItem == scrolloutItems)) {
//                    if (NetworkConnectionHelper.isOnline(DemoActivity.this)) {
//                        getData();
//                    } else {
//                        Toast.makeText(DemoActivity.this, "Please check your internet connection! try again...", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }
//        });
//
//        tv_sort.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(DemoActivity.this);
//                final View view = LayoutInflater.from(DemoActivity.this).inflate(R.layout.dialog_for_sortby, null, false);
//                final RadioGroup radioGroup = view.findViewById(R.id.rg_Sort);
//                builder.setView(view);
//                final AlertDialog sortDialog = builder.show();
//                if (sort_ByValue != null && sort_ByValue.equalsIgnoreCase("productname-dsc")) {
//                    ((RadioButton) radioGroup.getChildAt(1)).setChecked(true);
//                } else if (sort_ByValue != null && sort_ByValue.equalsIgnoreCase("selling_price-asc")) {
//                    ((RadioButton) radioGroup.getChildAt(2)).setChecked(true);
//                } else if (sort_ByValue != null && sort_ByValue.equalsIgnoreCase("selling_price-dsc")) {
//                    ((RadioButton) radioGroup.getChildAt(3)).setChecked(true);
//                }
//                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(RadioGroup group, int checkedId) {
//                        String selectedRadioButton = ((RadioButton) view.findViewById(checkedId)).getText().toString();
//                        sortBy = true;
//                        pageNo = 1;
//                        if (selectedRadioButton != null && selectedRadioButton.equalsIgnoreCase("Product A - Z")) {
//                            sort_ByValue = null;
//                        } else if (selectedRadioButton != null && selectedRadioButton.equalsIgnoreCase("Product Z - A")) {
//                            sort_ByValue = "productname-dsc";
//                        } else if (selectedRadioButton != null && selectedRadioButton.equalsIgnoreCase("Price low to high")) {
//                            sort_ByValue = "selling_price-asc";
//                        } else if (selectedRadioButton != null && selectedRadioButton.equalsIgnoreCase("Price high to low")) {
//                            sort_ByValue = "selling_price-dsc";
//                        }
//                        if (NetworkConnectionHelper.isOnline(DemoActivity.this)) {
//                            getData();
//                        } else {
//                            Toast.makeText(DemoActivity.this, "Please check your internet connection! try again...", Toast.LENGTH_SHORT).show();
//                        }
//                        sortDialog.dismiss();
//                    }
//                });
//
//            }
//        });
//        tv_filter.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (drawer.isDrawerVisible(GravityCompat.END)) {
//                    drawer.closeDrawer(GravityCompat.END);
//                } else {
//                    drawer.openDrawer(GravityCompat.END);
//                }
//            }
//        });
//
//        tv_ApplyFilter.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                pageNo = 1;
//                if (et_minPrice.getText().toString().trim().equalsIgnoreCase("")) {
//                    by_minPriceValue = null;
//                } else {
//                    by_minPriceValue = et_minPrice.getText().toString().trim();
//                }
//                if (et_maxPrice.getText().toString().trim().equalsIgnoreCase("")) {
//                    by_maxPriceValue = null;
//                } else {
//                    by_maxPriceValue = et_maxPrice.getText().toString().trim();
//                }
//                by_BrandValue = "";
//                if (brandDataList != null && brandDataList.size() > 0) {
//                    for (BrandData brandData : brandDataList) {
//                        if (brandData.isChecked()) {
//                            if (!by_BrandValue.equalsIgnoreCase("")) {
//                                by_BrandValue += "," + brandData.getId();
//                            } else {
//                                by_BrandValue += brandData.getId();
//                            }
//                        }
//                    }
//                }
//                byDiscount_Value = "";
//                if (ch_5.isChecked()) {
//                    byDiscount_Value = byDiscount_Value + "5,";
//                }
//                if (ch_10.isChecked()) {
//                    byDiscount_Value = byDiscount_Value + "10,";
//                }
//                if (ch_15.isChecked()) {
//                    byDiscount_Value = byDiscount_Value + "15,";
//                }
//                if (ch_20.isChecked()) {
//                    byDiscount_Value = byDiscount_Value + "20,";
//                }
//                btn_Filter_click = true;
//                if (NetworkConnectionHelper.isOnline(DemoActivity.this)) {
//                    getData();
//                } else {
//                    Toast.makeText(DemoActivity.this, "Please check your internet connection! try again...", Toast.LENGTH_SHORT).show();
//                }
//                drawer.closeDrawers();
//            }
//        });
//        tv_clearFilter.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (brandDataList != null && brandDataList.size() > 0) {
//                    for (int i = 0; i < brandDataList.size(); i++) {
//                        brandDataList.get(i).setChecked(false);
//                    }
//                    pageNo = 1;
//                    byDiscount_Value = null;
//                    by_minPriceValue = null;
//                    by_maxPriceValue = null;
//                    btn_Filter_click = true;
//                    by_BrandValue = null;
//                    adapterForBrand.notifyDataSetChanged();
//                }
//                if (NetworkConnectionHelper.isOnline(DemoActivity.this)) {
//                    getData();
//                } else {
//                    Toast.makeText(DemoActivity.this, "Please check your internet connection! try again...", Toast.LENGTH_SHORT).show();
//                }
//                drawer.closeDrawers();
//            }
//        });
//        iv_discount.setRotation(90);
//        ll_discount.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (ll_discountView.getVisibility() == View.VISIBLE) {
//                    ll_discountView.setVisibility(View.GONE);
//                    iv_discount.setRotation(0);
//                } else {
//                    ll_discountView.setVisibility(View.VISIBLE);
//                    iv_discount.setRotation(90);
//                }
//            }
//        });
//
//        iv_backSearch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });
//
//        brandDataList = BrandData.createJsonInList(Constants.getSavedPreferences(DemoActivity.this, Constants.Brand_List, null));
//        if (brandDataList != null && brandDataList.size() > 0) {
//            ll_Brand.setVisibility(View.VISIBLE);
//            rv_Brand.setHasFixedSize(true);
//            rv_Brand.setLayoutManager(new GridLayoutManager(DemoActivity.this, 1));
//            adapterForBrand = new AdapterForBrand(DemoActivity.this, brandDataList);
//            rv_Brand.setAdapter(adapterForBrand);
//            iv_Brand.setRotation(0);
//        } else {
//            ll_Brand.setVisibility(View.GONE);
//        }
//        ll_Brand.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (rv_Brand.getVisibility() == View.VISIBLE) {
//                    rv_Brand.setVisibility(View.GONE);
//                    iv_Brand.setRotation(0);
//                } else {
//                    rv_Brand.setVisibility(View.VISIBLE);
//                    iv_Brand.setRotation(90);
//                }
//            }
//        });
//
////        cartDetails(DemoActivity.this,tv_productCount,tv_total_Price);
////        tv_View_Cart.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                startActivity(new Intent(DemoActivity.this, CartActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("fromWhere","Products"));
////            }
////        });
//    }
//
//    private void findViewByIds() {
//        rec_prodeuctData = findViewById(R.id.rec_prodeuctData);
//        txt_nodata = findViewById(R.id.txt_nodata);
//        srl_refresh = findViewById(R.id.srl_refresh);
//        et_search = findViewById(R.id.et_search);
//        btn_search = findViewById(R.id.btn_search);
//        ll_sort = findViewById(R.id.ll_sort);
//        tv_sort = findViewById(R.id.tv_sort);
//        tv_filter = findViewById(R.id.tv_filter);
//        toolbar = findViewById(R.id.toolbar);
//        //ll_sort.setVisibility(View.GONE);
//        drawer = findViewById(R.id.drawer_layout);
//        ll_search = findViewById(R.id.ll_search);
//
//        et_minPrice = findViewById(R.id.et_minPrice);
//        et_maxPrice = findViewById(R.id.et_maxPrice);
//        ch_5 = findViewById(R.id.ch_5);
//        ch_10 = findViewById(R.id.ch_10);
//        ch_15 = findViewById(R.id.ch_15);
//        ch_20 = findViewById(R.id.ch_20);
//        tv_clearFilter = findViewById(R.id.tv_clearFilter);
//        tv_ApplyFilter = findViewById(R.id.tv_ApplyFilter);
//
//        ll_discount = findViewById(R.id.ll_discount);
//        ll_discountView = findViewById(R.id.ll_discountView);
//        iv_discount = findViewById(R.id.iv_discount);
//        iv_backSearch = findViewById(R.id.iv_backSearch);
//
//        ll_Brand = findViewById(R.id.ll_Brand);
//        iv_Brand = findViewById(R.id.iv_Brand);
//        rv_Brand = findViewById(R.id.rv_Brand);
//
////        tv_productCount = findViewById(R.id.tv_productCount);
////        tv_total_Price = findViewById(R.id.tv_total_Price);
////        tv_View_Cart = findViewById(R.id.tv_View_Cart);
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//    }
//
//    private void implementPagination(GridLayoutManager linearLayoutManager) {
//        rec_prodeuctData.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
//            @Override
//            protected void loadMoreItems() {
//                isLast = false;
//                isLoad = true;
//                pageNo++;
//                // CommonUtill.print("loadMoreItems ==" + pageNo);
//                getData();
//                //rec_prodeuctData.addOnScrollListener(this);
//            }
//            @Override
//            public int getTotalPageCount() {
//                return lst_productData.size();
//            }
//            @Override
//            public boolean isLastPage() {
//                return isLast;
//            }
//            @Override
//            public boolean isLoading() {
//                return isLoad;
//            }
//        });
//
//    }
//
//    public void getData() {
//        if (NetworkConnectionHelper.isOnline(DemoActivity.this)) {
//            final ProgressDialog progressDialog = new ProgressDialog(DemoActivity.this);
//            progressDialog.setMessage("Loading");
//            progressDialog.show();
//            StringRequest stringRequest = new StringRequest(Request.Method.POST, Base_URl + url, new Response.Listener<String>() {
//                @Override
//                public void onResponse(String response) {
//                    progressDialog.dismiss();
//                    Toast.makeText(DemoActivity.this, response, Toast.LENGTH_SHORT).show();
//                    try {
//                        JSONObject jsonObject = new JSONObject(response);
//                        if (btn_search_click) {
//                            lst_productData.clear();
//                            btn_search_click = false;
//                        } else if (sortBy || btn_Filter_click) {
//                            lst_productData.clear();
//                            sortBy = false;
//                            btn_Filter_click = false;
//                        }
//                        if (jsonObject.getBoolean("status")) {
//                            JSONObject jsonObject1 = jsonObject.getJSONObject("data");
//                            String listInString = jsonObject1.getString("data");
//                            List<ProductData> productList = ProductData.createJsonInList(listInString);
//                            isLoad = false;
//                            if (productList != null && productList.size() > 0) {
//                                for (ProductData productData : productList) {
//                                    lst_productData.add(productData);
//                                }
//                            }
//                            //    GridLayoutManager gridLayoutManager = new GridLayoutManager(DemoActivity.this, 1);
//                            if (pageNo > 1) {
//                                adapterForProduct.notifyDataSetChanged();
//                            } else {
//                                //  GridLayoutManager gridLayoutManager = new GridLayoutManager(DemoActivity.this, 2);
//                                adapterForProduct = new AdapterForDemo(DemoActivity.this, lst_productData);
//                                rec_prodeuctData.setLayoutManager(gridLayoutManager);
//                                rec_prodeuctData.setAdapter(adapterForProduct);
//                                adapterForProduct.notifyDataSetChanged();
//                            }
//                        }
//                        if (lst_productData.size() > 0) {
//                            isLast = false;
//                            txt_nodata.setVisibility(View.GONE);
//                            rec_prodeuctData.setVisibility(View.VISIBLE);
//                            //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//                        } else {
//                            isLast = true;
//                            txt_nodata.setVisibility(View.VISIBLE);
//                            rec_prodeuctData.setVisibility(View.GONE);
//                            //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//                        }
//                        //    implementPagination(gridLayoutManager);
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    progressDialog.dismiss();
//                    // Toast.makeText(DemoActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            }) {
//                @Override
//                protected Map<String, String> getParams() throws AuthFailureError {
//                    HashMap<String, String> params = new HashMap<>();
//                    if (btn_search_click) {
//                        params.put("bykeyword", key);
//                        params.put("page", pageNo + "");
//                        params.put("city_id", Constants.getSavedPreferences(DemoActivity.this, Constants.CITY_ID, ""));
//                    } else if (sortBy || btn_Filter_click) {
//                        if (sort_ByValue != null) {
//                            params.put("sortby", sort_ByValue);
//                        }
//                        if (by_minPriceValue != null && !by_minPriceValue.equalsIgnoreCase("")) {
//                            params.put("byprice_min", by_minPriceValue);
//                        }
//                        if (by_maxPriceValue != null && !by_maxPriceValue.equalsIgnoreCase("")) {
//                            params.put("byprice_max", by_maxPriceValue);
//                        }
//                        if (by_BrandValue != null && !by_BrandValue.equalsIgnoreCase("")) {
//                            params.put("bybrand", by_BrandValue);
//                        }
//                        if (byDiscount_Value != null && !byDiscount_Value.equalsIgnoreCase("")) {
//                            params.put("sortby", sort_ByValue);
//                        }
//                        if (getIntent().getStringExtra("id") != null && !getIntent().getStringExtra("id").equalsIgnoreCase("")) {
//                            params.put("bycategory", getIntent().getStringExtra("id"));
//                        }
//                        params.put("page", pageNo + "");
//                        params.put("city_id", Constants.getSavedPreferences(DemoActivity.this, Constants.CITY_ID, ""));
//                    } else if (fromWhere != null && fromWhere.equalsIgnoreCase("Search")) {
//                        if (sort_ByValue != null) {
//                            params.put("sortby", sort_ByValue);
//                        }
//                        if (by_minPriceValue != null && !by_minPriceValue.equalsIgnoreCase("")) {
//                            params.put("byprice_min", by_minPriceValue);
//                        }
//                        if (by_maxPriceValue != null && !by_maxPriceValue.equalsIgnoreCase("")) {
//                            params.put("byprice_max", by_maxPriceValue);
//                        }
//                        if (byDiscount_Value != null && !byDiscount_Value.equalsIgnoreCase("")) {
//                            params.put("sortby", sort_ByValue);
//                        }
//                        if (!et_search.getText().toString().trim().equalsIgnoreCase("")) {
//                            params.put("bykeyword", key);
//                        }
//                        if (by_BrandValue != null && !by_BrandValue.equalsIgnoreCase("")) {
//                            params.put("bybrand", by_BrandValue);
//                        }
//                        params.put("page", pageNo + "");
//                        params.put("city_id", Constants.getSavedPreferences(DemoActivity.this, Constants.CITY_ID, ""));
//                    } else {
//                        if (sort_ByValue != null) {
//                            params.put("sortby", sort_ByValue);
//                        }
//                        if (by_minPriceValue != null && !by_minPriceValue.equalsIgnoreCase("")) {
//                            params.put("byprice_min", by_minPriceValue);
//                        }
//                        if (by_maxPriceValue != null && !by_maxPriceValue.equalsIgnoreCase("")) {
//                            params.put("byprice_max", by_maxPriceValue);
//                        }
//                        if (byDiscount_Value != null && !byDiscount_Value.equalsIgnoreCase("")) {
//                            params.put("sortby", sort_ByValue);
//                        }
//                        if (by_BrandValue != null && !by_BrandValue.equalsIgnoreCase("")) {
//                            params.put("bybrand", by_BrandValue);
//                        }
//                        params.put("page", pageNo + "");
//                        params.put("city_id", Constants.getSavedPreferences(DemoActivity.this, Constants.CITY_ID, ""));
//                        params.put("bycategory", getIntent().getStringExtra("id"));
//                        params.put("page", pageNo + "");
//                        params.put("city_id", Constants.getSavedPreferences(DemoActivity.this, Constants.CITY_ID, ""));
//                    }
//                    return params;
//                }
//            };
//            RequestQueue requestQueue = Volley.newRequestQueue(this);
//            requestQueue.add(stringRequest);
//        } else {
//            Toast.makeText(this, "Please check your internet connection! try again...", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if (id == android.R.id.home) {
//            onBackPressed();
//        }
//        if (id == R.id.opt_Search) {
//            ll_search.setVisibility(View.VISIBLE);
//            et_search.requestFocus();
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    @Override
//    public void onBackPressed() {
//        if (ll_search.getVisibility() == View.VISIBLE && fromWhere == null) {
//            ll_search.setVisibility(View.GONE);
//        } else if (drawer.isDrawerVisible(GravityCompat.END)) {
//            drawer.closeDrawer(GravityCompat.END);
//        } else {
//            finish();
//        }
//
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.opt_menu_products, menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    public static void cartDetails(Context context, TextView tv_productCount, TextView tv_total_Price) {
//        DataSource dataSource = new DataSource(context);
//        dataSource.open();
//        List<ProductData> list_Products = dataSource.selectInto_tbl_Products("", "");
//        dataSource.close();
//        if (list_Products != null && list_Products.size() > 0) {
//            tv_productCount.setText(list_Products.size() + " Items");
//            CartActivity.totalRsforCart(context, tv_total_Price, null);
//        } else {
//            tv_productCount.setText("0 Items");
//            tv_total_Price.setText("Rs. 0");
//        }
//    }
//}
