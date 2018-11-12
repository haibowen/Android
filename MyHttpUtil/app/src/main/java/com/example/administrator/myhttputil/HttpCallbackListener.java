package com.example.administrator.myhttputil;

public interface HttpCallbackListener {

    void  onFinish(String response);
    void onError(Exception e);
}
