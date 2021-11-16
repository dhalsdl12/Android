package com.taxi.tabara;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
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


public class MainActivity extends Activity {

        // 로그에 사용할 TAG 변수 선언
        final private String TAG = getClass().getSimpleName();

        // 사용할 컴포넌트 선언
        EditText userid_et, passwd_et;
        Button login_button, join_button;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.login);

// 사용할 컴포넌트 초기화
            userid_et = findViewById(R.id.userid_et);
            passwd_et = findViewById(R.id.passwd_et);
            login_button = findViewById(R.id.login_button);
            join_button = findViewById(R.id.join_button);

            // 로그인 버튼 이벤트 추가
            login_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 로그인 함수

                    Toast.makeText(MainActivity.this, "로그인 되었습니다.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, ListActivity.class);
                    startActivity(intent);
                }
            });

            // 조인 버튼 이벤트 추가
            join_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, JoinActivity.class);
                    intent.putExtra("userid", userid_et.getText().toString());
                    startActivity(intent);
                }
            });
        }
}