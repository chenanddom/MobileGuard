package com.mobileguard.service;

import com.mobileguard.domain.Contact;

import java.util.List;

/**
 *业务逻辑操作接口
 * Created by chendom on 2017/4/14 0014.
 */

public interface ContactsService {
    public boolean addContact(Contact contact)throws  Exception;
    @Deprecated
    public boolean deleteContact(int _id)throws  Exception;
    public boolean deleteContactByNameAndNumber(Contact contact)throws Exception;
    public boolean updateContact(Contact contact)throws Exception;
    public List<Contact> findContact()throws  Exception;
    public Contact findContactById(int _id)throws Exception;
    public Contact findContactByNumber(String number)throws Exception;
}
