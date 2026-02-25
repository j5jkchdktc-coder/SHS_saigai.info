package jp.shsit.shsinfo2025.room.hinan

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface HinanDao {
    @Query("SELECT * FROM hinan_table")
    fun getAll(): LiveData<List<HinanEntity>>

    @Insert
    fun insert(vararg users: HinanEntity)

    @Query("DELETE FROM hinan_table")
    fun deleteAll()
    //件数
    @Query("SELECT COUNT(*) FROM hinan_table")
    fun count(): LiveData<Int>

    //市町村データ読み込み
    @Query("SELECT * FROM hinan_table WHERE cityCode = :citycode ")
    fun getcity(citycode:String):LiveData<List<HinanEntity>>

    /*
        @Query("SELECT * FROM pay_table WHERE date = :targetDate")
        fun findDate(targetDate: String):LiveData<List<HinanEntity>>

        @Query("SELECT sum(price)  FROM pay_table Group BY date")
        fun groupDatePrice(): LiveData<List<Int>>

        @Query("SELECT price,date  FROM pay_table Group BY date")
        fun groupDate(): LiveData<List<Group>>
    */
    @Query("DELETE FROM hinan_table WHERE id= :id_obj")
    fun del(id_obj:Int)
/*
    @Query("SELECT * FROM pay_table WHERE date LIKE '%'||:date ||'%' ORDER BY date DESC")
    fun monSelect(date: String):LiveData<List<HinanEntity>>
*/
}