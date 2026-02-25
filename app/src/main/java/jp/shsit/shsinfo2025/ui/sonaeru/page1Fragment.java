package jp.shsit.shsinfo2025.ui.sonaeru;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import jp.shsit.shsinfo2025.MainActivity;
import jp.shsit.shsinfo2025.R;


public class page1Fragment extends Fragment {
    int position;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_page1, container, false);
        Bundle args = getArguments();
        position = args.getInt("pos");
        System.out.println(position+"です");
        ImageView imageView = root.findViewById(R.id.imageView);
        TextView text = root.findViewById(R.id.textView2);
        TextView text2 = root.findViewById(R.id.textView3);
        TextView text3 = root.findViewById(R.id.textView4);

        Button moBtn = root.findViewById(R.id.mo2Button);
        moBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
            }
        });

        String language= PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("lang", "日本語");
        MainActivity main = new MainActivity();

        switch (position){
            case 0:
                imageView.setImageResource(R.drawable.kaityuu);
                String s0= main.LangReader("mochi",0,language);
                text.setText(s0);
                text2.setText("説明");
                String s00= main.LangReader("mochi",15,language);
                text3.setText(s00);

                break;
            case 1:
                imageView.setImageResource(R.drawable.mobairu);
                String s1= main.LangReader("mochi",1,language);
                text.setText(s1);
                text2.setText("説明");
                String s101= main.LangReader("mochi",16,language);
                text3.setText(s101 );
                break;
            case 2:
                imageView.setImageResource(R.drawable.denti);
                String s2= main.LangReader("mochi",2,language);
                text.setText(s2);
                text2.setText("説明");
                String s202= main.LangReader("mochi",17,language);
                text3.setText(s202);
                break;
            case  3:
                imageView.setImageResource(R.drawable.radio);
                String s3= main.LangReader("mochi",3,language);
                text.setText(s3);
                text2.setText("説明");
                String s303= main.LangReader("mochi",18,language);
                text3.setText(s303);
                break;
            case 4:
                imageView.setImageResource(R.drawable.kityouhin);
                String s4= main.LangReader("mochi",4,language);
                text.setText(s4);
                text2.setText("説明");
                String s441= main.LangReader("mochi",19,language);
                String s442= main.LangReader("mochi",20,language);
                String s443= main.LangReader("mochi",21,language);
                String s444= main.LangReader("mochi",22,language);
                String s445= main.LangReader("mochi",23,language);
                String s446= main.LangReader("mochi",24,language);
                text3.setText(s441+"\n" +
                        s442+"\n" +
                        s443+"\n" +
                        s444+"\n" +
                        s445+"\n" +
                        s446);
                break;
            case  5:
                imageView.setImageResource(R.drawable.fuku);
                //"衣類";
                String s5= main.LangReader("mochi",5,language);
                text.setText(s5);
                text2.setText("説明");
                String s501= main.LangReader("mochi",25,language);
                text3.setText(s501);
                break;
            case 6:
                imageView.setImageResource(R.drawable.hozon);
                //"保存食");
                String s6= main.LangReader("mochi",6,language);
                text.setText(s6);
                text2.setText("説明");
                text3.setText("・缶詰・乾パン\n・飲料水\n・アルファ米\n・チョコレート\n自宅が倒壊した場合は避難所での生活を送ることになります。救援物資が届くまで最短でも3日かかると想定しましょう。");
                String s61= main.LangReader("mochi",26,language);
                String s62= main.LangReader("mochi",27,language);
                String s63= main.LangReader("mochi",28,language);
                String s64= main.LangReader("mochi",29,language);
                String s65= main.LangReader("mochi",30,language);
                text3.setText(s61+"\n" +
                        s62+"\n" +
                        s63+"\n" +
                        s64+"\n" +
                        s65);
                break;
            case 7:
                imageView.setImageResource(R.drawable.raita);
                //"ライター");
                String s7= main.LangReader("mochi",7,language);
                text.setText(s7);
                text2.setText("説明");
                String s71= main.LangReader("mochi",31,language);
                text3.setText(s71);
                break;
            case 8:
                imageView.setImageResource(R.drawable.tebukuro);
                //"防寒グッズ");
                String s8= main.LangReader("mochi",8,language);
                text.setText(s8);
                text2.setText("説明");
                String s81= main.LangReader("mochi",32,language);
                String s82= main.LangReader("mochi",33,language);
                String s83= main.LangReader("mochi",34,language);
                String s84= main.LangReader("mochi",35,language);
                text3.setText(s81+"\n" +
                        s82+"\n" +
                        s83+"\n" +
                        s84);
                break;
            case 9:
                imageView.setImageResource(R.drawable.kyuukyuu);
                //"救急用品");
                String s9= main.LangReader("mochi",9,language);
                text.setText(s9);
                text2.setText("説明");
                String s91= main.LangReader("mochi",36,language);
                text3.setText(s91);
                break;
            case 10:
                imageView.setImageResource(R.drawable.keitai);
                //"携帯電話");
                String s10= main.LangReader("mochi",10,language);
                text.setText(s10);
                text2.setText("説明");
                String s1001= main.LangReader("mochi",37,language);
                text3.setText(s1001);
                break;
            case 11:
                imageView.setImageResource(R.drawable.kami);
                //"衛生用品");
                String s11 = main.LangReader("mochi",11,language);
                text.setText(s11);
                text2.setText("説明");
                String s111= main.LangReader("mochi",38,language);
                String s112= main.LangReader("mochi",39,language);
                String s113= main.LangReader("mochi",40,language);
                String s114= main.LangReader("mochi",41,language);
                String s115= main.LangReader("mochi",42,language);
                String s116= main.LangReader("mochi",43,language);
                text3.setText(s111+"\n" +
                        s112+"\n" +
                        s113+"\n" +
                        s114+"\n" +
                        s115+"\n" +
                        s116);
                break;
            case 12:
                imageView.setImageResource(R.drawable.pfudebako);
                //"筆記用具");
                String s12= main.LangReader("mochi",12,language);
                text.setText(s12);
                text2.setText("説明");
                String s121= main.LangReader("mochi",44,language);
                text3.setText(s121);
                break;
            case 13:
                imageView.setImageResource(R.drawable.baby);
                //"赤ちゃんに必要なもの");
                String s13= main.LangReader("mochi",13,language);
                text.setText(s13);
                text2.setText("説明");
                String s131= main.LangReader("mochi",45,language);
                String s132= main.LangReader("mochi",46,language);
                String s133= main.LangReader("mochi",47,language);
                String s134= main.LangReader("mochi",48,language);
                String s135= main.LangReader("mochi",49,language);
                text3.setText(s131+"\n" +
                        s132+"\n" +
                        s133+"\n" +
                        s134+"\n" +
                        s135);
                break;
            case 14:
                imageView.setImageResource(R.drawable.inu);
                //"犬に必要なもの");
                String s14= main.LangReader("mochi",14,language);
                text.setText(s14);
                text2.setText("説明");
                String s141= main.LangReader("mochi",50,language);
                String s142= main.LangReader("mochi",51,language);
                String s143= main.LangReader("mochi",52,language);
                String s144= main.LangReader("mochi",53,language);
                text3.setText(s141+"\n" +
                        s142+"\n" +
                        s143+"\n" +
                        s144);
               default:


        }


        return root;

    }
}
