package jp.shsit.shsinfo2025.ui.kikikuru.sinsui;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.gms.location.LocationResult;

import jp.shsit.shsinfo2025.MainActivity;
import jp.shsit.shsinfo2025.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SinsuiFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SinsuiFragment extends Fragment implements LatLongCatch.OnLocationResultListener{

    private Button button;
    private TextView textDepth,textEleva,textAdd;
    LatLongCatch locationManager;
    private double lat = 0.0;
    private double lon = 0.0;
    //BroadCastReceiverからtextViewを変更する
    private static SinsuiFragment ins;
    ImageView img;




    public static SinsuiFragment newInstance(String param1, String param2) {
        SinsuiFragment fragment = new SinsuiFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_sinsui, container, false);

        ins = this;



        textDepth = root.findViewById(R.id.textView);
        textEleva = root.findViewById(R.id.textView2);
        textAdd = root.findViewById(R.id.textView5);
        img = root.findViewById(R.id.imageView);

        /***********言語選択********************/
        String language= PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("lang", "日本語");
        MainActivity main = new MainActivity();
        /*************************************/
        TextView tv1 = root.findViewById(R.id.textadd);
        TextView tv2 = root.findViewById(R.id.textView31);
        TextView tv3 = root.findViewById(R.id.textView35);
        tv1.setText(main.LangReader("shinsui",0,language));
        tv2.setText(main.LangReader("shinsui",1,language));
        tv3.setText(main.LangReader("shinsui",2,language));



        locationManager = new LatLongCatch(getContext(),SinsuiFragment.this); locationManager.startLocationUpdates();
        locationManager.startLocationUpdates();



        BroadcastReceiver1 receiver = new BroadcastReceiver1();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("MY_ACTION");
        getActivity().registerReceiver(receiver, intentFilter);

        Button modoruBtn = root.findViewById(R.id.modorubtn);
        modoruBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
            }
        });


        modoruBtn.setText(main.LangReader("hinan",5,language));

        return root;
    }
    /*************BroadCastReceiverからtextViewを変更する****************************************/
    public static SinsuiFragment  getInstace(){
        return ins;
    }
    public void updateTheTextView( String depth, String eleva,String address) {
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                Log.i("test",depth+"ですすすす");
                String depth1 ="0";
                if (depth == null){
                    depth1 ="0";
                }else{
                    depth1=depth;
                }
                Log.i("test",depth1+"ですすすす");
                textDepth.setText(depth1);
                textEleva.setText(eleva);
                textAdd.setText(address);
                float depth2= Float.parseFloat(depth1);
                if(depth2 == 0){
                    img.setImageResource(R.drawable.sinsui_img1);
                }
                else if(depth2<0.3){
                    img.setImageResource(R.drawable.sinsui_img11);
                }
                else if(depth2<0.5){
                    img.setImageResource(R.drawable.sinsui_img22);
                }
                else if(depth2<1.0){
                    img.setImageResource(R.drawable.sinsui_img33);
                }
                else if(depth2<3){
                    img.setImageResource(R.drawable.sinsui_img4);
                }
                else if(depth2<5){
                    img.setImageResource(R.drawable.sinsui_img5);
                }
                else if(depth2<10){
                    img.setImageResource(R.drawable.sinsui_img6);
                }
                else if(depth2<20){
                    img.setImageResource(R.drawable.sinsui_img7);
                }
                else{
                    img.setImageResource(R.drawable.sinsui_img8);
                }
            }
        });
    }
    /*****************************************************/
    @Override
    public void onLocationResult(LocationResult locationResult) {
        // 緯度・経度を取得
        lat = locationResult.getLastLocation().getLatitude();
        lon = locationResult.getLastLocation().getLongitude();

        // サーバーに値を渡す
        float lat1 = (float)lat;
        float lon1 = (float)lon;
        Intent intent = new Intent(getContext(), IntentService1.class);
        intent.putExtra("lat",lat1);
        intent.putExtra("lon",lon1);
        getActivity().startService(intent);
    }
}