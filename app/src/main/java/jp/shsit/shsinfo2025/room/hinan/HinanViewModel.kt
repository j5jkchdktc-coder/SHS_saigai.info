package jp.shsit.shsinfo2025.room.hinan

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

 class HinanViewModel (application: Application) : AndroidViewModel(application) {

    private val dao: HinanDao

    init {
        val db = SHS_Database.buildDatabase(application)
        dao = db.HinanDao()
    }
    //全件取得
    var items = dao.getAll()

    //市町村取得
    fun item_city(code:String) :LiveData<List<HinanEntity>>{
        return dao.getcity(code)
    }

    fun insert(cityCode: String, cityName: String, cityNo:String, facilityName:String,
               facilityAddress:String,kouzui:String,dosya:String,takasio:String,
               jishin:String,tunami:String,kaji:String,hanran:String,kazan:String,
               hinanjyo:String,lat:Double,lon:Double) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.insert(
                HinanEntity(
                    id = 0,
                    cityCode = cityCode,
                    cityName = cityName,
                    cityNo = cityNo,
                    facilityName = facilityName,
                    facilityAddress=facilityAddress,
                    kouzui=kouzui,
                    dosya=dosya,
                    takasio=takasio,
                    jishin=jishin,
                    tunami=tunami,
                    kaji =kaji,
                    hanran=hanran,
                    kazan=kazan,
                    hinanjyo=hinanjyo,
                    lat=lat,
                    lon=lon
                )
            )
        }
    }

    fun deleteAll() {
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteAll()
        }
    }



    var datacount = dao.count()
   // var groupDate = dao.groupDate()

    fun del(id:Int) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.del(id)
        }
    }



}