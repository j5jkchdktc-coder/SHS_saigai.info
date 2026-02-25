package jp.shsit.shsinfo2025.ui.sonaeru.public_facility;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

import jp.shsit.shsinfo2025.R;


/**
 * Created by shsit on 2021/04/21.
 */

public class PublicListViewAdapter extends ArrayAdapter<PublicListData> {
    private LayoutInflater layoutInflater;

    public PublicListViewAdapter(Context context, int resource, ArrayList<PublicListData> objects) {
        super(context, resource, objects);
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PublicListData data = (PublicListData) getItem(position);
        if (null == convertView) {
            convertView = layoutInflater.inflate(R.layout.public_list_item, null);
        }
        System.out.println(data.getName());

        TextView nameText;
        TextView addressText;

        nameText = (TextView) convertView.findViewById(R.id.name);
        addressText = (TextView) convertView.findViewById(R.id.address);

        nameText.setText(data.getName());
        addressText.setText(data.getAddress());



        return convertView;
    }
}