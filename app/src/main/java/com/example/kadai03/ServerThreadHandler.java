package com.example.kadai03;

import android.os.Handler;
import android.os.Message;

/**
 * Created by st201707 on 2017/08/30.
 */

public abstract class ServerThreadHandler extends Handler {
    public void handleMessage(Message msg){
        String wifi_str = msg.getData().getString("wifi_str");
        onGetMessage(wifi_str);
    }
    //抽象メソッド
    public abstract void onGetMessage(String wifi_str);
}
