package com.mobileguard.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.mobileguard.activitys.R;
import com.mobileguard.adapter.BlackListMessageAdapter;
import com.mobileguard.domain.Message;
import com.mobileguard.service.MessageService;
import com.mobileguard.service.impl.ContactsServiceImpl;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * 黑名单的信息电话的切换的实现
 * Created by chendom on 2017/4/14 0014.
 */

public class BlackListInfoFragment extends Fragment {
    private static final BlackListInfoFragment InfoList = new BlackListInfoFragment();
    private List<Message> messages;
    private ListView lvMessages;
    private BlackListMessageAdapter adapter;
    private MessageService messageService;
    public static BlackListInfoFragment getInstance(){
        return InfoList;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initData(getActivity());
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.layout_blacklist_information,null);
        lvMessages = (ListView)layout.findViewById(R.id.lvmessges);
        if (messages.size()>0){
            adapter =  new BlackListMessageAdapter(getActivity(),messages);
            lvMessages.setAdapter(adapter);
        }else{
            lvMessages.setVisibility(View.GONE);
            ((TextView)layout.findViewById(R.id.msgtips)).setVisibility(View.VISIBLE);
        }
        lvMessages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        return layout;
    }
    private void initData(Context context){
        messages = new ArrayList<>();
        messageService = new ContactsServiceImpl(context);
        try {
            messages = messageService.findAllMessage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
