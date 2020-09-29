package com.example.customview;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.customview.CustomCombinationView.InputNumberView;

public class MainActivity extends AppCompatActivity {

    private InputNumberView inputNumberView;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button1 = findViewById(R.id.button1);
        Button button2 = findViewById(R.id.button2);
        Button button3 = findViewById(R.id.button3);
        Button button4 = findViewById(R.id.button4);
        Button button5 = findViewById(R.id.button5);
        button1.setOnClickListener(new onClick());
        button2.setOnClickListener(new onClick());
        button3.setOnClickListener(new onClick());
        button4.setOnClickListener(new onClick());
        button5.setOnClickListener(new onClick());
    }
    public class onClick implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            intent = new Intent(MainActivity.this,ShowActivity.class);
            switch (v.getId()){
                case R.id.button1:
                    intent.putExtra("module","inputnumberView");
                    startActivity(intent);
                    break;
                case R.id.button2:
                    intent.putExtra("module","loginkeyboard");
                    startActivity(intent);
                    break;
                case R.id.button3:
                    intent.putExtra("module","FlowLayout");
                    startActivity(intent);
                    break;
                case R.id.button4:
                    intent.putExtra("module","KeyPadView");
                    startActivity(intent);
                    break;
                case R.id.button5:
                    intent.putExtra("module","SlideMenuView");
                    startActivity(intent);
            }
        }
    }
}
