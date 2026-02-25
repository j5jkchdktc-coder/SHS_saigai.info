package jp.shsit.shsinfo2025.ui.uses;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import jp.shsit.shsinfo2025.ui.uses.lang.LanguageFragment;


public class FragmentPageAdapter extends FragmentPagerAdapter {
   int PAGE_COUNT=2;
   private String tabTitles[]= new String[]{"使い方","言語選択"};
   Context context;
   public FragmentPageAdapter(@NonNull FragmentManager fm, int behavior, Context c) {
      super(fm, behavior);
      context =c;

   }

   @NonNull
   @Override
   public Fragment getItem(int position) {

      //Fragmentを切り替える
      switch (position){
         case 0:
            return new UsesFragment();
         case 1:
            return new LanguageFragment();

      }
      return null;
   }

   @Nullable
   @Override
   public CharSequence getPageTitle(int position) {
      String lan= UsesHomeBase.language;
      if(lan.equals("日本語")||lan.equals("Japanese")||lan.equals("にほんご")) {
         tabTitles[0]= "使い方";
         tabTitles[1] = "言語選択";
      } else if(lan.equals("English")||lan.equals("えいご")) {
         tabTitles[0]= "Usage";
         tabTitles[1] = "language";

      } else if(lan.equals("ひらがな")||lan.equals("Hiragana")){
         tabTitles[0]= "つかいかた";
         tabTitles[1] = "げんごせんたく";
      }

      return tabTitles[position];
   }

   @Override
   public int getCount() {
      return PAGE_COUNT;
   }
}

