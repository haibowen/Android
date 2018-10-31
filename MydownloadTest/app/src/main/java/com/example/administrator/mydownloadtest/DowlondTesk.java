package com.example.administrator.mydownloadtest;

import android.os.AsyncTask;

public class DowlondTesk extends AsyncTask<String ,Integer,Integer> {


    //状态值
    public static  final int TYDEFINE_SUCESS=0;
    public static  final  int TYDEFINE_FAILL=1;
    public static  final int TYDEFINE_PAUSE=2;
    public static  final  int TYDEFINE_CANLEE=3;

    //
    private MyLinstener myLinstener;

    //下载状态
    Boolean ispaused=false;
    Boolean iscancle=false;





    @Override
    protected Integer doInBackground(String... strings) {
        return null;
    }

  public   DowlondTesk(MyLinstener myLinstener){
        this.myLinstener=myLinstener;

    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);
    }

    public void pasedowload(){
        ispaused=true;



    }
    public  void canceldowload(){
        iscancle=true;

    }
}
