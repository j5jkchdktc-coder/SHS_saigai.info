package jp.shsit.shsinfo2025.ui.sonaeru.ooame_sonae;

import android.content.Context;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class FragmentPageAdapter extends FragmentPagerAdapter {
   int PAGE_COUNT = 5;
   private String tabTitles[];


   Context context;
   public FragmentPageAdapter(@NonNull FragmentManager fm, int behavior,Context c) {
      super(fm, behavior);
      String language= PreferenceManager.getDefaultSharedPreferences(c).getString("lang", "日本語");
      if(language.equals("English")
              ||language.equals("Vietnamese")) {
         tabTitles = new String[]{"FIRST", "SECOND", "THIRD", "FORTH", "FIFTH"};
      }else if(language.equals("Chinese")){
         //||language.equals("Vietnamese")||language.equals("Chinese")
         tabTitles = new String[]{"第1件", "第2件", "第3件", "第4件", "第5件"};
      }else{
         tabTitles = new String[]{"1件目", "2件目", "3件目", "4件目", "5件目"};
      }


      context =c;
   }

   @NonNull
   @Override
   public Fragment getItem(int position) {

      //Fragmentを切り替える
      switch (position){
         case 0:
            return new Fragment1();
         case 1:
            return new Fragment2();
         case 2:
            return new Fragment3();
         case 3:
            return new Fragment4();
         case 4:
            return new Fragment5();
      }
      return null;
   }

   @Nullable
   @Override
   public CharSequence getPageTitle(int position) {

      return tabTitles[position];
   }

   @Override
   public int getCount() {
      return PAGE_COUNT;
   }
}

