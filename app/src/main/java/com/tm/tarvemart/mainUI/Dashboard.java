package com.tm.tarvemart.mainUI;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.annotation.RequiresApi;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adefruandta.spinningwheel.SpinningWheelView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tm.tarvemart.Adapter.SectionsPagerAdapterBottom;
import com.tm.tarvemart.Helper.CurrentCityDialog;
import com.tm.tarvemart.Helper.NetworkConnectionHelper;
import com.tm.tarvemart.MoneyTransferActivity;
import com.tm.tarvemart.R;
import com.tm.tarvemart.Util.CommonUtill;
import com.tm.tarvemart.Util.Constants;
import com.tm.tarvemart.entity.SpinOfferData;
import com.tm.tarvemart.entity.User;
import com.tm.tarvemart.sqliteDatabase.DataSource;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;
import static com.tm.tarvemart.Util.Constants.CITY_ID;
import static com.tm.tarvemart.Util.Constants.CITY_NAME;
import static com.tm.tarvemart.Util.Constants.ID;
import static com.tm.tarvemart.inerface.GlobleInterfce.Base_URl;

public class Dashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnNavigationItemSelectedListener, SpinningWheelView.OnRotationListener {
    BottomNavigationView bottom_navigation;
    TextView tv_Location;
    ImageButton iv_edit;
    TextView tv_search;
    Toolbar toolbar;
    LinearLayout ll_search;
    DrawerLayout drawer;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    ViewPager viewPager;
    TextView tv_dashboard, tv_Mybooking, tv_myProfile, tv_MyQr, tv_help, tv_share, tv_rateUs, tv_signOut, txt_username, txt_usergmail, tv_MyOrder;
    Button btn_login;
    TextView txt_versionname;
    boolean doubleBackToExitPressedOnce = false;
    int productCount = 0;
    LinearLayout ll1;
    TextView tv_MyOffer;
    SectionsPagerAdapterBottom sectionsPagerAdapter;
    TextView tv_AllProducts, tv_Policy;
    CircleImageView civ_profile_image;
    private String referalcode;
    private SharedPreferences login_preference;
    private SharedPreferences.Editor login_editor;


    AlertDialog alertDialogWheel;

    String[] items = {"2%", "3%", "4%", "5%", "6%", "7%", "8%", "9%", "11%", "12%"};

    LinearLayout ll_wheel, ll_offerview;
    TextView tv_offers;
    Button rotate;

    private SharedPreferences.Editor editor;
    SharedPreferences prefs=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        findViewByIds();
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu);
        //toolbar.setOverflowIcon(ContextCompat.getDrawable(this, R.drawatv_MyOfferble.ic_menu));
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        sectionsPagerAdapter = new SectionsPagerAdapterBottom(this, getSupportFragmentManager());
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        viewPager.setCurrentItem(2);
        bottom_navigation.setOnNavigationItemSelectedListener(this);
        bottom_navigation.setSelectedItemId(R.id.bottom_Home);
        openCityoptions();
        viewPagerSelection();

        navigationMenuClickListener();
        setLogin();
        tv_Location.setText(Constants.getSavedPreferences(Dashboard.this, CITY_NAME, ""));
        //  tv_Location.setVisibility(View.VISIBLE);
        //  img_edit_city.setVisibility(View.VISIBLE);


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closedrawer();
                startActivity(new Intent(Dashboard.this, SendOtpActivity.class));
            }
        });

        DataSource dataSource = new DataSource(Dashboard.this);
        dataSource.open();
        productCount = dataSource.selectCountInto_tbl_Products("");
        dataSource.close();
        setVersion();
        showBadge(Dashboard.this, bottom_navigation, R.id.bottom_Basket, String.valueOf(productCount));

        Animation animation = AnimationUtils.loadAnimation(Dashboard.this, R.anim.blink);
        btn_login.setAnimation(animation);
        viewPager.beginFakeDrag();
        //checkNetConnection(Dashboard.this);
       // luckeyWheel();

        if (Constants.getSavedPreferences(Dashboard.this, ID, "").equalsIgnoreCase("")){
        }
        else {
            SpinOfferData spinOfferData = SpinOfferData.createObjectFromjson(Constants.getSavedPreferences(Dashboard.this, Constants.SpinOffer, null));
            if (spinOfferData == null) {
                luckeyWheel();
            } else if (spinOfferData != null && spinOfferData.getOfferdate() != null && !spinOfferData.getOfferdate().equalsIgnoreCase(new SimpleDateFormat("dd/MM/yyyy").format(new Date()))) {
                luckeyWheel();
            }
        }


