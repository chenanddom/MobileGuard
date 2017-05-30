package com.mobileguard.service;

import com.mobileguard.domain.Call;
import com.mobileguard.domain.Message;

import java.util.List;

/**
 * 实现黑名单的发送的信息的数据的管理
 * Created by chendom on 2017/4/17 0017.
 */

public interface MessageService {
    public boolean addMessage(Message msg)throws Exception;
    public boolean deleteMessage(int _id)throws Exception;
    public boolean deleteMessage(Message msg)throws Exception;
    public boolean updateMessage(Message msg)throws Exception;
    public List<Message> findAllMessage()throws Exception;
    public Message findMessageById(int _id)throws Exception;
    public Message findMessageByNumber(String number)throws Exception;
}
