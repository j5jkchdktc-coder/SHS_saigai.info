package jp.shsit.shsinfo2025.ui.kikikuru;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.location.LocationResult;

import jp.shsit.shsinfo2025.LatLongCatch;
import jp.shsit.shsinfo2025.MainActivity;
import jp.shsit.shsinfo2025.R;

public class KikikuruFragment extends Fragment implements LatLongCatch.OnLocationResultListener {

    LatLongCatch locationManager;
    String language;
    ImageView centerImage2,commentImage2;
    Boolean flag =false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_kikikuru, container, false);
        language= PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("lang", "日本語");


        MainActivity main = new MainActivity();
        centerImage2 = root.findViewById(R.id.centerImage2);
        commentImage2 = root.findViewById(R.id.imageView8);
        commentImage2.setAlpha(0.0f);
        //吹き出し
        centerImage2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if(flag==false){
                            flag = true;
                            commentImage2.setAlpha(1.0f);
                        }else{
                            flag = false;
                            commentImage2.setAlpha(0.0f);
                        }
                }

                return true;
            }
        });

        //土砂災害
        TextView tv1 = root.findViewById(R.id.textView10);
        tv1.setText( main.LangReader("kikikuru",0,language));
        ImageView dosyaBtn = root.findViewById(R.id.imageButton);
        dosyaBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                commentImage2.setAlpha(0.0f);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //タップした瞬間
                        dosyaBtn.setImageResource(R.drawable.dosha22);
                        break;
                    case MotionEvent.ACTION_UP:
                        dosyaBtn.setImageResource(R.drawable.dosha);
                        FragmentManager manager = getActivity().getSupportFragmentManager();
                        FragmentTransaction transaction = manager.beginTransaction();
                        Bundle bundle = new Bundle();
                        bundle.putInt("INT_KEY", 1);
                        KikikuruFragment1 fragment = new KikikuruFragment1();
                        //値を書き込む
                        fragment.setArguments(bundle);
                        /* もどるボタンで戻ってこれるように */
                        transaction.addToBackStack(null);
                        transaction.replace(R.id.nav_host_fragment, fragment);
                        transaction.commit();
                        break;

                }
                return true;
            }
        });
//  浸水
        TextView tv2 = root.findViewById(R.id.textView11);
        tv2.setText( main.LangReader("kikikuru",1,language));
        ImageView suiBtn = root.findViewById(R.id.imageButton10);
        suiBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                commentImage2.setAlpha(0.0f);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //タップした瞬間
                        suiBtn.setImageResource(R.drawable.kouzui22);
                        break;
                    case MotionEvent.ACTION_UP:
                        suiBtn.setImageResource(R.drawable.kouzui);
                        FragmentManager manager = getActivity().getSupportFragmentManager();
                        FragmentTransaction transaction = manager.beginTransaction();
                        Bundle bundle = new Bundle();
                        bundle.putInt("INT_KEY", 2);
                        KikikuruFragment1 fragment = new KikikuruFragment1();
                        //値を書き込む
                        fragment.setArguments(bundle);
                        /* もどるボタンで戻ってこれるように */
                        transaction.addToBackStack(null);
                        transaction.replace(R.id.nav_host_fragment, fragment);
                        transaction.commit();
                        break;

                }
                return true;
            }
        });
        // 洪水
        TextView tv3 = root.findViewById(R.id.textView12);
        tv3.setText( main.LangReader("kikikuru",2,language));
        ImageView kouBtn = root.findViewById(R.id.imageButton11);
        kouBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                commentImage2.setAlpha(0.0f);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //タップした瞬間
                        kouBtn.setImageResource(R.drawable.sinsui22);
                        break;
                    case MotionEvent.ACTION_UP:
                        kouBtn.setImageResource(R.drawable.sinsui);
                        FragmentManager manager = getActivity().getSupportFragmentManager();
                        FragmentTransaction transaction = manager.beginTransaction();
                        Bundle bundle = new Bundle();
                        bundle.putInt("INT_KEY", 0);
                        KikikuruFragment1 fragment = new KikikuruFragment1();
                        //値を書き込む
                        fragment.setArguments(bundle);
                        /* もどるボタンで戻ってこれるように */
                        transaction.addToBackStack(null);
                        transaction.replace(R.id.nav_host_fragment, fragment);
                        transaction.commit();
                        break;

                }
                return true;
            }
        });

        //雨雲
        TextView tv4 = root.findViewById(R.id.textView13);
        tv4.setText( main.LangReader("kikikuru",3,language));
        ImageView amaBtn = root.findViewById(R.id.imageButton12);

        amaBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                commentImage2.setAlpha(0.0f);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //タップした瞬間
                        amaBtn.setImageResource(R.drawable.amagumo22);
                        break;
                    case MotionEvent.ACTION_UP:
                        amaBtn.setImageResource(R.drawable.amagumo);
                        FragmentManager manager = getActivity().getSupportFragmentManager();
                        FragmentTransaction transaction = manager.beginTransaction();
                        Bundle bundle = new Bundle();
                        bundle.putInt("INT_KEY", 3);
                        KikikuruFragment1 fragment = new KikikuruFragment1();
                        //値を書き込む
                        fragment.setArguments(bundle);
                        /* もどるボタンで戻ってこれるように */
                        transaction.addToBackStack(null);
                        transaction.replace(R.id.nav_host_fragment, fragment);
                        transaction.commit();
                        break;

                }
                return true;
            }
        });



        locationManager = new LatLongCatch(getContext(), (LatLongCatch.OnLocationResultListener) getActivity());
        locationManager.startLocationUpdates();


        return root;
    }

    @Override
    public void onLocationResult(LocationResult locationResult) {
        // 緯度・経度を取得
        double lat = locationResult.getLastLocation().getLatitude();
        double lon = locationResult.getLastLocation().getLongitude();
        //プレファランス保存
        SharedPreferences prefer = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
        SharedPreferences.Editor editor = prefer.edit();
        editor.putFloat("lat",(float) lat);
        editor.putFloat("lon",(float) lon);
        editor.commit();
    }
}