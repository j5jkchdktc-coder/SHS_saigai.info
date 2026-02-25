package jp.shsit.shsinfo2025;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.location.LocationResult;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity implements LatLongCatch.OnLocationResultListener{
    public static ArrayList<ItemLang> itemArray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        LangCsvReader();

        MainActivity mainActivity = new MainActivity();
        String language = PreferenceManager.getDefaultSharedPreferences(this).getString("lang", "日本語");

        TextView descriptionText = findViewById(R.id.splach_description_text);
        TextView developmentText = findViewById(R.id.splach_development_text);

        descriptionText.setText(mainActivity.LangReader("splash", 0, language));
        developmentText.setText(mainActivity.LangReader("splash", 1, language));

        final Handler handler = new Handler();
        Timer timer = new Timer(false);

        timer.schedule(new TimerTask() {
                           @Override
                           public void run() {
                               handler.post(new Runnable() {
                                   @Override
                                   public void run() {

                                       // メイン画面に遷移して、現在のSplashActivityを終了
                                       Intent intent = new Intent(SplashActivity.this, MainActivity.class);//画面遷移のためのIntentを準備

                                       startActivity(intent);//実際の画面遷移を開始
                                       finish();//現在のActivityを削除
                                   }
                               });
                           }
                       },
                5000);//2秒後にrun()を行う

        LatLongCatch locationManager = new LatLongCatch(SplashActivity.this,SplashActivity.this);
        locationManager.startLocationUpdates();

        //雷通知のため
        Log.i("test","実行される１");
        SharedPreferences sharedPreferences = getSharedPreferences("app_preferences", Context.MODE_PRIVATE);
        // アプリが起動したときに値をfalseに設定
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("has_sent_notification", false);
        editor.apply();


        // 🔽 通知済みフラグをリセット
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.edit()
                .putBoolean("thunder_notified", false)
                .putBoolean("heat_notified", false)
                .apply();

        Log.i("NotificationFragment", "通知済みフラグをリセットしました");

    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        View view = findViewById(R.id.fg111);
        //プレファランス保存
        SharedPreferences prefer = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefer.edit();
        editor.putInt("width",view.getWidth());
        editor.putInt("height",view.getHeight());
        editor.commit();
        Log.i("item", "ViewSize:" + view.getWidth() + "," + view.getHeight());
    }


    @Override
    public void onLocationResult(LocationResult locationResult) {

    }
    public void LangCsvReader(){
        itemArray = new ArrayList<ItemLang>();
        ItemLang item=new ItemLang();
        String result = "";
        try {
            InputStream inputStream = getResources().getAssets().open("language_2025new.csv");
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            while((line = bufferedReader.readLine()) != null){
                ArrayList<String> array = new ArrayList<>();
                StringTokenizer stringTokenizer = new StringTokenizer(line,",");
                String V1= stringTokenizer.nextToken();
                String V2= stringTokenizer.nextToken();
                String V3= stringTokenizer.nextToken();
                String V4 = stringTokenizer.nextToken();
                String V5 = stringTokenizer.nextToken();
                String V6 = stringTokenizer.nextToken();
                String V7 = "";

                try {
                    V7 = stringTokenizer.nextToken();
                } catch (Exception e) {
                    // Nothing
                }

                item.setNo(V1);
                item.setPage(V2);
                item.setJap(V3.replace("\\n", "\n"));
                item.setEng(V4.replace("\\n", "\n"));
                item.setSmall(V5.replace("\\n", "\n"));
                item.setVnm(V6.replace("\\n", "\n"));
                item.setChn(V7.replace("\\n", "\n"));
                System.out.println(V2);
                itemArray.add(item);
                item =new ItemLang();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}