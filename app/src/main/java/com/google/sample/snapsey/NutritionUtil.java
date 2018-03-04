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

    public static List<Integer> postData(String item) {
        List<Integer> numbers = new ArrayList<>();

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("https://api.nutritionix.com/v1_1/search");

        double fiber;
        double grams;
        double carbs;
        String nutt;
        double glucoseIndex;
        int gluten;
        int calories;

        try {
            List<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("appId", appId));
            nameValuePairs.add(new BasicNameValuePair("appKey", appKey));
            nameValuePairs.add(new BasicNameValuePair("query", item));
            nameValuePairs.add(new BasicNameValuePair("limit", Integer.toString(1)));
            nameValuePairs.add(new BasicNameValuePair("fields[]", "nf_dietary_fiber"));
            nameValuePairs.add(new BasicNameValuePair("fields[]", "nf_serving_weight_grams"));
            nameValuePairs.add(new BasicNameValuePair("fields[]", "nf_ingredient_statement"));
            nameValuePairs.add(new BasicNameValuePair("fields[]", "nf_total_carbohydrate"));
            nameValuePairs.add(new BasicNameValuePair("fields[]", "nf_calories"));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            org.apache.http.HttpResponse response = httpclient.execute(httppost);

            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            StringBuilder builder = new StringBuilder();
            for (String line; (line = reader.readLine()) != null;) {
                builder.append(line).append("\n");
            }
            JSONTokener tokener = new JSONTokener(builder.toString());
            JSONObject finalResult = new JSONObject(tokener);

            fiber = finalResult.getJSONArray("hits").getJSONObject(0).getJSONObject("fields").getInt("nf_dietary_fiber");
            grams = finalResult.getJSONArray("hits").getJSONObject(0).getJSONObject("fields").getInt("nf_serving_weight_grams");
            carbs = finalResult.getJSONArray("hits").getJSONObject(0).getJSONObject("fields").getInt("nf_total_carbohydrate");
            nutt = finalResult.getJSONArray("hits").getJSONObject(0).getJSONObject("fields").getString("nf_ingredient_statement");
            calories = finalResult.getJSONArray("hits").getJSONObject(0).getJSONObject("fields").getInt("nf_calories");
            glucoseIndex = ((carbs - fiber) / grams) * 100.0;
            gluten = 0;

            if(glucoseIndex > 100) glucoseIndex = 100;
            if(glucoseIndex < 0) glucoseIndex = 0;

            for(String s : glutenItems)
            {
                if(nutt.toLowerCase().contains(s))
                {
                    gluten += 25;
                }
            }

        } catch (Exception e)
        {
            e.printStackTrace();
            calories = 0;
            glucoseIndex = 0;
            gluten = 0;
        }

        numbers.add(calories);
        numbers.add(gluten);
        numbers.add((int)glucoseIndex);

        return numbers;
    }
}
