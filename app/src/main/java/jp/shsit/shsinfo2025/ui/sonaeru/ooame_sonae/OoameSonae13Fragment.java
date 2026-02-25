package jp.shsit.shsinfo2025.ui.sonaeru.ooame_sonae;

import android.graphics.Color;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;

import jp.shsit.shsinfo2025.MainActivity;
import jp.shsit.shsinfo2025.R;

public class OoameSonae13Fragment extends Fragment {
//TODO 下の結果を保持
    int level;
    ImageView imageView; String page;

    private ToggleButton speakBtn = null;
    private MediaPlayer mediaPlayer;
//切り替わる度
    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);


        getParentFragmentManager()
                .setFragmentResultListener("key", this, new FragmentResultListener() {
                    @Override
                    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {

                    }
                });
    }
//一度だけ
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_ooame1, container, false);
        imageView = root.findViewById(R.id.ooame1_imege1);
        page = "3";

        Integer result = PreferenceManager.getDefaultSharedPreferences(getActivity()).getInt(page+"lv",0);

        level=result;
        //言語選択
        String language= PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("lang", "日本語");
        MainActivity main = new MainActivity();

        //level = PreferenceManager.getDefaultSharedPreferences(getActivity()).getInt("level",0);
        if(language.equals("English")){
            switch (level) {
                case 1:
                    imageView.setImageResource(R.drawable.ooame_lv1_eng);
                    break;
                case 2:
                    imageView.setImageResource(R.drawable.ooame_lv2_eng);
                    break;
                case 3:
                    imageView.setImageResource(R.drawable.ooame_lv3_eng);
                    break;
                case 4:
                    imageView.setImageResource(R.drawable.ooame_lv4_eng);
                    break;
            }
        }else {
            switch (level) {
                case 1:
                    imageView.setImageResource(R.drawable.ooame_lv1);
                    break;
                case 2:
                    imageView.setImageResource(R.drawable.ooame_lv2);
                    break;
                case 3:
                    imageView.setImageResource(R.drawable.ooame_lv3);
                    break;
                case 4:
                    imageView.setImageResource(R.drawable.ooame_lv4);
                    break;
            }
        }
        TextView tv1 = root.<TextView>findViewById(R.id.ooame1_text1);
        tv1.setTextColor(Color.BLACK);
        SpannableStringBuilder sb = new SpannableStringBuilder();
        String s1= main.LangReader("ooame",0,language);
        sb.append(s1);
        int start = sb.length();
        //"「大雨時の行動整理」ボタンを押して、身の回りの災害リスクを明らかにし、大雨時の自分の行動を決めておきましょう。"
        String s2= main.LangReader("ooame",1,language);
        sb.append(s2);
        sb.setSpan(new RelativeSizeSpan(0.6f), start, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv1.setText(sb);

        TextView tv2 = root.findViewById(R.id.ooame1_text2);
        //"大雨の時のあなたの避難行動"
        String s3= main.LangReader("ooame",2,language);
        tv2.setText(s3);

        tv2.setTextColor(Color.BLACK);
        tv2.setTextSize(24);

      /*   int actlv = PreferenceManager.getDefaultSharedPreferences(getActivity()).getInt("actlv",0);

        switch (actlv){
            case 1:
                imageView.setImageResource(R.drawable.ooame_lv1);
                break;
            case 2:
                imageView.setImageResource(R.drawable.ooame_lv2);
                break;
            case 3:
                imageView.setImageResource(R.drawable.ooame_lv3);
                break;
            case 4:
                imageView.setImageResource(R.drawable.ooame_lv4);
                break;
        }*/

        /****************各種タイトル*****************************/
        //立ち退き避難先
        TextView title1 = root.findViewById(R.id.textView27);
        String t1= main.LangReader("ooame",3,language);
        title1.setText(t1);
        //移動手段・方法
        TextView title2 = root.findViewById(R.id.textView29);
        String t2= main.LangReader("ooame",6,language);
        title2.setText(t2);
        //誰と
        TextView title3 = root.findViewById(R.id.textView30);
        String t3= main.LangReader("ooame4",2,language);
        title3.setText(t3);
        //移動手段
        TextView title4 = root.findViewById(R.id.textView32);
        String t4= main.LangReader("ooame4",3,language);
        title4.setText(t4);
        //移動時間
        TextView title5 = root.findViewById(R.id.textView34);
        String t5= main.LangReader("ooame4",6,language);
        title5.setText(t5);

        /*******************結果**************************/

        TextView HinanText = root.findViewById(R.id.hinan_text);
        String ans4_1 = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(page+"q4",null);
        HinanText.setText(ans4_1);

        TextView HinanText2 = root.findViewById(R.id.hinan_text2);
        String ans4_2 = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(page+"q42",null);
        HinanText2.setText(ans4_2);

        TextView WhoText = root.findViewById(R.id.who_text);
        String ans5_who = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(page+"who",null);
        WhoText.setText(ans5_who);

        TextView HowText = root.findViewById(R.id.how_text);
        String ans5_how = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(page+"how",null);
        HowText.setText(ans5_how);

        TextView TimeText = root.findViewById(R.id.time_text);
        String ans5_pre = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(page+"pre","0");
        String  ans5_act = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(page+"act","0");
        int val1 = Integer.parseInt(ans5_pre);
        int val2 = Integer.parseInt(ans5_act);
        int total = val1+val2;
        TimeText.setText(String.valueOf(total));




        ImageView ooameBtn = root.findViewById(R.id.ooame1_btn1);
        if(language.equals("English")){
            ooameBtn.setImageResource(R.drawable.ooame_btn1_eng);
        }
//行動整理ボタン
        ooameBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        audioStop();
                        //タップした瞬間
                        ooameBtn.setImageResource(R.drawable.ooame_btn122);
                        if(language.equals("English")){
                            ooameBtn.setImageResource(R.drawable.ooame_btn1_eng_b);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        audioStop();
                        ooameBtn.setImageResource(R.drawable.ooame_btn1);
                        if(language.equals("English")){
                            ooameBtn.setImageResource(R.drawable.ooame_btn1_eng);
                        }
                        FragmentManager manager = getActivity().getSupportFragmentManager();
                        FragmentTransaction transaction = manager.beginTransaction();
                        //値渡し
                        Fragment fragment = new OoameSonae2Fragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("key","1");
                        fragment.setArguments(bundle);

                        /* もどるボタンで戻ってこれるように */
                        transaction.addToBackStack(null);
                        transaction.replace(R.id.fg1, fragment);
                        transaction.commit();
                        break;

                }
                return true;
            }
        });

       /* Button modoruBtn = root.findViewById(R.id.modorubtn);
        modoruBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
            }
        });
*/
        speakBtn = root.findViewById(R.id.speakbtn);

        speakBtn.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // The switch is checked.
                audioStart();
            } else {
                // The switch isn't checked.
                audioStop();
            }
        });

        return root;
    }

    private boolean audioSetup() {
        boolean fileCheck = true;
        // rawフォルダーから読み込む
        mediaPlayer = MediaPlayer.create(getContext(), R.raw.ooame);
        // 音量調整を端末のボタンに任せる
        getActivity().setVolumeControlStream(AudioManager.STREAM_MUSIC);

        return fileCheck;
    }

    private void audioStart() {
        audioSetup();
        mediaPlayer.start();
        speakBtn.setTextOn("");
        speakBtn.setBackgroundResource(R.drawable.baseline_volume_off_24);
        speakBtn.setChecked(true);
    }

    private void audioStop() {
        try {
            // 再生終了
            mediaPlayer.stop();
            speakBtn.setTextOff("");
            speakBtn.setBackgroundResource(R.drawable.baseline_volume_up_24);
            speakBtn.setChecked(false);
        } catch (Exception e) {

        }
    }

    public void onPause() {
        super.onPause();
        audioStop();
    }
}