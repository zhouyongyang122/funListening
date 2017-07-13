package com.funlisten.business.album.model.bean;

import com.funlisten.base.bean.ZYIBaseBean;

import java.util.List;

/**
 * Created by ZY on 17/6/13.
 */

public class ZYAlbumDetail implements ZYIBaseBean {

    public int id;

    public String name;

    public String title;

    public String coverUrl;//封面url

    public String backgroundUrl;//背景url

    public int audioCount;//音频数量

    public int categoryId;//类目id

    public int commentCount;//评论次数

    public String costType;//收费类型  收费类型 free:免费,paid:付费

    public String costTypeName;

    public String creator;//作者

    public int downloadCount;//下载次数

    public int favoriteCount;//订阅次数

    public String gmtCreate;//创建时间

    public String gmtModified;//修改时间

    public int originPrice;//原价

    public int sellPrice;//价格

    public int payCount;//购买次数

    public int playCount;//播放次数

    public String status;//专辑状态

    public int publisherId;//发布人id

    public Publisher publisher;//发布人信息

    public String saleType;//售卖类型 售卖类型 album:专辑,audio:单集

    public String saleTypeName;

    public List<Detail> details;

    public List<ZYCategory> categoryList;

    public String backgoundUrl;//背景

    public String getCategoryNames() {
        String names = "";
        if (categoryList != null && categoryList.size() > 0) {
            for (ZYCategory category : categoryList) {
                names += "," + category.name;
            }
        }
        return names;
    }

    public class Detail implements ZYIBaseBean {
        public String title;
        public String content;//富文本内容
    }

    public class Publisher implements ZYIBaseBean {

        public int id;

        public String nickname;

        public int follow;

        public int fans;

        public String avatarUrl;
    }
}
