package com.bus.busapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Source;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class StationInfoActivity extends Activity {

    //정류장 정보를 보여주기 위한 listView
    ListView listView;
    ArrayList<StationInfo> total_stationinfo = new ArrayList<StationInfo>();

    EditText station;
    String result = ""; //수신된 정류장 정보 임시로 저장 확인을 위한 변수

    //안드로이드에서 network 통신을 필요한 3가지 필수 요소
    //(1) Androidmanifest.xml에 INTERNET 퍼미션 할당
    //(2) Thread사용
    class MyThread extends Thread{
        public void run(){
            //Thread가 수행할 작업 코딩
            //정류장 정보 요청
            try{
                URL url = new URL("http://businfo.daegu.go.kr/ba/arrbus/arrbus.do");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setDoOutput(true);

//                String parameter = "act=findByBusStopNo&bsNm=경북대";
                String parameter = "act=findByBusStopNo&bsNm=" + station.getText();
                OutputStream wr = con.getOutputStream();

                wr.write(parameter.getBytes("euc-kr"));
                wr.flush();

                BufferedReader rd = new BufferedReader(
                        new InputStreamReader(con.getInputStream(), "euc-kr"));

                Source s = new Source(rd);
                s.fullSequentialParse();    //모든 앨리먼트(태그)를 객체화
                List<Element> all = s.getAllElements();    //모든 앨리먼트 리스트로 가져오기

                StationInfo stationInfo = new StationInfo();
                int cnt = 0;
                for(int i = 0; i < all.size(); i++){
                    Element e = all.get(i);     //특정 index의 앨리먼트 객체 가져오기

                    if(e.getName().equals("td")) {
                        if (cnt == 0) {
                            stationInfo.name = e.getTextExtractor().toString();
                            cnt = 1;
                        }
                        else if(cnt == 1){
                            stationInfo.number = e.getTextExtractor().toString();
                            //정류장 정보 한 세트 저장
                            total_stationinfo.add(stationInfo);

                            //다음 정류장정보 저장을 위한 객체 할당
                            stationInfo = new StationInfo();
                            cnt = 0;
                        }
                    }
                }

//                et.setText(result);     //Working thread에서는 화면 갱신이 불가
                mh.sendEmptyMessage(0);
            }
            catch(Exception e){
                Log.d("########", "버스 도착정보 요청 오류", e);
            }
        }
    }
    //(3) Thread에서 수신한 데이터 화면 반영을 위해 Handler 사용
    class MyHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            //3. ListView와 데이터를 연계해줄 Adapter 생성
            StationInfoAdapter adapter = new StationInfoAdapter(StationInfoActivity.this, total_stationinfo);

            //4. ListView에 Adapter 적용
            listView.setAdapter(adapter);
        }
    }
    MyHandler mh = new MyHandler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.stationinfoactivity);

        Button search = findViewById(R.id.search);  //검색버튼 가져오기
        station = findViewById(R.id.station);    //입력창 가져오기

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //키보드 비활성화
                InputMethodManager im = (InputMethodManager)getSystemService(
                        INPUT_METHOD_SERVICE);
                im.hideSoftInputFromWindow(station.getWindowToken(), 0);

                //이전에 검색된 정류장 정보 리셋
                total_stationinfo.clear();

                //2. 공급되는 데이터 설정 - 정류장 정보 가져오기
                new MyThread().start();     //정류장 검색 요청 후 파싱
            }
        });

        //1. ListView 만들기
        listView = findViewById(R.id.list);
        EditText title = findViewById(R.id.station);
        title.setHint("Daegu Bus Info");
        title.setTextSize(30);

        //ListView 한줄 클릭 이벤트 처리
        //선택된 정류장 도착정보를 보여주기 위한 화면 호출 처리
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                LinearLayout layout = (LinearLayout) view;
                LinearLayout layout2 = (LinearLayout) layout.getChildAt(0);

                TextView name = (TextView) layout2.getChildAt(0);
                TextView number = (TextView) layout2.getChildAt(1);

                Intent intent = new Intent(StationInfoActivity.this, Station.class);

                intent.putExtra("name", name.getText());
                intent.putExtra("number", number.getText());

                startActivity(intent);
            }
        });
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
                Intent i = new Intent(StationInfoActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(StationInfoActivity.this, MainActivity.class);
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
    }
}
