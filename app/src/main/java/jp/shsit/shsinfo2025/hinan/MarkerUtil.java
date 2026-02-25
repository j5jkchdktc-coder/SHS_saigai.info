package jp.shsit.shsinfo2025.hinan;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.OverlayItem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import jp.shsit.shsinfo2025.R;


class MarkerUtil {

    // debug
    private final static boolean D = true;
    private final static String TAG = "OSM";
    private final static String TAG_SUB = "MarkerUtil";

    private final static String SPACE = " ";

    private AssetManager mAssetManager;

    private Resources mResources;

    private List<Drawable> mMarkerIcons;


    /**
     *  constractor
     */
    public MarkerUtil(Context context) {
        //mAssetManager = context.getAssets();
        try {
            mResources = context.getResources();
        } catch (Exception e) {
        }

    }
    /**
     *  getMarkers
     */
    public List<OverlayItem> getMarkers(ArrayList<ListData> map_arrays, int hinan_type){
        setupMarkerIcon();

        /* Create ItemizedOverlay showing a some Markers on some cities. */

        List<OverlayItem> items = new ArrayList<>();

       // List<String> lines = readAsset(FILE_NAME);

        String DESC = "";
      //  int hinan_type=6;

        for(int i=1;i<map_arrays.size();i++) {
            // title, latitude, longitude, icon_num
                String title = map_arrays.get(i).name;
                double lat = parseDouble(map_arrays.get(i).lat);
                double lon = parseDouble(map_arrays.get(i).lon);
                int kouzui = parseInt(map_arrays.get(i).kouzui);
                int jishin = parseInt(map_arrays.get(i).jishin);
                int tsunami = parseInt(map_arrays.get(i).tsunami);
                int hinan = parseInt(map_arrays.get(i).jyou);
               //津波７地震６洪水３避難所１１


                Log.i("item", title + SPACE +  lat + SPACE + lon + SPACE  );
                int iconNum;
                //津波　赤
                if(hinan_type==7){
                    iconNum = 1;
                }//地震　緑
                else if(hinan_type ==6){
                    iconNum = 3;
                }//洪水　青
                else if(hinan_type ==3){
                    iconNum =2;
                }//避難所　紫
                else{
                    iconNum = 5;
                }

                //アイコンを変更する
                OverlayItem item = new OverlayItem(title, DESC, new GeoPoint(lat, lon));
                Drawable icon = getMarkerIcon( iconNum );

                if ( icon != null ) {
                    item.setMarker( icon );
                    item.setMarkerHotspot(OverlayItem.HotspotPlace.BOTTOM_CENTER);
                }
                if(hinan_type== 7 && tsunami==1) {
                    items.add(item);
                }
                if(hinan_type== 6 && jishin==1) {
                    items.add(item);
                }
                if(hinan_type== 3 && kouzui==1) {
                    items.add(item);
                }
                if(hinan_type== 11 && hinan==1) {
                    items.add(item);
                }
        } // for

        return items;
    } // getMarkers
    /**
     *  getMarkers
     */
    public List<OverlayItem> getMarker_current(String name , Double lat, Double lon){
        setupMarkerIcon();

        /* Create ItemizedOverlay showing a some Markers on some cities. */
        List<OverlayItem> items = new ArrayList<>();
        //アイコンを変更する
        OverlayItem item = new OverlayItem(name, "", new GeoPoint(lat, lon));
        Drawable icon = getMarkerIcon( 6);

        if ( icon != null ) {
            item.setMarker( icon );
            item.setMarkerHotspot(OverlayItem.HotspotPlace.BOTTOM_CENTER);
        }

        items.add(item);
        return items;
    } // getMarkers
    /**
     *  setupMarkerIcon
     */
    private void setupMarkerIcon() {

        mMarkerIcons = new ArrayList<Drawable>();

        mMarkerIcons.add( mResources.getDrawable(R.mipmap.pin0620) );
        mMarkerIcons.add( mResources.getDrawable(R.mipmap.pin_ao) );
        mMarkerIcons.add( mResources.getDrawable(R.mipmap.pin_gre) );
        mMarkerIcons.add( mResources.getDrawable(R.mipmap.pin_yel) );
        mMarkerIcons.add( mResources.getDrawable(R.mipmap.pin_pur) );
        mMarkerIcons.add( mResources.getDrawable(R.drawable.marker_32) );

    } // setupMarkeIcon


