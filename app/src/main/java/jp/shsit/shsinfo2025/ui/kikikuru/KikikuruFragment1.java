package jp.shsit.shsinfo2025.ui.kikikuru;

import static android.graphics.Color.WHITE;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import java.util.Timer;
import java.util.TimerTask;

import jp.shsit.shsinfo2025.MainActivity;
import jp.shsit.shsinfo2025.R;

public class KikikuruFragment1 extends Fragment {
    WebView web3_1,
            web3_2;
    String  url3_1,
            url3_2,
            zoom="11",
            lat="33.998027",
            lat2="33.998027",
            lon="133.538818",
            data="flood",
            hazado="";
    int i = 0;
    int s = 0;
    float s2 = (float)0.5;
    Button bt23_1,
           bt3_1;

    TextView tv3_1,
             tv3_2;
    SeekBar seekBar;

    View containerView;
    View numberpicker2;
    Animation inAnimation;
    Animation outAnimation;
    FrameLayout frameLayout;

    int kikiFlag =0;


    String language;



    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_kikikuru1, container, false);

        containerView = root.findViewById(R.id.container);
        numberpicker2 = root.findViewById(R.id.numberpicker2);
        //言語読み込み
        language= PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("lang", "日本語");
        MainActivity main = new MainActivity();

        TextView modoru = root.findViewById(R.id.button23_1);

        if(language.equals("English")) {
            modoru.setText("Back");
        }else if(language.equals("Vietnamese")){
            modoru.setText("Quay lại");
        }else if(language.equals("Chinese")){
            modoru.setText("返回");
        }else {
            modoru.setText("もどる");
        }

        TextView tv1 = root.findViewById(R.id.textView18);
        //"雨雲viewの透過度"
        tv1.setText( main.LangReader("kikikuru1",1,language));

        TextView tv2 = root.findViewById(R.id.textView19);
        tv2.setText( main.LangReader("kikikuru1",0,language));

        LinearLayout kikikuruBottomSheet = root.findViewById(R.id.kikikuru_bottomsheet);

       // tv3_2 = root.findViewById(R.id.textView2);
        seekBar = root.findViewById(R.id.seekBar);

        inAnimation = (Animation) AnimationUtils.loadAnimation(getActivity(), R.anim.in_layout);
        outAnimation= (Animation) AnimationUtils.loadAnimation(getActivity(), R.anim.out_layout);

        //プレファランスによる値読み出し
        Float lat1 = PreferenceManager.getDefaultSharedPreferences(getActivity()).getFloat("lat", (float) 33.998);
        lat = String.valueOf(lat1);
        Float lon1 = PreferenceManager.getDefaultSharedPreferences(getActivity()).getFloat("lon", (float) 133.538);
        lon = String.valueOf(lon1);
