package com.mobileguard.utils;

import android.util.Log;

import com.mobileguard.domain.Comment;
import com.mobileguard.domain.Updation;
import com.mobileguard.domain.User;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * 实现对json数据的解析
 * Created by chendom on 2017/4/9 0009.
 */

public class JSONParserUtil {
    /**
     * 解析json格式的数据得到数据的对象
     *
     * @paramjson传入json格式的数据,
     * @returnf返回一个对象
     */
    public static User parseJson(String json) {
        JSONObject object = JSONObject.fromObject(json);
        int code = object.getInt("code");
        if (code != 0 && code == 200) {
            User user = new User();
            JSONArray array = object.getJSONArray("user");
            JSONObject object2 = JSONObject.fromObject(array.get(0));
            user.setUserId(object2.getString("userId"));
            user.setUserName(object2.getString("userName"));
            user.setUserAge(object2.getInt("userAge"));
            user.setUserGender(object2.getString("userGender"));
            user.setUserClass(object2.getInt("userClass"));
            user.setVersionNumber(object2.getInt("versionNumber"));
            user.setUserDescription(object2.getString("userDescription"));
            return user;
        } else {
            return null;
        }
    }

    /*
    {"code":200,"comments":[{"commContent":"test data for debug","commId":"123","commTime":1493217660671,"praiseTime":1},
    {"commContent":"test data for debug","commId":"08cc4798-e223-48d8-8d27-4457886ba098","commTime":1493217696817,"praiseTime":1},
    {"commContent":"test data for debug","commId":"1c7a9ce1-7441-480b-ab5a-496d3e5244b4","commTime":1493217711238,"praiseTime":1},
    {"commContent":"test data for debug","commId":"268dcf5d-432e-4d6f-b300-484fb96868e6","commTime":1493217725973,"praiseTime":1},
    {"commContent":"test data for debug","commId":"417ddb43-1d74-4621-9144-c521ec3132a1","commTime":1493217740082,"praiseTime":1}]}
     */
    public static List<Comment> parseCommentsJson(String json) {
        JSONObject object = JSONObject.fromObject(json);
        int code = object.getInt("code");
        Log.d("code", "code:" + code);
        JSONArray array=null;
        List<Comment> comments=null;
        if (code == 200) {
            array = object.getJSONArray("comments");
            comments = new ArrayList<>();
            for (Object obj : array) {
                Comment comment = new Comment();
                String commContent = ((JSONObject) obj).getString("commContent");
                String commId = ((JSONObject) obj).getString("commId");
                long commTime = ((JSONObject) obj).getLong("commTime");
                int praiseTime = ((JSONObject) obj).getInt("praiseTime");
                comment.setCommId(commId);
                comment.setCommContent(commContent);
                comment.setCommTime(commTime);
                comment.setPraiseTime(praiseTime);
                comments.add(comment);
            }
        }
        if (comments.size() > 0) {
            return comments;
        } else {
            return null;
        }

    }
public static Updation paserUpdationJson(String json){
    JSONObject obj = JSONObject.fromObject(json);
    JSONObject object = obj.getJSONObject("updation");
    String versionName = object.getString("versionName");
    int versionConde = object.getInt("versionCode");
    System.out.println("------versionCode------"+versionConde);
    String description = object.getString("description");
    String downloadUrl = object.getString("downloadUrl");
  Updation updation = new Updation(versionName,versionConde,description,downloadUrl);
 if (updation!=null){
        return updation;
    }else{
        return null;
    }

}

}
