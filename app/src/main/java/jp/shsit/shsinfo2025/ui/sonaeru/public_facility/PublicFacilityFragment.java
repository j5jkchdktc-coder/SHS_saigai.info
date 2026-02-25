package jp.shsit.shsinfo2025.ui.sonaeru.public_facility;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import jp.shsit.shsinfo2025.MainActivity;
import jp.shsit.shsinfo2025.R;


public class PublicFacilityFragment extends Fragment {
    TextView tv1,tv2,tv3,tv4;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_public_facility, container, false);


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



        //コンテキストメニューの実装
        //registerForContextMenu(tv1);


        // 上記コードからの続き
          tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popup = new PopupMenu(getContext(),tv1);
                popup.getMenuInflater().inflate(R.menu.context_menu, popup.getMenu());
                popup.show();

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int itemId = item.getItemId();

                        if (itemId == R.id.hinata_menu_1) {
                            FragmentManager manager = getActivity().getSupportFragmentManager();
                            FragmentTransaction transaction = manager.beginTransaction();
                            transaction.addToBackStack(null);
                            transaction.replace(R.id.nav_host_fragment, new HinataMapsFragment());
                            transaction.commit();
                        } else if (itemId == R.id.hinata_menu_2) {
                            FragmentManager manager = getActivity().getSupportFragmentManager();
                            FragmentTransaction transaction = manager.beginTransaction();
                            transaction.addToBackStack(null);
                            transaction.replace(R.id.nav_host_fragment, new HinataWeb2Fragment("sougou_aed2022.pdf"));
                            transaction.commit();
                        } else if (itemId == R.id.hinata_menu_3) {
                            FragmentManager manager = getActivity().getSupportFragmentManager();
                            FragmentTransaction transaction = manager.beginTransaction();
                            transaction.addToBackStack(null);
                            transaction.replace(R.id.nav_host_fragment, new HinataWeb2Fragment("tsunami_hinan.pdf"));
                            transaction.commit();
                        } else if (itemId == R.id.hinata_menu_4) {
                            FragmentManager manager = getActivity().getSupportFragmentManager();
                            FragmentTransaction transaction = manager.beginTransaction();
                            transaction.addToBackStack(null);
                            transaction.replace(R.id.nav_host_fragment, new HinataWeb2Fragment("tsunami_sunmarine2019.pdf"));
                            transaction.commit();
                        }


                        /*
                        switch (item.getItemId()){
                            case R.id.hinata_menu_1:
                                // 遷移先はWebViewFromTextView.javaとする
                                FragmentManager manager = getActivity().getSupportFragmentManager();
                                FragmentTransaction transaction = manager.beginTransaction();
                                // もどるボタンで戻ってこれるように
                                transaction.addToBackStack(null);
                                transaction.replace(R.id.nav_host_fragment, new HinataMapsFragment());
                                transaction.commit();
                                break;
                            case R.id.hinata_menu_2:
                                FragmentManager manager2 = getActivity().getSupportFragmentManager();
                                FragmentTransaction transaction2 = manager2.beginTransaction();
                                // もどるボタンで戻ってこれるように
                                transaction2.addToBackStack(null);
                                transaction2.replace(R.id.nav_host_fragment, new HinataWeb2Fragment("sougou_aed2022.pdf"));
                                transaction2.commit();
                                break;
                            case R.id.hinata_menu_3:
                                FragmentManager manager3 = getActivity().getSupportFragmentManager();
                                FragmentTransaction transaction3 = manager3.beginTransaction();
                                // もどるボタンで戻ってこれるように
                                transaction3.addToBackStack(null);
                                transaction3.replace(R.id.nav_host_fragment, new HinataWeb2Fragment("tsunami_hinan.pdf"));
                                transaction3.commit();
                                break;
                            case R.id.hinata_menu_4:
                                FragmentManager manager4 = getActivity().getSupportFragmentManager();
                                FragmentTransaction transaction4 = manager4.beginTransaction();
                                // もどるボタンで戻ってこれるように
                                transaction4.addToBackStack(null);
                                transaction4.replace(R.id.nav_host_fragment, new HinataWeb2Fragment("tsunami_sunmarine2019.pdf"));
                                transaction4.commit();
                                break;
                        }

                         */

                        return true;
                    }
                });

            }
        });





        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 遷移先はWebViewFromTextView.javaとする
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                // もどるボタンで戻ってこれるように
                transaction.addToBackStack(null);
                transaction.replace(R.id.nav_host_fragment, new SadowaraMapsFragmenrt());
                transaction.commit();
            }
        });

        tv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popup = new PopupMenu(getContext(),tv3);
                popup.getMenuInflater().inflate(R.menu.context_menu2, popup.getMenu());
                popup.show();

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int itemId = item.getItemId();

                        if (itemId == R.id.kisyoudai_menu_1) {
                            // 遷移先はWebViewFromTextView.javaとする
                            FragmentManager manager = getActivity().getSupportFragmentManager();
                            FragmentTransaction transaction = manager.beginTransaction();
                            // もどるボタンで戻ってこれるように
                            transaction.addToBackStack(null);
                            transaction.replace(R.id.nav_host_fragment, new kisyoudaiMapsFragment());
                            transaction.commit();
                        } else if (itemId == R.id.kisyoudai_menu_2) {
                            FragmentManager manager2 = getActivity().getSupportFragmentManager();
                            FragmentTransaction transaction2 = manager2.beginTransaction();
                            // もどるボタンで戻ってこれるように
                            transaction2.addToBackStack(null);
                            transaction2.replace(R.id.nav_host_fragment, new kisyoudaiWebFragment("kisyoudai.pdf"));
                            transaction2.commit();
                        }



                        return true;
                    }
                });

            }
        });







        /*************言語選択*******************/
        String language = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("lang", "日本語");
        MainActivity main = new MainActivity();

        Button bt1 = view.findViewById(R.id.back_btn);
        bt1.setText(main.LangReader("hinan", 5, language));
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPause();
                getParentFragmentManager().popBackStack();

            }
        });


        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        String name= PreferenceManager.getDefaultSharedPreferences(getContext()).getString("lang","日本語");
        CharSequence link = Html.fromHtml("<a href=>ひなた宮崎県総合運動公園</a>");
        if(name.equals("English")){
            link = Html.fromHtml("<a href=>Hinata Miyazaki Comprehensive Athletic Park</a>");
        }
        tv1.setText(link);


        CharSequence link2 = Html.fromHtml("<a href=>佐土原高校</a>");
        if(name.equals("English")){
            link2 = Html.fromHtml("<a href=>SadowaraHighSchool</a>");
        }

        tv2.setText(link2);


        CharSequence link3 = Html.fromHtml("<a href=>宮崎地方気象台</a>");
        if(name.equals("English")){
            link3 = Html.fromHtml("<a href=>Miyazaki Local Meteorological Observatory</a>");
        }

        tv3.setText(link3);





    }
}