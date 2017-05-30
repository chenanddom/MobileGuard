package com.mobileguard.dao;

import com.mobileguard.domain.Call;
import com.mobileguard.domain.Contact;

import java.util.List;

/**
 * Created by chendom on 2017/4/17 0017.
 */

public interface CallDao {
    public boolean addCallData(Call call)throws  Exception;
    @Deprecated
    public boolean deleteCallData(int _id)throws  Exception;
    public boolean deleteCallDataByTag(Call call)throws Exception;
    public boolean updateCallData(Call call)throws Exception;
    public List<Call> findAllCallData()throws  Exception;
    public Call findCallDataById(int _id)throws Exception;
    public Call findCallDataByName(String number)throws Exception;
}
