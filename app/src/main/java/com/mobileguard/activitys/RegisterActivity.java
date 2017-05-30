package com.mobileguard.activitys;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
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
 * 实现用户的注册的功能的窗体
 * Created by chendom on 2017/4/9 0009.
 */

public class RegisterActivity extends BaseActivity {
    private EditText edit_username;
    private EditText edit_password;
    private EditText edit_conform_password;
    private EditText edit_birthday;
    private EditText edit_description;
    private String gender;
    private OkHttpClient client;
    private String username;
    private String password;
    private String conformpassword;
    private String birthday;
    private String description;
    private String EXP = "^[A-Za-z0-9\\s]+$";//正则表达式的表示验证
    private String EXP2 = "^[0-9]+$";//正则表达式的数字匹配

    @Override
    public int getLayout() {
        return R.layout.activity_register;
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
       /* LocationManager lm = (LocationManager) this.getSystemService(this.LOCATION_SERVICE);
        String provider = lm.GPS_PROVIDER;*/
    }

    @Override
    public void initView() {
        edit_username = (EditText) findViewById(R.id.username);
        edit_password = (EditText) findViewById(R.id.password);
        edit_conform_password = (EditText) findViewById(R.id.conform_password);
        edit_birthday = (EditText) findViewById(R.id.birthday);
        edit_description = (EditText) findViewById(R.id.descrption);

    }

    @Override
    public void setListener() {
        findViewById(R.id.register_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.register_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userRegister(username, password, birthday, gender, description);
                System.out.println("username" + username + "--" + "password:" + password + "--" + "birthday:" + birthday + "--" + "gender:" + gender + "--" + "description:" + description);
            }
        });
        ((RadioButton) findViewById(R.id.gender_male)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    gender = ((RadioButton) findViewById(R.id.gender_male)).getText().toString();
                    System.out.println("-------gender:-----" + gender);
                }
            }
        });
        ((RadioButton) findViewById(R.id.gender_female)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    gender = ((RadioButton) findViewById(R.id.gender_female)).getText().toString();
                    System.out.println("-------gender:-----" + gender);
                }

            }
        });
        edit_username.addTextChangedListener(new EditChangedListener(edit_username, R.id.username));
        edit_password.addTextChangedListener(new EditChangedListener(edit_password, R.id.password));
        edit_conform_password.addTextChangedListener(new EditChangedListener(edit_conform_password, R.id.conform_password));
        edit_birthday.addTextChangedListener(new EditChangedListener(edit_birthday, R.id.birthday));
        edit_description.addTextChangedListener(new EditChangedListener(edit_description, R.id.descrption));
    }

    /**
     * 实现用户登录的方法
     */
    public void userRegister(final String username, final String password, final String age, final String gender, final String description) {
        FormBody.Builder builder = new FormBody.Builder();
                 /* 添加两个参数,实现用户的登录*/
        builder.add("username", username).add("pswd", password).add("age", age).add("gender", gender).add("description", description);
        FormBody body = builder.build();
        final Request request = new Request.Builder().url(Const.registerUrl).post(body).build();

                /* 下边和get一样了 */
        client.newCall(request).enqueue(new Callback() {
            public void onResponse(Call call, final Response response) throws IOException {
                final String bodyStr = response.body().string();
                final boolean ok = response.isSuccessful();
                runOnUiThread(new Runnable() {
                    public void run() {
                        if (ok) {
                            System.out.println("----------------------body:" + bodyStr);
                            if (bodyStr.trim().equals("success")) {
                                Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                finish();
                                overridePendingTransition(R.anim.welcome_fade_in, R.anim.welcome_fade_out);
                            } else if (bodyStr.trim().equals("500")) {
                                Toast.makeText(RegisterActivity.this, "用户已经存在", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT).show();

                            }
                        } else {
                            Toast.makeText(RegisterActivity.this, "server error : " + bodyStr, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            public void onFailure(Call call, final IOException e) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(RegisterActivity.this, "error : " + e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }

    class EditChangedListener implements TextWatcher {
        private EditText editText;
        private int id;

        public EditChangedListener(EditText editText, int id) {
            this.editText = editText;
            this.id = id;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            Log.d("tag", "-------before---------------");
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            Log.d("tag", "-------changing---------------");
        }

        @Override
        public void afterTextChanged(Editable s) {
            Log.d("tag", "-------after---------------");
            String content = editText.getText().toString().trim();
            switch (id) {
                case R.id.username:
                    if (content.matches(EXP)) {
                        username = content;
                        ((TextView) findViewById(R.id.username_info)).setText("");
                    } else {
                        ((TextView) findViewById(R.id.username_info)).setText("账号只能输入数字或者字母");
                    }
                    break;
                case R.id.password:
                    if (content.matches(EXP)) {
                        password = content;
                        ((TextView) findViewById(R.id.password_info)).setText("");
                    } else {
                        ((TextView) findViewById(R.id.password_info)).setText("账号只能输入数字或者字母");
                    }

                    break;
                case R.id.conform_password:
                    if (content.matches(EXP)) {

                        if (content.equals(password)) {
                            conformpassword = content;
                            ((TextView) findViewById(R.id.conform_password_info)).setText("");
                        } else {
                            ((TextView) findViewById(R.id.conform_password_info)).setText("两次输入的密码不一致");
                        }
                    } else {
                        ((TextView) findViewById(R.id.conform_password_info)).setText("密码只能输入数字或者字母");
                    }
                    break;

                case R.id.birthday:
                    if (content.matches(EXP2)) {
                        birthday = content;
                        ((TextView) findViewById(R.id.birthday_info)).setText("");
                    } else {
                        ((TextView) findViewById(R.id.birthday_info)).setText("年龄只能是数字");
                    }
                    break;
                case R.id.descrption:
                    if (content.matches(EXP)) {
                        description = content;
                        ((TextView) findViewById(R.id.des_info)).setText("");
                    } else {
                        ((TextView) findViewById(R.id.des_info)).setText("描述只能是数字和字母");
                    }
                    break;

                default:

                    break;
            }
            System.out.println(content);
        }
    }
}
