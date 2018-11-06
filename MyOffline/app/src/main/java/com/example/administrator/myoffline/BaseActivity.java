package com.example.administrator.myoffline;

import android.content.*;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class BaseActivity extends AppCompatActivity {
    private ForceoffLineReceiver forceoffLineReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        ActivityControler.addActivity(this);

    }

    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("com.example.broadcastbestpractice.FORCE_OFFLINE");
        forceoffLineReceiver=new ForceoffLineReceiver();
        registerReceiver(forceoffLineReceiver,intentFilter);



    }

    @Override
    protected void onPause() {
        super.onPause();
        if (forceoffLineReceiver!=null){
            unregisterReceiver(forceoffLineReceiver);
            forceoffLineReceiver=null;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityControler.removeActivity(this);
    }

    class  ForceoffLineReceiver extends BroadcastReceiver{


        @Override
        public void onReceive(final Context context, final Intent intent) {
            AlertDialog.Builder builder=new AlertDialog.Builder(context);
            builder.setTitle("Waring");
            builder.setMessage("you are forced to be offline.Please try to login again");
            builder.setCancelable(false);
            builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ActivityControler.finshActivity();;
                    Intent intent1=new Intent(context,LoginActivity.class);
                    context.startActivity(intent);

                }
            });
            builder.show();
        }
    }
}
