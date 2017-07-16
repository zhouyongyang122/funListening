package com.funlisten.business.play.model;

import android.content.SharedPreferences;

import com.funlisten.ZYApplication;
import com.funlisten.business.play.model.bean.ZYAudio;
import com.funlisten.business.play.model.bean.ZYPlay;

/**
 * Created by ZY on 17/7/16.
 */

public class ZYPLayManager {

    private static ZYPLayManager instance;

    public static final String DEF_PRE_NAME = "play_manager_pre_name";

    public static final String LAST_PLAY_AUDIO_ID = "last_play_audio_id";

    public static final String LAST_PLAY_ALBUM_ID = "last_play_album_id";

    public static final String LAST_PLAY_IMG = "last_play_img";

    private SharedPreferences defPre;

    ZYPlay mPlay;

    private ZYPLayManager() {

    }

    public static ZYPLayManager getInstance() {
        if (instance == null) {
            instance = new ZYPLayManager();
        }
        return instance;
    }

    public ZYPlay getPlay() {
        return mPlay;
    }

    public void setPlay(ZYPlay mPlay) {
        this.mPlay = mPlay;
    }

    public void saveLastPlayId(int albumId, int audioId) {
        SharedPreferences.Editor editor = getDefPre().edit();
        editor.putInt(LAST_PLAY_ALBUM_ID, albumId)
                .putInt(LAST_PLAY_AUDIO_ID, audioId).commit();
    }

    public int getLastPlayAudioId() {
        return getDefPre().getInt(LAST_PLAY_AUDIO_ID, 0);
    }

    public int getLastPlayAlbumId() {
        return getDefPre().getInt(LAST_PLAY_ALBUM_ID, 0);
    }

    public void saveLastPlayImg(String img) {
        getDefPre().edit().putString(LAST_PLAY_IMG, img).commit();
    }

    public String getLastPlayImg() {
        return getDefPre().getString(LAST_PLAY_IMG, null);
    }

    public SharedPreferences getDefPre() {
        if (defPre == null) {
            defPre = ZYApplication.getInstance().getSharedPreferences(DEF_PRE_NAME, 0);
        }
        return defPre;
    }
}
