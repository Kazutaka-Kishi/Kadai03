package com.example.kadai03;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by st201707 on 2017/08/30.
 */

public class ClientAsyncTask extends AsyncTask {

    private String host;
    private int port;
    private String message;

    //コンストラクタ
    public ClientAsyncTask(String host, int port, String message) {
        this.host = host;
        this.port = port;
        this.message = message;
    }

    //メインスレッドとは別のスレッドで実行
    @Override
    protected Object doInBackground(Object[] params) {

        Socket socket = new Socket();
        try {
            socket.bind(null);
            socket.connect(new InetSocketAddress(this.host, this.port), 0);
            OutputStream stream = socket.getOutputStream();
            byte[] byteArray = this.message.getBytes("UTF-8");
            stream.write(byteArray, 0, byteArray.length);
            stream.close();
        } catch (IOException e) {
            Log.e("Sample07", "接続失敗" + e);
        } finally {
            if (socket != null) {
                if (socket.isConnected()) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        Log.e("Sample07", "切断失敗" + e);
                    }
                }
            }
        }
        return null;
    }
}
