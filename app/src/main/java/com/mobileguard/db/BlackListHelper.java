package com.mobileguard.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 得到联系人的数据库
 * Created by chendom on 2017/4/14 0014.
 */

public class BlackListHelper extends SQLiteOpenHelper {
    private static final String DATABASENAME = "black_list.db";
    private static final String TABLENAME = "list";
    private static final String TABLENAME2 = "numberinfomation";
    private static final String TABLENAME3 = "messages";

    public BlackListHelper(Context context) {
        super(context, DATABASENAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table " + TABLENAME + "(_id integer primary key autoincrement,name varchar(55),telephonenumber varchar(55) unique)";
        db.execSQL(sql);
        String sql2 = "create table " + TABLENAME2 + "(_id integer primary key autoincrement,name varchar(55)," +
                "telephonenumber varchar(55),count integer,callTime text,foreign key(telephonenumber) references " + TABLENAME + "(telephonenumber))";
        db.execSQL(sql2);
        String sql3 = "create table " + TABLENAME3 + "(_id integer primary key autoincrement,name varchar(55)," +
                "telephonenumber varchar(55),messagecontent varchar(255),sendTime text,foreign key(telephonenumber) references " + TABLENAME + "(telephonenumber))";
        db.execSQL(sql3);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "drop table id exists" + TABLENAME;
        db.execSQL(sql);
        onCreate(db);
        String sql2 = "drop table id exists" + TABLENAME2;
        db.execSQL(sql2);
        onCreate(db);
        String sql3 = "drop table id exists" + TABLENAME3;
        db.execSQL(sql3);
        onCreate(db);
    }
}