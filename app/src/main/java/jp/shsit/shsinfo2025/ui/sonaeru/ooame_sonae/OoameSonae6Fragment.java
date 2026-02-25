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
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import jp.shsit.shsinfo2025.MainActivity;
import jp.shsit.shsinfo2025.R;

public class OoameSonae6Fragment extends Fragment {
    //TODO 終了後fragmentが残る
    String page;

    private ToggleButton speakBtn = null;
    private MediaPlayer mediaPlayer;
    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ooame6, container, false);
        page = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("page","");

       // TextView
       //         tv6_2 = view.findViewById(R.id.ooame6_text2),
       //         tv6_3 = view.findViewById(R.id.ooame6_text3);
      //  tv6_1.setText("自宅に、予想が困難な災害リスクはありますか？");
      //  tv6_2.setText("急激な水位上昇のおそれがある\n河川が近くにある");
     //   tv6_3.setText("土砂災害のリスクがある");

        //言語選択
        String language= PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("lang", "日本語");
        MainActivity main = new MainActivity();

        TextView tv6_1 = view.findViewById(R.id.ooame6_text1);

        SpannableStringBuilder sb = new SpannableStringBuilder();
        //問5　"自宅に、予想が困難な災害リスクはありますか？"
        String s1= main.LangReader("ooame5",0,language);
        sb.append(s1);
        int start = sb.length();

        String s2= main.LangReader("ooame5",1,language);
        sb.append(s2);
        sb.setSpan(new RelativeSizeSpan(1.0f), start, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //"急激な水位上昇のおそれがある\n河川が近くにある"
        String s3= main.LangReader("ooame5",2,language);
        sb.append("\n");
        start = sb.length();
        sb.append(s3);
        sb.setSpan(new RelativeSizeSpan(0.6f), start, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // "土砂災害のリスクがある"
        String s4= main.LangReader("ooame5",3,language);
        sb.append("\n");
        start = sb.length();
        sb.append(s4);
        sb.setSpan(new RelativeSizeSpan(0.6f), start, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        tv6_1.setText(sb);

        RadioButton
                radio6_1 = view.findViewById(R.id.ooame6_radio1),
                radio6_2 = view.findViewById(R.id.ooame6_radio2);



        //プレファランスによる値読み出し
        //radiobutton
        Boolean  ans6_1 = PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean(page+"q6_1",false);
        radio6_1.setChecked(ans6_1);
        radio6_2.setChecked(!ans6_1);






        SharedPreferences prefer = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = prefer.edit();
        //次へ
        ImageView btn2 = view.findViewById(R.id.bt_to7);
        if(language.equals("English")){
            btn2.setImageResource(R.drawable.ooame_hantei_eng);
        }
        btn2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        btn2.setImageResource(R.drawable.ooame_hanteibtn22);
                        if(language.equals("English")){
                            btn2.setImageResource(R.drawable.ooame_hantei_eng_b);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        btn2.setImageResource(R.drawable.ooame_hanteibtn);
                        if(language.equals("English")){
                            btn2.setImageResource(R.drawable.ooame_hantei_eng);
                        }
                        editor.putBoolean(page+"q6_1",radio6_1.isChecked());

                        editor.commit();

                        //画面リセット
                        // FragmentManager manager = getActivity().getSupportFragmentManager();
                        // FragmentTransaction transaction = manager.beginTransaction();
                        // transaction.replace(R.id.nav_host_fragment, new EmptyFragment());
                        // transaction.commit();

                        if(radio6_1.isChecked()) {
                            audioStop();
                            SharedPreferences prefer = PreferenceManager.getDefaultSharedPreferences(getContext());
                            SharedPreferences.Editor editor = prefer.edit();
                            editor.putInt(page+"lv",3);
                            editor.commit();
                            getParentFragmentManager().popBackStack("top",0);

                        }else{
                            audioStop();
                            SharedPreferences prefer = PreferenceManager.getDefaultSharedPreferences(getContext());
                            SharedPreferences.Editor editor = prefer.edit();
                            editor.putInt(page+"lv",4);
                            editor.commit();
                            getParentFragmentManager().popBackStack("top",0);

                        }
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
                audioStart();
            } else {
                // The switch isn't checked.
                audioStop();
            }
        });

        return view;
    }

    private boolean audioSetup() {
        boolean fileCheck = true;
        // rawフォルダーから読み込む
        mediaPlayer = MediaPlayer.create(getContext(), R.raw.ooame6);
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