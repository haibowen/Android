package com.example.administrator.mywifip2p.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import com.example.administrator.mywifip2p.filemode.FileTransfer;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class WifiService extends IntentService {

    public interface  OnProgressChangelistener{

        //传输进度发生变化
        void onProgressChanged(FileTransfer fileTransfer,int progress);

        //传输结束时

        void  OnTransferFinished(File file);


    }
    private ServerSocket serverSocket;

    private InputStream inputStream;
    private ObjectInputStream objectInputStream;

    private FileOutputStream fileOutputStream;
    public OnProgressChangelistener onProgressChangelistener;
    private static final int PORT=4786;

    public class MyBinder extends Binder{

        public WifiService getservice(){


            return  WifiService.this;

        }
    }



    public WifiService() {
        super("WifiService");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return  new MyBinder();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        clean();
        File file=null;

        try{
            serverSocket =new ServerSocket();
            serverSocket.setReuseAddress(true);
            serverSocket.bind(new InetSocketAddress(PORT));
            Socket client=serverSocket.accept();
            inputStream=client.getInputStream();
            objectInputStream=new ObjectInputStream(inputStream);
            FileTransfer fileTransfer= (FileTransfer) objectInputStream.readObject();
            String name=new File(fileTransfer.getFilepath()).getName();

            file=new File(Environment.getExternalStorageDirectory()+"/"+name);
            fileOutputStream=new FileOutputStream(file);
            byte [] buf=new byte[512];
            int len;
            long total=0;
            int progress;
            while ((len=inputStream.read(buf))!=-1){

                fileOutputStream.write(buf,0,len);
                total+=len;
                progress= (int) ((total*100)/fileTransfer.getFilelength());
                if (onProgressChangelistener!=null){

                    onProgressChangelistener.onProgressChanged(fileTransfer,progress);

                }
            }
            serverSocket.close();
            inputStream.close();
            objectInputStream.close();
            fileOutputStream.close();
            serverSocket=null;
            inputStream=null;
            objectInputStream=null;
            fileOutputStream=null;






        }catch (Exception e){



        }finally {
            clean();
            if (onProgressChangelistener!=null){

                onProgressChangelistener.OnTransferFinished(file);

            }
            //再次启动服务，等待客户端下次连接
            startService(new Intent(this,WifiService.class));

        }





    }

    public void  setOnProgressChangelistener(OnProgressChangelistener progressChangelistener){

        this.onProgressChangelistener=progressChangelistener;


    }

    public  void  clean(){
        if (serverSocket!=null){

            try {
                serverSocket.close();
                serverSocket=null;



            }catch (IOException e){
                e.printStackTrace();
            }
        }

        if (inputStream!=null){

            try {
                inputStream.close();
                inputStream=null;


            }catch (IOException e){
                e.printStackTrace();


            }
        }

        if (objectInputStream!=null){

            try {
                objectInputStream.close();
                objectInputStream=null;

            }catch (IOException e){
                e.printStackTrace();

            }
        }

        if (fileOutputStream!=null){

            try {
                fileOutputStream.close();
                fileOutputStream=null;

            }catch (IOException e){
                e.printStackTrace();


            }
        }


    }
}
