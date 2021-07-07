package com.tm.tarvemart.Map;

import android.Manifest;
import android.app.Activity;


import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.provider.Settings.Secure;
import android.provider.Settings.SettingNotFoundException;


import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.tm.tarvemart.R;

import java.text.SimpleDateFormat;
import java.util.Locale;


public class GpsTrackerForGoogleMap implements LocationListener {

    private static final int MY_PERMISSION_ACCESS_COURSE_LOCATION = 1;
    private static final int MY_PERMISSION_ACCESS_FINE_LOCATION = 2;

    public Context context;
    // public Fill_center_DetailActivity contextForFill_center_DetailActivity;
    int mode = 0;
    LocationManager locManager;
    Location location;
    long minTime = 1000;// 1 second
    float minDistance = 5;// 5 meter

    public GpsTrackerForGoogleMap(Context applicationContext) {
        this.context = applicationContext;
        getLocation(context);
    }

    public GpsTrackerForGoogleMap(Context context, FragmentManager fragmentManager) {
    }

    private void getLocation(Context context2) {
        locManager = (LocationManager) context2
                .getSystemService(Context.LOCATION_SERVICE);
        try {
            mode = Secure.getInt(context2.getContentResolver(),
                    Secure.LOCATION_MODE);
        } catch (SettingNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (!locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
                && !locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setIcon(R.drawable.ic_alert)

                    .setTitle(context.getResources().getString(R.string.gps_is_not_enable))
                    .setMessage(context.getResources().getString(R.string.gps_is_not_enable))
                    // Positive button
                    .setPositiveButton(context.getResources().getString(R.string.yes),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    context.startActivity(intent);
                                    dialog.dismiss();
                                }
                            })

                    // Negative Button
                    .setNegativeButton(context.getResources().getString(R.string.no),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();

                                    Toast.makeText(context, context.getResources().getString(R.string.gps_is_compulsary), Toast.LENGTH_LONG).show();
                                    ((Activity) context).onBackPressed();

                                }
                            }).show();
