package com.google.sample.snapsey;

import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NutritionUtil
{

    private static String appId;
    private static String appKey;
    private static List<String> glutenItems;

    public static void init()
    {
        appId = "1178665d";
        appKey = "0f21996549fcf98881c28c9aaa08fc2f";
        glutenItems = Arrays.asList("wheat", "barley", "rye", "oat");
    }

    public static void postData(String item) {
        Log.d("GI", calculateGILevels(item)+"");
        Log.d("GI", calculateGlucose(item)+"");
    }

    public static int calculateGILevels(String name)
    {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("https://api.nutritionix.com/v1_1/search");

        try {
            List<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("appId", appId));
            nameValuePairs.add(new BasicNameValuePair("appKey", appKey));
            nameValuePairs.add(new BasicNameValuePair("query", name));
            nameValuePairs.add(new BasicNameValuePair("limit", Integer.toString(1)));
            nameValuePairs.add(new BasicNameValuePair("fields[]", "nf_dietary_fiber"));
            nameValuePairs.add(new BasicNameValuePair("fields[]", "nf_serving_weight_grams"));
            nameValuePairs.add(new BasicNameValuePair("fields[]", "nf_total_carbohydrate"));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            org.apache.http.HttpResponse response = httpclient.execute(httppost);

            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            StringBuilder builder = new StringBuilder();
            for (String line; (line = reader.readLine()) != null;) {
                builder.append(line).append("\n");
            }
            JSONTokener tokener = new JSONTokener(builder.toString());
            JSONObject finalResult = new JSONObject(tokener);

            double fiber = finalResult.getJSONArray("hits").getJSONObject(0).getJSONObject("fields").getInt("nf_dietary_fiber");
            double grams = finalResult.getJSONArray("hits").getJSONObject(0).getJSONObject("fields").getInt("nf_serving_weight_grams");
            double carbs = finalResult.getJSONArray("hits").getJSONObject(0).getJSONObject("fields").getInt("nf_total_carbohydrate");
            double level = ((carbs - fiber) / grams) * 100.0;

            if(level > 100) level = 100;
            if(level < 0) level = 0;

            return (int) level;

        } catch (Exception e)
        {
            return 0;
        }
    }

    public static int calculateGlucose(String name)
    {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("https://api.nutritionix.com/v1_1/search");

        try {
            List<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("appId", appId));
            nameValuePairs.add(new BasicNameValuePair("appKey", appKey));
            nameValuePairs.add(new BasicNameValuePair("query", name));
            nameValuePairs.add(new BasicNameValuePair("limit", Integer.toString(1)));
            nameValuePairs.add(new BasicNameValuePair("fields[]", "nf_dietary_fiber"));
            nameValuePairs.add(new BasicNameValuePair("fields[]", "nf_total_carbohydrate"));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            org.apache.http.HttpResponse response = httpclient.execute(httppost);

            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            StringBuilder builder = new StringBuilder();
            for (String line; (line = reader.readLine()) != null;)
            {
                builder.append(line).append("\n");
            }
            JSONTokener tokener = new JSONTokener(builder.toString());
            JSONObject finalResult = new JSONObject(tokener);

            String nutt = finalResult.getJSONArray("hits").getJSONObject(0).getJSONObject("fields").getString("nf_ingredient_statement");

            int level = 0;

            for(String s : glutenItems)
            {
                if(nutt.toLowerCase().contains(s))
                {
                    level += 25;
                }
            }

            return level;

        } catch (Exception e)
        {
            return 0;
        }
    }

}
