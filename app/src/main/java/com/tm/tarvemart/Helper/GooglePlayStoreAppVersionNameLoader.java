package com.tm.tarvemart.Helper;

import android.os.AsyncTask;
import android.util.Log;

import com.tm.tarvemart.BuildConfig;

import org.jsoup.Jsoup;

public class GooglePlayStoreAppVersionNameLoader extends AsyncTask<String, String, String> {

    private String newVersion;
    private String url;
    protected String doInBackground(String... urls) {

        try {

                    url = Jsoup.connect("https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "&hl=en")
                            .timeout(30000)
                            .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                            .referrer("http://www.google.com")
                            .get()
                            //.select("div[itemprop=softwareVersion]")
                            .select("div.hAyfc:nth-child(4) > span:nth-child(2) > div:nth-child(1) > span:nth-child(1)")
                            //.first()
                            //.select(".hAyfc .htlgb")
                            //.get(3)
                           // .select(".xyOfqd .hAyfc:nth-child(4) .htlgb span")
                            .get(0)
                            .ownText();

Log.e("versioncode",url);
        } catch (Exception e) {
            Log.e("Exception21","er"+e);
            return "";
        }
        return url;
    }

    protected void onPostExecute(String string) {
       if(string!=null) {
           newVersion = string;
           Log.e("new Version", "new" + newVersion);
       }
       else
       {
           Log.e("new Version", "null");
       }
    }
}
