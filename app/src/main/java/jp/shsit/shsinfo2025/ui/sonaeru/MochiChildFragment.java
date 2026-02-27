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

public class MochiChildFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_mochi_child, container, false);

        MainActivity main = new MainActivity();
        String language = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("lang", "日本語");

        TextView tvTitle = root.findViewById(R.id.tv_title);
        RadioButton rbNone = root.findViewById(R.id.rb_none);
        RadioButton rbChild = root.findViewById(R.id.rb_child);
        RadioButton rbBaby = root.findViewById(R.id.rb_baby);
        RadioButton rbElderly = root.findViewById(R.id.rb_elderly);
        Button btnNext = root.findViewById(R.id.btn_next);

        tvTitle.setText(main.LangReader("mochi", 73, language));
        rbNone.setText(main.LangReader("mochi", 83, language));
        rbChild.setText(main.LangReader("mochi", 89, language));
        rbBaby.setText(main.LangReader("mochi", 90, language));
        rbElderly.setText(main.LangReader("mochi", 80, language));
        btnNext.setText(main.LangReader("mochi", 84, language));

        SharedPreferences prefs = getActivity().getSharedPreferences("MochiWizard", Context.MODE_PRIVATE);
        String savedStatus = prefs.getString("family_status", "none");
        if (savedStatus.equals("none")) rbNone.setChecked(true);
        else if (savedStatus.equals("child")) rbChild.setChecked(true);
        else if (savedStatus.equals("baby")) rbBaby.setChecked(true);
        else if (savedStatus.equals("elderly")) rbElderly.setChecked(true);

        btnNext.setOnClickListener(v -> {
            String status = "none";
            if (rbChild.isChecked()) status = "child";
            else if (rbBaby.isChecked()) status = "baby";
            else if (rbElderly.isChecked()) status = "elderly";

            prefs.edit().putString("family_status", status).apply();

            FragmentTransaction ft = getParentFragmentManager().beginTransaction();
            ft.replace(R.id.nav_host_fragment, new MochiPetFragment());
            ft.addToBackStack(null);
            ft.commit();
        });

        return root;
    }
}
