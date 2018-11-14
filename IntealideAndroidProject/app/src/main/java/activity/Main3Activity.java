package activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.example.administrator.myapplication.R;


public class Main3Activity extends AppCompatActivity {
    private Button button,button1,button2;
    private ProgressBar progressBar,progressBar1;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        toolbar=findViewById(R.id.toobal);
        setSupportActionBar(toolbar);

        button=findViewById(R.id.bt_next_pro);
        button1=findViewById(R.id.bt_next_dialog);
        button2=findViewById(R.id.bt_next_progressdialog);
        progressBar=findViewById(R.id.PB_1);
        progressBar1=findViewById(R.id.PB_2);
        SkipPrograss();
        DialogShow();
        ProDialogShow();
    }

    private void ProDialogShow() {
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressDialog progressDialog=new ProgressDialog(Main3Activity.this);
                progressDialog.setTitle("hahahhahhaahhah");
                progressDialog.setMessage("Loading");
                progressDialog.setCancelable(true);
                progressDialog.show();

            }
        });
    }

    private void SkipPrograss() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(progressBar.getVisibility()==View.VISIBLE){
                    progressBar.setVisibility(View.GONE);
                    int process=progressBar1.getProgress();
                    process=process+10;
                    progressBar1.setProgress(process);
                }else {
                    progressBar.setVisibility(View.VISIBLE);
                }
            }
        });
    }

   public void  DialogShow(){
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alg=new AlertDialog.Builder(Main3Activity.this);
                alg.setTitle("welcome");
                alg.setMessage("are you sure?");
                alg.setCancelable(false);
                alg.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(Main3Activity.this,"ok you are so beautiful",Toast.LENGTH_SHORT).show();

                    }
                });
                alg.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(Main3Activity.this,"NO I will be SKR" ,Toast.LENGTH_SHORT).show();


                    }
                });
                alg.show();

            }
        });

    }
}
