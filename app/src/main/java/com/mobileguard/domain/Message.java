package com.mobileguard.domain;

/**
 * 黑名单的发送的信息的域对象类
 * Created by chendom on 2017/4/17 0017.
 */

public class Message {
    private int _id;
    private String name;
    private String telephonenumber;
    private String messagecontent;
    private String sendTime;
    public Message(){}

    public Message(int _id, String name, String telephonenumber, String messagecontent, String sendTime) {
        this.name = name;
        this._id = _id;
        this.telephonenumber = telephonenumber;
        this.messagecontent = messagecontent;
        this.sendTime = sendTime;
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

    public String getMessagecontent() {
        return messagecontent;
    }

    public void setMessagecontent(String messagecontent) {
        this.messagecontent = messagecontent;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    @Override
    public String toString() {
        return "Message{" +
                "_id=" + _id +
                ", name='" + name + '\'' +
                ", telephonenumber='" + telephonenumber + '\'' +
                ", messagecontent='" + messagecontent + '\'' +
                ", sendTime='" + sendTime + '\'' +
                '}';
    }
}
