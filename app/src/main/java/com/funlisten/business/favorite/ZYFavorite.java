package com.funlisten.business.favorite;

import com.funlisten.base.activity.picturePicker.ZYAlbum;
import com.funlisten.base.bean.ZYIBaseBean;
import com.funlisten.business.play.model.bean.ZYAudio;

/**
 * Created by ZY on 17/7/4.
 */

public class ZYFavorite implements ZYIBaseBean {

    public ZYAlbum album;

    public ZYAudio audio;

    public String gmtCreate;

    public int id;

    public int objectId;

    public int objectUserId;

    public String type;//喜欢、订阅类型，album：专辑，audio：单集
}
