package com.funlisten.business.user.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.funlisten.R;
import com.funlisten.ZYApplication;
import com.funlisten.base.event.ZYEventLoginOutSuc;
import com.funlisten.base.mvp.ZYBaseFragment;
import com.funlisten.base.view.ZYSlipButton;
import com.funlisten.business.login.model.ZYUserManager;
import com.funlisten.business.persondata.activity.ZYPersonDataActivity;
import com.funlisten.utils.ZYFileUtils;
import com.funlisten.utils.ZYToast;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ZY on 17/7/21.
 */

public class ZYSetFragment extends ZYBaseFragment {

    @Bind(R.id.textCacheSize)
    TextView textCacheSize;

    @Bind(R.id.sbMsg)
    ZYSlipButton sbMsg;

    @Bind(R.id.sbPlay)
    ZYSlipButton sbPlay;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fz_fragment_set, container, false);
        ButterKnife.bind(this, view);
        refreshSize();

        return view;
    }

    void refreshSize() {
        try {
            long size = ZYFileUtils.getDirWholeFileSize(new File(ZYApplication.IMG_CACHE_DIR));
            textCacheSize.setText(ZYFileUtils.formatFileSize(size));
        } catch (Exception e) {
            e.printStackTrace();
        }
        textCacheSize.setText(ZYFileUtils.formatFileSize(0));
    }

    @OnClick({R.id.layoutUserInfo, R.id.layoutNet, R.id.layoutMsg, R.id.layoutCache, R.id.layoutSuport, R.id.layoutAbout, R.id.layoutVerson, R.id.layoutLoginOut})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layoutUserInfo:
                mActivity.startActivity(new Intent(mActivity, ZYPersonDataActivity.class));
                break;
            case R.id.layoutNet:
                break;
            case R.id.layoutMsg:
                break;
            case R.id.layoutCache:
                ZYToast.show(mActivity, "清理成功!");
                textCacheSize.setText(ZYFileUtils.formatFileSize(0));
                break;
            case R.id.layoutSuport:
                break;
            case R.id.layoutAbout:
                break;
            case R.id.layoutVerson:
                break;
            case R.id.layoutLoginOut:
                new AlertDialog.Builder(mActivity).setTitle("退出").setMessage("是否退出登录?")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ZYUserManager.getInstance().loginOut();
                                EventBus.getDefault().post(new ZYEventLoginOutSuc());
                                finish();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).create().show();
                break;
        }
    }

}
