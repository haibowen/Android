package com.example.administrator.mypopmenu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Button button;
    private TextView textView,textView1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button=findViewById(R.id.bt_show);
        textView=findViewById(R.id.group_name);
        textView1=findViewById(R.id.phone_num);
        textView.setVisibility(View.GONE);
        textView1.setVisibility(View.GONE);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popupMenu=new PopupMenu(MainActivity.this,v);
                getMenuInflater().inflate(R.menu.poup,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        switch (menuItem.getItemId()){

                            case R.id.popup_add:

                                break;

                            case R.id.popup_delete:

                                break;
                            case R.id.popup_more:

                                break;

                        }
                        return false;
                    }
                });

                popupMenu.show();


            }


        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.poup,menu);

        return true;
    }
}
