package jp.shsit.shsinfo2025.ui.uses;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;

import jp.shsit.shsinfo2025.R;

public class UsesHomeBase extends Fragment {

   public static String language;

    @Nullable
   @Override
   public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      View view = inflater.inflate(R.layout.fragment_uses_home_base, container, false);
       language= PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("lang", "日本語");

      FragmentManager manager = getActivity().getSupportFragmentManager();
      FragmentTransaction transaction = manager.beginTransaction();
      transaction.replace(R.id.fg1, new PageFragment1());

      transaction.commit();
      return view;


   }
}
