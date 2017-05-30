package com.mobileguard.domain;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import com.mobileguard.activitys.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 获取进程的信息
 * Created by chendom on 2017/5/12 0012.
 */

public class TaskInfoParser {
    public static List<TaskInfo> getTaskInfos(Context context) {
        List<TaskInfo> list = new ArrayList<TaskInfo>();

        //获取包管理器
        PackageManager packageManager = context.getPackageManager();
        //获取进程管理器
        ActivityManager activityManager = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
        //获取到手机上面所有运行的进程
        List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfo = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo2 : runningAppProcessInfo) {
            TaskInfo taskInfo = new TaskInfo();
            //获取到进程的名字
            String processName = runningAppProcessInfo2.processName;
            taskInfo.setPackageName(processName);

            try {

                // 获取到内存基本信息
                /**
                 * 这个里面一共只有一个数据
                 */
                android.os.Debug.MemoryInfo[] memoryInfo = activityManager.getProcessMemoryInfo(new int[]{runningAppProcessInfo2.pid});
                // Dirty弄脏
                // 获取到总共弄脏多少内存(当前应用程序占用多少内存)
                int totalPrivateDirty = memoryInfo[0].getTotalPrivateDirty() * 1024;


//				System.out.println("==========="+totalPrivateDirty);

                taskInfo.setMemorySize(totalPrivateDirty);

                //获取到包的信息
                PackageInfo packageInfo = packageManager.getPackageInfo(processName, 0);
                //获取到图标
                Drawable icon = packageInfo.applicationInfo.loadIcon(packageManager);
                taskInfo.setIcon(icon);
                //获取到进程的名称
                String appName = packageInfo.applicationInfo.loadLabel(packageManager).toString();
                //获取应用的标志
                int flag = packageInfo.applicationInfo.flags;
                if ((flag & ApplicationInfo.FLAG_SYSTEM) != 0) {
                    //系统默认程序
                    taskInfo.setUserApp(false);
                } else {
                    //用户程序
                    taskInfo.setUserApp(true);
                }
                taskInfo.setAppName(appName);

            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
                //有些系统的核心库里面没有图标，只能自定义一个图标给他
                taskInfo.setIcon(context.getResources().getDrawable(R.mipmap.ic_launcher));
            }
            list.add(taskInfo);

        }

        return list;
    }
}
