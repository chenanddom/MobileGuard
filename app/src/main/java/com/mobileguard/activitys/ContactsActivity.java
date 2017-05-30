package com.mobileguard.activitys;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mobileguard.adapter.ContactsAdapter;
import com.mobileguard.base.BaseActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 获取联系人的窗体
 * Created by chendom on 2017/4/11 0011.
 */

public class ContactsActivity extends BaseActivity {
    private ListView contactsList;
    private List<HashMap<String, String>> contacts = new ArrayList<>();

    @Override
    public int getLayout() {
        return R.layout.activity_contacts;
    }

    @Override
    public void initData() {
        contacts = getContacts();
    }

    @Override
    public void initView() {
        contactsList = (ListView) findViewById(R.id.contacts);
        contactsList.setAdapter(new ContactsAdapter(ContactsActivity.this, contacts));
    }
//C:\Users\Administrator\Downloads\MobileGuard
    @Override
    public void setListener() {
        contactsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               String name = contacts.get(position).get("name");
                String number = contacts.get(position).get("number");

                Intent intent = new Intent();
                intent.putExtra("name",name);
                intent.putExtra("safenumber", number);
                /*
                 *Activity.RESULT_OK,表示ok,如果没有这句，如果不选返回的话，会出现空指针异常
			     */
                setResult(Activity.RESULT_OK, intent);
                finish();

            }
        });
    }

    private ArrayList<HashMap<String, String>> getContacts() {
        // 首先,从raw_contacts中读取联系人的id("contact_id")
        // 其次, 根据contact_id从data表中查询出相应的电话号码和联系人名称
        // 然后,根据mimetype来区分哪个是联系人,哪个是电话号码
        Uri rawContactsUri = Uri
                .parse("content://com.android.contacts/raw_contacts");
        Uri dataUri = Uri.parse("content://com.android.contacts/data");

        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

        // 从raw_contacts中读取联系人的id("contact_id")
        Cursor rawContactsCursor = getContentResolver().query(rawContactsUri,
                new String[]{"contact_id"}, null, null, null);
        if (rawContactsCursor != null) {
            while (rawContactsCursor.moveToNext()) {
                String contactId = rawContactsCursor.getString(0);
                // System.out.println(contactId);

                // 根据contact_id从data表中查询出相应的电话号码和联系人名称, 实际上查询的是视图view_data
                Cursor dataCursor = getContentResolver().query(dataUri,
                        new String[]{"data1", "mimetype"}, "contact_id=?",
                        new String[]{contactId}, null);

                if (dataCursor != null) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    while (dataCursor.moveToNext()) {
                        String data1 = dataCursor.getString(0);
                        String mimetype = dataCursor.getString(1);
                        // System.out.println(contactId + ";" + data1 + ";"
                        // + mimetype);
                        if ("vnd.android.cursor.item/phone_v2".equals(mimetype)) {
                            map.put("number", data1);
                        } else if ("vnd.android.cursor.item/name"
                                .equals(mimetype)) {
                            map.put("name", data1);
                        }
                    }

                    list.add(map);
                    dataCursor.close();
                }
            }

            rawContactsCursor.close();
        }

        return list;
    }

}
