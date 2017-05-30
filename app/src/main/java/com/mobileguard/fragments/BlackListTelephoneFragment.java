package com.mobileguard.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobileguard.activitys.R;
import com.mobileguard.adapter.BlackListCallAdapter;
import com.mobileguard.domain.Call;
import com.mobileguard.service.CallService;
import com.mobileguard.service.impl.ContactsServiceImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * 黑名单的电话列表的实现
 * Created by chendom on 2017/4/14 0014.
 */

public class BlackListTelephoneFragment extends Fragment {
    private  static final BlackListTelephoneFragment telePhoneNum = new BlackListTelephoneFragment();
    ListView lvCallNumber;
    private CallService callService;
    private List<Call> list;
    private BlackListCallAdapter adapter;

public static BlackListTelephoneFragment getInstance(){
    return telePhoneNum;
}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initData(getActivity());
        RelativeLayout layout = (RelativeLayout)inflater.inflate(R.layout.layout_blacklist_telephone,null);

        lvCallNumber = (ListView)layout.findViewById(R.id.call_numbers);
        if(list.size()>0){
            adapter = new BlackListCallAdapter(getActivity(),list);
            lvCallNumber.setAdapter(adapter);
        }else{
            lvCallNumber.setVisibility(View.GONE);
            ((TextView)layout.findViewById(R.id.call_tips)).setVisibility(View.VISIBLE);
        }
        lvCallNumber.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });


        return layout;
    }
    public void initData(Context context){
       /* for (int i = 0;i<10;i++)
            try {

                Log.d("TAG","---------------flag------------------"+flag);
            } catch (Exception e) {
                e.printStackTrace();
            }*/

        list = new ArrayList<>();
        callService = new ContactsServiceImpl(context);
        try {
            list = callService.findAllCall();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
