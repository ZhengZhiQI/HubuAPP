package com.example.alan.hubuapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import HttpThread.HttpThread;
import JsonChange.UserInfo;

public class MainActivity extends AppCompatActivity {

    private Button buttonSend;
    private EditText editText;
    private String echoFromPHP = null;
    private Button getJson;
    private TextView textView;
    private Button parseJson;



    /**
     *handler子线程更新UI
     */
    private android.os.Handler handler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                default:
                    textView.setText(msg.obj.toString());


            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonSend = (Button) findViewById(R.id.button);
        editText = (EditText) findViewById(R.id.editText);
        getJson = (Button) findViewById(R.id.getJson);
        textView = (TextView) findViewById(R.id.textView);
        parseJson = (Button) findViewById(R.id.parseJson);


        /**
         *发送数据，并从服务器获取数据的按键事件
         */
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String text = editText.getText().toString().trim();
                String url = "http://192.168.31.140:3002/Hubu/Interface/testAPI_1.php";

                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                if (networkInfo == null || !networkInfo.isAvailable()) {
                    Toast.makeText(MainActivity.this, "当前网络不可用，无法发送数据请求", Toast.LENGTH_SHORT).show();

                } else {
                    //用此方法开启http请求
                    HttpThread.sendHttpMessage(text, url, new HttpThread.HttpCallbackListener() {
                        @Override
                        public void onFinish(String response) {
                            //数据返回后需要走的逻辑
                            Message msg = new Message();
                            msg.obj = response;
                            handler.sendMessage(msg);
                            Log.d("返回的数据", echoFromPHP);

                        }

                        @Override
                        public void onError(Exception e) {
                            //异常情况需要走的逻辑
                        }
                    });

                    //Toast.makeText(MainActivity.this, echoFromPHP, Toast.LENGTH_SHORT).show();

                }
            }
        });



        /**
         *从服务器获取json数据的按键事件
        */
        getJson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "http://192.168.31.140:3002/get_data.json";
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                if (networkInfo == null || !networkInfo.isAvailable()) {
                    Toast.makeText(MainActivity.this, "当前网络不可用，无法发送数据请求", Toast.LENGTH_SHORT).show();

                } else {
                    //用此方法开启http请求
                    HttpThread.GetJson(url, new HttpThread.HttpCallbackListener() {
                        @Override
                        public void onFinish(String response) {
                            //数据返回后需要走的逻辑
                            echoFromPHP = response;
                            Log.d("返回的Json数据", echoFromPHP);
                            Message msg = new Message();
                            msg.obj = response;
                            handler.sendMessage(msg);
                        }

                        @Override
                        public void onError(Exception e) {
                            //异常情况需要走的逻辑
                        }
                    });

                    // Toast.makeText(MainActivity.this, echoFromPHP, Toast.LENGTH_SHORT).show();


                }
            }
        });


          /**
          *处理Json数据的按键逻辑
          */
        parseJson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "http://192.168.31.140:3002/get_data.json";
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                if (networkInfo == null || !networkInfo.isAvailable()) {
                    Toast.makeText(MainActivity.this, "当前网络不可用，无法发送数据请求", Toast.LENGTH_SHORT).show();

                } else {
                    //用此方法开启http请求
                    HttpThread.GetJson(url, new HttpThread.HttpCallbackListener() {
                        @Override
                        public void onFinish(String response) {
                            //数据返回后需要走的逻辑
                            echoFromPHP = response;
                            Log.d("返回的Json数据", echoFromPHP);
                            parseJson(response);
                        }

                        @Override
                        public void onError(Exception e) {
                            //异常情况需要走的逻辑
                        }
                    });

                    // Toast.makeText(MainActivity.this, echoFromPHP, Toast.LENGTH_SHORT).show();


                }
            }
        });

    }

    /**
     * 将Json数据转化为java对象并处理的方法
     */

    public void parseJson(String jsonData) {
        Gson gson = new Gson();
        List<UserInfo> userinfoList = gson.fromJson(jsonData, new TypeToken<List<UserInfo>>() {
        }.getType());

        Message msg = new Message();
        msg.obj = "解析出的数据为：   ";
        //以下为处理数据的逻辑
        for (UserInfo userinfo : userinfoList) {

            msg.obj += "id: " + userinfo.getId() + "    " + "name：" + userinfo.getName() + "    " + "Password:" + userinfo.getPassword() + "    ";

            Log.d("MainActivity", "id=" + userinfo.getId());
            Log.d("MainActivity", "name=" + userinfo.getName());
            Log.d("MainActivity", "password=" + userinfo.getPassword());
        }
        handler.sendMessage(msg);
    }

}
