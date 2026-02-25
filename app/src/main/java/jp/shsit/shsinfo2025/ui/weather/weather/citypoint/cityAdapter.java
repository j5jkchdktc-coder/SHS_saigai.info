package jp.shsit.shsinfo2025.ui.weather.weather.citypoint;

import android.content.Context;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import jp.shsit.shsinfo2025.R;


public class cityAdapter extends ArrayAdapter<ItemCity> {
    private LayoutInflater layoutInflater;

    public cityAdapter(@NonNull Context context, int resource, List<ItemCity> objects) {
        super(context, resource, objects);
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // super.getView() は 呼ばない(カスタムビューにしているため)

        if (null == convertView) {
            convertView = layoutInflater.inflate(R.layout.item_city, null);
        }

        // データをgetItemで取る
        ItemCity item = getItem(position);

        // カスタムビューの場合はViewが確実にあるtry-catch は不要ためか。
        TextView city = (TextView) convertView.findViewById(R.id.city);

        String language= PreferenceManager.getDefaultSharedPreferences(getContext()).getString("lang", "日本語");
        if(language.equals("English")){
            city.setText(item.getCity_eng());
        }else if(language.equals("Vietnamese")){
            city.setText(item.getCity_vn());
        }else if(language.equals("Chinese")){
            city.setText(item.getCity_ch());
        }else{
            city.setText(item.getCity());
        }

        return convertView;
    }

    // 設定されている CustomListItem の ArrayList を返す。
    // 縦横切替などでデータを移行するために使う。
    public ArrayList<ItemCity> getItemList() {

        // 今回は Bundle#putParcelableArrayList() を使うことを想定する。
        // 必要に応じて Bundle#putSparseParcelableArray() を使ってもいい。

        int size = getCount();
        ArrayList<ItemCity> itemList = new ArrayList<ItemCity>(size);
        for (int index = 0; index < size; index++) {
            itemList.add(getItem(index));
        }
        return itemList;
    }


}