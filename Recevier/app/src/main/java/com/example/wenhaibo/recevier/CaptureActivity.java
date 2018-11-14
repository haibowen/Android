package com.example.wenhaibo.recevier;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class CaptureActivity extends AppCompatActivity {

    private Button btnCapture;
    private ImageView ivCaptureImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);

        btnCapture = (Button) findViewById(R.id.btnCapture);
        ivCaptureImg = (ImageView) findViewById(R.id.ivCaptureImg);

        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent capIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(capIntent,100);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,requestCode,data);
        if(requestCode == 100) {
            if(requestCode == RESULT_OK) {
                //显示图片到ImageView上
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                ivCaptureImg.setImageBitmap(bitmap);
            }else if(requestCode == RESULT_CANCELED){
                //提示用户未拍照成功
                Toast.makeText(CaptureActivity.this,"拍照未成功",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
