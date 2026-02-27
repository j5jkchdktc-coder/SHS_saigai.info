package jp.shsit.shsinfo2025.ui.sonaeru;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.location.LocationResult;

import jp.shsit.shsinfo2025.LatLongCatch;
import jp.shsit.shsinfo2025.MainActivity;
import jp.shsit.shsinfo2025.R;
import jp.shsit.shsinfo2025.hazardmap.hazardFragment;
import jp.shsit.shsinfo2025.hinan.csv.ReadHinan;
import jp.shsit.shsinfo2025.ui.kikikuru.sinsui.SinsuiFragment;
import jp.shsit.shsinfo2025.ui.sonaeru.keikai.KeikaiFragment;
import jp.shsit.shsinfo2025.ui.sonaeru.keikai.Keikai_child_Fragment;
import jp.shsit.shsinfo2025.ui.sonaeru.keikai.Keikai_en_Fragment;
import jp.shsit.shsinfo2025.ui.sonaeru.ooame_sonae.HomeBase;
import jp.shsit.shsinfo2025.ui.sonaeru.public_facility.PublicFacilityFragment;
import jp.shsit.shsinfo2025.ui.sonaeru.tuti.tuutiFragment;

public class SonaeFragment extends Fragment implements LatLongCatch.OnLocationResultListener {

    LatLongCatch locationManager;
    ProgressDialog progressDialog;
    String language;
    ImageView centerImage3,commentImage3;
    Boolean flag =false;

    int wContent = LinearLayout.LayoutParams.WRAP_CONTENT;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_sonae, container, false);
        language= PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("lang", "日本語");
        ImageView motiBtn = root.findViewById(R.id.imageButton13);
        ImageView hinaBtn = root.findViewById(R.id.imageButton14);
        ImageView hazaBtn = root.findViewById(R.id.imageButton15);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) ImageView tuutiBtn = root.findViewById(R.id.imageButton16);


        MainActivity main = (MainActivity) getActivity();
        centerImage3 = root.findViewById(R.id.centerImage3);
        commentImage3 = root.findViewById(R.id.imageView4);
        commentImage3.setAlpha(0.0f);
        //吹き出し
        centerImage3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if(flag==false){
                            flag = true;
                            commentImage3.setAlpha(1.0f);
                        }else{
                            flag = false;
                            commentImage3.setAlpha(0.0f);
                        }
                }

                return true;
            }
        });

       //避難所
        TextView tv3 = root.findViewById(R.id.textView15);
        tv3.setText( main.LangReader("sonae",3,language));
        hinaBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                commentImage3.setAlpha(0.0f);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //タップした瞬間
                        hinaBtn.setImageResource(R.drawable.hinanjo22);
                        break;
                    case MotionEvent.ACTION_UP:
                        hinaBtn.setImageResource(R.drawable.hinanjo);
                        FragmentManager manager = getActivity().getSupportFragmentManager();
                        FragmentTransaction transaction = manager.beginTransaction();
                        /* もどるボタンで戻ってこれるように */
                        transaction.addToBackStack("hinan");
                        transaction.replace(R.id.nav_host_fragment, new ReadHinan());
                        transaction.commit();
                        break;

                }
                return true;
            }
        });
         //持ち出し
        TextView tv0 = root.findViewById(R.id.textView14);
        tv0.setText( main.LangReader("sonae",0,language));
        motiBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                commentImage3.setAlpha(0.0f);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //タップした瞬間
                        motiBtn.setImageResource(R.drawable.motidasi22);
                        break;
                    case MotionEvent.ACTION_UP:
                        motiBtn.setImageResource(R.drawable.motidasi);
                        FragmentManager manager = getActivity().getSupportFragmentManager();
                        FragmentTransaction transaction = manager.beginTransaction();
                        /* もどるボタンで戻ってこれるように */
                        transaction.addToBackStack(null);
                        transaction.replace(R.id.nav_host_fragment, new MochiCheackFragment());
                        transaction.commit();
                        progressDialog = new ProgressDialog(getContext());
                        progressDialog.setTitle("タイトル");
                        progressDialog.setMessage("メッセージ");
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        progressDialog.setMax(100); // 最大値を設定
                        progressDialog.show();
                        AsyncTaskClass task = new AsyncTaskClass(progressDialog);
                        task.execute("");
                        break;

                }
                return true;
            }
        });

