package com.mobileguard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mobileguard.activitys.R;
import com.mobileguard.domain.Call;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Administrator on 2017/4/17 0017.
 */

public class BlackListCallAdapter extends BaseAdapter {
    private Context context;
    private List<Call> calls;
    public BlackListCallAdapter(Context context, List<Call> calls){
    this.context=context;
        this.calls=calls;
    }
    @Override
    public int getCount() {
        return calls.size();
    }

    @Override
    public Object getItem(int position) {
        return calls.get(position);
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
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_blackcall_item_style,null);
            viewHolder.tvNameOrNumber=(TextView)convertView.findViewById(R.id.callnameornumber);
            viewHolder.tvTime = (TextView)convertView.findViewById(R.id.calltime);
            convertView.setTag(viewHolder);
        }else{
        viewHolder=(ViewHolder)convertView.getTag();
        }
        Call call = calls.get(position);
        viewHolder.tvNameOrNumber.setText(call.getName()+"\t"+call.getTelephonenumber());
        viewHolder.tvTime.setText(call.getCallTime()+"");
        return convertView;
    }
    class ViewHolder{
        public TextView tvNameOrNumber;
        public TextView tvTime;
    }
}
