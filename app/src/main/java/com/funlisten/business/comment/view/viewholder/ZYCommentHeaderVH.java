package com.funlisten.business.comment.view.viewholder;

import android.widget.TextView;

import com.funlisten.R;
import com.funlisten.base.viewHolder.ZYBaseViewHolder;
import com.funlisten.business.comment.model.bean.ZYCommentHeaderInfo;

import butterknife.Bind;

/**
 * Created by Administrator on 2017/7/26.
 */

public class ZYCommentHeaderVH extends ZYBaseViewHolder<ZYCommentHeaderInfo>{
    @Bind(R.id.comment_count)
    TextView count;

    ZYCommentHeaderInfo mInfo;

    @Override
    public void updateView(ZYCommentHeaderInfo data, int position) {
        if(data != null){
            mInfo = data;
        }
        if(count != null && mInfo != null) count.setText("评论 ("+mInfo.totalCount+")");
    }

    @Override
    public int getLayoutResId() {
        return R.layout.gd_comment_hd_item;
    }
}
