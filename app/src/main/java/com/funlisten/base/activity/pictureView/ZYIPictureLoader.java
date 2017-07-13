package com.funlisten.base.activity.pictureView;

import java.io.Serializable;
import java.util.List;

public interface ZYIPictureLoader extends Serializable{
    void loadData(PhotoCallback photoCallback);

    interface PhotoCallback {

        void onPhoto(List<String> photoList);

        void onEmpty();
    }
}
