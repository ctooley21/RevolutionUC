package com.google.sample.snapsey;

import android.preference.PreferenceActivity;
import android.util.Log;

import com.google.api.client.http.HttpResponse;

import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class NutritionUtil
{

    private static String appId;
    private static String appKey;

    public static void init()
    {
        appId = "1178665d";
        appKey = "0f21996549fcf98881c28c9aaa08fc2f";
    }

    public static void postData(String item) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("https://api.nutritionix.com/v1_1/search");

        try {
            List<NameValuePair> nameValuePairs = new ArrayList<>(2);
            nameValuePairs.add(new BasicNameValuePair("appId", appId));
            nameValuePairs.add(new BasicNameValuePair("appKey", appKey));
            nameValuePairs.add(new BasicNameValuePair("query", item));
            nameValuePairs.add(new BasicNameValuePair("limit", Integer.toString(1)));
            nameValuePairs.add(new BasicNameValuePair("fields[]", "nf_calories"));
            nameValuePairs.add(new BasicNameValuePair("fields[]", "nf_dietary_fiber"));
            nameValuePairs.add(new BasicNameValuePair("fields[]", "nf_total_carbohydrate"));
            nameValuePairs.add(new BasicNameValuePair("fields[]", "nf_ingredient_statement"));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            org.apache.http.HttpResponse response = httpclient.execute(httppost);

            String responseBody = EntityUtils.toString(response.getEntity());

            Log.d("test", responseBody);

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
