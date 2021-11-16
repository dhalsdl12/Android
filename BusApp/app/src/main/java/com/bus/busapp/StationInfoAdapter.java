package com.bus.busapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

//버스 정류장 정보(이름, 번호)를 리스트뷰에 보여주기 위한 Adapter
public class StationInfoAdapter extends BaseAdapter {

    Context con;
    ArrayList<StationInfo> total_stationinfo;

    public StationInfoAdapter(Context con, ArrayList<StationInfo> total_stationinfo){
        this.con = con;
        this.total_stationinfo = total_stationinfo;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        //i번째 줄에 보여줄 정보장 정보 가져오기
        StationInfo sinfo = total_stationinfo.get(i);

        LinearLayout layout = new LinearLayout(con);

        //권장되는 방법으로 res/layout xml을 사용하여 한줄 모양 구성
        //res/layout/arr_line.xml 객체화
        LayoutInflater inflater = (LayoutInflater)
                con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.stationinfo_line, layout, true);

        TextView stationname =layout.findViewById(R.id.stationname);
        TextView stationnumber =layout.findViewById(R.id.stationnumber);

        stationname.setText(sinfo.name);
        stationnumber.setText(sinfo.number);

        return layout;
    }

    @Override
    public int getCount() {
        return total_stationinfo.size();
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
