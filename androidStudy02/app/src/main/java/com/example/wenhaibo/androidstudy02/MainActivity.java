package com.example.wenhaibo.androidstudy02;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    private EditText editText1, editText2, editText3;
    private Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        editText1 = findViewById( R.id.tv1 );
        editText2 = findViewById( R.id.tv2 );
        editText3 = findViewById( R.id.tv3 );
        button = findViewById( R.id.bt1 );
        button.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String a = editText1.getText().toString();
                String b = editText2.getText().toString();
                String c = editText3.getText().toString();
                Intent intent = new Intent( MainActivity.this, Main2Activity.class );
                intent.putExtra( "a", a );
                intent.putExtra( "b", b );
                intent.putExtra( "c", c );


                startActivityForResult(intent,1  );

            }
        } );


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode==1){
            if(resultCode==RESULT_OK){
                String e=data.getStringExtra( "result" );
                editText3.setText( e );
            }
        }
    }
}
