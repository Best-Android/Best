package com.best.limingxing.best.bean;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2017/3/16.
 */

public class JSONArray {
    private static final String TAG = JSONArray.class.getSimpleName();

    public static GankPerson parseJson(final String str){
            GankPerson gankPerson = null;
            try {
                JSONObject jsonObject = new JSONObject(str);
                Log.d(TAG,str);
                boolean error = jsonObject.getBoolean("error");
                org.json.JSONArray jsonArray = jsonObject.getJSONArray("results");
                List<Person> list = new ArrayList<Person>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    List<String> imagesList = new ArrayList<String>();
                    JSONObject jsonResults = (JSONObject) jsonArray.get(i);
                    String _id = jsonResults.getString("_id");
                    String createdAt = jsonResults.getString("createdAt");
                    String desc = jsonResults.getString("desc");

                    if (jsonResults.has("images")) {
                        org.json.JSONArray imagesArray = jsonResults.getJSONArray("images");
                        for (int j = 0; j < imagesArray.length(); j++) {
                            String images = (String) imagesArray.get(j);
                            Log.d(TAG, "onResponse: " + images);
                            imagesList.add(images);
                        }
                    }

                    String publishedAt = jsonResults.getString("publishedAt");
                    String source = jsonResults.getString("source");
                    String type = jsonResults.getString("type");
                    String url = jsonResults.getString("url");
                    Boolean used = jsonResults.getBoolean("used");
                    String who = jsonResults.getString("who");
                    Person persons = new Person(_id, createdAt, desc, imagesList, publishedAt, source, type, url, used, who);
                    list.add(persons);
                }
                Log.d(TAG, list.toString());
                gankPerson = new GankPerson();
                gankPerson.setError(error);
                gankPerson.setResults(list);
//                Log.d(TAG, "onResponse: " + gankPerson.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        return gankPerson;
    }
}
