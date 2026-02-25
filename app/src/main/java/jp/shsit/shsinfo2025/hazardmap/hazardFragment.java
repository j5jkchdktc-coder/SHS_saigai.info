package jp.shsit.shsinfo2025.hazardmap;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import jp.shsit.shsinfo2025.R;


/**
 * Created by shsit on 2021/05/05.
 */

public class hazardFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hazard_map, container, false);
        //container.removeAllViews();

       // if (savedInstanceState == null) {
            FragmentManager manager = getActivity().getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.f4, new gpsFragmrnt());
            transaction.commit();

      //  }



        return view;
    }
}
