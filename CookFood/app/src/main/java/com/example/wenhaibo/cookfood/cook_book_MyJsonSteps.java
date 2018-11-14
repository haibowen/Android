package com.example.wenhaibo.cookfood;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class cook_book_MyJsonSteps {
    //获取 步骤数组
    public ArrayList<String> JsonJXSteps(JSONObject object) {
        try {
            JSONArray jsonArray = object.getJSONArray("steps");
            ArrayList<String> aa = new ArrayList<>();

            for (int i =0;i<jsonArray.length();i++){
               object = (JSONObject) jsonArray.opt(i);
                String s =object.getString("step");
                aa.add(s);
            }
            return aa;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<String> JsonJXImgs(JSONObject object) {
        try {
            JSONArray jsonArray = object.getJSONArray("steps");
            ArrayList<String> imgs = new ArrayList<>();

            for (int i =0;i<jsonArray.length();i++){
                object = (JSONObject) jsonArray.opt(i);
                String im = object.getString("img");
                imgs.add(im);
            }
            return imgs;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
