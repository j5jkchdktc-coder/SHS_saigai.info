package jp.shsit.shsinfo2025.ui.kikikuru.sinsui;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Handler;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Locale;

public class IntentService1 extends IntentService {

   final static String TAG = "ServiceTest4";
   private String urlDepth = "";
   private String urlEleva = "";
   private String ip = "";
   private Handler handler = new Handler();
   private float lat;
   private float lon;
   String  dep,eleva,address;
   public IntentService1() {
      super(TAG);
   }



   @Override
   protected void onHandleIntent(Intent intent) {

       //MainActivityより値を取得する
       lat = intent.getFloatExtra("lat",0.0f);
       lon = intent.getFloatExtra("lon",0.0f);

      // lat=31.0f;
       //lon=131.07f;


      urlDepth ="https://suiboumap.gsi.go.jp/shinsuimap/Api/Public/GetMaxDepth?lon="+lon+"&lat="+lat;
      urlEleva = "https://cyberjapandata2.gsi.go.jp/general/dem/scripts/getelevation.php?lon="+lon+"&lat="+lat+"&outtype=JSON";



      Thread thread = new Thread(new Runnable() {
         @Override
         public void run() {
            String response = "";
            try {
               response = getAPI(urlEleva);
               JSONObject rootJSON = new JSONObject(response);
               eleva = rootJSON.getString("elevation");
            }catch (JSONException e){
               e.printStackTrace();
               System.out.println("エラー1");
            }
            try {
               response = getAPI(urlDepth);
               JSONObject rootJSON2 = new JSONObject(response);
               dep = rootJSON2.getString("Depth");
            } catch (JSONException e) {
               e.printStackTrace();
               System.out.println("エラー2");
            }
              address = re_geo_coder();
            Log.i("test",address+"です");

            handler.post(new Runnable() {
               @Override
               public void run() {
                  //レシーバーに値を渡す
                  Intent broadcastIntent = new Intent();
                  Log.i("test",dep+","+eleva);
                  broadcastIntent.putExtra("depth", dep);
                  broadcastIntent.putExtra("eleva", eleva);
                  broadcastIntent.putExtra("address", address);
                  broadcastIntent.setAction("MY_ACTION");
                  getBaseContext().sendBroadcast(broadcastIntent);
               }
            });
         }
      });
      thread.start();
   }

   public String getAPI(String urltext){
      HttpURLConnection urlConnection = null;
      InputStream inputStream = null;
      String result = "";
      String str = "";
      try {
         URL url = new URL(urltext);
         // 接続先URLへのコネクションを開く．まだ接続されていない
         urlConnection = (HttpURLConnection) url.openConnection();
         // 接続タイムアウトを設定
         urlConnection.setConnectTimeout(10000);
         // レスポンスデータの読み取りタイムアウトを設定
         urlConnection.setReadTimeout(10000);
         // ヘッダーにUser-Agentを設定
         urlConnection.addRequestProperty("User-Agent", "Android");
         // ヘッダーにAccept-Languageを設定
         urlConnection.addRequestProperty("Accept-Language", Locale.getDefault().toString());
         // HTTPメソッドを指定
         urlConnection.setRequestMethod("GET");
         //リクエストボディの送信を許可しない
         urlConnection.setDoOutput(false);
         //レスポンスボディの受信を許可する
         urlConnection.setDoInput(true);
         // 通信開始
         urlConnection.connect();
         // レスポンスコードを取得
         int statusCode = urlConnection.getResponseCode();
         // レスポンスコード200は通信に成功したことを表す
         if (statusCode == 200){
            inputStream = urlConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
            // 1行ずつレスポンス結果を取得しstrに追記
            result = bufferedReader.readLine();
            while (result != null){
               str += result;
               result = bufferedReader.readLine();
            }
            bufferedReader.close();
         }
      } catch (MalformedURLException e) {
         e.printStackTrace();
      } catch (IOException e) {
         e.printStackTrace();
      }
      // レスポンス結果のJSONをString型で返す
      return str;
   }
public String re_geo_coder(){
   String result="";
   // ジオコーディングサービスが存在していない
   if (!Geocoder.isPresent()) return null;
   Geocoder coder = new Geocoder(IntentService1.this);
   // ネットワークアクセスが発生するので、UI スレッド以外で動かすこと
   List<Address> addresses = null;
   try {
      addresses = coder.getFromLocation( lat, lon, 1);
   } catch (IOException e) {
      e.printStackTrace();
   }
   // リバースジオコーディングに失敗したときは、 addresses が null もしくは空リストになる
   String add=addresses.get(0).getAdminArea()+addresses.get(0).getAddressLine(0);
   String[] add2 =add.split("、");
   try {
      result = add2[1];
   }catch (Exception e){
      result = "エラー";
   }

   return result;
  /* try {
      String add = addresses.get(0).getAddressLine(0);
      String[] add2 =add.split("、");  //日本、〒880-0211 宮崎県宮崎市佐土原町下田島21567なので、
      return add2[1];                        // 、で分割して２個目をとる
   }catch (Exception e){
      e.printStackTrace();
      return null;
   }*/
}


}
