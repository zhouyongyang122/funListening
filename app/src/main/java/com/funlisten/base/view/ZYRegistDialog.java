package com.funlisten.base.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.funlisten.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/8/24.
 */

public class ZYRegistDialog extends Dialog {
    Context mContext;

    @Bind(R.id.cancel)
    TextView canle;

    @Bind(R.id.finish)
    TextView finish;

    CancelAndFinishListener listener;

    public ZYRegistDialog(@NonNull Context context,CancelAndFinishListener listener) {
        super(context, R.style.SRDialogStyle);
        this.mContext = context;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = LayoutInflater.from(mContext).inflate(
                R.layout.gd_regist_dialog_layout, null);
        setContentView(view);
        ButterKnife.bind(this,view);
    }

    @OnClick({R.id.cancel,R.id.finish})
    public void OnClick(View view){
        switch (view.getId()){
            case R.id.cancel:
                dismiss();
                listener.onCancel();
                break;
            case R.id.finish:
                dismiss();
                listener.onFinish();
                break;
        }
    }

    @Override
    public void dismiss() {
        ButterKnife.unbind(this);
        super.dismiss();
    }

    public interface CancelAndFinishListener{
        public void onCancel();
        public void onFinish();
    }
}
