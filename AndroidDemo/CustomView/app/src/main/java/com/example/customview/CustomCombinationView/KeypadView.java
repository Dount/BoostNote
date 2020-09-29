package com.example.customview.CustomCombinationView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.customview.R;
import com.example.customview.Utils.SizeUtils;

public class KeypadView extends ViewGroup {

    private int mTextColor;
    private float mTextSize;
    private int mItemPressBgColor;
    private int mItemNormalBgColor;
    private int row =4;
    private int colume =3;
    private int mitemMargin;
    private setKeyPadViewItemListener msetKeyPadViewItemListener;


    public KeypadView(Context context) {
        this(context,null);
    }

    public KeypadView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public KeypadView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr,0);
    }

    public KeypadView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        //定义相关的属性
        initAttrs(context, attrs);
        setupItem();
    }

    private void setupItem(){
        removeAllViews();
        for (int i=0;i<11;i++){

            TextView item = new TextView(getContext());
            //内容
            if(i==10){
                item.setTag(true);
                item.setText("删除");
            }else {
                item.setTag(false);
                item.setText(String.valueOf(i));
            }
            //大小
            item.setTextSize(TypedValue.COMPLEX_UNIT_PX,mTextSize);
            //居中
            item.setGravity(Gravity.CENTER);
            //字体颜色
            item.setTextColor(mTextColor);
            //设置背景
            item.setBackground(providerItemBg());

            item.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(msetKeyPadViewItemListener!=null){
                       boolean isDelete= (boolean) v.getTag();
                       if(!isDelete){
                           String value = ((TextView)v).getText().toString();
                           msetKeyPadViewItemListener.getVlaue(Integer.valueOf(value));
                       }else {
                           msetKeyPadViewItemListener.deleteEvent();
                       }
                    }
                }
            });

            addView(item);
        }
    }

    private Drawable providerItemBg(){
        //创建Selector
        StateListDrawable bg =new StateListDrawable();

        //按下去BackGround 创建Drawable
        GradientDrawable pressDrawable =new GradientDrawable();
        pressDrawable.setColor(mItemPressBgColor);
        pressDrawable.setCornerRadius(SizeUtils.dip2px(getContext(),5));
        bg.addState(new int[]{android.R.attr.state_pressed},pressDrawable);

        //普通状态的BackGround 创建Drawable
        GradientDrawable normalDrawable =new GradientDrawable();
        normalDrawable.setColor(mItemNormalBgColor);
        normalDrawable.setCornerRadius(SizeUtils.dip2px(getContext(),5));
        bg.addState(new int[]{},normalDrawable);

        return  bg;
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.KeypadView);
        mTextColor =a.getColor(R.styleable.KeypadView_numberColor,context.getColor(R.color.white));
        mTextSize = a.getDimension(R.styleable.KeypadView_numberSize,-1);
        mItemPressBgColor = a.getColor(R.styleable.KeypadView_itemPressColor,context.getColor(R.color.key_item_color));
        mItemNormalBgColor = a.getColor(R.styleable.KeypadView_itemNormalColor,context.getColor(R.color.key_item_color));
        mitemMargin = a.getDimensionPixelSize(R.styleable.KeypadView_itemMargin, SizeUtils.dip2px(getContext(), 2));

        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width=MeasureSpec.getSize(widthMeasureSpec);
        int height=MeasureSpec.getSize(heightMeasureSpec);
        //一行三列，求出列宽，三等分.
        int perItemWidth =(width-(colume+1)*mitemMargin)/colume;
        int perItemHeight=(height-(row+1)*mitemMargin)/row;
        //测量孩子
        int normalWidthSpec = MeasureSpec.makeMeasureSpec(perItemWidth,MeasureSpec.EXACTLY);
        int deleteWidthSpec = MeasureSpec.makeMeasureSpec(perItemWidth*2+mitemMargin,MeasureSpec.EXACTLY);
        int heightSpec = MeasureSpec.makeMeasureSpec(perItemHeight,MeasureSpec.EXACTLY);
        for(int i=0;i<getChildCount();i++){
            View item =getChildAt(i);
            boolean isDelete = (boolean) item.getTag();
            item.measure(isDelete?deleteWidthSpec:normalWidthSpec,heightSpec);
        }

        //测量自己
        setMeasuredDimension(widthMeasureSpec,heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int left=mitemMargin,right,top,bottom;
        for(int i=0;i<getChildCount();i++){
            int rowIndex= i/colume;//求出当前的列
            int columeIndex=i%colume;//求出当前的行

            if(columeIndex ==0){
                left=mitemMargin;
            }

            View item=getChildAt(i);

            Log.i("zhouwei","i="+i+",rowIndex="+rowIndex+",columeIndex="+columeIndex);

            top =rowIndex*item.getMeasuredHeight() +mitemMargin*(rowIndex+1);

            right=left+item.getMeasuredWidth();

            bottom = top+item.getMeasuredHeight();

            Log.i("zhouwei","left="+left+",top="+top+",right="+right+",bottom="+bottom);

            item.layout(left,top,right,bottom);

            //目的是将当前的宽度给与下一个View;
            left+=item.getMeasuredWidth()+mitemMargin;
        }
    }

    public void onKeyPadViewItemListener(setKeyPadViewItemListener listener){
        this.msetKeyPadViewItemListener = listener;
    }

    public interface setKeyPadViewItemListener{
        void getVlaue(int value);

        void deleteEvent();
    }
}
