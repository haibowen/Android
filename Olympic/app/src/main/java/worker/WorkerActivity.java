package worker;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wenhaibo.olympic.R;

import chat.RegistActivity;
import newnote.NoteActivity;


public class WorkerActivity extends AppCompatActivity {
    private GridView GV;
    private ImageView IV;
    private TextView TV;
    private int picture []={R.drawable.y,R.drawable.yb,R.drawable.yc,R.drawable.yd};
    private String msn[]={"按时工作","一键求医","实时沟通","工作心得",};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker);
        GV=(GridView)findViewById(R.id.GV);
        GV.setAdapter(new Mydapter());
        GV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:{
                       Intent intent0=new Intent(WorkerActivity.this, TaskActivity.class);
                        startActivity(intent0);
                        break;

                    }



                    case 1:{
                        AlertDialog.Builder dialog = new AlertDialog.Builder(WorkerActivity.this);
                        dialog.setTitle("YOU ARE NEED HELP");
                        dialog.setMessage("紧急电话拨打,请确认你是否真的需要！！！");
                        dialog.setCancelable(false);
                        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent2 = new Intent(Intent.ACTION_CALL);
                                intent2.setData(Uri.parse("tel:10086"));
                                if (ActivityCompat.checkSelfPermission(WorkerActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                    // TODO: Consider calling
                                    //    ActivityCompat#requestPermissions
                                    // here to request the missing permissions, and then overriding
                                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                    //                                          int[] grantResults)
                                    // to handle the case where the user grants the permission. See the documentation
                                    // for ActivityCompat#requestPermissions for more details.
                                    return;

                                }
                                startActivity(intent2);


                            }

                        });
                        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                        dialog.show();
                        break;

                    }
                    case 2:{
                        Intent intent3=new Intent(WorkerActivity.this, RegistActivity.class);
                        startActivity(intent3);
                        break;

                    }
                    case 3:{
                        Intent intent4=new Intent(WorkerActivity.this, NoteActivity.class);
                        startActivity(intent4);
                        break;

                    }
                }
            }
        });

    }
    public class Mydapter extends BaseAdapter{

        @Override
        public int getCount() {
            return picture.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view=View.inflate(WorkerActivity.this,R.layout.itemsworker,null);
            IV=(ImageView)view.findViewById(R.id.IV);
            TV=(TextView)view.findViewById(R.id.TV);
            IV.setImageResource(picture[position]);
            TV.setText(msn[position]);
            return view;
        }
    }




}
