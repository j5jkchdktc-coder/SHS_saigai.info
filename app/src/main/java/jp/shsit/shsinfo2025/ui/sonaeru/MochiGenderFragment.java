package jp.shsit.shsinfo2025.ui.sonaeru;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import jp.shsit.shsinfo2025.MainActivity;
import jp.shsit.shsinfo2025.R;

public class MochiGenderFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_mochi_gender, container, false);

        MainActivity main = new MainActivity();
        String language = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("lang", "日本語");

        TextView tvTitle = root.findViewById(R.id.tv_title);
        RadioButton rbMale = root.findViewById(R.id.rb_male);
        RadioButton rbFemale = root.findViewById(R.id.rb_female);
        RadioButton rbOther = root.findViewById(R.id.rb_other);
        Button btnNext = root.findViewById(R.id.btn_next);

        tvTitle.setText(main.LangReader("mochi", 86, language));
        rbMale.setText(main.LangReader("mochi", 67, language));
        rbFemale.setText(main.LangReader("mochi", 78, language));
        rbOther.setText(main.LangReader("mochi", 82, language));
        btnNext.setText(main.LangReader("mochi", 84, language));

        SharedPreferences prefs = getActivity().getSharedPreferences("MochiWizard", Context.MODE_PRIVATE);
        String savedGender = prefs.getString("gender", "male");
        if (savedGender.equals("male")) rbMale.setChecked(true);
        else if (savedGender.equals("female")) rbFemale.setChecked(true);
        else rbOther.setChecked(true);

        btnNext.setOnClickListener(v -> {
            String selectedGender = "other";
            if (rbMale.isChecked()) selectedGender = "male";
            else if (rbFemale.isChecked()) selectedGender = "female";

            prefs.edit().putString("gender", selectedGender).apply();

            FragmentTransaction ft = getParentFragmentManager().beginTransaction();
            ft.replace(R.id.nav_host_fragment, new MochiChildFragment());
            ft.addToBackStack(null);
            ft.commit();
        });

        return root;
    }
}
