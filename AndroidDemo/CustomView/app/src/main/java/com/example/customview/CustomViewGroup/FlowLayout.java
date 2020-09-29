package com.example.customview.CustomViewGroup;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.customview.R;
import com.example.customview.Utils.SizeUtils;

import java.util.ArrayList;
import java.util.List;

public class FlowLayout extends ViewGroup {

    private int mMaxLines;
    private float mHorizontalMargin;
    private float mVerticalMargin;
    private int mTextMaxLength;
    private int mTextColor;
    private int mBorderColor;
    private float mBorderRadius;
    private ArrayList<String> data=new ArrayList<>();
    private List<List<View>> mLines =new ArrayList<>();
    private OnItemClickListener onItemClickListener=null;

    public FlowLayout(Context context) {
        this(context,null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr,0);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        //获取属性
        TypedArray type =context.obtainStyledAttributes(attrs, R.styleable.FlowLayout);
        mMaxLines =type.getInt(R.styleable.FlowLayout_maxLine,3);
        mHorizontalMargin =type.getDimension(R.styleable.FlowLayout_itemHorizontalMargin, SizeUtils.dip2px(getContext(),5f));
        mVerticalMargin = type.getDimension(R.styleable.FlowLayout_itemVerticalMargin,SizeUtils.dip2px(getContext(),10f));
        mTextMaxLength =type.getInt(R.styleable.FlowLayout_maxLine, SizeUtils.dip2px(getContext(),20));
        mTextColor = type.getColor(R.styleable.FlowLayout_textColor,getResources().getColor(R.color.text_gray));
        mBorderColor = type.getColor(R.styleable.FlowLayout_borderColor,getResources().getColor(R.color.text_gray));
        mBorderRadius =type.getDimension(R.styleable.FlowLayout_borderRadios, SizeUtils.dip2px(getContext(),5));

        type.recycle();

    }

    public void setTextList(List<String> data){
        this.data.clear();
        this.data.addAll(data);
        //根据数据创建子View,并添加进来;
        setUpChildren();
    }

