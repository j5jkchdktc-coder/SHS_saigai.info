package jp.shsit.shsinfo2025.ui.uses;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import jp.shsit.shsinfo2025.R;


public class UsesFragment extends Fragment {
    TextView tv1,tv2,tv3,tv4,tv5,tv6;
    public static int tab;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_uses, container, false);


        int width= PreferenceManager.getDefaultSharedPreferences(getActivity()).getInt("width", 100);

        Log.i("item",width+",");

        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int winW = dm.widthPixels;
        int winH = dm.heightPixels;
        Log.i("Q_MainActivity : ", "画面幅 = " + winW);
        Log.i("Q_MainActivity : ", "画面高さ = " + winH);

        tv1= view.findViewById(R.id.help_tx1);
        tv1.setTextSize(30);
        // tvをclickableにする
        MovementMethod mMethod = LinkMovementMethod.getInstance();
        tv1.setMovementMethod(mMethod);

        String name= PreferenceManager.getDefaultSharedPreferences(getContext()).getString("lang","日本語");


        tv2= view.findViewById(R.id.help_tx2);
        tv2.setTextSize(30);
        // tvをclickableにする
        //MovementMethod mMethod2 = LinkMovementMethod.getInstance();
        tv2.setMovementMethod(mMethod);




        tv3= view.findViewById(R.id.help_tx3);
        tv3.setTextSize(30);
        tv3.setMovementMethod(mMethod);


        tv4= view.findViewById(R.id.help_tx4);
        tv4.setTextSize(30);
        tv4.setMovementMethod(mMethod);

        tv5= view.findViewById(R.id.help_tx5);
        tv5.setTextSize(30);
        tv5.setMovementMethod(mMethod);

        tv6= view.findViewById(R.id.help_tx6);
        tv6.setTextSize(30);
        tv6.setMovementMethod(mMethod);

        //タブの設定　戻るボタンからのもどるために
        SharedPreferences prefer = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = prefer.edit();
        editor.putInt("tab",0);
        editor.commit();

        // 上記コードからの続き
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 遷移先はWebViewFromTextView.javaとする
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.nav_host_fragment, new AR_MainActivity1());
                // もどるボタンで戻ってこれるように
                transaction.addToBackStack(null);

                transaction.commit();
            }
        });

        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 遷移先はWebViewFromTextView.javaとする
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.nav_host_fragment, new AR_MainActivity2());
                // もどるボタンで戻ってこれるように
                transaction.addToBackStack(null);

                transaction.commit();
            }
        });
        tv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 遷移先はWebViewFromTextView.javaとする
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.nav_host_fragment, new AR_MainActivity3());
                // もどるボタンで戻ってこれるように
                transaction.addToBackStack(null);

                transaction.commit();
            }
        });
        tv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 遷移先はWebViewFromTextView.javaとする
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.nav_host_fragment, new NS_MainActivity());
                // もどるボタンで戻ってこれるように
                transaction.addToBackStack(null);

                transaction.commit();
            }
        });
        tv5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 遷移先はWebViewFromTextView.javaとする
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.nav_host_fragment, new Tenki_use());
                // もどるボタンで戻ってこれるように
                transaction.addToBackStack(null);

                transaction.commit();
            }
        });
        tv6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 遷移先はWebViewFromTextView.javaとする
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.nav_host_fragment, new Kikikuru_use());
                // もどるボタンで戻ってこれるように
                transaction.addToBackStack(null);

                transaction.commit();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        String name= PreferenceManager.getDefaultSharedPreferences(getContext()).getString("lang","日本語");
        CharSequence link = Html.fromHtml("<a href=>AR機能の設定の方法</a>");
        if(name.equals("English")){
            link = Html.fromHtml("<a href=>How to set up the AR function</a>");
        }
        tv1.setText(link);

        CharSequence link2 = Html.fromHtml("<a href=>AR機能の使い方</a>");
        if(name.equals("English")){
            link2 = Html.fromHtml("<a href=>How to use the AR function</a>");
        }
        tv2.setText(link2);

        CharSequence link3 = Html.fromHtml("<a href=>ARで表示される表示板の見方</a>");
        if(name.equals("English")){
            link3 = Html.fromHtml("<a href=>How to see the display board shown in AR</a>");
        }
        tv3.setText(link3);

        CharSequence link4 = Html.fromHtml("<a href=>ハザードマップの使い方</a>");
        if(name.equals("English")){
            link4 = Html.fromHtml("<a href=>How to use hazard maps</a>");
        }
        tv4.setText(link4);

        //ここから
        CharSequence link5 = Html.fromHtml("<a href=>天気の設定の方法</a>");
        if(name.equals("English")){
            link5 = Html.fromHtml("<a href=>How to set the weather</a>");
        }
        tv5.setText(link5);

        CharSequence link6 = Html.fromHtml("<a href=>雨雲×キキクル使い方</a>");
        if(name.equals("English")){
            link6 = Html.fromHtml("<a href=>How to use overlapping rain clouds and kikikuru</a>");
        }
        tv6.setText(link6);
    }
}