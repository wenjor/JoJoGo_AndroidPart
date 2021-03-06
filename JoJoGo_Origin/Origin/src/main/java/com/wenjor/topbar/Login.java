package com.wenjor.topbar;


import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static java.lang.Thread.sleep;

public class Login extends Activity {

    private TextView uname,password;
    private Button post,forget,regist;
    private Handler handle= new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    SharedPreferences sp = getSharedPreferences("token",Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                   // Map<String,Object>map = (Map<String,Object>)msg.obj;
                    Gson gson=new Gson(); Map<String,Object> map = new HashMap<String,Object>();
                    map = gson.fromJson((String)msg.obj,map.getClass());
                    String tokenid =""+map.get("data");
                    System.out.println("DDDDDDDDDDDDDDDD"+tokenid);
                    editor.putString("data",tokenid);
                    editor.putString("name","wenjor2");
                    editor.commit();
                    break;
                default:
                    break;
            }
        }
    };
    private Handler handle2= new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    SharedPreferences sp = getSharedPreferences("ShopId", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(msg.obj);
                    Log.d("TAG", "LoginHandle2 saved json is " + json);
                    editor.putString("ShopId", json);

                    editor.commit();
                    break;
                default:
                    break;
            }
        }
    };
    private Handler handle3= new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    SharedPreferences sp = getSharedPreferences("shopInf",Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    Gson gson=new Gson();
                    String json = gson.toJson(msg.obj);

                    Log.d("TAG", "LoginHandle3 saved json is "+ json);
                    editor.putString("alterShopInf",json);
                    editor.commit();
                    break;
                default:
                    break;
            }
        }
    };
    private HttpClientClass httpclient;
    private String a,b;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        uname = findViewById(R.id.username);
        password = findViewById(R.id.password);
        post = findViewById(R.id.post);
        forget = findViewById(R.id.forget);
        regist = findViewById(R.id.forget2);


        a= uname.getText().toString();
        b=password.getText().toString();


        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获得token
                String st = "http://nightwing.top:8080/shop/login";
                Map<String,Object>map = new LinkedHashMap<String, Object>();
//                if(!uname.getText().equals("")&&!password.getText().equals("")){
//                    map.put("tel",uname.getText());map.put("password",password.getText());
//                }else{
//                    map.put("tel","13205586387");
//                    map.put("password", "867149");
//                }
                map.put("tel","15725365670");
//                map.put("tel","17720429416");
                map.put("password", "B0001FGAO4");
//                map.put("password", "B000AA5SMU");
                try {
                    httpclient = new HttpClientClass(st,"POST","JSON",map,
                            handle, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //Toast.makeText(Login.this,"On building",Toast.LENGTH_SHORT).show();
                httpclient.start();
                //handle.handleMessage();

                //获得StoreId
                 st = "http://nightwing.top:8080/shop/getshopid";//Log.d("ShopWebGGGGGGGGGGGGG",st);
                SharedPreferences  sp = getSharedPreferences("token",Activity.MODE_PRIVATE);
                String tokenid = sp.getString("data","");
                Log.d("authorization",tokenid);
                Map<String,Object> headers = new LinkedHashMap<String, Object>();
                headers.put("Authorization",tokenid);
                try{
                    httpclient = new HttpClientClass(st,
                            "GET",
                            "JSON",
                            null,
                            handle2, headers);
                }catch (Exception e){
                    e.printStackTrace();
                }
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (tokenid) {
                    httpclient.start();
                }

                sp= getSharedPreferences("token",
                        Activity.MODE_PRIVATE);
                String data =sp.getString("data", "");
                String name = sp.getString("name","");
                System.out.println("AAAAAAAAAAAAAAAAAAAAAAAA"+name);
                Toast.makeText(Login.this, "读取数据如下："+"\n"+"data：" + data ,
                        Toast.LENGTH_LONG).show();

                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //商店信息
//                 sp = getSharedPreferences("ShopId",Activity.MODE_PRIVATE);
//                String json = sp.getString("ShopId",null);
//                Log.d("ShopInfGGGGGGGGGGGGG",json);
//                Gson gson=new Gson(); map = new HashMap<String,Object>();
//                String ins = new String();
//                ins =gson.fromJson(json,ins.getClass());
//                map = gson.fromJson(ins,map.getClass());
//                String ShopId =(new Double((double)map.get("data"))).intValue()+"";
//
//
//
//                 st = "http://nightwing.top:8080/shop/1?id="+ShopId;
//                map = new LinkedHashMap<String, Object>();
//                try{
//                    httpclient = new HttpClientClass(st,"GET","JSON",map,handle3, null);
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//                httpclient.start();
                finish();
            }
        });
    }


}
