package com.example.customview.CustomCombinationView;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.customview.R;

public class LoginKeyboard  extends LinearLayout {

    private LoginNumPress myloginNumPress;

    public LoginKeyboard(Context context) {
        this(context,null);
    }

    public LoginKeyboard(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LoginKeyboard(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr,0);
    }

    public LoginKeyboard(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        View view= LayoutInflater.from(context).inflate(R.layout.num_key_pad,this,false);
        addView(view);
        initView();
    }

    private void initView() {
        TextView number_0 = findViewById(R.id.number_0);
        TextView number_1 = findViewById(R.id.number_1);
        TextView number_2 = findViewById(R.id.number_2);
        TextView number_3 = findViewById(R.id.number_3);
        TextView number_4 = findViewById(R.id.number_4);
        TextView number_5 = findViewById(R.id.number_5);
        TextView number_6 = findViewById(R.id.number_6);
        TextView number_7 = findViewById(R.id.number_7);
        TextView number_8 = findViewById(R.id.number_8);
        TextView number_9 = findViewById(R.id.number_9);
        TextView number_back = findViewById(R.id.number_back);
        number_0.setOnClickListener(new onclick());
        number_1.setOnClickListener(new onclick());
        number_2.setOnClickListener(new onclick());
        number_3.setOnClickListener(new onclick());
        number_4.setOnClickListener(new onclick());
        number_5.setOnClickListener(new onclick());
        number_6.setOnClickListener(new onclick());
        number_7.setOnClickListener(new onclick());
        number_8.setOnClickListener(new onclick());
        number_9.setOnClickListener(new onclick());
        number_back.setOnClickListener(new onclick());
    }

    public class onclick implements OnClickListener{

        @Override
        public void onClick(View v) {
            int buttonID=v.getId();
            if(myloginNumPress==null){
                return;
            }

            if(buttonID==R.id.number_back){
                myloginNumPress.backPress();
            }else {
                String num = ((TextView)v).getText().toString();
                myloginNumPress.onNumberPress(Integer.valueOf(num));
            }
        }
    }

    public void onLoginNumPressListener(LoginNumPress loginNumPress){

        this.myloginNumPress=loginNumPress;
    }

    public interface LoginNumPress{

        void onNumberPress(int num);

        void backPress();

    }

}
