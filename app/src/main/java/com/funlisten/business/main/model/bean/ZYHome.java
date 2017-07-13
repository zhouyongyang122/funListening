package com.funlisten.business.main.model.bean;

import com.funlisten.base.bean.ZYIBaseBean;

import java.util.List;

/**
 * Created by ZY on 17/5/17.
 */

public class ZYHome implements ZYIBaseBean {

    public List<Banner> recommendBannerDtoList;//banner集合

    public DayListening everyDayListeningDto;//每日精听

    public List<Module> moduleDtoList;//模块集合

    public static class Banner {
        public String type;//专辑(album)audio(音频)h5（h5地址)
        public String imageFileUrl;//图片地址
        public String id;
        public String url;
    }

    public static class Module {
        public int id;//模块id
        public String location;//模块位置
        public String moduleCode;//模块code
        public String type;//模块类型
        public String moduleName;//模块名称
        public String iconImageUrl;//模块图标名称
        public List<ModuleItem> moduleDetailDtoList;//模块数据集合
    }

    public static class ModuleItem {
        public String coverUrl;//背景地址
        public int id;//对象id
    }

    public static class DayListening {

        public List<DayListenAudio> everyDayAudioListeningDtoList;

        public int id;//专辑id

        public String imageUrl;//图片地址

        public String name;//专辑名称
    }

    public static class DayListenAudio {
        public int id;//音频id
        public String title;//音频标题
    }
}
