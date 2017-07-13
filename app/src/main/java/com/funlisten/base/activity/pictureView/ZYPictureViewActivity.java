package com.funlisten.base.activity.pictureView;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.funlisten.R;
import com.funlisten.base.mvp.ZYBaseActivity;
import com.funlisten.thirdParty.image.ZYImageLoadHelper;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import uk.co.senab.photoview.PhotoView;

public class ZYPictureViewActivity extends ZYBaseActivity {

    @Bind(R.id.view_pager)
    ViewPager mViewPager;

    private ZYIOperation mOperation;

    private List<String> mPicturePathList;

    private boolean mIsShowIndex = true;
    private int mIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sr_activity_picture_view);
        ButterKnife.bind(this);

        Bundle bundle = getIntent().getExtras();
        mPicturePathList = bundle.getStringArrayList(ZYPictureViewer.KEY_PICTURE_PATHS);
        mIndex = bundle.getInt(ZYPictureViewer.KEY_INDEX, 0);
        mIsShowIndex = bundle.getBoolean(ZYPictureViewer.KEY_IS_SHOW_INDEX, true);
        mOperation = (ZYIOperation) bundle.getSerializable(ZYPictureViewer.KEY_OPERATION);

        if (mPicturePathList != null) {
            showPictureList();
        } else {
            showError();
        }
    }

    private void showPictureList() {
        setIndex(mIndex + 1);
        PreviewAdapter adapter = new PreviewAdapter();
        mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setIndex(position + 1);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mViewPager.setCurrentItem(mIndex);
    }

    private void setIndex(int index) {
        if (mIsShowIndex) {
            mActionBar.showTitle(getString(R.string.picture_preview_index, index, mPicturePathList.size()));
        } else {
            mActionBar.showTitle("图片预览");
        }

    }

    private class PreviewAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mPicturePathList == null ? 0 : mPicturePathList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            PhotoView photoView = new PhotoView(container.getContext());
            final String path = mPicturePathList.get(position);
            if (!TextUtils.isEmpty(path)) {
                ZYImageLoadHelper.getImageLoader().setFitType(ZYImageLoadHelper.FIT_CENTER).loadImage(this, photoView, path);
            }
            container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);

            photoView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (mOperation != null) {
                        mOperation.showOperation(ZYPictureViewActivity.this, path);
                    }
                    return false;
                }
            });

            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

}
