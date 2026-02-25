package jp.shsit.shsinfo2025

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.io.StringReader
import java.net.HttpURLConnection
import java.net.URL
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*

class ThunderAlertChecker(private val context: Context) : LocationListener {

    private val TAG = "ThunderAlertChecker"
    private val geocoder = Geocoder(context, Locale.JAPAN)
    private val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    private var pref: String? = null

    fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // 権限がない場合の処理
            return
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, 0f, this)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun checkThunderAlert(lat: Double, lng: Double, callback: (AlertItem?) -> Unit) {
        Thread {
            // 既存の処理
            val pref = getPrefectureName(lat, lng)
            var alert: AlertItem? = null
            val url = URL("https://www.data.jma.go.jp/developer/xml/feed/extra_l.xml")
            val connection = url.openConnection() as HttpURLConnection
            try {
                connection.connectTimeout = 20000
                connection.readTimeout = 20000
                connection.requestMethod = "GET"
                connection.connect()
                val statusCode = connection.responseCode
                if (statusCode == HttpURLConnection.HTTP_OK && pref != null) {
                    val inputStream = connection.inputStream
                    alert = parseXML(inputStream, pref)
                }
            } catch (exception: Exception) {
                exception.printStackTrace()
            } finally {
                connection.disconnect()
            }
            callback(alert)
        }.start()
    }

    private fun getPrefectureName(latitude: Double, longitude: Double): String {
        return try {
            val addresses: MutableList<Address>? = geocoder.getFromLocation(latitude, longitude, 1)
            if (addresses!!.isNotEmpty()) {
                val address = addresses[0]
                address.adminArea.substring(0, 2)
            } else {
                ""
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error getting prefecture name", e)
            ""
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun parseXML(inputStream: InputStream, pref: String): AlertItem? {

        return try {
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            val responseBody = StringBuilder()
            bufferedReader.useLines { lines -> lines.forEach { responseBody.append(it) } }

            val factory = XmlPullParserFactory.newInstance()
            val xpp = factory.newPullParser()
            xpp.setInput(StringReader(responseBody.toString()))
            var eventType = xpp.next()
            var title = ""
            var url = ""
            var updated = ""
            var name = ""
            var content = ""
            val alertArray = ArrayList<AlertItem>()

            while (eventType != XmlPullParser.END_DOCUMENT) {
                when (eventType) {
                    XmlPullParser.START_TAG -> {
                        when (xpp.name) {
                            "title" -> title = xpp.nextText()
                            "id" -> url = xpp.nextText()
                            "updated" -> updated = xpp.nextText()
                            "name" -> name = xpp.nextText()
                            "content" -> content = xpp.nextText()
                        }
                    }
                    XmlPullParser.END_TAG -> if (xpp.name == "entry") {
                        val item = AlertItem(title, url, updated, name, content, pref)
                        alertArray.add(item)
                    }
                }
                eventType = xpp.next()
            }
            processAlerts(alertArray, pref)
        } catch (e: Exception) {
            Log.e("test", "Error parsing XML", e)
            null
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun processAlerts(alertArray: ArrayList<AlertItem>, pref: String): AlertItem? {
        for (i in alertArray) {
            val date = parseDateString(i.updated)
            val datenow = LocalDateTime.now()
            val sa = ChronoUnit.HOURS.between(date, datenow)

            Log.i("Thunder Alert's Data", "${i.name}, ${i.updated}, ${i.content}, ${i.title}")

            //24時間以内の雷注意報
            if (sa < 25) {
                val str = i.content
                var thunderTime = LocalDateTime.now().minusHours(24)
                var cancelTime = LocalDateTime.now().minusHours(24)
                if (str.contains(pref)) {
                    Log.i("test", str)
                    if (str.contains("雷") && str.contains("警報・注意報")) {
                        Log.i("test", i.content)
                        Log.i("test", i.updated)
                        thunderTime = parseDateString(i.updated)
                        if (cancelTime.isBefore(thunderTime)) {
                            Log.i("test", "現在時刻との差: ${i.name}")
                            return i;
                        }
                    }
                    else if (str.contains("解除")) {
                        Log.i(TAG, i.content)
                        Log.i(TAG, i.updated)
                        cancelTime = parseDateString(i.updated)
                        if (thunderTime.isBefore(cancelTime)) {
                            return null
                        }
                    }
                    else if (str.contains("雷") && str.contains("なくなりました") ){
                            cancelTime = parseDateString(i.updated)
                            if (thunderTime.isBefore(cancelTime)) {
                                return null
                            }
                    }

                }
            } else {
                Log.i(TAG, "24時間以内の雷注意報なし")
            }
        }
        return null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun parseDateString(dateStr: String): LocalDateTime {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
        return LocalDateTime.parse(dateStr, formatter)
    }

    override fun onLocationChanged(location: Location) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 新しいスレッドを作成して非同期に実行
            Thread {
                checkThunderAlert(location.latitude, location.longitude) { alertItem ->
                    if (alertItem != null) {

                    }
                }
            }.start()
        }
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

    override fun onProviderEnabled(provider: String) {}

    override fun onProviderDisabled(provider: String) {}
}

data class AlertItem(
    val title: String,
    val url: String,
    val updated: String,
    val name: String,
    val content: String,
    var pref:String
)
class NotificationDismissReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // 何もしない
    }
}