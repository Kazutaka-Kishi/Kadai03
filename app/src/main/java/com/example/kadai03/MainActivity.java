package com.example.kadai03;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    // これがあるとリストにどんどん追加できる！
    ArrayList<String> alertList = new ArrayList<String>();

    EditText editText;
    TextView textView;
    ListView listView;

    // 大域変数
    public ServerThread server = null;
    private MediaPlayer mp;

    public String ipaddress = "";
    public String toaddress = "";
    public String text = "";
    public String ip = "";
    public String comment = "";

    // ユーザーメソッド

    // サーバースタート
    public void startServer() {
        server = new ServerThread(9999, new ServerThreadHandler() {
            @Override
            public void onGetMessage(String wifi_str) {
                setString(wifi_str);
            }
        });
        server.start();
    }

    // 受信時のアクション
    public void setString(String message) {
        String[] ary = message.split(","); //カンマで配列に記入
        ip = "@" + ary[0];//IPアドレス
        comment = ary[1];//コメント
        mp.start();

        // ListViewへ追加
        // ArrayAdapterオブジェクト生成
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, alertList);
        //  ListAdapterセット
        alertList.add("【受信】" + ip + "【時刻】" + new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()) + "\n" + comment + "\n");
        listView.setAdapter(adapter);
    }

    // 画面表示直後に処理されるメッセージハンドラ
    @Override
    protected void onResume() {
        super.onResume();
        startServer();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText) findViewById(R.id.editText);
        textView = (TextView) findViewById(R.id.textView);
        listView = (ListView) findViewById(R.id.listView);

        // WIFIオン
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();

        // IPアドレスを変数に入れる
        int ip = wifiInfo.getIpAddress();

        // 文字列に変換
        ipaddress = ((ip >> 0) & 0xFF) + "." + ((ip >> 8) & 0xFF) + "." + ((ip >> 16) & 0xFF) + "." + ((ip >> 24) & 0xFF);

        // mp3リソース読み込み
        mp = MediaPlayer.create(this, R.raw.message);

        // TextView
        textView.setText("文章を入力してください");
        // EitText
        editText.setText("");
    }

    // メニューバーの実装
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // メニュー
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // エディットテキストのテキストを取得
        text = editText.getText().toString();

        // Spinnerオブジェクトを取得
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        // 選択されているアイテムのIndexを取得
        //int idx = spinner.getSelectedItemPosition();
        // 選択されているアイテムを取得
        String ipnum = (String) spinner.getSelectedItem();

        toaddress = "192.168.103." + ipnum;//学校用
        //String toaddress = "192.168.103." + String.valueOf(100 + ipnum);
        //toaddress = "192.168.103.107";//学校用
        //toaddress = "192.168.0." + ipnum;//自宅用

        // 送信時のアクション
        String comment = ipaddress + "," + text;
        ClientAsyncTask client = new ClientAsyncTask(toaddress, 9999, comment);
        client.execute();

        editText.setText("");

        // ListViewへ追加
        // ArrayAdapterオブジェクト生成
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, alertList);
        //  ListAdapterセット
        alertList.add("【送信】" + "@" + ipaddress + "【時刻】" + new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()) + "\n" + text + "\n");
        listView.setAdapter(adapter);

        return true;
    }

}
