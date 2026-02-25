package jp.shsit.shsinfo2025;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import jp.shsit.shsinfo2025.ui.kikikuru.KikikuruFragment;
import jp.shsit.shsinfo2025.ui.sonaeru.SonaeFragment;
import jp.shsit.shsinfo2025.ui.uses.UsesHomeBase;
import jp.shsit.shsinfo2025.ui.weather.WeatherFragment;


public class MainActivity extends AppCompatActivity implements LatLongCatch.OnLocationResultListener{

    private final int REQUEST_PERMISSION = 10;
    private SettingsClient settingsClient;
    private LocationSettingsRequest locationSettingsRequest;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;
    private Location location;

    private String lastUpdateTime;
    private Boolean requestingLocationUpdates;
    private static final int REQUEST_CHECK_SETTINGS = 0x1;
    private int priority = 0;
    double lat,lng;
    private FusedLocationProviderClient fusedLocationClient;
    private static final String PREF_NAME = "toggle_prefs";
    private static final String KEY_TOGGLE1_STATE = "toggle1_state";
    private static final String KEY_TOGGLE2_STATE = "toggle2_state";

    public static ArrayList<ItemLang> itemArray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String  language= PreferenceManager.getDefaultSharedPreferences(this).getString("lang", "日本語");
        setContentView(R.layout.activity_main);

