package com.example.customview.CustomCombinationView;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.customview.R;

public class InputNumberView extends RelativeLayout {

    private int Number = 0;
    private TextView editText;
    private OnClickListenerInputNumberView onClickListenerInputNumberView;
    private int max;
    private int min;

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    private int step;
    private int defaultValure;
    private boolean disable;
    private int btnBgRes;
    private int valuesize;
    private Button minue_btn;
    private Button plus_btn;

    public InputNumberView(Context context) {
        this(context, null);
    }

    public InputNumberView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public InputNumberView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public InputNumberView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initAttr(context, attrs);

//      LayoutInflater.from(context).inflate(R.layout.input_number_view,this,true);
//
//      LayoutInflater.from(context).inflate(R.layout.input_number_view,this);

        //上面方法与下面等效
        View view = LayoutInflater.from(context).inflate(R.layout.input_number_view, this, false);

        addView(view);

        editText = view.findViewById(R.id.edt_num);
        editText.setText(String.valueOf(defaultValure));
        minue_btn = view.findViewById(R.id.btn_minue);
        minue_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                plus_btn.setEnabled(true);
                Number-=step;
                if(min!=0 && Number<min){
                    Number=min;
                    minue_btn.setEnabled(false);
                }
                updateText(Number);
            }
        });

        plus_btn = view.findViewById(R.id.btn_plus);
        plus_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                minue_btn.setEnabled(true);
                Number+=step;
                if(max!=0 && Number>max){
                    Number=max;
                    plus_btn.setEnabled(false);
                }
                updateText(Number);
            }
        });
    }

    private void initAttr(Context context, AttributeSet attrs) {
        //获取相关属性
        TypedArray a =context.obtainStyledAttributes(attrs,R.styleable.InputNumberView);
        max = a.getInt(R.styleable.InputNumberView_max, 0);
        min = a.getInt(R.styleable.InputNumberView_min,0);
        step = a.getInt(R.styleable.InputNumberView_step, 0);
        defaultValure = a.getInt(R.styleable.InputNumberView_defaultvalue, 0);
        disable = a.getBoolean(R.styleable.InputNumberView_disable,false);
        btnBgRes = a.getResourceId(R.styleable.InputNumberView_btnBackground, -1);
        valuesize = a.getInt(R.styleable.InputNumberView_valueSize,0);
        //进行回收
        a.recycle();
    }

    private void updateText(int num){
        editText.setText(String.valueOf(num));
        if(onClickListenerInputNumberView!=null){
            onClickListenerInputNumberView.onClick(this.Number);
        }
    }


    public void setOnClickInputNumberViewListener(OnClickListenerInputNumberView listener){
        this.onClickListenerInputNumberView=listener;
    }

    public interface OnClickListenerInputNumberView{
        void onClick(int value);

    }
}
