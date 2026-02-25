package jp.shsit.shsinfo2025.ui.uses;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import jp.shsit.shsinfo2025.MainActivity;
import jp.shsit.shsinfo2025.R;


public class Kikikuru_use extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_kikikuru_use, container, false);
        // CustomImageView cv = view.findViewById(R.id.cusimageview);
        //cv.setImageResource(R.drawable.tukaikata2);

        Button modoruBtn = view.findViewById(R.id.ns1Button);
        MainActivity main = new MainActivity();
        String language= PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("lang", "日本語");
        modoruBtn.setText(main.LangReader("hinan",5,language));
        modoruBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
            }
        });
        return view;

    }

    @Override
    public void onPause() {
        super.onPause();
        getFragmentManager().beginTransaction().remove(this).commit();

    }
}
