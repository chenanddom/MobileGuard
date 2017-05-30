package com.mobileguard.engine;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import com.mobileguard.domain.AppInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**获取App的信息，并且将信息设置到域对象里面
 * Created by chendom on 2017/4/17 0017.
 */
public class AppInfos {
    public static List<AppInfo> getAppInfos(Context context){
        List<AppInfo> packAppInfos = new ArrayList<AppInfo>();
        //获取到包的管理者
        PackageManager packageManager = context.getPackageManager();
        //获取设备上所有安装包的信息
        List<PackageInfo> installPackages =packageManager.getInstalledPackages(0);//参数设置成零即可
        for(PackageInfo installPackage:installPackages){
            AppInfo appInfo = new AppInfo();
            //获取程序的图标
            Drawable drawable = installPackage.applicationInfo.loadIcon(packageManager);
            appInfo.setIcon(drawable);
            //得到apk的名字
            String apkName = installPackage.applicationInfo.loadLabel(packageManager).toString();
            appInfo.setApkName(apkName);
            //获取应用程序的包名
            String packageName = installPackage.packageName;
            appInfo.setApkPackageName(packageName);

            //获取到apk资源的路径
            String sourceDir = installPackage.applicationInfo.sourceDir;

            //获取apk的大小
            File file = new File(sourceDir);
            long apkSize = file.length();
            appInfo.setApkSize(apkSize);
            //获取应用程序的标志
            int flags = installPackage.applicationInfo.flags;
            if((flags & ApplicationInfo.FLAG_SYSTEM)!=0){
                //表示系统app
                appInfo.setUserApp(false);
            }else{
                //用户app
                appInfo.setUserApp(true);
            }

            if((flags&ApplicationInfo.FLAG_EXTERNAL_STORAGE)!=0){
                //表示在手机内存
                appInfo.setRom(false);
            }else{
                appInfo.setRom(true);
            }
            packAppInfos.add(appInfo);
        }
        return packAppInfos;
    }
}
