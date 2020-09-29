package com.example.customview.Utils;

import android.content.Context;

public class SizeUtils {

    public static int dip2px(Context context,float dpvalue){
        float scale =context.getResources().getDisplayMetrics().density;
        return (int) (dpvalue*scale+0.5f);
    }
}
