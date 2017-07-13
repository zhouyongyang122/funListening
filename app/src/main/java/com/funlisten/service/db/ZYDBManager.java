package com.funlisten.service.db;

import android.database.sqlite.SQLiteDatabase;

import com.funlisten.ZYApplication;
import com.funlisten.service.db.entity.DaoMaster;
import com.funlisten.service.db.entity.DaoSession;

/**
 * Created by ZY on 17/3/17.
 */

public class ZYDBManager {

    private static ZYDBManager instance;

    private final static String dbName = "SmartReader";

    private DaoMaster.DevOpenHelper openHelper;

    private ZYDBManager() {
        initDB();
    }

    public static ZYDBManager getInstance() {
        if (instance == null) {
            synchronized (ZYDBManager.class) {
                if (instance == null) {
                    instance = new ZYDBManager();
                }
            }
        }
        return instance;
    }

    private void initDB() {
        if (openHelper == null) {
            openHelper = new DaoMaster.DevOpenHelper(ZYApplication.getInstance(), dbName, null);
        }
    }

    public DaoSession getReadableDaoSession(){
        DaoMaster daoMaster = new DaoMaster(ZYDBManager.getInstance().getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        return daoSession;
    }

    public DaoSession getWritableDaoSession(){
        DaoMaster daoMaster = new DaoMaster(ZYDBManager.getInstance().getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        return daoSession;
    }

    public SQLiteDatabase getReadableDatabase() {
        initDB();
        SQLiteDatabase db = openHelper.getReadableDatabase();
        return db;
    }

    public SQLiteDatabase getWritableDatabase() {
        initDB();
        SQLiteDatabase db = openHelper.getWritableDatabase();
        return db;
    }
}
