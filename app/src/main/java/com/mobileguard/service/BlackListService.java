package com.mobileguard.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.internal.telephony.ITelephony;
import com.mobileguard.domain.Call;
import com.mobileguard.domain.Contact;
import com.mobileguard.service.impl.ContactsServiceImpl;
import com.mobileguard.utils.TimeUtil;

import java.lang.reflect.Method;
import java.util.Date;

/**拦截黑名单的黑名单的电话拦截的后台服务
 * @author chendoom
 *
 */
public class BlackListService extends Service {

    private TelephonyManager tm;
    private ContactsService contactsService;
    private CallService callService;
    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        contactsService = new ContactsServiceImpl(this);
        callService = new ContactsServiceImpl(this);
        tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        //监听电话状态
        tm.listen(new MyListener(), PhoneStateListener.LISTEN_CALL_STATE);//events:决定PhoneStateListener侦听什么内容
    }
    class MyListener extends PhoneStateListener{


        //一旦电话状态改变，此方法调用
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            // TODO Auto-generated method stub
            super.onCallStateChanged(state, incomingNumber);
            Log.d("TAG","----------------------------"+incomingNumber);
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:
                    Log.d("TAG","---------------空闲------------");
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    Log.d("TAG","----------------响铃----------");
                    try {
                       Contact contact =  contactsService.findContactByNumber(incomingNumber);
                        Log.d("TAG","--------------------------contacts---------------"+contact);
                        if(contact!=null){
                            Log.d("TAG","----------------挂断----------");
                            endCall();
                            boolean flag = callService.addCall(new Call(1,contact.getName(),contact.getTelephonenumber(),1, TimeUtil.getCuurentTime()));
                            Log.d("TAG","------------------flag--------"+flag);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    Log.d("TAG","----------------摘机----------");
                    break;

            }
        }

        private void endCall() {
            TelephonyManager telMag = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            Class<TelephonyManager> c = TelephonyManager.class;
            Method mthEndCall = null;
            try {
                mthEndCall = c.getDeclaredMethod("getITelephony", (Class[]) null);
                mthEndCall.setAccessible(true);
                ITelephony iTel = (ITelephony) mthEndCall.invoke(telMag,
                        (Object[]) null);
                iTel.endCall();

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

}