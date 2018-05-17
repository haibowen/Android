package com.example.wenhaibo.androidstudy_notice;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        button=findViewById( R.id.bt_notice );

        button.setOnClickListener( new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                Intent intent =new Intent( MainActivity.this,Main2Activity.class );
                PendingIntent pendingIntent=PendingIntent.getActivity( MainActivity.this,0,intent,0 );

                NotificationManager manager= (NotificationManager) getSystemService( NOTIFICATION_SERVICE );
                Notification notification=new NotificationCompat.Builder( MainActivity.this )

                        .setWhen(  System.currentTimeMillis())
                        .setContentText( "hai66666666666666,how to use the notce how to study android studio of learing to be or not to be this is a question" )
                        .setSmallIcon( R.mipmap.timg )

                        .setLargeIcon( BitmapFactory.decodeResource( getResources(),R.mipmap.timg ) )


                        .setContentIntent( pendingIntent )
                        .setDefaults( NotificationCompat.DEFAULT_ALL )
                        .setLights( Color.GREEN,1000,1000 )
                        .setAutoCancel( true )
                        .setStyle( new NotificationCompat.BigTextStyle(  ).bigText( "hai66666666666666,how to use the notce how to study android studio of learing to be or not to be this is a question" ) )
                        .setStyle( new NotificationCompat.BigPictureStyle(  ).bigPicture( BitmapFactory.decodeResource( getResources(),R.drawable.timg) ) )
                        .setPriority(NotificationCompat.PRIORITY_MAX )
                        .build();
                manager.notify( 1,notification );

            }
        } );
    }
}
