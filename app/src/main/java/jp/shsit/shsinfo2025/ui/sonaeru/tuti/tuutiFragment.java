package jp.shsit.shsinfo2025.ui.sonaeru.tuti;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import jp.shsit.shsinfo2025.MainActivity;
import jp.shsit.shsinfo2025.R;


public class tuutiFragment extends Fragment {


    private static final String PREF_NAME = "toggle_prefs";
    private static final String KEY_TOGGLE1_STATE = "toggle1_state";
    private static final String KEY_TOGGLE2_STATE = "toggle2_state";
    private Switch toggleSwitch1;
    private Switch toggleSwitch2;

    String language;

    MainActivity main = new MainActivity();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_tuuti, container, false);
        View root = inflater.inflate(R.layout.fragment_tuuti, container, false);
        language = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("lang", "日本語");

//        Button example_button = root.findViewById(R.id.example);
        TextView text1 = root.findViewById(R.id.textView33);
        text1.setText( main.LangReader("tuti",0,language));

        Switch switch1 = root.findViewById(R.id.switch1);
        switch1.setText( main.LangReader("tuti",1,language));

        Switch switch2 = root.findViewById(R.id.switch2);
        switch2.setText( main.LangReader("tuti",2,language));



        return root;

    }





    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        toggleSwitch1 = view.findViewById(R.id.switch1);
        toggleSwitch2 = view.findViewById(R.id.switch2);

        // SharedPreferencesからトグルスイッチの状態を読み込む
        SharedPreferences preferences = requireActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        boolean isChecked1 = preferences.getBoolean(KEY_TOGGLE1_STATE, false);  // トグルスイッチ1のデフォルトはOFF
        boolean isChecked2 = preferences.getBoolean(KEY_TOGGLE2_STATE, false);  // トグルスイッチ2のデフォルトはOFF

        // トグルスイッチの状態を設定
        toggleSwitch1.setChecked(isChecked1);
        toggleSwitch2.setChecked(isChecked2);

        // トグルスイッチ1の状態変化を監視して保存
        toggleSwitch1.setOnCheckedChangeListener((buttonView, checked) -> {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(KEY_TOGGLE1_STATE, checked);
            editor.apply();  // 非同期で保存
        });

        // トグルスイッチ2の状態変化を監視して保存
        toggleSwitch2.setOnCheckedChangeListener((buttonView, checked) -> {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(KEY_TOGGLE2_STATE, checked);
            editor.apply();  // 非同期で保存
        });



        Button backBtn = view.findViewById(R.id.back_btn);

        backBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.popBackStack();
                return true;
            }
        });

    }
}