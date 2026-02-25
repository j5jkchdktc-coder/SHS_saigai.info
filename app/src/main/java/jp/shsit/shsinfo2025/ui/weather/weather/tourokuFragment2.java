package jp.shsit.shsinfo2025.ui.weather.weather;

import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import jp.shsit.shsinfo2025.R;


public class tourokuFragment2  extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pref_city, container, false);
        view.setBackgroundColor(Color.WHITE);

        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.r_f1, new RssprefFragment());
        transaction.replace(R.id.f2, new RssCityFragment());
        transaction.commit();

        Button modoru = view.findViewById(R.id.modorubtn2);
        TextView prefText = view.findViewById(R.id.point_pref_text);
        TextView cityText = view.findViewById(R.id.point_city_text);
        String language= PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("lang", "日本語");

        if(language.equals("English")) {
            modoru.setText("Back");
            prefText.setText("Prefectures");
            cityText.setText("City");
        }else if(language.equals("Vietnamese")){
            modoru.setText("Quay lại");
            prefText.setText("các quận");
            cityText.setText("Thành phố");
        }else if(language.equals("Chinese")){
            modoru.setText("返回");
            prefText.setText("都道府县");
            cityText.setText("城市");
        }else {
            modoru.setText("もどる");
            prefText.setText("都道府県");
            cityText.setText("市町村");
        }

        modoru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
            }
        });


        view.setBackgroundColor(Color.rgb(251,247,192));
        return view;
    }
}