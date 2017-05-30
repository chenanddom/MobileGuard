package com.mobileguard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobileguard.activitys.R;
import com.mobileguard.domain.AppInfo;
import com.mobileguard.domain.TaskInfo;

import java.util.List;

/**
 * 进程管理适配器
 * Created by chendom on 2017/5/9 0009.
 */

public class ProccessAdapter extends BaseAdapter{

    private List<TaskInfo>  taskInfos;
    private Context context;

    public ProccessAdapter(Context context,List<TaskInfo> taskInfos){
        this.taskInfos = taskInfos;
        this.context = context;
    }

    @Override
    public int getCount() {
        return taskInfos.size();
    }

    @Override
    public TaskInfo getItem(int position) {
        return this.taskInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.layout_proccess_item_style,null);
            viewHolder.proName = (TextView) convertView.findViewById(R.id.myText);
            viewHolder.proIcon = (ImageView) convertView.findViewById(R.id.icon_img);
            viewHolder.cbIsSelected=(CheckBox)convertView.findViewById(R.id.isSelected);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
        TaskInfo appInfo = getItem(position);
        if(appInfo != null){
            viewHolder.proName.setText(appInfo.getAppName());
            viewHolder.proIcon.setImageDrawable(appInfo.getIcon());
        }
        return convertView;
    }

    private static class ViewHolder{
        TextView proName;
        ImageView proIcon;
        CheckBox cbIsSelected;
    }
}