package com.example.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.net.wifi.ScanResult;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import java.util.List;

/**
 * Created by xubowen on 2017/5/10.
 */
public class WifiActivity extends AppCompatActivity {
    private WifiManager wifiManager;
    private List<ScanResult> list;
    private PermissionHelper mHelper = new PermissionHelper(this);
    private SwipeRefreshLayout layout;
    private String pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi);
        pos=getIntent().getStringExtra("pos");
        //initWifi();
        layout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayouts);
        initData();
        layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });
    }
    private void initWifi(){
        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        mHelper.requestPermissions("请授予WLAN权限",
                new PermissionHelper.PermissionListener() {
                    @Override
                    public void doAfterGrand(String... permission) {

                    }
                    @Override
                    public void doAfterDenied(String... permission) {
                        Toast.makeText(WifiActivity.this,"获取WLAN权限失败，无法使用该功能",Toast.LENGTH_LONG).show();
                    }
                }, Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.CHANGE_WIFI_STATE);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mHelper.handleRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    private void initData(){
        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        list = wifiManager.getScanResults();
        ListView listView = (ListView) findViewById(R.id.listview_wifi);
        if (list == null) {
            Toast.makeText(WifiActivity.this, "附近没有wifi", Toast.LENGTH_LONG).show();
        }else {
            //
            layout.setRefreshing(false);
            listView.setAdapter(new WifiAdapter(WifiActivity.this,list));
        }
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent mIntent = new Intent();
                mIntent.putExtra("mac",list.get(position).BSSID);
                mIntent.putExtra("name",list.get(position).SSID);
                if(pos.equals("es")){
                    SPUtils.put(WifiActivity.this,"mac_es",list.get(position).BSSID);
                    SPUtils.put(WifiActivity.this,"name_es",list.get(position).SSID);
                    setResult(1000, mIntent);
                }else if(pos.equals("en")){
                    SPUtils.put(WifiActivity.this,"mac_en",list.get(position).BSSID);
                    SPUtils.put(WifiActivity.this,"name_en",list.get(position).SSID);
                    setResult(1001, mIntent);
                }else if(pos.equals("wn")){
                    SPUtils.put(WifiActivity.this,"mac_wn",list.get(position).BSSID);
                    SPUtils.put(WifiActivity.this,"name_wn",list.get(position).SSID);
                    setResult(1002, mIntent);
                }else if(pos.equals("ws")){
                    SPUtils.put(WifiActivity.this,"mac_ws",list.get(position).BSSID);
                    SPUtils.put(WifiActivity.this,"name_ws",list.get(position).SSID);
                    setResult(1003, mIntent);
                }
                finish();
            }
        });

    }

}
