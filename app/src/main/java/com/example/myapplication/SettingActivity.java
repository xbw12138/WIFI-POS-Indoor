package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by xubowen on 2017/5/10.
 */
public class SettingActivity extends AppCompatActivity {

    private String AP_NAME_WN;
    private String AP_NAME_WS;
    private String AP_NAME_ES;
    private String AP_NAME_EN;

    private String AP_MAC_WN;
    private String AP_MAC_WS;
    private String AP_MAC_ES;
    private String AP_MAC_EN;

    private Button btn_bindwn;//西北
    private Button btn_bindws;//西南
    private Button btn_bindes;//东南
    private Button btn_binden;//东北

    private TextView tx_bindwn;//西北
    private TextView tx_bindws;//西南
    private TextView tx_bindes;//东南
    private TextView tx_binden;//东北

    private SPUtils spUtils;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        spUtils=new SPUtils();
        FindView();
        Listen();
    }
    private void FindView(){
        btn_binden=(Button)findViewById(R.id.button_en);
        btn_bindes=(Button)findViewById(R.id.button_es);
        btn_bindwn=(Button)findViewById(R.id.button_wn);
        btn_bindws=(Button)findViewById(R.id.button_ws);

        tx_binden=(TextView)findViewById(R.id.tx_en);
        tx_bindes=(TextView)findViewById(R.id.tx_es);
        tx_bindwn=(TextView)findViewById(R.id.tx_wn);
        tx_bindws=(TextView)findViewById(R.id.tx_ws);

        if(SPUtils.get(this,"name_es","")!=null){
            tx_bindes.setText(SPUtils.get(this,"name_es","").toString());
        }
        if(SPUtils.get(this,"name_en","")!=null){
            tx_binden.setText(SPUtils.get(this,"name_en","").toString());
        }
        if(SPUtils.get(this,"name_wn","")!=null){
            tx_bindwn.setText(SPUtils.get(this,"name_wn","").toString());
        }
        if(SPUtils.get(this,"name_ws","")!=null){
            tx_bindws.setText(SPUtils.get(this,"name_ws","").toString());
        }

    }
    private void Listen(){
        btn_bindes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent=new Intent();
                mIntent.setClass(SettingActivity.this,WifiActivity.class);
                mIntent.putExtra("pos","es");
                startActivityForResult(mIntent, 1000);
            }
        });
        btn_binden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent=new Intent();
                mIntent.setClass(SettingActivity.this,WifiActivity.class);
                mIntent.putExtra("pos","en");
                startActivityForResult(mIntent, 1001);
            }
        });
        btn_bindwn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent=new Intent();
                mIntent.setClass(SettingActivity.this,WifiActivity.class);
                mIntent.putExtra("pos","wn");
                startActivityForResult(mIntent, 1002);
            }
        });
        btn_bindws.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent=new Intent();
                mIntent.setClass(SettingActivity.this,WifiActivity.class);
                mIntent.putExtra("pos","ws");
                startActivityForResult(mIntent, 1003);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null)
            return;
        if (requestCode == 1000) {
            String SSID = data.getStringExtra("name");
            String BSSID = data.getStringExtra("mac");
            tx_bindes.setText(SSID);
        }else if(requestCode==1001){
            String SSID = data.getStringExtra("name");
            String BSSID = data.getStringExtra("mac");
            tx_binden.setText(SSID);
        }else if(requestCode==1002){
            String SSID = data.getStringExtra("name");
            String BSSID = data.getStringExtra("mac");
            tx_bindwn.setText(SSID);
        }else if(requestCode==1003){
            String SSID = data.getStringExtra("name");
            String BSSID = data.getStringExtra("mac");
            tx_bindws.setText(SSID);

        }

    }

}
