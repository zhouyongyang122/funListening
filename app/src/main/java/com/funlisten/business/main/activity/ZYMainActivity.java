package com.funlisten.business.main.activity;

import android.animation.Animator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.funlisten.R;
import com.funlisten.ZYApplication;
import com.funlisten.base.adapter.ZYFragmentAdapter;
import com.funlisten.base.mvp.ZYBaseActivity;
import com.funlisten.business.login.model.ZYUserManager;
import com.funlisten.business.login.model.bean.ZYUser;
import com.funlisten.business.main.presenter.ZYMePresenter;
import com.funlisten.business.play.activity.ZYPlayActivity;
import com.funlisten.business.play.model.FZAudionPlayEvent;
import com.funlisten.business.play.model.ZYPlayManager;
import com.funlisten.business.play.model.bean.ZYPlayHistory;
import com.funlisten.service.ZYUpdateService;
import com.funlisten.business.main.contract.ZYMainContract;
import com.funlisten.business.main.model.bean.ZYVersion;
import com.funlisten.business.main.presenter.ZYHomePresenter;
import com.funlisten.business.main.presenter.ZYMainPresenter;
import com.funlisten.business.main.view.ZYHomeFragment;
import com.funlisten.business.main.view.ZYMeFragment;
import com.funlisten.thirdParty.image.ZYImageLoadHelper;
import com.funlisten.utils.ZYStatusBarUtils;
import com.funlisten.utils.ZYToast;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.OnClick;
import pl.com.salsoft.sqlitestudioremote.SQLiteStudioService;

import static com.funlisten.business.play.model.ZYPlayManager.STATE_BUFFERING_END;
import static com.funlisten.business.play.model.ZYPlayManager.STATE_BUFFERING_START;
import static com.funlisten.business.play.model.ZYPlayManager.STATE_COMPLETED;
import static com.funlisten.business.play.model.ZYPlayManager.STATE_ERROR;
import static com.funlisten.business.play.model.ZYPlayManager.STATE_NEED_BUY_PAUSED;
import static com.funlisten.business.play.model.ZYPlayManager.STATE_PAUSED;
import static com.funlisten.business.play.model.ZYPlayManager.STATE_PLAYING;
import static com.funlisten.business.play.model.ZYPlayManager.STATE_PREPARED;
import static com.funlisten.business.play.model.ZYPlayManager.STATE_PREPARING;
import static com.funlisten.business.play.model.ZYPlayManager.STATE_PREPARING_NEXT;

/**
 * Created by ZY on 17/4/27.
 */

public class ZYMainActivity extends ZYBaseActivity<ZYMainContract.IPresenter> implements ZYMainContract.IView {

    public static final int MAIN_HOME_INDEX = 0;

    public static final int MAIN_ME_INDEX = 1;

    @Bind(R.id.mainViewPager)
    ViewPager mainViewPager;

    @Bind(R.id.homeImg)
    ImageView homeImg;

    @Bind(R.id.homeName)
    TextView homeName;

    @Bind(R.id.meImg)
    ImageView meImg;

    @Bind(R.id.meName)
    TextView meName;

    ZYHomeFragment homeFragment;

    ZYMeFragment meFragment;

    ZYFragmentAdapter fragmentAdapter;

    private int mCurrentPage = -1;

    @Bind(R.id.layoutPlayer)
    RelativeLayout layoutPlayer;

    @Bind(R.id.imgAvatar)
    ImageView imgAvatar;

    @Bind(R.id.imgPlayer)
    ImageView imgPlayer;

