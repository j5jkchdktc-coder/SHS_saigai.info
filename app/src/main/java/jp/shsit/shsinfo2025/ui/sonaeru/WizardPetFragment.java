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

public class WizardPetFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_wizard_pet, container, false);

        String language = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("lang", "日本語");
        MainActivity main = (MainActivity) getActivity();

        TextView title = root.findViewById(R.id.wizard_pet_title);
        RadioButton radioPresent = root.findViewById(R.id.radio_pet_present);
        RadioButton radioNone = root.findViewById(R.id.radio_pet_none);
        Button btnResult = root.findViewById(R.id.btn_pet_result);

        title.setText(main.LangReader("wizard", 9, language));
        radioPresent.setText(main.LangReader("wizard", 10, language));
        radioNone.setText(main.LangReader("wizard", 11, language));
        btnResult.setText(main.LangReader("wizard", 12, language));

        SharedPreferences prefs = getActivity().getSharedPreferences("WizardData", Context.MODE_PRIVATE);
        String savedPet = prefs.getString("pet_status", "");
        if (savedPet.equals("yes")) {
            radioPresent.setChecked(true);
        } else if (savedPet.equals("no")) {
            radioNone.setChecked(true);
        }

        btnResult.setOnClickListener(v -> {
            RadioGroup rg = root.findViewById(R.id.pet_radio_group);
            int selectedId = rg.getCheckedRadioButtonId();
            String status = "";
            if (selectedId == R.id.radio_pet_present) {
                status = "yes";
            } else if (selectedId == R.id.radio_pet_none) {
                status = "no";
            }

            if (!status.isEmpty()) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("pet_status", status);
                editor.apply();

                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, new WizardResultFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return root;
    }
}
