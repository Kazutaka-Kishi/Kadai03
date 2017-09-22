package com.example.kadai03;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by st201707 on 2017/08/30.
 */

public class ServerThread extends Thread {

    private ServerThreadHandler ui_handler = null;
    private String wifistr;
    private int port;

    public ServerThread(int port, ServerThreadHandler ui_handler) {
        this.port = port;
        this.ui_handler = ui_handler;
    }

    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(this.port);
            while (true) {
                Socket socket = serverSocket.accept();
                InputStream mInput = socket.getInputStream();
                char[] buf = new char[1024];
                Reader reader = new InputStreamReader(mInput, "UTF-8");
                StringBuilder stb = new StringBuilder();
                int len;
                while ((len = reader.read(buf)) != -1){
                    stb.append(buf, 0, len);
                }
                mInput.close();
                wifistr = stb.toString();
                Log.i("accept", stb.toString());

                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putString("wifi_str", wifistr);
                message.setData(bundle);
                ui_handler.sendMessage(message);
            }
        } catch (IOException e) {
            Log.e("Sample07", "error" + e);
        }
        super.run();
    }
}

