package jp.shsit.shsinfo2025.hinan.csv

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels

import kotlinx.coroutines.launch
import jp.shsit.shsinfo2025.MainActivity
import jp.shsit.shsinfo2025.R
import jp.shsit.shsinfo2025.databinding.FragmentReadHinanBinding
import jp.shsit.shsinfo2025.hinan.MapsFragmenrt2
import jp.shsit.shsinfo2025.room.hinan.HinanViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

import java.io.BufferedReader
import java.io.InputStreamReader

class ReadHinan :Fragment() {
    private var _binding : FragmentReadHinanBinding? = null
    private lateinit var itemArray : ArrayList<CsvItem>
    val viewModel: HinanViewModel by viewModels()

    private val binding get() = _binding!!
    var progressBar : ProgressBar? = null

    @SuppressLint("DiscouragedApi")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReadHinanBinding.inflate(inflater, container, false)
        var dataText= binding.datatext
        var mesText = binding.mestext
        var comText = binding.comtext
        /*************言語選択 **********************/
        val language = androidx.preference.PreferenceManager.getDefaultSharedPreferences(requireContext()).getString("lang", "日本語")
        val main = MainActivity()
        val t1 = main.LangReader("hinan", 11, language)
        dataText.text=t1
        val t2= main.LangReader("hinan", 12, language)
        mesText.text=t2
        val t3 = main.LangReader("hinan", 13, language)
        comText.text=t3
        /*********終了値の読込み*********************/
        val sharedPref = requireActivity().getSharedPreferences("読込み終了値", Context.MODE_PRIVATE)
        val dataRead = sharedPref.getInt("dataRead",0)
        Log.i("test",dataRead.toString())
        if( dataRead==0){
/*
            runBlocking {
                launch {
                    async(coroutineContext){
                        //途中読み込みのデータも消去
                        viewModel.deleteAll()
                    }
                    async(coroutineContext){
                        delay(500)
                    }
                    async(coroutineContext){
                        csvReader()
                    }

                }
            }
*/

        }
        if(dataRead==10){
            val manager = activity?.supportFragmentManager
            val transaction = manager?.beginTransaction()
            /* もどるボタンで戻ってこれるように */
            transaction?.addToBackStack(null)
            transaction?.replace(R.id.nav_host_fragment, MapsFragmenrt2())
            transaction?.commit()
        }
        /******************************/
/*
        var btn1 = binding.button6
        btn1.setOnClickListener {
            val manager = activity?.supportFragmentManager
            val transaction = manager?.beginTransaction()
            /* もどるボタンで戻ってこれるように */
            /* もどるボタンで戻ってこれるように */transaction?.addToBackStack(null)
            transaction?.replace(R.id.nav_host_fragment, MapsFragmenrt2())
            transaction?.commit()
        }

 */

        var btn = binding.readBtn
        /*************言語選択 **********************/
        val t4 = main.LangReader("hinan", 14, language)
        btn.text=t4
        /************/
        btn.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                try {
                    // csvReaderをバックグラウンドスレッドで実行
                    withContext(Dispatchers.IO) {
                        csvReader()
                    }
                } catch (e: Exception) {
                    // エラーハンドリング
                    e.printStackTrace()
                }
            }
        }

        //削除
        var delbtn = binding.delbtn
        /*************言語選択 **********************/
        val t5 = main.LangReader("hinan", 15, language)
        delbtn.text=t5
        /************/
        delbtn.setOnClickListener {

            viewModel.deleteAll()
            /*********終了値の書き込み*********************/
            val sharedPref = requireActivity().getSharedPreferences("読込み終了値", Context.MODE_PRIVATE)
            sharedPref.edit().putInt("dataRead",0).apply()
            /******************************/
        }


        progressBar = binding.progressbar
        progressBar!!.max =100

        var mes = binding.mestext
        var value:Int = 0
        //データベースの件数を表示
        val textcount: TextView = binding.countText

        viewModel.datacount.observe(viewLifecycleOwner){item ->
            textcount.text = item.toString()
            value = item*100/113066
            progressBar!!.progress=value
            Log.i("test",value.toString()+","+item)

            if(value>=99){
                mes.text="完了！"
                mes.setTextColor(Color.RED)
                btn.isEnabled=  false
                /*********終了値の書き込み*********************/
                val sharedPref = requireActivity().getSharedPreferences("読込み終了値", Context.MODE_PRIVATE)
                sharedPref.edit().putInt("dataRead",10).apply()
                /*******画面遷移***********************/
                val manager = activity?.supportFragmentManager
                val transaction = manager?.beginTransaction()
                /* もどるボタンで戻ってこれるように */
                transaction?.addToBackStack(null)
                transaction?.replace(R.id.nav_host_fragment, MapsFragmenrt2())
                transaction?.commit()
            }else{
                val t2= main.LangReader("hinan", 12, language)
                mesText.text=t2
                mes.setTextColor(Color.GRAY)
                btn.isEnabled= true
            }
        }





        return binding.root
    }

    private fun csvReader() {

        itemArray = ArrayList()
        var item: CsvItem
        val inputStream = resources.assets.open("hinan2.csv")
        val inputStreamReader = InputStreamReader(inputStream, "UTF-8")
        val bufferedReader = BufferedReader(inputStreamReader)
        bufferedReader.skip(1)//項目飛ばし
        var i=0
        bufferedReader.forEachLine {
            if (it.isNotBlank() ) {
                item = CsvItem()
                val line = it.split(",").toTypedArray()

                var v0 = "0"
                var v1 = "0"
                var v2 = "0"
                var v3 = "0"
                var v4 = "0"
                var v5 = "0"
                var v6 = "0"
                var v7 = "0"
                var v8 = "0"
                var v9 = "0"
                var v10 = "0"
                var v11 = "0"
                var v12 = "0"
                var v13 = "0"
                var v14 = 0.0
                var v15 = 0.0

                //CSVの左([1]番目)から順番にセット
                if (line[0] != "") {
                    v0=line[0]
                }
                if (line[1] != "") {
                    v1=line[1]
                }
                if (line[2] != "") {
                    v2=line[2]
                }
                if (line[3] != "") {
                    v3=line[3]
                }
                if (line[4] != "") {
                    v4=line[4]
                }
                if (line[5] != "") {
                    v5=line[5]
                }
                if (line[6] != "") {
                    v6=line[6]
                }
                if (line[7] != "") {
                    v7=line[7]
                }
                if (line[8] != "") {
                    v8=line[8]
                }
                if (line[9] != "") {
                    v9=line[9]
                }
                if (line[10] != "") {
                    v10=line[10]
                }
                if (line[11] != "") {
                    v11=line[11]
                }
                if (line[12] != "") {
                    v12=line[12]
                }
                if (line[13] != "") {
                    v13=line[13]
                }
                if (line[14] != "") {
                    v14=line[14].toDouble()
                }
                if (line[15] != "") {
                    v15=line[15].toDouble()
                }
                //備考
               // if (line[16] != "") {
             //       v17=line[16]
             //   }
                i+=1
                Log.i("test",v1+","+v0+","+i)
                viewModel.insert(v0,v1,v2,v3,v4,v5,v6,v7,v8,v9,v10,v11,v12,v13,v14,v15)
                Log.i("test",v1+","+v2)
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            val fragmentManager = parentFragmentManager
            fragmentManager.popBackStack(null, 0)
        }

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}