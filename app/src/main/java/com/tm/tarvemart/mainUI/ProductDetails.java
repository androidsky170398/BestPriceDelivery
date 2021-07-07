package com.tm.tarvemart.mainUI;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tm.tarvemart.Adapter.AdapterForMultipleBanner;
import com.tm.tarvemart.ColorImageLIst;
import com.tm.tarvemart.ColorSize_List;
import com.tm.tarvemart.R;
import com.tm.tarvemart.entity.BannerData;
import com.tm.tarvemart.entity.ProductData;
import com.tm.tarvemart.sqliteDatabase.DataSource;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import static com.tm.tarvemart.inerface.GlobleInterfce.Base_URl;

public class ProductDetails extends AppCompatActivity {
    ImageView img_product, iv_brandImage;
    TextView tv_City, tv_ProductName, tv_ProductDescription, tv_BrandName;
    Button btn_BuyNow, btn_AddtoCart;
    LinearLayout ll_forbtn;
    ProductData productData;
    String fromWhere;
    private String productID;
    private int buttonState = 1;
    private TextView productCuttedPrice;
    private TextView productPrice;
    private TextView productDiscount;
    private TextView description_1;
    private TextView description_2;
    private TextView description_3;
    private LinearLayout sizeLayout,imageLayout;
    private LinearLayout sizeTypeLayout,imageTypeLayout;
    private LinearLayout colorLayout;
    private LinearLayout colorTypeLayout;
    private View viewtype1, viewtype2;
    private String quantity_name,size_id,color_name,color_code,color_id;
    private ArrayList<ColorSize_List> size_dataArray,colorsize_dataArray;
    private ViewPager viewPager_banner;
    private List<BannerData> bannerData;
    private static int currentPage = 0;
    private CirclePageIndicator indicator;
    private View viewtype3;
    private String galary_no_id, galary_productid, galary_imgurl,galary_status,galary_created,galary_galary_id;
    private ArrayList<ColorImageLIst> image_dataArray;
    private TextView textview_tv;
    private TextView selected_tv;
    private String productImage;
    private String JsonInString;
    private ImageView productimage;
    private TextView des;
    private View sp_quantityName;
    private TextView quantity_tv;
    private String productQty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Product Details");
        findViewByIds();
        Intent intent = getIntent();
        productData = (ProductData) intent.getSerializableExtra("ProductDetails");
        fromWhere = intent.getStringExtra("fromWhere");
        productID = intent.getStringExtra("product_id");
        productQty = intent.getStringExtra("product_quantity");

        size_dataArray = new ArrayList<ColorSize_List>();
        colorsize_dataArray = new ArrayList<ColorSize_List>();
        image_dataArray = new ArrayList<ColorImageLIst>();


        viewPager_banner = (ViewPager) findViewById(R.id.viewPager_banner);
        indicator = (CirclePageIndicator) findViewById(R.id.indicator);
        productimage=(ImageView)findViewById(R.id.productimage);


        selected_tv=(TextView)findViewById(R.id.selected_tv);
        des=(TextView)findViewById(R.id.des);
        quantity_tv=(TextView)findViewById(R.id.quantity_tv);
        quantity_tv.setText(productQty);

        //  Toast.makeText(getApplicationContext(), fromWhere+" "+productID, Toast.LENGTH_LONG).show();
        if (fromWhere.equalsIgnoreCase("Garments")) {
            sizeLayout.setVisibility(View.VISIBLE);
           // imageLayout.setVisibility(View.VISIBLE);
           colorLayout.setVisibility(View.VISIBLE);
           // viewtype2.setVisibility(View.VISIBLE);
            viewtype1.setVisibility(View.VISIBLE);
            viewtype3.setVisibility(View.VISIBLE);
            getproductDetails();
        } else if (fromWhere.equalsIgnoreCase("Footwear")) {
            sizeLayout.setVisibility(View.VISIBLE);
           // imageLayout.setVisibility(View.VISIBLE);
            colorLayout.setVisibility(View.VISIBLE);
           // viewtype2.setVisibility(View.VISIBLE);
            viewtype1.setVisibility(View.VISIBLE);
            viewtype3.setVisibility(View.VISIBLE);
            getproductDetails();
        } else {
            sizeLayout.setVisibility(View.GONE);
            imageLayout.setVisibility(View.GONE);
            colorLayout.setVisibility(View.GONE);
            viewtype2.setVisibility(View.GONE);
            viewtype1.setVisibility(View.GONE);
            viewtype3.setVisibility(View.GONE);
            getproductDetails();
        }

