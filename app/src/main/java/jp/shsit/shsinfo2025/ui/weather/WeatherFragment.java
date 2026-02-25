package jp.shsit.shsinfo2025.ui.weather;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import jp.shsit.shsinfo2025.MainActivity;
import jp.shsit.shsinfo2025.R;
import jp.shsit.shsinfo2025.ui.weather.warnVolEarth.AmaFragment;
import jp.shsit.shsinfo2025.ui.weather.warnVolEarth.EarthFragment;
import jp.shsit.shsinfo2025.ui.weather.warnVolEarth.KamiFragment;
import jp.shsit.shsinfo2025.ui.weather.warnVolEarth.TaifuFragment;
import jp.shsit.shsinfo2025.ui.weather.warnVolEarth.VolcanoFragment;
import jp.shsit.shsinfo2025.ui.weather.warnVolEarth.warnFragment;
import jp.shsit.shsinfo2025.ui.weather.weather.RssWeatherFragment3;
import jp.shsit.shsinfo2025.ui.weather.weather.tourokuFragment2;

public class WeatherFragment extends Fragment {

    String language;
    ImageView centerImage,commentImage;
    Boolean flag =false;
    private ActivityResultLauncher<String> requestPermissionLauncher;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_weather, container, false);

        int width= PreferenceManager.getDefaultSharedPreferences(getActivity()).getInt("width", 100);
        language= PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("lang", "日本語");
        Log.i("item",width+",");

        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int winW = dm.widthPixels;
        int winH = dm.heightPixels;
        Log.i("test", "画面幅 = " + winW);
        Log.i("test", "画面高さ = " + winH);

        MainActivity main = new MainActivity();
        centerImage = root.findViewById(R.id.centerImage);
        commentImage = root.findViewById(R.id.imageView3);
        commentImage.setAlpha(0.0f);
        //吹き出し
        centerImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if(flag==false){
                            flag = true;
                            commentImage.setAlpha(1.0f);
                        }else{
                            flag = false;
                            commentImage.setAlpha(0.0f);
                        }
                }

                return true;
            }
        });


        //地点登録
        TextView tv5 = root.findViewById(R.id.textView5);
        tv5.setText( main.LangReader("tenki",3,language));
        ImageView tourokuBtn = root.findViewById(R.id.imageButton9);
        tourokuBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                commentImage.setAlpha(0.0f);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //タップした瞬間
                        tourokuBtn.setImageResource(R.drawable.titen22);

                        break;
                    case MotionEvent.ACTION_UP:
                        tourokuBtn.setImageResource(R.drawable.titen);
                        FragmentManager manager = getActivity().getSupportFragmentManager();
                        FragmentTransaction transaction = manager.beginTransaction();
                        /* もどるボタンで戻ってこれるように */
                        transaction.addToBackStack(null);
                        transaction.replace(R.id.nav_host_fragment, new tourokuFragment2());
                        transaction.commit();
                        break;

                }
                return true;
            }
        });




        //天気
        TextView tv6 = root.findViewById(R.id.textView6);
        tv6.setText( main.LangReader("tenki",4,language));
        ImageView tenkiBtn = root.findViewById(R.id.imageButton8);
        tenkiBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                commentImage.setAlpha(0.0f);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //タップした瞬間
                        tenkiBtn.setImageResource(R.drawable.tenki22);
                        break;
                    case MotionEvent.ACTION_UP:
                        tenkiBtn.setImageResource(R.drawable.tenki);
                        FragmentManager manager = getActivity().getSupportFragmentManager();
                        FragmentTransaction transaction = manager.beginTransaction();
                        /* もどるボタンで戻ってこれるように */
                        transaction.addToBackStack(null);
                        transaction.replace(R.id.nav_host_fragment, new RssWeatherFragment3());
                        transaction.commit();
                        break;

                }
                return true;
            }
        });
        //警報・注意
        TextView tv9 = root.findViewById(R.id.textView9);
        tv9.setText( main.LangReader("tenki",7,language));
        ImageView keihoBtn = root.findViewById(R.id.imageButton7);
        keihoBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                commentImage.setAlpha(0.0f);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //タップした瞬間
                        keihoBtn.setImageResource(R.drawable.keihou22);
                        break;
                    case MotionEvent.ACTION_UP:
                        keihoBtn.setImageResource(R.drawable.keihou);
                        FragmentManager manager = getActivity().getSupportFragmentManager();
                        FragmentTransaction transaction = manager.beginTransaction();
                        /* もどるボタンで戻ってこれるように */
                        transaction.addToBackStack(null);
                        transaction.replace(R.id.nav_host_fragment, new warnFragment());
                        transaction.commit();
                        break;

                }
                return true;
            }
        });
        //火山
        TextView tv8 = root.findViewById(R.id.textView8);
        tv8.setText( main.LangReader("tenki",6,language));
        ImageView kazanBtn = root.findViewById(R.id.imageButton6);
        kazanBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                commentImage.setAlpha(0.0f);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //タップした瞬間
                        kazanBtn.setImageResource(R.drawable.kazan22);
                        break;
                    case MotionEvent.ACTION_UP:
                        kazanBtn.setImageResource(R.drawable.kazan);
                        FragmentManager manager = getActivity().getSupportFragmentManager();
                        FragmentTransaction transaction = manager.beginTransaction();
                        /* もどるボタンで戻ってこれるように */
                        transaction.addToBackStack(null);
                        transaction.replace(R.id.nav_host_fragment, new VolcanoFragment());
                        transaction.commit();
                        break;

                }
                return true;
            }
        });

        //地震
        TextView tv7 = root.findViewById(R.id.textView7);
        tv7.setText( main.LangReader("tenki",5,language));
        ImageView jishiBtn = root.findViewById(R.id.imageButton5);
        jishiBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                commentImage.setAlpha(0.0f);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        //タップした瞬間
                        jishiBtn.setImageResource(R.drawable.jisin22);
                        break;
                    case MotionEvent.ACTION_UP:
                        jishiBtn.setImageResource(R.drawable.jisin);
                        FragmentManager manager = getActivity().getSupportFragmentManager();
                        FragmentTransaction transaction = manager.beginTransaction();
                        /* もどるボタンで戻ってこれるように */
                        transaction.addToBackStack(null);
                        transaction.replace(R.id.nav_host_fragment, new EarthFragment());
                        transaction.commit();
                        break;

                }
                return true;
            }
        });

        //雷
        TextView tv3 = root.findViewById(R.id.textView3);
        tv3.setText( main.LangReader("tenki",1,language));
        ImageView kaminariBtn = root.findViewById(R.id.imageButton3);
        kaminariBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                commentImage.setAlpha(0.0f);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //タップした瞬間
                        kaminariBtn.setImageResource(R.drawable.kaminari22);
                        break;
                    case MotionEvent.ACTION_UP:
                        kaminariBtn.setImageResource(R.drawable.kaminari);
                        FragmentManager manager = getActivity().getSupportFragmentManager();
                        FragmentTransaction transaction = manager.beginTransaction();
                        /* もどるボタンで戻ってこれるように */
                        transaction.addToBackStack(null);
                        transaction.replace(R.id.nav_host_fragment, new KamiFragment());
                        transaction.commit();
                        break;
                }
                return true;
            }
        });

        //雨雲
        TextView tv2 = root.findViewById(R.id.textView2);
        tv2.setText( main.LangReader("tenki",0,language));
        ImageView amaBtn = root.findViewById(R.id.imageButton2);
        amaBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                commentImage.setAlpha(0.0f);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //タップした瞬間
                        amaBtn.setImageResource(R.drawable.amagumo22);
                        break;
                    case MotionEvent.ACTION_UP:
                        amaBtn.setImageResource(R.drawable.amagumo);
                        FragmentManager manager = getActivity().getSupportFragmentManager();
                        FragmentTransaction transaction = manager.beginTransaction();
                        /* もどるボタンで戻ってこれるように */
                        transaction.addToBackStack(null);
                        transaction.replace(R.id.nav_host_fragment, new AmaFragment());
                        transaction.commit();
                        break;

                }
                return true;
            }
        });

        //台風
        TextView tv4 = root.findViewById(R.id.textView4);
        tv4.setText( main.LangReader("tenki",2,language));
        ImageView taifuBtn = root.findViewById(R.id.imageButton4);
        taifuBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                commentImage.setAlpha(0.0f);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //タップした瞬間
                        amaBtn.setImageResource(R.drawable.taihuu22);
                        break;
                    case MotionEvent.ACTION_UP:
                        amaBtn.setImageResource(R.drawable.taihuu);
                        FragmentManager manager = getActivity().getSupportFragmentManager();
                        FragmentTransaction transaction = manager.beginTransaction();
                        /* もどるボタンで戻ってこれるように */
                        transaction.addToBackStack(null);
                        transaction.replace(R.id.nav_host_fragment, new TaifuFragment());
                        transaction.commit();
                        break;

                }
                return true;
            }
        });


        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == 1) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                // 権限が付与された場合
//                ThunderAlertChecker alertChecker = new ThunderAlertChecker(getContext());
//                alertChecker.startLocationUpdates();
//
//            } else {
//                // 権限が拒否された場合
//                Toast.makeText(getContext(), "通知権限が必要です", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
}