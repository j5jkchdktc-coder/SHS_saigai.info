package jp.shsit.shsinfo2025.hinan;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by shsit on 2021/04/21.
 */

public class CsvReader extends AsyncTask<String, Integer,ListViewAdapter> {
   // List<ListData> objects = new ArrayList<ListData>();

    private ListViewAdapter yAdapter;
    private MapsFragmenrt2 yActivity;
    private ProgressDialog mProgressDialog;
    // コンストラクタ
    public CsvReader(MapsFragmenrt2 activity, ListViewAdapter adapter) {
        yActivity =  activity;
        yAdapter = adapter;
    }

    // 非同期処理
    @Override
    protected ListViewAdapter doInBackground(String... params) {

        ListViewAdapter result = null;
        try {
            // HTTP経由でアクセスし、InputStreamを取得する
            Log.i("item",params[0]);
            result=  reader(params[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // ここで返した値は、onPostExecuteメソッドの引数として渡される
        return result;
    }

    // タスクを実行した直後にコールされる
    @Override
    protected void onPreExecute() {
        // プログレスバーを表示する

            mProgressDialog = new ProgressDialog(yActivity.getContext());
            mProgressDialog.setMessage("Now Loading...");
            mProgressDialog.show();

    }

    // メインスレッド上で実行される

    @Override
    protected void onPostExecute(ListViewAdapter list) {
        super.onPostExecute(list);
        mProgressDialog.dismiss();
        callbacktask.CallBack("end");
       // yActivity.setListAdapter(list);

    }





    public ListViewAdapter reader(String is) throws IOException, XmlPullParserException {
        XmlPullParser parser = Xml.newPullParser();

        //AssetManager assetManager = context.getResources().getAssets();
        HttpURLConnection urlConnection = null;

        try {
            // CSVファイルの読み込み
          //  InputStream inputStream = assetManager.open("data.csv");

            URL url = new URL( is );

            //HttpURLConnection インスタンス生成
            urlConnection = (HttpURLConnection) url.openConnection();
            // タイムアウト設定
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(20000);
            // リクエストメソッド
            urlConnection.setRequestMethod("GET");
            // リダイレクトを自動で許可しない設定
            urlConnection.setInstanceFollowRedirects(false);
            // ヘッダーの設定(複数設定可能)
            urlConnection.setRequestProperty("Accept-Language", "jp");
            // 接続
            urlConnection.connect();
            int resp = urlConnection.getResponseCode();
            InputStream is1 = null;

            is1 = urlConnection.getInputStream();
            InputStream inputStream = is1;

            InputStreamReader inputStreamReader = new InputStreamReader(inputStream,"shift-jis");
            BufferedReader bufferReader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = bufferReader.readLine()) != null) {

                //カンマ区切りで１つづつ配列に入れる
                ListData data = new ListData();
                String[] RowData = line.split(",");

                //CSVの左([0]番目)から順番にセット
                if(RowData[0].equals("")){
                    data.id = "0";
                }else {
                    data.id = RowData[0];
                }
                if(RowData[1].equals("")){
                    data.name = "0";
                }else {
                    data.name = RowData[1];
                }
                if(RowData[2].equals("")){
                    data.setAddress("0");
                }else {
                    data.setAddress(RowData[2]);
                }
                if(RowData[3].equals("")){
                    data.kouzui = "0";
                }else {
                    data.kouzui = RowData[3];
                }
                if(RowData[6].equals("")){
                    data.jishin = "0";
                }else {
                    data.jishin = RowData[6];
                }
                if(RowData[7].equals("")){
                    data.tsunami = "0";
                }else {
                    data.tsunami = RowData[7];
                }
                if(RowData[11].equals("")){
                    data.jyou = "0";
                }else {
                    data.jyou = RowData[11];
                }
                if(RowData[12].equals("")){
                    data.lat = "0";
                }else {
                    data.lat = RowData[12];
                }
                if(RowData[13].equals("")){
                    data.lon = "0";
                }else {
                    data.lon = RowData[13];
                }
                yAdapter.add(data);
            }

            bufferReader.close();
            return yAdapter;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return yAdapter;
    }
    private CallBackTask callbacktask;
    public void setOnCallBack(CallBackTask _cbj) {
        callbacktask = _cbj;
    }

    public static class CallBackTask {
        public void CallBack(String result) {
        }
    }

}
