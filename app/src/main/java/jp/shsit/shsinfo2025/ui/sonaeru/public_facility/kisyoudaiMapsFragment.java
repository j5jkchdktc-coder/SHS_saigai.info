package jp.shsit.shsinfo2025.ui.sonaeru.public_facility;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.LocationResult;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.MapTileProviderBasic;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.MinimapOverlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.TilesOverlay;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

import jp.shsit.shsinfo2025.MainActivity;
import jp.shsit.shsinfo2025.R;
import jp.shsit.shsinfo2025.hinan.LatLongCatch;

public class kisyoudaiMapsFragment extends Fragment implements LatLongCatch.OnLocationResultListener {

    // debug
    private final static boolean D = true;
    private final static String TAG = "OSM";
    private final static String TAG_SUB = "MainActivity";

    private static final double MAP_ZOOM = 18.0;

    LatLongCatch locationManager;
    double lat, lng;

    private MapView mMapView;


    jp.shsit.shsinfo2025.hinan.cityData[] cityData;


    EditText ed;
    String value = "現在地";


    public static ArrayList<CsvItem> itemArray;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Configuration.getInstance().load(getContext(),
                PreferenceManager.getDefaultSharedPreferences(getContext()));

        //Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);
        locationManager = new LatLongCatch(getContext(), this);
        locationManager.startLocationUpdates();


    } //  onCreate

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.kisyoudai_map, container, false);

        mMapView = (MapView) view.findViewById(R.id.mapView);
        mMapView.setBuiltInZoomControls(true);
        mMapView.setMultiTouchControls(true);


        /*************言語選択*******************/
        String language = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("lang", "日本語");
        MainActivity main = new MainActivity();
        String t1 = main.LangReader("hinan", 1, language);


        /*********************************/


        Button bt1 = view.findViewById(R.id.hinata_btn1);

        bt1.setText(main.LangReader("hinan", 5, language));
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPause();
                getParentFragmentManager().popBackStack();

            }
        });
        //AED読み込み
        AED_CsvReader();
        return view;
    }

    /**
     * onResume
     */
    @Override
    public void onResume() {
        super.onResume();

        Configuration.getInstance().load(getContext(),
                PreferenceManager.getDefaultSharedPreferences(getContext()));

        if (mMapView != null) {
            mMapView.onResume();
        }
    } // onResume


    /**
     * onPause
     */
    @Override
    public void onPause() {
        super.onPause();

        Configuration.getInstance().save(getContext(),
                PreferenceManager.getDefaultSharedPreferences(getContext()));
        if (mMapView != null) {
            mMapView.onPause();
        }
    } // onPause


    /**
     * setupMarker
     */
    private void setupMarker() {
        /* Itemized Overlay */
/************ 地理院 標準地図*****************************************************/
        final String TILE_SERVER_1 = "https://cyberjapandata.gsi.go.jp/xyz/std/";
        // Add tiles layer with Custom Tile Source
        final MapTileProviderBasic tileProvider = new MapTileProviderBasic(getContext());

        ITileSource tileSource = null;
        tileSource = new XYTileSource("GSI", 12, 18, 256, ".png",
                new String[]{TILE_SERVER_1});
        tileProvider.setTileSource(tileSource);
        final TilesOverlay tilesOverlay = new TilesOverlay(tileProvider, this.getContext());
        tilesOverlay.setLoadingBackgroundColor(Color.TRANSPARENT);
        mMapView.getOverlays().clear();
        mMapView.getOverlays().add(tilesOverlay);
        mMapView.invalidate();
/*****************************************************************************/

//　基本
        mMapView.setMultiTouchControls(true);


        //ひなた運動公園　AED　設置場所
        Drawable markerIcon = getResources().getDrawable(R.mipmap.aed);


        //ひなた運動公園　津波避難　高台
        for(int i=1;i<itemArray.size();i++){
            CsvItem item = itemArray.get(i);
            if(item.getKind().equals("避難高台")) {
                double lat1 = Double.parseDouble(item.getLat());
                double lng1 = Double.parseDouble(item.getLng());
                String  name = item.getName();
                String draw = item.getDraw();
                String kind = item.getKind();

                int picId = getResources().getIdentifier(draw, "mipmap", getActivity().getPackageName());

                Log.i("test",lat1+","+lng1+","+draw+","+picId);
                markerIcon = getResources().getDrawable(picId);
                Marker marker16 = new Marker(mMapView);
                marker16.setPosition(new GeoPoint(lat1,lng1));
                marker16.setTitle(kind);
                marker16.setSnippet(name);
                marker16.setIcon(markerIcon);
                mMapView.getOverlays().add(marker16);

            }

            if(item.getKind().equals("施設")) {
                double lat1 = Double.parseDouble(item.getLat());
                double lng1 = Double.parseDouble(item.getLng());
                String  name = item.getName();
                String draw = item.getDraw();
                String kind = item.getKind();

                int picId = getResources().getIdentifier(draw, "mipmap", getActivity().getPackageName());

                Log.i("test",lat1+","+lng1+","+draw+","+picId);
                markerIcon = getResources().getDrawable(picId);
                Marker marker16 = new Marker(mMapView);
                marker16.setPosition(new GeoPoint(lat1,lng1));
                marker16.setTitle(kind);
                marker16.setSnippet(name);
                marker16.setIcon(markerIcon);
                mMapView.getOverlays().add(marker16);

            }
            if(item.getKind().indexOf("AED")==0) {
                double lat1 = Double.parseDouble(item.getLat());
                double lng1 = Double.parseDouble(item.getLng());
                String  name = item.getName();
                String draw = item.getDraw();
                String kind = item.getKind();

                int picId = getResources().getIdentifier(draw, "mipmap", getActivity().getPackageName());

                float dis[] = getDistance(lat,lng,lat1,lng1);
                int dis2= (int) (dis[0]+0.5);

                markerIcon = getResources().getDrawable(picId);
                Marker marker16 = new Marker(mMapView);
                marker16.setPosition(new GeoPoint(lat1,lng1));
                marker16.setTitle(kind+"\n"+name+"\n"+"現在地からの直線距離は"+dis2 +"m");
             //   marker16.setSnippet(name);
                marker16.setIcon(markerIcon);
                mMapView.getOverlays().add(marker16);

            }

            if(item.getKind().indexOf("担架")==0) {
                double lat1 = Double.parseDouble(item.getLat());
                double lng1 = Double.parseDouble(item.getLng());
                String  name = item.getName();
                String draw = item.getDraw();
                String kind = item.getKind();

                int picId = getResources().getIdentifier(draw, "mipmap", getActivity().getPackageName());

                float dis[] = getDistance(lat,lng,lat1,lng1);
                int dis2= (int) (dis[0]+0.5);

                markerIcon = getResources().getDrawable(picId);
                Marker marker16 = new Marker(mMapView);
                marker16.setPosition(new GeoPoint(lat1,lng1));
                marker16.setTitle(kind+"\n"+name+"\n"+"現在地からの直線距離は"+dis2 +"m");
                //   marker16.setSnippet(name);
                marker16.setIcon(markerIcon);
                mMapView.getOverlays().add(marker16);

            }
        }


    }

    private void setupMarker_current(String name) {
        final MinimapOverlay miniMapOverlay = new MinimapOverlay(getContext(),
                mMapView.getTileRequestCompleteHandler());
        mMapView.getOverlays().add(miniMapOverlay);
    }

    public void AED_CsvReader(){
        itemArray = new ArrayList<CsvItem>();
        CsvItem item=new CsvItem();
        String result = "";
        try {
            InputStream inputStream = getResources().getAssets().open("kisyoudai.csv");
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream,"UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line="";
            while((line = bufferedReader.readLine()) != null){
                ArrayList<String> array = new ArrayList<>();
                StringTokenizer stringTokenizer = new StringTokenizer(line,",");

                try {
                    String V1= stringTokenizer.nextToken();
                    String V2= stringTokenizer.nextToken();
                    String V3= stringTokenizer.nextToken();
                    String V4 = stringTokenizer.nextToken();
                    String V5 = stringTokenizer.nextToken();
                    item.setKind(V1);
                    item.setName(V2);
                    item.setLat(V3);
                    item.setLng(V4);
                    item.setDraw(V5);
                }catch (Exception e){
                }

                itemArray.add(item);
                item =new CsvItem();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //距離を求める　results[0]= ２点間距離　results[1]=始点から見た方位角　results[2]=終点から見た方位角
    public float[] getDistance(double x, double y, double x2, double y2) {
        // 結果を格納するための配列を生成
        float[] results = new float[3];

        // 距離計算
        Location.distanceBetween(x, y, x2, y2, results);

        return results;
    }
    //球面三角法
    public double distance(double lat1, double lon1, double lat2, double lon2) {
        double radLat1 = Math.toRadians(lat1);
        double radLon1 = Math.toRadians(lon1);
        double radLat2 = Math.toRadians(lat2);
        double radLon2 = Math.toRadians(lon2);
        double r = 6378137.0; // equatorial radius
        double averageLat = (radLat1 - radLat2) / 2;
        double averageLon = (radLon1 - radLon2) / 2;
        return 2 * r * Math.asin(Math.sqrt(sin(averageLat) * sin(averageLat) + cos(radLat1) * cos(radLat2) * sin(averageLon) * sin(averageLon)));
    }


    @Override
    public void onLocationResult(LocationResult locationResult) {
        lat = locationResult.getLastLocation().getLatitude();
        lng = locationResult.getLastLocation().getLongitude();

        setupMarker();

        Marker marker = new Marker(mMapView);
        marker.setTitle("現在地");
        Drawable icon=getResources().getDrawable(R.mipmap.pin0620);
        GeoPoint Point = new GeoPoint(lat, lng);
        OverlayItem item = new OverlayItem("", "", Point);
        item.setMarker(icon);
        marker.setIcon(icon);
        marker.setPosition(Point);

        //ピンを表示する
        mMapView.getOverlays().add(marker);


        //地図を表示する
        IMapController mapController = mMapView.getController();
        mapController.setZoom(18);

        //武道館　31.828812,131.44659
        Double dis = distance(31.93863,131.41405,lat,lng);
        Log.i("test",dis+"desu");
        //地図の表示位置
        //現在地周辺

        if(dis>=1300){//公園外なら 武道館 1300m以上
            GeoPoint centerPoint = new GeoPoint(31.93863,131.41405);
            mapController.setCenter(centerPoint);
        }else{//公園内なら　現在地
            GeoPoint centerPoint = new GeoPoint(lat,lng);
            mapController.setCenter(centerPoint);
        }




    }

}