package com.mobileguard.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.mobileguard.activitys.R;
import com.mobileguard.domain.Contact;

import java.util.List;

/**
 * 关于黑名单的适配器
 * Created by chendom on 2017/4/14 0014.
 */

public class BlackListAdapter extends BaseAdapter {
    private Context context;
    private List<Contact> contactList;

    public BlackListAdapter(Context context, List<Contact> contactList) {
        this.context = context;
        this.contactList = contactList;
    }

    @Override
    public int getCount() {
        return contactList.size();
    }

    @Override
    public Object getItem(int position) {
        return contactList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (viewHolder == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_blackcontacts_item_style, null);
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.black_contact_name);
            viewHolder.tvNumber = (TextView) convertView.findViewById(R.id.black_contact_number);
            viewHolder.cbIsChecked = (CheckBox)convertView.findViewById(R.id.isChecked);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Contact contact = contactList.get(position);
        Log.i("TAG","-----------------------------contact--------------------"+contact.toString());
        viewHolder.tvName.setText(contact.getName());
        viewHolder.tvNumber.setText(contact.getTelephonenumber());
        return convertView;
    }

    class ViewHolder {
        public TextView tvName;
        public TextView tvNumber;
        public CheckBox cbIsChecked;
    }
}
