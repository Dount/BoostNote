package com.example.customview.CustomViewGroup;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;
import android.widget.TextView;

import com.example.customview.R;

import org.w3c.dom.Text;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_MOVE;
import static android.view.MotionEvent.ACTION_UP;

public class SlideMenuView extends ViewGroup {

    private int function;
    private View mContentView;
    private View mEditaView;
    private OnEditClickListener mOnEditClickListener = null;
    private float downx;
    private float downy;
    private float movex;
    private float movey;

    private Scroller scroller; //是一个专门用于处理滚动效果的工具类

    public SlideMenuView(Context context) {
        this(context,null);
    }

    public SlideMenuView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SlideMenuView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr,0);
    }

    public SlideMenuView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        //自定义属性
        initAttr(context, attrs);
    }

    private void initAttr(Context context, AttributeSet attrs) {

        TypedArray typedArray=context.obtainStyledAttributes(attrs, R.styleable.SlideMenuView);
        function = typedArray.getInt(R.styleable.SlideMenuView_function,0x30);
        typedArray.recycle();
        scroller=new Scroller(context);
    }

    //是否已经打开
    private boolean isOpen =false;

    private Direction mCurrentDirect =Direction.NONE;

    enum Direction {
        LEFT,RIGHT,NONE
    }

    //mDuration 走完的mEditView 4/5宽度所需的时间
    private int mMaxDuration =800;
    private int mMinDuration =300;



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action =event.getAction();
        switch (action){
            case ACTION_DOWN:
                downx = event.getX();
                downy = event.getY();
                break;
            case ACTION_MOVE:
                int scrollx = getScrollX(); //返回当前滑动View左边界的位置
                movex = event.getX();
                movey = event.getY();
                //移动的差值
                int dx = (int) (movex - downx);

                if(dx>0){
                    mCurrentDirect = Direction.RIGHT;
                }else{
                    mCurrentDirect = Direction.LEFT;
                }


                //判断边界值
                int resultScrollx = -dx +scrollx;
                if(resultScrollx <=0){
                    scrollTo(0,0);
                }else if(resultScrollx >mEditaView.getMeasuredWidth()){
                    scrollTo(mEditaView.getMeasuredWidth(),0);

                }
                else {
                    scrollBy(-dx,0);
                }
                break;
            case ACTION_UP:
                float upx=event.getX();
                float upy=event.getY();

                int hasBeenScrollx =getScrollX();
                int editViewWidth = mEditaView.getMeasuredWidth();

                if(isOpen){
                    //当前状态打开
                    if(mCurrentDirect == Direction.RIGHT){
                        //方向右，如果小于3/4,那么就关闭
                        //否则打开
                        if(hasBeenScrollx <=editViewWidth *4/5){
                            close();
                        }else{
                            open();
                        }
                    }else if(mCurrentDirect == Direction.LEFT){
                        open();
                    }
                }else {
                    //当前状态关闭
                    if(mCurrentDirect == Direction.LEFT){
                        //向左滑动，判断滑动的距离
                        if(hasBeenScrollx > editViewWidth/5){
                            open();
                        }else{
                            close();
                        }
                    } else if(mCurrentDirect == Direction.RIGHT){
                        //向右滑动
                        close();
                    }
                }
                break;
        }
        return true;
    }

    private void open(){
        int dx =mEditaView.getMeasuredWidth()-getScrollX();
        int duration = (int) (dx / (mEditaView.getMeasuredWidth()*4/5f)*mMaxDuration);
        int absDuration = Math.abs(duration);
        if(absDuration<mMinDuration){
            absDuration = mMinDuration;
        }
        scroller.startScroll(getScrollX(),0,dx,0,absDuration);
        invalidate();
        isOpen=true;
    }

    private void close(){
        int dx =-getScrollX();
        int duration = (int) (dx/(mEditaView.getMeasuredWidth()*4/5f)*mMaxDuration);
        int absDuration = Math.abs(duration);
        if(absDuration<mMinDuration){
            absDuration = mMinDuration;
        }
        scroller.startScroll(getScrollX(),0,dx,0,absDuration);
        invalidate();
        isOpen=false;
    }


    //差值器，scroller与差值器进行配合使用
    @Override
    public void computeScroll() {
        //返回值为boolean，true说明滚动尚未完成，false说明滚动已经完成。
        // 这是一个很重要的方法，通常放在View.computeScroll()中，用来判断是否滚动是否结束。
        if(scroller.computeScrollOffset()){
            int currx=scroller.getCurrX(); //获取Scroller当前水平滚动的位置
            Log.i("zhouwei","mcurrx="+currx);
            //滑动到指定位置;
            scrollTo(scroller.getCurrX(),0);
            invalidate();
        }
    }

    //当XML布局被加载完后，就会回调onFinshInfalte这个方法，在这个方法中可以初始化控件和数据。
    //但是在 onFinishInflate 中不能获取 view 宽高，需要在 onMeasure 之后获取，
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int childCount =getChildCount();
        //只能有一个子View
        if(childCount>1){
            throw new IllegalArgumentException("no more then one child.");
        }
        mContentView = getChildAt(0); //textView
        //根据属性，继续添加子View item_slide_action
        mEditaView = LayoutInflater.from(getContext()).inflate(R.layout.item_slide_action,this,false);
        TextView mread = mEditaView.findViewById(R.id.read);
        TextView mTipTop = mEditaView.findViewById(R.id.tiptop);
        TextView mDelete = mEditaView.findViewById(R.id.delete);
        mread.setOnClickListener(new onClick());
        mTipTop.setOnClickListener(new onClick());
        mDelete.setOnClickListener(new onClick());

        this.addView(mEditaView);

    }

    public class onClick implements OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.read:
                    mOnEditClickListener.onReadClick();
                    break;
                case R.id.tiptop:
                    mOnEditClickListener.onTopClick();
                    break;
                case R.id.delete:
                    mOnEditClickListener.onDeleteClick();
                    break;
            }
            close();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        LayoutParams contentLayoutParams = mContentView.getLayoutParams();
        int contentHeight = contentLayoutParams.height;
        int contentHeightMeasureSpace;

        if(contentHeight == LayoutParams.MATCH_PARENT){
            contentHeightMeasureSpace = MeasureSpec.makeMeasureSpec(heightSize,MeasureSpec.EXACTLY);
        }else if(contentHeight == LayoutParams.WRAP_CONTENT){
            contentHeightMeasureSpace = MeasureSpec.makeMeasureSpec(heightSize,MeasureSpec.AT_MOST);
        }else{
            //指定大小
            contentHeightMeasureSpace = MeasureSpec.makeMeasureSpec(contentHeight,MeasureSpec.EXACTLY);
        }
        //获取内容的测量值
        mContentView.measure(widthMeasureSpec,contentHeightMeasureSpace);
        //拿到内容部分测量后的高度
        int contentMeasureHeight = mContentView.getMeasuredHeight();
        //测量编辑部分 宽度3/4 高度一样
        int editWidthSize = widthSize *3/4;
        int editWidthSizeMeasureSpace = MeasureSpec.makeMeasureSpec(editWidthSize,MeasureSpec.EXACTLY);
        int editHeightSizeMeasureSpace = MeasureSpec.makeMeasureSpec(contentMeasureHeight,MeasureSpec.EXACTLY);;
        mEditaView.measure(editWidthSizeMeasureSpace,editHeightSizeMeasureSpace);

        //测量自己
        setMeasuredDimension(widthSize+editWidthSize,contentMeasureHeight);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //摆放内容
        int contentLeft=0;
        int contentTop = 0;
        int contentRight = contentLeft +mContentView.getMeasuredWidth();
        int contentBottom = contentTop +mContentView.getMeasuredHeight();
        mContentView.layout(contentLeft, contentTop,contentRight,contentBottom);
        //摆放编辑部分
        int editViewLeft = contentRight;
        int editViewTop  = contentTop;
        int editViewRight = editViewLeft +mEditaView.getMeasuredWidth();
        int editViewBottom = editViewTop+mEditaView.getMeasuredHeight();
        mEditaView.layout(editViewLeft,editViewTop,editViewRight,editViewBottom);

    }

    public void setOnEditClickListener(OnEditClickListener listener){
        this.mOnEditClickListener=listener;
    }

    public interface OnEditClickListener{
        void onReadClick();

        void onTopClick();

        void onDeleteClick();
    }

}
