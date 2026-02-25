package jp.shsit.shsinfo2025.ui.weather.weather;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.ListFragment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

import jp.shsit.shsinfo2025.R;
import jp.shsit.shsinfo2025.ui.weather.weather.citypoint.ItemCity;
import jp.shsit.shsinfo2025.ui.weather.weather.citypoint.cityAdapter;


/**
 *  Created by shsit on 2019/04/08.
 */

public class RssCityFragment extends ListFragment {
    String TAG = "tourokuFragment";
    private ArrayList cityItem;
    private cityAdapter adapter;
    String prefCode;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG,"oncreate");
        super.onCreate(savedInstanceState);
//プレファランスデータ読込み
        prefCode = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("key2", "4520100");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.r_fragment_fragment4, container, false);

        cityItem = new ArrayList<>();
        readCSV();
        adapter = new cityAdapter(getContext(),0,cityItem);
        setListAdapter(adapter);

     /*   Bundle args = new Bundle();
        prefCode = args.getString("key2");
        System.out.println(prefCode);*/


        view.setBackgroundColor(Color.rgb(251,247,192));
        return view;
    }
    public void readCSV(){

        ItemCity item=new ItemCity();
        try {
            InputStream inputStream = getResources().getAssets().open("weather_areacode2025.csv");
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream,"UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line="";
            while((line = bufferedReader.readLine()) != null){
                ArrayList<String> array = new ArrayList<>();
                StringTokenizer stringTokenizer = new StringTokenizer(line,",");
                String V1= stringTokenizer.nextToken();
                String V2= stringTokenizer.nextToken();
                String V3= stringTokenizer.nextToken();
                String V4= stringTokenizer.nextToken();
                String V5= stringTokenizer.nextToken();
                String V6= stringTokenizer.nextToken();
                String V7= stringTokenizer.nextToken();
                String V8= stringTokenizer.nextToken();
                if(V5.equals(prefCode)) {
                    System.out.println(V5+","+V4+V3);
                    item.setNo(V1);
                    item.setCity(V3);
                    item.setCity_eng(V4);
                    item.setCity_vn(V6);
                    item.setCity_ch(V7);
                    item.setArea(V8);
                    cityItem.add(item);
                    item = new ItemCity();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        ItemCity itemCity = (ItemCity) cityItem.get(position);
        String cityUrl = itemCity.getNo().toString();
        String areaCode2 = itemCity.getArea().toString();
        //プレファランス保存
        SharedPreferences prefer = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
        SharedPreferences.Editor editor = prefer.edit();
        editor.putString("key3",cityUrl);
        editor.putString("weather_area",areaCode2);
        editor.commit();

        Toast.makeText(this.getActivity(), "地点を登録しました", Toast.LENGTH_LONG).show();
        //もどる
        Log.i("test",cityUrl);
        getFragmentManager().popBackStack();
    }
}