package jp.shsit.shsinfo2025.hinan;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import jp.shsit.shsinfo2025.R;


/**
 * Created by shsit on 2021/6/04.
 */

public class MapActivity extends AppCompatActivity {
    private WebView webView;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        webView = (WebView)findViewById(R.id.webview);

        //スクリーンショットのボタンを表示するか？しないか？
        String name = PreferenceManager.getDefaultSharedPreferences(this).getString("name", null);
        Button btn = findViewById(R.id.button_a);
        btn.setVisibility(View.VISIBLE);


        Button modBtn = findViewById(R.id.mo1Button);
        modBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Button bt3 = findViewById(R.id.button_a);
        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCapture(webView, name);
               finish();

            }
        });

        //test0();
        test1();
    }
private void test1(){
    //webViewの設定

    webView.setWebViewClient(new WebViewClient());
    webView.getSettings().setJavaScriptEnabled(true);

    //プレファランスによる値読み出し
    url = PreferenceManager.getDefaultSharedPreferences(this).getString("url", null);

    Log.i("test",url);

    webView.requestFocus();
    webView.setWebViewClient(new ViewClient(this,"データ取得中"));
    webView.loadUrl(url);
}
/*
        // 地名を入れて経路を検索
        private void test0(){

            //プレファランスによる値読み出し
            url = PreferenceManager.getDefaultSharedPreferences(this).getString("url", null);

            // 起点
           /* String start = genzai_latlng;
            // 目的地
            String destination =lat+","+lng;
            // 移動手段：電車:r, 車:d, 歩き:w
            String[] dir = {"r", "d", "w"};



                // 出発地, 目的地, 交通手段
                String str = String.format(Locale.US,
                        "http://maps.google.com/maps?saddr=%s&daddr=%s&dirflg=%s",
                        start, destination, dir[1]);
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setClassName("com.google.android.apps.maps",
                    "com.google.android.maps.MapsActivity");
            intent.setData(Uri.parse(url));
            startActivity(intent);
            finish();

        }
*/

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
    public void onStop() {
        super.onStop();


        }

    //スクリーンショット用
    public void saveCapture(View view,String name) {

        // キャプチャを撮る
        Bitmap capture = getViewCapture(view);
        FileOutputStream fos = null;
        // 保存先のフォルダー
        File cFolder = getExternalFilesDir(Environment.DIRECTORY_DCIM);

        File file1 = new File(cFolder,name);
        FileOutputStream outStream = null;
        try {
            outStream = new FileOutputStream(file1);
            // 画像のフォーマットと画質と出力先を指定して保存
            capture.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
            Log.i("test","成功");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.i("test","sippai ");
        }
        //ファイルのフラグを更新
        SharedPreferences prefer = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefer.edit();
        if (name.equals("CameraIntent1.jpg")) {
            editor.putBoolean("file1", true);
            editor.commit();
        }else if (name.equals("CameraIntent2.jpg")) {
            editor.putBoolean("file2", true);
            editor.commit();
        }else if (name.equals("CameraIntent3.jpg")) {
            editor.putBoolean("file3", true);
            editor.commit();
        }else if (name.equals("CameraIntent4.jpg")) {
            editor.putBoolean("file4", true);
            editor.commit();
        }

    }
    public Bitmap getViewCapture(View view) {
        view.setDrawingCacheEnabled(true);
        Log.i("test","1");
        // Viewのキャプチャを取得
        Bitmap cache = view.getDrawingCache();
        if(cache == null){

            Log.i("test","2");
            return null;
        }
        Bitmap screenShot = Bitmap.createBitmap(cache);
        view.setDrawingCacheEnabled(false);
        return screenShot;
    }


}

