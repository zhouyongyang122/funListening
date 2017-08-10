package com.funlisten.base.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.funlisten.R;
import com.funlisten.utils.ZYResourceUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;

/**
 * Created by ZY on 17/3/22.
 */

public class ZYWheelSelectDialog implements OnWheelChangedListener {

    private Dialog mDialog;

    private Activity mContext;

    @Bind(R.id.layoutSingleSel)
    LinearLayout layoutSingleSel;

    @Bind(R.id.wheelSingle)
    WheelView wheelSingle;

    @Bind(R.id.textTitle)
    TextView textTitle;

    String[] mSingleDatas;
    private int mFontSize = 20;

    private WheelSelectListener mSelectListener;

    public ZYWheelSelectDialog(Activity context, String[] singleDatas, WheelSelectListener selectListener) {
        initDialog(context, singleDatas, selectListener);
    }

    private void initDialog(Activity context, String[] singleDatas, WheelSelectListener selectListener) {
        mContext = context;
        mSingleDatas = singleDatas;
        mSelectListener = selectListener;
        mDialog = new Dialog(mContext, R.style.SRDialogStyle);
        Window dialogWindow = mDialog.getWindow();
        dialogWindow.getAttributes().windowAnimations = R.style.DialogAnimation;
        dialogWindow.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
        mDialog.setCanceledOnTouchOutside(true);
        FrameLayout wrapLayout = new FrameLayout(mContext);
        wrapLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mDialog.dismiss();
            }
        });
        View view = LayoutInflater.from(mContext).inflate(
                R.layout.sr_view_wheel_selecte, null);
        ButterKnife.bind(this, view);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);
        wrapLayout.addView(view, layoutParams);
        mDialog.setContentView(wrapLayout, new FrameLayout.LayoutParams(
                mContext.getResources().getDisplayMetrics().widthPixels, ViewGroup.LayoutParams.WRAP_CONTENT));
        initSingleSelView();
    }

    private void initSingleSelView() {
        layoutSingleSel.setVisibility(View.VISIBLE);
        wheelSingle.setViewAdapter(new StringArrayAdapter(mContext, mSingleDatas));
        wheelSingle.addChangingListener(this);
    }

    public void showDialog(int curPosition) {
        wheelSingle.setCurrentItem(curPosition);
        mDialog.show();
    }

    public void setTitle(String title) {
        if (title == null) {
            return;
        }
        textTitle.setText(title);
    }

    @OnClick({R.id.textOK})
    public void onClick(View view) {
        mDialog.dismiss();
        if (mSelectListener != null) {
            mSelectListener.onWheelSelected(this, wheelSingle.getCurrentItem(), mSingleDatas[wheelSingle.getCurrentItem()]);
        }
    }

    @Override
    public void onChanged(WheelView wheelView, int i, int i1) {

    }

    public void clear() {
        try {
            ButterKnife.unbind(this);
        } catch (Exception e) {

        }
    }

    public void setFontSize(int fontSize) {
        this.mFontSize = fontSize;
    }

    public interface WheelSelectListener {
        void onWheelSelected(ZYWheelSelectDialog dialog, int position, String value);
    }

    private class StringArrayAdapter extends ArrayWheelAdapter<String> {

        public StringArrayAdapter(Context context, String[] items) {
            super(context, items);
            setTextSize(mFontSize);
            setTextColor(ZYResourceUtils.getColor(R.color.c4));
        }
    }

}
