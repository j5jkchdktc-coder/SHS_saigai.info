package jp.shsit.shsinfo2025.ui.sonaeru.screenshot;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.List;

import jp.shsit.shsinfo2025.R;

public class GridAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private int layoutId;
    private List<String> imgPathList;

    public GridAdapter(Context context, int layoutId, List<String> imgPathList)
    {
        super();
        this.inflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        this.layoutId = layoutId;
        this.imgPathList = imgPathList;
    }

    @Override
    public int getCount()
    {
        return imgPathList.size();
    }

    @Override
    public Object getItem(int position)
    {
        return null;
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        String imgFilePath = imgPathList.get(position);

        if(convertView == null){
            convertView = inflater.inflate(layoutId, parent, false);
        }

        ImageView imageView;
        imageView = (ImageView) convertView.findViewById(
                R.id.imageView);

        /** ImageViewに画像を設定 */
        if(imageView != null){
            Bitmap bitmap = BitmapFactory.decodeFile(imgFilePath);
            imageView.setImageBitmap(bitmap);
        }

        return convertView;
    }
}