    Animation playAnima;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.zy_activity_main);

        setPresenter(new ZYMainPresenter(this));

        initView();
        ZYStatusBarUtils.immersiveStatusBar(this, 1);
        if (ZYStatusBarUtils.isCanLightStatusBar()) {
            ZYStatusBarUtils.tintStatusBar(this, Color.TRANSPARENT, 0);
        }

        SQLiteStudioService.instance().start(this);

        playAnima = AnimationUtils.loadAnimation(mActivity, R.anim.play_rotate);
    }

    private void initView() {
        hideActionBar();
        fragmentAdapter = new ZYFragmentAdapter(getSupportFragmentManager());
        homeFragment = new ZYHomeFragment();
        new ZYHomePresenter(homeFragment);
        meFragment = new ZYMeFragment();
        new ZYMePresenter(meFragment);
        fragmentAdapter.addFragment(homeFragment, "");
        fragmentAdapter.addFragment(meFragment, "");

        mainViewPager.setOffscreenPageLimit(1);
        mainViewPager.setCurrentItem(0);
        mainViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                changeFragment(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mainViewPager.setAdapter(fragmentAdapter);
        changeFragment(0);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
    }

    @OnClick({R.id.homeBtn, R.id.meBtn, R.id.layoutPlayer})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.homeBtn:
                mainViewPager.setCurrentItem(0);
                break;
            case R.id.meBtn:
                mainViewPager.setCurrentItem(1);
                break;
            case R.id.layoutPlayer:
//                if (ZYUserManager.getInstance().isGuesterUser(true)) {
//                    return;
//                }
                ZYPlayHistory history = ZYPlayManager.getInstance().queryLastPlay();
                if (history != null) {
                    ZYPlayActivity.toPlayActivity(mActivity, Integer.parseInt(history.albumId), Integer.parseInt(history.audioId));
                } else {
                    ZYToast.show(mActivity, "您还没有播放过任何音频哦,请先去选择要播放的音频!");
                }
                break;
        }
    }

    private void changeFragment(int position) {
        if (position == mCurrentPage) {
            return;
        }
        mCurrentPage = position;
        if (mCurrentPage == MAIN_HOME_INDEX) {
            homeImg.setSelected(true);
            meImg.setSelected(false);
            homeName.setTextColor(getResources().getColor(R.color.c1));
            meName.setTextColor(getResources().getColor(R.color.c4));
//            setDarkMode(false);
        } else if (mCurrentPage == MAIN_ME_INDEX) {
            homeImg.setSelected(false);
            meImg.setSelected(true);
            homeName.setTextColor(getResources().getColor(R.color.c4));
            meName.setTextColor(getResources().getColor(R.color.c1));
//            setDarkMode(true);
        }
        showTitle(fragmentAdapter.getPageTitle(position).toString());
    }

    @Override
    public void showUpdateView(final ZYVersion version) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("版本更新");
        dialog.setMessage(version.info);
        dialog.setPositiveButton("更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ZYToast.show(mActivity, "正在下载中...");
                startService(ZYUpdateService.createIntent(version.download));
            }
        });
        if (version.keyupdate > 0) {
            //强更
            dialog.setCancelable(false);
        } else {
            dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }
        dialog.create().show();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.getVersion();
        refreshPlay();
    }

    private void refreshPlay() {
        ZYPlayHistory history = ZYPlayManager.getInstance().queryLastPlay();
        if (history != null) {
            ZYImageLoadHelper.getImageLoader().loadCircleImage(this, imgAvatar, history.img, R.drawable.def_avatar, R.drawable.def_avatar);
        }
    }

    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(this).setTitle("退出").setMessage("是否退出听谁说?")
                .setPositiveButton("退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MobclickAgent.onKillProcess(mActivity);
                        ZYPlayManager.getInstance().clearNotification();
                        finish();
                        ZYApplication.getInstance().finisedAllActivities();
                        System.exit(0);
                    }
                })
                .setNegativeButton("再看看", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setNeutralButton("最小化", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                moveTaskToBack(false);
            }
        }).create().show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SQLiteStudioService.instance().stop();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(FZAudionPlayEvent playEvent) {
        try {
            if (playEvent != null) {
                if (playEvent.state == STATE_ERROR) {
                } else if (playEvent.state == STATE_PREPARING) {
                } else if (playEvent.state == STATE_PREPARED) {
                    imgAvatar.clearAnimation();
                    imgAvatar.startAnimation(playAnima);
                } else if (playEvent.state == STATE_PLAYING) {
                } else if (playEvent.state == STATE_PAUSED) {
                    imgAvatar.clearAnimation();
                } else if (playEvent.state == STATE_NEED_BUY_PAUSED) {
                } else if (playEvent.state == STATE_BUFFERING_START) {
                } else if (playEvent.state == STATE_BUFFERING_END) {
                } else if (playEvent.state == STATE_PREPARING_NEXT) {
                } else if (playEvent.state == STATE_COMPLETED) {
                    imgAvatar.clearAnimation();
                }
            }
        } catch (Exception e) {

        }
    }
}
