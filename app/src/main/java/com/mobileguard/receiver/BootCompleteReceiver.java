package com.mobileguard.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.widget.Toast;

/**实现对sim卡的绑定
 * Created by chendom on 2017/5/11 0011.
 */

public class BootCompleteReceiver extends BroadcastReceiver {
    private SharedPreferences mPreferences;
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub

        mPreferences = context.getSharedPreferences("appdata", context.MODE_PRIVATE);
        String sim = mPreferences.getString("simphone", null);// 获取绑定的sim卡

        if (!TextUtils.isEmpty(sim)) {
            // 获取当前手机的sim卡
            TelephonyManager tm = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            String currentSim = tm.getSimSerialNumber() ;

            if (sim.equals(currentSim)) {
                Toast.makeText(context, ""+"手机安全", Toast.LENGTH_SHORT).show();

            } else {
                String phone = mPreferences.getString("safenumber", null);
                Toast.makeText(context, currentSim+"sim卡已经变化, 发送报警短信!!!", Toast.LENGTH_SHORT).show();
                //发送短息的逻辑
                SmsManager smsManager=SmsManager.getDefault();
                smsManager.sendTextMessage(phone, null, "sim card changed!!!", null, null);
            }
        }
    }
}


