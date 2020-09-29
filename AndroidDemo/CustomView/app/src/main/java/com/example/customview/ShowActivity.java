package com.example.customview;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.customview.CustomCombinationView.InputNumberView;
import com.example.customview.CustomCombinationView.KeypadView;
import com.example.customview.CustomCombinationView.LoginPageView;
import com.example.customview.CustomViewGroup.FlowLayout;
import com.example.customview.CustomViewGroup.SlideMenuView;

import java.util.ArrayList;
import java.util.List;

public class ShowActivity extends AppCompatActivity {

    private InputNumberView myinputnumber;
    private LoginPageView myloginview;
    private FlowLayout mFlowLayout;
    private KeypadView myKeyPadLayout;
    private SlideMenuView mSlideMenuView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置全屏显示
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_show);
        initView();

        Intent intent=getIntent();
        String module=intent.getStringExtra("module");
        showView(module);
    }

    private void initView() {
        myinputnumber = findViewById(R.id.myinputNumber);
        myloginview = findViewById(R.id.myloginView);
        mFlowLayout = findViewById(R.id.myflow);
        myKeyPadLayout = findViewById(R.id.mykeypad);
        mSlideMenuView = findViewById(R.id.mSlideMenuView);
    }

    public void showView(String module){
        if(module.equals("inputnumberView")){
            ShowInputNumberView();
        }
        if(module.equals("loginkeyboard")){
            ShowMyLoginView();
        }
        if(module.equals("FlowLayout")){
            ShowFlowLayout();
        }
        if(module.equals("KeyPadView")){
            showKeyPad();
        }
        if(module.equals("SlideMenuView")){
            showSlideMenuView();
        }
    }


    private void ShowInputNumberView() {
        myinputnumber.setVisibility(View.VISIBLE);
        myloginview.setVisibility(View.GONE);
        myinputnumber = this.findViewById(R.id.myinputNumber);
        myinputnumber.setOnClickInputNumberViewListener(new InputNumberView.OnClickListenerInputNumberView() {
            @Override
            public void onClick(int value) {
                if(value==myinputnumber.getMax()){
                    Toast.makeText(ShowActivity.this,"达到最大值",Toast.LENGTH_SHORT).show();
                }
                if(value==myinputnumber.getMin()){
                    Toast.makeText(ShowActivity.this,"达到最小值",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void ShowMyLoginView(){
        myloginview.setVisibility(View.VISIBLE);
        myinputnumber.setVisibility(View.GONE);
    }

    private void ShowFlowLayout(){
        myloginview.setVisibility(View.GONE);
        myinputnumber.setVisibility(View.GONE);
        mFlowLayout.setVisibility(View.VISIBLE);
        List<String> data=new ArrayList<>();
        data.add("科比");
        data.add("詹姆斯");
        data.add("杜兰特");
        data.add("哈登");
        data.add("欧文");
        data.add("维斯布鲁");
        data.add("乔丹");
        data.add("皮蓬");
        data.add("奥拉朱旺");
        data.add("姚明");
        data.add("奥尼尔");
        data.add("麦迪");
        mFlowLayout.setTextList(data);
        mFlowLayout.setOnItemClickListener(new FlowLayout.OnItemClickListener() {
            @Override
            public void onclick(View s, String text) {
                Toast.makeText(ShowActivity.this,text,Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void showKeyPad(){
        myloginview.setVisibility(View.GONE);
        myinputnumber.setVisibility(View.GONE);
        mFlowLayout.setVisibility(View.GONE);
        myKeyPadLayout.setVisibility(View.VISIBLE);
        myKeyPadLayout.onKeyPadViewItemListener(new KeypadView.setKeyPadViewItemListener() {
            @Override
            public void getVlaue(int value) {
                Toast.makeText(ShowActivity.this,"value="+value,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void deleteEvent() {
                Toast.makeText(ShowActivity.this,"Delete",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showSlideMenuView(){
        myloginview.setVisibility(View.GONE);
        myinputnumber.setVisibility(View.GONE);
        mFlowLayout.setVisibility(View.GONE);
        myKeyPadLayout.setVisibility(View.GONE);
        mSlideMenuView.setVisibility(View.GONE);
        mSlideMenuView.setOnEditClickListener(new SlideMenuView.OnEditClickListener() {
            @Override
            public void onReadClick() {
                Log.i("zhouwei","点击Read");
            }

            @Override
            public void onTopClick() {
                Log.i("zhouwei","点击TopClick");
            }

            @Override
            public void onDeleteClick() {
                Log.i("zhouwei","点击Delete");
            }
        });
    }
}
