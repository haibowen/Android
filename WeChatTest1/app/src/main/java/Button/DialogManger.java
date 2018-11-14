package Button;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wenhaibo.wechattest1.R;

/**
 * Created by wenhaibo on 2017/12/18.
 */

public class DialogManger {
    private Dialog dialog;
    private ImageView imageView,imageView1;
    private TextView textView;
    private Context mcontext;

    public DialogManger(Context context){

        mcontext=context;

    }
    public void showRecordingDialog(){
        dialog=new Dialog(mcontext, R.style.Theme_AudioDialog);
        LayoutInflater layoutInflater=LayoutInflater.from(mcontext);
        View view= layoutInflater.inflate(R.layout.activity_recoder,null);
        dialog.setContentView(view);
        imageView=(ImageView)dialog.findViewById(R.id.IV_1);
        imageView1=(ImageView)dialog.findViewById(R.id.IV_2);
        textView=(TextView)dialog.findViewById(R.id.TV_lab);
        dialog.show();

    }
    public void recoring(){
        if(dialog!=null&&dialog.isShowing()){
            imageView.setVisibility(View.VISIBLE);
            imageView1.setVisibility(View.VISIBLE);
            textView.setVisibility(View.VISIBLE);
            imageView.setImageResource(R.drawable.d);
            textView.setText("手指上划，取消发送");
        }

    }
    public void wantToCancel(){
        if(dialog!=null&&dialog.isShowing()){
            imageView.setVisibility(View.VISIBLE);
            imageView1.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
            imageView.setImageResource(R.drawable.b);
            textView.setText("松开手指，取消发送");
        }


    }
    public void tooshort(){
        if(dialog!=null&&dialog.isShowing()){
            imageView.setVisibility(View.VISIBLE);
            imageView1.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
            imageView.setImageResource(R.drawable.g);
            textView.setText("录音时间过短");
        }

    }
    public   void dimissDialog(){
        if(dialog!=null&&dialog.isShowing()) {

        dialog.dismiss();
        dialog=null;



        }

    }
    public void updateVoiceLevel(int level){

        if(dialog!=null&&dialog.isShowing()){
           // imageView.setVisibility(View.VISIBLE);
            //imageView1.setVisibility(View.VISIBLE);
           // textView.setVisibility(View.VISIBLE);


        }

    }

}
