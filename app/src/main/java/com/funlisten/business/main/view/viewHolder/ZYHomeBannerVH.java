package com.funlisten.business.main.view.viewHolder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.funlisten.R;
import com.funlisten.base.viewHolder.ZYBaseViewHolder;
import com.funlisten.thirdParty.image.ZYImageLoadHelper;
import com.funlisten.business.main.model.bean.ZYHome;
import com.funlisten.utils.ZYScreenUtils;

import java.util.List;

import butterknife.Bind;

/**
 * Created by ZY on 17/5/22.
 */

public class ZYHomeBannerVH extends ZYBaseViewHolder<List<ZYHome.Banner>> {

    @Bind(R.id.banner)
    ConvenientBanner<ZYHome.Banner> mBanner;

    private View.OnTouchListener mOnTouchListener;
    private OnHomeBannerListener mOnHomeBannerListener;

    private List<ZYHome.Banner> mBannerList;

    public ZYHomeBannerVH(@NonNull View.OnTouchListener onTouchListener,
                          @NonNull OnHomeBannerListener onHomeBannerListener) {
        mOnTouchListener = onTouchListener;
        mOnHomeBannerListener = onHomeBannerListener;
    }

    @Override
    public void findView(View view) {
        super.findView(view);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mBanner.getLayoutParams();
        params.height = ZYScreenUtils.getScreenWidth(mContext) * 175 / 374;
        mBanner.setLayoutParams(params);
    }

    @Override
    public void updateView(List<ZYHome.Banner> data, int position) {
        if (data != null && !data.isEmpty()) {
            mBannerList = data;
            mItemView.setVisibility(View.VISIBLE);
            mBanner.setPages(new CBViewHolderCreator<ImageHolderView>() {
                @Override
                public ImageHolderView createHolder() {
                    return new ImageHolderView();
                }
            }, mBannerList)
                    .setPageIndicator(new int[]{R.drawable.sr_banner_indicator_normal, R.drawable.sr_banner_indicator_selected})
                    .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_LEFT)
                    .startTurning(3000);
            mBanner.getViewPager().setOnTouchListener(mOnTouchListener);
        } else {
            mItemView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.zy_view_home_banner;
    }

    public interface OnHomeBannerListener {

        void onBanner(ZYHome.Banner slider);
    }

    public class ImageHolderView implements Holder<ZYHome.Banner> {
        private ImageView imageView;
        private View view;

        @Override
        public View createView(Context context) {
            FrameLayout frameLayout = new FrameLayout(context);

            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);

            TypedValue value = new TypedValue();
            context.getTheme().resolveAttribute(android.R.attr.selectableItemBackground, value, true);
            view = new View(context);
            view.setLayoutParams(new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            view.setBackgroundResource(value.resourceId);
            view.setClickable(true);

            frameLayout.addView(imageView);
            frameLayout.addView(view);

            return frameLayout;
        }

        @Override
        public void UpdateUI(Context context, final int position, final ZYHome.Banner data) {
            ZYImageLoadHelper.getImageLoader().loadImage(context, imageView, data.imageFileUrl);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnHomeBannerListener.onBanner(mBannerList.get(position));
                }
            });
        }
    }
}
