package com.suse.networktest;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.suse.networktest.utils.HttpCallbackListener;
import com.suse.networktest.utils.HttpUtils;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;


public class MainActivity extends AppCompatActivity{
    private static final String TAG = "MainActivity";
    private Button mSendRequest;
    private TextView mResponseText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSendRequest = findViewById(R.id.send_request);
        mResponseText = findViewById(R.id.response_text);
        mSendRequest.setOnClickListener((View view)->{
            //通过HttpURLConnection发送网络请求
            //sendRequestWithHttpURLConnection();

            //通过OKHttp发送网络请求
            //sendRequestWithOkHttp();

            //测试HttpUtils
            /*HttpUtils.sendOkHttpRequest("http://192.168.101.10:8082/get_data.json",
                    new Callback() {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
                            Log.e(TAG, "onFailure: ",e);
                        }

                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                            parseJSONWithGSON(response.body().string());
                        }
                    });*/
            HttpUtils.sendHttpRequest("http://192.168.101.10:8082/get_data.json", new HttpCallbackListener() {
                @Override
                public void onFinish(String response) {
                    parseJSONWithGSON(response);
                }

                @Override
                public void onError(Exception e) {
                    Log.e(TAG, "onFailure: ",e);
                }
            });
        });
    }

    //通过OKHttp发送网络请求
    private void sendRequestWithOkHttp() {
        new Thread(()->{
            OkHttpClient client = new OkHttpClient();
            Request request = new  Request.Builder()
                    //192.168.101.8是电脑的IP地址
                    //请求get_data.json
                    .url("http://192.168.101.8:8082/get_data.json")
                    //请求get_data.xml
                    //.url("http://192.168.101.8:8082/get_data.xml")
                    .build();
            try(Response response = client.newCall(request).execute()){
                String responseData = response.body().string();
                //=============解析JSON文件==============//
                //用JSONObject解析JSON数据
                //parseJSONWithJSONObject(responseData);
                //用Gson解析JSON数据
                parseJSONWithGSON(responseData);
                //=============解析XML文件==============//
                //用SAX解析XML
                //parseXMLWithSAX(responseData);
                //用Pull解析XML
                //parseXMLWithPull(responseData);
                //=============将数据显示在界面上==============//
                //showResponse(responseData);
            }catch (Exception e){
                e.printStackTrace();
            }
        }).start();
    }

    //用Gson解析JSON数据
    private void parseJSONWithGSON(String jsonData) {
        Gson gson = new Gson();
        List<App> appList = gson.fromJson(jsonData,new TypeToken<List<App>>(){}.getType());
        for (App app:appList){
            Log.d(TAG, app.toString());
        }
    }

    //用JSONObject解析JSON数据
    private void parseJSONWithJSONObject(String jsonData) {
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            for (int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String id  = jsonObject.getString("id");
                String name = jsonObject.getString("name");
                String version = jsonObject.getString("version");
                Log.d(TAG, "id: "+id+"\tname: " + name + "\tversion: " + version);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //用SAX解析XML
    private void parseXMLWithSAX(String responseData) {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            XMLReader xmlReader = factory.newSAXParser().getXMLReader();
            ContentHandler handler = new ContentHandler();
            xmlReader.setContentHandler(handler);
            //开始执行解析
            xmlReader.parse(new InputSource(new StringReader(responseData)));
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //用Pull解析XML
    private void parseXMLWithPull(String responseData) {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(new StringReader(responseData));
            int eventType = xmlPullParser.getEventType();
            String id = "";
            String name = "";
            String version = "";
            while (eventType != XmlPullParser.END_DOCUMENT){
                String nodeName = xmlPullParser.getName();
                switch (eventType){
                    //开始解析某个节点
                    case XmlPullParser.START_TAG:{
                        if ("id".equals(nodeName)){
                            id = xmlPullParser.nextText();
                        }else if("name".equals(nodeName)){
                            name = xmlPullParser.nextText();
                        }else  if("version".equals(nodeName)){
                            version = xmlPullParser.nextText();
                        }
                        break;
                    }
                    case XmlPullParser.END_TAG:{
                        if ("app".equals(nodeName)){
                            Log.d(TAG, "id is " + id);
                            Log.d(TAG, "name is " + name);
                            Log.d(TAG, "version is " + version);
                        }
                        break;
                    }
                    default:
                        break;
                }
                eventType = xmlPullParser.next();
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
    }

    //通过HttpURLConnection发送网络请求
    private void sendRequestWithHttpURLConnection() {
        //开启线程来发起网络请求
        new Thread(()->{
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                try {
                    URL url = new URL("http://www.baidu.com");
                    connection = (HttpURLConnection) url.openConnection();
                    //如果是发送POST请求，发送数据的话
                  /*  connection.setRequestMethod("POST");
                    DataOutputStream out  = new DataOutputStream(connection.getOutputStream());
                    out.writeBytes("username=admin&password=123456");*/

                    //发送GET请求获取数据
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream in = connection.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while((line = reader.readLine()) != null){
                        response.append(line);
                    }
                    showResponse(response.toString());
                } finally {
                   if (reader != null){
                       reader.close();
                   }
                   if (connection != null){
                       connection.disconnect();
                   }
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void showResponse(String response) {
        //Android不允许在子线程中进行UI操作，我们需要通过这个方法将线程切换到主线程
        runOnUiThread(()->{
            //将结果显示在界面上
            mResponseText.setText(response);
        });
    }
}