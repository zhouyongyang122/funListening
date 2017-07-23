package com.funlisten.business.search.model;

import com.funlisten.business.search.model.bean.ZYSearchHistory;
import com.funlisten.service.db.ZYDBManager;

import java.util.List;

/**
 * Created by gd on 2017/7/19.
 */

public class ZYSearchHistoryManager {

    private static ZYSearchHistoryManager instance;
    private ZYSearchHistory searchHistory = new ZYSearchHistory();
    public static ZYSearchHistoryManager getInsatnce(){
        if(instance == null){
            instance = new ZYSearchHistoryManager();
        }
        return instance;
    }


    public void save(ZYSearchHistory history){
        if(history == null )return;
        updateHistory(history);
        searchHistory.save();
    }

    public void deleteHistory(){
        searchHistory.delete();
    }
    public List<ZYSearchHistory> getAllHistory(){
        return searchHistory.getAllHistory();
    }

    private void updateHistory(ZYSearchHistory history){
        if(history != null){
            this.searchHistory.userId = history.userId;
            this.searchHistory.history = history.history;
        }

    }
}
