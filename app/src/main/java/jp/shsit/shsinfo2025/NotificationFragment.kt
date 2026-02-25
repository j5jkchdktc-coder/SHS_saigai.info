package jp.shsit.shsinfo2025

import android.app.ProgressDialog
import android.content.Context
import android.graphics.Bitmap
import android.location.Geocoder
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import jp.shsit.shsinfo2025.ui.weather.WeatherFragment
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.Locale


class NotificationFragment : Fragment() {
    private var webView: WebView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 🔽 通知済みフラグをリセット
      //  val prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
      // prefs.edit()
       //     .putBoolean("thunder_notified", false)
        //    .putBoolean("heat_notified", false)
         //   .apply()
        //Log.i("NotificationFragment", "通知済みフラグをリセットしました")


        val view = inflater.inflate(R.layout.fragment_weather3, container, false)
        //webViewの設定
        webView = view.findViewById<View>(R.id.webView) as WebView

        //プレファランスによる値読み出し
       /* val urlNo = PreferenceManager.getDefaultSharedPreferences(
            activity
        ).getString("key3", "4520200")
        val areaCode2 = PreferenceManager.getDefaultSharedPreferences(
            activity
        ).getString("weather_area", "016010")*/
        val prefer = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val latitude = prefer.getFloat("lat", 0.0f).toDouble()
        val longitude = prefer.getFloat("lon", 0.0f).toDouble()


        // ジオコーダーで都道府県名を取得
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        Thread {
            try {
                val addresses = geocoder.getFromLocation(latitude, longitude, 1)
                if (!addresses.isNullOrEmpty()) {
                    val adminArea = addresses[0].adminArea  // 都道府県名
                    Log.d("DEBUG", "都道府県: $adminArea")

                    var code = getCodeFromCsv(requireContext(), adminArea)  // CSVからコード取得
                    if(addresses.equals("北海道")){
                        code = "012000"
                    }

                    if (code != null) {
                        val language = PreferenceManager.getDefaultSharedPreferences(
                            activity
                        ).getString("lang", "日本語")
                        val url = if (language == "English") {
                            "https://www.jma.go.jp/bosai/#area_type=offices&area_code=${code}&lang=en"
                        } else {
                            "https://www.jma.go.jp/bosai/#area_type=offices&area_code=${code}"
                        }
Log.i("test1",url)
                        activity?.runOnUiThread {
                            webView?.settings?.javaScriptEnabled = true
                            webView?.loadUrl(url)
                            Log.d("DEBUG", "読み込みURL: $url")
                        }
                    } else {
                        Log.e("DEBUG", "コードが見つかりません")
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()



        //javascript を処理するために以下のコードが必要でした
        val client: WebViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return false
            }
        }
        val settings = webView!!.settings
        settings.domStorageEnabled = true
        settings.javaScriptEnabled = true
        webView!!.webViewClient = client
        webView!!.settings.builtInZoomControls = true
        webView!!.webViewClient = ViewClient(this.context, "テータ取得中")

        val bt1 = view.findViewById<Button>(R.id.frw1Button)
/*
        val language = PreferenceManager.getDefaultSharedPreferences(
            activity
        ).getString("lang", "日本語")
        if (language == "English") {
            bt1.text = "Back"
            if (urlNo!!.length > 6) {
                webView!!.loadUrl("https://www.jma.go.jp/bosai/#lang=en&pattern=forecast&area_type=class20s&area_code=$urlNo")
            } else {
                webView!!.loadUrl("https://www.jma.go.jp/bosai/#lang=en&pattern=fore/multi/yoho/index.html?forecast=wcast&area_type=class20s&area_code=$urlNo")
            }
        }
        /*else if (language == "Vietnamese") {
            bt1.text = "Quay lại"
            webView!!.loadUrl("https://www.data.jma.go.jp/multi/yoho/yoho_detail.html?code=$areaCode2&lang=vn")
        } else if (language == "Chinese") {
            bt1.text = "返回"
            webView!!.loadUrl("https://www.data.jma.go.jp/multi/yoho/yoho_detail.html?code=$areaCode2&lang=cn_zs")
        }*/
        else {

            if (urlNo!!.length > 6) {
                webView!!.loadUrl("https://www.jma.go.jp/bosai/#pattern=forecast&area_type=class20s&area_code=$urlNo")
            } else {
                webView!!.loadUrl("https://www.jma.go.jp/bosai/#pattern=forecast&area_type=class20s&area_code=$urlNo")
            }
        }
*/

        //back button
        bt1.setOnClickListener {
            val manager = requireActivity().supportFragmentManager
            val transaction = manager.beginTransaction()
            transaction.replace(R.id.nav_host_fragment, WeatherFragment())
            // 戻るボタンで戻る必要がなければ addToBackStackは不要
            transaction.commit()
        }


        return view
    }






    fun getCodeFromCsv(context: Context, address: String): String? {
        val normalizedInput = normalizePrefName(address)

        try {
            val inputStream = context.assets.open("NotificationCode.csv")  // assetsから読み込み
            val reader = BufferedReader(InputStreamReader(inputStream))

            reader.useLines { lines ->
                lines.forEach { line ->
                    val parts = line.split(",")
                    if (parts.size >= 2) {
                        val name = normalizePrefName(parts[0])
                        if (normalizedInput.contains(name)) {
                            return parts[1]
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    fun normalizePrefName(name: String): String {
        return name
            .replace("都", "")
            .replace("道", "")
            .replace("府", "")
            .replace("県", "")
            .replace("地方", "")
            .replace(Regex("（.*）"), "")
            .trim()
    }

    inner class ViewClient(context: Context?, message: String?) :
        WebViewClient() {
        private val progressDialog = ProgressDialog(context)

        init {
            progressDialog.setMessage(message)
        }

        override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            progressDialog.show()
        }

        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)
            progressDialog.dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }


    companion object {
        var RSS_FEED_URL: String = ""
    }
}