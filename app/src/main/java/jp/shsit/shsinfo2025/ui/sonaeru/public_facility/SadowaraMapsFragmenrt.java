package jp.shsit.shsinfo2025.ui.sonaeru.public_facility;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.TilesOverlay;

import jp.shsit.shsinfo2025.LatLongCatch;
import jp.shsit.shsinfo2025.MainActivity;
import jp.shsit.shsinfo2025.R;

public class SadowaraMapsFragmenrt extends Fragment implements LatLongCatch.OnLocationResultListener{

    // debug
    private final static boolean D = true;
    private final static String TAG = "OSM";
    private final static String TAG_SUB = "MainActivity";

    private static final double MAP_ZOOM = 18.0;

    LatLongCatch locationManager;
    double lat,lon;

    private MapView mMapView;


    jp.shsit.shsinfo2025.hinan.cityData[] cityData;



    EditText ed;
    String value="現在地";



    /**
     * onCreate
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Configuration.getInstance().load(getContext(),
                PreferenceManager.getDefaultSharedPreferences(getContext()));

      //  Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);
        locationManager = new LatLongCatch(getContext(),this);
        locationManager.startLocationUpdates();




    } //  onCreate
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sadowara_map, container, false);

        mMapView = (MapView) view.findViewById(R.id.mapView);
        mMapView.setBuiltInZoomControls(true);
        mMapView.setMultiTouchControls(true);



        /*************言語選択*******************/
        String language= PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("lang", "日本語");
        MainActivity main = new MainActivity();
        String t1= main.LangReader("hinan",1,language);


        /*********************************/




        Button bt1 = view.findViewById(R.id.hinan_btn1);

        bt1.setText(main.LangReader("hinan",5,language));
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPause();
                getParentFragmentManager().popBackStack();

            }
        });

        setupMarker();
        return  view;
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

        ITileSource tileSource =  null;
        tileSource = new XYTileSource( "GSI",  12, 18, 256, ".png",
                new String[] { TILE_SERVER_1 });
        tileProvider.setTileSource(tileSource);
        final TilesOverlay tilesOverlay = new TilesOverlay(tileProvider, this.getContext());
        tilesOverlay.setLoadingBackgroundColor(Color.TRANSPARENT);
        mMapView.getOverlays().clear();
        mMapView.getOverlays().add(tilesOverlay);
        mMapView.invalidate();
/*****************************************************************************/

//　基本
        mMapView.setMultiTouchControls(true);




//担架

        Drawable  markerIcon = getResources().getDrawable(R.mipmap.sado_tanka1);
        Marker marker2 = new Marker( mMapView );
        marker2.setPosition( new GeoPoint(32.023225,131.466801 ) );

        Double dis = distance(lat,lon,32.023225,131.466801);
        int i= (int) (dis+0.5);
        marker2.setTitle ( "担架\n"+ "管理棟1階保健室\n"+ "現在地からの直線距離は"+i+"m" );
        marker2.setIcon(markerIcon);
        mMapView.getOverlays().add(marker2);


        markerIcon = getResources().getDrawable(R.mipmap.sado_tanka1);
        Marker marker22 = new Marker( mMapView );
        marker22.setPosition( new GeoPoint(32.023943,131.465857) );

        Double dis2 = distance(lat,lon,32.023943,131.465857);
        int a= (int) (dis2+0.5);
        marker22.setTitle ( "担架\n"+ "産振棟一階工芸実習室１横\n"+ "現在地からの直線距離は"+a+"m" );
        marker22.setIcon(markerIcon);
        mMapView.getOverlays().add(marker22);


        //   System.out.println(dis+"です");
        markerIcon = getResources().getDrawable(R.mipmap.sado_tanka1);
        Marker marker3 = new Marker( mMapView );
        marker3.setPosition( new GeoPoint(32.023823,131.465857) );

        Double dis22 = distance(lat,lon,32.023823,131.465857);
        int aa= (int) (dis22+0.5);
        marker3.setTitle ( "担架\n"+ "産振棟三階トイレ付近\n"+ "現在地からの直線距離は"+aa+"m" );
        marker3.setIcon(markerIcon);
        mMapView.getOverlays().add(marker3);


        markerIcon = getResources().getDrawable(R.mipmap.sado_tanka1);
        Marker marker222 = new Marker( mMapView );
        marker222.setPosition( new GeoPoint(32.022634,131.466039) );

        Double dis3 = distance(lat,lon,32.022634,131.466039);
        int b= (int) (dis3+0.5);
        marker222.setTitle ( "担架\n"+  "体育館\n"+ "現在地からの直線距離は"+b+"m");
        marker222.setIcon(markerIcon);
        mMapView.getOverlays().add(marker222);



