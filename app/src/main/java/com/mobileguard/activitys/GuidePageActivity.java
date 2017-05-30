package com.mobileguard.activitys;

import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.mobileguard.base.BaseActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * 用户第一次使用的引导页的作用
 * Created by chendom on 2017/4/7 0007.
 */

public class GuidePageActivity extends BaseActivity {
    private ImageView iconImage;
    private Animation mFadeInScale;
    private SharedPreferences sharedPreferences;
    private boolean isFirst;

    @Override
    public int getLayout() {
        return R.layout.activity_guide;
    }

    @Override
    public void initData() {
        sharedPreferences = getSharedPreferences("appdata",MODE_PRIVATE);
        isFirst = sharedPreferences.getBoolean("isfirst" ,true);
        copyDB("antivirus.db");//拷贝病毒库到应用下:/data/data/<application package>/files
    }

    @Override
    public void initView() {
        iconImage = (ImageView) findViewById(R.id.mobile_guide);
        initAnim();

    }

    @Override
    public void setListener() {
        //实现对动画的监听
        mFadeInScale.setAnimationListener(new Animation.AnimationListener() {

            public void onAnimationStart(Animation animation) {
                System.out.println("scale animation is start");
                sharedPreferences.edit().putBoolean("isfirst",false).commit();
            }

            public void onAnimationRepeat(Animation animation) {
                System.out.println("scale animation is repeat");
            }

            public void onAnimationEnd(Animation animation) {
                System.out.println("scale animation is end");
                startActivity(new Intent(GuidePageActivity.this,LoginActivity.class));
                finish();
                overridePendingTransition(R.anim.welcome_fade_in,R.anim.welcome_fade_out);
            }
        });
        //判断是不是第一次的启动
        if(isFirst){
            iconImage.startAnimation(mFadeInScale);
        }else{
         startActivity(new Intent(GuidePageActivity.this,LoginActivity.class));
            finish();
            overridePendingTransition(R.anim.welcome_fade_in,R.anim.welcome_fade_out);
        }

    }

    /**
     * 初始化动画
     */
    private void initAnim() {
        mFadeInScale = AnimationUtils.loadAnimation(this, R.anim.welcome_fade_in_scale);
        mFadeInScale.setDuration(3000);
    }
    /**
     *拷贝数据库 (将归属地的数据库拷贝到指定的地方)
     */
    private void copyDB(String dbName) {
        // TODO Auto-generated method stub
        File desFile = new File(this.getFilesDir().getAbsolutePath(),dbName);//获取文件路径
        if(desFile.exists()){
            System.out.println("数据库已经存在");
            return ;

        }
        FileOutputStream out=null;
        InputStream is=null;
        try {
            is = getAssets().open(dbName);
            out = new FileOutputStream(desFile);
            int len=0;
            byte[] buff = new byte[1024];
            while((len=is.read(buff))!=-1){
                out.write(buff, 0, len);
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }finally{
            try {
                is.close();
                out.close();
            } catch (Exception e2) {
                // TODO: handle exception
            }

        }

    }
}
