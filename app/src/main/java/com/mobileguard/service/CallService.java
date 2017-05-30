package com.mobileguard.service;

import com.mobileguard.domain.Call;

import java.util.List;

/**
 * 实现对黑名单的拨打电话的管理
 * Created by chendom on 2017/4/17 0017.
 */

public interface CallService {
   public boolean addCall(Call call)throws Exception;
    public boolean deleteCall(int _id)throws Exception;
    public boolean deleteCall(Call call)throws Exception;
    public boolean updateCall(Call call)throws Exception;
    public List<Call> findAllCall()throws Exception;
    public Call findCallById(int _id)throws Exception;
    public Call findCallByNumber(String number)throws Exception;
}
