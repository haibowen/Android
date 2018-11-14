package com.example.wenhaibo.vister;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wenhaibo.olympic.R;

public class ScanActivity extends AppCompatActivity {
	private final static int SCANNIN_GREQUEST_CODE = 1;
	private TextView mTextView ;
	private ImageView mImageView;
	private Button button1;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scan);
		
		mTextView = (TextView) findViewById(R.id.result); 
		mImageView = (ImageView) findViewById(R.id.qrcode_bitmap);

		Button mButton = (Button) findViewById(R.id.button1);
		mButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(ScanActivity.this, MipcaActivityCapture.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
			}
		});
	}
	
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
		case SCANNIN_GREQUEST_CODE:
			if(resultCode == RESULT_OK){
				Bundle bundle = data.getExtras();

				mTextView.setText(bundle.getString("result"));

				mImageView.setImageBitmap((Bitmap) data.getParcelableExtra("bitmap"));

				String url=bundle.getString("result");
				Log.e("wen",url);
				Intent i1=new Intent();
				i1.setAction("android.intent.action.VIEW");
				Uri content_url=Uri.parse(url);
				i1.setData(content_url);
				startActivity(i1);
			}
			break;
		}
    }	

}
