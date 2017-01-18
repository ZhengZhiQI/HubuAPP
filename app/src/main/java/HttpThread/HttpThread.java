package HttpThread;

import android.os.Debug;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Alan on 2017/1/18.
 */

public class HttpThread {
   // public static String echoFromPHP;   //服务器返回的字符串
    //向服务器发送消息的线程，listener用于接收返回的数据
   // Listener用于防止子线程未返回服务器数据，必须添加
    public static void sendHttpMessage(final String text, final String address, final HttpCallbackListener listener){

        new Thread(new Runnable() {
            @Override
            public void run() {

                HttpURLConnection connection = null;

                try {
                    URL  url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setReadTimeout(5000);
                    OutputStream out = connection.getOutputStream();
                    String content = "text=" + text;
                    out.write(content.getBytes());

                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuffer sb = new StringBuffer();
                    String str;

                    while ((str = reader.readLine()) != null) {
                        sb.append(str);
                    }

                    if (listener != null){

                        listener.onFinish(sb.toString());
                    }
                  //  echoFromPHP = sb.toString();
                    //在logcat输出这个值用以检查验证
                  //  System.out.println("PHP服务器返回的字符串信息为:" + echoFromPHP);
                } catch (Exception e) {
                    if (listener != null){
                        listener.onError(e);
                    }
                    e.printStackTrace();
                }finally {
                    if (connection != null){
                        connection.disconnect();
                    }
                }

            }
        }).start();


    }
//HttpCallbackListener用于防止子线程未返回服务器数据
    public interface HttpCallbackListener {
        void onFinish(String response);
        void onError(Exception e);
    }



}
