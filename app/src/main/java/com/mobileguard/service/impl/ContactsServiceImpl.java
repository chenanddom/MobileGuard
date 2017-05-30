package com.mobileguard.service.impl;

import android.content.Context;

import com.mobileguard.dao.AntivirusDao;
import com.mobileguard.dao.CallDao;
import com.mobileguard.dao.ContactDao;
import com.mobileguard.dao.MessageDao;
import com.mobileguard.dao.impl.ContactDaoImpl;
import com.mobileguard.domain.Call;
import com.mobileguard.domain.Contact;
import com.mobileguard.domain.Message;
import com.mobileguard.service.AntivirusService;
import com.mobileguard.service.CallService;
import com.mobileguard.service.ContactsService;
import com.mobileguard.service.MessageService;

import java.util.List;

/**
 * 业务逻辑操作的service层
 * Created by chendom on 2017/4/14 0014.
 */

public class ContactsServiceImpl implements ContactsService ,CallService,MessageService,AntivirusService{
    private ContactDao dao;
    private CallDao callDao;
    private MessageDao messageDao;
    private AntivirusDao antivirusDao;

    public ContactsServiceImpl(Context context) {
        dao = new ContactDaoImpl(context);
        callDao = new ContactDaoImpl(context);
        messageDao = new ContactDaoImpl(context);
        antivirusDao = new ContactDaoImpl(context);
    }


    @Override
    public boolean addContact(Contact contact) throws Exception {
        return dao.addData(contact);
    }

    @Override
    public boolean deleteContact(int _id) throws Exception {
        return dao.deleteData(_id);
    }

    @Override
    public boolean deleteContactByNameAndNumber(Contact contact) throws Exception {
        return dao.deleteDataByTag(contact);
    }

    @Override
    public boolean updateContact(Contact contact) throws Exception {
        return dao.updateData(contact);
    }

    @Override
    public List<Contact> findContact() throws Exception {
        return dao.findAllData();
    }

    @Override
    public Contact findContactById(int _id) throws Exception {
        return dao.findDataById(_id);
    }

    @Override
    public Contact findContactByNumber(String number) throws Exception {
        return dao.findDataByName(number);
    }


    @Override
    public boolean addCall(Call call) throws Exception {
        return callDao.addCallData(call);
    }

    @Override
    public boolean deleteCall(int _id) throws Exception {
        return callDao.deleteCallData(_id);
    }

    @Override
    public boolean deleteCall(Call call) throws Exception {
        return callDao.deleteCallDataByTag(call);
    }

    @Override
    public boolean updateCall(Call call) throws Exception {
        return callDao.updateCallData(call);
    }

    @Override
    public List<Call> findAllCall() throws Exception {
        return callDao.findAllCallData();
    }

    @Override
    public Call findCallById(int _id) throws Exception {
        return callDao.findCallDataById(_id);
    }

    @Override
    public Call findCallByNumber(String number) throws Exception {
        return callDao.findCallDataByName(number);
    }

    @Override
    public boolean addMessage(Message msg) throws Exception {
        return messageDao.addMessageData(msg);
    }

    @Override
    public boolean deleteMessage(int _id) throws Exception {
        return messageDao.deleteMessageData(_id);
    }

    @Override
    public boolean deleteMessage(Message msg) throws Exception {
        return messageDao.deleteMessageDataByTag(msg);
    }

    @Override
    public boolean updateMessage(Message msg) throws Exception {
        return messageDao.updateMessageData(msg);
    }

    @Override
    public List<Message> findAllMessage() throws Exception {
        return messageDao.findAllMessageData();
    }

    @Override
    public Message findMessageById(int _id) throws Exception {
        return messageDao.findMessageDataById(_id);
    }

    @Override
    public Message findMessageByNumber(String number) throws Exception {
        return messageDao.findMessageDataByName(number);
    }

    @Override
    public String checkFileVirus(String md5) throws Exception {
        return antivirusDao.checkFileVirus(md5);
    }
}
