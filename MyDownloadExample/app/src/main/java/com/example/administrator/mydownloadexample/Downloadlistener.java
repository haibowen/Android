package com.example.administrator.mydownloadexample;

public interface Downloadlistener {

    void onProgress(int progress);
    void  onSuccess();
    void  onFailed();
    void onPaused();
    void  onCanceled();

}
