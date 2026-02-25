package jp.shsit.shsinfo2025.ui.sonaeru.ooame_sonae;

import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import jp.shsit.shsinfo2025.hinan.MapsFragmenrt2;

public class OoameSonae4Fragment extends Fragment {
    String page;

    private ToggleButton speakBtn = null;
    private ToggleButton speakBtn1 = null;
    private ToggleButton speakBtn2 = null;
    private MediaPlayer mediaPlayer;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ooame4, container, false);
        page = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("page","");

        //言語選択
        String language= PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("lang", "日本語");
        MainActivity main = new MainActivity();

        TextView tv4 = view.findViewById(R.id.ooame4_text0);
        SpannableStringBuilder sb = new SpannableStringBuilder();
        //問３　立ち退き避難の避難先はどこですか？"
        String s1= main.LangReader("ooame3",0,language);
        sb.append(s1);
        int start = sb.length();
        //"問２　自宅を離れた避難（立ち退き避難）が必要ですか？\n"
        String s2= main.LangReader("ooame3",1,language);
        sb.append(s2);
        sb.setSpan(new RelativeSizeSpan(0.6f), start, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv4.setText(sb);

        //安全な場所の知人
        TextView tv5 = view.findViewById(R.id.ooame4_text1);
        String s3= main.LangReader("ooame3",2,language);
        tv5.setText(s3);
        EditText editText4_1 = view.findViewById(R.id.ooame4_edit1);

        //指定緊急避難場所
        TextView tv6 = view.findViewById(R.id.ooame4_text2);
        String s4= main.LangReader("ooame3",3,language);
        tv6.setText(s4);
        EditText editText4_2 = view.findViewById(R.id.ooame4_edit2);

        //プレファランスによる値読み出し
        String ans4_1 = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(page+"q4",null);
        editText4_1.setText(ans4_1);
        String ans4_2 = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(page+"q42",null);
        editText4_2.setText(ans4_2);

        String atai =  getArguments().getString("lv2"," ");
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
                        btn2.setImageResource(R.drawable.ooame_nextbtn22);
                        if(language.equals("English")){
                            btn2.setImageResource(R.drawable.ooame_next_eng_b);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        btn2.setImageResource(R.drawable.ooame_nextbtn);
                        if(language.equals("English")){
                            btn2.setImageResource(R.drawable.ooame_next_eng);
                        }
                        SharedPreferences prefer = PreferenceManager.getDefaultSharedPreferences(getContext());
                        SharedPreferences.Editor editor = prefer.edit();
                        editor.putString(page+"q4",editText4_1.getText().toString());
                        editor.putString(page+"q42",editText4_2.getText().toString());

                        editor.commit();
                        if(atai.equals("ok")){
                            audioStop();
                            // トップ画面までもどる
                            getParentFragmentManager().popBackStack("top", 0);
                        }else {
                            audioStop();
                            FragmentManager manager = getActivity().getSupportFragmentManager();
                            FragmentTransaction transaction = manager.beginTransaction();
                            /* もどるボタンで戻ってこれるように */
                            transaction.addToBackStack(null);
                            transaction.replace(R.id.fg1, new OoameSonae5Fragment());
                            transaction.commit();
                        }
                        break;
                }
                return true;
            }
        });


        ImageView hinaBtn = view.findViewById(R.id.hinanimageview);
        if(language.equals("English")){
            hinaBtn.setImageResource(R.drawable.ooame_hina_eng);
        }
        //避難所
        hinaBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //タップした瞬間
                        hinaBtn.setImageResource(R.drawable.ooame_hinanbtn22);
                        if(language.equals("English")){
                            hinaBtn.setImageResource(R.drawable.ooame_hina_eng_b);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        hinaBtn.setImageResource(R.drawable.ooame_hinanbtn);
                        if(language.equals("English")){
                            hinaBtn.setImageResource(R.drawable.ooame_hina_eng);
                        }
                        audioStop();
                        FragmentManager manager = getActivity().getSupportFragmentManager();
                        FragmentTransaction transaction = manager.beginTransaction();
                        /* もどるボタンで戻ってこれるように */
                        transaction.addToBackStack("hinan");
                        transaction.replace(R.id.fg1, new MapsFragmenrt2());
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
                audioStop();
                getParentFragmentManager().popBackStack();
            }
        });

        speakBtn = view.findViewById(R.id.speakbtn);
        speakBtn.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // The switch is checked.
                audioStart("ooame4", 0);
            } else {
                // The switch isn't checked.
                audioStop();
            }
        });
        speakBtn1 = view.findViewById(R.id.speakbtn2);
        speakBtn1.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // The switch is checked.
                audioStart("ooame4_1", 1);
            } else {
                // The switch isn't checked.
                audioStop();
            }
        });
        speakBtn2 = view.findViewById(R.id.speakbtn3);
        speakBtn2.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // The switch is checked.
                audioStart("ooame4_2", 2);
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
            case "ooame4":
                mediaPlayer = MediaPlayer.create(getContext(), R.raw.ooame4);
                break;
            case "ooame4_1":
                mediaPlayer = MediaPlayer.create(getContext(), R.raw.ooame4_1);
                break;
            case "ooame4_2":
                mediaPlayer = MediaPlayer.create(getContext(), R.raw.ooame4_2);
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
