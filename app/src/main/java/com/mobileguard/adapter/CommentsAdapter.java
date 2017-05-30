package com.mobileguard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.mobileguard.activitys.CommentsActivity;
import com.mobileguard.activitys.R;
import com.mobileguard.constants.Const;
import com.mobileguard.domain.Comment;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 实现对用户评论的列表的适配
 * Created by chendom on 2017/4/27 0027.
 */

public class CommentsAdapter extends BaseAdapter {
    private Context context;
    private List<Comment> comments;
    private SimpleDateFormat format;
    private OkHttpClient client;
    private List<String> cookies;
    private String JSESSIONID;

    public CommentsAdapter(Context context, List<Comment> comments,String JSESSIONID) {
        this.context = context;
        this.comments = comments;
        this.JSESSIONID=JSESSIONID;
        format = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
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
    }

    @Override
    public int getCount() {
        return comments.size();
    }

    @Override
    public Object getItem(int position) {
        return comments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (viewHolder == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_comments_item_style, null);
            viewHolder.tvContent = (TextView) convertView.findViewById(R.id.praiseContent);
            viewHolder.btnPraise = (CheckBox) convertView.findViewById(R.id.btnPraise);
            viewHolder.commentTime = (TextView) convertView.findViewById(R.id.commentTime);
            viewHolder.tvPraiseTime = (TextView) convertView.findViewById(R.id.praiseTime);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final Comment comment = comments.get(position);
        viewHolder.tvContent.setText(comment.getCommContent());
        viewHolder.commentTime.setText(format.format(new Date(comment.getCommTime())));
        viewHolder.tvPraiseTime.setText(comment.getPraiseTime()+"");
        final ViewHolder finalViewHolder = viewHolder;
        viewHolder.btnPraise.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (finalViewHolder.btnPraise.isChecked()){
                    finalViewHolder.btnPraise.setChecked(true);
                   addPraise(comment.getCommId(),comment.getCommTime());
                    int time = Integer.parseInt(finalViewHolder.tvPraiseTime.getText().toString());
                    finalViewHolder.tvPraiseTime.setText((time+1)+"");
                }else{

                    finalViewHolder.btnPraise.setChecked(false);
                    dividePraise(comment.getCommId(),comment.getCommTime());
                    int time = Integer.parseInt(finalViewHolder.tvPraiseTime.getText().toString());
                    finalViewHolder.tvPraiseTime.setText((time-1)+"");
                }
            }
        });
        return convertView;
    }

    class ViewHolder {
        public TextView tvContent;
        public CheckBox btnPraise;
        public TextView commentTime;
        public TextView tvPraiseTime;

    }
    public void addPraise(String commId,long time){
        FormBody.Builder builder = new FormBody.Builder();
                 /* 添加两个参数,实现用户的登录*/
        builder.add("commId", commId).add("praiseTime", time+"");
        FormBody body = builder.build();
        final Request request;

        System.out.println("---------------JSESSIONID--------------------:" + JSESSIONID);
        if (JSESSIONID == null) {
            request = new Request.Builder().url(Const.addPraiseUrl).post(body).build();
        } else {
            request = new Request.Builder().url(Const.addPraiseUrl).addHeader("cookie", JSESSIONID).post(body).build();
        }
                /* 下边和get一样了 */
        client.newCall(request).enqueue(new Callback() {
            public void onResponse(Call call, final Response response) throws IOException {
                final String bodyStr = response.body().string();
                if (JSESSIONID == null) {
                    Headers headers = response.headers();
                    cookies = headers.values("set-cookie");
                    JSESSIONID = cookies.get(0);
                    System.out.println("onResponse-size: " + cookies);
                    JSESSIONID = JSESSIONID.substring(0, JSESSIONID.indexOf(";"));
                    System.out.println("session:" + JSESSIONID);
                }
                final boolean ok = response.isSuccessful();
                if (ok) {

                } else {
                    Toast.makeText(context, "server error : " + bodyStr, Toast.LENGTH_SHORT).show();
                }

            }

            public void onFailure(Call call, final IOException e) {
                Toast.makeText(context, "error : " + e.toString(), Toast.LENGTH_SHORT).show();

            }
        });


    }

    public void dividePraise(String commId,long time){
        FormBody.Builder builder = new FormBody.Builder();
                 /* 添加两个参数,实现用户的登录*/
        builder.add("commId", commId).add("praiseTime", time+"");
        FormBody body = builder.build();
        final Request request;

        System.out.println("---------------JSESSIONID--------------------:" + JSESSIONID);
        if (JSESSIONID == null) {
            request = new Request.Builder().url(Const.minusUrl).post(body).build();
        } else {
            request = new Request.Builder().url(Const.minusUrl).addHeader("cookie", JSESSIONID).post(body).build();
        }
                /* 下边和get一样了 */
        client.newCall(request).enqueue(new Callback() {
            public void onResponse(Call call, final Response response) throws IOException {
                final String bodyStr = response.body().string();
                if (JSESSIONID == null) {
                    Headers headers = response.headers();
                    cookies = headers.values("set-cookie");
                    JSESSIONID = cookies.get(0);
                    System.out.println("onResponse-size: " + cookies);
                    JSESSIONID = JSESSIONID.substring(0, JSESSIONID.indexOf(";"));
                    System.out.println("session:" + JSESSIONID);
                }
                final boolean ok = response.isSuccessful();
                if (ok) {

                } else {
                    Toast.makeText(context, "server error : " + bodyStr, Toast.LENGTH_SHORT).show();
                }
            }

            public void onFailure(Call call, final IOException e) {
                Toast.makeText(context, "error : " + e.toString(), Toast.LENGTH_SHORT).show();

            }
        });

    }
}
