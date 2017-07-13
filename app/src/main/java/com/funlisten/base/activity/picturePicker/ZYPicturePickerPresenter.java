package com.funlisten.base.activity.picturePicker;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ZYPicturePickerPresenter implements ZYPicturePickerContract.Presenter{

    private int mMaxSelect;
    private ZYPicturePickerContract.View mView;
    private List<ZYAlbum> mAlbumList;
    private ArrayList<String> mSelectedPathList = new ArrayList<>();
    private ZYAlbum mSelectedAlbum;
    private boolean mShowGif = false;
    private boolean mIsSingle = false;

    public ZYPicturePickerPresenter(ZYPicturePickerContract.View view, int maxSelect) {
        mView = view;
        mMaxSelect = maxSelect;
        mView.setPresenter(this);
    }

    public ZYPicturePickerPresenter(ZYPicturePickerContract.View view) {
        mView = view;
        mMaxSelect = 1;
        mView.setPresenter(this);
        mIsSingle = true;
    }

    @Override
    public void getData(FragmentActivity activity) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ZYMediaStoreHelper.KEY_IS_SHOW_GIF, mShowGif);
        ZYMediaStoreHelper.getAlbums(activity, bundle, new ZYMediaStoreHelper.AlbumsResultCallback() {
            @Override
            public void onResultCallback(List<ZYAlbum> albums) {
                if (albums != null && !albums.isEmpty()) {
                    mAlbumList = albums;
                    mSelectedAlbum = albums.get(0);
                    mSelectedAlbum.isSelected = true;
                    mView.showAlbumList(albums, mSelectedAlbum.name);
                    mView.showPictureList(albums.get(0).pictures);
                } else {
                    mView.showEmpty();
                }
            }
        });
    }

    @Override
    public void changeAlbum(int position) {
        mSelectedAlbum.isSelected = false;
        mSelectedAlbum = mAlbumList.get(position);
        mSelectedAlbum.isSelected = true;
        mView.showPictureList(mSelectedAlbum.pictures);
        mView.showAlbumList(mAlbumList, mSelectedAlbum.name);
    }

    @Override
    public void selectPicture(boolean isSelect, int position) {
        ZYPicture picture = mSelectedAlbum.pictures.get(position);
        if (mIsSingle) {
            Intent intent = new Intent();
            intent.putExtra(ZYPicker.KEY_SINGLE_URI, Uri.fromFile(new File(picture.path)));
            mView.pickFinish(intent);
        } else {
            picture.isSelected = isSelect;
            if (isSelect) {
                if (mSelectedPathList.size() == mMaxSelect) {
                    picture.isSelected = false;
                    mView.showMaxSelectTip(mMaxSelect, position);
                } else {
                    if (!mSelectedPathList.contains(picture.path)) {
                        mSelectedPathList.add(picture.path);
                    }
                }
            } else {
                if (mSelectedPathList.contains(picture.path)) {
                    mSelectedPathList.remove(picture.path);
                }
            }
        }

        mView.setPreviewCount(mSelectedPathList.size(), mMaxSelect);
    }

    @Override
    public ArrayList<String> getSelectedPathList() {
        return mSelectedPathList;
    }

    @Override
    public ArrayList<String> getAllPictureList() {
        ArrayList<String> allPictureList = new ArrayList<>();
        for (ZYPicture picture : mSelectedAlbum.pictures) {
            allPictureList.add(picture.path);
        }
        return allPictureList;
    }

    @Override
    public boolean isSingle() {
        return mIsSingle;
    }
}
