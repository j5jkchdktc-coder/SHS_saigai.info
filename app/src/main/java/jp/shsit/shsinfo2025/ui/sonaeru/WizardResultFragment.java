package jp.shsit.shsinfo2025.ui.sonaeru;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jp.shsit.shsinfo2025.MainActivity;
import jp.shsit.shsinfo2025.R;

public class WizardResultFragment extends Fragment {

    private ListView listView;
    private List<String> stockpileItems;
    private List<Boolean> checkedStates;
    private SharedPreferences wizardPrefs;
    private SharedPreferences checkPrefs;
    private String language;
    private MainActivity main;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_wizard_result, container, false);

        language = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("lang", "日本語");
        main = (MainActivity) getActivity();
        wizardPrefs = getActivity().getSharedPreferences("WizardData", Context.MODE_PRIVATE);
        checkPrefs = getActivity().getSharedPreferences("WizardChecks", Context.MODE_PRIVATE);

        TextView title = root.findViewById(R.id.wizard_result_title);
        title.setText(main.LangReader("wizard", 13, language));

        Button btnBack = root.findViewById(R.id.btn_result_back);
        btnBack.setText(main.LangReader("hinan", 5, language));
        btnBack.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        listView = root.findViewById(R.id.wizard_result_list);

        generateStockpileList();

        StockpileAdapter adapter = new StockpileAdapter(getContext(), stockpileItems);
        listView.setAdapter(adapter);

        return root;
    }

    private void generateStockpileList() {
        stockpileItems = new ArrayList<>();
        checkedStates = new ArrayList<>();

        // Common items
        stockpileItems.add(main.LangReader("wizard", 14, language)); // Water
        stockpileItems.add(main.LangReader("wizard", 15, language)); // Food
        stockpileItems.add(main.LangReader("wizard", 16, language)); // Flashlight
        stockpileItems.add(main.LangReader("wizard", 17, language)); // Mobile Battery

        // Gender specific
        String gender = wizardPrefs.getString("gender", "");
        if (gender.equals("female")) {
            stockpileItems.add(main.LangReader("wizard", 18, language)); // Sanitary
        }

        // Child specific
        String childStatus = wizardPrefs.getString("child_status", "");
        if (childStatus.equals("child")) {
            stockpileItems.add(main.LangReader("wizard", 19, language)); // Child food
        } else if (childStatus.equals("baby")) {
            stockpileItems.add(main.LangReader("wizard", 20, language)); // Milk
            stockpileItems.add(main.LangReader("wizard", 21, language)); // Diaper
        }

        // Pet specific
        String petStatus = wizardPrefs.getString("pet_status", "");
        if (petStatus.equals("yes")) {
            stockpileItems.add(main.LangReader("wizard", 22, language)); // Pet food
            stockpileItems.add(main.LangReader("wizard", 23, language)); // Pet water
        }

        // Load checked states
        Set<String> checkedItems = checkPrefs.getStringSet("checked_items", new HashSet<>());
        for (String item : stockpileItems) {
            checkedStates.add(checkedItems.contains(item));
        }
    }

    private class StockpileAdapter extends ArrayAdapter<String> {
        public StockpileAdapter(Context context, List<String> items) {
            super(context, 0, items);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
            }

            String item = getItem(position);
            TextView textView = convertView.findViewById(R.id.text_view);
            CheckBox checkBox = convertView.findViewById(R.id.checkBox);

            // Hide elements not used in this simplified view if necessary,
            // but list_item has img_item and text_menber
            View img = convertView.findViewById(R.id.img_item);
            if (img != null) img.setVisibility(View.GONE);
            View member = convertView.findViewById(R.id.text_menber);
            if (member != null) member.setVisibility(View.GONE);

            textView.setText(item);
            checkBox.setChecked(checkedStates.get(position));

            checkBox.setOnClickListener(v -> {
                boolean isChecked = checkBox.isChecked();
                checkedStates.set(position, isChecked);
                saveCheckedStates();
            });

            return convertView;
        }
    }

    private void saveCheckedStates() {
        Set<String> checkedItems = new HashSet<>();
        for (int i = 0; i < stockpileItems.size(); i++) {
            if (checkedStates.get(i)) {
                checkedItems.add(stockpileItems.get(i));
            }
        }
        checkPrefs.edit().putStringSet("checked_items", checkedItems).apply();
    }
}
