package jp.shsit.shsinfo2025.ui.sonaeru;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import jp.shsit.shsinfo2025.MainActivity;
import jp.shsit.shsinfo2025.R;


// BaseAdapterを継承したクラス
public class firstAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private int layoutID;
    private String[] namelist;
    private boolean[] flaglist;
    private Bitmap[] photolist;
    int i;
    EventListener listener;

    static class ViewHolder  {
        TextView text;
        TextView menber;
        ImageView img;
        CheckBox checkBox;
    }

    public firstAdapter(Context context, int itemLayoutId,
                        String[] names, boolean[] flags, int[] photos, EventListener listener){

        inflater = LayoutInflater.from(context);
        layoutID = itemLayoutId;

        namelist = names;
        flaglist = flags;
        // bitmapの配列
        photolist = new Bitmap[photos.length];
        // drawableのIDからbitmapに変換
        for( int i=0; i< photos.length; i++){
            photolist[i] = BitmapFactory.decodeResource(context.getResources(), photos[i]);
        }
        this.listener = listener;
    }
    //表示される前に１行ずつ処理
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(layoutID, null);
            holder = new ViewHolder();
            holder.img = convertView.findViewById(R.id.img_item);
            holder.text = convertView.findViewById(R.id.text_view);
            holder.menber = convertView.findViewById(R.id.text_menber);
            holder.checkBox = convertView.findViewById(R.id.checkBox);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.img.setImageBitmap(photolist[position]);

        //長押しタップ
        String language= PreferenceManager.getDefaultSharedPreferences(inflater.getContext()).getString("lang", "日本語");
        MainActivity main = new MainActivity();
        String str = main.LangReader("mochi",55,language);

        holder.menber.setText(str);

        holder.text.setText(namelist[position]);

        if (flaglist[position] == true) {
            holder.checkBox.setChecked(true);
        }else{
            holder.checkBox.setChecked(false);
        }


        //チェックボックスのリスナーを登録
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean check = holder.checkBox.isChecked();
                if(check){
                   // holder.checkBox.setText("チェック");
                    flaglist[position]= true;
                    i++;
                }
                else{
                   // holder.checkBox.setText("チェックされてない");
                    flaglist[position]= false;
                    i--;


                }

                listener.onEvent(i);
            }
        });



        return convertView;
    }
    public interface EventListener {
        void onEvent(int data);
    }

    @Override
    public int getCount() {
        return namelist.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
