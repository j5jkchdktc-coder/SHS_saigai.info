package jp.shsit.shsinfo2025.ui.sonaeru.ooame_sonae;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import jp.shsit.shsinfo2025.R;

public class Fragment4 extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_ooame_fragment4, container, false);

        EditText editText = root.findViewById(R.id.ed4);
        String page = "4";
        String key = page +"space";
        String space = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(key,"");
        editText.setText(space);
        ImageView button = root.findViewById(R.id.button4);

        TextView tv = root.findViewById(R.id.textspace4);
        String language= PreferenceManager.getDefaultSharedPreferences(getContext()).getString("lang", "日本語");
        if(language.equals("English")||language.equals("Vietnamese")||language.equals("Chinese")){
            tv.setText("place");
            editText.setHint("Please enter a location");
            button.setImageResource(R.drawable.save_en);
        }

        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        button.setImageResource(R.drawable.save2);
                        if(language.equals("English")||language.equals("Vietnamese")||language.equals("Chinese")){
                            button.setImageResource(R.drawable.save_en2);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        button.setImageResource(R.drawable.save);
                        if(language.equals("English")||language.equals("Vietnamese")||language.equals("Chinese")){
                            button.setImageResource(R.drawable.save_en);
                        }
                        SharedPreferences prefer = PreferenceManager.getDefaultSharedPreferences(getContext());
                        SharedPreferences.Editor editor = prefer.edit();
                        editor.putString(key,editText.getText().toString());
                        editor.commit();

                        break;

                }
                return true;
            }
        });

        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        /* もどるボタンで戻ってこれるように */

        transaction.replace(R.id.ooame_fragment4, new OoameSonae14Fragment());
        transaction.commit();

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences prefer = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = prefer.edit();
        editor.putString("page","4");
        editor.commit();
        Log.i("test","4表示");

    }
}
