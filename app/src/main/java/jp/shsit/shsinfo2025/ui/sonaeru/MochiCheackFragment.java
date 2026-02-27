package jp.shsit.shsinfo2025.ui.sonaeru;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.List;

import jp.shsit.shsinfo2025.MainActivity;
import jp.shsit.shsinfo2025.R;

public class MochiCheackFragment extends Fragment implements firstAdapter.EventListener {

    TextView tv;
    private List<ChecklistItem> allItems;
    private List<ChecklistItem> visibleItems;
    private boolean[] allFlags;

    private static final int TOTAL_POSSIBLE_ITEMS = 25;

    static class ChecklistItem {
        int id; // Original index for flag storage
        int csvIndex;
        int iconResId;
        String category;

        ChecklistItem(int id, int csvIndex, int iconResId, String category) {
            this.id = id;
            this.csvIndex = csvIndex;
            this.iconResId = iconResId;
            this.category = category;
        }
    }

    private void initItems() {
        allItems = new ArrayList<>();
        allItems.add(new ChecklistItem(0, 0, R.drawable.kaityuu, "essential"));
        allItems.add(new ChecklistItem(1, 1, R.drawable.mobairu, "essential"));
        allItems.add(new ChecklistItem(2, 2, R.drawable.denti2, "essential"));
        allItems.add(new ChecklistItem(3, 3, R.drawable.radio, "essential"));
        allItems.add(new ChecklistItem(4, 4, R.drawable.kityouhin, "essential"));
        allItems.add(new ChecklistItem(5, 5, R.drawable.fuku, "essential"));
        allItems.add(new ChecklistItem(6, 6, R.drawable.hozon, "essential"));
        allItems.add(new ChecklistItem(7, 7, R.drawable.raita, "essential"));
        allItems.add(new ChecklistItem(8, 8, R.drawable.mafuro, "essential"));
        allItems.add(new ChecklistItem(9, 9, R.drawable.kyuukyuu, "essential"));
        allItems.add(new ChecklistItem(10, 10, R.drawable.keitai, "essential"));
        allItems.add(new ChecklistItem(11, 11, R.drawable.kami, "essential"));
        allItems.add(new ChecklistItem(12, 12, R.drawable.pfudebako, "essential"));
        allItems.add(new ChecklistItem(13, 58, R.drawable.kami, "essential")); // Portable Toilet
        allItems.add(new ChecklistItem(14, 13, R.drawable.baby, "baby"));
        allItems.add(new ChecklistItem(15, 56, R.drawable.noimage2, "elderly"));
        allItems.add(new ChecklistItem(16, 57, R.drawable.noimage2, "woman"));
        allItems.add(new ChecklistItem(17, 59, R.drawable.inu, "pet"));
        allItems.add(new ChecklistItem(18, 69, R.drawable.noimage2, "child"));
        allItems.add(new ChecklistItem(19, 75, R.drawable.noimage2, "male"));
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_mochi, container, false);
        initItems();

        allFlags = getArray("key_v4", TOTAL_POSSIBLE_ITEMS);

        String language = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("lang", "日本語");
        MainActivity main = new MainActivity();

        CheckBox cbMale = root.findViewById(R.id.cb_male);
        CheckBox cbWoman = root.findViewById(R.id.cb_woman);
        CheckBox cbBaby = root.findViewById(R.id.cb_baby);
        CheckBox cbChild = root.findViewById(R.id.cb_child);
        CheckBox cbElderly = root.findViewById(R.id.cb_elderly);
        CheckBox cbPet = root.findViewById(R.id.cb_pet);

        SharedPreferences profilePrefs = getContext().getSharedPreferences("Profiles", Context.MODE_PRIVATE);
        cbMale.setChecked(profilePrefs.getBoolean("male", false));
        cbWoman.setChecked(profilePrefs.getBoolean("woman", false));
        cbBaby.setChecked(profilePrefs.getBoolean("baby", false));
        cbChild.setChecked(profilePrefs.getBoolean("child", false));
        cbElderly.setChecked(profilePrefs.getBoolean("elderly", false));
        cbPet.setChecked(profilePrefs.getBoolean("pet", false));

        cbMale.setText(main.LangReader("mochi", 67, language));
        cbWoman.setText(main.LangReader("mochi", 57, language));
        cbBaby.setText(main.LangReader("mochi", 13, language));
        cbChild.setText(main.LangReader("mochi", 68, language));
        cbElderly.setText(main.LangReader("mochi", 56, language));
        cbPet.setText(main.LangReader("mochi", 59, language));

        TextView tvProfile = root.findViewById(R.id.profile_desc);
        tvProfile.setText(main.LangReader("mochi", 73, language));

        updateVisibleItems();
        setupListView(root, language, main);

        View.OnClickListener profileListener = v -> {
            SharedPreferences.Editor editor = profilePrefs.edit();
            editor.putBoolean("male", cbMale.isChecked());
            editor.putBoolean("woman", cbWoman.isChecked());
            editor.putBoolean("baby", cbBaby.isChecked());
            editor.putBoolean("child", cbChild.isChecked());
            editor.putBoolean("elderly", cbElderly.isChecked());
            editor.putBoolean("pet", cbPet.isChecked());
            editor.apply();

            updateVisibleItems();
            setupListView(root, language, main);
            updateCheckCountDisplay();
        };

