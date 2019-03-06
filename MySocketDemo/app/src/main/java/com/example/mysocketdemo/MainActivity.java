package com.example.mysocketdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editText;
    private Button buttonSend, buttonConnect, buttonDisConnect;

    private static final String HOST = "192.168.0.19";
    private static final int PORT = 8888;
    private PrintWriter printWriter;
    private BufferedReader bufferedReader;
    private ExecutorService executorService = null;
    private String ReceiveMsg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.edit_show);
        buttonSend = findViewById(R.id.send);
        buttonConnect = findViewById(R.id.connect);
        buttonDisConnect = findViewById(R.id.disconnect);

        buttonDisConnect.setOnClickListener(this);
        buttonConnect.setOnClickListener(this);
        buttonSend.setOnClickListener(this);

        executorService = Executors.newCachedThreadPool();


    }

    public void Connect() {

        executorService.execute(new ConnectService());


    }

    public void Send() {
        String sendMsg = editText.getText().toString();
        executorService.execute(new sendService(sendMsg));


    }

    public void DisConnect() {
        executorService.execute(new sendService("0"));


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.send:

                Send();

                break;

            case R.id.connect:

                Connect();

                break;

            case R.id.disconnect:

                DisConnect();

                break;
        }

    }

    private class sendService implements Runnable {

        private String msg;

        public sendService(String msg) {
            this.msg = msg;
        }

        @Override
        public void run() {

            printWriter.println(this.msg);


        }
    }

    private class ConnectService implements Runnable {

        @Override
        public void run() {

            try {
                Socket socket = new Socket(HOST, PORT);
                socket.setSoTimeout(6000);

                printWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                        socket.getOutputStream(), "UTF-8")), true);
                bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
                receiveMsg();


            } catch (Exception e) {
                e.printStackTrace();

                Log.e("2222", "run: "+e.getMessage() );


            }

        }

        private void receiveMsg() {
            try {
                while (true) {

                    if ((ReceiveMsg = bufferedReader.readLine()) != null) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                            }
                        });
                    }
                }

            } catch (IOException e) {

                e.printStackTrace();
            }
        }
    }


}
