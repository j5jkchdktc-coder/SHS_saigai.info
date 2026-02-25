package jp.shsit.shsinfo2025.ui.sonaeru.ooame_sonae;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import jp.shsit.shsinfo2025.MainActivity;
import jp.shsit.shsinfo2025.R;

public class OoameSonae5Fragment extends Fragment {

    int val1=0,val2=0,total;
    String page;

    private ToggleButton speakBtn = null;
    private MediaPlayer mediaPlayer;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ooame5, container, false);
        page = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("page","");

        //言語選択
        String language= PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("lang", "日本語");
        MainActivity main = new MainActivity();

        TextView tv5 = view.findViewById(R.id.ooame5_text1);
        //問４　自分または一緒に避難される方は避難に時間がかかりますか？"
        String s1= main.LangReader("ooame4",0,language);
        tv5.setText(s1);


        RadioButton
                radio5_1 = view.findViewById(R.id.ooame5_radio1),
                radio5_2 = view.findViewById(R.id.ooame5_radio2);
        /***************言語選択**************************************/
        if(language.equals("English")){
            radio5_1.setText("Yes");
        }else{
            radio5_1.setText("はい");
        }
        if(language.equals("English")){
            radio5_2.setText("No");
        }else{
            radio5_2.setText("いいえ");
        }
        String s5= main.LangReader("ooame4",2,language);
        String s6= main.LangReader("ooame4",3,language);
        String s7= main.LangReader("ooame4",4,language);
        String s8= main.LangReader("ooame4",5,language);
        String s9= main.LangReader("ooame4",6,language);
        TextView textView5_2 = view.findViewById(R.id.ooame5_text2);
        TextView textView5_3 = view.findViewById(R.id.ooame5_text3);
        TextView textView5_4 = view.findViewById(R.id.ooame5_text4);
        TextView textView5_6 = view.findViewById(R.id.ooame5_text6);
        textView5_2.setText(s5);
        textView5_3.setText(s6);
        textView5_4.setText(s7);
        textView5_6.setText(s8);
        /************************************************************/
        TableLayout
                tableLayout5_1 = view.findViewById(R.id.table_group5_1);

        EditText
                editText5_1 = view.findViewById(R.id.ooame5_edit1),
                editText5_2 = view.findViewById(R.id.ooame5_edit2),
                editText5_3 = view.findViewById(R.id.ooame5_edit3),
                editText5_4 = view.findViewById(R.id.ooame5_edit4);

        TextView textView5_8 = view.findViewById(R.id.ooame5_text8);
        //"合計0分"
        textView5_8.setText(s9);

        //プレファランスによる値読み出し
        Boolean  ans5_1 = PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean(page+"q5_1",false);
        radio5_1.setChecked(ans5_1);
        radio5_2.setChecked(!ans5_1);

        LinearLayout linea = view.findViewById(R.id.ooame5_linea);

        if (ans5_1) {
            //tableLayout5_1.setVisibility(View.VISIBLE);
            linea.setVisibility(View.VISIBLE);

        } else {
          //tableLayout5_1.setVisibility(View.INVISIBLE);
          linea.setVisibility(View.INVISIBLE);

        }

        String
                ans5_who = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(page+"who",null),
                ans5_how = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(page+"how",null),
                ans5_pre = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(page+"pre","0"),
                ans5_act = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(page+"act","0");

        Log.i("test",ans5_who + "," + ans5_how + "," + ans5_pre + "," + ans5_act);

        editText5_1.setText(ans5_who);
        editText5_2.setText(ans5_how);
        editText5_3.setText(ans5_pre);
        editText5_4.setText(ans5_act);

        val1 = Integer.parseInt(editText5_3.getText().toString());
        val2 = Integer.parseInt(editText5_4.getText().toString());
        total = val1+val2;
        textView5_8.setText("合計" + total + "分");

        radio5_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audioStop();
                if (radio5_1.isChecked()) {
                   // tableLayout5_1.setVisibility(View.VISIBLE);
                    linea.setVisibility(View.VISIBLE);
                }
            }
        });

        radio5_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audioStop();
                if (radio5_2.isChecked()) {
                    //tableLayout5_1.setVisibility(View.INVISIBLE);
                    //linea.setVisibility(View.INVISIBLE);
                    linea.setVisibility(View.VISIBLE);
                }
            }
        });

        SharedPreferences prefer = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = prefer.edit();
        //prefer.edit().remove("Google").commit();

        editText5_3.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    // フォーカスされた場合
                    audioStop();
                }else{
                    // フォーカスが外れた場合
                    if (editText5_3.getText().toString().length() == 0) {
                        editText5_3.setText("0");
                        val1 = 0;
                    }
                    else {
                        val1 = Integer.parseInt(editText5_3.getText().toString());
                    }
                    total = val1+val2;
                    textView5_8.setText("合計" + total + "分");
                }
            }
        });
        editText5_3.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE){

                    //    Enterが押されたときに行いたい処理
                    editText5_3.clearFocus();

                }
                return false;
            }
        });

        editText5_4.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    // フォーカスされた場合
                    audioStop();
                }else{
                    // フォーカスが外れた場合
                    if (editText5_4.getText().toString().length() == 0) {
                        editText5_4.setText("0");
                        val2 = 0;
                    }
                    else {
                        val2 = Integer.parseInt(editText5_4.getText().toString());
                    }
                    total = val1+val2;
                    textView5_8.setText("合計" + total + "分");
                }
            }
        });
        editText5_4.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE){

                    //    Enterが押されたときに行いたい処理
                    editText5_4.clearFocus();

                }
                return false;
            }
        });


        //次へ
        ImageView btn2 = view.findViewById(R.id.bt_to6);
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
                        editor.putBoolean(page+"q5_1",radio5_1.isChecked());
                        editor.putString(page+"who",editText5_1.getText().toString());
                        editor.putString(page+"how",editText5_2.getText().toString());
                        editor.putString(page+"pre",editText5_3.getText().toString());
                        editor.putString(page+"act",editText5_4.getText().toString());
                        editor.commit();

                        if(radio5_1.isChecked()) {
                            //警戒レベル３でトップ画面に移動
                            audioStop();
                            SharedPreferences prefer = PreferenceManager.getDefaultSharedPreferences(getContext());
                            SharedPreferences.Editor editor = prefer.edit();
                            editor.putInt(page+"lv",3);
                            editor.commit();
                            getParentFragmentManager().popBackStack("top",0);

                        }else{
                            audioStop();
                            FragmentManager manager = getActivity().getSupportFragmentManager();
                            FragmentTransaction transaction = manager.beginTransaction();
                            /* もどるボタンで戻ってこれるように */
                            transaction.addToBackStack(null);
                            transaction.replace(R.id.fg1, new OoameSonae6Fragment());
                            transaction.commit();

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
        mediaPlayer = MediaPlayer.create(getContext(), R.raw.ooame5);
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