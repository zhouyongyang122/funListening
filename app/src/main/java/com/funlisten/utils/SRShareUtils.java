package com.funlisten.utils;

import android.app.Activity;
import android.view.MenuItem;
import android.view.View;

import com.funlisten.R;
import com.funlisten.ZYAppConstants;
import com.funlisten.ZYApplication;
import com.kennyc.bottomsheet.BottomSheet;
import com.kennyc.bottomsheet.BottomSheetListener;
import com.third.loginshare.ShareProxy;
import com.third.loginshare.ThirdPartyBase;
import com.third.loginshare.entity.ShareEntity;
import com.third.loginshare.interf.IShareCallBack;

/**
 * Created by ZY on 17/4/14.
 */

public class SRShareUtils {

    public static final String TAG = "SRShareUtils";
    private Activity activity;
    private ShareEntity shareEntity;
    private BottomSheet.Builder builder;

    private OnShareClickListener mOnShareClickListener;

    private int mCurrentShareType;

    public SRShareUtils(Activity activity, ShareEntity shareEntity) {
        this.activity = activity;
        this.shareEntity = shareEntity;
    }

    public void setOnShareClickListener(OnShareClickListener onShareClickListener) {
        mOnShareClickListener = onShareClickListener;
    }

    public void share() {
        View extendView = View.inflate(activity, R.layout.sr_view_share, null);
        int menu = R.menu.sr_menu_share;

        builder = new BottomSheet.Builder(activity);
        final BottomSheet bottomSheet = builder.setSheet(menu)
                .grid()
                .setExtendLayout(extendView)
                .setListener(new BottomSheetListener() {
                    @Override
                    public void onSheetShown() {
                    }

                    @Override
                    public void onSheetItemSelected(MenuItem item) {
                        int type = -1;
                        switch (item.getItemId()) {
                            case R.id.qq:
                                type = ShareProxy.SHARE_TYPE_QQ;
                                break;
                            case R.id.wechat:
                                type = ShareProxy.SHARE_TYPE_WECHAT;
                                break;
                            case R.id.qzone:
                                type = ShareProxy.SHARE_TYPE_QZONE;
                                break;
                            case R.id.friends:
                                type = ShareProxy.SHARE_TYPE_WECHAT_FRIEND;
                                break;
//                            case R.id.weibo:
//                                type = ShareProxy.SHARE_TYPE_WEIBO;
//                                break;
                            default:
                                break;
                        }
                        if (type != -1) {
                            share(type);
                        }
                    }

                    @Override
                    public void onSheetDismissed(int which) {

                    }
                }).create();
        bottomSheet.show();

        extendView.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheet.dismiss();
            }
        });
    }

    private void share(int type) {
        if (mOnShareClickListener != null) {
            mOnShareClickListener.onClick(type);
        }
        mCurrentShareType = type;
        switch (type) {
            case ShareProxy.SHARE_TYPE_QQ:
                ThirdPartyBase.getInstance().initQQ(ZYApplication.getInstance(), ZYAppConstants.TENCENT_APP_ID);
                break;
            case ShareProxy.SHARE_TYPE_QZONE:
                ThirdPartyBase.getInstance().initQQ(ZYApplication.getInstance(), ZYAppConstants.TENCENT_APP_ID);
                break;
            case ShareProxy.SHARE_TYPE_WECHAT:
                ThirdPartyBase.getInstance().initWechat(ZYApplication.getInstance(),
                        ZYAppConstants.WECHAT_APP_KEY, ZYAppConstants.WECHAT_APP_SECRET);
                break;
            case ShareProxy.SHARE_TYPE_WECHAT_FRIEND:
                ThirdPartyBase.getInstance().initWechat(ZYApplication.getInstance(),
                        ZYAppConstants.WECHAT_APP_KEY, ZYAppConstants.WECHAT_APP_SECRET);
                break;
            case ShareProxy.SHARE_TYPE_WEIBO:
                ThirdPartyBase.getInstance().initWeibo(ZYApplication.getInstance(),
                        ZYAppConstants.SINA_APP_KEY, ZYAppConstants.SINA_SCOPE, ZYAppConstants.SINA_REDIRECT_URL);
                break;
        }
        ShareProxy.getInstance().share(type, activity, shareEntity, new IShareCallBack() {
            @Override
            public void onShareFailed(int errorCode, String errorMsg) {
                if (builder != null) {
                    builder.show();
                }
                ZYToast.show(activity, "分享失败!");
            }

            @Override
            public void onShareSuccess() {
                ZYToast.show(activity, "分享成功!");
            }

            @Override
            public void onShareCancel() {
                if (builder != null) {
                    builder.show();
                }
                ZYToast.show(activity, "分享取消!");
            }
        });
    }

    public interface OnShareClickListener {
        void onClick(int witch);
    }
}
