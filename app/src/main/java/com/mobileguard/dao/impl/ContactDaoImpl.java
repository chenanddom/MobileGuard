package com.mobileguard.dao.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mobileguard.dao.AntivirusDao;
import com.mobileguard.dao.CallDao;
import com.mobileguard.dao.ContactDao;
import com.mobileguard.dao.MessageDao;
import com.mobileguard.db.BlackListHelper;
import com.mobileguard.domain.Call;
import com.mobileguard.domain.Contact;
import com.mobileguard.domain.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * 与数据库交互的dao层
 * Created by chendom on 2017/4/14 0014.
 */

public class ContactDaoImpl implements ContactDao ,CallDao,MessageDao,AntivirusDao{
    private BlackListHelper helper;
    private SQLiteDatabase database;
    private Context context;

    public ContactDaoImpl(Context context) {
        this.context=context;
        helper = new BlackListHelper(context);
    }

    ;

    @Override
    public boolean addData(Contact contact) throws Exception {
        database = helper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("name", contact.getName());
        cv.put("telephonenumber", contact.getTelephonenumber());
        long rowId = database.insert("list", null, cv);
        if (rowId >= 1) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean deleteData(int _id) throws Exception {
        database = helper.getWritableDatabase();
        int flag = database.delete("list", "_id=?", new String[]{String.valueOf(_id)});
        if (flag > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean deleteDataByTag(Contact contact) throws Exception {
        database = helper.getWritableDatabase();
        int flag = database.delete("list", "name=? and telephonenumber=?", new String[]{contact.getName(), contact.getTelephonenumber()});
        if (flag > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean updateData(Contact contact) throws Exception {
        return false;
    }

    @Override
    public List<Contact> findAllData() throws Exception {
        database = helper.getReadableDatabase();
        Cursor cursor = database.rawQuery("select *from list", null);
        List<Contact> list = new ArrayList<Contact>();
        while (cursor.moveToNext()) {
            Contact contact = new Contact();
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String telephone = cursor.getString(2);
            contact.set_id(id);
            contact.setName(name);
            contact.setTelephonenumber(telephone);
            list.add(contact);
        }

        return list;
    }

    @Override
    public Contact findDataById(int _id) throws Exception {
        return null;
    }

    @Override
    public Contact findDataByName(String number) throws Exception {
        database = helper.getReadableDatabase();
        Cursor cursor = database.rawQuery("select *from list where telephonenumber=?", new String[]{number});
        Contact contact = null;
        while (cursor.moveToNext()) {
            contact = new Contact();
            contact.set_id(cursor.getInt(0));
            contact.setName(cursor.getString(1));
            contact.setTelephonenumber(cursor.getString(2));
        }
        return contact;
    }


    @Override
    public boolean addCallData(Call call) throws Exception {
        database = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name",call.getName());
        cv.put("telephonenumber",call.getTelephonenumber());
        cv.put("count",call.getCount());
        cv.put("callTime",call.getCallTime());
        long rowId = database.insert("numberinfomation",null,cv);
        if(rowId>0){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public boolean deleteCallData(int _id) throws Exception {
        return false;
    }

    @Override
    public boolean deleteCallDataByTag(Call call) throws Exception {
        return false;
    }

    @Override
    public boolean updateCallData(Call call) throws Exception {
        return false;
    }

    @Override
    public List<Call> findAllCallData() throws Exception {
        database = helper.getReadableDatabase();
        Cursor cursor = database.rawQuery("select *from numberinfomation",null);
        List<Call> calls = new ArrayList<>();
        while (cursor.moveToNext()){
            Call call = new Call();
            call.set_id(cursor.getInt(0));
            call.setName(cursor.getString(1));
            call.setTelephonenumber(cursor.getString(2));
            call.setCount(cursor.getInt(3));
            call.setCallTime(cursor.getString(4));
            calls.add(call);
        }
        return calls;
    }

    @Override
    public Call findCallDataById(int _id) throws Exception {
        return null;
    }

    @Override
    public Call findCallDataByName(String number) throws Exception {
        return null;
    }

    @Override
    public boolean addMessageData(Message msg) throws Exception {
        database = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name",msg.getName());
        cv.put("telephonenumber",msg.getTelephonenumber());
        cv.put("messagecontent",msg.getMessagecontent());
        cv.put("sendTime",msg.getSendTime());
        long rowId = database.insert("messages",null,cv);
        if(rowId>0){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public boolean deleteMessageData(int _id) throws Exception {
        return false;
    }

    @Override
    public boolean deleteMessageDataByTag(Message msg) throws Exception {
        return false;
    }

    @Override
    public boolean updateMessageData(Message msg) throws Exception {
        return false;
    }

    @Override
    public List<Message> findAllMessageData() throws Exception {
        database = helper.getReadableDatabase();
        List<Message> msgs = new ArrayList<>();
        Cursor cursor = database.rawQuery("select *from messages",null);
        while (cursor.moveToNext()){
            Message msg = new Message();
            msg.set_id(cursor.getInt(0));
            msg.setName(cursor.getString(1));
            msg.setTelephonenumber(cursor.getString(2));
            msg.setMessagecontent(cursor.getString(3));
            msg.setSendTime(cursor.getString(4));
            msgs.add(msg);
        }
        return msgs;
    }

    @Override
    public Message findMessageDataById(int _id) throws Exception {
        return null;
    }

    @Override
    public Message findMessageDataByName(String number) throws Exception {
        return null;
    }

    @Override
    public String checkFileVirus(String md5) throws Exception {
        String desc = null;
        SQLiteDatabase db = SQLiteDatabase
                .openDatabase(context.getFilesDir().getAbsolutePath() +
                                "/antivirus.db",
                        null, SQLiteDatabase.OPEN_READONLY);

        Cursor cursor = db.rawQuery("select desc from datable where md5=?", new String[]{md5});//查询当前的md5是否在当前的数据库里面
        //判断当前的游标是否可以移动
        if(cursor.moveToNext()){
            desc=cursor.getString(0);
        }
        return desc;
    }
}
