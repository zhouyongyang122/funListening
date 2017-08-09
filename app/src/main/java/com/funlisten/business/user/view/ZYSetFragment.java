package com.funlisten.business.user.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.funlisten.R;
import com.funlisten.ZYApplication;
import com.funlisten.ZYPreferenceHelper;
import com.funlisten.base.event.ZYEventLoginOutSuc;
import com.funlisten.base.mvp.ZYBaseFragment;
import com.funlisten.base.view.SlipButton;
import com.funlisten.base.view.ZYSlipButton;
import com.funlisten.business.login.model.ZYUserManager;
import com.funlisten.business.persondata.activity.ZYPersonDataActivity;
import com.funlisten.business.user.activity.ZYAboutActivity;
import com.funlisten.utils.ZYFileUtils;
import com.funlisten.utils.ZYSystemUtils;
import com.funlisten.utils.ZYToast;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.tencent.open.utils.Global.getPackageName;

/**
 * Created by ZY on 17/7/21.
 */

public class ZYSetFragment extends ZYBaseFragment {

    @Bind(R.id.textCacheSize)
    TextView textCacheSize;

    @Bind(R.id.layoutMsg)
    LinearLayout layoutMsg;

    @Bind(R.id.sbMsg)
    ZYSlipButton sbMsg;

    @Bind(R.id.sbPlay)
    ZYSlipButton sbPlay;

    @Bind(R.id.textVerson)
    TextView textVerson;

    @Bind(R.id.layoutLoginOut)
    LinearLayout layoutLoginOut;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fz_fragment_set, container, false);
        ButterKnife.bind(this, view);
        refreshSize();

        textVerson.setText("V" + ZYSystemUtils.getAppVersionName(mActivity));

        if (ZYUserManager.getInstance().isGuesterUser(false)) {
            layoutLoginOut.setVisibility(View.GONE);
            layoutMsg.setVisibility(View.GONE);
        } else {
            sbMsg.setSelectState(ZYPreferenceHelper.getInstance().isOpenMsg());
            sbMsg.setChangeListener(new SlipButton.OnSelectChangeListener() {
                @Override
                public void onSelectChanged(boolean isSelect, View v) {
                    ZYPreferenceHelper.getInstance().saveOpenMsg(isSelect);
                }
            });
        }

        sbPlay.setSelectState(ZYPreferenceHelper.getInstance().isOpen4GPlay());
        sbPlay.setChangeListener(new SlipButton.OnSelectChangeListener() {
            @Override
            public void onSelectChanged(boolean isSelect, View v) {
                ZYPreferenceHelper.getInstance().saveOpen4GPlay(isSelect);
            }
        });

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

    @OnClick({R.id.layoutUserInfo, R.id.layoutCache, R.id.layoutSuport, R.id.layoutAbout, R.id.layoutLoginOut})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layoutUserInfo:
                if (ZYUserManager.getInstance().isGuesterUser(true)) {
                    return;
                }
                mActivity.startActivity(new Intent(mActivity, ZYPersonDataActivity.class));
                break;
            case R.id.layoutCache:
                ZYToast.show(mActivity, "清理成功!");
                textCacheSize.setText(ZYFileUtils.formatFileSize(0));
                break;
            case R.id.layoutSuport:
                Uri uri = Uri.parse("market://details?id=" + mActivity.getPackageName());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                try {
                    startActivity(intent);
                } catch (Exception e) {
                    ZYToast.show(mActivity, "尚未安装商店类应用！");
                }
                break;
            case R.id.layoutAbout:
                startActivity(ZYAboutActivity.createIntent(mActivity));
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
