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
 * Created by shsit on 2021/07/03.
 */

public class TaifuFragment extends Fragment {

    public static String RSS_FEED_URL ;

    private WebView webView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_weather3, container, false);
        //プレファランスによる値読み出し
        /*
        Float lat1 = PreferenceManager.getDefaultSharedPreferences(getActivity()).getFloat("lat", (float) 33.998);
        String lat = String.valueOf(lat1);
        Float lon1 = PreferenceManager.getDefaultSharedPreferences(getActivity()).getFloat("lon", (float) 133.538);
        String lon = String.valueOf(lon1);
        */
        Button bt1 = view.findViewById(R.id.frw1Button);

        RSS_FEED_URL = "https://www.jma.go.jp/bosai/map.html#5/34.507/137/&elem=typhoon_all&typhoon=all&contents=typhoon";
        String language= PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("lang", "日本語");
        if(language.equals("English")) {
            bt1.setText("back");
            RSS_FEED_URL = "https://www.jma.go.jp/bosai/map.html#5/34.507/137/&elem=typhoon_all&typhoon=all&contents=typhoon&lang=en";
        }else if(language.equals("Vietnamese")){
            bt1.setText("Quay lại");
            RSS_FEED_URL = "https://www.data.jma.go.jp/multi/cyclone/index.html?lang=vn";
        }else if(language.equals("Chinese")){
            bt1.setText("返回");
            RSS_FEED_URL = "https://www.data.jma.go.jp/multi/cyclone/index.html?lang=cn_zs";
        }else{
            RSS_FEED_URL = "https://www.jma.go.jp/bosai/map.html#5/34.507/137/&elem=typhoon_all&typhoon=all&contents=typhoon";
        }

        //プレファランスによる値読み出し
        //RSS_FEED_URL = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("key3", "https://www.jma.go.jp/jp/volcano/map_0.html");


        view.setBackgroundColor(Color.rgb(251,247,192));

        //webViewの設定
        webView = (WebView)view.findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.requestFocus();

        webView.setWebViewClient(new ViewClient(this.getContext(),"テータ取得中"));
        webView.loadUrl(RSS_FEED_URL);




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