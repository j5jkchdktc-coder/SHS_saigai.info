package jp.shsit.shsinfo2025.hinan

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.location.Geocoder
import android.os.Bundle
import android.os.Environment
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.preference.PreferenceManager
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.osmdroid.api.IGeoPoint

import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.MapTileProviderBasic
import org.osmdroid.tileprovider.tilesource.ITileSource
import org.osmdroid.tileprovider.tilesource.XYTileSource
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.ItemizedIconOverlay
import org.osmdroid.views.overlay.ItemizedIconOverlay.OnItemGestureListener
import org.osmdroid.views.overlay.ItemizedOverlay
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.MinimapOverlay
import org.osmdroid.views.overlay.OverlayItem
import org.osmdroid.views.overlay.TilesOverlay
import jp.shsit.shsinfo2025.AR.AR_Fragment
import jp.shsit.shsinfo2025.MainActivity
import jp.shsit.shsinfo2025.R
import jp.shsit.shsinfo2025.hazardmap.gpsFragmrnt
import jp.shsit.shsinfo2025.room.hinan.HinanViewModel
import jp.shsit.shsinfo2025.ui.sonaeru.screenshot.ScreenShotFragment
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.util.Collections
import java.util.Locale

class MapsFragmenrt2 : Fragment(), LatLongCatch.OnLocationResultListener {
    var locationManager: LatLongCatch? = null
    var lat = 0.0
    var lng = 0.0
    private var mMapView: MapView? = null
    private var mMyLocationOverlay: ItemizedOverlay<OverlayItem>? = null
    var geocoder: Geocoder? = null

    //    Intent intent = getActivity().getIntent();
    var yAdapter: ListViewAdapter? = null
    var map_arrays: ArrayList<ListData>? = null
    var map_arrays2: ArrayList<ListData>? = null
    var city_code: String? = null
    var city_code_genzai: String? = null
    lateinit var listData: Array<ListData>
    lateinit var listData2: Array<ListData>
    lateinit var Top5list: Array<ListData2?>
    lateinit var cityData: Array<cityData>
    private var hinan_type = 0
    var pref: String? = null
    var rg: RadioGroup? = null
    var rb1: RadioButton? = null
    var rb2: RadioButton? = null
    var rb3: RadioButton? = null
    var rb4: RadioButton? = null
    var ed: EditText? = null
    var value = "現在地"

    //ARボタン
    var ar_btn: FloatingActionButton? = null
    private var pictureFlag = false

    /**
     * onCreate
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Configuration.getInstance().load(
            context,
            PreferenceManager.getDefaultSharedPreferences(requireContext())
        )
       // Configuration.getInstance().userAgentValue = BuildConfig.APPLICATION_ID
        locationManager = LatLongCatch(context, this)
        locationManager!!.startLocationUpdates()

        //緯度、経度から住所を検索するのに必要
        geocoder = Geocoder(requireContext(), Locale.JAPAN)
        // Itemオブジェクトを保持するためのリストを生成し、アダプタに追加する
        map_arrays = ArrayList()
        yAdapter = ListViewAdapter(context, 0, map_arrays)


        //初期値
        hinan_type = 6 //地震
    } //  onCreate

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.hinan_activity_maps2, container, false)
        mMapView = view.findViewById<View>(R.id.mapView) as MapView
        mMapView!!.setBuiltInZoomControls(true)
        mMapView!!.setMultiTouchControls(true)


        //ラジオボタンの設定
        rg = view.findViewById(R.id.radioGroup)
        rb1 = view.findViewById(R.id.radioButton1)
        rb2 = view.findViewById(R.id.radioButton2)
        rb3 = view.findViewById(R.id.radioButton3)
        rb4 = view.findViewById(R.id.radioButton4)
        /*************言語選択 */
        val language = PreferenceManager.getDefaultSharedPreferences(
            requireContext()
        ).getString("lang", "日本語")
        val main = MainActivity()
        val t1 = main.LangReader("hinan", 1, language)
        rb1!!.setText(t1)
        val t2 = main.LangReader("hinan", 2, language)
        rb2!!.setText(t2)
        val t3 = main.LangReader("hinan", 3, language)
        rb3!!.setText(t3)
        val t4 = main.LangReader("hinan", 4, language)
        rb4!!.setText(t4)
        //ピン長押し
        val tv1 = view.findViewById<TextView>(R.id.textView1)
        tv1.text = main.LangReader("hinan", 0, language)
        /** */
        rg!!.check(rb2!!.getId())

