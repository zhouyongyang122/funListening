package com.funlisten.base.activity.picturePicker;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import static android.provider.BaseColumns._ID;
import static android.provider.MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME;
import static android.provider.MediaStore.Images.ImageColumns.BUCKET_ID;
import static android.provider.MediaStore.MediaColumns.DATA;
import static android.provider.MediaStore.MediaColumns.DATE_ADDED;

public class ZYMediaStoreHelper {
    public final static int INDEX_ALL_PHOTOS = 0;
    public final static String KEY_IS_SHOW_GIF = "is_show_gif";


    public static void getAlbums(FragmentActivity activity, @NonNull Bundle args, AlbumsResultCallback resultCallback) {
        activity.getSupportLoaderManager()
                .initLoader(0, args, new AlbumLoaderCallbacks(activity, resultCallback));
    }

    static class AlbumLoaderCallbacks implements LoaderManager.LoaderCallbacks<Cursor> {

        private WeakReference<Context> context;
        private AlbumsResultCallback resultCallback;

        public AlbumLoaderCallbacks(Context context, AlbumsResultCallback resultCallback) {
            this.context = new WeakReference<>(context);
            this.resultCallback = resultCallback;
        }

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            return new ZYAlbumLoader(context.get(), args.getBoolean(KEY_IS_SHOW_GIF, false));
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

            if (data == null) return;
            List<ZYAlbum> albumList = new ArrayList<>();
            ZYAlbum albumAll = new ZYAlbum();
            albumAll.name = "所有图片";
            albumAll.id = "ALL";

            List<String> albumNames = new ArrayList<>();
            while (data.moveToNext()) {

                int imageId = data.getInt(data.getColumnIndexOrThrow(_ID));
                String bucketId = data.getString(data.getColumnIndexOrThrow(BUCKET_ID));
                String name = data.getString(data.getColumnIndexOrThrow(BUCKET_DISPLAY_NAME));
                String path = data.getString(data.getColumnIndexOrThrow(DATA));

                if (path != null && !path.isEmpty() && new File(path).exists()) {
                    ZYPicture picture = new ZYPicture(imageId, path);
                    if (!albumNames.contains(name)) {
                        ZYAlbum album = new ZYAlbum();
                        album.id = bucketId;
                        album.name = name;
                        album.coverPath = path;
                        album.dateAdded = data.getLong(data.getColumnIndexOrThrow(DATE_ADDED));
                        albumList.add(album);
                        albumNames.add(name);
                    }

                    albumList.get(albumNames.indexOf(name)).pictures.add(picture);

                    albumAll.pictures.add(picture);
                }

            }
            if (albumAll.pictures.size() > 0) {
                albumAll.coverPath = albumAll.pictures.get(0).path;
            }
            albumList.add(INDEX_ALL_PHOTOS, albumAll);
            if (resultCallback != null) {
                resultCallback.onResultCallback(albumList);
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    }


    public interface AlbumsResultCallback {
        void onResultCallback(List<ZYAlbum> albums);
    }
}
