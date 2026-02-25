package jp.shsit.shsinfo2025.ui.sonaeru.ooame_sonae;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
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

public class OoameSonae3Fragment extends Fragment {
    String page;

    private ToggleButton speakBtn = null;
    private ToggleButton speakBtn1 = null;
    private ToggleButton speakBtn2 = null;
    private ToggleButton speakBtn3 = null;
    private ToggleButton speakBtn4 = null;
    private MediaPlayer mediaPlayer;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ooame3, container, false);
        page = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("page","");

        //言語選択
        String language= PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("lang", "日本語");
        MainActivity main = new MainActivity();

        TextView tv0 = view.findViewById(R.id.ooame3_text0);
        tv0.setTextColor(Color.BLACK);
        SpannableStringBuilder sb = new SpannableStringBuilder();
        //"問２　自宅を離れた避難（立ち退き避難）が必要ですか？\n"
        String s1= main.LangReader("ooame2",0,language);
        sb.append(s1);
        int start = sb.length();
        //"条件が一つでも当てはまれば、立退き避難が必要です。"
        String s2= main.LangReader("ooame2",1,language);
        sb.append(s2);
        sb.setSpan(new RelativeSizeSpan(0.6f), start, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv0.setText(sb);

        //水害リスク
        TextView tv1 = view.findViewById(R.id.ooame3_text1);
        String s3= main.LangReader("ooame2",2,language);
        tv1.setText(s3);
        //土砂災害リスク
        TextView tv2 = view.findViewById(R.id.textView26);
        String s4= main.LangReader("ooame2",6,language);
        tv2.setText(s4);

        CheckBox
                cb3_1 = view.findViewById(R.id.ooame3_checkBox1),
                cb3_2 = view.findViewById(R.id.ooame3_checkBox2),
                cb3_3 = view.findViewById(R.id.ooame3_checkBox3),
                cb3_4 = view.findViewById(R.id.ooame3_checkBox4);

        String s5= main.LangReader("ooame2",3,language);
        String s6= main.LangReader("ooame2",4,language);
        String s7= main.LangReader("ooame2",5,language);
        String s8= main.LangReader("ooame2",7,language);

        cb3_1.setText(s5);
        cb3_2.setText(s6);
        cb3_3.setText(s7);
        cb3_4.setText(s8);


        //プレファランスによる値読み出し
        Boolean ans3_1 = PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean(page+"q3_1",false);
        Boolean ans3_2 = PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean(page+"q3_2",false);
        Boolean ans3_3 = PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean(page+"q3_3",false);
        Boolean ans3_4 = PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean(page+"q3_4",false);
        cb3_1.setChecked(ans3_1);
        cb3_2.setChecked(ans3_2);
        cb3_3.setChecked(ans3_3);
        cb3_4.setChecked(ans3_4);

        SharedPreferences prefer = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = prefer.edit();

        cb3_1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                audioStop();
                if(isChecked) {
                    editor.putBoolean(page+"q3_1",true);
                    editor.commit();
                } else {
                    editor.putBoolean(page+"q3_1",false);
                    editor.commit();
                }
            }
        });

        cb3_2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                audioStop();
                if(isChecked) {
                    editor.putBoolean(page+"q3_2",true);
                    editor.commit();
                } else {
                    editor.putBoolean(page+"q3_2",false);
                    editor.commit();
                }
            }
        });

        cb3_3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                audioStop();
                if(isChecked) {
                    editor.putBoolean(page+"q3_3",true);
                    editor.commit();
                } else {
                    editor.putBoolean(page+"q3_3",false);
                    editor.commit();
                }
            }
        });

        cb3_4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                audioStop();
                if(isChecked) {
                    editor.putBoolean(page+"q3_4",true);
                    editor.commit();
                } else {
                    editor.putBoolean(page+"q3_4",false);
                    editor.commit();
                }
            }
        });
        //次へ
        ImageView btn2 = view.findViewById(R.id.bt_to4);
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
                        Log.i("test",cb3_1.isChecked()+","+cb3_2.isChecked()+","+cb3_3.isChecked()+","+cb3_4.isChecked());
                        if(cb3_1.isChecked() || cb3_2.isChecked() || cb3_3.isChecked() || cb3_4.isChecked()){
                            FragmentManager manager = getActivity().getSupportFragmentManager();
                            FragmentTransaction transaction = manager.beginTransaction();
                            //値渡し
                            Fragment fragment = new OoameSonae4Fragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("lv2","no");
                            fragment.setArguments(bundle);
                            /* もどるボタンで戻ってこれるように */
                            transaction.addToBackStack(null);
                            transaction.replace(R.id.fg1, fragment);
                            transaction.commit();

                        }else{
                            //画面リセット
                            SharedPreferences prefer = PreferenceManager.getDefaultSharedPreferences(getContext());
                            SharedPreferences.Editor editor = prefer.edit();
                            editor.putInt(page+"lv",2);
                            editor.commit();

                            FragmentManager manager = getActivity().getSupportFragmentManager();
                            FragmentTransaction transaction = manager.beginTransaction();
                            //値渡し
                            Fragment fragment = new OoameSonae4Fragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("lv2","ok");
                            fragment.setArguments(bundle);
                            /* もどるボタンで戻ってこれるように */
                            transaction.addToBackStack(null);
                            transaction.replace(R.id.fg1, fragment);
                            transaction.commit();

                            // トップ画面までもどる
                            //getParentFragmentManager().popBackStack("top", 0);
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
        speakBtn3 = view.findViewById(R.id.speakbtn3);
        speakBtn4 = view.findViewById(R.id.speakbtn4);

        speakBtn.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // The switch is checked.
                audioStart("ooame3", 0);
            } else {
                // The switch isn't checked.
                audioStop();
            }
        });

        speakBtn1.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // The switch is checked.
                audioStart("ooame3_1", 1);
            } else {
                // The switch isn't checked.
                audioStop();
            }
        });

        speakBtn2.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // The switch is checked.
                audioStart("ooame3_2", 2);
            } else {
                // The switch isn't checked.
                audioStop();
            }
        });

        speakBtn3.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // The switch is checked.
                audioStart("ooame3_3", 3);
            } else {
                // The switch isn't checked.
                audioStop();
            }
        });

        speakBtn4.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // The switch is checked.
                audioStart("ooame3_4", 4);
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
            case "ooame3":
                mediaPlayer = MediaPlayer.create(getContext(), R.raw.ooame3);
                break;
            case "ooame3_1":
                mediaPlayer = MediaPlayer.create(getContext(), R.raw.ooame3_1);
                break;
            case "ooame3_2":
                mediaPlayer = MediaPlayer.create(getContext(), R.raw.ooame3_2);
                break;
            case "ooame3_3":
                mediaPlayer = MediaPlayer.create(getContext(), R.raw.ooame3_3);
                break;
            case "ooame3_4":
                mediaPlayer = MediaPlayer.create(getContext(), R.raw.ooame3_4);
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
            case 3:
                speakBtn3.setBackgroundResource(R.drawable.baseline_volume_off_24);
                speakBtn3.setChecked(true);
                break;
            case 4:
                speakBtn4.setBackgroundResource(R.drawable.baseline_volume_off_24);
                speakBtn4.setChecked(true);
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
            speakBtn3.setBackgroundResource(R.drawable.baseline_volume_up_24);
            speakBtn3.setChecked(false);
            speakBtn4.setBackgroundResource(R.drawable.baseline_volume_up_24);
            speakBtn4.setChecked(false);
        } catch (Exception e) {
            // TODO
        }
    }

    public void onPause() {
        super.onPause();
        audioStop();
    }
}
