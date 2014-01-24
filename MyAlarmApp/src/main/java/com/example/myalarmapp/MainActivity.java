package com.example.myalarmapp;


import android.app.Activity;
import android.app.AlarmManager;
import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.app.PendingIntent;
import android.content.Intent;

import android.content.IntentFilter;
import android.content.BroadcastReceiver;
import android.util.Log;
import android.net.wifi.WifiManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileLockInterruptionException;
import java.sql.SQLException;
import java.util.List;
import android.net.wifi.ScanResult;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ListView;
import android.net.wifi.WifiManager;


import android.content.Context;


public class MainActivity extends Activity {

    private static final String TAG = "MyActivity";

    WifiManager mainWifi;
    WifiReceiver receiverWifi;
    public List<ScanResult> wifiList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getDBFileSys();
        int erm = R.string.blah;

        //getSSIDs();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {

        int id = item.getItemId();
        boolean r = true;
        switch(id)
        {
            case R.id.action_exit:
                finish();
                break;
            case R.id.action_layout2:
                setContentView(R.layout.layout2);
                break;
            default:
                r = super.onMenuItemSelected(featureId, item);

        }
        return r;

    }

    public void startAlert(View view) {
        EditText text = (EditText) findViewById(R.id.time);
        int i = Integer.parseInt(text.getText().toString());
        Intent intent = new Intent(this, MyBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this.getApplicationContext(), 234324243, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
                + (i * 1000), pendingIntent);
        Toast.makeText(this, "Alarm set in " + i + " seconds",
                Toast.LENGTH_LONG).show();

    }

    public void getSSIDs(View view)
    {
        IntentFilter i = new IntentFilter();
        i.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);

         registerReceiver(new BroadcastReceiver() {

             @Override
             public void onReceive(Context context, Intent intent) {
                 // TODO Auto-generated method stub
                 Log.i(TAG, "opening wifimanager");
                 WifiManager mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                 mWifiManager.getScanResults();
                 List<ScanResult> scanResults = mWifiManager.getScanResults();
                 for (ScanResult r : scanResults) {
                        Toast.makeText(context,r.SSID,Toast.LENGTH_LONG).show();
                     Log.i(TAG, r.SSID);
                 }
             }
         }
                 , i);
        WifiManager mWifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);

        if (mWifiManager.isWifiEnabled() == false)
        {
            // If wifi disabled then enable it
            Toast.makeText(getApplicationContext(), "wifi is disabled..making it enabled",
                    Toast.LENGTH_LONG).show();

            mWifiManager.setWifiEnabled(true);
        }

        mWifiManager.startScan();
    }

    public void getSSIDs2(View view){
        // Initiate wifi service manager
        mainWifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);

        // Check for wifi is disabled
        if (mainWifi.isWifiEnabled() == false)
        {
            // If wifi disabled then enable it
            Toast.makeText(getApplicationContext(), "wifi is disabled..making it enabled",
                    Toast.LENGTH_LONG).show();

            mainWifi.setWifiEnabled(true);
        }

        // wifi scanned value broadcast receiver
        ListView lv = (ListView)findViewById(R.id.SSIDListView);
        receiverWifi = new WifiReceiver(lv);

        // Register broadcast receiver
        // Broadcast receiver will automatically call when number of wifi connections changed
        registerReceiver(receiverWifi, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        mainWifi.startScan();
    }

    public void getDBFileSys()
    {
        try{
            String destPath  = "/data/" + getPackageName() + "/databases/MyAlarmAppDB";
            File f = new File(destPath);
            if(!f.exists())
            {
                CopyDB( getBaseContext().getAssets().open("mydb"), new FileOutputStream(destPath));
            }
        }catch(FileNotFoundException ex){
            ex.printStackTrace();
        } catch(IOException ex)
        {
            ex.printStackTrace();
        }
    }

    private void CopyDB(InputStream mydb, FileOutputStream fileOutputStream)
            /* Copy 1K bytes at a time */
    throws IOException {
        byte [] buffer  = new byte[1024];
        int length;
        while((length = mydb.read(buffer)) > 0){
            fileOutputStream.write(buffer, 0, length);
        }
        mydb.close();
        fileOutputStream.close();
    }

    public void GetDBData() throws SQLException {
        DBAdapter db = new DBAdapter(getBaseContext());
        db.open();
        Cursor cursor = db.getAllRecords();
        while(cursor.moveToNext())
        {

        }
        //r.SSID, r.BSSID, r.frequency, r.level, time
        db.close();
    }


}
