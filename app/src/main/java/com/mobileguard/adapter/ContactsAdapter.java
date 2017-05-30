package com.mobileguard.adapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.mobileguard.activitys.R;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;

/**
 * 实现联系人列表的适配
 * Created by chendom on 2017/4/11 0011.
 */

public class ContactsAdapter extends BaseAdapter {
    private Context context;
    private List<HashMap<String, String>> contacts;

    public ContactsAdapter(Context context, List<HashMap<String, String>> contacts) {
        this.context = context;
        this.contacts = contacts;
    }

    @Override
    public int getCount() {
        return contacts.size();
    }

    @Override
    public Object getItem(int position) {
        return contacts.get(position);
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
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_contacts_item_style, null);
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.contact_name);
            viewHolder.tvNumber = (TextView) convertView.findViewById(R.id.contact_numbers);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        HashMap<String, String> hs = contacts.get(position);
        viewHolder.tvName.setText(hs.get("name"));
        viewHolder.tvNumber.setText(hs.get("number"));


        return convertView;
    }

    class ViewHolder {
        TextView tvName;
        TextView tvNumber;

    }

}
