package com.funlisten.business.dailylisten.presenter;

import com.funlisten.base.bean.ZYListResponse;
import com.funlisten.base.bean.ZYResponse;
import com.funlisten.base.mvp.ZYBasePresenter;
import com.funlisten.base.mvp.ZYListDataPresenter;
import com.funlisten.business.album.model.ZYAlbumModel;
import com.funlisten.business.album.model.bean.ZYAlbumDetail;
import com.funlisten.business.dailylisten.contract.ZYDailyListenContract;
import com.funlisten.business.dailylisten.model.ZYDailyListenModel;
import com.funlisten.business.dailylisten.model.bean.ZYHeaderInfo;
import com.funlisten.business.dailylisten.model.bean.ZYTimeInfo;
import com.funlisten.business.photo.ZYPhoto;
import com.funlisten.business.play.model.bean.ZYAudio;
import com.funlisten.service.net.ZYNetSubscriber;
import com.funlisten.service.net.ZYNetSubscription;
import com.funlisten.utils.ZYDateUtils;
import com.funlisten.utils.ZYLog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by gd on 2017/7/20.
 */

public class ZYDailyListenPresenter extends ZYBasePresenter implements ZYDailyListenContract.IPresenter {

    String mSortType = ZYAlbumModel.SORT_ASC;
    int mAlbumId;
    ZYDailyListenModel mModel;
    ZYDailyListenContract.IView iView;
    ArrayList<Object> mDatas = new ArrayList<Object>();
    HashMap<String, ArrayList<Object>> hashMap = new HashMap<>();

    public ZYDailyListenPresenter(ZYDailyListenContract.IView view, ZYDailyListenModel model, int albumId) {
        this.mAlbumId = albumId;
        this.mModel = model;
        this.iView = view;
    }

    public void loadData() {
        mSubscriptions.add(ZYNetSubscription.subscription(mModel.getAudios(1, 40, mAlbumId, mSortType), new ZYNetSubscriber<ZYResponse<ZYListResponse<ZYAudio>>>() {
            @Override
            public void onSuccess(ZYResponse<ZYListResponse<ZYAudio>> response) {
                super.onSuccess(response);
                List<ZYAudio> list = response.data.data;
                mDatas.add(new ZYHeaderInfo(response.data.totalCount));
                Collections.sort(list,new TimeComparator());
                sortTime(list);
                iView.showDatas(mDatas);
            }

            @Override
            public void onFail(String message) {
                onFail(message);
            }
        }));
    }

    @Override
    public void getAlbumDetail(final ZYAudio audio){
        mSubscriptions.add(ZYNetSubscription.subscription(mModel.getAlbumDetail(audio.albumId), new ZYNetSubscriber<ZYResponse<ZYAlbumDetail>>() {
            @Override
            public void onSuccess(ZYResponse<ZYAlbumDetail> response) {
                iView.loadAudio(response.data,audio);
                super.onSuccess(response);
            }

            @Override
            public void onFail(String message) {
                super.onFail(message);
            }
        }));
    }

    private void sortTime(List<ZYAudio> list) {
        if( list.size() <= 0) return;
        String tempStr="";
        for (int i= 0;i< list.size();i++) {
           String time = getTodayOrYesterday(list.get(i).gmtCreate);
            if(tempStr.equals(time)){
                mDatas.add(list.get(i));
            }else {
                tempStr = time;
                mDatas.add(new ZYTimeInfo(tempStr));
                mDatas.add(list.get(i));
            }

        }
    }

    @Override
    public void refresh() {

    }

    @Override
    public void loadMore() {

    }

    @Override
    public ArrayList<Object> getDataList() {
        return mDatas;
    }

    public static String getTodayOrYesterday(String time) {

        //所在时区时8，系统初始时间是1970-01-01 80:00:00，注意是从八点开始，计算的时候要加回去
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date datetest = null;
        try {
            datetest = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long date = datetest.getTime();
        int offSet = Calendar.getInstance().getTimeZone().getRawOffset();
        long today = (System.currentTimeMillis() + offSet) / 86400000;
        long start = (date + offSet) / 86400000;
        long intervalTime = start - today;
        //-2:前天,-1：昨天,0：今天,1：明天,2：后天
        String strDes = "";
        if (intervalTime == 0) {
            strDes = "今天";//今天
        } else if (intervalTime == -1) {
            strDes = "昨天";
        } else {
            strDes = ZYDateUtils.getTimeString(time, ZYDateUtils.YYMMDDHHMM24, ZYDateUtils.YYMMDDHH);
        }
        return strDes;
    }
}

class TimeComparator implements Comparator {

    @Override
    public int compare(Object lhs, Object rhs) {
        ZYAudio zyAudio = (ZYAudio) lhs;
        ZYAudio zyAudios = (ZYAudio) rhs;

        return zyAudios.gmtCreate.compareTo(zyAudio.gmtCreate);
    }
}
