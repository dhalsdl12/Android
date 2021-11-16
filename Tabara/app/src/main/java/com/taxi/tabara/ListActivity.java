package com.taxi.tabara;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ListActivity extends Activity {

    SQLiteDatabase sd;
    Cursor c;
    Button write;
    String result;      //롱클릭에서 선택된 한줄의 "_id" 값 저장
    SimpleCursorAdapter adapter;
    LinearLayout line;  //롱클릭된 한 줄 그 자체
    ListView list;

    @Override
    protected void onStart() {
        super.onStart();

        //2. 공급되는 데이터 설정
        //UI자원 아님
        c = sd.query("note", null, null,
                null, null, null, null);

        //3. ListView와 데이터 연동을 위한 Adapter 생성
        //UI자원 아님
        adapter = new SimpleCursorAdapter(
                this,       //context
                R.layout.line,     //한줄의 모양
                c,                 //공급되는 데이터
                new String[]{"_id", "title_end", "title_time"},       //어떤 데이터를(from)
                new int[]{R.id._id, R.id.title_end, R.id.title_time});   //어디에 보여주겠다(to)

        list.setAdapter(adapter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        c.close();
    }

    class MyDialogListener implements DialogInterface.OnClickListener{

        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            if(i == DialogInterface.BUTTON_POSITIVE){

                //지워지고 있다는 상황을 인지시켜주기 위해 애니메이션 효과 적용
                TranslateAnimation ta = new TranslateAnimation(0, 500, 0, 0);
                ta.setDuration(1000);
                ta.setFillAfter(true);
                //애니메이션 진행상황에 따라 적절한 작업 처리를 위해
                //리스너 달아주기(꼭, start 하기 전에 할것)
                ta.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
//                           String query = "delete from note where _id=5;";
                        String query = "delete from note where _id=" + result + ";";
                        sd.execSQL(query);

                        //DB에서 갱신된 데이터 다시 읽어오기
                        c = sd.query("note", null, null,
                                null, null, null, null);

                        adapter.changeCursor(c);    //새로이 갱신된 데이터 어댑터에 반영(화면에 반영)
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                line.startAnimation(ta);    //롱클릭된 한줄에 애니메이션 효과 적용

            }
            else if(i == DialogInterface.BUTTON_NEGATIVE){
                Toast.makeText(ListActivity.this, "Not Erase",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sd = openOrCreateDatabase("memo.db", 0, null);
        String schema = "create table note (_id integer primary key autoincrement, title text not null , body text not null);";

        try {
            sd.execSQL(schema);
        }
        catch(Exception ignore){
            Log.d("#######", "db error");
        }


        setContentView(R.layout.activity_list);
        write = findViewById(R.id.reg_button);
        write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListActivity.this,
                        RegisterActivity.class);
                intent.putExtra("ACTION", "WRITE");
                startActivity(intent);
            }
        });

        //db에서 저장된 메모 데이터를 가져와서 ListView에 보여주기
        //1. ListView 생성(만들기)
        //UI 자원
        list = findViewById(R.id.listView);



        //ListView에 클릭 이벤트 처리
        //하나의 메모를 선택하면 세부 내용을 보여주기 위한
        //subActivity를 호출
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                //listView 클릭된 한줄의 정보 가져오기
                LinearLayout layout = (LinearLayout)view;
                TextView t = (TextView) layout.getChildAt(0);
                result = t.getText().toString();

                String title_start = "";
                String title_end = "";
                String title_time = "";
                String content_et = "";

                c.moveToFirst();
                while(c.isAfterLast() == false){

                    String _id = c.getString(0);

                    //현재 선택된 ListView 한줄의 _id값(result)이
                    //Db에서 읽어온 데이터를 저장하고 있는 Cursor에 _id값과 같은지 확인
                    if(_id.equals(result)){
                        title_start = c.getString(1);     //title
                        title_end = c.getString(2);     //body
                        title_time = c.getString(3);
                        content_et = c.getString(4);
                    }
                    c.moveToNext();
                }

                Intent intent = new Intent(ListActivity.this,
                        RegisterActivity.class);
                intent.putExtra("title_start", title_start);
                intent.putExtra("title_end", title_end);
                intent.putExtra("title_time", title_time);
                intent.putExtra("content_et", content_et);
                intent.putExtra("ACTION", "READ");

                startActivity(intent);
            }
        });

        //ListView에 롱클릭 이벤트 처리
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                line = (LinearLayout)view;
                TextView t = (TextView) line.getChildAt(0);
                result = t.getText().toString();

                MyDialogListener md = new MyDialogListener();
                //안드로이드 팝업창 사용
                new AlertDialog.Builder(ListActivity.this).setMessage("Erase?")
                        .setPositiveButton("Yes", md).setNegativeButton("No", md)
                        .setTitle("menu").show();

                return true;
                //롱클릭 이벤트 처리후 클릭이벤트 처리를 처리 하지 않기 위해서는 true 리턴
            }
        });

    }
}
