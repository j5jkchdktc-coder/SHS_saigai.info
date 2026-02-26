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

public class firstAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private int layoutID;
    private String[] namelist;
    private boolean[] flaglist;
    private Bitmap[] photolist;
    private int[] originalIds;
    private EventListener listener;

    static class ViewHolder {
        TextView text;
        TextView member;
        ImageView img;
        CheckBox checkBox;
    }

    public interface EventListener {
        void onCheckChanged(int originalId, boolean isChecked);
    }

    public firstAdapter(Context context, int itemLayoutId,
                        String[] names, boolean[] flags, int[] photos, EventListener listener) {
        inflater = LayoutInflater.from(context);
        layoutID = itemLayoutId;
        namelist = names;
        flaglist = flags;
        photolist = new Bitmap[photos.length];
        for (int i = 0; i < photos.length; i++) {
            photolist[i] = BitmapFactory.decodeResource(context.getResources(), photos[i]);
        }
        this.listener = listener;
    }

    public void setOriginalIds(int[] ids) {
        this.originalIds = ids;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(layoutID, null);
            holder = new ViewHolder();
            holder.img = convertView.findViewById(R.id.img_item);
            holder.text = convertView.findViewById(R.id.text_view);
            holder.member = convertView.findViewById(R.id.text_menber);
            holder.checkBox = convertView.findViewById(R.id.checkBox);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.img.setImageBitmap(photolist[position]);

        String language = PreferenceManager.getDefaultSharedPreferences(inflater.getContext()).getString("lang", "日本語");
        MainActivity main = new MainActivity();
        String str = main.LangReader("mochi", 55, language);
        holder.member.setText(str);
        holder.text.setText(namelist[position]);
        holder.checkBox.setChecked(flaglist[position]);

        holder.checkBox.setOnClickListener(v -> {
            boolean isChecked = holder.checkBox.isChecked();
            flaglist[position] = isChecked;
            if (originalIds != null && listener != null) {
                listener.onCheckChanged(originalIds[position], isChecked);
            }
        });

        return convertView;
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
