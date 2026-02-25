package jp.shsit.shsinfo2025.ui.uses.lang;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import jp.shsit.shsinfo2025.R;
import jp.shsit.shsinfo2025.ui.uses.UsesHomeBase;

public class LanguageFragment extends Fragment {
   TextView textView;
   public static int tab;
   @Nullable
   @Override
   public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      //一回Viewを取り外す
      container.removeAllViews();
      final View root = inflater.inflate(R.layout.fragment_uses_language, container, false);


      NumberPicker picker = root.findViewById(R.id.numberPicker);
      //textView = root.findViewById(R.id.textView37);
      String[] lang = {"日本語", "English", "ひらがな","Vietnamese","Chinese"};
      String[] lang2 = {"日本語", "English", "ひらがな","Vietnamese","Chinese"};

      picker.setMinValue(0);
      picker.setMaxValue(4);
      picker.setDisplayedValues(lang);
      //プレファランスによる値読み出し
      String language = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("lang","日本語");
//      textView.setText(language);
      //ピッカーの初期値
    /*  if(language.equals("日本語")){
         picker.setValue(0);
      }else if(language.equals("English")){
         picker.setValue(1);
      }else{
         picker.setValue(2);
      }*/
      //ピッカーの初期値
      if(language.equals("日本語")||language.equals("Japanese")||language.equals("にほんご")||language.equals("Tiếng Nhật")|language.equals("日语")){
         picker.setValue(0);
         lang[0]= "日本語";
         lang[1] = "English";
         lang[2] = "ひらがな";
         lang[3] ="ベトナム語";
         lang[4] ="中国語";
      }else if(language.equals("English")||language.equals("えいご")||language.equals("Tiếng Anh")|language.equals("英语")){
         picker.setValue(1);
         lang[0]= "Japanese";
         lang[1] = "English";
         lang[2] = "Hiragana";
         lang[3] ="Vietnamese";
         lang[4] ="Chinese";
      }else if(language.equals("ひらがな")||language.equals("Hiragana")){
         picker.setValue(2);
         lang[0]= "にほんご";
         lang[1] = "えいご";
         lang[2] = "ひらがな";
         lang[3] ="べとなむご";
         lang[4] ="ちゅうごくご";
      }else if(language.equals("ベトナム語")||language.equals("Vietnamese")|language.equals("Tiếng Việt")|language.equals("越南语")){
         picker.setValue(3);
         lang[0]= "Tiếng Nhật";
         lang[1] = "Tiếng Anh";
         lang[2] = "Hiragana";
         lang[3] ="Tiếng Việt";
         lang[4] ="Tiếng Trung Quốc";
      }else if(language.equals("中国語")||language.equals("Chinese")|language.equals("中文")|language.equals("Tiếng Trung Quốc")){
         picker.setValue(4);
         lang[0] = "日语";
         lang[1] = "英语";
         lang[2] = "Hiragana";
         lang[3] ="越南语";
         lang[4] ="中文";
      }


      picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
           //タブの設定
           SharedPreferences prefer = PreferenceManager.getDefaultSharedPreferences(getContext());
           SharedPreferences.Editor editor = prefer.edit();
           editor.putInt("tab",1);
           editor.commit();

          //言語の設定
           editor.putString("lang",lang2[newVal]);
           editor.commit();

           /********画面を更新*******************************/
           Bundle b = new Bundle();
           b.putString("name", "gravedoll");
           FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
           FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
           UsesHomeBase newFragment = new UsesHomeBase();
           newFragment.setArguments(b);
           // Fragmentの置き換え(更新)
           fragmentTransaction.replace(R.id.container,newFragment);

           fragmentTransaction.commit();
            /**********************************************/

//           Intent intent = new Intent(requireContext(), MainActivity.class);
//           intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//           requireContext().startActivity(intent);
        }
     });

      return root;
   }

   @Override
   public void onResume() {
      super.onResume();
      SharedPreferences prefer = PreferenceManager.getDefaultSharedPreferences(getContext());
      SharedPreferences.Editor editor = prefer.edit();
      editor.putString("page","1");
      editor.commit();
      Log.i("test","1表示");
   }

}

