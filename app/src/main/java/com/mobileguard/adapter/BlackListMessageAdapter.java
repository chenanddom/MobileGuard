package com.mobileguard.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mobileguard.activitys.R;
import com.mobileguard.domain.Call;
import com.mobileguard.domain.Message;

import java.util.List;

/**
 * Created by Administrator on 2017/4/17 0017.
 */

public class BlackListMessageAdapter extends BaseAdapter {
    private Context context;
    private List<Message> messages;
    public BlackListMessageAdapter(Context context, List<Message> messages){
    this.context=context;
        this.messages=messages;
    }
    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Object getItem(int position) {
        return messages.get(position);
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
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_blackmessage_item_style,null);
            viewHolder.sendTime = (TextView)convertView.findViewById(R.id.sendTime);
            viewHolder.view = (View)convertView.findViewById(R.id.devision);
            viewHolder.tvNameOrNumber=(TextView)convertView.findViewById(R.id.blackmesagename);
            viewHolder.partContent = (TextView)convertView.findViewById(R.id.partcontent);
            convertView.setTag(viewHolder);
        }else{
        viewHolder=(ViewHolder)convertView.getTag();
        }
        Message message = messages.get(position);
        viewHolder.sendTime.setText(message.getSendTime());
        viewHolder.view.setBackgroundColor(Color.GRAY);
        viewHolder.tvNameOrNumber.setText(message.getName()+"\t"+message.getTelephonenumber());
        viewHolder.partContent.setText(message.getMessagecontent());
        /*Message message = calls.get(position);
        viewHolder.tvNameOrNumber.setText(call.getName()+"\t"+call.getTelephonenumber());
        viewHolder.tvTime.setText(call.getCallTime()+"");*/
        return convertView;
    }
    class ViewHolder{
        public TextView sendTime;
        public View view;
        public TextView tvNameOrNumber;
        public TextView partContent;
    }
}
