package postaround.tcc.inatel.br.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import postaround.tcc.inatel.br.model.CropImage;
import postaround.tcc.inatel.br.postaround.R;

/**
 * Created by Carol on 05/11/2015.
 */
public class CropImageAdapter extends ArrayAdapter<CropImage> {

    private ArrayList<CropImage> mOptions;
    private LayoutInflater mInflater;

    public CropImageAdapter(Context context, ArrayList<CropImage> options) {
        super(context, R.layout.crop_selector, options);

        mOptions = options;

        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup group) {
        if (convertView == null)
            convertView = mInflater.inflate(R.layout.crop_selector, null);

        CropImage item = mOptions.get(position);

        if (item != null) {
            ((ImageView) convertView.findViewById(R.id.iv_icon))
                    .setImageDrawable(item.icon);
            ((TextView) convertView.findViewById(R.id.tv_name))
                    .setText(item.title);

            return convertView;
        }

        return null;
    }
}
