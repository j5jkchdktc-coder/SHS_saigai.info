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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import jp.shsit.shsinfo2025.MainActivity;
import jp.shsit.shsinfo2025.R;

public class MochiPetFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_mochi_pet, container, false);

        MainActivity main = new MainActivity();
        String language = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("lang", "日本語");

        TextView tvTitle = root.findViewById(R.id.tv_title);
        RadioButton rbYes = root.findViewById(R.id.rb_yes);
        RadioButton rbNo = root.findViewById(R.id.rb_no);
        Button btnResult = root.findViewById(R.id.btn_result);

        tvTitle.setText(main.LangReader("mochi", 88, language));
        rbYes.setText(main.LangReader("mochi", 91, language));
        rbNo.setText(main.LangReader("mochi", 92, language));
        btnResult.setText(main.LangReader("mochi", 85, language));

        SharedPreferences prefs = getActivity().getSharedPreferences("MochiWizard", Context.MODE_PRIVATE);
        boolean hasPet = prefs.getBoolean("has_pet", false);
        if (hasPet) rbYes.setChecked(true);
        else rbNo.setChecked(true);

        btnResult.setOnClickListener(v -> {
            prefs.edit().putBoolean("has_pet", rbYes.isChecked()).apply();

            FragmentTransaction ft = getParentFragmentManager().beginTransaction();
            ft.replace(R.id.nav_host_fragment, new MochiResultFragment());
            ft.addToBackStack(null);
            ft.commit();
        });

        return root;
    }
}
