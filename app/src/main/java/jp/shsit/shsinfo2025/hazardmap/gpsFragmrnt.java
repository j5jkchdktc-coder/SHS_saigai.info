package jp.shsit.shsinfo2025.hazardmap;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import jp.shsit.shsinfo2025.MainActivity;
import jp.shsit.shsinfo2025.R;
import jp.shsit.shsinfo2025.ui.sonaeru.screenshot2.ScreenShotFragment2;


/**
 * Created by shsit on 2021/05/05.
 */

public class gpsFragmrnt extends Fragment {

    private WebView webView;
  //  private String searchWord;
    double lat,lng;


    // Fused Location Provider API.
    private FusedLocationProviderClient fusedLocationClient;
    // Location Settings APIs.
    private SettingsClient settingsClient;
    private LocationSettingsRequest locationSettingsRequest;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;
    private Location location;

    private String lastUpdateTime;
    private Boolean requestingLocationUpdates;
    private static final int REQUEST_CHECK_SETTINGS = 0x1;
    private int priority = 0;

    private String textLog;

    private final int REQUEST_PERMISSION = 10;


    TextView tv;
    private  Boolean pictureFlag = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.gps_location, container, false);

        //container.removeAllViews();
        if(Build.VERSION.SDK_INT >= 23){
            checkPermission();
        }
        else{
            createLocationCallback();
        }



        //webViewの設定
        webView = (WebView)view.findViewById(R.id.gps_webview);
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);




        webView.requestFocus();

        fusedLocationClient =
                LocationServices.getFusedLocationProviderClient(this.getActivity());
        settingsClient = LocationServices.getSettingsClient(this.getActivity());


         tv= (TextView)view.findViewById(R.id.study_textView);
        //createLocationCallback();
        createLocationRequest();
        buildLocationSettingsRequest();

        startLocationUpdates();
        lat = PreferenceManager.getDefaultSharedPreferences(getActivity()).getFloat("lat", 0.0f);
        lng = PreferenceManager.getDefaultSharedPreferences(getActivity()).getFloat("lon", 0.0f);
        webView.setWebViewClient(new ViewClient(this.getContext(),"データ取得中"));
        webView.loadUrl("https://disaportal.gsi.go.jp/maps/index.html?ll=" + lat + "," +  lng + "&z=13&base=pale&vs=c1j0l0u0");

        tv.setText(lat+","+lng);


        Button bt =(Button) view.findViewById(R.id.gps1Button2);
        /*************言語選択*******************/
        String language= PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("lang", "日本語");
        MainActivity main = new MainActivity();
        bt.setText(main.LangReader("hinan",6,language));
        /***********************************************************/
            bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLocationUpdates();
               webView.setWebViewClient(new ViewClient(getContext(),"データ取得中"));
               webView.loadUrl("https://disaportal.gsi.go.jp/maps/index.html?ll=" + lat + "," +  lng + "&z=13&base=pale&vs=c1j0l0u0");
                //webView.loadUrl("https://www.yahoo.co.jp");
                tv.setText(lat+","+lng);

            }
        });

        Button bt1 = view.findViewById(R.id.gps1Button);
        /*************言語選択*******************/
        bt1.setText(main.LangReader("hinan",5,language));
        /***********************************************************/
        bt1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        Button bt3 = view.findViewById(R.id.button_a);
        Button bt4 = view.findViewById(R.id.save_btn2);
        bt3.setVisibility(View.VISIBLE);

        if(language.equals("English")) {
            bt3.setText("Take a picture");
            bt4.setText("Saved List");
        }else if(language.equals("Vietnamese")){
            bt3.setText("Chụp một bức ảnh");
            bt4.setText("Danh sách đã lưu");
        }else if(language.equals("Chinese")){
            bt3.setText("拍照");
            bt4.setText("已保存列表");
        }else {
            bt3.setText("撮る");
            bt4.setText("保存一覧");
        }
        /*
        String fileName = PreferenceManager.getDefaultSharedPreferences(getContext()).getString("key_Hz","no");
        if (fileName.equals("ok")){
            //表示
            bt3.setVisibility(View.VISIBLE);
        }else{
            //非表示 スペースを詰める
            bt3.setVisibility(View.GONE);
        }
*/
        Boolean flag = ScreenShotIntent();
        if(flag){
            bt3.setEnabled(true);
        }else{
            bt3.setEnabled(false);
        }
        String finalFileName =   PreferenceManager.getDefaultSharedPreferences(getContext()).getString("name2","noImage");
        Log.i("test",finalFileName);
        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(getContext());
                // タイトル
                builder.setTitle("保存します");
                // ポジティブボタン
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        saveCapture(webView, finalFileName);
                    //保存のビューに画面遷移　連写用　2023-11-22改訂
                        FragmentManager manager = getActivity().getSupportFragmentManager();
                        FragmentTransaction transaction = manager.beginTransaction();
                        /* もどるボタンで戻ってこれるように */
                        transaction.addToBackStack(null);
                        transaction.replace(R.id.nav_host_fragment, new ScreenShotFragment2());
                        transaction.commit();


                    }
                    });
                    // ネガティブボタン
                builder.setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // do something
                        }
                    });

             builder.create();
             builder.show();

                //getParentFragmentManager().popBackStack();

            }
        });

        Button saveBtn = view.findViewById(R.id.save_btn2);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                /* もどるボタンで戻ってこれるように */
                transaction.addToBackStack(null);
                transaction.replace(R.id.nav_host_fragment, new ScreenShotFragment2());
                transaction.commit();
            }
        });


        return view;

    }
   



        public static double[] getLocationFromPlaceName(Context context, String name) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> location = geocoder.getFromLocationName(name, 1);
            if (location == null || location.size() < 1) {
                double[] latlng = {32.0239,131.4659};
                return latlng;
            }

            Address address = location.get(0);
            double[] latlng = {address.getLatitude(), address.getLongitude()};

            return latlng;
        } catch (IOException e) {
            // 例外処理
            double[] latlng = {32.0239,131.4659};
            return latlng;
        }
    }

    public static final class ViewClient extends WebViewClient {
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

    // locationのコールバックを受け取る
    private void createLocationCallback() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                location = locationResult.getLastLocation();

                lastUpdateTime = DateFormat.getTimeInstance().format(new Date());
                updateLocationUI();
            }
        };
    }

    private void updateLocationUI() {
        // getLastLocation()からの情報がある場合のみ
        if (location != null) {

            String fusedName[] = {
                    "Latitude", "Longitude", "Accuracy",
                    "Altitude", "Speed", "Bearing"
            };

            double fusedData[] = {
                    location.getLatitude(),
                    location.getLongitude(),
                    location.getAccuracy(),
                    location.getAltitude(),
                    location.getSpeed(),
                    location.getBearing()
            };
            lat= location.getLatitude();
            lng= location.getLongitude();
            StringBuilder strBuf =
                    new StringBuilder("---------- UpdateLocation ---------- \n");

            for(int i=0; i< fusedName.length; i++) {
                strBuf.append(fusedName[i]);
                strBuf.append(" = ");
                strBuf.append(String.valueOf(fusedData[i]));
                strBuf.append("\n");
            }

            strBuf.append("Time");
            strBuf.append(" = ");
            strBuf.append(lastUpdateTime);
            strBuf.append("\n");

            textLog += strBuf;
//            textView.setText(textLog);
        }

    }

    private void createLocationRequest() {
        locationRequest = new LocationRequest();

        if (priority == 0) {
            // 高い精度の位置情報を取得したい場合
            // インターバルを例えば5000msecに設定すれば
            // マップアプリのようなリアルタイム測位となる
            // 主に精度重視のためGPSが優先的に使われる
            locationRequest.setPriority(
                    LocationRequest.PRIORITY_HIGH_ACCURACY);

        } else if (priority == 1) {
            // バッテリー消費を抑えたい場合、精度は100mと悪くなる
            // 主にwifi,電話網での位置情報が主となる
            // この設定の例としては　setInterval(1時間)、setFastestInterval(1分)
            locationRequest.setPriority(
                    LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        } else if (priority == 2) {
            // バッテリー消費を抑えたい場合、精度は10kmと悪くなる
            locationRequest.setPriority(
                    LocationRequest.PRIORITY_LOW_POWER);

        } else {
            // 受け身的な位置情報取得でアプリが自ら測位せず、
            // 他のアプリで得られた位置情報は入手できる
            locationRequest.setPriority(
                    LocationRequest.PRIORITY_NO_POWER);
        }

        // アップデートのインターバル期間設定
        // このインターバルは測位データがない場合はアップデートしません
        // また状況によってはこの時間よりも長くなることもあり
        // 必ずしも正確な時間ではありません
        // 他に同様のアプリが短いインターバルでアップデートしていると
        // それに影響されインターバルが短くなることがあります。
        // 単位：msec
        locationRequest.setInterval(60000);
        // このインターバル時間は正確です。これより早いアップデートはしません。
        // 単位：msec
        locationRequest.setFastestInterval(5000);

    }

    // 端末で測位できる状態か確認する。wifi, GPSなどがOffになっているとエラー情報のダイアログが出る
    private void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder =
                new LocationSettingsRequest.Builder();

        builder.addLocationRequest(locationRequest);
        locationSettingsRequest = builder.build();
    }

    @Override
    public void onActivityResult(int requestCode,
                                    int resultCode, Intent data) {
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Log.i("debug", "User agreed to make required location settings changes.");
                        // Nothing to do. startLocationupdates() gets called in onResume again.
                        break;
                    case Activity.RESULT_CANCELED:
                        Log.i("debug", "User chose not to make required location settings changes.");
                        requestingLocationUpdates = false;
                        break;
                }
                break;
        }
    }

    // FusedLocationApiによるlocation updatesをリクエスト
    private void startLocationUpdates() {
        // Begin by checking if the device has the necessary location settings.
        settingsClient.checkLocationSettings(locationSettingsRequest)
                .addOnSuccessListener(this.getActivity(),
                        new OnSuccessListener<LocationSettingsResponse>() {
                            @Override
                            public void onSuccess(
                                    LocationSettingsResponse locationSettingsResponse) {
                                Log.i("debug", "All location settings are satisfied.");

                                // パーミッションの確認
                                if (ActivityCompat.checkSelfPermission(
                                        gpsFragmrnt.this.getActivity(),
                                        Manifest.permission.ACCESS_FINE_LOCATION) !=
                                        PackageManager.PERMISSION_GRANTED
                                        && ActivityCompat.checkSelfPermission(
                                        gpsFragmrnt.this.getActivity(),
                                        Manifest.permission.ACCESS_COARSE_LOCATION) !=
                                        PackageManager.PERMISSION_GRANTED) {


                                    return;
                                }
                                fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());

                            }
                        })
                .addOnFailureListener(this.getActivity(), new OnFailureListener() {

                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                Log.i("debug", "Location settings are not satisfied. Attempting to upgrade " +
                                        "location settings ");
                                try {
                                    // Show the dialog by calling startResolutionForResult(), and g4_check the
                                    // result in onActivityResult().
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(
                                            gpsFragmrnt.this.getActivity(),
                                            REQUEST_CHECK_SETTINGS);

                                } catch (IntentSender.SendIntentException sie) {
                                    Log.i("debug", "PendingIntent unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings.";
                                Log.e("debug", errorMessage);

                                Toast.makeText(gpsFragmrnt.this.getContext(),errorMessage, Toast.LENGTH_LONG).show();

                                requestingLocationUpdates = false;

                        }

                    }
                });

        requestingLocationUpdates = true;
    }

    private void stopLocationUpdates() {
        textLog += "onStop()\n";
        //  textView.setText(textLog);

        if (!requestingLocationUpdates) {
            Log.d("debug", "stopLocationUpdates: " +
                    "updates never requested, no-op.");


            return;
        }

        fusedLocationClient.removeLocationUpdates(locationCallback)
                .addOnCompleteListener(this.getActivity(),
                        new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                requestingLocationUpdates = false;
                            }
                        });
    }

    @Override
    public void onPause() {
        super.onPause();
        // バッテリー消費を鑑みLocation requestを止める
        stopLocationUpdates();
    }
