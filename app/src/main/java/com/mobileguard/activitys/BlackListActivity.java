package com.mobileguard.activitys;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.mobileguard.base.BaseActivity;
import com.mobileguard.domain.Contact;
import com.mobileguard.fragments.BlackListInfoFragment;
import com.mobileguard.fragments.BlackListTelephoneFragment;
import com.mobileguard.service.ContactsService;
import com.mobileguard.service.impl.ContactsServiceImpl;

import java.util.List;

/**
 * 实现黑名单的功能
 * Created by chendom on 2017/4/13 0013.
 */

public class BlackListActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {
    private RadioButton rbInfo;
    private RadioButton rbTelephone;

    private Fragment blackInfo;
    private Fragment blackTelephone;
    private ImageView imgSetting;


    @Override
    public int getLayout() {
        return R.layout.activity_blacklist;
    }

    @Override
    public void initData(){
        ContactsService service = new ContactsServiceImpl(BlackListActivity.this);
        /*for (int i = 0; i < 10; i++) {;
            Contact contact = new Contact();
            contact.setName("zhangsan" + i);
            contact.setTelephonenumber("213231232" + i);
            try {
                boolean flag = service.addContact(contact);
                Log.d("TAG","--------------------------falg:"+flag);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/
        try {
            List<Contact> contactList = service.findContact();
            Log.d("TAG","-----------------list:"+contactList);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void initView() {
        rbInfo = (RadioButton) findViewById(R.id.rbInfo);
        rbTelephone = (RadioButton) findViewById(R.id.rbTelephone);
        blackInfo = BlackListInfoFragment.getInstance();
        blackTelephone = BlackListTelephoneFragment.getInstance();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        if (rbInfo.isChecked()) {
            transaction.replace(R.id.blacklist_content, blackInfo).commit();
        } else {
            transaction.replace(R.id.blacklist_content, blackTelephone).commit();
        }
    }

    @Override
    public void setListener() {
        rbInfo.setOnCheckedChangeListener(this);
        rbTelephone.setOnCheckedChangeListener(this);
        ((ImageView)findViewById(R.id.blacklist_icon)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BlackListActivity.this,BlackListSettingActivity.class));
                overridePendingTransition(R.anim.welcome_fade_in,R.anim.welcome_fade_out);
            }
        });

    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        switch (buttonView.getId()) {
            case R.id.rbInfo:
                if (rbInfo.isChecked()) {
                    System.out.println("--------------1111--------------------");
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction transaction = fm.beginTransaction();
                    if (blackInfo == null) {
                        blackInfo = BlackListInfoFragment.getInstance();
                        transaction.replace(R.id.blacklist_content, blackInfo).commit();
                    } else {
                        transaction.replace(R.id.blacklist_content, blackInfo).commit();
                    }
                }
                break;
            case R.id.rbTelephone:
                if (rbTelephone.isChecked()) {
                    System.out.println("--------------2222--------------------");

                    FragmentManager fm2 = getFragmentManager();
                    FragmentTransaction transaction2 = fm2.beginTransaction();

                    if (blackTelephone == null) {
                        blackTelephone = BlackListTelephoneFragment.getInstance();
                        transaction2.replace(R.id.blacklist_content, blackTelephone).commit();
                    }
                    transaction2.replace(R.id.blacklist_content, blackTelephone).commit();
                }
                break;
            default:
                break;
        }

    }
}
