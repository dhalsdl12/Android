package com.taxi.tabara;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class JoinActivity extends Activity {

    // 로그 찍을 때 사용하는 TAG 변수
    final private String TAG = getClass().getSimpleName();

    // 사용할 컴포넌트 선언
    EditText userid_et, passwd_et;
    Button join_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

// 컴포넌트 초기화
        userid_et = findViewById(R.id.userid_et);
        passwd_et = findViewById(R.id.passwd_et);
        join_button = findViewById(R.id.join_button);

// 버튼 이벤트 추가
        join_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
// 회원가입 함수 호출
                Toast.makeText(JoinActivity.this,
                        "축하합니다!", Toast.LENGTH_SHORT).show();

                finish();
            }
        });
    }
}