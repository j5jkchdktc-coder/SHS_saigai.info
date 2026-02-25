package jp.shsit.shsinfo2025.ui.sonaeru;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import jp.shsit.shsinfo2025.MainActivity;
import jp.shsit.shsinfo2025.R;

public class MochiCheackFragment extends Fragment implements firstAdapter.EventListener{

    TextView tv;



    private static String s11;
    private  String[] names ;

    // drawableに画像を入れる、R.id.xxx はint型
    private static final int[] photos = {
            R.drawable.kaityuu,
            R.drawable.mobairu,
            R.drawable.denti2,
            R.drawable.radio,
            R.drawable.kityouhin,
            R.drawable.fuku,
            R.drawable.hozon,
            R.drawable.raita,
            R.drawable.mafuro,
            R.drawable.kyuukyuu,
            R.drawable.keitai,
            R.drawable.kami,
            R.drawable.pfudebako,
            R.drawable.baby,
            R.drawable.inu

    };
    private static boolean[] flags = {
            false,false,false,false,false,
            false,false,false,false,false,false,false,false,false,false
    };//アイテム分必要です。

    int sum;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_mochi, container, false);

        String language= PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("lang", "日本語");
        MainActivity main = new MainActivity();
        //懐中電灯
        String s1= main.LangReader("mochi",0,language);
        // "モバイルバッテリー",
        String s2= main.LangReader("mochi",1,language);
        //"予備の電池",
        String s3= main.LangReader("mochi",2,language);
        //"ラジオ",
        String s4= main.LangReader("mochi",3,language);
        //"貴重品",
        String s5= main.LangReader("mochi",4,language);
        // "衣類・下着",
        String s6= main.LangReader("mochi",5,language);
        // "保存食",
        String s7= main.LangReader("mochi",6,language);
        //ライター
        String s8= main.LangReader("mochi",7,language);
        //"防寒グッズ",
        String s9= main.LangReader("mochi",8,language);
        //"救急用品",
        String s10= main.LangReader("mochi",9,language);
        // "携帯電話",
        String s11= main.LangReader("mochi",10,language);
        //"衛生用品",
        String s12= main.LangReader("mochi",11,language);
        //"筆記用具",
        String s13= main.LangReader("mochi",12,language);
        //"赤ちゃんに必要なもの"
        String s14= main.LangReader("mochi",13,language);
        //"犬に必要なもの"
        String s15= main.LangReader("mochi",14,language);

        names  = new String[]{
                s1,
                s2,
                s3,
                s4,
                s5,
                s6,
                s7,
                s8,
                s9,
                s10,
                s11,
                s12,
                s13,
                s14,
                s15
        };

        // ListViewのインスタンスを生成
        ListView listView = root.findViewById(R.id.listview);
        sum=0;
        //flagsの読みこみ
        flags = getArray("key");
        for(int i=0;i<flags.length;i++){
            if(flags[i]==true){
                sum++;
            }
        }


        // BaseAdapter を継承したadapterのインスタンスを生成
        // レイアウトファイル list_items.xml を
        // activity_main.xml に inflate するためにadapterに引数として渡す
        BaseAdapter adapter = new firstAdapter(getActivity().getApplicationContext(),

                R.layout.list_item, names, flags, photos,this);

        // ListViewにadapterをセット
        listView.setAdapter(adapter);

        tv=root.findViewById(R.id.check);


        String hyouji = sum + "/15";
        tv.setText(String.valueOf(hyouji));

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String text = "onItemLongClick:" + position;
                Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                Bundle args = new Bundle();
                args.putInt("pos",position);
                Fragment fragment = new page1Fragment();
                fragment.setArguments(args);
                //もどるボタンで戻ってこれるように
                transaction.addToBackStack(null);
                transaction.replace(R.id.nav_host_fragment,fragment);
                transaction.commit();



                return false;
            }
        });

        Button modBtn = root.findViewById(R.id.mo1Button);
        modBtn.setText(main.LangReader("hinan",5,language));
        modBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
            }
        });
        //チェックの数
        TextView tv2 = root.findViewById(R.id.check_title);
        tv2.setText(main.LangReader("mochi",54,language));

        return root;
    }
    @Override
    public void onPause() {
        super.onPause();
        saveArray(flags, "key");

    }

    /********adapterから実行されるメソッド　***********/
    @Override
    public void onEvent(int date) {
        int b=sum+date;
        float c= (float) (b/15.0*100);
        int d=(int) c;
        String a = String.valueOf(d);

        String hyouji = b+"/15";
        tv.setText(hyouji);
    }

/******************************************/

    //配列を保存する
    private void saveArray(boolean[] array, String PrefKey) {
        String[] array1 = new String[array.length];
        for (int i = 0; i < array.length; i++) {
            array1[i] = String.valueOf(array[i]);
        }
        StringBuffer buffer = new StringBuffer();
        String stringItem = null;
        for (String item : array1) {
            buffer.append(item + ",");
        }
        ;

        if (buffer != null) {
            String buf = buffer.toString();
            stringItem = buf.substring(0, buf.length() - 1);
            Log.i("test", "koko2");
            SharedPreferences prefs1 = getContext().getSharedPreferences("Array", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs1.edit();
            editor.putString(PrefKey, stringItem).commit();
        }
    }
/******************************************/
//配列を読み出す

private boolean[] getArray(String PrefKey) {
        SharedPreferences prefs2 = getContext().getSharedPreferences("Array", Context.MODE_PRIVATE);
        String stringItem = prefs2.getString(PrefKey, "");
        if (stringItem != null && stringItem.length() != 0) {
        String[] a = stringItem.split(",");
        boolean[] b = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
        b[i] = Boolean.valueOf(a[i]);
        }
        return b;
        } else {
        //初期値の場合
        boolean[] b = new boolean[flags.length];
        for (int i = 0; i < flags.length; i++) {
        b[i] = false;
        }
        return b;
        }
        }
    }


