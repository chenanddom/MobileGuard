package com.mobileguard.utils;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;

/**
 * md5加密
 * Created by chendom on 2017/4/19 0019.
 */

public class MD5Utils {
    public static String encode(String pass){
        try {
            //如果没有改算法则就会错误
            MessageDigest instance=MessageDigest.getInstance("MD5");
            byte[] digist=instance.digest(pass.getBytes());
            StringBuffer sb=new StringBuffer();
            for(byte b : digist){
                int i=b & 0xff;
                String  hexString = Integer.toHexString(i);
                if(hexString.length()<2){
                    hexString="0"+hexString;
                }
                sb.append(hexString);
            }
            return sb.toString();

        } catch (Exception e) {
            // TODO: handle exception
        }
        return "";
    }
@Deprecated
    public static String getFileMD5(String sourceDir) {
        // TODO Auto-generated method stub
        File file = new File(sourceDir);
        try {
            FileInputStream fis = new FileInputStream(file);
            byte[] buf = new byte[1024];
            int len=0;
            //获取数字摘要
            MessageDigest messageDigest = MessageDigest.getInstance("md5");
            while((len = fis.read(buf))!=-1){
                messageDigest.update(buf, 0, len);
            }
            byte[] result = messageDigest.digest();
            StringBuffer sb = new StringBuffer();
            for(byte b : result){
                int number = b&0xff;//+1
                String hex = Integer.toHexString(number);
                if(hex.length()==1){
                    sb.append("0"+hex);
                }else{
                    sb.append(hex);
                }
                return sb.toString();
            }
        } catch (Exception e) {
            // TODO: handle exception
        }

        return null;
    }

}
