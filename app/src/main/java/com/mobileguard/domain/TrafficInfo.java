package com.mobileguard.domain;



import android.graphics.drawable.Drawable;
/**
 * 存储需要使用数据流量的应用的对象的类
 * Created by chendom on 2017/5/7 0007.
 */
public class TrafficInfo {
    private Drawable icon;//应用的图标
    private String appName;//应用名称
    private String packageName;//包名
    private long down;//下载的流量
    private long send;//上传的流量
    public Drawable getIcon() {
        return icon;
    }
    public void setIcon(Drawable icon) {
        this.icon = icon;
    }
    public String getAppName() {
        return appName;
    }
    public void setAppName(String appName) {
        this.appName = appName;
    }
    public String getPackageName() {
        return packageName;
    }
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
    public long getDown() {
        return down;
    }
    public void setDown(long down) {
        this.down = down;
    }
    public long getSend() {
        return send;
    }
    public void setSend(long send) {
        this.send = send;
    }
    @Override
    public String toString() {
        return "TrafficInfo [icon=" + icon + ", appName=" + appName
                + ", packageName=" + packageName + ", down=" + down + ", send="
                + send + "]";
    }

}