        // Itemオブジェクトを保持するためのリストを生成し、アダプタに追加する
        map_arrays = ArrayList()
        yAdapter = ListViewAdapter(context, 0, map_arrays)

        //現在地
        locationManager!!.startLocationUpdates()
        val mapController = mMapView!!.controller
        mapController.setZoom(MAP_ZOOM)
        val centerPoint = GeoPoint(lat, lng)
        mapController.setCenter(centerPoint)
        value = "現在地"
        setupMarker_current(value)

        val bt1 = view.findViewById<Button>(R.id.hinan_btn1)
        bt1.text = main.LangReader("hinan", 5, language)
        bt1.setOnClickListener(View.OnClickListener {
            val fragmentManager = activity?.supportFragmentManager
            fragmentManager?.popBackStack("hinan",1)
            onPause()

        })
        ed = view.findViewById(R.id.editText1)
        ed!!.setHint(main.LangReader("hinan", 10, language))
        val bt2 = view.findViewById<Button>(R.id.hinan_btn2)
        bt2.text = main.LangReader("hinan", 6, language)
        bt2.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                // Itemオブジェクトを保持するためのリストを生成し、アダプタに追加する
                map_arrays = ArrayList()
                yAdapter = ListViewAdapter(context, 0, map_arrays)
                locationManager!!.startLocationUpdates()
                val mapController = mMapView!!.controller
                mapController.setZoom(MAP_ZOOM)
                val centerPoint = GeoPoint(lat, lng)
                mapController.setCenter(centerPoint)
                value = "現在地"
                setupMarker_current(value)
                hinanmap()
            }
        })

        //スクリーンショット
        val bt3 = view.findViewById<Button>(R.id.button_a)
        bt3.text = main.LangReader("hinan", 9, language)
        //Bundleによる値渡し
        /*
        try{
            fileName = getArguments().getString("name");
            Log.i("test",fileName);
            //表示
            bt3.setVisibility(View.VISIBLE);
        }catch (NullPointerException e){
            //非表示 スペースを詰める
            bt3.setVisibility(View.GONE);
        }*/
        //  String finalFileName = fileName;
        bt3.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                val fileName = ScreenShotIntent()
                if (fileName != "full") {
                    saveCapture(mMapView, fileName)

                }
            }
        })
        val bt4 = view.findViewById<ImageButton>(R.id.searchbutton)
        bt4.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                // Itemオブジェクトを保持するためのリストを生成し、アダプタに追加する
                map_arrays = ArrayList()
                yAdapter = ListViewAdapter(context, 0, map_arrays)
                value = ed!!.getText().toString()
                val a = gpsFragmrnt.getLocationFromPlaceName(context, value)
                lat = a[0]
                lng = a[1]
                val mapController = mMapView!!.controller
                mapController.setZoom(MAP_ZOOM)
                val centerPoint = GeoPoint(lat, lng)
                mapController.setCenter(centerPoint)
                hinanmap2(value)
                //キーボードを隠す
                // ソフトキーボードを非表示にする
                val imm =
                    activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)

                //エディタの文字消去
                //    ed.getEditableText().clear();
                //位置情報取得ストップ
                locationManager!!.stopLocationUpdates()
            }
        })

        //ARボタン
        ar_btn = view.findViewById(R.id.fab)
        ar_btn!!.setEnabled(false)

        //ARボタン
        ar_btn!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                top5()
                val manager = activity!!.supportFragmentManager
                val transaction = manager.beginTransaction()
                val fragment = AR_Fragment()
                val bundle = Bundle()
                //ArrayListを書き込む
                // bundle.putString("INT_KEY", "hajime");
                bundle.putSerializable("INT_KEY", Top5list)
                bundle.putDouble("lat", lat)
                bundle.putDouble("lng", lng)

                //bundle.putSerializable("INT_KEY", Top5list);
                //値を書き込む
                fragment.arguments = bundle

                // もどるボタンで戻ってこれるように
                transaction.addToBackStack(null)
                transaction.replace(R.id.nav_host_fragment, fragment)
                transaction.commit()
            }
        })
        val saveBtn = view.findViewById<Button>(R.id.save_btn)
        saveBtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                val manager = activity!!.supportFragmentManager
                val transaction = manager.beginTransaction()
                /* もどるボタンで戻ってこれるように */transaction.addToBackStack(null)
                transaction.replace(R.id.nav_host_fragment,  ScreenShotFragment());
                //transaction.replace(R.id.nav_host_fragment, ReadHinan())
                transaction.commit()
            }
        })


        //ラジボタン変更
        val rd11 = rb1!!.getId()
        val rd22 = rb2!!.getId()
        val rd33 = rb3!!.getId()
        // ラジオグループのチェック状態変更イベントを登録
        rg!!.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener {
            // チェック状態変更時に呼び出されるメソッド
            override fun onCheckedChanged(group: RadioGroup, checkedId: Int) {
                // チェック状態時の処理を記述
                // チェックされたラジオボタンオブジェクトを取得
                val radioButton = view.findViewById<RadioButton>(checkedId)
                // ラジオボタンのテキストを取得
                val text = radioButton.id
                if (rd33 == text) {
                    hinan_type = 7 //津波
                    val mapController = mMapView!!.controller
                    mapController.setZoom(MAP_ZOOM)
                    val centerPoint = GeoPoint(lat, lng)
                    mapController.setCenter(centerPoint)
                    setupMarker()
                    setupMarker_current(value)
                    // hinanmap2();
                } else if (rd22 == text) {
                    hinan_type = 6 //地震
                    val mapController = mMapView!!.controller
                    mapController.setZoom(MAP_ZOOM)
                    val centerPoint = GeoPoint(lat, lng)
                    mapController.setCenter(centerPoint)
                    setupMarker()
                    setupMarker_current(value)
                    //   hinanmap2();
                } else if (rd11 == text) {
                    hinan_type = 3 //洪水
                    val mapController = mMapView!!.controller
                    mapController.setZoom(MAP_ZOOM)
                    val centerPoint = GeoPoint(lat, lng)
                    mapController.setCenter(centerPoint)
                    setupMarker()
                    setupMarker_current(value)
                    //   hinanmap2();
                } else {
                    hinan_type = 11 //避難所
                    val mapController = mMapView!!.controller
                    mapController.setZoom(MAP_ZOOM)
                    val centerPoint = GeoPoint(lat, lng)
                    mapController.setCenter(centerPoint)
                    setupMarker()
                    setupMarker_current(value)
                }
            }
        })



