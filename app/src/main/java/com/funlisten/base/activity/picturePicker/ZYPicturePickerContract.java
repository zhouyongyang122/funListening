package com.funlisten.base.activity.picturePicker;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import java.util.ArrayList;
import java.util.List;

public interface ZYPicturePickerContract {

    interface View  {

        void setPresenter(Presenter presenter);

        void showPictureList(List<ZYPicture> pictureList);

        void showAlbumList(List<ZYAlbum> albumList, String albumName);

        void showEmpty();

        void showAlbumView(boolean isShow);

        void showMaxSelectTip(int max, int position);

        void setPreviewCount(int count, int max);

        void pickFinish(Intent intent);
    }

    interface Presenter {

        void getData(FragmentActivity activity);

        void changeAlbum(int position);

        void selectPicture(boolean isSelect, int position);

        ArrayList<String> getSelectedPathList();

        ArrayList<String> getAllPictureList();

        boolean isSingle();
    }
}
