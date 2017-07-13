package com.funlisten.business.album.model.bean;

import com.funlisten.base.bean.ZYIBaseBean;

/**
 * Created by ZY on 17/7/5.
 */

public class ZYAlbumEpisode implements ZYIBaseBean {

    public int start;

    public int end;

    public ZYAlbumEpisode(int start, int end) {
        this.start = start;
        this.end = end;
    }
}
