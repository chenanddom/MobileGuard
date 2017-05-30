package com.mobileguard.activitys;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.mobileguard.base.BaseActivity;

/**
 * 实现手机防盗的功能
 * Created by chendom on 2017/4/9 0009.
 */

public class MobileAgainstBurglarsActivity extends BaseActivity {
    private TextView tvSafeNumber;
    private CheckBox cbIsOpenAgainstBurglars;
    private String number;
    private SharedPreferences preferences;
    private String simPhone;

    @Override
    public int getLayout() {
        return R.layout.activity_mobile_against_burglars;
    }

    @Override
    public void initData() {
        preferences = getSharedPreferences("appdata", MODE_PRIVATE);
        number = preferences.getString("safenumber", null);
        simPhone = preferences.getString("simphone", null);
    }

    @Override
    public void initView() {
        tvSafeNumber = (TextView) findViewById(R.id.safe_number);
        cbIsOpenAgainstBurglars = (CheckBox) findViewById(R.id.open_against_burglars);
        if (!TextUtils.isEmpty(number)) {
            tvSafeNumber.setText(number);
        }
        if (!TextUtils.isEmpty(simPhone)) {
            cbIsOpenAgainstBurglars.setChecked(true);
        } else {
            cbIsOpenAgainstBurglars.setChecked(false);
        }
        if (Build.VERSION.SDK_INT >= 23) {
            int REQUEST_CODE_CONTACT = 101;
            String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_CONTACTS};
            //验证是否许可权限
            for (String str : permissions) {
                if (MobileAgainstBurglarsActivity.this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    //申请权限
                    MobileAgainstBurglarsActivity.this.requestPermissions(permissions, REQUEST_CODE_CONTACT);
                    // return;
                }
            }
        }
    }

    @Override
    public void setListener() {
        tvSafeNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MobileAgainstBurglarsActivity.this, ContactsActivity.class), 0x123);
                overridePendingTransition(R.anim.welcome_fade_in, R.anim.welcome_fade_out);
            }
        });
        cbIsOpenAgainstBurglars.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cbIsOpenAgainstBurglars.isChecked()) {
                    cbIsOpenAgainstBurglars.setChecked(true);
                    // 获取当前手机的sim卡
                    TelephonyManager tm = (TelephonyManager) MobileAgainstBurglarsActivity.this
                            .getSystemService(MobileAgainstBurglarsActivity.this.TELEPHONY_SERVICE);
                    String currentSim = tm.getSimSerialNumber();
                    preferences.edit().putString("simphone", currentSim).commit();

                } else {
                    cbIsOpenAgainstBurglars.setChecked(false);
                    preferences.edit().putString("simphone", null).commit();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            number = data.getStringExtra("safenumber");
            tvSafeNumber.setText(number);
            preferences.edit().putString("safenumber", number).commit();
            Toast.makeText(MobileAgainstBurglarsActivity.this, "" + number, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 101 && grantResults.length > 0) {
            boolean granted = grantResults[0] == PackageManager.PERMISSION_GRANTED;//是否授权，可以根据permission作为标记
            System.out.println("------------------granted-----------------" + granted);
        }
    }
}
