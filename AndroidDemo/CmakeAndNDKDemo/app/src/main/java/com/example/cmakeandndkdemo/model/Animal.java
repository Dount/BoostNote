package com.example.cmakeandndkdemo.model;

import android.util.Log;

public class Animal {
    protected String name;

    public static int num=0;

    public Animal(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static int getNum() {
        return num;
    }

    public static void setNum(int num) {
        Animal.num = num;
    }
    //C++调用Java的实例方法
    public void callInstanceMethod(int num){
        Log.i("zhouwei","call instance method and num is "+num);
    }

    //C++调用Java的实例方法
    public static String callStaticMethod(String str){
        if(str!=null){
            Log.i("zhouwei","call static method with "+str);
        }
        else {
            Log.i("zhouwei","call static method str is null");
        }
        return "";
    }

    //C++调用Java的实例方法
    public static String callStaticMethod(String[] strs,int num){
        if(strs!=null){
            for(String str:strs){
                Log.i("zhouwei","str in array is "+str);
            }
        }
        return "";
    }
}
