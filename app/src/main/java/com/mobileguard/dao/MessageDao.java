package com.mobileguard.dao;

import com.mobileguard.domain.Call;
import com.mobileguard.domain.Contact;
import com.mobileguard.domain.Message;

import java.util.List;

/**
 *
 * Created by chendom on 2017/4/17 0017.
 */
public interface MessageDao {
    public boolean addMessageData(Message msg)throws  Exception;
    @Deprecated
    public boolean deleteMessageData(int _id)throws  Exception;
    public boolean deleteMessageDataByTag(Message msg)throws Exception;
    public boolean updateMessageData(Message msg)throws Exception;
    public List<Message> findAllMessageData()throws  Exception;
    public Message findMessageDataById(int _id)throws Exception;
    public Message findMessageDataByName(String number)throws Exception;
}
