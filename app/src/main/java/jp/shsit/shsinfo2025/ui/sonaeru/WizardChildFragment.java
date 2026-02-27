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

public class WizardChildFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_wizard_child, container, false);

        String language = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("lang", "日本語");
        MainActivity main = (MainActivity) getActivity();

        TextView title = root.findViewById(R.id.wizard_child_title);
        RadioButton radioNone = root.findViewById(R.id.radio_child_none);
        RadioButton radioChild = root.findViewById(R.id.radio_child_present);
        RadioButton radioBaby = root.findViewById(R.id.radio_baby_present);
        Button btnNext = root.findViewById(R.id.btn_child_next);

        title.setText(main.LangReader("wizard", 5, language));
        radioNone.setText(main.LangReader("wizard", 6, language));
        radioChild.setText(main.LangReader("wizard", 7, language));
        radioBaby.setText(main.LangReader("wizard", 8, language));
        btnNext.setText(main.LangReader("wizard", 4, language));

        SharedPreferences prefs = getActivity().getSharedPreferences("WizardData", Context.MODE_PRIVATE);
        String savedChild = prefs.getString("child_status", "");
        if (savedChild.equals("none")) {
            radioNone.setChecked(true);
        } else if (savedChild.equals("child")) {
            radioChild.setChecked(true);
        } else if (savedChild.equals("baby")) {
            radioBaby.setChecked(true);
        }

        btnNext.setOnClickListener(v -> {
            RadioGroup rg = root.findViewById(R.id.child_radio_group);
            int selectedId = rg.getCheckedRadioButtonId();
            String status = "";
            if (selectedId == R.id.radio_child_none) {
                status = "none";
            } else if (selectedId == R.id.radio_child_present) {
                status = "child";
            } else if (selectedId == R.id.radio_baby_present) {
                status = "baby";
            }

            if (!status.isEmpty()) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("child_status", status);
                editor.apply();

                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, new WizardPetFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return root;
    }
}