//        setupMarker();
        return view
    }

    fun hinanmap() {

        runBlocking {
            val job = launch() {
                val taskA = async(coroutineContext ) { // @
                    Log.v("item", "start!!!A"+lat+","+lng)

                    try {
                        pref = pointaddress(lat, lng, context)
                        Log.v("item", pref!!)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                    return@async pref
                }
                val valueA = taskA.await()

                //緯度経度から住所を検索し、ファイル名を検索
                val taskB = async(coroutineContext ) { // @
                    Log.v("item", "start!!!B"+lat+","+lng)
                    try {
                        pref = pointaddress(lat, lng, context)
                        Log.v("item", pref!!)
                        //市町村名から市町村コードに変換
                        val util = MarkerUtil(context)
                        city_code = util.readCSV2(pref)
                        Log.v("item", "$pref,$city_code")
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                    return@async city_code
                }
                val valueB = taskB.await()

                val taskC = async(coroutineContext ) { // @
                    Log.v("item", "start!!!C")

                    map_arrays2 = ArrayList<ListData>()

                    val viewModel: HinanViewModel by viewModels()
                    viewModel.item_city(city_code.toString()).observe(requireActivity()) { item ->
                        for (i in item) {
                            var item1: ListData? = ListData()
                            item1!!.name = i.facilityName
                            item1.address = i.facilityAddress
                            item1.jishin = i.jishin
                            item1.kouzui = i.kouzui
                            item1.tsunami = i.tunami
                            item1.jyou = i.hinanjyo
                            item1.lat = i.lat.toString()
                            item1.lon = i.lon.toString()
                            map_arrays2?.add(item1)
                            Log.i("test", i.facilityName.toString() + "です")
                        }
                        Log.i("test", item.size.toString() + "です")
                        setupMarker()
                        setupMarker_current("現在地")
                        ar_btn!!.setEnabled(true)
                    }
                    return@async map_arrays2
                }
                val valueC = taskC.await();

                val taskD = async(coroutineContext ) {
                    Log.v("item", "start!!!D")
                    //  delay(2000)

                    //return@async sb.toString()

                }
                val valueD = taskD.await();

                val taskE = async(coroutineContext ) { // @
                    Log.v("item", "start!!!E")
                    val viewModel: HinanViewModel by viewModels()
                    viewModel.item_city(city_code.toString()).observe(requireActivity()) { item ->
                        if(item.size==0){
                            val text = TextView(activity?.applicationContext)
                            //Toastに表示する文字
                            text.text = "避難場所データがありません"
                            //フォントの種類
                            text.typeface = Typeface.SANS_SERIF
                            //フォントの大きさ
                            text.textSize = 30f
                            //フォントの色
                            text.setTextColor(Color.RED)
                            //文字の背景色(ARGB)
                            text.setBackgroundColor(-0x77232324)

                            //Toastの表示
                            val toast = Toast(activity?.applicationContext)
                            toast.view = text
                            toast.duration = Toast.LENGTH_LONG
                            toast.setGravity(Gravity.CENTER, 0, 0)
                            toast.show()
                        }
                    }
                }
                val valueE = taskE.await();


            }

        }

    }

    //.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    //緯度、経度をセット、避難所コードを取得
    fun hinanmap2(name: String) {
        runBlocking {
            val job = launch() {
                val taskA = async(coroutineContext) {
                    try {
                        pref = pointaddress(lat, lng, context)
                        //市町村名から市町村コードに変換
                        Log.v("item", pref + "hinanmap2")
                        val util = MarkerUtil(context)
                        city_code = util.readCSV2(pref)
                        Log.v("item", city_code + "hinanmap2")
                        if (city_code!!.length == 4) {
                            city_code = "0$city_code"
                        }
                        Log.v("item", "code$city_code")
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
                val taskB = async(coroutineContext) {
                    Log.v("item", "start!!!B")

                    map_arrays2 = ArrayList<ListData>()

                    val viewModel: HinanViewModel by viewModels()
                    viewModel.item_city(city_code.toString()).observe(requireActivity()) { item ->
                        for (i in item) {
                            var item1: ListData? = ListData()
                            item1!!.name = i.facilityName
                            item1.address = i.facilityAddress
                            item1.jishin = i.jishin
                            item1.kouzui = i.kouzui
                            item1.tsunami = i.tunami
                            item1.jyou = i.hinanjyo
                            item1.lat = i.lat.toString()
                            item1.lon = i.lon.toString()
                            map_arrays2?.add(item1)
                            Log.i("test", i.facilityName.toString() + "です")
                        }
                        Log.i("test", item.size.toString() + "です")
                        setupMarker()
                        setupMarker_current("現在地")
                    }
                }
                val taskC = async(coroutineContext) {
                    val viewModel: HinanViewModel by viewModels()
                    viewModel.item_city(city_code.toString()).observe(requireActivity()) { item ->
                        if(item.size==0){
                            val text = TextView(activity?.applicationContext)
                            //Toastに表示する文字
                            text.text = "避難場所データがありません"
                            //フォントの種類
                            text.typeface = Typeface.SANS_SERIF
                            //フォントの大きさ
                            text.textSize = 30f
                            //フォントの色
                            text.setTextColor(Color.RED)
                            //文字の背景色(ARGB)
                            text.setBackgroundColor(-0x77232324)

                            //Toastの表示
                            val toast = Toast(activity?.applicationContext)
                            toast.view = text
                            toast.duration = Toast.LENGTH_LONG
                            toast.setGravity(Gravity.CENTER, 0, 0)
                            toast.show()
                        }
                    }
                }
            }
        }

    }

    //緯度・軽度から住所
    @Throws(IOException::class)
    fun pointaddress(latitude: Double, longitude: Double, context: Context?): String {
        var address1 = ""

        //geocoedrの実体化
        Log.d("map", "Start point2adress")
        //Geocoder geocoder = new Geocoder(getContext().getApplicationContext(), Locale.JAPAN);
        val list_address = geocoder!!.getFromLocation(latitude, longitude, 1) //引数末尾は返す検索結果数

        //ジオコーディングに成功したらStringへ
        if (!list_address!!.isEmpty()) {
            val address = list_address[0]
            val strbuf = StringBuffer()

            //adressをStringへ
            var buf: String
            var i = 0
            /*            while ((address.getAddressLine(i).also { buf = it }) != null) {
                            Log.d("map", "loop no.$i")
                            strbuf.append("address.getAddressLine($i):$buf\n")
                            i++
                        }
              */
            //addressから県名だけを抜き出す
            val pref = address.adminArea
            val city = address.locality
            //String city2 = address.getAddressLine(0);
            address1 = pref + city
        } else {
            Log.d("map", "住所取得に失敗")
        }
        return address1
    }

    /**
     * onResume
     */
    override fun onResume() {
        super.onResume()
        Configuration.getInstance().load(
            context,
            PreferenceManager.getDefaultSharedPreferences(requireContext())
        )
        if (mMapView != null) {
            mMapView!!.onResume()
        }
    } // onResume

    /**
     * onPause
     */
    override fun onPause() {
        super.onPause()
        Configuration.getInstance().save(
            context,
            PreferenceManager.getDefaultSharedPreferences(requireContext())
        )
        if (mMapView != null) {
            mMapView!!.onPause()
        }
    } // onPause

    /**
     * setupMarker
     */
    private fun setupMarker() {
        /* Itemized Overlay */
        /************ 地理院 標準地図 */
        val TILE_SERVER_1 = "https://cyberjapandata.gsi.go.jp/xyz/std/"
        // Add tiles layer with Custom Tile Source
        val tileProvider = MapTileProviderBasic(context)
        var tileSource: ITileSource? = null
        tileSource = XYTileSource("GSI", 12, 18, 256, ".png", arrayOf(TILE_SERVER_1))
        tileProvider.tileSource = tileSource
        val tilesOverlay = TilesOverlay(tileProvider, this.context)
        tilesOverlay.loadingBackgroundColor = Color.TRANSPARENT
        mMapView!!.overlays.clear()
        mMapView!!.overlays.add(tilesOverlay)
        mMapView!!.invalidate()
        /** */

//　基本
        mMapView!!.setMultiTouchControls(true)
        for (item1 in map_arrays2!!) {

            try {
                val marker = Marker(mMapView)
                val Point = GeoPoint(
                    item1.lat!!.toDouble(),
                    item1.lon!!.toDouble()
                )
                marker.title = item1.name
                val item = OverlayItem("", "", Point)

                // アイコンを変更する
                var icon: Drawable?
                if (hinan_type == 7) {
                    icon = resources.getDrawable(R.mipmap.pin0620)
                } //地震　緑
                else if (hinan_type == 6) {
                    icon = resources.getDrawable(R.mipmap.pin_gre)
                } //洪水　青
                else if (hinan_type == 3) {
                    icon = resources.getDrawable(R.mipmap.pin_ao)
                } //避難所　紫
                else {
                    icon = resources.getDrawable(R.mipmap.pin_pur)
                }
                item.setMarker(icon)
                marker.icon = icon
                marker.position = Point

                // マーカーをまとめるための MarkerCluster の設定
                //ここ参考にしたが、できないhttps://cpoint-lab.co.jp/article/201912/13219/
                //val clusterer = RadiusMarkerClusterer(context)
               // clusterer.setRadius(100)
               // clusterer.add(marker)

                //ピンを表示するか？
                val kouzui = item1.kouzui!!.toInt()
                val jishin = item1.jishin!!.toInt()
                val tsunami = item1.tsunami!!.toInt()
                val hinan = item1.jyou!!.toInt()
                if (hinan_type == 7 && tsunami == 1) {
                    mMapView!!.overlays
                        .add(marker)
                }
                if (hinan_type == 6 && jishin == 1) {
                    mMapView!!.overlays
                        .add(marker)
                }
                if (hinan_type == 3 && kouzui == 1) {
                    mMapView!!.overlays
                        .add(marker)
                }
                if (hinan_type == 11 && hinan == 1) {
                    mMapView!!.overlays
                        .add(marker)
                }
            } catch (e: Exception) {
            }
        }
        val util = MarkerUtil(context)
        val items = util.getMarkers(map_arrays2, hinan_type)
        run {
            mMyLocationOverlay = ItemizedIconOverlay(items,
                object : OnItemGestureListener<OverlayItem> {
                    override fun onItemSingleTapUp(index: Int, item: OverlayItem): Boolean {
                        //  toast_long(item.getTitle());

                        //   log_d(item.getHeight()+","+item.getWidth());
                        return false // We 'handled' this event.
                    }

                    override fun onItemLongPress(index: Int, item: OverlayItem): Boolean {
                        // ルート検索
                        val latLngStart: LatLng = LatLng(lat, lng)
                        val name: String = item.getTitle()
                        val lat_lng: IGeoPoint = item.getPoint()
                        val lat_obj: Double = lat_lng.getLatitude()
                        val lng_obj: Double = lat_lng.getLongitude()
                        val latLngGole: LatLng = LatLng(lat_obj, lng_obj)

                        //ルート検索　（スタートとゴールが違う場合）
                        if (!(latLngStart == latLngGole)) {
                            drawRoute2(latLngStart, latLngGole, name)
                        }
                        return true
                    }
                }, getContext()
            )
            this.mMapView!!.getOverlays().add(this.mMyLocationOverlay)
        }
    }

    private fun setupMarker_current(name: String) {
        val util = MarkerUtil(context)
        val items = util.getMarker_current(name, lat, lng)

        /* OnTapListener for the Markers, shows a simple Toast. */run {
            mMyLocationOverlay = ItemizedIconOverlay(items,
                object : OnItemGestureListener<OverlayItem> {
                    override fun onItemSingleTapUp(index: Int, item: OverlayItem): Boolean {
                        toast_long(item.getTitle())
                        log_d(item.getHeight().toString() + "," + item.getWidth())
                        return true // We 'handled' this event.
                    }

                    override fun onItemLongPress(index: Int, item: OverlayItem): Boolean {
                        //      toast_long(item.getTitle());
                        return false
                    }
                }, getContext()
            )
            this.mMapView!!.getOverlays().add(this.mMyLocationOverlay)
        }

        /* MiniMap */
        val miniMapOverlay = MinimapOverlay(
            context,
            mMapView!!.tileRequestCompleteHandler
        )
        mMapView!!.overlays.add(miniMapOverlay)
    }

    private fun drawRoute2(a: LatLng, g: LatLng, name: String) {
        var url: String = "https://maps.google.com/maps?dirflg=w"
        url += "&saddr=" + a.latitude + "," + a.longitude + "(現在地)"
        url += "&daddr=" + g.latitude + "," + g.longitude + "(" + name + ")"

        /*
        String fileName = "noimage";
        try{
            fileName = getArguments().getString("name");
            Log.i("test",fileName);
            //表示
        }catch (NullPointerException e){
            //非表示 スペースを詰める
        }
        ?

 */
        val fileName = ScreenShotIntent()

        //プレファランス保存
        val prefer = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val editor = prefer.edit()
        editor.putString("url", url)
        editor.putString("name", fileName)
        editor.commit()
        val intent = Intent(activity, MapActivity::class.java)
        startActivity(intent)
    }

    fun top5() {
        Top5list = arrayOfNulls(5)
        // 各要素ごとにインスタンス化
        for (i in 0..4) {
            Top5list[i] = ListData2()
        }
        val list: MutableList<ListData2> = ArrayList()
        var buff = ListData2()
        for (i in 1 until (map_arrays2!!.size - 2)) {
            buff.setId(map_arrays2!![i].id)
            buff.setName(map_arrays2!![i].name)
            buff.address = map_arrays2!!.get(i).address
            buff.setLat(map_arrays2!![i].lat)
            buff.setLon(map_arrays2!![i].lon)
            buff.setKouzui(map_arrays2!![i].kouzui)
            buff.setJishin(map_arrays2!![i].jishin)
            buff.setTsunami(map_arrays2!![i].tsunami)
            buff.setJyou(map_arrays2!![i].jyou)
            buff.setDistance(
                distance(
                    lat,
                    lng,
                    map_arrays2!![i].lat!!.toDouble(),
                    map_arrays2!![i].lon!!.toDouble()
                )
            )
            buff.setBearing(
                bearing(
                    lat,
                    lng,
                    map_arrays2!![i].lat!!.toDouble(),
                    map_arrays2!![i].lon!!.toDouble()
                )
            )
            list.add(buff)
            Log.i(
                "map",
                map_arrays2!![i].name + "です" + map_arrays2!![i].lat + "," + map_arrays2!!.size
            )
            //buffの初期化
            buff = ListData2()
        }
        //ArrayListを並び替え HinanData2クラスの距離でソート
        //自作オブジェクトクラスを格納するListの要素をソートする　を参考にした
        //https://pointsandlines.jp/server-side/java/collection-list-sort
        Collections.sort(list, object : Comparator<ListData2> {
            override fun compare(o1: ListData2, o2: ListData2): Int {
                return java.lang.Double.compare(o1.getDistance(), o2.getDistance())
            }
        })
        /** */
        //上位５件をAR関係変数に代入
        for (i in 0..4) {
            Top5list[i]!!.setId(list[i].getId())
            Top5list[i]!!.setName(list[i].getName())
            Top5list.get(i)!!.address = list.get(i).address
            Top5list[i]!!.setLat(list[i].getLat())
            Top5list[i]!!.setLon(list[i].getLon())
            Top5list[i]!!.setKouzui(list[i].getKouzui())
            Top5list[i]!!.setJishin(list[i].getJishin())
            Top5list[i]!!.setTsunami(list[i].getTsunami())
            Top5list[i]!!.setJyou(list[i].getJyou())
            Top5list[i]!!.setDistance(list[i].getDistance())
            Top5list[i]!!.setBearing(list[i].getBearing())
            Log.i("map", Top5list[i]!!.getName() + "です")
        }
    }

    //球面三角法
    fun distance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val radLat1 = Math.toRadians(lat1)
        val radLon1 = Math.toRadians(lon1)
        val radLat2 = Math.toRadians(lat2)
        val radLon2 = Math.toRadians(lon2)
        val r = 6378137.0 // equatorial radius
        val averageLat = (radLat1 - radLat2) / 2
        val averageLon = (radLon1 - radLon2) / 2
        return 2 * r * Math.asin(
            Math.sqrt(
                Math.sin(averageLat) * Math.sin(averageLat) + Math.cos(
                    radLat1
                ) * Math.cos(radLat2) * Math.sin(averageLon) * Math.sin(averageLon)
            )
        )
    }

    //方角を計算　現在地->lat1,lon1 目的地->lat2,lon2
    fun bearing(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val radLat1 = Math.toRadians(lat1)
        val radLat2 = Math.toRadians(lat2)
        val diffLon = Math.toRadians(lon2 - lon1)
        val x =
            Math.cos(radLat1) * Math.sin(radLat2) - Math.sin(radLat1) * Math.cos(radLat2) * Math.cos(
                diffLon
            )
        val y = Math.cos(radLat2) * Math.sin(diffLon)
        return (Math.toDegrees(Math.atan2(y, x)) + 360) % 360
    }

    private fun toast_long(msg: String) {
        val t = ToastMaster.makeText(this.activity, msg, Toast.LENGTH_LONG)
        val v = t.view
        v!!.setBackgroundColor(Color.YELLOW)
        if (v is ViewGroup) {
            val g = v
            for (i in 0 until g.childCount) {
                val c = g.getChildAt(i)
                if (c is TextView) {
                    c.setTextColor(Color.BLACK)
                }
            }
        }
        t.show()
    } // toast_long

    /**
     * write into logcat
     */
    private fun log_d(msg: String) {
        if (D) Log.d(TAG, TAG_SUB + " " + msg)
    } // log_d

    override fun onLocationResult(locationResult: LocationResult) {
        lat = locationResult.lastLocation!!.latitude
        lng = locationResult.lastLocation!!.longitude

        if (lat != 0.0) {
            hinanmap()
            Log.i("test",lat.toString())
        }
        //プレファランス保存
        /*        SharedPreferences prefer = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
        SharedPreferences.Editor editor = prefer.edit();
        Float lat1 = (float)lat;
        Float lng1 = (float)lng;
        editor.putFloat("lat",lat1);
        editor.putFloat("lng",lng1);
        editor.commit();*/
        val mapController = mMapView!!.controller
        mapController.setZoom(MAP_ZOOM)
        val centerPoint = GeoPoint(lat, lng)
        mapController.setCenter(centerPoint)
        setupMarker_current(value)
    }

    //スクリーンショット用
    fun saveCapture(view: View?, name: String) {
        val builder: AlertDialog.Builder
        builder = AlertDialog.Builder(requireContext())
        // タイトル
        builder.setTitle("保存します")
        // ポジティブボタン
        builder.setPositiveButton("OK", object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface, id: Int) {
                // キャプチャを撮る
                val capture = getViewCapture(view)
                val fos: FileOutputStream? = null
                // 保存先のフォルダー
                //   val cFolder = Environment.getExternalStorageDirectory().path + "/DCIM/capture_app/"
                //  val file1 = File(cFolder, name)

                //val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                val path = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                if (!path!!.exists()) {
                    /* 親ディレクトリが存在しない場合は親ディレクトリも作成する。 */
                    val result = path.mkdirs()
                }
                val file1 = File(path, name)
                try {
                    //この３行うまく行った
                    view?.isDrawingCacheEnabled = true
                    view?.drawingCacheBackgroundColor = Color.TRANSPARENT
                    val saveBitmap = Bitmap.createBitmap(view!!.drawingCache)  // Bitmap生成

                    var outStream = FileOutputStream(file1,false)
                    // 画像のフォーマットと画質と出力先を指定して保存
                    saveBitmap?.compress(Bitmap.CompressFormat.JPEG, 100, outStream)
                    Log.i("test", "成功")
                    outStream.flush()
                    outStream.close()
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                    Log.i("test", "失敗 ")
                }
                //ファイルのフラグを更新
                val prefer = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
                with(prefer.edit()){
                    if ((name == "CameraIntent1.jpg")) {
                        putBoolean("file1", true)
                        apply()
                    } else if ((name == "CameraIntent2.jpg")) {
                        putBoolean("file2", true)
                        apply()
                    } else if ((name == "CameraIntent3.jpg")) {
                        putBoolean("file3", true)
                        apply()
                    } else if ((name == "CameraIntent4.jpg")) {
                        putBoolean("file4", true)
                        apply()
                    }
                }


            }
        })
        // ネガティブボタン
        builder.setNegativeButton("キャンセル", object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface, id: Int) {
                // do something
            }
        })
        builder.create()
        builder.show()
    }

    fun getViewCapture(view: View?): Bitmap? {
        view?.isDrawingCacheEnabled = true
        Log.i("test", "1")
        // Viewのキャプチャを取得
        val cache = view?.drawingCache
        if (cache == null) {
            Log.i("test", "2")
            return null
        }
        val screenShot = Bitmap.createBitmap(cache)
        view.isDrawingCacheEnabled = false
        return screenShot
    }

    private fun ScreenShotIntent(): String {
        val context = context
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
        // 保存先のフォルダー
        val cFolder = context!!.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        Log.d("log", "path: " + cFolder.toString())
        val fileflag1 =
            sharedPref?.getBoolean("file1", false)
        val fileflag2 =
            sharedPref?.getBoolean("file2", false)
        val fileflag3 =
            sharedPref?.getBoolean("file3", false)
        val fileflag4 =
            sharedPref?.getBoolean("file4", false)
        var fileN = "CameraIntent1.jpg"
        pictureFlag = false
        if (fileflag1 == false) {
            fileN = "CameraIntent1.jpg"
        } else if (fileflag2 == false) {
            fileN = "CameraIntent2.jpg"
        } else if (fileflag3 == false) {
            fileN = "CameraIntent3.jpg"
        } else if (fileflag4 == false) {
            fileN = "CameraIntent4.jpg"
        } else {
            pictureFlag = true
        }
        Log.i("test",fileN+","+pictureFlag)
        if (pictureFlag == false) {
            // ファイル名
            return fileN

            //  Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
            // startActivityForResult(intent, RESULT_CAMERA);
        } else {
            /*************言語選択 */
            val language = PreferenceManager.getDefaultSharedPreferences(
                requireContext()
            ).getString("lang", "日本語")
            val main = MainActivity()
            val s1 = main.LangReader("hinanSave", 5, language)
            val s2 = main.LangReader("hinanSave", 6, language)
            val s3 = main.LangReader("hinanSave", 7, language)
            val sb = SpannableStringBuilder()
            sb.append(s1)
            sb.append("\n")
            sb.append(s2)
            sb.append("\n")
            sb.append(s3)
            Toast.makeText(
                getContext(),
                sb,
                Toast.LENGTH_LONG
            ).show()
            return "full"
        }
    }

    companion object {
        // debug
        private val D = true
        private val TAG = "OSM"
        private val TAG_SUB = "MainActivity"

        // Yokohama
        private val MAP_LAT = 35.4472391
        private val MAP_LON = 139.6414945
        private val MAP_ZOOM = 16.0
    }
}