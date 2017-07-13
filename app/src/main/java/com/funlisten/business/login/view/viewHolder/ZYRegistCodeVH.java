package com.funlisten.business.login.view.viewHolder;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.funlisten.R;
import com.funlisten.ZYApplication;
import com.funlisten.base.view.ZYClearEditView;
import com.funlisten.base.viewHolder.ZYBaseViewHolder;
import com.funlisten.business.login.model.bean.ZYRegistUpload;
import com.funlisten.business.login.view.ZYRegistListener;
import com.funlisten.utils.ZYToast;
import com.funlisten.utils.ZYUtils;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by ZY on 17/7/1.
 */

public class ZYRegistCodeVH extends ZYBaseViewHolder<ZYRegistUpload> {

    @Bind(R.id.editCode)
    ZYClearEditView editCode;

    @Bind(R.id.textTip)
    TextView textTip;

    @Bind(R.id.textReSend)
    TextView textReSend;

    @Bind(R.id.textNext)
    TextView textNext;

    ZYRegistUpload mData;

    ZYRegistListener mListener;

    Timer timer;
    int maxtTime = 60;
    int curTime = 0;

    public ZYRegistCodeVH(ZYRegistListener listener) {
        mListener = listener;
    }

    @Override
    public void findView(View view) {
        super.findView(view);
        mItemView.setVisibility(View.GONE);
    }

    @Override
    public void updateView(ZYRegistUpload data, int position) {
        mData = data;
        editCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String code = editCode.getText().toString();
                if (!TextUtils.isEmpty(code)) {
                    textNext.setSelected(true);
                } else {
                    textNext.setSelected(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        if (!TextUtils.isEmpty(mData.code)) {
            editCode.setText(mData.code);
        }
    }

    public void attachTo(ViewGroup viewGroup) {
        bindView(LayoutInflater.from(viewGroup.getContext()).inflate(getLayoutResId(), viewGroup, false));
        viewGroup.addView(getItemView());
    }

    @OnClick({R.id.textNext, R.id.textReSend})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textNext:
                String code = editCode.getText().toString();
                if (TextUtils.isEmpty(code)) {
                    ZYToast.show(mContext, "验证码不能为空!");
                    return;
                }
                mData.code = code;
                mListener.completeCode();
                cancleTimer();
                ZYUtils.hideInput(editCode);
                break;
            case R.id.textReSend:
                mListener.sendCode();
                startTimer();
                break;
        }
    }

    private void startTimer() {
        textReSend.setEnabled(false);
        curTime = 0;
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                ZYApplication.getInstance().getCurrentActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        curTime++;
                        if (curTime >= 60) {
                            cancleTimer();
                        } else {
                            textReSend.setText((maxtTime - curTime) + "S");
                        }
                    }
                });
            }
        },0,1000);
    }

    private void cancleTimer() {
        try {
            curTime = 0;
            textReSend.setText("重新发送");
            textReSend.setEnabled(true);
            timer.cancel();
            timer = null;
        } catch (Exception e) {

        }
    }

    public void codeSuc() {
        startTimer();
    }

    public boolean isVisible() {
        return mItemView.getVisibility() == View.VISIBLE;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.zy_view_regist_code;
    }

    public void show() {
        mItemView.setVisibility(View.VISIBLE);
        if (TextUtils.isEmpty(mData.code)) {
            mListener.sendCode();
            textTip.setText("已向手机号 " + mData.phone + " 发送短信验证码");
        }
    }

    public void hide() {
        mItemView.setVisibility(View.GONE);
    }
}
