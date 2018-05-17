package com.example.wenhaibo.androidstudy04;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<Message> messageList=new ArrayList<>(  );
    private EditText inputText;
    private Button send;
    private RecyclerView recyclerView;
    private MassageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );


        //初始化数据

        initMsgs();

        //绑定id

        inputText=findViewById( R.id.input_text );
        send=findViewById( R.id.button_send );
        recyclerView=findViewById( R.id.recycler_view );

        //布局控制器

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager( this );
        recyclerView.setLayoutManager( linearLayoutManager );

        //加载适配器

        adapter=new MassageAdapter( messageList );
        recyclerView.setAdapter( adapter );

        send.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content=inputText.getText().toString();
                if(!"".equals( content )){
                    Message message=new Message( content,Message.TYPE_SENT);
                    messageList.add( message );

                    //当有消息时刷新显示

                    adapter.notifyItemInserted( messageList.size()-1 );

                    //将recyclerview 定位到最后一行

                    recyclerView.scrollToPosition( messageList.size()-1 );

                    //清空输入空内容

                    inputText.setText( "" );

                }
            }
        } );

    }

    private void initMsgs() {

        Message message=new Message( "hello guy",Message.TYPE_RECEIVED );
        messageList.add( message );

        Message message1=new Message( "hi,how are you?",Message.TYPE_SENT );
        messageList.add( message1 );

        Message message2=new Message( "bybye",Message.TYPE_RECEIVED );
        messageList.add( message2 );
    }
}
