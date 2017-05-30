package com.mobileguard.activitys;

import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.mobileguard.base.BaseActivity;
import com.mobileguard.service.BlackListService;

import org.w3c.dom.Text;

/**
 * 实现设置的窗体
 * Created by chendom on 2017/4/19 0019.
 */

public class SettingActivity extends BaseActivity {
    private TextView tvBangSim;
    private TextView tvOpenBlacklist;
    private TextView tvOpenUpdate;
    private CheckBox cbBangSim;
    private CheckBox cbOpenBlacklist;
    private CheckBox cbOpenUpdate;
    private SharedPreferences preferences;
    private boolean isBangSim;
    private boolean isOpenBlacklist;
    private boolean isoOpenUpdateApp;


    @Override
    public int getLayout() {
        return R.layout.activity_setting;
    }

    @Override
    public void initData() {
        preferences = getSharedPreferences("appdata", MODE_PRIVATE);
        isBangSim = preferences.getBoolean("isbangsim", false);
        isOpenBlacklist = preferences.getBoolean("isopenblacknumber", false);
        isoOpenUpdateApp = preferences.getBoolean("isopenupdate", false);
    }

    @Override
    public void initView() {
        tvBangSim = (TextView) findViewById(R.id.bangsim);
        cbBangSim = (CheckBox) findViewById(R.id.isbangsim);
        tvOpenBlacklist = (TextView) findViewById(R.id.openblacklist);
        cbOpenBlacklist = (CheckBox) findViewById(R.id.isonblacklist);
        cbOpenUpdate = (CheckBox) findViewById(R.id.updateapp);
        tvOpenUpdate = (TextView) findViewById(R.id.tvupdateapp);
        if (isBangSim) {
            tvBangSim.setText(getResources().getString(R.string.bangsim) + "(开)");
            cbBangSim.setChecked(true);
        } else {
            tvBangSim.setText(getResources().getString(R.string.bangsim) + "(关)");
            cbBangSim.setChecked(false);
        }
        if (isOpenBlacklist) {
            tvOpenBlacklist.setText(getResources().getString(R.string.blacklist) + "(开)");
            cbOpenBlacklist.setChecked(true);
        } else {
            tvOpenBlacklist.setText(getResources().getString(R.string.blacklist) + "(关)");
            cbOpenBlacklist.setChecked(false);
        }
        if (isoOpenUpdateApp) {
            tvOpenUpdate.setText(getResources().getText(R.string.updateapp) + "(开)");
            cbOpenUpdate.setChecked(true);
        } else {
            tvOpenUpdate.setText(getResources().getText(R.string.updateapp) + "(关)");
            cbOpenUpdate.setChecked(false);
        }
    }

    @Override
    public void setListener() {
        cbBangSim.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cbBangSim.isChecked()) {
                    tvBangSim.setText(getResources().getString(R.string.bangsim) + "(开)");
                    preferences.edit().putBoolean("isbangsim", true).commit();
                } else {
                    tvBangSim.setText(getResources().getString(R.string.bangsim) + "(关)");
                    preferences.edit().putBoolean("isbangsim", false).commit();
                }
            }
        });
        cbOpenBlacklist.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cbOpenBlacklist.isChecked()) {
                    tvOpenBlacklist.setText(getResources().getString(R.string.blacklist) + "(开)");
                    preferences.edit().putBoolean("isopenblacknumber", true).commit();
                    startService(new Intent(SettingActivity.this, BlackListService.class));
                } else {
                    tvOpenBlacklist.setText(getResources().getString(R.string.blacklist) + "(关)");
                    preferences.edit().putBoolean("isopenblacknumber", false).commit();
                    stopService(new Intent(SettingActivity.this, BlackListService.class));

                }
            }
        });
        cbOpenUpdate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cbOpenUpdate.isChecked()) {
                    tvOpenUpdate.setText(getResources().getString(R.string.updateapp) + "(开)");
                    preferences.edit().putBoolean("isopenupdate", true).commit();
                } else {
                    tvOpenUpdate.setText(getResources().getString(R.string.updateapp) + "(关)");
                    preferences.edit().putBoolean("isopenupdate", false).commit();
                }
            }
        });

    }
}
