package jp.shsit.shsinfo2025.hinan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

import jp.shsit.shsinfo2025.R;


/**
 * Created by shsit on 2021/04/21.
 */

public class ListViewAdapter extends ArrayAdapter<ListData> {
    private LayoutInflater layoutInflater;

    public ListViewAdapter(Context context, int resource, List<ListData> objects) {
        super(context, resource, objects);
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListData data = (ListData) getItem(position);
        if (null == convertView) {
            convertView = layoutInflater.inflate(R.layout.hinan_list_item, parent, false);
        }

        System.out.println(data.name);

        TextView idText;
        TextView nameText;
        TextView addressText;
        TextView kouzuiText;
        TextView jishinText;
        TextView tsunamiText;
        TextView jyouText;

        idText = (TextView) convertView.findViewById(R.id.id);
        nameText = (TextView) convertView.findViewById(R.id.name);
        addressText = (TextView) convertView.findViewById(R.id.address);
        kouzuiText = (TextView) convertView.findViewById(R.id.kouzui);
        jishinText = (TextView) convertView.findViewById(R.id.jishin);
        tsunamiText = (TextView) convertView.findViewById(R.id.tsunami);
        jyouText = (TextView) convertView.findViewById(R.id.jyou);


        idText.setText(data.id);
        nameText.setText(data.name);
        addressText.setText(data.getAddress());
        kouzuiText.setText(data.kouzui);
        jishinText.setText(data.jishin);
        tsunamiText.setText(data.tsunami);
        jyouText.setText(data.jyou);


        return convertView;
    }
}