package com.example.administrator.mycompanytest;

import android.content.Context;
import android.util.DisplayMetrics;

public class LcdInfo {
    public static  String getDisplayAbsolutewidth(Context cx){
        String str = "";
        DisplayMetrics dm = new DisplayMetrics();
        dm = cx.getApplicationContext().getResources().getDisplayMetrics();
        int screenWidth = dm.widthPixels;
        str += "绝对宽度:" + String.valueOf(screenWidth) + "pixels";
        return str;

    }
    public static String getDisplayAbsoluteheight(Context cx){
        String str = "";
        DisplayMetrics dm = new DisplayMetrics();
        dm = cx.getApplicationContext().getResources().getDisplayMetrics();
        int screenHeight = dm.heightPixels;
        str += "绝对高度:" + String.valueOf(screenHeight)
                + "pixels";
        return str;


    }
    public static  String getDisPlaydensity(Context cx){
        String str = "";
        DisplayMetrics dm = new DisplayMetrics();
        dm = cx.getApplicationContext().getResources().getDisplayMetrics();

        float density = dm.density;
        str += "显示器的物理密度:" + String.valueOf(density)
                ;
        return str;


    }
    public static String getDisplayXdpi(Context cx){

        String str = "";
        DisplayMetrics dm = new DisplayMetrics();
        dm = cx.getApplicationContext().getResources().getDisplayMetrics();
        float xdpi = dm.xdpi;
        str += "X维度:" + String.valueOf(xdpi) + "pixels per inch";
        return str;


    }
    public static String getDisplayYdpi(Context cx){

        String str = "";
        DisplayMetrics dm = new DisplayMetrics();
        dm = cx.getApplicationContext().getResources().getDisplayMetrics();
        float ydpi = dm.ydpi;
        str += "Y维度:" + String.valueOf(ydpi) + "pixels per inch";
        return str;


    }
    public static String getDisplayDpi(Context cx){

        String str = "";
        DisplayMetrics dm = new DisplayMetrics();
        dm = cx.getApplicationContext().getResources().getDisplayMetrics();
        int dpi = dm.densityDpi;
        str += "显示器的显示密度:" + String.valueOf(dpi)
                ;
        return str;

    }

    public static String getDisplayWidth(Context cx){
        String str = "";
        DisplayMetrics dm = new DisplayMetrics();
        dm = cx.getApplicationContext().getResources().getDisplayMetrics();
        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;
        float density = dm.density;
        int width = (int) (screenWidth/density);
        str += "宽度:" + String.valueOf(width) + "dp";
        return str;
    }
    public static String getDisplayHeight(Context cx){
        String str = "";
        DisplayMetrics dm = new DisplayMetrics();
        dm = cx.getApplicationContext().getResources().getDisplayMetrics();
        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;
        float density = dm.density;
        int height = (int) (screenHeight/density);
        str += "高度:" + String.valueOf(height) + "dp";
        return str;

    }



    public static String getDisplayMetrics(Context cx) {
        String str = "";
        DisplayMetrics dm = new DisplayMetrics();
        dm = cx.getApplicationContext().getResources().getDisplayMetrics();
        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;
        float density = dm.density;
        float xdpi = dm.xdpi;
        float ydpi = dm.ydpi;
        int dpi = dm.densityDpi;
        int width = (int) (screenWidth/density);
        int height = (int) (screenHeight/density);

        str += "The absolute width:" + String.valueOf(screenWidth) + "pixels\n";
        str += "The absolute heightin:" + String.valueOf(screenHeight)
                + "pixels\n";
        str += "The logical density of the display.:" + String.valueOf(density)
                + "\n";
        str += "X dimension :" + String.valueOf(xdpi) + "pixels per inch\n";
        str += "Y dimension :" + String.valueOf(ydpi) + "pixels per inch\n";
        str += "The densityDpi of the display.:" + String.valueOf(dpi)
                + "\n";
        str += "The width:" + String.valueOf(width) + "dp\n";
        str += "The height:" + String.valueOf(height) + "dp\n";
        return str;
    }


}
