package com.funlisten.business.search.view.viewholder;

import android.view.View;
import android.widget.TextView;

import com.funlisten.R;
import com.funlisten.base.viewHolder.ZYBaseViewHolder;
import com.funlisten.business.search.model.bean.ZYSearchHeaderInfo;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/8/9.
 */

public class ZYSearhListHeaderVH extends ZYBaseViewHolder<ZYSearchHeaderInfo> {

    @Bind(R.id.total_count)
    TextView total_count;

    @Bind(R.id.textSort)
    TextView sort;

    SortListener sortListener;

    ZYSearchHeaderInfo mData;

    public ZYSearhListHeaderVH(SortListener listener) {
        this.sortListener = listener;
    }

    @Override
    public void updateView(ZYSearchHeaderInfo data, int position) {
        if(data != null){
            mData = data;
            total_count.setText("共"+mData.totalCount+"个搜索结果");
        }
    }

    @OnClick({R.id.total_count})
    public void OnClick(View view){
        sortListener.onSort();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.gd_search_list_hd_vh;
    }

    interface SortListener{
        void onSort();
    }
}
