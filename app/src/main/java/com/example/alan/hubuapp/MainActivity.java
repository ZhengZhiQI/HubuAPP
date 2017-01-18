package com.example.alan.hubuapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import HttpThread.HttpThread;

public class MainActivity extends AppCompatActivity {

    private Button buttonSend;
    private EditText editText;
    private String echoFromPHP = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonSend = (Button) findViewById(R.id.button);
        editText = (EditText) findViewById(R.id.editText);

        //button的发送事件
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String text = editText.getText().toString().trim();
                String url = "http://192.168.31.140:3002/Hubu/Interface/testAPI_1.php";

                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                if (networkInfo == null || !networkInfo.isAvailable()){
                    Toast.makeText(MainActivity.this, "当前网络不可用，无法发送数据请求", Toast.LENGTH_SHORT).show();

                }else{
                    //用此方法开启http请求
                    HttpThread.sendHttpMessage(text, url, new HttpThread.HttpCallbackListener() {
                        @Override
                        public void onFinish(String response) {
                        //数据返回后需要走的逻辑
                            echoFromPHP = response;
                            Log.d("返回的数据",echoFromPHP);
                        }

                        @Override
                        public void onError(Exception e) {
                        //异常情况需要走的逻辑
                        }
                    });

                    Toast.makeText(MainActivity.this, echoFromPHP, Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
}
