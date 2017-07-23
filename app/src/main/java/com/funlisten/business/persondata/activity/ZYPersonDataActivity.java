package com.funlisten.business.persondata.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.funlisten.R;
import com.funlisten.base.mvp.ZYBaseActivity;
import com.funlisten.business.login.model.ZYUserManager;
import com.funlisten.business.login.model.bean.ZYUser;
import com.funlisten.thirdParty.image.ZYImageLoadHelper;

import butterknife.Bind;

/**
 * Created by gd on 2017/7/22.
 * 个人资料
 */

public class ZYPersonDataActivity extends ZYBaseActivity {
    @Bind(R.id.head_img)
    ImageView headImg;

    @Bind(R.id.nick_name)
    TextView nickName;

    @Bind(R.id.sex_tv)
    TextView sex;

    @Bind(R.id.age_tv)
    TextView age;

    @Bind(R.id.zone)
    TextView zone;

    @Bind(R.id.synopsis)
    TextView synopsis;

    ZYUser zyUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gd_person_data_layout);
        showTitle("个人资料");
        zyUser = ZYUserManager.getInstance().getUser();
        init();
    }

    private  void init(){
        if(zyUser != null){
            ZYImageLoadHelper.getImageLoader().loadCircleImage(this,headImg,zyUser.avatarUrl,R.drawable.def_avatar,R.drawable.def_avatar);
            nickName.setText(zyUser.nickname);
            sex.setText(zyUser.sex);
        }

    }
}
