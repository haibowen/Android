package com.example.wenhaibo.androidstudy01;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.testlayout );
        button = findViewById( R.id.btshow );
        button.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText( MainActivity.this, "sucess", Toast.LENGTH_SHORT ).show();
                finish();
            }
        } );


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate( R.menu.main, menu );


        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.bt1:
                Toast.makeText( MainActivity.this, "add onclick", Toast.LENGTH_SHORT ).show();
                //break;
            case R.id.bt2:
                Toast.makeText( MainActivity.this, "delte onclick", Toast.LENGTH_SHORT ).show();

        }

        return true;

    }
}