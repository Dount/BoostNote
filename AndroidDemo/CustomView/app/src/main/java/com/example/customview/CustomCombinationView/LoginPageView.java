package com.example.customview.CustomCombinationView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputFilter;
import android.text.SpannableString;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.customview.R;

import java.lang.reflect.Field;

public class LoginPageView extends FrameLayout {

    private int color;
    private int verifyCodeSize;
    private OngetNumberListener ongetNumberListener;
    private LoginKeyboard loginKeyboard;

    public LoginPageView(Context context) {
        this(context,null);
    }

    public LoginPageView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LoginPageView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr,0);
    }

    public LoginPageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        //初始化属性
        initattr(context, attrs);
        //初始化View
        initView(context);
    }

    private void initattr(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LoginPageView);
        color = a.getColor(R.styleable.LoginPageView_maincolor,-1);
        verifyCodeSize = a.getInt(R.styleable.LoginPageView_verifyCodeSize,11);
        a.recycle();
    }

    private void initView(Context context){
        View view=LayoutInflater.from(context).inflate(R.layout.login_page_view,this,false);
        addView(view);

        EditText editText =findViewById(R.id.phone_num);
        SpannableString s =new SpannableString("请输入"+verifyCodeSize+"位手机号");
        editText.setHint(s);
        //设置editText最大长度
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(verifyCodeSize){}});
        //当获取焦点时，不再弹出键盘。
        editText.setShowSoftInputOnFocus(false);
        //取消赋值粘贴功能
        disableCopyAndPaste(editText);
        loginKeyboard = findViewById(R.id.numpageview);
        loginKeyboard.onLoginNumPressListener(new LoginKeyboard.LoginNumPress() {
            @Override
            public void onNumberPress(int num) {
                EditText focusEdt =getFocusEdt();
                if(focusEdt!=null) {
                    Editable text = focusEdt.getText();
                    //获取光标最后的位置
                    int index = focusEdt.getSelectionEnd();
                    text.insert(index, String.valueOf(num));
                }
            }

            @Override
            public void backPress() {
                //获取当前焦点
                EditText focusEdt =getFocusEdt();
                if(focusEdt!=null) {
                    //当前焦点的最后一位
                   int index =focusEdt.getSelectionEnd();
                   Editable editable =focusEdt.getText();
                   if(index>0){
                       //参数1 开始位置
                       //参数2 结束位置
                       editable.delete(index-1,index);
                   }
                }
            }
        });
    }


    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getVerifyCodeSize() {
        return verifyCodeSize;
    }

    public void setVerifyCodeSize(int verifyCodeSize) {
        this.verifyCodeSize = verifyCodeSize;
    }

    public void setOngetNumberListener(OngetNumberListener instance){
        this.ongetNumberListener=instance;
    }

    public interface OngetNumberListener{
        void getNumber(int num);
    }

    //获取当前有焦点的输入框
    private EditText getFocusEdt(){
        View view=this.findFocus();
        if(view instanceof EditText){
            return (EditText) view;
        }
        return null;
    }

    //取消赋值粘贴
    @SuppressLint("ClickableViewAccessibility")
    public void disableCopyAndPaste(final EditText editText) {
        try {
            if (editText == null) {
                return ;
            }

            editText.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return true;
                }
            });
            editText.setLongClickable(false);
            editText.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        // setInsertionDisabled when user touches the view
                        setInsertionDisabled(editText);
                    }

                    return false;
                }
            });
            editText.setCustomSelectionActionModeCallback(new ActionMode.Callback() {
                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    return false;
                }

                @Override
                public void onDestroyActionMode(ActionMode mode) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setInsertionDisabled(EditText editText) {
        try {
            Field editorField = TextView.class.getDeclaredField("mEditor");
            editorField.setAccessible(true);
            Object editorObject = editorField.get(editText);

            // if this view supports insertion handles
            Class editorClass = Class.forName("android.widget.Editor");
            Field mInsertionControllerEnabledField = editorClass.getDeclaredField("mInsertionControllerEnabled");
            mInsertionControllerEnabledField.setAccessible(true);
            mInsertionControllerEnabledField.set(editorObject, false);

            // if this view supports selection handles
            Field mSelectionControllerEnabledField = editorClass.getDeclaredField("mSelectionControllerEnabled");
            mSelectionControllerEnabledField.setAccessible(true);
            mSelectionControllerEnabledField.set(editorObject, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
