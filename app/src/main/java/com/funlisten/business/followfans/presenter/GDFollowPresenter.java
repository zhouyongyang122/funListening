package com.funlisten.business.followfans.presenter;

import com.funlisten.base.bean.ZYListResponse;
import com.funlisten.base.bean.ZYResponse;
import com.funlisten.base.mvp.ZYListDataPresenter;
import com.funlisten.business.followfans.contract.GDFollowContract;
import com.funlisten.business.followfans.model.GDFollowModel;
import com.funlisten.business.login.model.bean.ZYUser;
import com.funlisten.business.user.model.ZYUserList;
import com.funlisten.service.net.ZYNetSubscriber;
import com.funlisten.service.net.ZYNetSubscription;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/12.
 */

public class GDFollowPresenter extends ZYListDataPresenter<GDFollowContract.IView,GDFollowModel,ZYUserList> implements GDFollowContract.IPresenter {
    private String userId;
    public GDFollowPresenter(GDFollowContract.IView view, GDFollowModel model,String userId) {
        super(view, model);
        this.userId = userId;
    }

    @Override
    protected void loadData() {
        mSubscriptions.add(ZYNetSubscription.subscription(mModel.follows(userId,mPageIndex,mRows),new ZYNetSubscriber<ZYResponse<ZYListResponse<ZYUserList>>>(){
            @Override
            public void onSuccess(ZYResponse<ZYListResponse<ZYUserList>> response) {
                List<ZYUserList> lists = new ArrayList<>();
                ZYUser user1 =   new ZYUser();
                user1.setAlbumCount(20);
                user1.setAvatarUrl("http://img4q.duitang.com/uploads/item/201411/05/20141105003804_hQFiB.jpeg");
                user1.setNickname("杨幂");
                user1.setFans(40);
                ZYUserList userList1 = new ZYUserList();
                userList1.user = user1;
                lists.add(userList1);

                ZYUser user2 =   new ZYUser();
                user2.setAlbumCount(30);
                user2.setAvatarUrl("http://img4q.duitang.com/uploads/item/201411/23/20141123072535_28aMM.jpeg");
                user2.setNickname("杨幂1");
                user2.setFans(50);
                ZYUserList userList2 = new ZYUserList();
                userList2.user = user2;
                lists.add(userList2);

                ZYUser user3 =   new ZYUser();
                user3.setAlbumCount(20);
                user3.setAvatarUrl("http://img4.duitang.com/uploads/item/201504/25/20150425H4628_NHPX8.jpeg");
                user3.setNickname("杨幂2");
                user3.setFans(40);
                ZYUserList userList3 = new ZYUserList();
                userList3.user = user3;
                lists.add(userList3);


                ZYUser user4 =   new ZYUser();
                user4.setAlbumCount(40);
                user4.setAvatarUrl("http://img4q.duitang.com/uploads/item/201506/12/20150612142309_Hu4JF.jpeg");
                user4.setNickname("杨幂4");
                user4.setFans(90);
                ZYUserList userList4 = new ZYUserList();
                userList4.user = user4;
                lists.add(userList4);

                ZYListResponse response1 =  new ZYListResponse();
                response1.data = lists;
                response.data = response1;
                success(response);
            }

            @Override
            public void onFail(String message) {
              fail(message);
            }
        }));
    }
    public void follow(){

    }
}
