package jp.shsit.shsinfo2025.ui.sonaeru.keikai;

import android.content.res.Resources;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import jp.shsit.shsinfo2025.R;

public class KeikaiFragment extends Fragment {


    private MediaPlayer mediaPlayer;
    int position;
    double coe;
    int margin1=0;
    ImageView img1;
    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_keikai, container, false);
      //  ImageView img1 = view.findViewById(R.id.imageView);

       // img1.setImageResource(R.drawable.base2);
        //プレファランスによる値読み出し
        int wid1 = PreferenceManager.getDefaultSharedPreferences(getActivity()).getInt("width", 1080);
        int hei1 = PreferenceManager.getDefaultSharedPreferences(getActivity()).getInt("height", 1970);
        Log.i("test", "size = " + wid1+","+hei1);


        //タイトルの高さ
        int titleHi = wid1*120/1080;
        Log.i("item", "size = " + wid1+","+titleHi);

        coe = (double)(hei1-titleHi) / 1850.0;//←1970-120-120＝1970
        //スマホの表示したときの幅
        int wid2 = (int) (1080*coe);
        //余白を計算

        Log.i("item", "width = " + wid1+","+wid2);
        if(wid1>wid2){
            margin1= (int)(wid1-wid2)/2;

        }
        Log.i("item", "余白 = " + margin1+","+wid1+","+wid2+","+coe);

        Resources r = getResources();
     //   Bitmap bmp = BitmapFactory.decodeResource(r,R.drawable.base2);
        img1 = view.findViewById(R.id.imageView);
        img1.setImageResource(R.drawable.base3);
        img1.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams)img1.getLayoutParams();
      //  Log.i("item", "高さ = " + lp.topMargin+","+bmp.getHeight()+","+bmp.getWidth());


        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int x = (int)event.getX();
                int y = (int)event.getY();
                Log.i("item", x + "," + y + "関数");
                int xx= x - margin1;
                int xxx= (int)(xx / coe);
                int yy = (int)(y / coe);
                Log.i("item", xxx + "," + yy + "補正関数");

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.i("test",event.getX() + "," + event.getY());
                        if (xxx > 200 && xxx < 487 && yy > 256 && yy < 522) {
                            img1.setImageResource(R.drawable.lv555);
                            audioStop();
                            audioPlay("lv5");
                        } else if (xxx > 200 && xxx < 531 && yy > 532 && yy < 720) {
                            img1.setImageResource(R.drawable.lv444);
                            audioStop();
                            audioPlay("lv4");
                        } else if (xxx > 200 && xxx < 568 && yy > 800 && yy < 970) {
                            img1.setImageResource(R.drawable.lv333);
                            audioStop();
                            audioPlay("lv3");
                        } else if (xxx > 200 && xxx < 605 && yy > 1010 && yy < 1215) {
                            img1.setImageResource(R.drawable.lv222);
                            audioStop();
                            audioPlay("lv2");
                        } else if (xxx > 200 && xxx < 645 && yy > 1258 && yy < 1500) {
                            img1.setImageResource(R.drawable.lv111);
                            audioStop();
                            audioPlay("lv1");

                        }

                        Log.i("test", event.getX() + "osareta" + event.getY());

                }
                return false;

            }
        });

        Button modBtn = view.findViewById(R.id.mo1Button);
        modBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
            }
        });

        Button keikaiBtn = view.findViewById(R.id.kisyou_btn);
        keikaiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                /* もどるボタンで戻ってこれるように */
                transaction.addToBackStack(null);
                transaction.replace(R.id.nav_host_fragment, new webViewFragment());
                transaction.commit();
            }
        });


        return view;

    }


    @Override
    public void onPause() {
        super.onPause();
        audioStop();
    }

    private void audioPlay(String level) {
        audioSetup(level);
        // 再生する
        mediaPlayer.start();
    }


    private void audioStop() {

        try {
            // 再生終了
            mediaPlayer.stop();
        } catch (Exception e) {

        }
    }
    private boolean audioSetup(String level) {
        boolean fileCheck = false;


        // rawにファイルがある場合
        if (level.equals("lv1")) {
            Log.i("test", "koko");
            mediaPlayer = MediaPlayer.create(getContext(), R.raw.level1);
        } else if (level.equals("lv2")) {
            mediaPlayer = MediaPlayer.create(getContext(), R.raw.level2);
        } else if (level.equals("lv3")) {
            mediaPlayer = MediaPlayer.create(getContext(), R.raw.level3);
        } else if (level.equals("lv4")) {
            mediaPlayer = MediaPlayer.create(getContext(), R.raw.level4);
        } else if (level.equals("lv5")) {
            mediaPlayer = MediaPlayer.create(getContext(), R.raw.level5);
        }


        // 音量調整を端末のボタンに任せる
        getActivity().setVolumeControlStream(AudioManager.STREAM_MUSIC);
        fileCheck = true;


        return fileCheck;
    }
}