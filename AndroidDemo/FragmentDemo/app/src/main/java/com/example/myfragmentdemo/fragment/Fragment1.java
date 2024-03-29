package com.example.myfragmentdemo.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myfragmentdemo.Interface.FragmentToActivity;
import com.example.myfragmentdemo.Interface.FragmentToFragment;
import com.example.myfragmentdemo.R;

public class Fragment1 extends Fragment {

    private FragmentToActivity fragmentToActivity;
    private FragmentToFragment fragmentToFragment;
    private TextView textView1;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        fragmentToActivity= (FragmentToActivity) context;
        fragmentToFragment= (FragmentToFragment) context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_one,container,false);
        TextView textView= view.findViewById(R.id.fragment1_text);
        textView1 = view.findViewById(R.id.fragment1_text2);
        Button button=view.findViewById(R.id.fragment1_button);
        Button button1=view.findViewById(R.id.fragment1_button2);
        button.setOnClickListener(new button());
        button1.setOnClickListener(new button());
        Bundle bundle=getArguments();
        if(bundle!=null){
            String s=bundle.getString("one","null");
            textView.setText(s);
        }
        return view;
    }

    public class  button implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.fragment1_button:
                    fragmentToActivity.message("来自Fragment1");
                    break;
                case R.id.fragment1_button2:
                    fragmentToFragment.sendFragmenttwoMessage("来自Fragment1的消息");
                    break;
            }
        }
    }

    public void setText(String msg){
        textView1.setText(msg);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        fragmentToFragment=null;
        fragmentToActivity=null;
    }
}
