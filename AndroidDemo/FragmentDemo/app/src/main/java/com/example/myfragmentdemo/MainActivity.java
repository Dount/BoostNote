package com.example.myfragmentdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.myfragmentdemo.Interface.FragmentToActivity;
import com.example.myfragmentdemo.Interface.FragmentToFragment;
import com.example.myfragmentdemo.fragment.Fragment1;
import com.example.myfragmentdemo.fragment.Fragment2;

public class MainActivity extends AppCompatActivity implements FragmentToActivity, FragmentToFragment {

    private Button button1;
    private Button button2;
    private FragmentManager fragmentManager;
    private Fragment1 fragment1;
    private Fragment2 fragment2;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        textView = findViewById(R.id.main_text);
        button1.setOnClickListener(new onclick());
        button2.setOnClickListener(new onclick());
        fragmentManager = getSupportFragmentManager();
        selectFragment(0);

    }

    @Override
    public void message(String msg) {
        textView.setText(msg);
    }

    @Override
    public void sendFragmentoneMessage(String msg) {
        if(fragment1!=null){
            fragment1.setText(msg);
        }
    }

    @Override
    public void sendFragmenttwoMessage(String msg) {
        if(fragment2!=null){
            fragment2.setText(msg);
        }
    }


    public class  onclick implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case  R.id.button1:
                    selectFragment(0);
                    break;
                case R.id.button2:
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
                fragmentTransaction.add(R.id.framelayout,fragment1);
            }else {
                fragmentTransaction.show(fragment1);
            }
            Bundle bundle=new Bundle();
            bundle.putString("one","给Fragment1的值");
            fragment1.setArguments(bundle);
        }
        if(i==1){
            if(fragment2==null){
                fragment2=new Fragment2();
                fragmentTransaction.add(R.id.framelayout,fragment2);
            }
            else {
                fragmentTransaction.show(fragment2);
            }

            Bundle bundle=new Bundle();
            bundle.putString("two","给Fragment2的值");
            fragment2.setArguments(bundle);
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
