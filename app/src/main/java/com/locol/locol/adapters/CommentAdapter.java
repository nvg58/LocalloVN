package com.locol.locol.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.locol.locol.R;
import com.locol.locol.application.MainApplication;
import com.parse.ParseObject;
import com.parse.ParseQueryAdapter;
import com.squareup.picasso.Picasso;

import java.text.MessageFormat;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by GiapNV on 5/3/15.
 * Project LocoL
 */
public class CommentAdapter extends ParseQueryAdapter<ParseObject> {

    public CommentAdapter(Context context, ParseQueryAdapter.QueryFactory<ParseObject> parseQuery) {
        super(context, parseQuery);
    }

    @Override
    public View getItemView(final ParseObject object, View v, ViewGroup parent) {
        if (v == null) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_row, parent, false);
        }
        super.getItemView(object, v, parent);

        object.pinInBackground();

        CircleImageView userPhoto = (CircleImageView) v.findViewById(R.id.user_photo);
        Picasso.with(getContext())
                .load(object.getString("author_avatar_url"))
                .placeholder(R.drawable.profile)
                .into(userPhoto);

        TextView comment = (TextView) v.findViewById(R.id.comment);
        comment.setText(object.getString("comment"));

        TextView author = (TextView) v.findViewById(R.id.author);
        author.setText(object.getString("author_name"));

        TextView time = (TextView) v.findViewById(R.id.time);
        int hourPast = checkedCast((Calendar.getInstance().getTime().getTime() - object.getCreatedAt().getTime()) / 1000 / 60 / 60);
        int minPast = checkedCast((Calendar.getInstance().getTime().getTime() - object.getCreatedAt().getTime()) / 1000 / 60);
        int secPast = checkedCast((Calendar.getInstance().getTime().getTime() - object.getCreatedAt().getTime()) / 1000);
        Resources res = MainApplication.getAppContext().getResources();
        if (hourPast > 0)
            time.setText(MessageFormat.format(res.getText(R.string.hour).toString(), hourPast));
        else if (minPast > 0)
            time.setText(MessageFormat.format(res.getText(R.string.minute).toString(), minPast));
        else
            time.setText(MessageFormat.format(res.getText(R.string.second).toString(), secPast));

        return v;
    }

    private int checkedCast(long value) {
        int result = (int) value;
        if (result != value) {
            // if value is greater than Integer.MAX_VALUE or less than Integer.MIN_VALUE
            throw new IllegalArgumentException("Out of range: " + value);
        }
        return result;
    }

}