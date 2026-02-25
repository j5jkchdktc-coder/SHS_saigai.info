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
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import jp.shsit.shsinfo2025.R;


/**
 * Created by shsit on 2021/07/03.
 */

public class warnFragment extends Fragment {

    private WebView webView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_weather3, container, false);



       // TextView tv2 = view.findViewById(R.id.text1);

        view.setBackgroundColor(Color.rgb(251,247,192));

        //プレファランスによる値読み出し
        String urlNo = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("key3", "4520200");


        //webViewの設定
        webView = (WebView)view.findViewById(R.id.webView);
        //javascript を処理するために以下のコードが必要でした
        final WebViewClient client = new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }
        };
        WebSettings settings = webView.getSettings();
        settings.setDomStorageEnabled(true);
        settings.setJavaScriptEnabled(true);
        webView.setWebViewClient(client);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.setWebViewClient(new ViewClient(this.getContext(),"テータ取得中"));

        Button bt1 = view.findViewById(R.id.frw1Button);

        String language= PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("lang", "日本語");
        if(language.equals("English")) {
            bt1.setText("back");
            if(urlNo.length()>6) {
                webView.loadUrl("https://www.jma.go.jp/bosai/warning/#lang=en&area_type=class20s&area_code=" + urlNo);
            }else{
                webView.loadUrl("https://www.jma.go.jp/bosai/warning/#lang=en&area_type=class20s&area_code=0" + urlNo);
            }
        }else if(language.equals("Vietnamese")){
            bt1.setText("Quay lại");
            webView.loadUrl("https://www.data.jma.go.jp/multi/warn/index.html?warning=all&lang=vn");
        } else if(language.equals("Chinese")){
            bt1.setText("返回");
            webView.loadUrl("https://www.data.jma.go.jp/multi/warn/index.html?warning=all&lang=cn_zs");
        } else {
            if(urlNo.length()>6) {
                webView.loadUrl("https://www.jma.go.jp/bosai/warning/#lang=ja&area_type=class20s&area_code=" + urlNo);
            }else{
                webView.loadUrl("https://www.jma.go.jp/bosai/warning/#lang=ja&area_type=class20s&area_code=0" + urlNo);
            }
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