//webviewのalpha値
        SeekBar seekBar = root.findViewById(R.id.seekBar);

        //初期値
        seekBar.setProgress(5);
        seekBar.setMax(10);
        seekBar.setMin(1);


        //初期値
        containerView.setVisibility(View.GONE);


        //メニューのアニメーション
        Button animeBtn = root.findViewById(R.id.button9);
        //コントローラの表示
        animeBtn.setText( main.LangReader("kikikuru1",3,language));
        animeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // ビューが表示されてるか判定
                if(containerView.getVisibility() == View.GONE){
                    containerView.startAnimation(inAnimation);
                    containerView.setVisibility(View.VISIBLE);
                    animeBtn.setText( main.LangReader("kikikuru1",2,language));

                    // アニメーションしながらViewを表示
                }
                else if(containerView.getVisibility() == View.VISIBLE){
                    // アニメーションしながらViewを隠す
                    containerView.startAnimation(outAnimation);
                    containerView.setVisibility(View.GONE);
                    animeBtn.setText( main.LangReader("kikikuru1",3,language));
                }
            }
        });

        // 画面遷移で渡された値
        kikiFlag = getArguments().getInt("INT_KEY");
        switch (kikiFlag){
            //洪水
            case 0:
                url3_2 = "https://www.jma.go.jp/bosai/risk/#zoom:" + zoom + "/lat:" + lat + "/lon:" + lon + "/colordepth:normal/elements:flood";

                if(language.equals("English") || language.equals("Chinese") || language.equals("Vietnamese")) {
                    url3_2 = "https://www.jma.go.jp/bosai/en_risk/#zoom:" + zoom + "/lat:" + lat + "/lon:" + lon + "/colordepth:normal/elements:flood";
                }
                data="flood";
                break;
                //土砂
            case 1:
                url3_2 = "https://www.jma.go.jp/bosai/risk/#zoom:" + zoom + "/lat:" + lat + "/lon:" + lon + "/colordepth:normal/elements:land";

                if(language.equals("English") || language.equals("Chinese") || language.equals("Vietnamese")) {
                    url3_2 = "https://www.jma.go.jp/bosai/en_risk/#zoom:" + zoom + "/lat:" + lat + "/lon:" + lon + "/colordepth:normal/elements:land";
                }
                data="land";
                break;
                // 浸水
            case 2:
                url3_2 = "https://www.jma.go.jp/bosai/risk/#zoom:" + zoom + "/lat:" + lat + "/lon:" + lon + "/colordepth:normal/elements:inund";

                if(language.equals("English") || language.equals("Chinese") || language.equals("Vietnamese")) {
                    url3_2 = "https://www.jma.go.jp/bosai/en_risk/#zoom:" + zoom + "/lat:" + lat + "/lon:" + lon + "/colordepth:normal/elements:inund";
                }

                data="inund";
                break;
            default:
                url3_1 = "https://www.jma.go.jp/bosai/nowc/#zoom:"+zoom+"/lat:"+lat+ "/lon:"+lon+"/colordepth:normal/elements:slmcs&amds_rain10m&liden&hrpns";
                url3_2 = "https://www.jma.go.jp/bosai/nowc/#zoom:"+zoom+"/lat:"+lat+ "/lon:"+lon+"/colordepth:normal";

                if(language.equals("English") || language.equals("Chinese") || language.equals("Vietnamese")) {
                    containerView.setVisibility(View.INVISIBLE);
                    kikikuruBottomSheet.setVisibility(View.INVISIBLE);
                    animeBtn.setVisibility(View.INVISIBLE);

                    url3_2 = "https://www.jma.go.jp/bosai/en_nowc/#zoom:"+zoom+"/lat:"+lat+ "/lon:"+lon+"/colordepth:normal";
                }

                break;
        }



//キキクルと雨雲のview
        web3_1 = root.findViewById(R.id.webview);
        web3_1.setWebViewClient(new ViewClient());
        WebSettings webSettings = web3_1.getSettings();
        webSettings.setJavaScriptEnabled(true);
        url3_1 = "https://www.jma.go.jp/bosai/nowc/#zoom:"+zoom+"/lat:"+lat+ "/lon:"+lon+"/colordepth:normal/elements:slmcs&amds_rain10m&liden&hrpns";
        web3_1.loadUrl(url3_1);

        //url3_2 = "https://www.jma.go.jp/bosai/risk/#zoom:" + zoom + "/lat:" + lat +
        //        "/lon:" + lon + "/colordepth:normal/elements:" + hazado + data;

        web3_2 = root.findViewById(R.id.webview2);
        web3_2.setWebViewClient(new WebViewClient());
        web3_2.getSettings().setJavaScriptEnabled(true);
        web3_2.loadUrl(url3_2);
        Log.i("test",url3_2);

        web3_2.setBackgroundColor(0);

        //初期値
        web3_1.setAlpha(0.1f);

      //  web3_1.setVisibility(View.INVISIBLE);

//もどるボタン
        bt23_1 = root.findViewById(R.id.button23_1);
        bt23_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });


        web3_1.setBackgroundColor(WHITE);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                float a = (float) progress / 10;
                s = progress;
                s2 = a;
                System.out.println(progress + "です" + a);
                if(kikiFlag!=3) {
                    if (a == 0) {
                        web3_1.setVisibility(View.INVISIBLE);

                    } else {
                        web3_1.setVisibility(View.VISIBLE);

                    }
                    web3_1.setAlpha(a);
                }else{
                    //  雨雲を選んだ時
                    web3_1.setVisibility(View.INVISIBLE);
                }
                //web3_1.setVisibility(View.VISIBLE);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

//webviewがタッチされたときの処理
        web3_1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.i("test", "down");
                        break;
                    case MotionEvent.ACTION_MOVE:
                        Log.i("test", "move");
                        web3_1.setAlpha(1);
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.i("test", "up");
                        final Handler handler1 = new Handler();
                        Timer timer1 = new Timer(false);
                        Log.i("now1", web3_1.getUrl());
                        timer1.schedule(new TimerTask() {
                                            @Override
                                            public void run() {
                                                handler1.post(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        // メイン画面に遷移して、現在のSplashActivityを終了
                                                        Log.i("now3", "up " + web3_1.getUrl());
                                                        url_seiri();
                                                        //後ろのviewが動かない
                                                        Timer timer2 = new Timer(false);
                                                        timer2.schedule(new TimerTask() {
                                                            @Override
                                                            public void run() {
                                                                web3_1.setAlpha(s2);
                                                                seekBar.setProgress(s);
                                                            }
                                                        }, 1000);

                                                    }
                                                });
                                            }
                                        },
                                2000);//2秒後にrun()を行う
                        break;
                }
                return false;
            }
        });

