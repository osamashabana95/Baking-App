package com.example.osama.baking.utility;

import android.net.Uri;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by osama on 4/13/2018.
 */

public class Utility {

final static String RECIPE_URL ="https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
    /* to build needed url*/
    public static URL buildUrl() {

        Uri uri = Uri.parse(RECIPE_URL).buildUpon().build();
        URL url =null;

        try {

            url = new URL(uri.toString());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }
    /*to make stable connection and get response*/
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    /* to get array of recipes name*/
    public static String[][] getStingArrayOfRecipesDetails (String json  )throws Exception{


        JSONArray jsonArray = new JSONArray(json);

        String[][] list =new String[jsonArray.length()][2];
        for(int i = 0; i < jsonArray.length(); i++){
            JSONObject object = jsonArray.getJSONObject(i);
            String name=object.getString("name");
            String image=object.getString("image");
            list[i][0] = name;
            list[i][1]=image;
        }
        return list;
    }

    //load step details from json
    public static String[][] getStepsDetails (String json,int position )throws Exception{
        JSONArray jsonArray = new JSONArray(json);
        JSONObject object = jsonArray.getJSONObject(position);
        JSONArray array = object.getJSONArray("steps");
        String[][] list =new String[array.length()][4];
        for (int i = 0; i < array.length(); i++) {
            JSONObject object1=array.getJSONObject(i);
            for (int j = 0; j <4 ; j++) {
                if (j == 0) {
                    if(i==0){list[i][j] ="      "+object1.optString("shortDescription");}
                    else{list[i][j] = i+"     "+object1.optString("shortDescription");}
                }else if (j==1) {
                    list[i][j] = object1.optString("description");
                }else if (j==2){
                    list[i][j] = object1.optString("videoURL");
                }else if(j==3)
                {
                   list[i][j] = object1.optString("thumbnailURL");
                }

            }
        }
    return list;
    }
    //load data of ingredients from json
    public static String[] getIngredientsDetails (String json,int position )throws Exception{
        JSONArray jsonArray = new JSONArray(json);
        JSONObject object = jsonArray.getJSONObject(position);
        JSONArray array = object.getJSONArray("ingredients");
        String[] list =new String[array.length()];
        for (int i = 0; i < array.length(); i++) {
            JSONObject object1=array.getJSONObject(i);
           String s1= object1.optString("ingredient");
            String s2 =object1.optString("quantity");
            String s3= object1.optString("measure");
            String all= s1+"__"+s2+"*"+s3;
            list[i]=all;

        }
        return list;
    }


}
