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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.app.AlertDialog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jp.shsit.shsinfo2025.ItemLang;
import jp.shsit.shsinfo2025.MainActivity;
import jp.shsit.shsinfo2025.R;
import jp.shsit.shsinfo2025.SplashActivity;

public class WizardResultFragment extends Fragment {

    private ListView listView;
    private List<String> stockpileItems;
    private List<Boolean> checkedStates;
    private Map<String, String> itemDescriptions = new HashMap<>();
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

        TextView hintText = root.findViewById(R.id.wizard_result_hint);
        hintText.setText(main.LangReader("mochi", 55, language)); // "Long-press description"

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
        itemDescriptions.clear();

        // 1. Collect required items based on wizard preferences
        List<String> requiredNames = new ArrayList<>();

        // Common
        requiredNames.add(main.LangReader("wizard", 14, language)); // Water
        requiredNames.add(main.LangReader("wizard", 15, language)); // Food
        requiredNames.add(main.LangReader("wizard", 16, language)); // Flashlight
        requiredNames.add(main.LangReader("wizard", 17, language)); // Mobile Battery
        requiredNames.add(main.LangReader("wizard", 25, language)); // Clothing
        requiredNames.add(main.LangReader("mochi", 58, language)); // Portable toilet (New)

        // Gender specific
        String gender = wizardPrefs.getString("gender", "");
        if ("female".equals(gender)) {
            requiredNames.add(main.LangReader("mochi", 57, language)); // Items for women (New)
        } else if ("male".equals(gender)) {
            requiredNames.add(main.LangReader("mochi", 75, language)); // Items for men (New)
        }

        // Child specific
        if (wizardPrefs.getBoolean("child_present", false)) {
            requiredNames.add(main.LangReader("mochi", 69, language)); // Items for children (New)
        }
        if (wizardPrefs.getBoolean("baby_present", false)) {
            requiredNames.add(main.LangReader("mochi", 13, language)); // Baby needs (New)
        }

        // Pet specific
        if (wizardPrefs.getBoolean("pet_present", false)) {
            requiredNames.add(main.LangReader("mochi", 59, language)); // Items for pets (New)
        }

        // Elderly specific
        if (wizardPrefs.getBoolean("elderly_present", false)) {
            requiredNames.add(main.LangReader("mochi", 56, language)); // Items for elderly (New)
        }

        // 2. Fetch descriptions from CSV for these items
        // Since LangReader returns the translated name, we'll try to match the name in the CSV
        // In this app, itemArray in SplashActivity contains all CSV data.
        ArrayList<ItemLang> items = SplashActivity.itemArray;
        for (String name : requiredNames) {
            if (name == null || name.isEmpty()) continue;
            stockpileItems.add(name);

            // Look for matching description in 'mochi' page
            String description = "";
            String keyName = ""; // used to find description

            // First pass: find the ID or some link to description.
            // The CSV has names (rows 0-14, 56-59, etc) and descriptions (rows 15-53, 60-66, etc).
            // This is a bit complex. Let's try to map them.
            description = findDescriptionFor(name);
            itemDescriptions.put(name, description);
        }

