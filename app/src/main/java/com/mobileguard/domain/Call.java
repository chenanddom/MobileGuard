package com.mobileguard.domain;

import java.util.Date;

/**
 * 黑名单的域对象类
 * Created by chendom on 2017/4/17 0017.
 */

public class Call {
    private int _id;
    private String name;
    private String telephonenumber;
    private int count;
    private String callTime;
    public Call(){}

    public Call(int _id, String name, String telephonenumber, int count, String callTime) {
        this._id = _id;
        this.name = name;
        this.telephonenumber = telephonenumber;
        this.count = count;
        this.callTime = callTime;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelephonenumber() {
        return telephonenumber;
    }

    public void setTelephonenumber(String telephonenumber) {
        this.telephonenumber = telephonenumber;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getCallTime() {
        return callTime;
    }

    public void setCallTime(String callTime) {
        this.callTime = callTime;
    }

    @Override
    public String toString() {
        return "Call{" +
                "_id=" + _id +
                ", name='" + name + '\'' +
                ", telephonenumber='" + telephonenumber + '\'' +
                ", count=" + count +
                ", callTime=" + callTime +
                '}';
    }
}
