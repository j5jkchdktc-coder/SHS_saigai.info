package jp.shsit.shsinfo2025.ui.sonaeru;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.List;

import jp.shsit.shsinfo2025.MainActivity;
import jp.shsit.shsinfo2025.R;

public class MochiResultFragment extends Fragment implements firstAdapter.EventListener {

    private ListView listView;
    private TextView tvCount;
    private List<MochiCheackFragment.ChecklistItem> allItems;
    private List<MochiCheackFragment.ChecklistItem> visibleItems;
    private boolean[] allFlags;
    private static final int TOTAL_POSSIBLE_ITEMS = 25;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_mochi_result, container, false);

        MainActivity main = new MainActivity();
        String language = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("lang", "日本語");

        TextView tvTitle = root.findViewById(R.id.tv_title);
        tvTitle.setText(main.LangReader("mochi", 93, language));

        this.listView = new ListView(getContext());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1);
        listView.setLayoutParams(lp);

        LinearLayout containerLayout = root.findViewById(R.id.ll_todo_container);
        containerLayout.addView(listView);

        initItems();
        allFlags = getArray("key_v5", TOTAL_POSSIBLE_ITEMS);

        updateVisibleItems();
        setupListView(language, main);

        this.tvCount = new TextView(getContext());
        tvCount.setTextSize(20);
        tvCount.setGravity(android.view.Gravity.CENTER);
        containerLayout.addView(tvCount, 0);
        updateCheckCountDisplay();

        Button btnBack = root.findViewById(R.id.btn_back_to_top);
        btnBack.setText(main.LangReader("hinan", 5, language));
        btnBack.setOnClickListener(v -> getParentFragmentManager().popBackStack(null, androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE));

        return root;
    }

    private void initItems() {
        allItems = new ArrayList<>();
        allItems.add(new MochiCheackFragment.ChecklistItem(0, 0, R.drawable.kaityuu, "essential"));
        allItems.add(new MochiCheackFragment.ChecklistItem(1, 1, R.drawable.mobairu, "essential"));
        allItems.add(new MochiCheackFragment.ChecklistItem(2, 2, R.drawable.denti2, "essential"));
        allItems.add(new MochiCheackFragment.ChecklistItem(3, 3, R.drawable.radio, "essential"));
        allItems.add(new MochiCheackFragment.ChecklistItem(4, 4, R.drawable.kityouhin, "essential"));
        allItems.add(new MochiCheackFragment.ChecklistItem(5, 5, R.drawable.fuku, "essential"));
        allItems.add(new MochiCheackFragment.ChecklistItem(6, 6, R.drawable.hozon, "essential"));
        allItems.add(new MochiCheackFragment.ChecklistItem(7, 7, R.drawable.raita, "essential"));
        allItems.add(new MochiCheackFragment.ChecklistItem(8, 8, R.drawable.mafuro, "essential"));
        allItems.add(new MochiCheackFragment.ChecklistItem(9, 9, R.drawable.kyuukyuu, "essential"));
        allItems.add(new MochiCheackFragment.ChecklistItem(10, 10, R.drawable.keitai, "essential"));
        allItems.add(new MochiCheackFragment.ChecklistItem(11, 11, R.drawable.kami, "essential"));
        allItems.add(new MochiCheackFragment.ChecklistItem(12, 12, R.drawable.pfudebako, "essential"));
        allItems.add(new MochiCheackFragment.ChecklistItem(13, 58, R.drawable.kami, "essential")); // Portable Toilet
        allItems.add(new MochiCheackFragment.ChecklistItem(14, 13, R.drawable.baby, "baby"));
        allItems.add(new MochiCheackFragment.ChecklistItem(15, 56, R.drawable.noimage2, "elderly"));
        allItems.add(new MochiCheackFragment.ChecklistItem(16, 57, R.drawable.noimage2, "woman"));
        allItems.add(new MochiCheackFragment.ChecklistItem(17, 59, R.drawable.inu, "pet"));
        allItems.add(new MochiCheackFragment.ChecklistItem(18, 69, R.drawable.noimage2, "child"));
        allItems.add(new MochiCheackFragment.ChecklistItem(19, 75, R.drawable.noimage2, "male"));
    }

    private void updateVisibleItems() {
        SharedPreferences wizardPrefs = getActivity().getSharedPreferences("MochiWizard", Context.MODE_PRIVATE);
        String gender = wizardPrefs.getString("gender", "male");
        String familyStatus = wizardPrefs.getString("family_status", "none");
        boolean hasPet = wizardPrefs.getBoolean("has_pet", false);

        visibleItems = new ArrayList<>();
        for (MochiCheackFragment.ChecklistItem item : allItems) {
            if (item.category.equals("essential")) {
                visibleItems.add(item);
            } else if (item.category.equals("baby") && familyStatus.equals("baby")) {
                visibleItems.add(item);
            } else if (item.category.equals("child") && familyStatus.equals("child")) {
                visibleItems.add(item);
            } else if (item.category.equals("elderly") && familyStatus.equals("elderly")) {
                visibleItems.add(item);
            } else if (item.category.equals("male") && gender.equals("male")) {
                visibleItems.add(item);
            } else if (item.category.equals("woman") && gender.equals("female")) {
                visibleItems.add(item);
            } else if (item.category.equals("pet") && hasPet) {
                visibleItems.add(item);
            }
        }
    }

    private void setupListView(String language, MainActivity main) {
        String[] names = new String[visibleItems.size()];
        int[] photos = new int[visibleItems.size()];
        boolean[] flags = new boolean[visibleItems.size()];
        int[] originalIds = new int[visibleItems.size()];

        for (int i = 0; i < visibleItems.size(); i++) {
            MochiCheackFragment.ChecklistItem item = visibleItems.get(i);
            names[i] = main.LangReader("mochi", item.csvIndex, language);
            photos[i] = item.iconResId;
            flags[i] = allFlags[item.id];
            originalIds[i] = item.id;
        }

        firstAdapter adapter = new firstAdapter(getActivity().getApplicationContext(),
                R.layout.list_item, names, flags, photos, this);
        adapter.setOriginalIds(originalIds);
        listView.setAdapter(adapter);

        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            MochiCheackFragment.ChecklistItem selectedItem = visibleItems.get(position);
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
        for (MochiCheackFragment.ChecklistItem item : visibleItems) {
            if (allFlags[item.id]) {
                checkedCount++;
            }
        }
        String hyouji = checkedCount + "/" + visibleItems.size();
        if (tvCount != null) tvCount.setText(hyouji);
    }

    @Override
    public void onCheckChanged(int originalId, boolean isChecked) {
        allFlags[originalId] = isChecked;
        updateCheckCountDisplay();
        saveArray(allFlags, "key_v5");
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