//ハザードマップ
        TextView tv4 = root.findViewById(R.id.textView16);
        tv4.setText( main.LangReader("sonae",4,language));
        hazaBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                commentImage3.setAlpha(0.0f);
                SharedPreferences prefer = PreferenceManager.getDefaultSharedPreferences(getContext());
                SharedPreferences.Editor editor2 = prefer.edit();
                editor2.putString("key_Hz", "no");
                editor2.commit();

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //タップした瞬間
                        hazaBtn.setImageResource(R.drawable.hazado22);
                        break;
                    case MotionEvent.ACTION_UP:
                        hazaBtn.setImageResource(R.drawable.hazado);
                        FragmentManager manager = getActivity().getSupportFragmentManager();
                        FragmentTransaction transaction = manager.beginTransaction();
                        /* もどるボタンで戻ってこれるように */
                        transaction.addToBackStack(null);
                        transaction.replace(R.id.nav_host_fragment, new hazardFragment());
                        transaction.commit();
                        break;

                }
                return true;
            }
        });


    //通知
        TextView tv8 = root.findViewById(R.id.textView29);
        tv8.setText( main.LangReader("sonae",9
                ,language));
        tuutiBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                commentImage3.setAlpha(0.0f);
                SharedPreferences prefer = PreferenceManager.getDefaultSharedPreferences(getContext());
                SharedPreferences.Editor editor2 = prefer.edit();
                editor2.putString("key_Hz", "no");
                editor2.commit();

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //タップした瞬間
                        tuutiBtn.setImageResource(R.drawable.kaminari_tuti2);
                        break;
                    case MotionEvent.ACTION_UP:
                        tuutiBtn.setImageResource(R.drawable.kaminari_tuti2);
                        FragmentManager manager = getActivity().getSupportFragmentManager();
                        FragmentTransaction transaction = manager.beginTransaction();
                        /* もどるボタンで戻ってこれるように */
                        transaction.addToBackStack(null);
                        transaction.replace(R.id.nav_host_fragment, new tuutiFragment());
                        transaction.commit();
                        break;

                }
                return true;
            }
        });

