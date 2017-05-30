package com.mobileguard.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mobileguard.adapter.CommentsAdapter;
import com.mobileguard.base.BaseActivity;
import com.mobileguard.constants.Const;
import com.mobileguard.domain.Comment;
import com.mobileguard.domain.User;
import com.mobileguard.utils.JSONParserUtil;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 用于显示用户的评论
 * Created by chendom on 2017/4/26 0026.
 */

public class CommentsActivity extends BaseActivity {
    private OkHttpClient client;
    private List<String> cookies;
    private String JSESSIONID;
    private User user;
    private ListView lvCommentsList;
    private CommentsAdapter adapter;
    private List<Comment> comments;

    @Override
    public int getLayout() {
        return R.layout.activity_comments;
    }

    @Override
    public void initData() {
        client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        Response response = chain.proceed(request);
                        return response;
                    }
                })
                .connectTimeout(4000, TimeUnit.MILLISECONDS)
                .readTimeout(4000, TimeUnit.MILLISECONDS)
                .writeTimeout(4000, TimeUnit.MILLISECONDS)
                .build();
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("data");
        JSESSIONID = bundle.getString("JSESSIONID");
        user = bundle.getParcelable("user");
        System.out.println("-----users-----:" + user.toString());

       /* String content = "{\"code\":200,\"comments\":[{\"commContent\":\"test data for debug\",\"commId\":\"123\",\"commTime\":1493217660671,\"praiseTime\":1}," +
                "{\"commContent\":\"test data for debug\",\"commId\":\"1c7a9ce1-7441-480b-ab5a-496d3e5244b4\",\"commTime\":1493217711238,\"praiseTime\":1}," +
                "{\"commContent\":\"test data for debug\",\"commId\":\"268dcf5d-432e-4d6f-b300-484fb96868e6\",\"commTime\":1493217725973,\"praiseTime\":1}]}";
        comments = JSONParserUtil.parseCommentsJson(content);*/
    }

    @Override
    public void initView() {
        lvCommentsList = (ListView) findViewById(R.id.lvcomments);
        getComments();

    }

    @Override
    public void setListener() {
        lvCommentsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        ((Button) findViewById(R.id.submitcomment)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitComment();
            }
        });
    }

    public void getComments() {
        FormBody.Builder builder = new FormBody.Builder();
                 /* 添加两个参数,实现用户的登录*/
        // builder.add("username", user.getUserName()).add("pswd", user.getUserPassword());
        FormBody body = builder.build();
        final Request request;

        System.out.println("------+++---------JSESSIONID--------------------:" + JSESSIONID);
        if (JSESSIONID == null) {
            request = new Request.Builder().url(Const.searchPraiseUrl).post(body).build();
        } else {
            request = new Request.Builder().url(Const.searchPraiseUrl).addHeader("cookie", JSESSIONID).post(body).build();
        }
                /* 下边和get一样了 */
        client.newCall(request).enqueue(new Callback() {
            public void onResponse(Call call, final Response response) throws IOException {
                final String bodyStr = response.body().string();
                System.out.println("-----------bodystr-------------" + bodyStr);
                if (JSESSIONID == null) {
                    Headers headers = response.headers();
                    cookies = headers.values("set-cookie");
                    JSESSIONID = cookies.get(0);
                    System.out.println("onResponse-size: " + cookies);
                    JSESSIONID = JSESSIONID.substring(0, JSESSIONID.indexOf(";"));
                    System.out.println("session:" + JSESSIONID);
                }
                final boolean ok = response.isSuccessful();
                runOnUiThread(new Runnable() {
                    public void run() {
                        if (ok) {
                            comments = JSONParserUtil.parseCommentsJson(bodyStr);
                            System.out.println("---------------comments------------------:" + comments);
                            if (comments.size() > 0) {
                                adapter = new CommentsAdapter(CommentsActivity.this, comments, JSESSIONID);
                                lvCommentsList.setAdapter(adapter);
                            }
                        } else {
                            Toast.makeText(CommentsActivity.this, "server error : " + bodyStr, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            public void onFailure(Call call, final IOException e) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(CommentsActivity.this, "error : " + e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }

    public void submitComment() {
        final String commContent = ((EditText) findViewById(R.id.commentcontent)).getText().toString().trim();
        FormBody.Builder builder = new FormBody.Builder();
                 /* 添加两个参数,实现用户的登录*/
        builder.add("userId", user.getUserId()).add("content", commContent).add("praisetime", "0");
        FormBody body = builder.build();
        final Request request;

        System.out.println("---------------JSESSIONID--------------------:" + JSESSIONID);
        if (JSESSIONID == null) {
            request = new Request.Builder().url(Const.addCommentUrl).post(body).build();
        } else {
            request = new Request.Builder().url(Const.addCommentUrl).addHeader("cookie", JSESSIONID).post(body).build();
        }
                /* 下边和get一样了 */
        client.newCall(request).enqueue(new Callback() {
            public void onResponse(Call call, final Response response) throws IOException {
                final String bodyStr = response.body().string();
                if (JSESSIONID == null) {
                    Headers headers = response.headers();
                    cookies = headers.values("set-cookie");
                    JSESSIONID = cookies.get(0);
                    System.out.println("onResponse-size: " + cookies);
                    JSESSIONID = JSESSIONID.substring(0, JSESSIONID.indexOf(";"));
                    System.out.println("session:" + JSESSIONID);
                }
                final boolean ok = response.isSuccessful();
                runOnUiThread(new Runnable() {
                    public void run() {
                        if (ok) {
                          /*  //   List<Comment> comments = JSONParserUtil.parseCommentsJson(bodyStr);
                            Comment comment = new Comment();
                            comment.setCommId(user.getUserId());
                            comment.setCommContent(commContent);
                            comment.setPraiseTime(0);
                            Date date = new Date();
                            comment.setCommTime(date.getTime());
                            comments.add(0, comment);
                            adapter.notifyDataSetChanged();*/
                            getComments();
                        } else {
                            Toast.makeText(CommentsActivity.this, "server error : " + bodyStr, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            public void onFailure(Call call, final IOException e) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(CommentsActivity.this, "error : " + e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }
}