       // getproductDetails();
        if (fromWhere != null && fromWhere.equalsIgnoreCase("Cart")) {
            ll_forbtn.setVisibility(View.GONE);
            quantity_tv.setText(productQty);
        } else if (fromWhere != null && fromWhere.equalsIgnoreCase("MyOrder")) {

        } else {
            if (Integer.parseInt(productData.getTotal_availability()) > 0) {
                ll_forbtn.setVisibility(View.VISIBLE);
            } else {
                ll_forbtn.setVisibility(View.GONE);
            }
        }
        if (productData != null) {
            if (productData.getProduct_image() != null && !productData.getProduct_image().equalsIgnoreCase("")) {
                Picasso.with(ProductDetails.this).load(productData.getProduct_image()).placeholder(R.drawable.logo).into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        productimage.setImageBitmap(bitmap);
                        productimage.setScaleType(ImageView.ScaleType.FIT_XY);
                    }
                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                    }
                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {
                    }
                });
            }

            if (productData.getProductname() != null && !productData.getProductname().equalsIgnoreCase("")) {
                tv_ProductName.setText(productData.getProductname());
            } else {
                tv_ProductName.setText("N/A");
            }

            if (productData.getCity_id() != null && !productData.getCity_id().equalsIgnoreCase("")) {
                tv_City.setText(productData.getCity_name());
            } else {
                tv_City.setText("N/A");
            }
            if (productData.getBrand_image() != null && !productData.getBrand_image().equalsIgnoreCase("")) {
                Picasso.with(ProductDetails.this).load(productData.getBrand_image()).placeholder(R.drawable.logo).into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        iv_brandImage.setImageBitmap(bitmap);
                        iv_brandImage.setScaleType(ImageView.ScaleType.FIT_XY);
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {
                    }
                });
            } else {
            }
            if (productData.getBrandname() != null && !productData.getBrandname().equalsIgnoreCase("")) {
                tv_BrandName.setText(productData.getBrandname());
            } else {
                tv_BrandName.setText("N/A");
            }
            if (productData.getDescription() != null && !productData.getDescription().equalsIgnoreCase("")) {
                tv_ProductDescription.setText(productData.getDescription());
            } else {
                tv_ProductDescription.setText("N/A");
            }
        }

        btn_AddtoCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = 0;
                DataSource dataSource = new DataSource(ProductDetails.this);
                dataSource.open();
                ProductData productData1;
                try {
                    productData1 = dataSource.selectInto_tbl_Products(productData.getId(), productData.getQuantity_name()).get(0);
                } catch (Exception e) {
                    productData1 = null;
                }
                if (productData1 != null) {
                    productData1.setSelected_quantity((Integer.parseInt(productData1.getSelected_quantity()) + 1) + "");
                    count = dataSource.updateInto_tbl_Products(productData1.getCommon_id(), productData1);
                } else {
                    count = (int) dataSource.insertInto_tbl_Products(productData);
                }
                dataSource.close();
                if (count > 0) {
                    Toast.makeText(ProductDetails.this, "Product added successfully!!!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ProductDetails.this, CartActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                }
            }
        });
        //Dashboard.checkNetConnection(ProductDetails.this);
    }

    public void getproductDetails() {
        String url = "product-details";
        StringRequest request = new StringRequest(Request.Method.POST, Base_URl + url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean("status")) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        // JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        if (jsonObject1.has("data")) {
                            JSONArray jsonArray = jsonObject1.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject getDetails = jsonArray.getJSONObject(i);
                                productCuttedPrice.setText("MRP." + getDetails.getString("price"));
                                productCuttedPrice.setPaintFlags(productCuttedPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                                productPrice.setText(getDetails.getString("selling_price"));
                                if (getDetails.getString("percent_off") != null && !getDetails.getString("percent_off").equalsIgnoreCase("0.00")) {
                                    productDiscount.setText("(" + getDetails.getString("percent_off") + "%)");
                                } else {
                                    productDiscount.setVisibility(View.GONE);
                                }

                                des.setText(getDetails.getString("description"));

                                des.setText(getDetails.getString("description"));
                                description_1.setText(getDetails.getString("product_about"));
                                description_2.setText(getDetails.getString("product_ingredient"));
                                description_3.setText(getDetails.getString("productinfo"));
                                productImage = getDetails.getString("product_image");

                                JsonInString = getDetails.getString("gallery");
                                bannerData = BannerData.createJsonInList(JsonInString);
                                viewPager_banner.setAdapter(new AdapterForMultipleBanner(ProductDetails.this, bannerData));
                                indicator.setViewPager(viewPager_banner);
                                final float density = getResources().getDisplayMetrics().density;
                                indicator.setRadius(2 * density);
                                // Auto start of viewpager
                                final Handler handler = new Handler();
                                final Runnable Update = new Runnable() {
                                    public void run() {
                                        if (currentPage == bannerData.size()) {
                                            currentPage = 0; }
                                        viewPager_banner.setCurrentItem(currentPage++, true);
                                    }
                                };
                                Timer swipeTimer = new Timer();
                                swipeTimer.schedule(new TimerTask() {
                                    @Override
                                    public void run() {
                                        handler.post(Update); }
                                }, 5000, 3000);
// Pager listener over indicator
                                indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                                    @Override
                                    public void onPageSelected(int position) {
                                        currentPage = position; }
                                    @Override
                                    public void onPageScrolled(int pos, float arg1, int arg2) { }
                                    @Override
                                    public void onPageScrollStateChanged(int pos) { }
                                });


                                if (getDetails.has("sizes")) {
                                    JSONArray size = getDetails.getJSONArray("sizes");
                                    for (int j = 0; j < size.length(); j++) {
                                        JSONObject sizeObject = size.getJSONObject(j);
                                        quantity_name = sizeObject.getString("quantity_name");
                                        size_id = sizeObject.getString("size_id");

                                        ColorSize_List size_list = new ColorSize_List(quantity_name, size_id);
                                        size_dataArray.add(size_list);
                                        getSizeList(j); } }


                                if (getDetails.has("colors")) {
                                    JSONArray color = getDetails.getJSONArray("colors");
                                    for (int k = 0; k < color.length(); k++) {
                                        JSONObject colorObject = color.getJSONObject(k);
                                        color_name = colorObject.getString("color_name");
                                        color_code = colorObject.getString("color_code");
                                        color_id = colorObject.getString("color_id");
                                        ColorSize_List color_list = new ColorSize_List(color_name, color_code, color_id);
                                        colorsize_dataArray.add(color_list);
                                        getColorLIst(k);
                                    } }

                                if (getDetails.has("gallery")) {
                                    JSONArray color = getDetails.getJSONArray("gallery");
                                    for (int l = 0; l < color.length(); l++) {
                                        JSONObject colorObject = color.getJSONObject(l);
                                        galary_no_id = colorObject.getString("id");
                                        galary_productid = colorObject.getString("product_id");
                                        galary_imgurl = colorObject.getString("image_url");
                                        galary_status = colorObject.getString("status");
                                        galary_created = colorObject.getString("created_at");
                                        galary_galary_id = colorObject.getString("gallery_id");
                                        ColorImageLIst color_list = new ColorImageLIst(galary_no_id
                                                , galary_productid, galary_imgurl, galary_status, galary_created, galary_galary_id);
                                        image_dataArray.add(color_list);
                                        getdynamicImageList(l);
                                    }
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

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("product_id", productID);
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }


    private void getSizeList(final int pos) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View convertView = inflater.inflate(R.layout.dynamic_size, null);
        sizeTypeLayout.addView(convertView);


        textview_tv=(TextView)convertView.findViewById(R.id.textview_tv);
        textview_tv.setText(size_dataArray.get(pos).getQuantity_name());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                colorTypeLayout.removeAllViews();
                String url = "product-details";
                StringRequest request = new StringRequest(Request.Method.POST, Base_URl + url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        selected_tv.setVisibility(View.VISIBLE);
                        selected_tv.setText("*Select Size is  "+size_dataArray.get(pos).getQuantity_name());
                      //  Toast.makeText(getApplicationContext(),"Select Size is  "+size_dataArray.get(pos).getQuantity_name(),Toast.LENGTH_LONG).show();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getBoolean("status")) {
                                JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                                // JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                                if (jsonObject1.has("data")) {
                                    JSONArray jsonArray = jsonObject1.getJSONArray("data");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject getDetails = jsonArray.getJSONObject(i);
                                        productCuttedPrice.setText("Rs." + getDetails.getString("price"));
                                        productCuttedPrice.setPaintFlags(productCuttedPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                                        productPrice.setText(getDetails.getString("selling_price"));
                                        if (getDetails.getString("percent_off") != null && !getDetails.getString("percent_off").equalsIgnoreCase("0.00")) {
                                            productDiscount.setText("(" + getDetails.getString("percent_off") + "%)");
                                        } else {
                                            productDiscount.setVisibility(View.GONE);
                                        }
                                        description_1.setText(getDetails.getString("product_about"));
                                        description_2.setText(getDetails.getString("product_ingredient"));
                                        description_3.setText(getDetails.getString("productinfo"));
                                        productImage = getDetails.getString("product_image");

                                        JsonInString = getDetails.getString("gallery");
                                        bannerData = BannerData.createJsonInList(JsonInString);
                                        viewPager_banner.setAdapter(new AdapterForMultipleBanner(ProductDetails.this, bannerData));
                                        indicator.setViewPager(viewPager_banner);
                                        final float density = getResources().getDisplayMetrics().density;
                                        indicator.setRadius(2 * density);
                                        // Auto start of viewpager
                                        final Handler handler = new Handler();
                                        final Runnable Update = new Runnable() {
                                            public void run() {
                                                if (currentPage == bannerData.size()) {
                                                    currentPage = 0; }
                                                viewPager_banner.setCurrentItem(currentPage++, true);
                                            }
                                        };
                                        Timer swipeTimer = new Timer();
                                        swipeTimer.schedule(new TimerTask() {
                                            @Override
                                            public void run() {
                                                handler.post(Update); }
                                        }, 5000, 3000);
// Pager listener over indicator
                                        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                                            @Override
                                            public void onPageSelected(int position) {
                                                currentPage = position; }
                                            @Override
                                            public void onPageScrolled(int pos, float arg1, int arg2) { }
                                            @Override
                                            public void onPageScrollStateChanged(int pos) { }
                                        });


                                        if (getDetails.has("colors")) {
                                            JSONArray color = getDetails.getJSONArray("colors");
                                            for (int k = 0; k < color.length(); k++) {
                                                JSONObject colorObject = color.getJSONObject(k);
                                                color_name = colorObject.getString("color_name");
                                                color_code = colorObject.getString("color_code");
                                                color_id = colorObject.getString("color_id");
                                                ColorSize_List color_list = new ColorSize_List(color_name, color_code, color_id);
                                                colorsize_dataArray.add(color_list);
                                                getColorLIst(k);
                                            } }
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

                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("product_id", productID);
                        params.put("size_id", size_dataArray.get(pos).getSize_id());
                        return params;
                    }
                };
                RequestQueue queue = Volley.newRequestQueue(ProductDetails.this);
                queue.add(request);
            }
        });

        View view = new View(ProductDetails.this);
        LinearLayout.LayoutParams params_view = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 10);
        view.setLayoutParams(params_view);
        sizeTypeLayout.addView(view);
    }

    private void getColorLIst(final int position) {
        LayoutInflater inflater1 = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View convertView1 = inflater1.inflate(R.layout.dynamic_color, null);
        colorTypeLayout.addView(convertView1);

        TextView color_tv=(TextView)convertView1.findViewById(R.id.color_tv);
        // color_tv.setText(colorsize_dataArray.get(i).getcolor_code());

        if (colorsize_dataArray.get(position).getColor_code().equals("")){
        }
        else {
            color_tv.setBackgroundColor(Color.parseColor(colorsize_dataArray.get(position).getColor_code()));
        }
        convertView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "product-details";
                StringRequest request = new StringRequest(Request.Method.POST, Base_URl + url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getBoolean("status")) {
                                JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                                // JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                                if (jsonObject1.has("data")) {
                                    JSONArray jsonArray = jsonObject1.getJSONArray("data");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject getDetails = jsonArray.getJSONObject(i);
                                        productCuttedPrice.setText("Rs." + getDetails.getString("price"));
                                        productCuttedPrice.setPaintFlags(productCuttedPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                                        productPrice.setText(getDetails.getString("selling_price"));
                                        if (getDetails.getString("percent_off") != null && !getDetails.getString("percent_off").equalsIgnoreCase("0.00")) {
                                            productDiscount.setText("(" + getDetails.getString("percent_off") + "%)");
                                        } else {
                                            productDiscount.setVisibility(View.GONE);
                                        }
                                        description_1.setText(getDetails.getString("product_about"));
                                        description_2.setText(getDetails.getString("product_ingredient"));
                                        description_3.setText(getDetails.getString("productinfo"));
                                        productImage = getDetails.getString("product_image");

                                        JsonInString = getDetails.getString("gallery");
                                        bannerData = BannerData.createJsonInList(JsonInString);
                                        viewPager_banner.setAdapter(new AdapterForMultipleBanner(ProductDetails.this, bannerData));
                                        indicator.setViewPager(viewPager_banner);
                                        final float density = getResources().getDisplayMetrics().density;
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
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("product_id", productID);
                        params.put("color_id", colorsize_dataArray.get(position).getColor_id());
                        return params;
                    }
                };
                RequestQueue queue = Volley.newRequestQueue(ProductDetails.this);
                queue.add(request);
            }
        });

        View view1 = new View(ProductDetails.this);
        LinearLayout.LayoutParams params_view1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 10);
        view1.setLayoutParams(params_view1);
        colorTypeLayout.addView(view1);
    }


    private void getdynamicImageList(final int posi)
    {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View convertView2 = inflater.inflate(R.layout.dynamic_color_img, null);
        imageTypeLayout.addView(convertView2);
        ImageView color_img=(ImageView)convertView2.findViewById(R.id.color_img);
        color_img.setPadding(0,0,10,0);
        Picasso.with(ProductDetails.this).load(image_dataArray.get(posi).getGalary_imgurl())
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background).fit().into(color_img);

        convertView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "product-details";
                StringRequest request = new StringRequest(Request.Method.POST, Base_URl + url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getBoolean("status")) {
                                JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                                // JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                                if (jsonObject1.has("data")) {
                                    JSONArray jsonArray = jsonObject1.getJSONArray("data");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject getDetails = jsonArray.getJSONObject(i);
                                        productCuttedPrice.setText("Rs." + getDetails.getString("price"));
                                        productCuttedPrice.setPaintFlags(productCuttedPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                                        productPrice.setText(getDetails.getString("selling_price"));
                                        if (getDetails.getString("percent_off") != null && !getDetails.getString("percent_off").equalsIgnoreCase("0.00")) {
                                            productDiscount.setText("(" + getDetails.getString("percent_off") + "%)");
                                        } else {
                                            productDiscount.setVisibility(View.GONE);
                                        }
                                        description_1.setText(getDetails.getString("product_about"));
                                        description_2.setText(getDetails.getString("product_ingredient"));
                                        description_3.setText(getDetails.getString("productinfo"));
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
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("product_id", productID);
                        params.put("gallery_id", image_dataArray.get(posi).getGalary_galary_id());
                        return params;
                    }
                };
                RequestQueue queue = Volley.newRequestQueue(ProductDetails.this);
                queue.add(request);
            }
        });


        View view11 = new View(ProductDetails.this);
        LinearLayout.LayoutParams params_view1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 10);
        view11.setLayoutParams(params_view1);
        view11.setPadding(0,0,5,0);
        imageTypeLayout.addView(view11);
    }


    @Override
    protected void onResume() {
        super.onResume();
        //Dashboard.checkNetConnection(ProductDetails.this);
    }

    private void findViewByIds() {
        img_product = findViewById(R.id.img_product);
        iv_brandImage = findViewById(R.id.iv_brandImage);
        tv_City = findViewById(R.id.tv_City);
        tv_ProductName = findViewById(R.id.tv_ProductName);
        tv_ProductDescription = findViewById(R.id.tv_ProductDescription);
        btn_BuyNow = findViewById(R.id.btn_BuyNow);
        btn_AddtoCart = findViewById(R.id.btn_AddtoCart);
        ll_forbtn = findViewById(R.id.ll_forbtn);
        tv_BrandName = findViewById(R.id.tv_BrandName);
        productCuttedPrice = findViewById(R.id.product_cutted_price);
        productPrice = findViewById(R.id.product_discounted_price);
        productDiscount = findViewById(R.id.discount_at_product);
        description_1 = findViewById(R.id.description_1);
        description_2 = findViewById(R.id.description_2);
        description_3 = findViewById(R.id.description_3);
        sizeLayout = findViewById(R.id.size_layout);
        sizeTypeLayout = findViewById(R.id.size_type);
        colorLayout = findViewById(R.id.color_layout);
        colorTypeLayout = findViewById(R.id.color_type);
        sp_quantityName = findViewById(R.id.sp_quantityName);
        imageLayout = findViewById(R.id.image_layout);
        imageTypeLayout = findViewById(R.id.image_type);

        viewtype1 = findViewById(R.id.view_type1);
        viewtype2 = findViewById(R.id.view_type2);
        viewtype3 = findViewById(R.id.view_type3);
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
