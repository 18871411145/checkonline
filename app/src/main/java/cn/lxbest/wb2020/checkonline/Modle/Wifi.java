package cn.lxbest.wb2020.checkonline.Modle;

import android.net.wifi.ScanResult;

/**wifi信息类*/
public class Wifi {
   public String ssid;//WiFi名称

    public boolean ifON;//是否被选中
    public Wifi(ScanResult sr) {
        ssid=sr.SSID;
    }
}
