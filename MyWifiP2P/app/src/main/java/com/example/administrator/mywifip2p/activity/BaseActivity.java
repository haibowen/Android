package com.example.administrator.mywifip2p.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
import com.example.administrator.mywifip2p.R;
import com.example.administrator.mywifip2p.common.ShowLoadingDialog;

public class BaseActivity extends AppCompatActivity {
    private ShowLoadingDialog showLoadingDialog;



    protected void  showLoadingDialog(String message){

        if (showLoadingDialog==null){

            showLoadingDialog=new ShowLoadingDialog(this);

        }

       showLoadingDialog.show(message,true,false);



    }

    protected void dismissLoadingDialog(){

        if (showLoadingDialog!=null){

            dismissLoadingDialog();
        }
    }

    protected void showToast(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();



    }
}