/*********************study1にあった***********************************************************/
    // 位置情報許可の確認
    public void checkPermission() {
        // 既に許可している
        if (ActivityCompat.checkSelfPermission(this.getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)==
                PackageManager.PERMISSION_GRANTED){

            createLocationCallback();
           // startLocationUpdates();
           // locationActivity();
        }
        // 拒否していた場合
        else{
            requestLocationPermission();
        }
    }

    // 許可を求める
    private void requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this.getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            ActivityCompat.requestPermissions(this.getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSION);

        } else {
            Toast toast = Toast.makeText(this.getActivity(),
                    "許可されないとアプリが実行できません", Toast.LENGTH_SHORT);
            toast.show();

            ActivityCompat.requestPermissions(this.getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,},
                    REQUEST_PERMISSION);

        }
    }

    // 結果の受け取り
    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults) {

        if (requestCode == REQUEST_PERMISSION) {
            // 使用が許可された
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                createLocationCallback();
               // startLocationUpdates();
                // locationActivity();

            } else {
                // それでも拒否された時の対応
                Toast toast = Toast.makeText(this.getActivity(),
                        "これ以上なにもできません", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }
    //スクリーンショット用
    public void saveCapture(View view,String name) {

        // キャプチャを撮る
        Bitmap capture = getViewCapture(view);
        FileOutputStream fos = null;
        // 保存先のフォルダー
        File cFolder = getContext().getExternalFilesDir(Environment.DIRECTORY_DCIM);

        File file1 = new File(cFolder,name);
        FileOutputStream outStream = null;
        try {
            outStream = new FileOutputStream(file1);
            // 画像のフォーマットと画質と出力先を指定して保存
            capture.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
            Log.i("test","成功"+name);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.i("test","sippai ");
        }
        //ファイルのフラグを更新
        SharedPreferences prefer = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = prefer.edit();
        if (name.equals("CameraIntent5.jpg")) {
            editor.putBoolean("file5", true);
            editor.commit();
        }else if (name.equals("CameraIntent6.jpg")) {
            editor.putBoolean("file6", true);
            editor.commit();
        }else if (name.equals("CameraIntent7.jpg")) {
            editor.putBoolean("file7", true);
            editor.commit();
        }else if (name.equals("CameraIntent8.jpg")) {
            editor.putBoolean("file8", true);
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

    private boolean ScreenShotIntent() {
        Context context = getContext();
        // 保存先のフォルダー
        File cFolder = context.getExternalFilesDir(Environment.DIRECTORY_DCIM);
        Log.d("log", "path: " + String.valueOf(cFolder));

        boolean fileflag1 = PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean("file5",false);
        boolean fileflag2 = PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean("file6",false);
        boolean fileflag3 = PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean("file7",false);
        boolean fileflag4 = PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean("file8",false);

        String fileN = "CameraIntent5.jpg";


        pictureFlag = false;
        if (fileflag1 == false){
            fileN="CameraIntent5.jpg";

        }else if(fileflag2 == false){
            fileN="CameraIntent6.jpg";

        }else if(fileflag3 == false){
            fileN="CameraIntent7.jpg";

        }else if(fileflag4 == false){
            fileN="CameraIntent8.jpg";

        }
        else{
            pictureFlag = true;
        }
        if(pictureFlag == false) {



            SharedPreferences prefer = PreferenceManager.getDefaultSharedPreferences(getContext());
            SharedPreferences.Editor editor = prefer.edit();
            editor.putString("name2", fileN);
            //editor.putString("key_Hz","ok");
            editor.commit();

            return true;

        } else{
            /*************言語選択*******************/
            String language= PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("lang", "日本語");
            MainActivity main = new MainActivity();
            String s1 = main.LangReader("hinanSave",5,language);
            String s2 = main.LangReader("hinanSave",6,language);
            String s3 = main.LangReader("hinanSave",7,language);
            SpannableStringBuilder sb = new SpannableStringBuilder();
            sb.append(s1);
            sb.append("\n");
            sb.append(s2);
            sb.append("\n");
            sb.append(s3);
            Toast.makeText(getContext(),
                    sb,
                    Toast.LENGTH_LONG).show();
            return false;
        }
    }

}
