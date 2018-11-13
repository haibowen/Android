package com.example.administrator.mydownloadexample;

import android.os.AsyncTask;
import android.os.Environment;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

public class DownloadTask extends AsyncTask<String ,Integer,Integer> {
    public  static  final  int TYPE_SUCCESS=0;
    public  static  final int TYPE_FAILED=1;
    public static  final int TYPE_PAUSED=2;
    public  static  final  int TYPE_CANCELED=3;

    private Downloadlistener downloadlistener;
    private boolean isCanceled=false;
    private boolean isPaused=false;
    private int lastProgress;
    public DownloadTask(Downloadlistener downloadlistener){
        this.downloadlistener=downloadlistener;

    }
    @Override
    protected Integer doInBackground(String... params) {

        InputStream inputStream=null;
        RandomAccessFile randomAccessFile=null;
        File file=null;
        try {
            long downloadedLength=0;//记录已经下载的文件长度
            String downloadUrl=params[0];
            String fileName=downloadUrl.substring(downloadUrl.lastIndexOf("/"));
            String directory= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
            file=new File(directory+fileName);
            if (file.exists()){
                downloadedLength=file.length();

            }
            long contentLength=getContentLength(downloadUrl);
            if (contentLength==0){
                return TYPE_FAILED;
            }else if (contentLength==downloadedLength){

                return TYPE_SUCCESS;
            }
            OkHttpClient client=new OkHttpClient();
            Request request=new Request.Builder()
                    .addHeader("RANGE","bytes="+downloadedLength+"-")
                    .url(downloadUrl)
                    .build();
            Response response=client.newCall(request).execute();
            if (response!=null){
                inputStream=response.body().byteStream();
                randomAccessFile=new RandomAccessFile(file,"rw");
                randomAccessFile.seek(downloadedLength);
                byte[] b=new byte[1024];
                int total=0;
                int len;
                while ((len=inputStream.read(b))!=-1){
                    if (isCanceled){
                        return TYPE_CANCELED;
                    }else if (isPaused){
                        return TYPE_PAUSED;
                    }else {
                        total+=len;
                        randomAccessFile.write(b,0,len);
                        int progress= (int) ((total+downloadedLength)*100/contentLength);
                        publishProgress(progress);

                    }
                }
                response.body().close();
                return TYPE_SUCCESS;
            }


        }catch (Exception e){
            e.printStackTrace();

        }finally {
            try {
                if (inputStream!=null){
                    inputStream.close();
                }
                if (randomAccessFile!=null){
                    randomAccessFile.close();
                }
                if (isCanceled&&file!=null){
                    file.delete();
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }


       return  TYPE_FAILED;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        int progress=values[0];
        if (progress>lastProgress){
            downloadlistener.onProgress(progress);
            lastProgress=progress;

        }

    }

    @Override
    protected void onPostExecute(Integer status) {

        switch (status){
            case TYPE_SUCCESS:
                downloadlistener.onSuccess();
                break;
            case TYPE_FAILED:
                downloadlistener.onFailed();
                break;
            case  TYPE_PAUSED:
                downloadlistener.onPaused();
                break;
            case TYPE_CANCELED:
                downloadlistener.onCanceled();
                break;
                default:
                    break;
        }

    }
    public void pauseDownload(){

        isPaused=true;
    }
    public void  cancelDownload(){
        isCanceled=true;
    }
    private long getContentLength(String downloadUrl) throws IOException {
        OkHttpClient okHttpClient=new OkHttpClient();
        Request request=new Request.Builder()
                .url(downloadUrl)
                .build();
        Response response=okHttpClient.newCall(request).execute();
        if (response!=null&&response.isSuccessful()){
            long contentLength=response.body().contentLength();
            response.body().close();
            return contentLength;
        }
        return 0;
    }
}
