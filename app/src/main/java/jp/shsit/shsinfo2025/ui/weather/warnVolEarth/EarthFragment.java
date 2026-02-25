package jp.shsit.shsinfo2025.ui.weather.warnVolEarth;


import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import jp.shsit.shsinfo2025.R;


/**
 *  Created by shsit on 2018/07/03.
 */

public class EarthFragment extends Fragment {

    public static String RSS_FEED_URL = "https://www.jma.go.jp/jp/quake/";

    private WebView webView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String language= PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("lang", "日本語");
        if(language.equals("English")) {

            RSS_FEED_URL = "https://www.jma.go.jp/bosai/map.html#5/38/135/&elem=int&contents=earthquake_map&lang=en";
        }else if(language.equals("Vietnamese")){
            RSS_FEED_URL = "https://www.data.jma.go.jp/multi/quake/index.html?lang=vn";
        } else if(language.equals("Chinese")){
            RSS_FEED_URL = "https://www.data.jma.go.jp/multi/quake/index.html?lang=cn_zs";
        } else{
            RSS_FEED_URL = "https://www.jma.go.jp/jp/quake/";
        }
        //https://www.jma.go.jp/bosai/map.html#5/38/135/&elem=int&contents=earthquake_map&lang=cn_zs
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_weather3, container, false);



        //TextView tv2 = view.findViewById(R.id.text1);
        //プレファランスによる値読み出し
        //RSS_FEED_URL = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("key3", "https://www.jma.go.jp/jp/quake/");


        view.setBackgroundColor(Color.rgb(251,247,192));

        //webViewの設定
        webView = (WebView)view.findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.requestFocus();

        webView.setWebViewClient(new ViewClient(this.getContext(),"テータ取得中"));
        webView.loadUrl(RSS_FEED_URL);

        Button bt1 = view.findViewById(R.id.frw1Button);

        String language= PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("lang", "日本語");
        if(language.equals("English")) {
            bt1.setText("back");
        } else if (language.equals("Vietnamese")) {
            bt1.setText("Quay lại");
        } else if (language.equals("Chinese")) {
            bt1.setText("返回");
        }

        bt1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

       /*FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.r_f1, new r2_Fragment1());
        transaction.commit();*/



        return view;


    }
    public final class ViewClient extends WebViewClient {
        private ProgressDialog progressDialog;

        public ViewClient(Context context, String message){
            super();
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage(message);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            progressDialog.show();

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progressDialog.dismiss();




        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    // リストの項目を選択した時の処理

}