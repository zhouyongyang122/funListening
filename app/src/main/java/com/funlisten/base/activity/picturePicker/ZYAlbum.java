package com.funlisten.base.activity.picturePicker;

import com.funlisten.base.bean.ZYIBaseBean;

import java.util.ArrayList;

public class ZYAlbum implements ZYIBaseBean {

    public String id;
    public String coverPath;
    public String name;
    public long   dateAdded;
    public ArrayList<ZYPicture> pictures = new ArrayList<>();
    public boolean isSelected;
}
