package com.mobileguard.dao;

import com.mobileguard.domain.Contact;

import java.util.List;

/**
 * dao接口
 * Created by chendom on 2017/4/14 0014.
 */

public interface ContactDao {
    public boolean addData(Contact contact)throws  Exception;
    @Deprecated
    public boolean deleteData(int _id)throws  Exception;
    public boolean deleteDataByTag(Contact contact)throws Exception;
    public boolean updateData(Contact contact)throws Exception;
    public List<Contact> findAllData()throws  Exception;
    public Contact findDataById(int _id)throws Exception;
    public Contact findDataByName(String number)throws Exception;
}
