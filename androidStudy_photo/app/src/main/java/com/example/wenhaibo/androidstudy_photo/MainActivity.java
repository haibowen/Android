package com.example.wenhaibo.androidstudy_photo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private Button button;
    private ImageView imageView;

    private Uri imaguri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        button=findViewById( R.id.bt_photo );
        imageView=findViewById( R.id.im_photo );

        button.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File outputImage=new File (getExternalCacheDir(),"output_image.jpg");
                try{
                    if(outputImage.exists()){
                        outputImage.delete();
                    }
                    outputImage.createNewFile();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(Build.VERSION.SDK_INT>=24){
                    imaguri= FileProvider.getUriForFile( MainActivity.this,"com.example.wenhaibo.androidstudy_photo.fileprovider",outputImage );

                   // Uri imageUri=FileProvider.getUriForFile(MainActivity.this,"me.xifengwanzhao.fileprovider", outputImage);//这里进行替换uri的获得方式




                }else {
                    imaguri= Uri.fromFile( outputImage );

                }

                Intent intent =new Intent( "android.media.action.IMAGE_CAPTURE" );
                intent.putExtra( MediaStore.EXTRA_OUTPUT,imaguri );
               //intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//这里加入flag
                startActivityForResult( intent,1);

            }
        } );

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode==1){
            if(resultCode==RESULT_OK){
                try{
                    Bitmap bitmap= BitmapFactory.decodeStream( getContentResolver().openInputStream( imaguri ) );
                    imageView.setImageBitmap( bitmap);

                }catch (FileNotFoundException e) {
                    e.printStackTrace();

                }
            }


        }
    }
}
