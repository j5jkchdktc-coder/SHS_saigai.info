package jp.shsit.shsinfo2025.ui.uses;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import jp.shsit.shsinfo2025.R;


public class PageFragment1 extends Fragment {
   //スライド用の部品
   private ViewPager mPager;

   @Nullable
   @Override
   public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

      View view = inflater.inflate(R.layout.users_viewpage_fragment,container,false);
      mPager = (ViewPager)view.findViewById(R.id.viewpager);
      mPager.setAdapter(new FragmentPageAdapter(getChildFragmentManager(),1,getContext()));

      //上部にタブをセット
      TabLayout tabLayout = (TabLayout)view.findViewById(R.id.tabs);
      tabLayout.setupWithViewPager(mPager);

      //タブの初期値
      //プレファランスによる値読み出し
      Integer tab = PreferenceManager.getDefaultSharedPreferences(getActivity()).getInt("tab",1);
      tabLayout.getTabAt(tab).select();

      return view;
   }
}

