package com.mobileguard.dao;

/**
 * Created by chendom on 2017/4/19 0019.
 */

public interface AntivirusDao {
    public String checkFileVirus(String md5)throws Exception;
}