//numberpickerの処理
        //"洪水害"
        String kou = main.LangReader("kikikuru",2,language);
        //"土砂災害"
        String dosya = main.LangReader("kikikuru",0,language);
        //"浸水害"
        String shin = main.LangReader("kikikuru",1,language);
        String[] amagumo = {kou,dosya ,shin };
        NumberPicker numberPicker = root.findViewById(R.id.numberpicker2);
        numberPicker.setMaxValue(2);
        numberPicker.setMinValue(0);
        numberPicker.setValue(kikiFlag);
        numberPicker.setDisplayedValues(amagumo);

        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                System.out.println(newVal + "です");
                if (newVal == 0) {
                    data = "flood";

                } else if (newVal == 1) {
                    data = "land";

                } else {
                    data = "inund";

                }
                url3_2 = "https://www.jma.go.jp/bosai/risk/#zoom:" + zoom + "/lat:" + lat + "/lon:" + lon + "/colordepth:normal/elements:" + data;
                if(language.equals("English")) {
                    url3_2 = "https://www.jma.go.jp/bosai/en_risk/#zoom:" + zoom + "/lat:" + lat + "/lon:" + lon + "/colordepth:normal/elements:" + data;
                }

                //雨雲以外の時
                if(kikiFlag!=3) {
                    web3_2.loadUrl(url3_2);
                }
                seekBar.setProgress(s);
            }
        });

//ハザードマップのオンオフ
        /*
        bt3_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i % 2 == 0) {
                    hazado = "";
                    bt3_1.setBackgroundColor(BLUE);
                } else {
                    hazado = "hazardmap&";
                    bt3_1.setBackgroundColor(RED);
                }
                i++;
                url3_2 = "https://www.jma.go.jp/bosai/risk/#zoom:" + zoom + "/lat:" + lat +
                        "/lon:" + lon + "/colordepth:normal/elements:" + hazado + data;
                web3_2.loadUrl(url3_2);

            }
        });

*/

        return root;
    }

//webviewにweb1を重ねる処理
    public void url_seiri(){
        Timer timer3 = new Timer(false);
        String url = web3_1.getUrl();
        Log.i("now4","up "+web3_1.getUrl());
                @SuppressLint("StaticFieldLeak") AsyncTask task1 = new AsyncTask() {
                    protected Object doInBackground(Object[] objects) {
                        while(lat==lat2){
                            String url11 = url;
                            Log.i("test","2");
                            int index1 = url11.indexOf("zoom:");
                            index1 += "zoom:".length();
                            String url22 = url11.substring(index1);
                            index1 = url22.indexOf("/");
                            zoom = url22.substring(0,index1);
                            Log.i("test",zoom);

                            index1 = url22.indexOf("lat:");
                            index1 += "lat:".length();
                            url22 = url22.substring(index1);
                            index1 = url22.indexOf("/");
                            lat = url22.substring(0,index1);
                            Log.i("test",lat);

                            index1 = url22.indexOf("lon:");
                            index1 += "lon:".length();
                            url22 = url22.substring(index1);
                            index1 = url22.indexOf("/");
                            lon = url22.substring(0,index1);
                            Log.i("test",lon);

                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                        return null;
                    }

                    @Override
                    protected void onPostExecute(Object o) {
                        super.onPostExecute(o);

                    }
                }.execute();

                AsyncTask task2 = new AsyncTask() {

                    @Override
                    protected Object doInBackground(Object[] objects) {
                        Log.i("test","3");

                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Object o) {
                        super.onPostExecute(o);
                        Log.i("test","4");
                        Log.i("test2",url3_2);
                        web3_2.loadUrl(url3_2);
                        lat2=lat;
                        web3_2.setAlpha(1);
                    }


                }.execute();
            }

public class ViewClient extends WebViewClient{
    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        //タイトルを消す
        String js1 ="document.getElementsByClassName('area-nav')[0].style.display = 'none';";
        web3_1.loadUrl("javascript:(function() {"+js1+"})()");
        //中の表示を消す
        String js2 ="document.getElementsByClassName('leaflet-control-container')[0].style.display = 'none';";
        web3_1.loadUrl("javascript:(function() {"+js2+"})()");

    }
}
}




