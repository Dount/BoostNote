package com.lypeer.eventbusdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.lypeer.eventbusdemo.Fragment.Fragment1;
import com.lypeer.eventbusdemo.Fragment.Fragment2;

import org.greenrobot.eventbus.EventBus;

public class MainActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private Fragment1 fragment1;
    private Fragment2 fragment2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button1 =findViewById(R.id.Button1);
        Button button2 =findViewById(R.id.Button2);
        button1.setOnClickListener(new onclick());
        button2.setOnClickListener(new onclick());
        fragmentManager = getSupportFragmentManager();
        selectFragment(0);
    }

    public class onclick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.Button1:
                    selectFragment(0);
                    break;
                case R.id.Button2:
                    selectFragment(1);
                    break;
            }
        }
    }
    public void selectFragment(int i){
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        hide(fragmentTransaction);
        if(i==0){
            if(fragment1==null){
                fragment1=new Fragment1();
                fragmentTransaction.add(R.id.framelayout,fragment1,"fragment1");
            }
            else {
                fragmentTransaction.show(fragment1);
            }
        }
       if(i==1){
           if(fragment2==null){
               fragment2=new Fragment2();
               fragmentTransaction.add(R.id.framelayout,fragment2,"fragment2");
           }
           else {
               fragmentTransaction.show(fragment2);
           }
       }
       fragmentTransaction.commit();
    }

    public void hide(FragmentTransaction fragmentTransaction){
        if(fragment1!=null){
            fragmentTransaction.hide(fragment1);
        }
        if(fragment2!=null){
            fragmentTransaction.hide(fragment2);
        }
    }
}
