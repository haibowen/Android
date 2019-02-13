package com.example.administrator.mylmada;

        import android.content.ContentProvider;
        import android.content.ContentValues;
        import android.content.UriMatcher;
        import android.database.Cursor;
        import android.net.Uri;

public class MyProvider extends ContentProvider {
    public  static  final  int TABLE1_DIR=0;
    public  static  final  int TABLE1_ITEM=1;
    public  static  final  int TABLE2_DIR=2;
    public  static  final  int TABLE2_ITEM=3;

    static  {

        UriMatcher uriMatcher=new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI("","",TABLE1_DIR);
        uriMatcher.addURI("","",TABLE1_ITEM);
        uriMatcher.addURI("","",TABLE2_DIR);
        uriMatcher.addURI("","",TABLE2_ITEM);



    }


    @Override
    public boolean onCreate() {
        return false;
    }

    @androidx.annotation.Nullable
    @Override
    public Cursor query(@androidx.annotation.NonNull Uri uri, @androidx.annotation.Nullable String[] projection, @androidx.annotation.Nullable String selection, @androidx.annotation.Nullable String[] selectionArgs, @androidx.annotation.Nullable String sortOrder) {
        return null;
    }

    @androidx.annotation.Nullable
    @Override
    public String getType(@androidx.annotation.NonNull Uri uri) {
        return null;
    }

    @androidx.annotation.Nullable
    @Override
    public Uri insert(@androidx.annotation.NonNull Uri uri, @androidx.annotation.Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@androidx.annotation.NonNull Uri uri, @androidx.annotation.Nullable String selection, @androidx.annotation.Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@androidx.annotation.NonNull Uri uri, @androidx.annotation.Nullable ContentValues values, @androidx.annotation.Nullable String selection, @androidx.annotation.Nullable String[] selectionArgs) {
        return 0;
    }
}
