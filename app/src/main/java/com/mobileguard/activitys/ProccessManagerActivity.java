package com.mobileguard.activitys;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mobileguard.adapter.ProccessAdapter;
import com.mobileguard.base.BaseActivity;
import com.mobileguard.domain.TaskInfo;
import com.mobileguard.domain.TaskInfoParser;


import java.util.List;
/**
 * 进程管理的窗口
 * Created by chendom on 2017/4/18 0018.
 */

public class ProccessManagerActivity extends BaseActivity {
    private ActivityManager activityManager;
    private List<ActivityManager.RunningAppProcessInfo> runningProccesses;
    private int runnigSize;
    private long mavailableMemory;
    private long mtotalMemory;
    private ActivityManager.MemoryInfo memoryInfo;
    private ListView lvRunningProccesses;
    private List<TaskInfo> taskInfos;
    private ProccessAdapter adapter;
    private int[] flag;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what){
                case 0x123:
                    adapter = new ProccessAdapter(ProccessManagerActivity.this,taskInfos);
                    lvRunningProccesses.setAdapter(adapter);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public int getLayout() {
        return R.layout.activity_proccessmanager;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void initData() {
        activityManager = (ActivityManager)getSystemService(ACTIVITY_SERVICE);
        runningProccesses = activityManager.getRunningAppProcesses();
        memoryInfo = new ActivityManager.MemoryInfo();
        basementData();
    }

    @Override
    public void initView() {
        lvRunningProccesses=(ListView)findViewById(R.id.runningProccesses);
        ((TextView)findViewById(R.id.tv_task_process_count)).setText((getResources().getText(R.string.appruning).toString())+runnigSize+"个");
        ((TextView)findViewById(R.id.tv_task_memory)).setText((getResources().getText(R.string.memoryleftandused).toString())+ Formatter.formatFileSize(ProccessManagerActivity.this, this.mavailableMemory) + "/"
                + Formatter.formatFileSize(ProccessManagerActivity.this, this.mtotalMemory));
        getRunningProccesses();
    }

    @Override
    public void setListener() {
        lvRunningProccesses.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
              /*  CheckBox checkBox =  (CheckBox)view.findViewById(R.id.isSelected);
                checkBox.setVisibility(View.VISIBLE);
                checkBox.setChecked(true);
                flag[position]=1;*/
                mshowDialog(position);
                return false;
            }
        });
        ((ImageView)findViewById(R.id.clearProccess)).setVisibility(View.GONE);
       /* ((ImageView)findViewById(R.id.clearProccess)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    int length = taskInfos.size();
                    for (int i=0;i<length;i++){
                        if (flag[i]==1){
                       TaskInfo taskInfo =  taskInfos.get(i);
                           *//* mavailableMemory += taskInfo.getMemorySize();*//*
                            activityManager.killBackgroundProcesses(taskInfo
                                    .getPackageName());
                        }
                    }
                basementData();
                ((TextView)findViewById(R.id.tv_task_process_count)).setText((getResources().getText(R.string.appruning).toString())+runnigSize+"个");
                ((TextView)findViewById(R.id.tv_task_memory)).setText((getResources().getText(R.string.memoryleftandused).toString())+ Formatter.formatFileSize(ProccessManagerActivity.this, mavailableMemory) + "/"
                        + Formatter.formatFileSize(ProccessManagerActivity.this, mtotalMemory));
                for (int i=0;i<length;i++){
                if (flag[i]==1){
                    TaskInfo taskInfo =  taskInfos.get(i);
                    activityManager.killBackgroundProcesses(taskInfo
                            .getPackageName());
                }
            }
                for (int i=0;i<length;i++){
                    if (flag[i]==1){
                        taskInfos.remove(i);

                        adapter.notifyDataSetChanged();
                    }
                }

        }
        });*/

    }
    public void basementData(){
        if(runningProccesses.size()>0){
            runnigSize = runningProccesses.size();
        }else{
            Log.i("tips","没有在运行的程序");
        }
        activityManager.getMemoryInfo(memoryInfo);
        this.mavailableMemory = memoryInfo.availMem;
        this.mtotalMemory = this.memoryInfo.totalMem;//高版本才可以使用，最低是16
    }
    public void getRunningProccesses(){
        new Thread() {
            public void run() {
                taskInfos = TaskInfoParser.getTaskInfos(ProccessManagerActivity.this);
                if(taskInfos.size()>0){
                    flag=new int[taskInfos.size()];
                    handler.sendEmptyMessage(0x123);

                }


            };
        }.start();
    }
    public void mshowDialog(final int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(ProccessManagerActivity.this);
        builder.setTitle("提示");
        builder.setMessage("确定删除？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                TaskInfo taskInfo =  taskInfos.get(position);
                activityManager.killBackgroundProcesses(taskInfo
                        .getPackageName());
                taskInfos.remove(position);
                adapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
}