        // Load checked states
        Set<String> checkedItems = checkPrefs.getStringSet("checked_items", new HashSet<>());
        for (String item : stockpileItems) {
            checkedStates.add(checkedItems.contains(item));
        }
    }

    private String findDescriptionFor(String translatedName) {
        ArrayList<ItemLang> items = SplashActivity.itemArray;

        // Find the index or Japanese name first to identify the item
        String japName = "";
        int foundNo = -1;
        for (ItemLang item : items) {
            if ("mochi".equals(item.getPage().toString())) {
                if (translatedName.equals(item.getJap().toString()) ||
                    translatedName.equals(item.getEng().toString()) ||
                    translatedName.equals(item.getVnm().toString()) ||
                    translatedName.equals(item.getChn().toString()) ||
                    translatedName.equals(item.getSmall().toString())) {
                    japName = item.getJap().toString();
                    try {
                        foundNo = Integer.parseInt(item.getNo().toString());
                    } catch (Exception e) {}
                    break;
                }
            }
            if ("wizard".equals(item.getPage().toString())) {
                 if (translatedName.equals(item.getJap().toString()) ||
                    translatedName.equals(item.getEng().toString()) ||
                    translatedName.equals(item.getVnm().toString()) ||
                    translatedName.equals(item.getChn().toString()) ||
                    translatedName.equals(item.getSmall().toString())) {
                    japName = item.getJap().toString();
                    // Maps wizard items to mochi page equivalent if needed
                    if (japName.equals("水")) { foundNo = 27; }
                    else if (japName.equals("非常食")) { foundNo = 30; } // generic food desc
                    else if (japName.equals("懐中電灯")) { foundNo = 15; }
                    else if (japName.equals("モバイルバッテリー")) { foundNo = 16; }
                    else if (japName.equals("衣服・下着")) { foundNo = 25; }
                    break;
                }
            }
        }

        if (foundNo != -1) {
            // Mapping logic for descriptions
            int descNo = -1;
            if (foundNo == 15 || japName.equals("懐中電灯")) descNo = 15;
            else if (foundNo == 16 || japName.equals("モバイルバッテリー")) descNo = 16;
            else if (foundNo == 25 || japName.equals("衣服・下着")) descNo = 25;
            else if (japName.equals("水") || japName.equals("飲料水")) descNo = 27;
            else if (japName.equals("非常食") || japName.equals("保存食")) descNo = 30;
            else if (japName.equals("簡易トイレ")) descNo = 64;
            else if (japName.equals("高齢者に必要なもの")) descNo = 61;
            else if (japName.equals("女性に必要なもの")) descNo = 63;
            else if (japName.equals("男性に必要なもの")) descNo = 77;
            else if (japName.equals("子供に必要なもの")) descNo = 71;
            else if (japName.equals("赤ちゃんに必要なもの")) descNo = 45; // multiple rows 45-49 but 45 is first
            else if (japName.equals("犬に必要なもの") || japName.equals("ペットに必要なもの")) descNo = 66;

            if (descNo != -1) {
                return main.LangReader("mochi", descNo, language);
            }
        }

        return "";
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
            ImageView imageView = convertView.findViewById(R.id.img_item);

            // Hide member text not used in this view
            View member = convertView.findViewById(R.id.text_menber);
            if (member != null) member.setVisibility(View.GONE);

            textView.setText(item);
            checkBox.setChecked(checkedStates.get(position));

            // Set illustration
            if (imageView != null) {
                imageView.setVisibility(View.VISIBLE);

                // Mapping items to drawables
                if (item.contains(main.LangReader("wizard", 14, language))) {
                    imageView.setImageResource(R.drawable.sinsui_img1); // Water
                } else if (item.contains(main.LangReader("wizard", 15, language))) {
                    imageView.setImageResource(R.drawable.sonae1); // Food
                } else if (item.contains(main.LangReader("wizard", 16, language))) {
                    imageView.setImageResource(R.drawable.kaityuu); // Flashlight
                } else if (item.contains(main.LangReader("wizard", 17, language))) {
                    imageView.setImageResource(R.drawable.mobairu); // Mobile Battery
                } else if (item.contains(main.LangReader("wizard", 18, language)) ||
                           item.contains(main.LangReader("mochi", 57, language))) {
                    imageView.setImageResource(R.drawable.kyuukyuu); // Sanitary / Women's items
                } else if (item.contains(main.LangReader("wizard", 19, language)) ||
                           item.contains(main.LangReader("mochi", 69, language))) {
                    imageView.setImageResource(R.drawable.fuku); // Child items
                } else if (item.contains(main.LangReader("wizard", 20, language)) ||
                           item.contains(main.LangReader("wizard", 21, language)) ||
                           item.contains(main.LangReader("mochi", 13, language))) {
                    imageView.setImageResource(R.drawable.baby); // Baby items
                } else if (item.contains(main.LangReader("wizard", 22, language)) ||
                           item.contains(main.LangReader("wizard", 23, language)) ||
                           item.contains(main.LangReader("mochi", 59, language))) {
                    imageView.setImageResource(R.drawable.inu); // Pet items
                } else if (item.contains(main.LangReader("mochi", 56, language))) {
                    imageView.setImageResource(R.drawable.saigai2); // Elderly items placeholder
                } else if (item.contains(main.LangReader("wizard", 25, language))) {
                    imageView.setImageResource(R.drawable.fuku); // Clothing
                } else if (item.contains(main.LangReader("mochi", 58, language))) {
                    imageView.setImageResource(R.drawable.sonae2); // Portable toilet placeholder
                } else {
                    imageView.setVisibility(View.GONE);
                }

                // Long press to show description
                imageView.setOnLongClickListener(v -> {
                    String desc = itemDescriptions.get(item);
                    if (desc != null && !desc.isEmpty()) {
                        new AlertDialog.Builder(getContext())
                            .setTitle(item)
                            .setMessage(desc)
                            .setPositiveButton("OK", null)
                            .show();
                        return true;
                    }
                    return false;
                });
            }

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
