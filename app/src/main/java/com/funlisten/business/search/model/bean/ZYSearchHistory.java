package com.funlisten.business.search.model.bean;

import com.funlisten.service.db.ZYDBManager;
import com.funlisten.service.db.entity.ZYBaseEntity;
import com.funlisten.service.db.entity.ZYSearchHistoryDao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

import java.util.List;

/**
 * Created by gd on 2017/7/19.
 */

@Entity
public class ZYSearchHistory extends ZYBaseEntity {
    public String userId;

    @Id
    public String history;

    @Generated(hash = 845344695)
    public ZYSearchHistory(String userId, String history) {
        this.userId = userId;
        this.history = history;
    }

    @Generated(hash = 1708688087)
    public ZYSearchHistory() {
    }

    @Override
    public long save() {
        ZYSearchHistoryDao dao =  ZYDBManager.getInstance().getWritableDaoSession().getZYSearchHistoryDao();
        return dao.insertOrReplace(this);
    }

    @Override
    public long update() {
        ZYSearchHistoryDao dao =  ZYDBManager.getInstance().getWritableDaoSession().getZYSearchHistoryDao();
        return dao.insertOrReplace(this);
    }

    @Override
    public void delete() {
        ZYSearchHistoryDao dao =  ZYDBManager.getInstance().getWritableDaoSession().getZYSearchHistoryDao();
        dao.deleteAll();
    }

    public List<ZYSearchHistory> getAllHistory(){
        ZYSearchHistoryDao dao =    ZYDBManager.getInstance().getReadableDaoSession().getZYSearchHistoryDao();
        return dao.loadAll();
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getHistory() {
        return this.history;
    }

    public void setHistory(String history) {
        this.history = history;
    }

    @Override
    public long update(boolean needInsert) {
        return 0;
    }
}
