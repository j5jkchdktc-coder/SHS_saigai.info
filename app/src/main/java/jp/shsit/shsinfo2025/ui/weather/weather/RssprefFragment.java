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

import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.StringTokenizer;

import jp.shsit.shsinfo2025.R;
import jp.shsit.shsinfo2025.ui.weather.weather.citypoint.ItemCity;
import jp.shsit.shsinfo2025.ui.weather.weather.citypoint.cityAdapter;


public class RssprefFragment extends ListFragment {
    String TAG = "tourokuFragment";
    private ArrayList cityItem;
    private cityAdapter adapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG,"oncreate");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rss_pref_fragment, container, false);
        view.setBackgroundColor(Color.WHITE);
        cityItem = new ArrayList<>();
        readCSV();
        adapter = new cityAdapter(getContext(),0,cityItem);

        setListAdapter(adapter);





        view.setBackgroundColor(Color.rgb(251,247,192));
        return view;
    }
    public void readCSV(){

        ItemCity item=new ItemCity();
        try {
            InputStream inputStream = getResources().getAssets().open("weather_prefcode2025.csv");
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line="";
            //タイトル読み飛ばし
            line = bufferedReader.readLine();
            while((line = bufferedReader.readLine()) != null){
                ArrayList<String> array = new ArrayList<>();
                StringTokenizer stringTokenizer = new StringTokenizer(line,",");
                String V1= stringTokenizer.nextToken();
                String V2= stringTokenizer.nextToken();
                String V3= stringTokenizer.nextToken();
                String V4= stringTokenizer.nextToken();
                String V5= stringTokenizer.nextToken();
                String V6= stringTokenizer.nextToken();
                System.out.println(V1+",");

                item.setNo(V1);
                item.setCity(V2);
                item.setCity_eng(V3);
                item.setCity_vn(V5);
                item.setCity_ch(V6);
                cityItem.add(item);
                item =new ItemCity();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        ItemCity itemCity = (ItemCity) cityItem.get(position);
        //System.out.println(itemCity.getCity());
        String prefCode = itemCity.getNo().toString();
        //プレファランス保存
        SharedPreferences prefer = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
        SharedPreferences.Editor editor = prefer.edit();
        editor.putString("key2",prefCode);
        editor.commit();


        //fragmentによる値渡し使用していない
        Fragment fragment = new RssCityFragment();
        getFragmentManager().beginTransaction()
                .replace(R.id.f2, fragment).commit();

        System.out.println(prefCode);
    }
}