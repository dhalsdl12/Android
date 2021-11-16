package com.taxi.tabara;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
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

public class RegisterActivity extends Activity {

    SQLiteDatabase sd;
    EditText content_et;
    EditText title_start, title_end, title_time;
    Button reg_button;
    String action;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        title_start = findViewById(R.id.title_start);
        title_end = findViewById(R.id.title_end);
        title_time = findViewById(R.id.title_time);
        content_et = findViewById(R.id.content_et);
        reg_button = findViewById(R.id.reg_button);

        Intent i = getIntent();
        action = i.getStringExtra("ACTION");

        switch(action){
            case "READ":
                title_start.setText(i.getStringExtra("title_start"));
                title_end.setText(i.getStringExtra("title_end"));
                title_time.setText(i.getStringExtra("title_time"));
                content_et.setText(i.getStringExtra("content_et"));

                title_start.setEnabled(false);
                title_end.setEnabled(false);
                title_time.setEnabled(false);
                content_et.setEnabled(false);
                reg_button.setText("BACK");
                break;
            case "WRITE":
                sd = openOrCreateDatabase("memo.db", 0, null);

                break;
        }
        reg_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch(action){
                    case "READ":
                        finish();   //현재 화면 종료
                        break;
                    case "WRITE":
                        //insert
                        ContentValues values = new ContentValues();

                        values.put("title_start", title_start.getText().toString());
                        values.put("title_end", title_end.getText().toString());
                        values.put("title_time", title_time.getText().toString());
                        values.put("content_et", content_et.getText().toString());
                        sd.insert("note", null, values);

                        Toast.makeText(RegisterActivity.this,
                                "SAVE!", Toast.LENGTH_SHORT).show();

                        finish();
                        break;
                }
            }
        });
    }
}
