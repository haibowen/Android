package CookFood;


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class cook_book_MyJson {
    public ArrayList<String> JsonJX(JSONObject object) {
        try {
            JSONArray jsonArray = object.getJSONArray("data");
            ArrayList<String> aa = new ArrayList<>();
            String de, food, key, pic;

            object = (JSONObject) jsonArray.opt(0);
            de = object.getString("title");
            aa.add(de);
            food = object.getString("ingredients");
            aa.add(food);
            key = object.getString("burden");
            aa.add(key);
            pic = object.getString("albums");
            String f = pic.replace("\\","/").replace("//", "/");
            //转型
            Log.e("gaoyupic","pic"+f);
            aa.add(f);
            return aa;


        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;

    }
}
