package com.mobileguard.constants;

/**
 * 存放常量的类
 * Created by chendom on 2017/4/9 0009.
 */

public class Const {
    private static final String IPADDRESS="http://192.168.43.229";
    //private static final String IPADDRESS="http://172.18.97.118";
   // private static final String IPADDRESS="http://192.168.1.108";
    public static final String loginUrl = IPADDRESS+":8080/MobileGuardServer/mobileguard/mobilelogin";
    public static final String registerUrl = IPADDRESS+":8080/MobileGuardServer/mobileguard/mobileregister";
    public static final String addPraiseUrl=IPADDRESS+":8080/MobileGuardServer/mobileguard/praise";
    public static final String searchPraiseUrl=IPADDRESS+":8080/MobileGuardServer/mobileguard/search";
    public static final String minusUrl=IPADDRESS+":8080/MobileGuardServer/mobileguard/minus";
    public static final String addCommentUrl=IPADDRESS+":8080/MobileGuardServer/mobileguard/comment";
   // public static final String downloadVersionInfoUrl = "http://172.18.99.90:8080/versioninfomation.json";
    public static final String versionInfoUrl = IPADDRESS+":8080/MobileGuardServer/mobileguard/update";
}
