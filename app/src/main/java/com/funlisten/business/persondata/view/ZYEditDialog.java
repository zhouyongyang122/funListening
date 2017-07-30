package com.funlisten.business.persondata.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.funlisten.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/7/24.
 */

public class ZYEditDialog extends Dialog {

    Context mContext;

    @Bind(R.id.edit_id)
    EditText editText;

    @Bind(R.id.cancel)
    TextView cancel;

    @Bind(R.id.ensure)
    TextView ensure;

    OnSelectText onSelectText;
    public ZYEditDialog(@NonNull Context context) {
        this(context, R.style.Dialog_Fullscreen);
        this.mContext =context;
    }

    public ZYEditDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, R.style.Dialog_Fullscreen);
        this.mContext =context;
    }

    protected ZYEditDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext =context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gd_dialog_edit_layout);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.cancel,R.id.ensure})
    public void OnClick(View view){
        switch (view.getId()){
            case R.id.cancel:
                dismiss();
                break;
            case R.id.ensure:
                String text  = editText.getText().toString();
                if(onSelectText != null)onSelectText.onSelecte(text);
                dismiss();
                break;
        }

    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        ButterKnife.unbind(this);
    }

    public void setOnSelectText(OnSelectText onSelectText){
        this.onSelectText = onSelectText;
    }
   public interface OnSelectText{
        void onSelecte(String text);
    }

}