//        SpinOfferData spinOfferData = SpinOfferData.createObjectFromjson(Constants.getSavedPreferences(Dashboard.this, Constants.SpinOffer, null));
//        if (Constants.getSavedPreferences(Dashboard.this, ID, "") == null) {
//            luckeyWheel();
//        } else if (spinOfferData != null && spinOfferData.getOfferdate() != null && !spinOfferData.getOfferdate().equalsIgnoreCase(new SimpleDateFormat("dd/MM/yyyy").format(new Date()))) {
//            luckeyWheel();
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        DataSource dataSource = new DataSource(Dashboard.this);
        dataSource.open();
        productCount = dataSource.selectCountInto_tbl_Products("");
        dataSource.close();
        showBadge(Dashboard.this, bottom_navigation, R.id.bottom_Basket, String.valueOf(productCount));
        setLogin();
        //checkNetConnection(Dashboard.this);
    }

    private void viewPagerSelection() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onPageSelected(int i) {
                if (i == 0) {
                    bottom_navigation.setSelectedItemId(R.id.bottom_categories);
                    ll_search.setVisibility(View.GONE);
                } else if (i == 1) {
                    ll_search.setVisibility(View.GONE);
                    // bottom_navigation.setSelectedItemId(R.id.bottom_Search);
                    Intent intent = new Intent(Dashboard.this, ActivityProduct.class);
                    intent.putExtra("fromWhere", "Search");
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else if (i == 2) {
                    bottom_navigation.setSelectedItemId(R.id.bottom_Home);
                    ll_search.setVisibility(View.GONE);
                } else if (i == 3) {
                    ll_search.setVisibility(View.GONE);
                    bottom_navigation.setSelectedItemId(R.id.bottom_Coupon);
                } else if (i == 4) {
                    ll_search.setVisibility(View.GONE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        startActivity(new Intent(Dashboard.this, CartActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK), ActivityOptions.makeCustomAnimation(Dashboard.this, R.anim.alpha_open, R.anim.alpha_close1).toBundle());
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        /*tv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, ActivityProduct.class);
                intent.putExtra("fromWhere", "Search");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });*/
    }

    private void findViewByIds() {
        bottom_navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        tv_Location = findViewById(R.id.tv_Location);
        iv_edit = findViewById(R.id.iv_edit);
        tv_search = findViewById(R.id.tv_search);
        ll_search = (LinearLayout) findViewById(R.id.ll_search);

        //Navigation Menu
        tv_dashboard = (TextView) findViewById(R.id.tv_dashboard);
        tv_Mybooking = (TextView) findViewById(R.id.tv_Mybooking);
        tv_myProfile = (TextView) findViewById(R.id.tv_myProfile);
        tv_MyQr = (TextView) findViewById(R.id.tv_MyQr);
        //tv_help = (TextView) findViewById(R.id.tv_help);
        tv_share = (TextView) findViewById(R.id.tv_share);
        tv_rateUs = (TextView) findViewById(R.id.tv_rateUs);
        tv_signOut = (TextView) findViewById(R.id.tv_signOut);
        txt_versionname = findViewById(R.id.txt_versionname);
        tv_MyOrder = findViewById(R.id.tv_MyOrder);
        // header
        btn_login = findViewById(R.id.btn_login);
        ll1 = findViewById(R.id.ll1);
        txt_usergmail = findViewById(R.id.txt_usergmail);
        txt_username = findViewById(R.id.txt_username);
        civ_profile_image = findViewById(R.id.civ_profile_image);
        //tv_MyOffer = findViewById(R.id.tv_MyOffer);
        //srl_Refresh = findViewById(R.id.srl_Refresh);

        //tv_AllProducts = findViewById(R.id.tv_AllProducts);
        tv_Policy = findViewById(R.id.tv_Policy);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (viewPager.getCurrentItem() == 2) {
                if (doubleBackToExitPressedOnce) {
                    Constants.clearSavedPreferences(Dashboard.this, CITY_NAME);
                    Constants.clearSavedPreferences(Dashboard.this, CITY_ID);
                    super.onBackPressed();
                    return;
                }
                this.doubleBackToExitPressedOnce = true;
                Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce = false;
                    }
                }, 2000);
            } else {
                viewPager.setCurrentItem(2);
            }


        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_wallet) {
            if (Constants.getSavedPreferences(Dashboard.this, ID, "") != null && !Constants.getSavedPreferences(Dashboard.this, ID, "").equalsIgnoreCase("")) {
                startActivity(new Intent(Dashboard.this, WalletActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK), ActivityOptions.makeCustomAnimation(Dashboard.this, R.anim.alpha_open, R.anim.alpha_close1).toBundle());
            } else {
                Toast.makeText(this, "First login. After that you can see your wallet.", Toast.LENGTH_LONG).show();
                startActivity(new Intent(Dashboard.this, SendOtpActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK), ActivityOptions.makeCustomAnimation(Dashboard.this, R.anim.alpha_open, R.anim.alpha_close1).toBundle());
            }
            return true;
        } else if (id == R.id.action_Notification) {
            Intent intent=new Intent(getApplicationContext(), MoneyTransferActivity.class);
            startActivity(intent);
          //  Toast.makeText(this, "Work in progress. comming soon...", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle Drawer navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
        } else if (id == R.id.nav_slideshow) {
        } else if (id == R.id.nav_tools) {
        } else if (id == R.id.nav_share) {
        } else if (id == R.id.nav_send) {
        }


        //Bottom Navigation Selection
        else if (id == R.id.bottom_categories) {
            viewPager.setCurrentItem(0);
        } else if (id == R.id.bottom_Search) {
            Intent intent = new Intent(Dashboard.this, ActivityProduct.class);
            intent.putExtra("fromWhere", "Search");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            //  viewPager.setCurrentItem(1);
        } else if (id == R.id.bottom_Home) {
            viewPager.setCurrentItem(2);
        } else if (id == R.id.bottom_Coupon) {
            viewPager.setCurrentItem(3);
        } else if (id == R.id.bottom_Basket) {
            //viewPager.setCurrentItem(4);
            startActivity(new Intent(Dashboard.this, CartActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


    void navigationMenuClickListener() {
        tv_dashboard.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                /*Intent intent = new Intent(Dashboard.this, Dashboard.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);*/
                viewPager.setCurrentItem(2);
                closedrawer();
            }
        });

        tv_Mybooking.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(Dashboard.this, CartActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                closedrawer();
            }
        });

        tv_myProfile.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                closedrawer();
                startActivity(new Intent(Dashboard.this, ProfileActivity.class));
            }
        });

        tv_MyQr.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(Dashboard.this, Contact_UsActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                /*CommonUtill.openBrowser(Dashboard.this, "http://bestpricedelivery.com/front/contact");
                closedrawer();*/
                closedrawer();
            }
        });

       /* tv_help.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CommonUtill.openBrowser(Dashboard.this, "http://bestpricedelivery.com/front/term");
                closedrawer();
            }
        });*/

        tv_share.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CommonUtill.shareApp(Dashboard.this, "Hey Download the TarveMart app. Use this referral code " + referalcode + " and get Rs. 50 in your wallet.", "com.tm.tarvemart");
                closedrawer();
            }
        });

        tv_rateUs.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CommonUtill.downloadApp(Dashboard.this, "com.tm.tarvemart");
                closedrawer();
            }
        });

        tv_signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closedrawer();
                Constants.clearSavedPreferences(Dashboard.this, ID);
                startActivity(new Intent(Dashboard.this, SplashActivity.class));
                closedrawer();
            }
        });
        iv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                closedrawer();
                new CurrentCityDialog(Dashboard.this, fragmentManager, tv_Location);
            }
        });

        tv_MyOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Dashboard.this, MyOrderActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                drawer.closeDrawers();
            }
        });
        /*tv_MyOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(3);
                closedrawer();
            }
        });*/

        /*tv_AllProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, ActivityProduct.class);
                intent.putExtra("fromWhere", "AllProducts");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                closedrawer();
            }
        });*/
        tv_Policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtill.openBrowser(Dashboard.this, "http://tarvemart.com/public/front/privacy");
                closedrawer();
            }
        });
    }


    void closedrawer() {
        drawer.closeDrawers();
    }

    void setVersion() {
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            txt_versionname.setText("VERSION : " + version);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showBadge(Context context, BottomNavigationView bottomNavigationView, @IdRes int itemId, String value) {
        removeBadge(bottomNavigationView, itemId);
        BottomNavigationItemView itemView = bottomNavigationView.findViewById(itemId);
        View badge = LayoutInflater.from(context).inflate(R.layout.layout_badge, bottomNavigationView, false);

        TextView text = badge.findViewById(R.id.badge_text_view);
        text.setText(value);
        itemView.addView(badge);
    }

    public static void removeBadge(BottomNavigationView bottomNavigationView, @IdRes int itemId) {
        BottomNavigationItemView itemView = bottomNavigationView.findViewById(itemId);
        if (itemView.getChildCount() == 3) {
            itemView.removeViewAt(2);
        }
    }


    public void openCityoptions() {
        if (NetworkConnectionHelper.isOnline(Dashboard.this)) {
            String city = Constants.getSavedPreferences(Dashboard.this, CITY_NAME, null);
            if (city != null && !city.equalsIgnoreCase("")) {
                tv_Location.setText(city);
            } else {
                FragmentManager fragmentManager = getSupportFragmentManager();
                new CurrentCityDialog(Dashboard.this, fragmentManager, tv_Location);
            }
        } else {
            Toast.makeText(this, "Please check your internet connection! try again...", Toast.LENGTH_SHORT).show();
        }
    }

    public void setLogin() {
        if (!Constants.getSavedPreferences(Dashboard.this, ID, "").equalsIgnoreCase("")) {
            tv_myProfile.setVisibility(View.VISIBLE);
            ll1.setVisibility(View.VISIBLE);
            tv_MyOrder.setVisibility(View.VISIBLE);
            btn_login.setVisibility(View.GONE);
            tv_signOut.setVisibility(View.VISIBLE);
            getProfileData();
        } else {
            tv_myProfile.setVisibility(View.GONE);
            ll1.setVisibility(View.GONE);
            tv_MyOrder.setVisibility(View.GONE);
            btn_login.setVisibility(View.VISIBLE);
            tv_signOut.setVisibility(View.GONE);
        }
    }

    public static void checkNetConnection(final Context context) {
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (NetworkConnectionHelper.isOnline(context)) {

                } else {
                    context.startActivity(new Intent(context, NoInternetConnectionActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    timer.purge();
                    timer.cancel();
                }
            }
        }, 2000, 2000);
    }

    public void getProfileData() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        String url = "view-profile";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Base_URl + url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                //    Toast.makeText(SendOtpActivity.this, response, Toast.LENGTH_SHORT).show();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean("status")) {
                        String data = jsonObject.getString("data");
                        if (data != null && !data.equalsIgnoreCase("{}")) {
                            User user = User.createJsonInObjcet(data.toString());
                            if (user != null) {
                                txt_usergmail.setText(user.getEmail());
                                txt_username.setText(user.getName());
                                Picasso.with(Dashboard.this).load(user.getProfile_picture())
                                        .placeholder(R.drawable.logo)
                                        .error(R.drawable.logo).into(civ_profile_image);
                                referalcode = user.getReferel_code();
                            }
                        }
                    } else {
                        Constants.clearSavedPreferences(Dashboard.this, ID);
                        setLogin();
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
                params.put("customer_id", Constants.getSavedPreferences(Dashboard.this, ID, null));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Dashboard.this);
        requestQueue.add(stringRequest);
    }

    void luckeyWheel() {
        View view = LayoutInflater.from(Dashboard.this).inflate(R.layout.dialog_for_luckywheel, null, false);
        final SpinningWheelView wheelView = (SpinningWheelView) view.findViewById(R.id.wheel);
        rotate = (Button) view.findViewById(R.id.rotate);
        ImageView iv_cancel = (ImageView) view.findViewById(R.id.iv_cancel);
        ll_wheel = view.findViewById(R.id.ll_wheel);
        ll_offerview = view.findViewById(R.id.ll_offerview);
        tv_offers = view.findViewById(R.id.tv_offers);

        ll_wheel.setVisibility(View.VISIBLE);
        ll_offerview.setVisibility(View.GONE);
        AlertDialog.Builder builder = new AlertDialog.Builder(Dashboard.this);
        builder.setCancelable(false);
        builder.setView(view);
        alertDialogWheel = builder.show();
        //  wheelView.setItems(R.array.dummy);
        wheelView.setItems(Arrays.asList(items));
        wheelView.setOnRotationListener(this);
        wheelView.setEnabled(false);

        wheelView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });

        iv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialogWheel.dismiss();
            }
        });


        rotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rotate.getText().toString().equalsIgnoreCase("OK")) {
                    alertDialogWheel.dismiss();
                } else {
                    Random random = new Random();
                    // max angle 50
                    // duration 10 second
                    // every 50 ms rander rotation
                    float maxAngle = Float.parseFloat((random.nextInt(360)) + "");
                    long maxDuration = random.nextInt(5000) + 3000;
                    long maxInterval = random.nextInt(50);

                    wheelView.rotate(50, maxDuration, maxInterval);
                }
            }
        });
    }

    @Override
    public void onRotation() {
        Log.d("XXXX", "On Rotation");
    }


    @Override
    public void onStopRotation(final Object item) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ll_wheel.setVisibility(View.GONE);
                ll_offerview.setVisibility(View.VISIBLE);
                tv_offers.setText(item + "");
                SpinOfferData spinOfferData = new SpinOfferData();
                spinOfferData.setOfferPer(item + "");

                prefs = getSharedPreferences("com.tm.tarvemart.mainUI", MODE_PRIVATE);
                if (prefs.getBoolean("offfer_preference", true)) {
                    //login perference
                    login_preference = getSharedPreferences("offer_preference", MODE_PRIVATE);
                    login_editor = login_preference.edit();
                    login_editor.putString("offer_percentage", tv_offers.getText().toString());
                    login_editor.commit();
                    //You can perform anything over here. This will call only first time
                    editor = prefs.edit();
                    editor.putBoolean("offfer_preference", false);
                    editor.commit();
                }


                spinOfferData.setOfferdate(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
                Constants.savePreferences(Dashboard.this, Constants.SpinOffer, SpinOfferData.createJsonFromObject(spinOfferData));
                rotate.setText("OK");
            }
        }, 1000);

        //Toast.makeText(this, , Toast.LENGTH_LONG).show();
        //alertDialogWheel.dismiss();
    }

}
