package com.funlisten.business.search.contract;

import com.funlisten.base.mvp.ZYListDataContract;
import com.funlisten.business.user.model.ZYUserList;

import java.util.List;

/**
 * Created by Administrator on 2017/7/12.
 */

public interface ZYSearchContract {

    interface IView extends ZYListDataContract.View<IPresenter> {
        void success(List<String> data);
        void showHotWord(List<String> data);
    }

    interface IPresenter extends ZYListDataContract.Presenter<String>{
        void loadHotWord();
        void loadSearch(String type,String keyword);
    }
}
