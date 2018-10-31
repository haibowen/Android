package com.example.administrator.mydownloadtest;

public interface MyLinstener {

    void onprogress(int progress);
    void onSucess();
    void onFailed();
    void onPause();
    void onCanceled();

}
