package com.example.myalarmapp;

/**
 * Created by Jimmy.Ader on 1/22/14.
 */
public class WifiItem {
    private String SSID;
    private String BSSID;
    private int frequency;
    private int level;

    public WifiItem(String SSID, String BSSID, int frequency, int level){
        this.SSID = SSID;
        this.BSSID = BSSID;
        this.frequency = frequency;
        this.level = level;
    }

    public String getSSID(){
        return SSID;
    }

    public void setSSID(String SSID){
        this.SSID = SSID;
    }

    public String getBSSID(){
        return SSID;
    }

    public void setBSSID(String SSID){
        this.SSID = SSID;
    }

    public int getFrequency(){
        return frequency;
    }

    public void setFrequency(int frequency){
        this.frequency = frequency;
    }

    public int getLevel(){
        return level;
    }

    public void setLevel(){
        this.level = level;
    }




}