    /**
     *  getMarkerIcon
     */
    private Drawable getMarkerIcon( int num ) {
        Drawable icon = null;
        int index= num -1;

        if (index >= 0 && index < mMarkerIcons.size() ) {
            icon = mMarkerIcons.get(index);
        }

        return icon;
    } // getMarkerIcon


    /**
     *  readAsset
     */
    private  List<String> readAsset(String fileName ) {

        List<String> lines = new ArrayList<String>();

        InputStream is = null;
        BufferedReader br = null;

        try {
            try {
                is = mAssetManager.open(fileName);
                br = new BufferedReader(new InputStreamReader(is,"shift-JIS"));

                String str;
                while ((str = br.readLine()) != null) {
                    lines.add( str);
                }
            } finally {
                if (is != null) is.close();
                if (br != null) br.close();
            }
        } catch (Exception e){
            if (D) e.printStackTrace();
        }

        return lines;
    }


    /**
     * parseDouble
     */
    private  double parseDouble(String str) {
        double d = 0;
        try {
            d = Double.parseDouble(str);
        } catch (Exception e){
            if (D) e.printStackTrace();
        }
        return d;
    }

    /**
     * parseInt
     */
    private  int parseInt(String str) {
        int i = 0;
        try {
            i = Integer.parseInt( str.trim() );
        } catch (Exception e){
            if (D) e.printStackTrace();
        }
        return i;
    }

    /**
     * write into logcat
     */
    private  void log_d( String msg ) {
        if (D) Log.d( TAG, TAG_SUB + " " + msg );
    } // log_d

    public class Node {
        public double lat;
        public double lon;
        public String title;

        // constractor
        public Node (double _lat, double _lon, String _title) {
            lat = _lat;
            lon = _lon;
            title = _title;

        } // Node

    } // class Node

    //市町村名から市町村コードに変換するメソッド
    public String readCSV2(String city_name){
        cityData[] cityData;
        try {

            InputStream inputStream = mResources.getAssets().open("city_code.csv");
            //UTF-8に変更
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream,"UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line="";

            cityData = new cityData[1750];
            // 各要素ごとにインスタンス化
            for(int i = 0; i < 1750; i++) {
                cityData[i] = new cityData();
            }
            int i=0;
            Log.i("item",city_name+"です");
            while((line = bufferedReader.readLine()) != null){

                StringTokenizer stringTokenizer = new StringTokenizer(line,",");
                String V1= stringTokenizer.nextToken();
                String V2= stringTokenizer.nextToken();
                String V3 =stringTokenizer.nextToken();
                String V4 = stringTokenizer.nextToken();
                cityData[i].setId(V1);
                cityData[i].setName(V2);//施設名
                cityData[i].setCode(V4);//コード
                cityData[i].setEtc(V3);//提出状況

                i++;
            }


            //検索
            int a=1;
            while(a<i){
                String name=cityData[a].getName();
                if(name.contains(city_name))
                    break;
                a++;
            }
            if( a==i){//見つからない時
              //  hinanflag=true;
                return "45201";
            }
            else {
                Log.i("item", cityData[a].getCode() + "です");
            /*System.out.println(cityData[a].getEtc()+"です");
            System.out.println(cityData[a].getName()+"です");*/

                if (cityData[a].getEtc().equals("未提出")) {
                 return "11111" ;

                } else {
                  // hinanflag = false;
                }
            }
            return cityData[a].getCode();

        } catch (IOException e) {
            e.printStackTrace();

            return null;
        }
    }

} // classMarkerUtil