//AED

        markerIcon = getResources().getDrawable(R.mipmap.aed);
        Marker marker1 = new Marker( mMapView );
        marker1.setPosition( new GeoPoint(32.022765,131.466039) );

        Double dis4 = distance(lat,lon,32.022765,131.466039);
        int c= (int) (dis4+0.5);
        marker1.setTitle ( "AED\n " +"体育館\n"+ "現在地からの直線距離は"+c+"m");
        //  marker1.setSnippet( "管理棟1階保健室"+ "現在地からの直線距離は"+c+"m");
        marker1.setIcon(markerIcon);
        mMapView.getOverlays().add(marker1);

        markerIcon = getResources().getDrawable(R.mipmap.aed);
        Marker marker11 = new Marker( mMapView );
        marker11.setPosition( new GeoPoint(32.023061,131.466812) );

        Double dis5 = distance(lat,lon,32.023061,131.466812);
        int d= (int) (dis5+0.5);
        marker11.setTitle ( "AED\n "+ "管理棟1階保健室前\n"+ "現在地からの直線距離は"+d +"m" );
        // marker11.setSnippet( "管理棟1階保健室前"+ "現在地からの直線距離は"+d +"m");
        marker11.setIcon(markerIcon);
        mMapView.getOverlays().add(marker11);

        markerIcon = getResources().getDrawable(R.mipmap.aed);
        Marker marker111 = new Marker( mMapView );
        marker111.setPosition( new GeoPoint(32.023916,131.465884) );

        Double dis6 = distance(lat,lon,32.023916,131.465884);
        int e= (int) (dis6+0.5);
        marker111.setTitle ( "AED\n " +"産振棟一階工芸実習室１横\n"+ "現在地からの直線距離は"+e+"m");
        // marker111.setSnippet( "産振棟一階工芸自習室１横"+ "現在地からの直線距離は"+e+"m");
        marker111.setIcon(markerIcon);
        mMapView.getOverlays().add(marker111);

        markerIcon = getResources().getDrawable(R.mipmap.aed);
        Marker marker1111 = new Marker( mMapView );
        marker1111.setPosition( new GeoPoint(32.023485,131.466268) );

        Double dis7 = distance(lat,lon,32.023485,131.466268);
        int f= (int) (dis7+0.5);
        marker1111.setTitle ( "AED\n"+"1階外廊下近く\n"+ "現在地からの直線距離は"+f +"m" );
        // marker1111.setSnippet( "1階外廊下近く"+ "現在地からの直線距離は"+f +"m");
        marker1111.setIcon(markerIcon);
        mMapView.getOverlays().add(marker1111);

        markerIcon = getResources().getDrawable(R.mipmap.aed);
        Marker marker11111 = new Marker( mMapView );
        marker11111.setPosition( new GeoPoint(32.023193,131.466308) );

        Double dis8 = distance(lat,lon,32.023193,131.466308);
        int g= (int) (dis8+0.5);
        marker11111.setTitle ( "AED\n"+"2階　階段上がったそば\n"+ "現在地からの直線距離は"+g +"m" );
        // marker11111.setSnippet( "1階外廊下近く"+ "現在地からの直線距離は"+g +"m");
        marker11111.setIcon(markerIcon);
        mMapView.getOverlays().add(marker11111);

    }




    //球面三角法
    public double  distance(double lat1, double lon1, double lat2, double lon2) {

        double radLat1 = Math.toRadians(lat1);
        double radLon1 = Math.toRadians(lon1);
        double radLat2 = Math.toRadians(lat2);
        double radLon2 = Math.toRadians(lon2);
        double r = 6378137.0; // 赤道半径
        double averageLat = (radLat1 - radLat2) / 2;
        double averageLon = (radLon1 - radLon2) / 2;
        return    2 * r * Math.asin(Math.sqrt(sin(averageLat) * sin(averageLat) + cos(radLat1) * cos(radLat2) * sin(averageLon) * sin(averageLon)));


    }


    @Override
    public void onLocationResult(LocationResult locationResult) {
        lat = locationResult.getLastLocation().getLatitude();
        lon = locationResult.getLastLocation().getLongitude();

        Marker marker = new Marker(mMapView);
        marker.setTitle("現在地");
        Drawable icon=getResources().getDrawable(R.mipmap.pin0620);
        GeoPoint Point = new GeoPoint(lat, lon);
        OverlayItem item = new OverlayItem("", "", Point);
        item.setMarker(icon);
        marker.setIcon(icon);
        marker.setPosition(Point);


        setupMarker();
        //ピンを表示する
        mMapView.getOverlays().add(marker);


        //地図を表示する
        IMapController mapController = mMapView.getController();
        mapController.setZoom(18);


        //佐土原　32.023163,131.466361
        Double dis = distance(32.023163,131.466361,lat,lon);

        //地図の表示位置
        if(dis>=300){//高校外なら 高校 300m以上
            GeoPoint centerPoint = new GeoPoint(32.023163,131.466361);
            mapController.setCenter(centerPoint);
        }else{//高校内なら　現在地
            GeoPoint centerPoint = new GeoPoint(lat,lon);
            mapController.setCenter(centerPoint);
        }



    }


}