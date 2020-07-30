package com.lypeer.eventbusdemo.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.lypeer.eventbusdemo.R;
import com.lypeer.eventbusdemo.model.MessageEvent;

import org.greenrobot.eventbus.EventBus;

public class Fragment1 extends Fragment {

    private MessageEvent messageEvent;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment1,container,false);
        messageEvent = new MessageEvent("收到Fragment的信息");
        init(view);
        return view;
    }

    public void init(View view){
        Button button=view.findViewById(R.id.fragment_Button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //粘性事件使用
                //EventBus.getDefault().postSticky(messageEvent);
                EventBus.getDefault().post(messageEvent);
            }
        });
    }
}