    //先清空原来的内容
    public void setUpChildren(){
        removeAllViews(); //由ViewGroup实现
        Log.i("zhouwei","data="+data.size());
            for(String datum:data){
            //设置TextView的相关属性，边距，颜色，border之类，可以直接加载控件代替。
            TextView textView= (TextView) LayoutInflater.from(getContext()).inflate(R.layout.item_flow_text,this,false);
            textView.setText(datum);
            //设置监听事件
            final String tempData=datum;
            textView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onItemClickListener!=null){
                        onItemClickListener.onclick(v,tempData);
                    }
                }
            });
            addView(textView);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener=onItemClickListener;
    }

    public interface OnItemClickListener{
        void onclick(View s,String text);
    }

    //widthMeasureSpec,heightMeasureSpec来自于父空间，包含值和模式
    //int 类型 => 32位 => 前2位是模式，后30位是值。
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //int mode=MeasureSpec.getMode(widthMeasureSpec);//获取模式
        int fatherWidthsize=MeasureSpec.getSize(widthMeasureSpec);//获取值
        int fatherHeightsize =MeasureSpec.getSize(heightMeasureSpec);
        int childrenCount =getChildCount();
        if(childrenCount==0){
            return;
        }
        //注意：对于不满意父容器测量的子 View, 父容器将会依据 子 View 的测量结果来开始第二次测量。
        //这里也是为什么 先清空防止重新绘制。
        //先清空防止重复绘制
        mLines.clear();
        List<View> line=new ArrayList<>();
        mLines.add(line);
        //可以自定义测量值,也可以获取父类控件的值
        int childWidthSize =MeasureSpec.makeMeasureSpec(fatherWidthsize,MeasureSpec.AT_MOST);
        int childHeightSize = MeasureSpec.makeMeasureSpec(fatherHeightsize,MeasureSpec.AT_MOST);

        for(int i=0;i<childrenCount;i++){
            View child =getChildAt(i);
            if(child.getVisibility()!=VISIBLE){
                return;
            }
            //测量孩子
            measureChild(child,childWidthSize,childHeightSize);  //由ViewGroup实现
            if(line.size()==0){
                //可以添加
                line.add(child);
            }
            else {
                //判断是否可以添加到当前行
                boolean result =checkChildCanBeAdd(line,child,fatherWidthsize);
                if(!result){
                    line =new ArrayList<>();
                    mLines.add(line);
                }
                line.add(child);
            }
        }
        //根据尺寸计算所有高
        View child =getChildAt(0);
        int childHeight = child.getMeasuredHeight();
        int parentHeightTargetSize=childHeight *mLines.size()+(mLines.size()+1)*(int)mVerticalMargin; //+1目的使上下都有间距

        setMeasuredDimension(fatherWidthsize,parentHeightTargetSize); //由ViewGroup实现
    }

    private boolean checkChildCanBeAdd(List<View> line,View child,int fatherWidthsize){
        int measuredWidth = child.getMeasuredWidth();
        int totalWidth =(int)mHorizontalMargin;//开始的时候增加边距
        for (View view:line){
            totalWidth +=view.getMeasuredWidth()+mHorizontalMargin; //mHorizontalMargin增加左右边距
        }
        totalWidth +=measuredWidth;

        return totalWidth<= fatherWidthsize;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        // l Left position, relative to parent
        // t Top position, relative to parent
        // r Right position, relative to parent
        // b Bottom position, relative to parent
        View firstView =getChildAt(0);
        int currentLeft=(int)mHorizontalMargin; //开始的时候增加边距
        int currentRight=(int)mHorizontalMargin;
        int currentTop= (int) mVerticalMargin; //增加上下边距
        int currentBottom=firstView.getMeasuredHeight()+(int) mVerticalMargin;;
        for(List<View> line:mLines){
            //循环一行中的View
            for(View view:line){
                int width =view.getMeasuredWidth();
                currentRight+=width;
                view.layout(currentLeft,currentTop,currentRight,currentBottom);
                currentLeft=currentRight+(int)mHorizontalMargin; //增加左右边距
                currentRight+=(int)mVerticalMargin; //解决字体被遮盖的问题。
            }
            currentLeft=(int)mHorizontalMargin;
            currentRight=(int)mHorizontalMargin;
            currentBottom +=firstView.getMeasuredHeight()+(int) mVerticalMargin;;
            currentTop +=firstView.getMeasuredHeight()+(int) mVerticalMargin;;
        }


    }

    public int getmMaxLines() {
        return mMaxLines;
    }

    public void setmMaxLines(int mMaxLines) {
        this.mMaxLines = mMaxLines;
    }

    public float getmHorizontalMargin() {
        return mHorizontalMargin;
    }

    public void setmHorizontalMargin(float mHorizontalMargin) {
        this.mHorizontalMargin = mHorizontalMargin;
    }

    public float getmVerticalMargin() {
        return mVerticalMargin;
    }

    public void setmVerticalMargin(float mVerticalMargin) {
        this.mVerticalMargin = mVerticalMargin;
    }

    public int getmTextMaxLength() {
        return mTextMaxLength;
    }

    public void setmTextMaxLength(int mTextMaxLength) {
        this.mTextMaxLength = mTextMaxLength;
    }

    public int getmTextColor() {
        return mTextColor;
    }

    public void setmTextColor(int mTextColor) {
        this.mTextColor = mTextColor;
    }

    public int getmBorderColor() {
        return mBorderColor;
    }

    public void setmBorderColor(int mBorderColor) {
        this.mBorderColor = mBorderColor;
    }

    public float getmBorderRadius() {
        return mBorderRadius;
    }

    public void setmBorderRadius(float mBorderRadius) {
        this.mBorderRadius = mBorderRadius;
    }
}