//**************************************************************************************************

        } else {
            if (mode == 0) {
                if (locManager
                        .isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    if (Build.VERSION.SDK_INT >= 23) {
                        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission
                                    .ACCESS_COARSE_LOCATION}, MY_PERMISSION_ACCESS_COURSE_LOCATION);
                            ActivityCompat.requestPermissions((Activity)context, new String[]{Manifest.permission
                                    .ACCESS_FINE_LOCATION}, MY_PERMISSION_ACCESS_FINE_LOCATION);
                            try {
                                locManager.requestLocationUpdates(
                                        LocationManager.NETWORK_PROVIDER, minTime,
                                        minDistance, this);
                            } catch (SecurityException se) {

                            }
                        }

                    } else {
                        try {
                            locManager.requestLocationUpdates(
                                    LocationManager.NETWORK_PROVIDER, minTime,
                                    minDistance, this);
                        } catch (SecurityException se) {

                        }
                    }
                }
                if (locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    if (Build.VERSION.SDK_INT >= 23) {
                        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions((Activity)context, new String[]{Manifest.permission
                                    .ACCESS_COARSE_LOCATION}, MY_PERMISSION_ACCESS_COURSE_LOCATION);
                            ActivityCompat.requestPermissions((Activity)context, new String[]{Manifest.permission
                                    .ACCESS_FINE_LOCATION}, MY_PERMISSION_ACCESS_FINE_LOCATION);
                            try {
                                locManager.requestLocationUpdates(
                                        LocationManager.NETWORK_PROVIDER, minTime,
                                        minDistance, this);
                            } catch (SecurityException se) {

                            }
                        }

                    } else {
                        try {
                            locManager.requestLocationUpdates(
                                    LocationManager.GPS_PROVIDER, minTime, minDistance,
                                    this);
                        } catch (SecurityException se) {

                        }
                    }
                } else {
                    // no provider Available
                }

            } else if (mode == 1)// Device Only
            {
                if (locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    if (Build.VERSION.SDK_INT >= 23) {
                        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions((Activity)context, new String[]{Manifest.permission
                                    .ACCESS_COARSE_LOCATION}, MY_PERMISSION_ACCESS_COURSE_LOCATION);
                            ActivityCompat.requestPermissions((Activity)context, new String[]{Manifest.permission
                                    .ACCESS_FINE_LOCATION}, MY_PERMISSION_ACCESS_FINE_LOCATION);
                            try {
                                locManager.requestLocationUpdates(
                                        LocationManager.GPS_PROVIDER, minTime,
                                        minDistance, this);
                            } catch (SecurityException se) {

                            }
                        } else {
                            try {
                                locManager.requestLocationUpdates(
                                        LocationManager.GPS_PROVIDER, minTime,
                                        minDistance, this);
                            } catch (SecurityException se) {

                            }
                        }

                    } else {
                        try {
                            locManager.requestLocationUpdates(
                                    LocationManager.GPS_PROVIDER, minTime, minDistance,
                                    this);
                        } catch (SecurityException se) {

                        }
                    }
                } else {
                    // /no provider is available
                }
            } else if (mode == 2)// Battery Saving Mode
            {
                if (locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    if (Build.VERSION.SDK_INT >= 23) {
                        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                            ActivityCompat.requestPermissions((Activity)context, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSION_ACCESS_COURSE_LOCATION);
                            ActivityCompat.requestPermissions((Activity)context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_ACCESS_FINE_LOCATION);

                            try {
                                locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, minTime, minDistance, this);
                            } catch (SecurityException se) {

                            }
                        } else {
                            try {
                                locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, minTime, minDistance, this);
                            } catch (SecurityException se) {

                            }
                        }

                    } else {
                        try {
                            locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, minTime, minDistance, this);
                        } catch (SecurityException se) {

                        }
                    }
                } else {
                    // /no provider is available
                }
            } else if (mode == 3)// HighAccurecy
            {
                if (locManager
                        .isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    if (Build.VERSION.SDK_INT >= 23) {
                        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions((Activity)context, new String[]{Manifest.permission
                                    .ACCESS_COARSE_LOCATION}, MY_PERMISSION_ACCESS_COURSE_LOCATION);
                            ActivityCompat.requestPermissions((Activity)context, new String[]{Manifest.permission
                                    .ACCESS_FINE_LOCATION}, MY_PERMISSION_ACCESS_FINE_LOCATION);
                            try {
                                locManager.requestLocationUpdates(
                                        LocationManager.NETWORK_PROVIDER, minTime,
                                        minDistance, this);
                            } catch (SecurityException se) {
                                //Toast.makeText(context, se.toString(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            try {
                                locManager.requestLocationUpdates(
                                        LocationManager.NETWORK_PROVIDER, minTime,
                                        minDistance, this);
                            } catch (SecurityException se) {
                                //Toast.makeText(context, se.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }

                    } else {
                        try {
                            locManager.requestLocationUpdates(
                                    LocationManager.NETWORK_PROVIDER, minTime,
                                    minDistance, this);
                        } catch (SecurityException se) {
                            // Toast.makeText(context, se.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    if (locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        if (Build.VERSION.SDK_INT >= 23) {
                            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                                ActivityCompat.requestPermissions((Activity)context, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSION_ACCESS_COURSE_LOCATION);
                                ActivityCompat.requestPermissions((Activity)context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_ACCESS_FINE_LOCATION);

                                try {
                                    locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, this);

                                } catch (SecurityException se) {
                                    // Toast.makeText(context, se.toString(), Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                try {
                                    locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, this);

                                } catch (SecurityException se) {
                                    //Toast.makeText(context, se.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }

                        } else {
                            try {
                                locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, this);

                            } catch (SecurityException se) {
                                //Toast.makeText(context, se.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        // /no provider is available
                    }
                }
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        // TODO Auto-generated method stub
        if (location != null) {
            String latitude = String.valueOf(location.getLatitude());
            String longitude = String.valueOf(location.getLongitude());
            long date = location.getTime();
            String provider = location.getProvider();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss", new Locale("en"));
            String dateTime = sdf.format(date);

            SharedPreferences sharedLocation = context.getSharedPreferences("AppLocation", 0);
            Editor editor = sharedLocation.edit();
            editor.putString("latitude", latitude);
            editor.putString("longitude", longitude);
            editor.putString("dateTime", dateTime);
            editor.putString("provider", provider);
            editor.commit();


        }

    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

}
