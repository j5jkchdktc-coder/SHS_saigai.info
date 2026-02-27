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

public class WizardGenderFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_wizard_gender, container, false);

        String language = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("lang", "日本語");
        MainActivity main = (MainActivity) getActivity();

        TextView title = root.findViewById(R.id.wizard_gender_title);
        RadioButton radioMale = root.findViewById(R.id.radio_male);
        RadioButton radioFemale = root.findViewById(R.id.radio_female);
        RadioButton radioOther = root.findViewById(R.id.radio_other);
        Button btnNext = root.findViewById(R.id.btn_gender_next);

        title.setText(main.LangReader("wizard", 0, language));
        radioMale.setText(main.LangReader("wizard", 1, language));
        radioFemale.setText(main.LangReader("wizard", 2, language));
        radioOther.setText(main.LangReader("wizard", 3, language));
        btnNext.setText(main.LangReader("wizard", 4, language));

        SharedPreferences prefs = getActivity().getSharedPreferences("WizardData", Context.MODE_PRIVATE);
        String savedGender = prefs.getString("gender", "");
        if (savedGender.equals("male")) {
            radioMale.setChecked(true);
        } else if (savedGender.equals("female")) {
            radioFemale.setChecked(true);
        } else if (savedGender.equals("other")) {
            radioOther.setChecked(true);
        }

        btnNext.setOnClickListener(v -> {
            RadioGroup rg = root.findViewById(R.id.gender_radio_group);
            int selectedId = rg.getCheckedRadioButtonId();
            String gender = "";
            if (selectedId == R.id.radio_male) {
                gender = "male";
            } else if (selectedId == R.id.radio_female) {
                gender = "female";
            } else if (selectedId == R.id.radio_other) {
                gender = "other";
            }

            if (!gender.isEmpty()) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("gender", gender);
                editor.apply();

                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, new WizardChildFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return root;
    }
}
