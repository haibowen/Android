package com.example.administrator.mynotification;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private Button button,button1;
    private ImageView imageView;
    private Uri imguri;
    public static final int TAKE_PHOTO=1;
    public  static  final  int CHOOSE_PHOTO=2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button=findViewById(R.id.bt_photo);
        button1=findViewById(R.id.bt_findphoto);
        imageView=findViewById(R.id.Imag_photo);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file=new File(getExternalCacheDir(),"123.jpg");
                try {
                if (file.exists()){
                    file.delete();
                }

                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (Build.VERSION.SDK_INT>=24){
                    imguri= FileProvider.getUriForFile(MainActivity.this,"com.example.666",file);

                }else {
                    imguri=Uri.fromFile(file);

                }
                Intent intent=new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT,imguri);
                startActivityForResult(intent,TAKE_PHOTO);

            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);

                }else {
                    openAlbum();
                }
            }
        });
    }

    private void openAlbum() {
        Intent intent=new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,CHOOSE_PHOTO);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){

            case 1:
                if (grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){

                    openAlbum();

                }else {
                    Toast.makeText(this,"you denied the permission",Toast.LENGTH_SHORT).show();

                }
                break;
                default:
                    break;
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case TAKE_PHOTO:
                if (resultCode==RESULT_OK){

                    Bitmap bitmap= null;
                    try {
                        bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imguri));


                        imageView.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }


                }
                break;
            case CHOOSE_PHOTO:
                if (resultCode==RESULT_OK){
                    if (Build.VERSION.SDK_INT<=19){
                        handleimageonkitkat(data);
                    }else {
                        handleimagebeforekitkat(data);

                    }
                }
                break;
                default:
                    break;
        }

    }

    @TargetApi(19)
    private void handleimageonkitkat(Intent data) {
        String imagepath=null;
        Uri uri=data.getData();
        if (DocumentsContract.isDocumentUri(this,uri)){
            String docId=DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())){
                String id=docId.split(".")[1];
                String selection=MediaStore.Images.Media._ID+"="+id;
                imagepath=getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            }else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())){

                Uri contentUri= ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(docId));
                imagepath=getImagePath(contentUri,null);
            }
        }else if ("content".equalsIgnoreCase(uri.getScheme())){
            imagepath=getImagePath(uri,null);
        }else if ("file".equalsIgnoreCase(uri.getScheme())){
            imagepath=uri.getPath();

        }
        displayImage(imagepath);




    }



    private void handleimagebeforekitkat(Intent data) {
        Uri uri=data.getData();
        String imagpath=getImagePath(uri,null);
        displayImage(imagpath);

    }

    private String getImagePath(Uri uri, String selection) {

        String path=null;
        Cursor cursor=getContentResolver().query(uri,null,selection,null,null);
        if (cursor!=null){
            if (cursor.moveToFirst()){
                path=cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));

            }
            cursor.close();
        }

        return path;
    }

    private void displayImage(String imagepath) {

        if (imagepath!=null){
            Bitmap bitmap=BitmapFactory.decodeFile(imagepath);
            Log.e("wenhaibo", String.valueOf(bitmap));
            imageView.setImageBitmap(bitmap);
        }else {
            Toast.makeText(this,"Failed to get image",Toast.LENGTH_SHORT).show();

        }
    }
}
