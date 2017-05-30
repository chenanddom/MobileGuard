package com.mobileguard.activitys;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.mobileguard.adapter.BlackListAdapter;
import com.mobileguard.base.BaseActivity;
import com.mobileguard.domain.Contact;
import com.mobileguard.service.BlackListService;
import com.mobileguard.service.ContactsService;
import com.mobileguard.service.impl.ContactsServiceImpl;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * 黑名单的设置
 * Created by chendom on 2017/4/14 0014.
 */

public class BlackListSettingActivity extends BaseActivity {
    private ListView lvBlackList;
    private SharedPreferences preferences;
    private boolean isOpenBlackNumber;
    private ContactsService contactsService;
    private List<Contact> contacts;
    private CheckBox[] cbs;
    private BlackListAdapter adapter;

    @Override
    public int getLayout() {
        return R.layout.activity_blacklist_setting;
    }

    @Override
    public void initData() {
        preferences = getSharedPreferences("appdata", MODE_PRIVATE);

        contactsService = new ContactsServiceImpl(BlackListSettingActivity.this);
        try {
            contacts = contactsService.findContact();

        } catch (Exception e) {
            e.printStackTrace();
        }
        cbs = new CheckBox[contacts.size()];
    }

    @Override
    public void initView() {

        lvBlackList = (ListView) findViewById(R.id.blacklist_numbers);
        adapter = new BlackListAdapter(BlackListSettingActivity.this, contacts);
        lvBlackList.setAdapter(adapter);
        isOpenBlackNumber = preferences.getBoolean("isopenblacknumber", false);
        if (isOpenBlackNumber) {
            ((CheckBox) findViewById(R.id.isOpenBlackList)).setChecked(true);
        } else {
            ((CheckBox) findViewById(R.id.isOpenBlackList)).setChecked(false);
        }
    }

    @Override
    public void setListener() {
        ((CheckBox) findViewById(R.id.isOpenBlackList)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d("TAG", "----------------------" + ((CheckBox) findViewById(R.id.isOpenBlackList)).isChecked());
                if (((CheckBox) findViewById(R.id.isOpenBlackList)).isChecked()) {
                    if (Build.VERSION.SDK_INT >= 23) {
                        int REQUEST_CODE_CONTACT = 102;
                        String[] permissions = {Manifest.permission.RECEIVE_SMS,Manifest.permission.READ_SMS,
                                Manifest.permission.SEND_SMS,Manifest.permission.WRITE_CONTACTS,
                                Manifest.permission.READ_PHONE_STATE,Manifest.permission.CALL_PHONE};
                        //验证是否许可权限
                        for (String str : permissions) {
                            if (BlackListSettingActivity.this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                                //申请权限
                                BlackListSettingActivity.this.requestPermissions(permissions, REQUEST_CODE_CONTACT);
                                // return;
                            }
                        }
                    }
                    ((CheckBox) findViewById(R.id.isOpenBlackList)).setChecked(true);
                    preferences.edit().putBoolean("isopenblacknumber", true).commit();
                    startService(new Intent(BlackListSettingActivity.this, BlackListService.class));
                } else {
                    ((CheckBox) findViewById(R.id.isOpenBlackList)).setChecked(false);
                    preferences.edit().putBoolean("isopenblacknumber", false).commit();
                    stopService(new Intent(BlackListSettingActivity.this, BlackListService.class));
                }
            }
        });
        lvBlackList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(BlackListSettingActivity.this, "" + position, Toast.LENGTH_SHORT).show();


            }
        });
        lvBlackList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //int length = cbs.length;
               /* if(position>length-1){
                    //cbs=null;//将原来的内存释放
                    cbs = new CheckBox[cbs.length+1];
                    cbs[position] = (CheckBox) view.findViewById(R.id.isChecked);
                    cbs[position].setVisibility(View.VISIBLE);
                }else{*/
                cbs[position] = (CheckBox) view.findViewById(R.id.isChecked);
                cbs[position].setVisibility(View.VISIBLE);
                //   }

                return false;
            }
        });
        ((ImageButton) findViewById(R.id.btn_addnumber)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();

            }
        });
        Log.d("TAG", "------------------------------cbslength-----------------------" + cbs.length);
        ((ImageView) findViewById(R.id.delete)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag;
                for (int i = 0; i < cbs.length; i++) {
                    if (cbs[i] != null) {
                        if (cbs[i].isChecked()) {
                            try {
                                //flag = contactsService.deleteContact(contacts.get(i).get_id());
                                flag = contactsService.deleteContactByNameAndNumber(contacts.get(i));
                                contacts.remove(i);
                                cbs = null;
                                cbs = new CheckBox[contacts.size()];
                                adapter.notifyDataSetChanged();
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(BlackListSettingActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
                            }
                        } else {

                        }
                    }
                }
            }
        });
    }

    public void showDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        View view = getLayoutInflater().inflate(R.layout.dialog_choose, null);
        TextView tvTitle = (TextView) view.findViewById(R.id.mtitle);
        TextView tvHandle = (TextView) view.findViewById(R.id.operation_handle);
        TextView tvChoose = (TextView) view.findViewById(R.id.operation_choose);

        final AlertDialog dialog = builder.create();
        tvHandle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogInput();
                dialog.dismiss();
            }
        });
        tvChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(BlackListSettingActivity.this, ContactsActivity.class), 0x123);
                dialog.dismiss();
            }
        });
        dialog.setView(view);
        dialog.show();


    }

    public void showDialogInput() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("添加联系人");
        View view = getLayoutInflater().inflate(R.layout.dialog_input, null);
        final EditText editBlackName = (EditText) view.findViewById(R.id.blackname);
        final EditText editBlackNumber = (EditText) view.findViewById(R.id.blacknumber);

        builder.setView(view);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String blackName = editBlackName.getText().toString().trim();
                String blackNumber = editBlackNumber.getText().toString().trim();
                Contact contact = new Contact();
                contact.setName(blackName);
                contact.setTelephonenumber(blackNumber);
                try {

                    boolean flag = contactsService.addContact(contact);
                    if (flag) {
                        contacts.add(contact);
                        adapter.notifyDataSetChanged();
                        cbs=null;
                        cbs = new CheckBox[contacts.size()];
                        Toast.makeText(BlackListSettingActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(BlackListSettingActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            String name = data.getStringExtra("name");
            String number = data.getStringExtra("safenumber");
            number = number.replace(" ","").trim();
            System.out.println("result:"+number+"-------------"+number);
            Contact contact = new Contact();
            contact.setName(name);
            contact.setTelephonenumber(number);
            try {
                boolean flag = contactsService.addContact(contact);
                contacts.add(contact);
                cbs = null;
                cbs = new CheckBox[contacts.size()];
                adapter.notifyDataSetChanged();
                if (flag) {
                    Toast.makeText(BlackListSettingActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 102 && grantResults.length > 0) {
            boolean granted = grantResults[0] == PackageManager.PERMISSION_GRANTED;//是否授权，可以根据permission作为标记
            System.out.println("------------------granted-----------------"+granted);
        }
    }
}
