package com.funlisten.base.event;

/**
 * Created by ZY on 17/8/24.
 */

public class ZYEventFavoriteAlbum {

    public boolean isFavorite;

    public int albumId;

    public ZYEventFavoriteAlbum(boolean isFavorite, int albumId) {
        this.isFavorite = isFavorite;
        this.albumId = albumId;
    }
}
