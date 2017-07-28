package com.funlisten.business.play.model.bean;

import com.funlisten.base.bean.ZYIBaseBean;

/**
 * Created by ZY on 17/7/4.
 */

public class ZYAudio implements ZYIBaseBean {

    public String gmtCreate;

    public int id;

    public String title;

    public int playCount;

    public int audioTimeLength;//s

    public int albumId;

    public int commentCount;

    public String costType;//收费类型，free:免费,paid:付费

    public String cover;

    public String coverUrl;

    public String fileUrl;

    public int downloadCount;

    public int favoriteCount;

    public int fileLength;

    public String isAudition;//是否能试听，y：是，n：否

    public String label;

    public int payCount;

    public int sort;//当前顺序

    public boolean isPlaying;

    public boolean isBuy;

    public boolean needBuy() {
        return costType != null && costType.equals("paid");
    }

    public boolean isFree() {
        return costType != null && costType.equals("free");
    }

    public boolean isAudition() {
        return isAudition != null && isAudition.equals("y");
    }

    public boolean isBuy() {
        return isBuy;
    }
}
