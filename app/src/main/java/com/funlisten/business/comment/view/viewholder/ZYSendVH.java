package com.funlisten.business.comment.view.viewholder;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.funlisten.R;
import com.funlisten.base.viewHolder.ZYBaseViewHolder;
import com.funlisten.utils.ZYScreenUtils;
import com.funlisten.utils.ZYUtils;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by gd on 2017/7/25.
 */

public class ZYSendVH extends ZYBaseViewHolder {
    @Bind(R.id.edit_id)
    EditText editText;

    @Bind(R.id.send_tv)
    TextView send;

    SendMessageListener listener;

    public ZYSendVH(SendMessageListener listener) {
        this.listener = listener;
    }

    @Override
    public void updateView(Object data, int position) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = s.toString();
                if(TextUtils.isEmpty(text)){
                    send.setEnabled(false);
                }else {
                    send.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        send.setEnabled(false);
    }

    @OnClick({R.id.send_tv})
    public void OnClick(){
        String text = editText.getText().toString();
        listener.sendMessage(text);
        editText.setText("");
        ZYUtils.hideInput(send);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.gd_send_comment_layout;
    }

    @Override
    public void attachTo(ViewGroup viewGroup) {
        View view  = LayoutInflater.from(viewGroup.getContext()).inflate(getLayoutResId(), viewGroup, false);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ZYScreenUtils.dp2px(viewGroup.getContext(),60));
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        view.setLayoutParams(layoutParams);
//        TextView textView = (TextView) view.findViewById(R.id.send_tv);
//        textView.setBackgroundResource(R.drawable.send_selector);
        bindView(view);
        viewGroup.addView(getItemView());
    }

    public interface SendMessageListener{
        void sendMessage(String text);
    }
}
