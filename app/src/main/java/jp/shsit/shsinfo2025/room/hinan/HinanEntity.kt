package jp.shsit.shsinfo2025.room.hinan

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "hinan_table")
data class HinanEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "cityCode") var cityCode: String,
    @ColumnInfo(name = "cityName") var cityName: String,
    @ColumnInfo(name = "cityNo") val cityNo: String,
    @ColumnInfo(name = "facilityName") var facilityName: String,
    @ColumnInfo(name = "facilityAddress") val facilityAddress: String,
    @ColumnInfo(name = "kouzui") var kouzui: String,
    @ColumnInfo(name = "dosya") val dosya: String,
    @ColumnInfo(name = "takasio") val takasio: String,
    @ColumnInfo(name = "jishin") var jishin: String,
    @ColumnInfo(name = "tunami") var tunami: String,
    @ColumnInfo(name = "kaji") val kaji: String,
    @ColumnInfo(name = "hanran") val hanran: String,
    @ColumnInfo(name = "kazan") val kazan: String,
    @ColumnInfo(name = "hinanjyo") var hinanjyo: String,
    @ColumnInfo(name = "lat") var lat: Double,
    @ColumnInfo(name = "lon") var lon: Double
)