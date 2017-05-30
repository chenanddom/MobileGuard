package com.mobileguard.activitys;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.mobileguard.base.BaseActivity;
import com.mobileguard.domain.TaskInfo;
import com.mobileguard.service.AntivirusService;
import com.mobileguard.service.impl.ContactsServiceImpl;
import com.mobileguard.utils.MD5Utils;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * 实现杀毒的功能
 * Created by chendom on 2017/4/19 0019.
 */

public class AntivirusActivity extends BaseActivity {
    private ImageView imageview;
    private List<TaskInfo> taskInfos;
    private Message message;
    private int size;
    private ArrayList<Info> list = new ArrayList<Info>();
    private TextView tv_init_virus;
    private ProgressBar progressBar;
    private LinearLayout layoutContent;
    private AntivirusService antivirusService;
    private int flag = 0;
    private String waittips;
    private  int progress = 0;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            Info info = (Info) msg.obj;

            switch (msg.what) {
                case 0://初始化
                    tv_init_virus.setText("初始化v8引擎");

                    break;
                case 1://病毒扫描中
                    progressBar.setProgress(progress);
                    LinearLayout layout = (LinearLayout) LayoutInflater.from(AntivirusActivity.this).inflate(R.layout.layout_antivirus_item_style, null);
                    TextView appName = (TextView) layout.findViewById(R.id.checkappname);
                    TextView checkResult = (TextView) layout.findViewById(R.id.checkresult);
                    if (info.desc) {//表示有病毒
                        appName.setText(info.appName);
                        checkResult.setTextColor(Color.RED);
                        checkResult.setText("有毒");
                        layoutContent.addView(layout);
                        ;
                    } else {//表示无病毒
                        appName.setText(info.appName);
                        checkResult.setText("正常");
                        layoutContent.addView(layout);
                    }
                    flag++;
                    if (flag > 3) {
                        flag = 0;
                        waittips = "";
                    } else {
                        waittips += "." + " ";
                        tv_init_virus.setText("病毒查杀中,请耐心等待" + waittips);

                    }

                    break;
                case 2:
                    tv_init_virus.setText("查杀完毕");
                    //listview.setAdapter(new MyAdapter());
                    //扫描结束就结束动画
                    imageview.clearAnimation();
                    break;

                default:
                    break;
            }
        }

        ;
    };
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    public int getLayout() {
        return R.layout.activity_antivirus;
    }

    @Override
    public void initData() {
        antivirusService = new ContactsServiceImpl(AntivirusActivity.this);
    }

    @Override
    public void initView() {
        imageview = (ImageView) findViewById(R.id.iv_scanning);
        tv_init_virus = (TextView) findViewById(R.id.tv_init_virus);
        progressBar = (ProgressBar) findViewById(R.id.progressBar1);
        layoutContent = (LinearLayout) findViewById(R.id.ll_content);
        // listview=(ListView)findViewById(R.id.lv_content);
        /**
         * 初始化动画
         *
         * @param1开始角度
         * @param2结束角度
         * @param3表示参照自己
         * @param4在自己的0.5的位置
         * @param5表示参照自己
         * @param6在自己的0.5的位置
         */
        RotateAnimation animation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(2000);//设置动画的循环时间
        //设置动画无线循环
        animation.setRepeatCount(Animation.INFINITE);
        imageview.startAnimation(animation);
        getData();
    }

    public void getData() {
        new Thread() {


            public void run() {
                message = Message.obtain();
                message.what = 0;

                PackageManager packageManager = getPackageManager();
                //获取到所有已经安装的应用程序
                List<PackageInfo> instaPackageInfos = packageManager.getInstalledPackages(0);
                size = instaPackageInfos.size();
                progressBar.setMax(size);

                for (PackageInfo packageInfo : instaPackageInfos) {
                    Info info = new Info();
                    //得到应用程序的名字
                    info.appName = packageInfo.applicationInfo.loadLabel(packageManager).toString();
                    //首先需要获取每一个程序的目录
                    String sourceDir = packageInfo.applicationInfo.sourceDir;
                    System.out.println("--------sourceDir-------" + sourceDir);
                    info.packageName = packageInfo.applicationInfo.packageName;
                    //获取应用程序的特征码
                    String md5Code = MD5Utils.encode(sourceDir);
                    System.out.println("--------md5Code-------" + md5Code);
                    String desc = null;
                    try {
                        desc = antivirusService.checkFileVirus(md5Code);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //如果当前的描述信息为空，那么就没有病毒
                    if (desc == null) {
                        info.desc = false;
                    } else {
                        info.desc = true;
                    }
                    progress++;

                    list.add(info);
                    message = Message.obtain();
                    message.what = 1;//进行中
                    message.obj = info;
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    handler.sendMessageDelayed(message, 0);
                }
                message = Message.obtain();
                message.what = 2;//结束
                handler.sendMessageDelayed(message, 0);

            }

            ;
        }.start();
    }

    @Override
    public void setListener() {

    }

    public static class Info {
        boolean desc;
        String appName;
        String packageName;
    }
}
