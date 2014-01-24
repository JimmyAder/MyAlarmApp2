package com.example.myalarmapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.BroadcastReceiver;
import android.net.wifi.WifiManager;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import android.net.wifi.ScanResult;
import android.text.format.Time;
import 	android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.app.Application;
import android.widget.Toast;

/**
 * Created by Jimmy.Ader on 1/18/14.
 */
class WifiReceiver extends BroadcastReceiver{

    public ListView listView;

    public WifiReceiver(ListView myListView){
        listView = myListView;
    }

    // This method call when number of wifi connections changed
    public void onReceive(Context c, Intent intent) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String time = sdf.format(date);


        WifiManager mWifiManager = (WifiManager) c.getSystemService(Context.WIFI_SERVICE);
        DBAdapter db = new DBAdapter(listView.getContext());

        /*
        ArrayList<String> arrayList = new ArrayList<String>();
        for(ScanResult r : mWifiManager.getScanResults())
        {
            arrayList.add(r.SSID);


        }
            ArrayAdapter adapter = new ArrayAdapter<String>(listView.getContext(),
                    android.R.layout.simple_list_item_1, arrayList);
            listView.setAdapter(adapter);
        */
        ArrayList<WifiItem> arrayList = new ArrayList<WifiItem>();
        for(ScanResult r : mWifiManager.getScanResults())
        {
            WifiItem wifiItem = new WifiItem(r.SSID, r.BSSID, r.frequency, r.level);
            try {
                db.open();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            db.insertRecord(r.SSID, r.BSSID, r.frequency, r.level, time);
            db.close();
            arrayList.add(wifiItem);
        }

        WifiAdapter wifiAdapter = new WifiAdapter(listView.getContext(), R.layout.layout_wifiitem, arrayList);
        listView.setAdapter(wifiAdapter);
    }

}
