package com.mobileguard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobileguard.activitys.R;

import java.util.List;

/**
 * 主页的九宫格的适配器
 * Created by chendom on 2017/4/8 0008.
 */

public class GridViewAdapter extends BaseAdapter {
    private Context context;
    private List<String> name;
    private int[] image;

    public GridViewAdapter(Context context,  List<String>  name, int[] image) {
        this.context = context;
        this.name = name;
        this.image = image;
    }

    @Override
    public int getCount() {
        return name.size();
    }

    @Override
    public Object getItem(int position) {
        return name.get(position);
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
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_item_style,null);
            viewHolder.icon=(ImageView)convertView.findViewById(R.id.icom_item);
            viewHolder.info=(TextView)convertView.findViewById(R.id.name_item);
            convertView.setTag(viewHolder);

        }else{
            viewHolder=(ViewHolder)convertView.getTag();
        }
        viewHolder.icon.setImageResource(image[position]);
        viewHolder.info.setText(name.get(position));
        return convertView;
    }
    class ViewHolder{
        ImageView icon;
        TextView info;
    }
}
