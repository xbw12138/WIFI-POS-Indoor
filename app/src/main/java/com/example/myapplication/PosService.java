package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.WindowManager;
import android.os.Handler;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.app.Service;
import android.net.wifi.ScanResult;
/**
 * Created by xubowen on 2017/5/10.
 */
public class PosService extends Service{
    public static final String TAG = "PosService";
    private final IBinder binder = new MyBinder();
    float x = 0;
    float y = 0;
    Map<String, Integer> now_status = new HashMap<String, Integer>();
    private WifiManager wifiManager;

    String Mac_es="";//SPUtils.get(this,"mac_es","").toString();//东南
    String Mac_en="";//SPUtils.get(this,"mac_en","").toString();//东北
    String Mac_ws="";//SPUtils.get(this,"mac_ws","").toString();//西南
    String Mac_wn="";//SPUtils.get(this,"mac_wn","").toString();//西北
    @Override
    public void onCreate() {
        super.onCreate();
        Mac_es=SPUtils.get(this,"mac_es","").toString();//东南
        Mac_en=SPUtils.get(this,"mac_en","").toString();//东北
        Mac_ws=SPUtils.get(this,"mac_ws","").toString();//西南
        Mac_wn=SPUtils.get(this,"mac_wn","").toString();//西北
        init();
        Log.d(TAG, "onCreate() executed");
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand() executed");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() executed");
    }
    public interface PosResultListener {
        public void PosR(String str);
    }
    private PosResultListener posResultListener=null;
    public void setPosResultListener(PosResultListener posListener) {                   ///
        this.posResultListener = posListener;                                       ///
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
    public class MyBinder extends Binder {
        public PosService getService(){
            return PosService.this;
        }
    }
    private  void update_pos(int aveEs,int aveEn,int aveWn,int aveWs){


        //Log.i("东南距离",getDistance(aveEs)+"m");//东南距离-右下
        //Log.i("东北距离",getDistance(aveEn)+"m");//东北距离-右上
        //Log.i("西北距离",getDistance(aveWn)+"m");//西北距离-左上
        //Log.i("西南距离",getDistance(aveWs)+"m");//西南距离-左下
        if(posResultListener!=null){
            posResultListener.PosR("es:"+getDistance(aveEs)+"\nen:"+getDistance(aveEn)
            +"\nwn:"+getDistance(aveWn)+"\nws:"+getDistance(aveWs));
        }
    }
    private Handler handler = new Handler();
    private Runnable task =new Runnable() {
        public void run() {
            // TODOAuto-generated method stub
            if(handler==null){
                return;
            }
            handler.postDelayed(this,1*1000);//设置延迟时间，此处是1秒
            now_status.clear();
            int Tes=0,Ten=0,Twn=0,Tws=0;
            int Res=0,Ren=0,Rwn=0,Rws=0;
            int avenum=10;
            while(Tes<avenum||Ten<avenum||Twn<avenum||Tws<avenum){
                wifiManager.startScan();
                List<ScanResult> list = wifiManager.getScanResults();
                for (ScanResult result : list) {
                    if(Mac_es.equals(result.BSSID)){
                        Tes++;
                        Res+=result.level;
                    }
                    if(Mac_en.equals(result.BSSID)){
                        Ten++;
                        Ren+=result.level;
                    }
                    if(Mac_wn.equals(result.BSSID)){
                        Twn++;
                        Rwn+=result.level;
                    }
                    if(Mac_ws.equals(result.BSSID)){
                        Tws++;
                        Rws+=result.level;
                    }
                }
                if(((Tes^Ten)|(Ten^Twn)|(Twn^Tws))!=0){//判断四个数相等
                    break;
                }
            }
            if(Tes!=0&&Ten!=0&&Twn!=0&&Tws!=0){
                int aveEs=Res/Tes;//均值滤波后的结果
                int aveEn=Ren/Ten;
                int aveWn=Rwn/Twn;
                int aveWs=Rws/Tws;
                update_pos(aveEs,aveEn,aveWn,aveWs);
            }
        }
    };
    //d=10^((ABS(RSSI)-A)/(10*n))、A 代表在距离一米时的信号强度(45 ~ 49), n 代表环境对信号的衰减系数(3.25 ~ 4.5)
    public float getDistance(int rssi) {
        return (float) Math.pow(10, (Math.abs(rssi) - 45) / (10 * 3.25));
    }
    private void init(){
        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        openWifi();
        handler.post(task);//立即调用
    }
    private void openWifi() {
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
    }
}