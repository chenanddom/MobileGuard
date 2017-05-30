package com.mobileguard.domain;

/**
 * 黑名单的对象
 * Created by chendom on 2017/4/14 0014.
 */

public class Contact {
    private int _id;
    private String name;
    private String telephonenumber;

    public Contact() {
    }

    public Contact(int _id, String name, String telephonenumber) {
        this._id = _id;
        this.name = name;
        this.telephonenumber = telephonenumber;
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

    @Override
    public String toString() {
        return "Contact{" +
                "_id='" + _id + '\'' +
                ", name='" + name + '\'' +
                ", telephonenumber='" + telephonenumber + '\'' +
                '}';
    }
}