        cbMale.setOnClickListener(profileListener);
        cbWoman.setOnClickListener(profileListener);
        cbBaby.setOnClickListener(profileListener);
        cbChild.setOnClickListener(profileListener);
        cbElderly.setOnClickListener(profileListener);
        cbPet.setOnClickListener(profileListener);

        tv = root.findViewById(R.id.check);
        updateCheckCountDisplay();

        Button modBtn = root.findViewById(R.id.mo1Button);
        modBtn.setText(main.LangReader("hinan", 5, language));
        modBtn.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        TextView tv2 = root.findViewById(R.id.check_title);
        tv2.setText(main.LangReader("mochi", 54, language));

        return root;
    }

    private void updateVisibleItems() {
        SharedPreferences profilePrefs = getContext().getSharedPreferences("Profiles", Context.MODE_PRIVATE);
        boolean male = profilePrefs.getBoolean("male", false);
        boolean woman = profilePrefs.getBoolean("woman", false);
        boolean baby = profilePrefs.getBoolean("baby", false);
        boolean child = profilePrefs.getBoolean("child", false);
        boolean elderly = profilePrefs.getBoolean("elderly", false);
        boolean pet = profilePrefs.getBoolean("pet", false);

        visibleItems = new ArrayList<>();
        for (ChecklistItem item : allItems) {
            if (item.category.equals("essential")) {
                visibleItems.add(item);
            } else if (item.category.equals("baby") && baby) {
                visibleItems.add(item);
            } else if (item.category.equals("child") && child) {
                visibleItems.add(item);
            } else if (item.category.equals("elderly") && elderly) {
                visibleItems.add(item);
            } else if (item.category.equals("male") && male) {
                visibleItems.add(item);
            } else if (item.category.equals("woman") && woman) {
                visibleItems.add(item);
            } else if (item.category.equals("pet") && pet) {
                visibleItems.add(item);
            }
        }
    }

    private void setupListView(View root, String language, MainActivity main) {
        String[] names = new String[visibleItems.size()];
        int[] photos = new int[visibleItems.size()];
        boolean[] flags = new boolean[visibleItems.size()];
        int[] originalIds = new int[visibleItems.size()];

        for (int i = 0; i < visibleItems.size(); i++) {
            ChecklistItem item = visibleItems.get(i);
            names[i] = main.LangReader("mochi", item.csvIndex, language);
            photos[i] = item.iconResId;
            flags[i] = allFlags[item.id];
            originalIds[i] = item.id;
        }

        ListView listView = root.findViewById(R.id.listview);
        firstAdapter adapter = new firstAdapter(getActivity().getApplicationContext(),
                R.layout.list_item, names, flags, photos, this);
        adapter.setOriginalIds(originalIds);
        listView.setAdapter(adapter);

        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            ChecklistItem selectedItem = visibleItems.get(position);
            FragmentManager manager = getActivity().getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            Bundle args = new Bundle();
            args.putInt("pos", selectedItem.id);
            args.putInt("csvIndex", selectedItem.csvIndex);
            Fragment fragment = new page1Fragment();
            fragment.setArguments(args);
            transaction.addToBackStack(null);
            transaction.replace(R.id.nav_host_fragment, fragment);
            transaction.commit();
            return true;
        });
    }

    private void updateCheckCountDisplay() {
        int checkedCount = 0;
        for (ChecklistItem item : visibleItems) {
            if (allFlags[item.id]) {
                checkedCount++;
            }
        }
        String hyouji = checkedCount + "/" + visibleItems.size();
        if (tv != null) tv.setText(hyouji);
    }

    @Override
    public void onCheckChanged(int originalId, boolean isChecked) {
        allFlags[originalId] = isChecked;
        updateCheckCountDisplay();
        saveArray(allFlags, "key_v4");
    }

    @Override
    public void onPause() {
        super.onPause();
        saveArray(allFlags, "key_v4");
    }

    private void saveArray(boolean[] array, String PrefKey) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            sb.append(array[i]).append(",");
        }
        String stringItem = sb.length() > 0 ? sb.substring(0, sb.length() - 1) : "";
        SharedPreferences prefs = getContext().getSharedPreferences("Array", Context.MODE_PRIVATE);
        prefs.edit().putString(PrefKey, stringItem).apply();
    }

    private boolean[] getArray(String PrefKey, int size) {
        SharedPreferences prefs = getContext().getSharedPreferences("Array", Context.MODE_PRIVATE);
        String stringItem = prefs.getString(PrefKey, "");
        boolean[] b = new boolean[size];
        if (stringItem != null && !stringItem.isEmpty()) {
            String[] a = stringItem.split(",");
            for (int i = 0; i < Math.min(a.length, size); i++) {
                b[i] = Boolean.parseBoolean(a[i]);
            }
        }
        return b;
    }
}
