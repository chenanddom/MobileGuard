package com.mobileguard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobileguard.activitys.R;
import com.mobileguard.domain.AppInfo;
import com.mobileguard.domain.Contact;

import android.text.format.Formatter;
import java.util.List;

/**
 * 软件管理的适配器
 * Created by chendom on 2017/4/17 0017.
 */

public class SoftwareAdapter extends BaseAdapter {
    private Context context;
    private List<AppInfo> infos;

    public SoftwareAdapter(Context context, List<AppInfo> infos) {
        this.context = context;
        this.infos = infos;
    }

    @Override
    public int getCount() {
        return infos.size();
    }
    @Override
    public Object getItem(int position) {
        return infos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    ViewHolder viewHolder=null;
        if(viewHolder==null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_softwaremanager_item_style,null);
            viewHolder.appIcon=(ImageView)convertView.findViewById(R.id.iv_icon);
            viewHolder.appName = (TextView)convertView.findViewById(R.id.tv_name);
            viewHolder.appSize = (TextView)convertView.findViewById(R.id.tv_apk_size);
            viewHolder.appLocation = (TextView)convertView.findViewById(R.id.tv_location);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        AppInfo appInfo = infos.get(position);
        viewHolder.appIcon.setImageDrawable(appInfo.getIcon());
        viewHolder.appName.setText(appInfo.getApkName());
        viewHolder.appSize.setText(Formatter.formatFileSize(context, appInfo.getApkSize()));
        if(appInfo.isRom()){
            viewHolder.appLocation.setText("手机内存");
        }else{
            viewHolder.appLocation.setText("外村设备中");
        }
        return convertView;
    }
    class ViewHolder{
        public ImageView appIcon;
        public TextView appName;
        public TextView appSize;
        public TextView appLocation;
    }
}
