package com.lypeer.eventbusdemo.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.lypeer.eventbusdemo.R;
import com.lypeer.eventbusdemo.model.MessageEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class Fragment2 extends Fragment {

    private Context context;
    private TextView textView;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context=context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment2,container,false);
        init(view);
        //粘性事件中注册订阅不能早于控件的注册
        //EventBus.getDefault().register(this);
        return view;
    }

    public void init(View view){
        textView = view.findViewById(R.id.fragment_textView);
    }
//    在粘性事件中使用
//    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
//    public void getEventBus1(MessageEvent messageEvent){
//        textView.setText(messageEvent.getMessage());
//        Log.i("zhouwei","在posting模式下"+Thread.currentThread().getName());
//    }

//    在粘性事件中使用
//    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
//    public void getEventBus1(MessageEvent messageEvent){
//        textView.setText(messageEvent.getMessage());
//        Log.i("zhouwei","在posting模式下"+Thread.currentThread().getName());
//    }

    @Subscribe(threadMode = ThreadMode.MAIN, priority = 1)
    public void getEventBus1(MessageEvent messageEvent){
        textView.setText(messageEvent.getMessage());
        Log.i("zhouwei","在priority 1模式下"+Thread.currentThread().getName());
    }

    @Subscribe(threadMode = ThreadMode.MAIN,priority = 2)
    public void getEventBus2(MessageEvent messageEvent){
        textView.setText(messageEvent.getMessage());
        Log.i("zhouwei","在priority 2模式下"+Thread.currentThread().getName());
    }

    @Subscribe(threadMode = ThreadMode.MAIN,priority = 3)
    public void getEventBus3(MessageEvent messageEvent){
        textView.setText(messageEvent.getMessage());
        Log.i("zhouwei","在priority 3模式下"+Thread.currentThread().getName());
    }

    @Subscribe(threadMode = ThreadMode.MAIN,priority = 4)
    public void getEventBus4(MessageEvent messageEvent){
        textView.setText(messageEvent.getMessage());
        Log.i("zhouwei","在priority 4模式下"+Thread.currentThread().getName());
    }

    @Subscribe(threadMode = ThreadMode.MAIN,priority = 5)
    public void getEventBus5(MessageEvent messageEvent){
        textView.setText(messageEvent.getMessage());
        Log.i("zhouwei","在priority 5模式下"+Thread.currentThread().getName());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
