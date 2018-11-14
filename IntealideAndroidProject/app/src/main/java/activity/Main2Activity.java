package activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import com.example.administrator.myapplication.R;
import org.greenrobot.eventbus.EventBus;
import util.BaseActivity;
import util.MessageEvent;

public class Main2Activity extends BaseActivity {
    private Button button,button1;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        toolbar=findViewById(R.id.toobal);
        setSupportActionBar(toolbar);

        button = findViewById(R.id.bt_send);
        button1=findViewById(R.id.bt_next);

       // JumpActivity();
        //SkipActivity();
    }

    public void JumpActivity() {

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new MessageEvent("welcome to haibowen.top"));
                finish();
                //ActivityControler.finishAll();
               // android.os.Process.killProcess(android.os.Process.myPid());


            }
        });


    }
   public void  SkipActivity(){
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Main2Activity.this,Main3Activity.class);
                startActivity(intent);

            }
        });


   }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toobal2,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.backup:

                break;
            case  R.id.delete:

                break;
            case R.id.Settings:

                break;
                default:
                    break;

        }
        return super.onOptionsItemSelected(item);
    }
}
