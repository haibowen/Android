package com.example.administrator.runtimepermision;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class MyProvider extends ContentProvider {


    public static final  int TABLE1_DIR=0;
    public static  final  int TABLE1_ITEM=1;
    public static  final  int TABLE2_DIR=2;
    public static final  int TABLE2_ITEM=3;
    private static UriMatcher uriMatcher;


    static {
        uriMatcher=new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI("com.example.administrator.runtimepermision","table1",TABLE1_DIR);
        uriMatcher.addURI("com.example.administrator.runtimepermision","table1/#",TABLE1_ITEM);
        uriMatcher.addURI("com.example.administrator.runtimepermision","table2",TABLE2_DIR);
        uriMatcher.addURI("com.example.administrator.runtimepermision","table2/#",TABLE2_ITEM);

    }



    @Override
    public boolean onCreate() {

        return false;
    }
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        switch (uriMatcher.match(uri)){
            case TABLE1_DIR:

                //
                break;
            case  TABLE1_ITEM:

                //

                /**
                 *
                 *
                 * ////当前操作元素父元素的第一个同胞元素的内容赋给thisid
                 * var thisid = $(this).parent().siblings("th:eq(0)").text();
                 * //当前操作元素val()赋给thisvalue
                 var thisvalue=$(this).val();
                 //当前操作元素父元素的类赋给thisclass
                 var thisclass = $(this).parent().attr("class");

                 */
                break;

            case TABLE2_DIR:

                //
                break;

            case TABLE2_ITEM:

                //
                break;


        }

        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        return null;

    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }


}
