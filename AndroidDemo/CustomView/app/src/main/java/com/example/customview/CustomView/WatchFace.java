package com.example.customview.CustomView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.customview.R;
import com.example.customview.Utils.SizeUtils;

import java.util.Calendar;
import java.util.TimeZone;

public class WatchFace extends View {

    private int mSecondColor;
    private int mMinColor;
    private int mHourColor;
    private int mScaleColor;
    private int mBgResId;
    private boolean mIsCaleSnow;
    private Paint mSecondPaint;
    private Paint mMinPaint;
    private Paint mScalePaint;
    private Paint mHoutPaint;
    private Bitmap bitmap;
    private Rect mSrcRect;
    private Rect mDesRect;
    private int mWidth;
    private int mHeight;
    private Calendar mCalendat;
    private boolean isUpdate =false;

    public WatchFace(Context context) {
        this(context,null);
    }

    public WatchFace(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public WatchFace(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr,0);
    }

    public WatchFace(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        //初始化属性
        initAttr(context, attrs);
        //创建画笔
        initPaints();
        //拿到日历
        mCalendat = Calendar.getInstance();
        //设置时区
        mCalendat.setTimeZone(TimeZone.getDefault());

    }
    //当此view附加到窗体上时调用该方法。
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        isUpdate =true;
        post(new Runnable() {
            @Override
            public void run() {
                if(isUpdate){
                    invalidate();
                    postDelayed(this,1000);
                } else {
                    removeCallbacks(this);//取消定时器
                }
            }
        });
    }

    //将视图从窗体上分离的时候调用该方法。
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.isUpdate=false;
    }

    private void initPaints(){
        //秒针
        mSecondPaint = new Paint();
        mSecondPaint.setColor(mSecondColor);
        mSecondPaint.setStyle(Paint.Style.STROKE);
        mSecondPaint.setStrokeWidth(5f);
        mSecondPaint.setAntiAlias(true);
        //分针
        mMinPaint = new Paint();
        mMinPaint.setColor(mMinColor);
        mMinPaint.setStyle(Paint.Style.STROKE);
        mMinPaint.setStrokeWidth(10f);
        mMinPaint.setAntiAlias(true);
        //时针
        mHoutPaint = new Paint();
        mHoutPaint.setColor(mHourColor);
        mHoutPaint.setStyle(Paint.Style.STROKE);
        mHoutPaint.setStrokeWidth(15f);
        mHoutPaint.setAntiAlias(true);
        //
        mScalePaint = new Paint();
        mScalePaint.setColor(mScaleColor);
        mScalePaint.setStyle(Paint.Style.STROKE);
        mScalePaint.setStrokeWidth(5f);
        mScalePaint.setAntiAlias(true);

    }

    private void initAttr(Context context, AttributeSet attrs) {

        TypedArray array = context.obtainStyledAttributes(attrs,R.styleable.WatchFace);
        mSecondColor = array.getColor(R.styleable.WatchFace_secondColor,getResources().getColor(R.color.secondDefaultColor));
        mMinColor = array.getColor(R.styleable.WatchFace_minColor,getResources().getColor(R.color.minDefaultColor));
        mHourColor = array.getColor(R.styleable.WatchFace_hourColor,getResources().getColor(R.color.hourDefaultColor));
        mScaleColor = array.getColor(R.styleable.WatchFace_scaleColor,getResources().getColor(R.color.scaleDefaultColor));
        mBgResId = array.getResourceId(R.styleable.WatchFace_WathchfaceBackground,-1);
        mIsCaleSnow = array.getBoolean(R.styleable.WatchFace_scaleShow,true);
        if(mBgResId!=-1){
            bitmap = BitmapFactory.decodeResource(getResources(),mBgResId);
        }

        array.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //测量自己
        int widthSize= MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int widthTargetSize = widthSize - getPaddingLeft() +getPaddingRight();
        int heightTargetSize = heightSize - getPaddingTop() - getPaddingBottom();
        int targetSize = Math.min(widthTargetSize,heightTargetSize);
        setMeasuredDimension(targetSize,targetSize);
        //初始化Rect
        initRect();
    }

    private void initRect(){
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        if(bitmap==null){
            return;
        }
        //图片的位置,如果跟图片一样，那么就截取图片所有内容
        mSrcRect = new Rect();
        mSrcRect.left =0;
        mSrcRect.top =0;
        mSrcRect.right = bitmap.getWidth();
        mSrcRect.bottom = bitmap.getHeight();

        //View位置,要填方源内容的地方
        mDesRect = new Rect();
        mDesRect.left = 0;
        mDesRect.top = 0;
        mDesRect.right = mWidth;
        mDesRect.bottom = mHeight;
    }

    //绘制
    @Override
    protected void onDraw(Canvas canvas) {
        long currentMills = System.currentTimeMillis();
        mCalendat.setTimeInMillis(currentMills);
        //绘制背景
        canvas.drawColor(Color.parseColor("#000000"));
        //绘制刻度
        drawScale(canvas);
        //绘制时针
        //save()方法，将记录当前的绘图状态，并压入一个堆栈中
        //调用restore()方法时，就会把上一次记录的绘图状态从堆栈中弹出
        //出栈的次数不能多于入栈的次数,restore方法调用的次数不应该比save方法多
        canvas.save();
        int radius  = (int) (mWidth/2f);
        int hourRadius = (int) (radius*0.8f);
        int minRadius = (int) (radius*0.75f);
        int secondRadius = (int) (radius*0.8f);

        int innerRadius =SizeUtils.dip2px(getContext(),5);
        int hourValue =mCalendat.get(Calendar.HOUR);
        int minValue =mCalendat.get(Calendar.MINUTE);
        int secondValue = mCalendat.get(Calendar.SECOND);

        float hourOffsetRerate = minValue /2;
        float hourRotate = hourValue *30 +hourOffsetRerate;
        //求旋转角度
        canvas.rotate(hourRotate,radius,radius);//以原点中进行旋转
        canvas.drawLine(radius,radius-hourRadius,radius,radius-innerRadius,mHoutPaint);
        canvas.restore();
        //求分针
        canvas.save();
        float minRotate = minValue *6f; //每分钟6°
        canvas.rotate(minRotate,radius,radius);
        canvas.drawLine(radius,radius-minRadius,radius,radius-innerRadius,mMinPaint);
        canvas.restore();
        //求秒针
        canvas.save();
        float secondRotate =secondValue *6f;
        canvas.rotate(secondRotate,radius,radius);
        canvas.drawLine(radius,radius-secondRadius,radius,radius-innerRadius,mSecondPaint);
        canvas.restore();
    }

    private void drawScale(Canvas canvas) {
        int radius = (int)(mWidth/2f);
        //内环半径
        int innerC = (int)(mWidth/2*0.85f);
        //外环半径
        int outerC = (int)(mWidth/2*0.95f);
        canvas.save();
        for(int i=0;i<12;i++){
            double th = i*Math.PI*2/12; //360°分成12份
            Log.i("zhouwei","th = "+th);
            //内环
            int innerB = (int)(Math.cos(th)*innerC);
            int innerX = mHeight/2 -innerB;
            int innerA = (int)(innerC*Math.sin(th));
            int innerY = mWidth/2 +innerA;
            //外环
            int outerB = (int)(Math.cos(th)*outerC);
            int outerX = mHeight/2 -outerB;
            int outerA = (int)(outerC*Math.sin(th));
            int outerY = mWidth/2 +outerA;

            canvas.drawLine(innerX,innerY,outerX,outerY,mScalePaint);
        }
        canvas.drawCircle(radius,radius, SizeUtils.dip2px(getContext(),5),mScalePaint);
        canvas.restore();
    }
}
