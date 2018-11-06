package com.example.administrator.filesave;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;

import java.io.*;

public class MainActivity extends AppCompatActivity {
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText=findViewById(R.id.et_save);
        try {
            String input=load();
            if (!TextUtils.isEmpty(input)){
                editText.setText(input);
                editText.setSelection(input.length());

            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private String load() throws IOException {

        FileInputStream in=null;
        BufferedReader reader=null;
        StringBuilder content=new StringBuilder();
        try{

            in=openFileInput("data");
            reader=new BufferedReader(new InputStreamReader(in));
            String line="";
            while ((line=reader.readLine())!=null){
                content.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (reader!=null){
                try{
                    reader.close();
                }catch (IOException e){
                    e.printStackTrace();
                }

            }
        }
        return content.toString();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        String input=editText.getText().toString();
        Log.e("wenhaibo", input);
        save(editText.getText().toString());
    }

    private void save(String input) {


        FileOutputStream outputStream=null;
        BufferedWriter writer=null;
        try{
            outputStream=openFileOutput("data", Context.MODE_PRIVATE);
            writer =new BufferedWriter(new OutputStreamWriter(outputStream));
            writer.write(input);

        }catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (writer!=null){
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