//公共施設
        TextView tv6 = root.findViewById(R.id.textView21);
        tv6.setText( main.LangReader("sonae",8,language));
        ImageView hinanScreenShotBtn2 = root.findViewById(R.id.hzsave_imageView3);
        hinanScreenShotBtn2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                commentImage3.setAlpha(0.0f);
                SharedPreferences prefer = PreferenceManager.getDefaultSharedPreferences(getContext());
                SharedPreferences.Editor editor2 = prefer.edit();
                editor2.putString("key_Hz", "ok");
                editor2.commit();

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //タップした瞬間
                        hinanScreenShotBtn2.setImageResource(R.drawable.pf_b_btn);
                        break;
                    case MotionEvent.ACTION_UP:
                        hinanScreenShotBtn2.setImageResource(R.drawable.pf_btn);
                        FragmentManager manager = getActivity().getSupportFragmentManager();
                        FragmentTransaction transaction = manager.beginTransaction();
                        /* もどるボタンで戻ってこれるように */
                        transaction.addToBackStack(null);
                        transaction.replace(R.id.nav_host_fragment, new PublicFacilityFragment());
                        transaction.commit();
                        break;

                }
                return true;
            }
        });

        //警戒レベル
        TextView tv2 = root.findViewById(R.id.textView23);
        tv2.setText( main.LangReader("sonae",2,language));
        ImageView keikaiBtn = root.findViewById(R.id.keikaiImageView);
        keikaiBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //タップした瞬間
                        keikaiBtn.setImageResource(R.drawable.keikai22);
                        break;
                    case MotionEvent.ACTION_UP:
                        keikaiBtn.setImageResource(R.drawable.keikai);
                        FragmentManager manager = getActivity().getSupportFragmentManager();
                        FragmentTransaction transaction = manager.beginTransaction();
                        /* もどるボタンで戻ってこれるように */
                        transaction.addToBackStack(null);
                        if(language.equals("English") || language.equals("Vietnamese") || language.equals("Chinese")) {
                            transaction.replace(R.id.nav_host_fragment, new Keikai_en_Fragment());
                        }
                        else if(language.equals("日本語")){
                            transaction.replace(R.id.nav_host_fragment, new KeikaiFragment());
                        }
                        else if(language.equals("ひらがな")){
                            transaction.replace(R.id.nav_host_fragment, new Keikai_child_Fragment());
                        }
                        transaction.commit();
                        break;

                }
                return true;
            }
        });

        //大雨行動
        TextView tv1 = root.findViewById(R.id.textView25);
        tv1.setText( main.LangReader("sonae",1,language));
        ImageView ooameKoudouBtn = root.findViewById(R.id.ooameImageView);
        ooameKoudouBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                commentImage3.setAlpha(0.0f);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //タップした瞬間
                        ooameKoudouBtn.setImageResource(R.drawable.ooamehinan22);
                        break;
                    case MotionEvent.ACTION_UP:
                        ooameKoudouBtn.setImageResource(R.drawable.ooamehinan);
                        FragmentManager manager = getActivity().getSupportFragmentManager();
                        FragmentTransaction transaction = manager.beginTransaction();
                        /* もどるボタンで戻ってこれるように */
                        transaction.addToBackStack("top");
                        transaction.replace(R.id.nav_host_fragment, new HomeBase());
                        transaction.commit();
                        break;

                }
                return true;
            }
        });

        //浸水深さ
        TextView tv7 = root.findViewById(R.id.textView28);
        tv7.setText( main.LangReader("sonae",7,language));
        ImageView fukasaBtn = root.findViewById(R.id.imageView2);
      //  ViewGroup.LayoutParams params = fukasaBtn.getLayoutParams();

        fukasaBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                commentImage3.setAlpha(0.0f);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //タップした瞬間
                        fukasaBtn.setImageResource(R.drawable.fukasa2);
                        break;
                    case MotionEvent.ACTION_UP:
                        fukasaBtn.setImageResource(R.drawable.fukasa1);
                        FragmentManager manager = getActivity().getSupportFragmentManager();
                        FragmentTransaction transaction = manager.beginTransaction();
                        Bundle bundle = new Bundle();
                        //bundle.putInt("INT_KEY", 3);
                        SinsuiFragment fragment = new SinsuiFragment();
                        //値を書き込む
                        fragment.setArguments(bundle);
                        /* もどるボタンで戻ってこれるように */

                        transaction.addToBackStack(null);
                        transaction.replace(R.id.nav_host_fragment, fragment);
                        transaction.commit();

                        break;


                }
                return true;
            }
        });

        // 備蓄品診断
        TextView tvWizard = root.findViewById(R.id.wizardText);
        tvWizard.setText(main.LangReader("wizard", 24, language));
        ImageView wizardBtn = root.findViewById(R.id.wizardBtn);
        wizardBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                commentImage3.setAlpha(0.0f);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        wizardBtn.setImageResource(R.drawable.motidasi22);
                        break;
                    case MotionEvent.ACTION_UP:
                        wizardBtn.setImageResource(R.drawable.motidasi);
                        FragmentManager manager = getActivity().getSupportFragmentManager();
                        FragmentTransaction transaction = manager.beginTransaction();
                        transaction.addToBackStack(null);
                        transaction.replace(R.id.nav_host_fragment, new WizardGenderFragment());
                        transaction.commit();
                        break;
                }
                return true;
            }
        });

        locationManager = new LatLongCatch(getContext(), (LatLongCatch.OnLocationResultListener) getActivity());
        locationManager.startLocationUpdates();

        return root;

    }

    @Override
    public void onLocationResult(LocationResult locationResult) {
        // 緯度・経度を取得
        double lat = locationResult.getLastLocation().getLatitude();
        double lon = locationResult.getLastLocation().getLongitude();
        Log.i("test", lat + "," + lon);

    }
}

class AsyncTaskClass extends AsyncTask<String, String, String> {

    ProgressDialog progressDialog;

    public AsyncTaskClass(ProgressDialog progressDialog) {
        super();
        this.progressDialog   = progressDialog;
    }

    @Override
    protected String doInBackground (String... params) {
        try {
            for (int i = 37; i < 100; i++) {
                progressDialog.setProgress(i);
                Thread.sleep(10);
            }
        } catch (InterruptedException e) { }
        return "";
    }

    @Override
    protected void onPostExecute(String str) {
        progressDialog.dismiss();
    }
}