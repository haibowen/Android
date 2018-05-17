package com.example.wenhaibo.android_liuming;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class Main3Activity extends AppCompatActivity implements View.OnClickListener {
    private EditText editText;
    private Button  button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main3 );
        Intent intent=getIntent();
        String a="hello every body";
        intent.putExtra( "a",a );
        setResult( 1,intent );

        editText=findViewById( R.id.et_save );
        button=findViewById( R.id.bt_save );

        button.setOnClickListener( this );
        String inputText=load();
        if(!TextUtils.isEmpty( inputText )){
            editText.setText( inputText );
            editText.setSelection( inputText.length() );

        }
    }

    private String load() {
        FileInputStream fileInputStream=null;
        BufferedReader reader=null;
        StringBuilder content=new StringBuilder(  );
        try{

            fileInputStream=openFileInput( "data" );
            reader=new BufferedReader( new InputStreamReader( fileInputStream ) );
            String line="";
            while ((line =reader.readLine())!=null){
                content.append( line );
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(reader!=null){
                try {
                    reader.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return content.toString();
    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        String inputtext=editText.getText().toString();
//        save( inputtext );
//    }

    @Override
    public void onClick(View v) {
        String inputtext=editText.getText().toString();
        save(inputtext);

    }

    private void save(String inputText) {
        FileOutputStream out=null;
        BufferedWriter writer=null;
        try {
            out =openFileOutput( "data", Context.MODE_PRIVATE );
            writer =new BufferedWriter(  new OutputStreamWriter( out ) );
            writer.write( inputText );
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(writer!=null){
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        }


    }


