package com.locol.locol.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.locol.locol.R;
import com.parse.ParseObject;
import com.parse.ParseQueryAdapter;

import java.lang.reflect.Field;

/**
 * Created by GiapNV on 5/2/15.
 * Project LocoL
 */
public class ListCategoryAdapter extends ParseQueryAdapter<ParseObject> {

    public ListCategoryAdapter(Context context, ParseQueryAdapter.QueryFactory<ParseObject> parseQuery) {
        super(context, parseQuery);
    }

    @Override
    public View getItemView(final ParseObject object, View v, ViewGroup parent) {
        if (v == null) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.lv_category_item, parent, false);
        }
        super.getItemView(object, v, parent);

        ImageView image = (ImageView) v.findViewById(R.id.image);
        Drawable d = null;

        try {
            Class res = R.drawable.class;
            Field field = res.getField("ic_" + object.getString("slug"));
            int drawableId = field.getInt(null);
            d = getContext().getResources().getDrawable(drawableId);
        } catch (Exception e) {
            Log.e("ListCategoryAdapter", "Failure to get drawable id.", e);
        }

        image.setImageDrawable(d);

        TextView text = (TextView) v.findViewById(R.id.text);
        text.setText(object.getString("name"));

        return v;
    }
}
