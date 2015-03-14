package com.locol.locol;

import com.locol.locol.pojo.FeedItem;

import java.util.ArrayList;

/**
 * Created by GiapNV on 3/13/15.
 * Project LocoL
 */
public interface FeedItemsLoadedListener {
    public void onFeedItemsLoaded(ArrayList<FeedItem> listMovies);
}
