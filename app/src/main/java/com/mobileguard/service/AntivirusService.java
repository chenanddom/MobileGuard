package com.mobileguard.service;

/**
 * 管理病毒库的业务逻辑层
 * Created by chendom on 2017/4/19 0019.
 */

public interface AntivirusService {
    public String checkFileVirus(String md5)throws Exception;
}
