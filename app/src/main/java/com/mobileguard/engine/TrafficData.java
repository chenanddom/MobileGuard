package com.mobileguard.engine;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.TrafficStats;
import android.os.Build;

import com.mobileguard.domain.TrafficInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 获取需要数据流量的应用使用的数据流量的情况
 * Created by chendom on 2017/5/7 0007.
 */
public class TrafficData {
    private Context context;
    private PackageManager packageManager;
    private List<PackageInfo> packageInfos;
    public List<TrafficInfo> trafficInfos;//需要的流量的程序的信息

    public TrafficData(Context context) {
        // TODO Auto-generated constructor stub
        this.context = context;
        packageManager = context.getPackageManager();
        trafficInfos = new ArrayList<TrafficInfo>();
    }

    /**
     * 返回需要流量的的程序的数据信息
     *
     * @return
     */
    public List<TrafficInfo> getTrafficInfos() {
//获取得到程序的权限的配置信息
        packageInfos = packageManager.getInstalledPackages(PackageManager.GET_PERMISSIONS);

        for (PackageInfo packageInfo : packageInfos) {
            String[] permissions = packageInfo.requestedPermissions;
            if (permissions != null && permissions.length > 0) {
                for (String permission : permissions) {
                    if (permission.equals("android.permission.INTERNET")) {
                        TrafficInfo trafficInfo = new TrafficInfo();
                        trafficInfo.setIcon(packageInfo.applicationInfo.loadIcon(packageManager));//设置程序的图标
                        trafficInfo.setAppName(packageInfo.applicationInfo.loadLabel(packageManager).toString());
                        trafficInfo.setPackageName(packageInfo.packageName);
                        //得到应用程序的UID
                        int uid = packageInfo.applicationInfo.uid;
                        TrafficStats.getTotalRxBytes();
                        trafficInfo.setDown(TrafficStats.getUidRxBytes(uid));//封装程序下载的数据流量
                        trafficInfo.setSend(TrafficStats.getUidTxBytes(uid));//封装程序上传的数据流量
                        trafficInfos.add(trafficInfo);
                        trafficInfo = null;
                        break;
                    }
                }
            }
        }
        return trafficInfos;
    }

}