package jp.shsit.shsinfo2025.ui.sonaeru;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
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

        // Question 1: Gender
        TextView genderTitle = root.findViewById(R.id.wizard_gender_title);
        RadioButton radioMale = root.findViewById(R.id.radio_male);
        RadioButton radioFemale = root.findViewById(R.id.radio_female);
        RadioButton radioOther = root.findViewById(R.id.radio_other);

        genderTitle.setText(main.LangReader("wizard", 0, language));
        radioMale.setText(main.LangReader("wizard", 1, language));
        radioFemale.setText(main.LangReader("wizard", 2, language));
        radioOther.setText(main.LangReader("wizard", 3, language));

        // Question 2: Children
        TextView childTitle = root.findViewById(R.id.wizard_child_title);
        CheckBox checkChild = root.findViewById(R.id.check_child_present);
        CheckBox checkBaby = root.findViewById(R.id.check_baby_present);

        childTitle.setText(main.LangReader("wizard", 5, language));
        checkChild.setText(main.LangReader("wizard", 7, language));
        checkBaby.setText(main.LangReader("wizard", 8, language));

        // Question 3: Pets
        TextView petTitle = root.findViewById(R.id.wizard_pet_title);
        CheckBox checkPet = root.findViewById(R.id.check_pet_present);

        petTitle.setText(main.LangReader("wizard", 9, language));
        checkPet.setText(main.LangReader("wizard", 10, language));

        // Result Button
        Button btnResult = root.findViewById(R.id.btn_wizard_result);
        btnResult.setText(main.LangReader("wizard", 12, language));

        SharedPreferences prefs = getActivity().getSharedPreferences("WizardData", Context.MODE_PRIVATE);

        // Load Gender
        String savedGender = prefs.getString("gender", "");
        if (savedGender.equals("male")) {
            radioMale.setChecked(true);
        } else if (savedGender.equals("female")) {
            radioFemale.setChecked(true);
        } else if (savedGender.equals("other")) {
            radioOther.setChecked(true);
        }

        // Load Child/Baby status
        checkChild.setChecked(prefs.getBoolean("child_present", false));
        checkBaby.setChecked(prefs.getBoolean("baby_present", false));

        // Load Pet status
        checkPet.setChecked(prefs.getBoolean("pet_present", false));

        btnResult.setOnClickListener(v -> {
            SharedPreferences.Editor editor = prefs.edit();

            // Save Gender
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
            editor.putString("gender", gender);

            // Save Child/Baby status
            editor.putBoolean("child_present", checkChild.isChecked());
            editor.putBoolean("baby_present", checkBaby.isChecked());

            // Save Pet status
            editor.putBoolean("pet_present", checkPet.isChecked());

            editor.apply();

            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.nav_host_fragment, new WizardResultFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        });

        return root;
    }
}
