package com.mobileguard.adapter;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobileguard.activitys.R;
import com.mobileguard.domain.TrafficInfo;

import java.text.Format;
import java.util.Formatter;
import java.util.List;

/**
 * 查看流量的适配器
 * Created by chendom on 2017/5/7 0007.
 */

public class TrafficAdapter extends BaseAdapter {
    private Context context;
    private List<TrafficInfo> trafficInfos;
    public TrafficAdapter(Context context, List<TrafficInfo> trafficInfos){
        this.context=context;
        this.trafficInfos=trafficInfos;
    }
    @Override
    public int getCount() {
        return trafficInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return trafficInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (viewHolder==null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_traffic_item_style,null);
            viewHolder.appIcon = (ImageView)convertView.findViewById(R.id.appicon);
            viewHolder.appName = (TextView)convertView.findViewById(R.id.appName);
            viewHolder.packageName = (TextView)convertView.findViewById(R.id.packageName);
            viewHolder.dataTraffic = (TextView)convertView.findViewById(R.id.datatraffic);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        TrafficInfo trafficInfo = trafficInfos.get(position);
        viewHolder.appIcon.setImageDrawable(trafficInfo.getIcon());
        viewHolder.appName.setText(trafficInfo.getAppName());
        viewHolder.packageName.setText(trafficInfo.getPackageName());
        viewHolder.dataTraffic.setText(android.text.format.Formatter.formatFileSize(context,(trafficInfo.getDown()+trafficInfo.getSend())));
        return convertView;
    }
    class ViewHolder{
        public ImageView appIcon;
        public TextView appName;
        public TextView packageName;
        public TextView dataTraffic;
    }
}
