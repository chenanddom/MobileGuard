package com.mobileguard.activitys;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.format.Formatter;
import android.view.Gravity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mobileguard.adapter.SoftwareAdapter;
import com.mobileguard.base.BaseActivity;
import com.mobileguard.domain.AppInfo;
import com.mobileguard.engine.AppInfos;
import com.mobileguard.views.CustomProgressBar;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * 实现软件管理的类
 * Created by chendom on 2017/4/17 0017.
 */

public class SoftwareManagermentActivity extends BaseActivity implements View.OnClickListener{
    private ProgressBar loadProgressBar;
    private List<AppInfo> appInfos;
    private ListView lvAppList;
    private PopupWindow popupWindow;
    private AppInfo appInfo;
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            SoftwareAdapter adapter = new SoftwareAdapter(SoftwareManagermentActivity.this,appInfos);
            lvAppList.setAdapter(adapter);
            loadProgressBar.setVisibility(View.GONE);
        }
    };

    @Override
    public int getLayout() {
        return R.layout.activity_managerment;
    }

    @Override
    public void initData() {

    }

    @Override
    public void initView() {
        lvAppList=(ListView)findViewById(R.id.softwarelist);
        loadProgressBar = (ProgressBar) findViewById(R.id.loadpregress);
    //获取rom内存的运行大小
        long rom_freeSpace = Environment.getDataDirectory().getFreeSpace();
        //获取sd卡的大小
        long sd_freeSpace = Environment.getExternalStorageDirectory().getFreeSpace();
        //将获取到的数据显示到相应的位置
        ((TextView)findViewById(R.id.tv_rom)).setText("内存可用空间:"+ Formatter.formatFileSize(this, rom_freeSpace));
        ((TextView)findViewById(R.id.tv_sdcard)).setText("sd卡可用空间:"+Formatter.formatFileSize(this,sd_freeSpace));
        getAppData();
    }
    public void getAppData(){
        new Thread() {
            public void run() {
                appInfos = AppInfos.getAppInfos(SoftwareManagermentActivity.this);
                handler.sendEmptyMessage(0x123);
            }
        }.start();
    }

    @Override
    public void setListener() {
        lvAppList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//获取到当前点击的item对象
                Object obj = lvAppList.getItemAtPosition(position);

                if(obj!=null && obj instanceof AppInfo){
                    appInfo = (AppInfo)obj;//将对象强转
                    View contentView =
                            View.inflate(SoftwareManagermentActivity.this, R.layout.layout_poup_item, null);
                    LinearLayout lv_uninstall = (LinearLayout)contentView.findViewById(R.id.uninstall);
                    LinearLayout lv_run = (LinearLayout)contentView.findViewById(R.id.run);
                    LinearLayout lv_share = (LinearLayout)contentView.findViewById(R.id.share);
                    lv_uninstall.setOnClickListener(SoftwareManagermentActivity.this);
                    lv_run.setOnClickListener(SoftwareManagermentActivity.this);
                    lv_share.setOnClickListener(SoftwareManagermentActivity.this);
                    popupWindowDismiss();
                    popupWindow = new PopupWindow(contentView,-2,-2);
                    int[] location = new int[2]; //表示x和y
                    //获取view展示的位置
                    view.getLocationInWindow(location);
                    popupWindow.showAtLocation(parent, Gravity.LEFT+Gravity.TOP, 80, location[1]);


                }
            }
        });
        lvAppList.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                // TODO Auto-generated method stub
                popupWindowDismiss();

            }
        });
    }

    @Override
    public void onClick(View v) {
// TODO Auto-generated method stub
        switch(v.getId()){
            //卸载
            case R.id.uninstall:
                Intent uninstall_localIntent = new Intent("android.intent.action.DELETE", Uri.parse("package:" + appInfo.getApkPackageName()));
                startActivity(uninstall_localIntent);
                popupWindowDismiss();
                break;
            //运行
            case R.id.run:
                Intent start_localIntent = this.getPackageManager().getLaunchIntentForPackage(appInfo.getApkPackageName());
                this.startActivity(start_localIntent);
                popupWindowDismiss();
                break;
            //分享
            case R.id.share:
                Intent share_localIntent = new Intent("android.intent.action.SEND");
                share_localIntent.setType("text/plain");
                share_localIntent.putExtra("android.intent.extra.SUBJECT", "分享");
                share_localIntent.putExtra("android.intent.extra.TEXT",
                        "Hi！推荐您使用软件：" + appInfo.getApkName()+"下载地址:"+"https://play.google.com/store/apps/details?id="+appInfo.getApkPackageName());
                this.startActivity(Intent.createChooser(share_localIntent, "分享"));
                popupWindowDismiss();
                break;
        }
    }
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        popupWindowDismiss();
    }
    private void popupWindowDismiss() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            popupWindow = null;
        }
    }
}
