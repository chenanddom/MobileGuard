package com.mobileguard.activitys;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.mobileguard.adapter.GridViewAdapter;
import com.mobileguard.base.BaseActivity;
import com.mobileguard.constants.Const;
import com.mobileguard.domain.Updation;
import com.mobileguard.domain.User;
import com.mobileguard.engine.GPSInfoProvider;
import com.mobileguard.utils.JSONParserUtil;
import com.mobileguard.utils.StreamUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends BaseActivity {
    // private Button btnRequest;
    private GridView gridView;
    private List<String> name = new ArrayList<>();
    private int[] images;
    private String JSESSIONID;
    private User user;
    // 定义一个变量，来标识是否退出
    private static boolean isExit = false;
    private SharedPreferences preferences;
    private boolean isopenupdate;
    private static final int EXIT = 0;
    private static final int UPDATEAPP = 1;
    private static final int NORMAL = 2;
    private Updation updation;
    private OkHttpClient client;
    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case EXIT:
                    isExit = false;
                    break;
                case UPDATEAPP:
                    if (Build.VERSION.SDK_INT >= 23) {
                        int REQUEST_CODE_CONTACT = 101;
                        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        //验证是否许可权限
                        for (String str : permissions) {
                            if (MainActivity.this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                                //申请权限
                                MainActivity.this.requestPermissions(permissions, REQUEST_CODE_CONTACT);
                                // return;
                            }
                        }
                    }
                    showUpdateDialog();
                    break;
                case NORMAL:
                    Log.d("TAG", "------该应用已经是最新-----");
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public int getLayout() {
        return R.layout.activity_main;
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
        name.add("手机防盗");
        name.add("通信卫士");
        name.add("软件管理");
        name.add("进程管理");
        name.add("流量统计");
        name.add("手机杀毒");
        name.add("缓存清理");
        name.add("未开发");
        name.add("未开发");
        images = new int[]{R.mipmap.mobilesecurity, R.mipmap.communicationguards, R.mipmap.mobilemanager,
                R.mipmap.processmanager, R.mipmap.flowmanager, R.mipmap.mobileantivirus,
                R.mipmap.mobilecache, R.mipmap.mobilehighgrade, R.mipmap.mobilesetting};
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("data");
        user = bundle.getParcelable("user");
        JSESSIONID = bundle.getString("JSESSIONID");
        isopenupdate = preferences.getBoolean("isopenupdate", false);
        System.out.println("versionCode:" + getVersionCode() + "----" + "isopenupdate:" + isopenupdate);
    }

    @Override
    public void initView() {
        // btnRequest = (Button) findViewById(R.id.request);
        gridView = (GridView) findViewById(R.id.gridview);
        gridView.setAdapter(new GridViewAdapter(MainActivity.this, name, images));
        if (user != null)
            ((TextView) findViewById(R.id.content)).setText(user.getUserName() + "欢迎你的使用");
        if (isopenupdate) {
            System.out.println("------------------updateapp---------------");
            checkVersion();
        }

    }

    @Override
    public void setListener() {
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        if (JSESSIONID != null) {
                            startActivity(new Intent(MainActivity.this, MobileAgainstBurglarsActivity.class));
                            overridePendingTransition(R.anim.welcome_fade_in, R.anim.welcome_fade_out);
                        } else {
                            Toast.makeText(MainActivity.this, "你还没有登录,请先登录", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(MainActivity.this, LoginActivity.class));
                            overridePendingTransition(R.anim.welcome_fade_in, R.anim.welcome_fade_out);
                        }
                        break;
                    case 1:
                        startActivity(new Intent(MainActivity.this, BlackListActivity.class));
                        overridePendingTransition(R.anim.welcome_fade_in, R.anim.welcome_fade_out);
                        break;
                    case 2:
                        startActivity(new Intent(MainActivity.this, SoftwareManagermentActivity.class));
                        overridePendingTransition(R.anim.welcome_fade_in, R.anim.welcome_fade_out);
                        break;
                    case 3:
                        startActivity(new Intent(MainActivity.this, ProccessManagerActivity.class));
                        overridePendingTransition(R.anim.welcome_fade_in, R.anim.welcome_fade_out);
                        break;
                    case 4:
                        startActivity(new Intent(MainActivity.this, TrafficManagerActivity.class));
                        overridePendingTransition(R.anim.welcome_fade_in, R.anim.welcome_fade_out);
                        break;
                    case 5:
                        startActivity(new Intent(MainActivity.this, AntivirusActivity.class));
                        overridePendingTransition(R.anim.welcome_fade_in, R.anim.welcome_fade_out);
                        break;
                    case 6:
                        startActivity(new Intent(MainActivity.this, CleanCacheActivity.class));
                        overridePendingTransition(R.anim.welcome_fade_in, R.anim.welcome_fade_out);
                        break;
                    case 7:
                        Toast.makeText(getApplicationContext(), "该功能还没开始", Toast.LENGTH_SHORT).show();
                        break;
                    case 8:
                        Toast.makeText(getApplicationContext(), "该功能还没开始", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(getApplicationContext(), "该功能还没开始", Toast.LENGTH_SHORT).show();
                        break;

                }

            }
        });
        ((ImageView) findViewById(R.id.ivmainsetting)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SettingActivity.class));
            }
        });

        /*btnRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  userLogin();
            }
        });*/
        ((ImageView) findViewById(R.id.comments)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CommentsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("JSESSIONID", JSESSIONID);
                bundle.putParcelable("user", user);
                intent.putExtra("data", bundle);
                startActivity(intent);
                overridePendingTransition(R.anim.welcome_fade_in, R.anim.welcome_fade_out);
            }
        });
    }

    public void startActivity(Context context, Class clazz) {
        if (clazz != null) {
            startActivity(new Intent(context, clazz));
        } else {
            Toast.makeText(context, "你要开启的窗体不存在", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            // 利用handler延迟发送更改状态信息
            mHandler.sendEmptyMessageDelayed(EXIT, 2000);
        } else {
            finish();
            System.exit(0);
        }
    }

    public void showUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(R.string.updatetips);
        builder.setMessage(getResources().getString(R.string.updateversion) + updation.getVersionName() + getResources().getString(R.string.isupdaste) + System.getProperty("line.separator")
                + updation.getDescription());
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                downloadAPK();
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void checkVersion() {
        final long time = System.currentTimeMillis();//获取系统的当前的时间
        new Thread() {
            @Override
            public void run() {
                HttpURLConnection conn = null;
                InputStream is = null;
                try {
                    URL url = new URL(Const.versionInfoUrl);
                    conn = (HttpURLConnection) url.openConnection();//还不是真正的连接
                    conn.setRequestMethod("GET");
                    conn.setReadTimeout(8000);
                    conn.setReadTimeout(5000);
                    conn.connect();//真正的连接
                    if (conn.getResponseCode() == 200) {
                        System.out.println("------------------download---------------");
                        is = conn.getInputStream();
                        StringBuilder builder = new StringBuilder();
                        byte[] buf = new byte[1024];
                        int len = 0;
                        while ((len = is.read(buf)) != -1) {
                            builder.append(new String(buf, 0, len));
                        }
                        System.out.println(builder.toString());
                        updation = JSONParserUtil.paserUpdationJson(builder.toString());
                        System.out.println("------------------updation---------------" + updation);
                        Message msg = Message.obtain();
                        if (updation.getVersionCode() > getVersionCode()) {
                            msg.what = UPDATEAPP;// 如果网络服务器端获取的版本号比当前的版本号要大，那么就发送消息弹出对话框提示更新
                            mHandler.sendMessage(msg);
                        } else {
                            msg.what = NORMAL;// 没有更新
                            mHandler.sendMessage(msg);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (is != null) {
                        try {
                            is.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (conn != null) {
                        conn.disconnect();
                    }
                }
            }
        }.start();
    }

    /**
     * 获取本应用的版本号
     *
     * @return返回版本号
     */
    private int getVersionCode() {
        PackageManager packageManager = getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);//获取应用的信息
            int versionCode = packageInfo.versionCode;
            return versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 根据应用的版本号决定下载更新应用的使用的是XUtils
     */
    private void downloadAPK() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
           /* File parent = getApplicationContext().getFilesDir();
            File file = new File(parent,"update.apk");*/
           File parent = Environment.getExternalStorageDirectory();
            File file = new File(parent,"update.apk");
            HttpUtils utils = new HttpUtils();
            utils.download(updation.getDownloadUrl(),file.getPath() ,new RequestCallBack<File>() {
                // 表示文件的下载进度
                @Override
                public void onLoading(long total, long current,
                                      boolean isUploading) {
                    // TODO Auto-generated method stub
                    super.onLoading(total, current, isUploading);
                    System.out.println("-----------------current-------------" + current + "--------------total------------" + total);
                }

                // 下载成功是调用
                @Override
                public void onSuccess(ResponseInfo<File> arg0) {
                    // TODO Auto-generated method stub
                    // Toast.makeText(SplashActivity.this, "下载成功!",
                    // Toast.LENGTH_SHORT).show();
                    // 下载成功就跳到系统提示安装的界面
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    intent.setDataAndType(Uri.fromFile(arg0.result),
                            "application/vnd.android.package-archive");
                    // startActivity(intent);
                    startActivityForResult(intent, 0);// 如果用户取消安装的话,
                    // 如果用户取消了安装就直接进入主界面
                }

                // 下载失败时调用
                @Override
                public void onFailure(HttpException arg0, String arg1) {
                    // TODO Auto-generated method stub
                    Toast.makeText(MainActivity.this, "下载失败!",
                            Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(MainActivity.this, "你没有SD卡", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    /**
     * 使用的是okhttp
     */
  @Deprecated
    public void downloadApk(){
        final Request request = new Request.Builder().url(updation.getDownloadUrl()).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    Toast.makeText(MainActivity.this,"下载失败",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                   boolean isOk = response.isSuccessful();
                 File parent = getApplicationContext().getFilesDir();
                   File file = new File(parent,"update.apk");
                    if(isOk){
                        InputStream is=null;
                        FileOutputStream fos=null;

                        /*if (Build.VERSION.SDK_INT >= 23) {
                            int REQUEST_CODE_CONTACT = 101;
                            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                            //验证是否许可权限
                            for (String str : permissions) {
                                if (MainActivity.this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                                    //申请权限
                                    MainActivity.this.requestPermissions(permissions, REQUEST_CODE_CONTACT);
                                   // return;
                                }
                            }
                        }*/
                        try{
                           is = response.body().byteStream();
                            fos = new FileOutputStream(file);
                            byte[] buf = new byte[1024*10];
                            int len;
                            while((len=is.read(buf))!=-1){
                                fos.write(buf,0,len);
                            }
                            Log.d("TAG","--------------------下载成功---------------");
                        }catch (IOException e){
                        e.printStackTrace();
                            Log.d("TAG","--------------------下载失败---------------");
                        }finally {
                            if (is!=null){
                                try{
                                    is.close();
                                }catch (IOException e2){
                                e2.printStackTrace();
                                }
                            }
                        }
                        if (fos!=null){
                            try{
                                fos.close();
                            }catch (IOException e2){
                                e2.printStackTrace();
                            }
                        }
                    }else{
                        Log.d("TAG","--------------------下载失败---------------");
                    }
                    }

        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101 && grantResults.length > 0) {
           boolean granted = grantResults[0] == PackageManager.PERMISSION_GRANTED;//是否授权，可以根据permission作为标记
            System.out.println("------------------granted-----------------"+granted);
        }
    }
}
