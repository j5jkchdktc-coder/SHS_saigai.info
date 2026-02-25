package jp.shsit.shsinfo2025.ui.sonaeru.ooame_sonae;

import android.content.SharedPreferences;
import android.graphics.Color;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import jp.shsit.shsinfo2025.MainActivity;
import jp.shsit.shsinfo2025.R;
import jp.shsit.shsinfo2025.hazardmap.hazardFragment;

public class OoameSonae2Fragment extends Fragment {
    String page;


    private ToggleButton speakBtn = null;
    private ToggleButton speakBtn1 = null;
    private ToggleButton speakBtn2 = null;
    private MediaPlayer mediaPlayer;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ooame2, container, false);
        page = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("page","");


    Log.i("test",page+"page");
//値をもらう
        String atai = getArguments().getString("key");
        Log.i("test",atai);

        //言語選択
        String language= PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("lang", "日本語");
        MainActivity main = new MainActivity();

        TextView tv2_0 = view.findViewById(R.id.ooame2_text0);
        tv2_0.setTextColor(Color.BLACK);
        String s1= main.LangReader("ooame1",0,language);
        tv2_0.setText(s1);
        TextView tv2_1 = view.findViewById(R.id.ooame2_text1);
        //"水害のリスクはありますか"
        String s2= main.LangReader("ooame1",1,language);
        tv2_1.setText(s2);
        tv2_1.setTextColor(Color.BLACK);
        TextView tv2_2 = view.findViewById(R.id.ooame2_text2);
        //"土砂災害のリスクは\nありますか"
        String s3= main.LangReader("ooame1",3,language);
        tv2_2.setText(s3);
        tv2_2.setTextColor(Color.BLACK);
        //水害の読み込み
        RadioButton rb2_1 = view.findViewById(R.id.ooame2_radioButton1);
        RadioButton rb2_2 = view.findViewById(R.id.ooame2_radioButton2);
        //プレファランスによる値読み出し
        Boolean  ans2_1 = PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean(page+"q2_1",false);
       if(ans2_1) {
           rb2_1.setChecked(true);
       }else{
           rb2_2.setChecked(true);
       }
        //土砂の読み込み
        RadioButton rb2_3 = view.findViewById(R.id.ooame2_radioButton3);
        RadioButton rb2_4 = view.findViewById(R.id.ooame2_radioButton4);
        //プレファランスによる値読み出し
        Boolean  ans2_2 = PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean(page+"q2_2",false);
        if(ans2_2) {
            rb2_3.setChecked(true);
        }else{
            rb2_4.setChecked(true);
        }

        SharedPreferences prefer = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = prefer.edit();

        // ラジオグループのオブジェクトを取得　水害
        RadioGroup rg = (RadioGroup) view.findViewById(R.id.ooame2_radiogroup1);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
               RadioButton radio = (RadioButton)view.findViewById(checkedId);

                if(radio.isChecked() == true) {
                    audioStop();
                // チェックされた状態の時の処理を記述
                    // チェックされているラジオボタンオブジェクトを取得
                    RadioButton radioButton = (RadioButton) view.findViewById(checkedId);
                    //チェックされているラジオボタンを取得
                    if (rb2_1.isChecked()) {
                        editor.putBoolean(page+"q2_1",true);
                    } else {
                        editor.putBoolean(page+"q2_1",false);
                    }
                    editor.commit();
                }
                else {
                // チェックされていない状態の時の処理を記述

                }
            }
        });


        // ラジオグループのオブジェクトを取得　土砂
        RadioGroup rg2 = (RadioGroup) view.findViewById(R.id.ooame2_radiogroup2);

        rg2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radio = (RadioButton)view.findViewById(checkedId);

                if(radio.isChecked() == true) {
                    audioStop();
                    // チェックされた状態の時の処理を記述
                    // チェックされているラジオボタンオブジェクトを取得
                    RadioButton radioButton = (RadioButton) view.findViewById(checkedId);
                    //チェックされているラジオボタンを取得
                    if (rb2_3.isChecked()) {
                        editor.putBoolean(page+"q2_2",true);
                    } else {
                        editor.putBoolean(page+"q2_2",false);
                    }
                    editor.commit();
                }
                else {
                    // チェックされていない状態の時の処理を記述

                }
            }
        });

        ImageView btn2 = view.findViewById(R.id.bt_to3);
        if(language.equals("English")){
            btn2.setImageResource(R.drawable.ooame_next_eng);
        }
        btn2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        audioStop();
                        btn2.setImageResource(R.drawable.ooame_nextbtn22);
                        if(language.equals("English")){
                            btn2.setImageResource(R.drawable.ooame_next_eng_b);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        audioStop();
                        btn2.setImageResource(R.drawable.ooame_nextbtn);
                        if(language.equals("English")){
                            btn2.setImageResource(R.drawable.ooame_next_eng);
                        }
                        if(rb2_1.isChecked()!= true && rb2_3.isChecked() !=true){
                            SharedPreferences prefer = PreferenceManager.getDefaultSharedPreferences(getContext());
                            SharedPreferences.Editor editor = prefer.edit();
                            editor.putInt(page+"lv",1);
                            editor.commit();

                            getParentFragmentManager().popBackStack();
                        }else{
                            FragmentManager manager = getActivity().getSupportFragmentManager();
                            FragmentTransaction transaction = manager.beginTransaction();
                            /* もどるボタンで戻ってこれるように */
                            transaction.addToBackStack(null);
                            transaction.replace(R.id.fg1 ,new OoameSonae3Fragment());
                            transaction.commit();
                        }
                        break;
                }
                return true;
            }
        });
        //ハザードマップ
        ImageView hazaBtn = view.findViewById(R.id.hzimageview);
        if(language.equals("English")){
            hazaBtn.setImageResource(R.drawable.ooame_hz_eng);
        }
        hazaBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //スクリーンショットを撮るボタンを隠す
                SharedPreferences prefer = PreferenceManager.getDefaultSharedPreferences(getContext());
                SharedPreferences.Editor editor2 = prefer.edit();
                editor2.putString("key_Hz", "no");
                editor2.commit();

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        audioStop();
                        //タップした瞬間
                        hazaBtn.setImageResource(R.drawable.ooame_hzbtn22);
                        if(language.equals("English")){
                            hazaBtn.setImageResource(R.drawable.ooame_hz_eng_b);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        audioStop();
                        hazaBtn.setImageResource(R.drawable.ooame_hzbtn);
                        if(language.equals("English")){
                            hazaBtn.setImageResource(R.drawable.ooame_hz_eng);
                        }
                        FragmentManager manager = getActivity().getSupportFragmentManager();
                        FragmentTransaction transaction = manager.beginTransaction();
                        /* もどるボタンで戻ってこれるように */
                        transaction.addToBackStack(null);
                        transaction.replace(R.id.fg1, new hazardFragment());
                        transaction.commit();
                        break;

                }
                return true;
            }
        });

        //もどる
        Button modoruBtn = view.findViewById(R.id.modorubtn);
        modoruBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
                audioStop();
            }
        });

        speakBtn = view.findViewById(R.id.speakbtn);
        speakBtn1 = view.findViewById(R.id.speakbtn1);
        speakBtn2 = view.findViewById(R.id.speakbtn2);

        speakBtn.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // The switch is checked.
                audioStart("ooame2", 0);
            } else {
                // The switch isn't checked.
                audioStop();
            }
        });

        speakBtn1.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // The switch is checked.
                audioStart("ooame2_1", 1);
            } else {
                // The switch isn't checked.
                audioStop();
            }
        });

        speakBtn2.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // The switch is checked.
                audioStart("ooame2_2", 2);
            } else {
                // The switch isn't checked.
                audioStop();
            }
        });

        return view;
    }

    private boolean audioSetup(String file) {
        boolean fileCheck = true;

        // rawフォルダーから読み込む
        switch (file) {
            case "ooame2":
                mediaPlayer = MediaPlayer.create(getContext(), R.raw.ooame2);
                break;
            case "ooame2_1":
                mediaPlayer = MediaPlayer.create(getContext(), R.raw.ooame2_1);
                break;
            case "ooame2_2":
                mediaPlayer = MediaPlayer.create(getContext(), R.raw.ooame2_2);
                break;
        }

        // 音量調整を端末のボタンに任せる
        getActivity().setVolumeControlStream(AudioManager.STREAM_MUSIC);

        return fileCheck;
    }

    private void audioStart(String file, int flag) {
        audioSetup(file);
        mediaPlayer.start();

        switch (flag) {
            case 0:
                speakBtn.setBackgroundResource(R.drawable.baseline_volume_off_24);
                speakBtn.setChecked(true);
                break;
            case 1:
                speakBtn1.setBackgroundResource(R.drawable.baseline_volume_off_24);
                speakBtn1.setChecked(true);
                break;
            case 2:
                speakBtn2.setBackgroundResource(R.drawable.baseline_volume_off_24);
                speakBtn2.setChecked(true);
                break;
        }
    }

    private void audioStop() {
        try {
            // 再生終了
            mediaPlayer.stop();
            speakBtn.setBackgroundResource(R.drawable.baseline_volume_up_24);
            speakBtn.setChecked(false);
            speakBtn1.setBackgroundResource(R.drawable.baseline_volume_up_24);
            speakBtn1.setChecked(false);
            speakBtn2.setBackgroundResource(R.drawable.baseline_volume_up_24);
            speakBtn2.setChecked(false);
        } catch (Exception e) {
            // TODO
        }
    }

    public void onPause() {
        super.onPause();
        audioStop();
    }
}
