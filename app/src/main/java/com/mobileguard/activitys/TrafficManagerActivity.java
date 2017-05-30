package com.mobileguard.activitys;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.mobileguard.adapter.TrafficAdapter;
import com.mobileguard.base.BaseActivity;
import com.mobileguard.domain.TrafficInfo;
import com.mobileguard.engine.TrafficData;

import java.util.List;

/**
 * 实现流量的管理
 * Created by chendom on 2017/5/7 0007.
 */

public class TrafficManagerActivity extends BaseActivity {
    private ListView trafficList;
    private TrafficAdapter adapter;
    private List<TrafficInfo> trafficInfos;
    private TrafficData trafficData;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0x123:
                    ((ProgressBar) findViewById(R.id.pbLoad)).setVisibility(View.GONE);
                    adapter = new TrafficAdapter(TrafficManagerActivity.this,trafficInfos);
                    trafficList.setAdapter(adapter);
                    break;
                default:
                    break;
            }
        }
    };
    @Override
    public int getLayout() {
        return R.layout.activity_trafficmanager;
    }

    @Override
    public void initData() {

    }

    @Override
    public void initView() {
        trafficList = (ListView)findViewById(R.id.lvtraffic);
        trafficData = new TrafficData(TrafficManagerActivity.this);
        new Thread(){
            @Override
            public void run() {
                        trafficInfos =  trafficData.getTrafficInfos();
                        System.out.println("-------trafficInfos------"+trafficInfos);
                        handler.sendEmptyMessage(0x123);

            }
        }.start();

       /* adapter = new TrafficAdapter(TrafficManagerActivity.this,trafficInfos);
        trafficList.setAdapter(adapter);*/
    }
    @Override
    public void setListener() {
        trafficList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }
}