        //ナビゲーションバーインスタンス化
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.nav_view);
        //ｊｐｇ等のアイコンを表示
        bottomNavigationView.setItemIconTintList(null);

        //下部にあるナビゲーションボトムバーのアイテム一つ一つを取得
        //    BottomNavigationMenuView menuView = (BottomNavigationMenuView) bottomNavigationView.getChildAt(0);

        if (getIntent().getStringExtra("navigateTo") != null) {
            handleIntent(getIntent());
        } else {
            // 通常のフラグメント表示
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.nav_host_fragment, new WeatherFragment());
           // transaction.addToBackStack(null); // ← 追加
            transaction.commit();
        }


        int width= PreferenceManager.getDefaultSharedPreferences(this).getInt("width", 100);
        Log.i("item",width+",");
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int winW = dm.widthPixels;
        int winH = dm.heightPixels;

        Menu bottomNavigationMenu = bottomNavigationView.getMenu();

        switch(language) {
            case "English":
                bottomNavigationMenu.findItem(R.id.navigation_home).setIcon(R.drawable.ic_weather_en_24dp);
                bottomNavigationMenu.findItem(R.id.navigation_dashboard).setIcon(R.drawable.ic_kikikuru_en_24dp);
                bottomNavigationMenu.findItem(R.id.navigation_notifications).setIcon(R.drawable.ic_sonae_en_24dp);
                bottomNavigationMenu.findItem(R.id.navigation_uses).setIcon(R.drawable.ic_uses_en_24dp);
                break;
            case "Chinese":
                bottomNavigationMenu.findItem(R.id.navigation_home).setIcon(R.drawable.ic_weather_ch_24dp);
                bottomNavigationMenu.findItem(R.id.navigation_dashboard).setIcon(R.drawable.ic_kikikuru_ch_24dp);
                bottomNavigationMenu.findItem(R.id.navigation_notifications).setIcon(R.drawable.ic_sonae_ch_24dp);
                bottomNavigationMenu.findItem(R.id.navigation_uses).setIcon(R.drawable.ic_uses_ch_24dp);
                break;
            case "Vietnamese":
                bottomNavigationMenu.findItem(R.id.navigation_home).setIcon(R.drawable.ic_weather_vn_24dp);
                bottomNavigationMenu.findItem(R.id.navigation_dashboard).setIcon(R.drawable.ic_kikikuru_vn_24dp);
                bottomNavigationMenu.findItem(R.id.navigation_notifications).setIcon(R.drawable.ic_sonae_vn_24dp);
                bottomNavigationMenu.findItem(R.id.navigation_uses).setIcon(R.drawable.ic_uses_vn_24dp);
                break;
            default:
                bottomNavigationMenu.findItem(R.id.navigation_home).setIcon(R.drawable.ic_weather_24dp);
                bottomNavigationMenu.findItem(R.id.navigation_dashboard).setIcon(R.drawable.ic_kikikuru_24dp);
                bottomNavigationMenu.findItem(R.id.navigation_notifications).setIcon(R.drawable.ic_sonae_24dp);
                bottomNavigationMenu.findItem(R.id.navigation_uses).setIcon(R.drawable.ic_uses_24dp);
                break;
        };

        Log.i("WHAT THIS?", LangReader("splash", 0, "English"));

        if (winH<2000){
            //アイコンのサイズを変更
            bottomNavigationView.setItemIconSize(100);

        }else {
            //アイコンのサイズを変更
            bottomNavigationView.setItemIconSize(200);
        }

        // Menu menu = bottomNavigationView.getMenu();
        // menu.findItem(R.id.navigation_home).setTitle("yosika");

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                int itemId = item.getItemId();

                if (itemId == R.id.navigation_home) {
                    item.setChecked(true);
                    transaction.replace(R.id.nav_host_fragment, new WeatherFragment());
                    transaction.commit();
                } else if (itemId == R.id.navigation_dashboard) {
                    item.setChecked(true);
                    transaction.replace(R.id.nav_host_fragment, new KikikuruFragment());
                    transaction.commit();
                } else if (itemId == R.id.navigation_notifications) {
                    item.setChecked(true);
                    transaction.replace(R.id.nav_host_fragment, new SonaeFragment());
                    transaction.commit();
                } else if (itemId ==R.id.navigation_uses) {
                    item.setChecked(true);
                    transaction.replace(R.id.nav_host_fragment, new UsesHomeBase());
                    transaction.commit();
                } else  {
                    item.setChecked(false);
                }

                return false;
            }
        });

        LatLongCatch locationManager = new LatLongCatch(MainActivity.this,MainActivity.this);
        locationManager.startLocationUpdates();

        ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            isGranted -> {
                if (isGranted) {
                    checkAlerts();
                } else {
                    Toast.makeText(this, "通知の許可が必要です", Toast.LENGTH_SHORT).show();
                }
            }
        );

        requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS);

    }

    //通知処理
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent); // 新しいインテントをセット
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (intent != null && intent.getStringExtra("navigateTo") != null) {
            String fragmentName = intent.getStringExtra("navigateTo");
            if ("NotificationFragment".equals(fragmentName)) {
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();

                // NotificationFragment に置き換え
                NotificationFragment notificationFragment = new NotificationFragment();
                transaction.replace(R.id.nav_host_fragment, notificationFragment);

                // 戻るボタンで前の画面（WeatherFragment）に戻れるようにスタックに追加
                transaction.addToBackStack(null);

                transaction.commit();
            }
        }
    }



    @Override
    protected void onStart() {
        super.onStart();

        checkAlerts();
    }

    @Override
    public void onLocationResult(LocationResult locationResult) {
        // 緯度・経度を取得
        double lat = locationResult.getLastLocation().getLatitude();
        double lon = locationResult.getLastLocation().getLongitude();
        Log.i("test",lat+","+lon);
        SharedPreferences prefer = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefer.edit();
        editor.putFloat("lat",(float) lat);
        editor.putFloat("lon",(float) lon);
        editor.commit();

// 住所を取得
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lon, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                String addressStr = address.getAddressLine(0); // 住所全体
                Log.i("test", "住所: " + addressStr);
            } else {
                Log.i("test", "住所が見つかりませんでした。");
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("test", "Geocoder失敗: " + e.getMessage());
        }

    }

    public String LangReader(String page,int no,String language){
        //検索
        String result = "";
        int count=0;

        SplashActivity splashActivity = new SplashActivity();
        itemArray = splashActivity.itemArray;

        for(int i=0;i<itemArray.size();i++) {

            //PreferenceManager.getDefaultSharedPreferences(SplashActivity.this).getString("lang","日本語");
            String page1 = (String) itemArray.get(i).getPage();
            if(page1.equals(page)){
                System.out.println(count);
                if(no==count){
                    if(language.equals("English")){
                        result = (String) itemArray.get(i).getEng();
                    }else if(language.equals("ひらがな")){
                        result = (String) itemArray.get(i).getSmall();
                    }else if(language.equals("Vietnamese")) {
                        result = (String) itemArray.get(i).getVnm();
                    }else if(language.equals("Chinese")) {
                        result = (String) itemArray.get(i).getChn();
                    }else {
                        result = (String) itemArray.get(i).getJap();
                    }
                    return result;
                }
                count++;
            }
        }
        return result;
    }

    private void checkAlerts() {
        Float lat = PreferenceManager.getDefaultSharedPreferences(this).getFloat("lat", 0.0f);
        Float lon = PreferenceManager.getDefaultSharedPreferences(this).getFloat("lon", 0.0f);

        SharedPreferences preferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        boolean thunderState = preferences.getBoolean(KEY_TOGGLE1_STATE, false);
        boolean hotterState = preferences.getBoolean(KEY_TOGGLE2_STATE, false);

        SharedPreferences notifyPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean thunderNotified = notifyPrefs.getBoolean("thunder_notified", false);
        boolean heatNotified = notifyPrefs.getBoolean("heat_notified", false);

// ログにトグルの状態 + 通知済みフラグを出力
        Log.i("test2", "スイッチ状態 → 雷: " + thunderState + ", 熱中症: " + hotterState);
        Log.i("test2", "通知済み状態 → 雷: " + thunderNotified + ", 熱中症: " + heatNotified);

        if (thunderState) {
            ThunderAlertChecker thunderChecker = new ThunderAlertChecker(this);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                thunderChecker.checkThunderAlert(lat, lon, alertItem -> {
                    if (alertItem != null) {
                        boolean alreadyNotified = notifyPrefs.getBoolean("thunder_notified", false);
                        if (!alreadyNotified) {
                            sendNotification(alertItem);
                            notifyPrefs.edit().putBoolean("thunder_notified", true).apply();
                        }
                    }
                    return null;
                });
            }
        }

        if (hotterState) {
            HeatAlertChecker heatChecker = new HeatAlertChecker(this);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                heatChecker.checkHeatAlert(lat, lon, alertItem -> {
                    if (alertItem != null) {
                        boolean alreadyNotified = notifyPrefs.getBoolean("heat_notified", false);
                        if (!alreadyNotified) {
                            sendNotification(alertItem);
                            notifyPrefs.edit().putBoolean("heat_notified", true).apply();
                        }
                    }
                    return null;
                });
            }
        }
    }


    private void sendNotification(AlertItem alert) {
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = "thunder_alert_channel";

        // Notification Channelの作成（初回起動時またはチャンネルがまだ作成されていない場合のみ）
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelName = "Thunder Alert Channel";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, importance);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        // 通知をタップしたときに実行するインテントの作成
        Intent intent = new Intent(this.getApplication(), MainActivity.class);
        intent.putExtra("navigateTo", "NotificationFragment");
        PendingIntent pendingIntent = PendingIntent.getActivity(this.getApplication(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // 通知の作成
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this.getApplicationContext(), channelId)
                .setSmallIcon(R.mipmap.icon) // 適切なアイコンに置き換えてください
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        // 例: alert.getPref() が「熊本」の場合、「熊本県」と表示されます
        String fullPref = formatPrefectureName(alert.getPref());
        // 雷注意報
        if (alert.getContent().contains("雷")) {
            builder
                    .setContentTitle("雷注意報")
                    .setContentText(fullPref + "に"  + "雷注意報発表");
            Log.i("test","通知1");
            // 通知の送信
            notificationManager.notify(1, builder.build()); // 通知IDは1

            // 熱中症警戒アラート
        } else if (alert.getContent().contains("熱中症")) {
            builder
                    .setContentTitle("熱中症警戒アラート")
                    .setContentText(fullPref + "に"  + "熱中症警戒アラート発表");

            notificationManager.notify(2, builder.build()); // 通知IDは2

        }
    }

    public String formatPrefectureName(String prefecture) {
        switch (prefecture) {
            case "北海道":
                return "北海道";
            case "京都":
                return "京都府";
            case "大阪":
                return "大阪府";
            case "東京":
                return "東京都";
            default:
                return prefecture + "県";
        }
    }





}