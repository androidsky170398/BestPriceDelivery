package com.tm.tarvemart.mainUI;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tm.tarvemart.R;
import com.tm.tarvemart.Util.Constants;
import com.tm.tarvemart.entity.User;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.tm.tarvemart.Util.Constants.ID;
import static com.tm.tarvemart.inerface.GlobleInterfce.Base_URl;

public class ProfileActivity extends AppCompatActivity {

    TextInputEditText edt_name, edt_email, edt_mobile;
    CircleImageView img_showprofilepic;
    private String fileName, state, city;
    private String SourcefilePath;
    Button btn_update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Profile");
        edt_name = findViewById(R.id.edt_name);
        edt_email = findViewById(R.id.edt_email);
        edt_mobile = findViewById(R.id.edt_mobile);
        img_showprofilepic = findViewById(R.id.img_showprofilepic);
        btn_update = findViewById(R.id.btn_update);
        edt_mobile.setKeyListener(null);
        getProfileData();
        // Dashboard.checkNetConnection(ProfileActivity.this);
        img_showprofilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
            }
        });

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfileData();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Dashboard.checkNetConnection(ProfileActivity.this);
    }

    public void updateProfileData() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        String url = "profile-update";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Base_URl + url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
              //   Toast.makeText(ProfileActivity.this, response, Toast.LENGTH_SHORT).show();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean("status")) {
                        Toast.makeText(ProfileActivity.this, jsonObject.getString("message") + "", Toast.LENGTH_SHORT).show();
                        String data = jsonObject.getString("data");
                        finish();
                    } else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {


            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> params = new HashMap<>();
                params.put("id", Constants.getSavedPreferences(ProfileActivity.this, ID, null));
                params.put("name", edt_name.getText().toString().trim());
                params.put("email", edt_email.getText().toString().trim());
                params.put("state", state);
                params.put("city", city);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(ProfileActivity.this);
        requestQueue.add(stringRequest);
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
                                edt_name.setText(user.getName());
                                edt_email.setText(user.getEmail());
                                edt_mobile.setText(user.getMobile());
                                Picasso.with(ProfileActivity.this).load(user.getProfile_picture())
                                        .placeholder(R.drawable.logo)
                                        .error(R.drawable.logo).fit().into(img_showprofilepic);
                                state = user.getState();
                                city = user.getCity();
                            }
                        }
                    } else {

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
                params.put("customer_id", Constants.getSavedPreferences(ProfileActivity.this, ID, null));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(ProfileActivity.this);
        requestQueue.add(stringRequest);
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    try {
                        Bitmap bitmap;
                        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                        BitmapFactory.Options bitmapOptionsForCompression = new BitmapFactory.Options();
                        bitmap = BitmapFactory.decodeFile("", bitmapOptions);
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                        FileOutputStream fos = null;
                        ByteArrayOutputStream streamForCompression = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, streamForCompression);
                        byte[] bArrayForCompression = streamForCompression.toByteArray();
                        File CompressedDir = getDir("Images", 0);
                        fileName = System.currentTimeMillis() + ".jpg";
                        File fileNew = new File(CompressedDir, fileName);
                        try {
                            fos = new FileOutputStream(fileNew);
                            fos.write(bArrayForCompression);
                            fos.flush();
                            fos.close();
                            SourcefilePath = fileNew.toString();
                            img_showprofilepic.setImageBitmap(bitmap);
                        } catch (Exception e) {
                            Log.e("Error : ", e.getMessage());
                        }

                        imageUpload(SourcefilePath);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(ProfileActivity.this, "Canceled", Toast.LENGTH_SHORT).show();
            }
        }
    }


    void imageUpload(String sourcefilePath) {
        try {
            Ion.with(this).
                    load(Base_URl + "update-photo")
                    .setMultipartParameter("customer_id", Constants.getSavedPreferences(ProfileActivity.this, ID, null))
                    .setMultipartFile("image",
                            new File(sourcefilePath.trim())).asJsonObject()
                    .setCallback(
                            new FutureCallback<JsonObject>() {
                                @Override
                                public void onCompleted(Exception e, JsonObject result) {
                                    if (result.getAsJsonObject().get("status") != null && result.getAsJsonObject().get("status").toString().equalsIgnoreCase("true")) {
                                        Toast.makeText(ProfileActivity.this, result.getAsJsonObject().get("message") + "", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(ProfileActivity.this, result.getAsJsonObject().get("message") + "", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
        } catch (Exception e) {
            // Do something about exceptions
            System.out.println("exception: " + e);
        }
    }

}
