package com.example.myapplication;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import java.util.Random;
import android.os.Handler;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private MapView mapView;
    private RelativeLayout mapRelative;

    private PosService servicePosBinder;
    protected ServiceConnection mPos = new ServiceConnection(){
        public void onServiceConnected(ComponentName className, IBinder service) {
            // Called when the connection is made.
            servicePosBinder = ((PosService.MyBinder)service).getService();
            servicePosBinder.setPosResultListener(new PosService.PosResultListener() {
                @Override
                public void PosR(String s) {
                    Toast.makeText(MainActivity.this,s,Toast.LENGTH_SHORT).show();
                }
            });
        }
        public void onServiceDisconnected(ComponentName className) {
            // Received when the service unexpectedly disconnects.
            servicePosBinder = null;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mapView = new MapView(this,this);
        mapRelative=(RelativeLayout)findViewById(R.id.map_rel);
        mapRelative.addView(mapView);
        Log.d("@@@@",SPUtils.get(this,"mac_es","").toString());
        Log.d("@@@@",SPUtils.get(this,"mac_en","").toString());
        Log.d("@@@@",SPUtils.get(this,"mac_ws","").toString());
        Log.d("@@@@",SPUtils.get(this,"mac_wn","").toString());
        if(SPUtils.get(this,"mac_es","")!=null&&SPUtils.get(this,"mac_en","")!=null
                &&SPUtils.get(this,"mac_wn","")!=null&&SPUtils.get(this,"mac_ws","")!=null){
            Intent bindIntent = new Intent(this, PosService.class);
            bindService(bindIntent, mPos, Context.BIND_AUTO_CREATE);
            startService(bindIntent);
        }else{
        }
        handler.post(task);
    }

    private Handler handler = new Handler();
    private Runnable task =new Runnable() {
        public void run() {
            if (handler == null) {
                return;
            }
            handler.postDelayed(this, 1 * 1000);
            Random r = new Random();
            mapView.change(r.nextInt(1000), r.nextInt(1000));
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        /* Start an intent to launch the SettingsActivity */
        if (id == R.id.action_settings) {
            Intent intent = new Intent();
            intent.setClass(this,SettingActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onDestroy() {
        unbindService(mPos);
        super.onDestroy();
    }
}
