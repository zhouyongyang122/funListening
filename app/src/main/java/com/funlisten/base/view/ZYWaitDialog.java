package com.funlisten.base.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.funlisten.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ZY on 17/3/15.
 */

public class ZYWaitDialog extends Dialog {

    @Bind(R.id.wd_message)
    TextView mMessage;

    public ZYWaitDialog(Context context) {
        super(context, R.style.SRDialogStyle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sr_view_wait_dialog);
        ButterKnife.bind(this, this);
    }

    public void showWidthMessage(final String message) {
        refreshMessage(message);
        show();
    }


    public void refreshMessage(final String message) {
        if (message == null || mMessage == null) {
            return;
        }
        mMessage.post(new Runnable() {
            @Override
            public void run() {
                try {
                    mMessage.setText(message);
                } catch (Exception e) {

                }
            }
        });
    }

    @Override
    public void dismiss() {
        super.dismiss();
        try {
            ButterKnife.unbind(this);
        }catch (Exception e){

        }
    }
}
