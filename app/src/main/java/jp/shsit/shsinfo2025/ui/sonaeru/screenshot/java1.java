package jp.shsit.shsinfo2025.ui.sonaeru.screenshot;

import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import jp.shsit.shsinfo2025.MainActivity;
import jp.shsit.shsinfo2025.R;


/**
 * Created by s29336 on 2019/05/29.
 */

public class java1 extends Fragment {
    private final static int RESULT_CAMERA = 1001;
    private ImageView imageView;
    private Uri cameraUri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.java1, container, false);


        Button modoruButton = view.findViewById(R.id.mo1Button);
        modoruButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
            }
        });
        /*************言語選択*******************/
        String language= PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("lang", "日本語");
        MainActivity main = new MainActivity();
        /*****************************************/
        modoruButton.setText(main.LangReader("hinan",5,language));
        return view;

    }


}
