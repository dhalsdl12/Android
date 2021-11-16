package com.bus.busapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;


//버스 도착정보 ListView에 연동해서 보여줄 수 있도록
//ListView와 total_arrinfo 연동을 위한 Custom Adapter
public class ArrInfoAdapter extends BaseAdapter {

    Context con;
    ArrayList<Businfo> data;    //버스 도착정보가 저장되어 있음

    public ArrInfoAdapter(Context con, ArrayList<Businfo> data){
        this.con = con;
        this.data = data;
    }

    //listView 한줄에 보여줄 데이터가 세팅된 한 줄 그 자체를 리턴
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Businfo binfo = data.get(i);  //i번째 도착정보 데이터 가져오기

        LinearLayout layout = new LinearLayout(con);

        LayoutInflater inflater = (LayoutInflater)
                con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.arr_line, layout, true);

        TextView route =layout.findViewById(R.id.route_no);
        TextView arr =layout.findViewById(R.id.arr_state);
        TextView cur =layout.findViewById(R.id.cur_pos);

        route.setText(binfo.route_no);
        route.setTextSize(25);
        arr.setText(binfo.arr_state);
        arr.setTextSize(25);
        cur.setText(binfo.cur_pos);
        cur.setTextSize(20);

        return layout;
    }

    @Override
    public int getCount() {
        return data.size();   //ListView에 보여줄 line 수 리턴
    }

    @Override
    public Object getItem(int i) {

        return null;
    }

    @Override
    public long getItemId(int i) {

        return 0;
    }
}
