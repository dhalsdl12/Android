package com.bus.busapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Source;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SanAca extends Activity {

    //버스 도착정보 묶음(여러개의 BusInfo 객체를 저장)
    ArrayList<Businfo> total_arrinfo = new ArrayList<Businfo>();

    EditText et;
    String result = "";
    ListView listView;


    class MyThread extends Thread{
        public void run(){

            try{
                URL url = new URL("http://businfo.daegu.go.kr/ba/arrbus/arrbus.do");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setDoOutput(true);

                String parameter = "act=arrbus&winc_id=05291";
                OutputStream wr = con.getOutputStream();

                wr.write(parameter.getBytes());
                wr.flush();

                BufferedReader rd = new BufferedReader(
                        new InputStreamReader(con.getInputStream(), "euc-kr"));

                Source s = new Source(rd);
                s.fullSequentialParse();    //모든 앨리먼트(태그)를 객체화
                List<Element> all = s.getAllElements();    //모든 앨리먼트 리스트로 가져오기

                Businfo binfo = new Businfo();

                for(int i = 0; i < all.size(); i++){
                    Element e = all.get(i);     //특정 index의 앨리먼트 객체 가져오기

                    //해당 엘리먼트의 이름 가져오기 (태그명 가져오기)
                    if(e.getName().equals("span")){

                        String flag = e.getAttributeValue("class");
                        switch (flag){
                            case "route_no":
                                binfo.route_no = e.getTextExtractor().toString();
                                break;
                            case "arr_state":
                                binfo.arr_state = e.getTextExtractor().toString();
                                break;
                            case "cur_pos busDN":
                            case "cur_pos busNN":
                                binfo.cur_pos = e.getTextExtractor().toString();

                                total_arrinfo.add((binfo));     //도착정보 한 세트 저장
                                binfo = new Businfo();
                                break;
                        }
                    }
                }
                if(total_arrinfo.size() == 0){
                    binfo.route_no = "";
                    binfo.arr_state = "";
                    binfo.cur_pos = "버스 도착 정보가 없습니다.";
                    total_arrinfo.add((binfo));
                }

                mh.sendEmptyMessage(0);
            }
            catch(Exception e){
                Log.d("########", "버스 도착정보 요청 오류", e);
            }
        }
    }

    //(3) Thread에서 수신한 데이터 화면 반영을 위해 Handler 사용
    class MyHandler extends Handler{
        @Override
        public void handleMessage(@NonNull Message msg) {
            //UI thread에 부탁할 화면갱신 작업 코딩
            //et.setText(result);

            //버스 도착정보를 모두 수신한 뒤 파싱까지 완료해서
            //ListView에 보여줄 데이터가 온전히 갖춰졌을 때
            //Adapter를 만들고 ListView에 연동해준다.

            //3. ListView와 데이터를 연계해줄 Adapter 생성
            ArrInfoAdapter adapter = new ArrInfoAdapter(SanAca.this, total_arrinfo);

            //4. ListView에 Adapter 적용
            listView.setAdapter(adapter);
        }
    }
    MyHandler mh = new MyHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        listView = new ListView(this);

        new MyThread().start();

        setContentView(R.layout.aca);

        TextView title =findViewById(R.id.title);
        title.setText("경신고등학교앞");
        title.setTextSize(30);

        TextView made =findViewById(R.id.made);
        made.setText("@made by ohmin");

        Button back = findViewById(R.id.back);
        Button home = findViewById(R.id.home);
        Button newinfo = findViewById(R.id.newinfo);

        back.setText("◀");
        back.setTextSize(30);
        back.setTypeface(null, Typeface.BOLD);
        home.setText("❀");
        home.setTextSize(30);
        home.setTypeface(null, Typeface.BOLD);
        newinfo.setText("↺");
        newinfo.setTextSize(30);
        newinfo.setTypeface(null, Typeface.BOLD);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SanAca.this, MainActivity.class);
                startActivity(i);
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SanAca.this, MainActivity.class);
                startActivity(i);
            }
        });
        newinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });

        listView = findViewById(R.id.list_item);
    }
}

