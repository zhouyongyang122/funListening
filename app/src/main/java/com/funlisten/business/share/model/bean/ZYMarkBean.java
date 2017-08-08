package com.funlisten.business.share.model.bean;

import com.funlisten.service.db.entity.ZYBaseEntity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by gd on 2017/7/29.
 */

@Entity
public class ZYMarkBean extends ZYBaseEntity {

    @Id
    public String mark_id;//mark_id由  book_id + page_id + track_Id组成

    public int score;

    public long audioTime;

    public String audioPath;

    public String share_url;//分享时 服务器返回的分享路径

    public String show_track_id;//分享时 服务器返回的id

    @Generated(hash = 1491740978)
    public ZYMarkBean(String mark_id, int score, long audioTime, String audioPath,
            String share_url, String show_track_id) {
        this.mark_id = mark_id;
        this.score = score;
        this.audioTime = audioTime;
        this.audioPath = audioPath;
        this.share_url = share_url;
        this.show_track_id = show_track_id;
    }

    @Generated(hash = 1275851608)
    public ZYMarkBean() {
    }

    @Override
    public long save() {
        return 0;
    }

    @Override
    public long update() {
        return 0;
    }

    @Override
    public void delete() {

    }

    @Override
    public long update(boolean needInsert) {
        return 0;
    }

    public String getMark_id() {
        return this.mark_id;
    }

    public void setMark_id(String mark_id) {
        this.mark_id = mark_id;
    }

    public int getScore() {
        return this.score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public long getAudioTime() {
        return this.audioTime;
    }

    public void setAudioTime(long audioTime) {
        this.audioTime = audioTime;
    }

    public String getAudioPath() {
        return this.audioPath;
    }

    public void setAudioPath(String audioPath) {
        this.audioPath = audioPath;
    }

    public String getShare_url() {
        return this.share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }

    public String getShow_track_id() {
        return this.show_track_id;
    }

    public void setShow_track_id(String show_track_id) {
        this.show_track_id = show_track_id;
    }
}
