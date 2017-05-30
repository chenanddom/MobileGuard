package com.mobileguard.activitys;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mobileguard.base.BaseActivity;
import com.mobileguard.constants.Const;
import com.mobileguard.domain.User;
import com.mobileguard.utils.JSONParserUtil;

import java.io.IOException;
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
 * 实现用户的登录的功能
 * Created by chendom on 2017/4/9 0009.
 */

public class LoginActivity extends BaseActivity {
    private String userAccount;
    private String userPassword;
    private OkHttpClient client;
    private List<String> cookies;
    private String mysession;
    private CheckBox checkBox;
    private boolean isAutoLogin;
    private SharedPreferences preferences;
    private long time;


    @Override
    public int getLayout() {
        return R.layout.activity_login;
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
        preferences = getSharedPreferences("appdata", MODE_PRIVATE);
        isAutoLogin = preferences.getBoolean("isautologin", false);
        userAccount = preferences.getString("username", null);
        userPassword = preferences.getString("password", null);
        System.out.println("userAccount:" + userAccount + "----" + "userPassword:" + userPassword);
        time = preferences.getLong("time", 0);
        if (System.currentTimeMillis() - time >= 18000000) {
            preferences.edit().putLong("time", 0).commit();
            preferences.edit().putString("JSESSIONID", null).commit();
            System.out.println("-----------------------------------------------------");
        }
        mysession = preferences.getString("JSESSIONID", null);
    }
    @Override
    public void initView() {
        checkBox = (CheckBox) findViewById(R.id.isautologin);
        if (isAutoLogin) {
            checkBox.setChecked(true);
            if (!TextUtils.isEmpty(userAccount) && !TextUtils.isEmpty(userPassword)) {
                ((EditText) findViewById(R.id.useraccount)).setText(userAccount);
                ((EditText) findViewById(R.id.userpassword)).setText(userPassword);
                userLogin(userAccount, userPassword);
            }
        } else {
            checkBox.setChecked(false);
        }
    }

    @Override
    public void setListener() {
        findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userAccount = ((EditText) findViewById(R.id.useraccount)).getText().toString().trim();
                userPassword = ((EditText) findViewById(R.id.userpassword)).getText().toString().trim();
                if (!TextUtils.isEmpty(userAccount) && !TextUtils.isEmpty(userPassword)) {
                    userLogin(userAccount, userPassword);
                } else {
                    Toast.makeText(LoginActivity.this, "请填写完整账号或者密码", Toast.LENGTH_SHORT).show();
                }

            }
        });
        findViewById(R.id.register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.welcome_fade_in, R.anim.welcome_fade_out);
            }
        });
        //实现对复选框的监听(对后续的自动登录的操作做出相应的操作)
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isAutoLogin) {
                    System.out.println("-------设置为不自动-----------");
                    isAutoLogin = false;
                    checkBox.setChecked(false);
                    preferences.edit().putBoolean("isautologin", false).commit();
                } else {
                    System.out.println("-------设置为自动-----------");
                    isAutoLogin = true;
                    checkBox.setChecked(true);
                    preferences.edit().putBoolean("isautologin", true).commit();
                }
            }
        });

    }

    /**
     * 实现用户登录的方法
     */
    public void userLogin(final String userAccount, final String userPassword) {
        FormBody.Builder builder = new FormBody.Builder();
                 /* 添加两个参数,实现用户的登录*/
        builder.add("username", userAccount).add("pswd", userPassword);
        FormBody body = builder.build();
        final Request request;

        System.out.println("---------------JSESSIONID--------------------:" + mysession);
        if (mysession == null) {
            request = new Request.Builder().url(Const.loginUrl).post(body).build();
        } else {
            request = new Request.Builder().url(Const.loginUrl).addHeader("cookie", mysession).post(body).build();
        }
                /* 下边和get一样了 */
        client.newCall(request).enqueue(new Callback() {
            public void onResponse(Call call, final Response response) throws IOException {
                final String bodyStr = response.body().string();
                if (mysession == null) {
                    Headers headers = response.headers();
                    cookies = headers.values("set-cookie");
                    mysession = cookies.get(0);
                    System.out.println("onResponse-size: " + cookies);
                    mysession = mysession.substring(0, mysession.indexOf(";"));
                    System.out.println("session:" + mysession);
                }
                final boolean ok = response.isSuccessful();
                runOnUiThread(new Runnable() {
                    public void run() {
                        if (ok) {
                            System.out.println(bodyStr);
                            User user = JSONParserUtil.parseJson(bodyStr);
                            if (user != null) {
                                if (isAutoLogin) {

                                    preferences.edit().putString("username", userAccount).commit();
                                    preferences.edit().putString("password", userPassword).commit();
                                    System.out.println("----------------autologin-------------------");
                                    //如果表示回话成功的话将回话的标示记录在文件中，便于下一次的登录
                                    System.out.println("---------------------------save session before---------------" + mysession);
                                    preferences.edit().putString("JSESSIONID", mysession).commit();

                                    preferences.edit().putLong("time", System.currentTimeMillis()).commit();
                                    System.out.println("---------------------------save session---------------");
                                } else {
                                    String username = preferences.getString("username", null);
                                    String password = preferences.getString("password", null);
                                    if (!TextUtils.isEmpty(username)) {
                                        preferences.edit().putString("username", null).commit();
                                    }
                                    if (!TextUtils.isEmpty(password)) {
                                        preferences.edit().putString("password", null).commit();
                                    }

                                }
                                System.out.println("user:" + user);
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("JSESSIONID", mysession);
                                bundle.putParcelable("user", user);
                                intent.putExtra("data", bundle);
                                startActivity(intent);
                                finish();
                                overridePendingTransition(R.anim.welcome_fade_in, R.anim.welcome_fade_out);
                            }

                            // Toast.makeText(MainActivity.this, bodyStr, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LoginActivity.this, "server error : " + bodyStr, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            public void onFailure(Call call, final IOException e) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(LoginActivity.this, "error : " + e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }

    public void startActivity(Context context, Class clazz) {
        if (clazz != null) {
            startActivity(new Intent(context, clazz));
            overridePendingTransition(R.anim.welcome_fade_in, R.anim.welcome_fade_out);
        } else {
            Toast.makeText(context, "你要开启的窗体不存在", Toast.LENGTH_SHORT).show();
        }
    }

}
