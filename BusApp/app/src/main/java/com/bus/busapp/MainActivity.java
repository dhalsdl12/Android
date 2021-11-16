package com.bus.busapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

    class MyListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            Intent i;
            switch(view.getId()){
                case R.id.button:
                    i = new Intent(MainActivity.this, SanHome.class);
                    startActivity(i);
                    break;
                case R.id.button2:
                    i = new Intent(MainActivity.this, SanSchool.class);
                    startActivity(i);
                    break;
                case R.id.button3:
                    i = new Intent(MainActivity.this, SanAca.class);
                    startActivity(i);
                    break;
                case R.id.button4:
                    i = new Intent(MainActivity.this, StationInfoActivity.class);
                    startActivity(i);
                    break;
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainactivity);

        TextView title = findViewById(R.id.title);
        title.setText("산이는 오빠를 좋아해❤");
        title.setTextSize(30);

        TextView t = findViewById(R.id.textView);
        t.setText("@made by ohmin");

        Button b1 = findViewById(R.id.button);
        Button b2 = findViewById(R.id.button2);
        Button b3 = findViewById(R.id.button3);
        Button b4 = findViewById(R.id.button4);
        b1.setText("집 앞 정류장");
        b1.setTextSize(25);
        b2.setText("학교 앞 정류장");
        b2.setTextSize(25);
        b3.setText("학원 앞 정류장");
        b3.setTextSize(25);
        b4.setText("검색");
        b4.setTextSize(25);


        MyListener m = new MyListener();
        b1.setOnClickListener(m);
        b2.setOnClickListener(m);
        b3.setOnClickListener(m);
        b4.setOnClickListener(m);
    }
}