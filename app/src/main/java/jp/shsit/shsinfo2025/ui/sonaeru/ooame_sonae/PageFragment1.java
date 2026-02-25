package jp.shsit.shsinfo2025.ui.sonaeru.ooame_sonae;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import jp.shsit.shsinfo2025.MainActivity;
import jp.shsit.shsinfo2025.R;

public class PageFragment1 extends Fragment {
   //スライド用の部品
   private ViewPager mPager;

   @Nullable
   @Override
   public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

      View view = inflater.inflate(R.layout.viewpage_fragment,container,false);
      mPager = (ViewPager)view.findViewById(R.id.viewpager);
      mPager.setAdapter(new FragmentPageAdapter(getChildFragmentManager(),1,getContext()));

      //上部にタブをセット
      TabLayout tabLayout = (TabLayout)view.findViewById(R.id.tabs);

      tabLayout.setupWithViewPager(mPager);

      Button modoruBtn = view.findViewById(R.id.modorubtn);
      String language= PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("lang", "日本語");
      MainActivity main = new MainActivity();
      modoruBtn.setText(main.LangReader("hinan",5,language));
      modoruBtn.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            getParentFragmentManager().popBackStack();
         }
      });

      return view;
   }
}

