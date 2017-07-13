package com.funlisten.base.activity.picturePicker;

import android.animation.Animator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.funlisten.R;
import com.funlisten.base.activity.pictureView.ZYPictureViewer;
import com.funlisten.base.adapter.ZYCommonAdapter;
import com.funlisten.base.viewHolder.ZYBaseViewHolder;
import com.funlisten.utils.ZYToast;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ZYPicturePickerFragment extends Fragment implements ZYPicturePickerContract.View,
        ZYPictureVH.OnPictureSelectListener {

    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.gv_pictures)
    GridView mGvPictures;
    @Bind(R.id.tv_album)
    TextView mTvAlbum;
    @Bind(R.id.tv_preview)
    TextView mTvPreview;
    @Bind(R.id.view_mark)
    View mViewMark;
    @Bind(R.id.lv_album)
    ListView mLvAlbum;
    @Bind(R.id.layout_bottom)
    View mLayoutBottom;
    @Bind(R.id.tv_send)
    TextView mTvSend;

    private ZYCommonAdapter<ZYAlbum> mAlbumAdapter;
    private ZYCommonAdapter<ZYPicture> mPictureAdapter;

    private ZYPicturePickerContract.Presenter mPresenter;
    private FragmentActivity mActivity;

    private boolean mIsOpenAlbum;
    private int mLvAlbumHeight;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.sr_fragment_picture_picker, container, false);
        ButterKnife.bind(this, rootView);

        mActivity = getActivity();

        mPictureAdapter = new ZYCommonAdapter<ZYPicture>() {
            @Override
            public ZYBaseViewHolder<ZYPicture> createViewHolder(int type) {
                return new ZYPictureVH(ZYPicturePickerFragment.this, mPresenter.isSingle());
            }
        };
        mGvPictures.setAdapter(mPictureAdapter);

        mAlbumAdapter = new ZYCommonAdapter<ZYAlbum>() {
            @Override
            public ZYBaseViewHolder<ZYAlbum> createViewHolder(int type) {
                return new ZYAlbumVH();
            }
        };
        mLvAlbum.setAdapter(mAlbumAdapter);

        mLvAlbum.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mPresenter.changeAlbum(position);
                showAlbumView(false);
            }
        });

        mGvPictures.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mPresenter.isSingle()) {
                    mPresenter.selectPicture(true, position);
                } else {
                    ZYPictureViewer.create().withData(mPresenter.getAllPictureList())
                            .withIndex(position)
                            .start(mActivity);
                }
            }
        });

        if (mPresenter.isSingle()) {
            mTvSend.setVisibility(View.GONE);
            mTvPreview.setVisibility(View.GONE);
        }

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.getData(mActivity);
    }

    @Override
    public void setPresenter(ZYPicturePickerContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showPictureList(List<ZYPicture> pictureList) {
        mPictureAdapter.setDatas(pictureList);
    }

    @Override
    public void showAlbumList(List<ZYAlbum> albumList, String albumName) {
        mAlbumAdapter.setDatas(albumList);
        mTvAlbum.setText(albumName);
        mTvTitle.setText(albumName);
    }

    @Override
    public void showEmpty() {

    }

    @Override
    public void showAlbumView(boolean isShow) {
        if (isShow) {
            final ViewTreeObserver vto = mLvAlbum.getViewTreeObserver();
            if (mLvAlbumHeight == 0) {
                mLvAlbum.setVisibility(View.VISIBLE);
                mLvAlbum.setAlpha(0);
                vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    public boolean onPreDraw() {
                        vto.removeOnPreDrawListener(this);
                        mLvAlbumHeight = mLvAlbum.getMeasuredHeight();
                        mLvAlbum.setY(mLayoutBottom.getTop());
                        mLvAlbum.animate().yBy(-mLvAlbumHeight).alpha(1)
                                .setInterpolator(new DecelerateInterpolator()).setDuration(300).setListener(null).start();
                        return true;
                    }
                });
            } else {
                mLvAlbum.setVisibility(View.VISIBLE);
                mLvAlbum.setAlpha(0);
                mLvAlbum.setY(mLayoutBottom.getTop());
                mLvAlbum.animate().yBy(-mLvAlbumHeight).alpha(1)
                        .setInterpolator(new DecelerateInterpolator()).setDuration(300).setListener(null).start();
            }
            mViewMark.setVisibility(View.VISIBLE);
            mViewMark.animate().alpha(0.8f).setDuration(300).start();
            mIsOpenAlbum = true;
        } else {
            mLvAlbum.animate().yBy(mLvAlbumHeight).alpha(0)
                    .setInterpolator(new DecelerateInterpolator()).setDuration(300).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    mLvAlbum.setVisibility(View.GONE);
                    mViewMark.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            }).start();
            mViewMark.animate().alpha(0).setDuration(300).start();

            mIsOpenAlbum = false;
        }
    }

    @Override
    public void showMaxSelectTip(int max, int position) {
        ZYToast.show(mActivity, getString(R.string.picker_select_max, max));
        mPictureAdapter.notifyDataSetChanged();
    }

    @Override
    public void setPreviewCount(int count, int max) {
        if (count > 0) {
            mTvPreview.setText(getString(R.string.picker_preview_count, count));
            mTvPreview.setEnabled(true);
            mTvSend.setText(getString(R.string.picker_send_count, count, max));
            mTvSend.setEnabled(true);
        } else {
            mTvPreview.setText(R.string.picker_preview);
            mTvSend.setText(R.string.picker_send);
            mTvSend.setEnabled(false);
            mTvPreview.setEnabled(false);
        }
    }

    @Override
    public void pickFinish(Intent intent) {
        mActivity.setResult(Activity.RESULT_OK, intent);
        mActivity.finish();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.layout_album, R.id.tv_preview, R.id.img_back, R.id.tv_send, R.id.layout_bottom})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                mActivity.finish();
                break;
            case R.id.tv_send:
                Intent intent = new Intent();
                intent.putExtra(ZYPicker.KEY_SELECTED_PICTURES, mPresenter.getSelectedPathList());
                pickFinish(intent);
                break;
            case R.id.layout_album:
                showAlbumView(!mIsOpenAlbum);
                break;
            case R.id.tv_preview:
                ZYPictureViewer.create().withData(mPresenter.getSelectedPathList()).start(mActivity);
                break;
        }
    }

    @Override
    public void onSelect(boolean isSelect, int position) {
        mPresenter.selectPicture(isSelect, position);
    }
}
