package com.funlisten.business.order;

import com.funlisten.base.activity.picturePicker.ZYAlbum;
import com.funlisten.base.bean.ZYIBaseBean;
import com.funlisten.business.album.model.bean.ZYAlbumDetail;
import com.funlisten.business.play.model.bean.ZYAudio;

/**
 * Created by ZY on 17/7/4.
 */

public class ZYOrder implements ZYIBaseBean {

    public ZYAlbumDetail album;

    public ZYAudio audio;

    public String gmtCreate;

    public int objectId;

    public String type;//订购内容类型，album：专辑，audio：单集。专辑取album对象里的数据，音频取audio对象里的数据

    public int userId;
}
