package com.funlisten.business.album.view.viewHolder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.funlisten.R;
import com.funlisten.base.viewHolder.ZYBaseViewHolder;
import com.funlisten.utils.ZYScreenUtils;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by gd on 2017/7/27.
 */

public class ZYBatchDownFooterVH extends ZYBaseViewHolder {

    @Bind(R.id.img_selected_all)
    ImageView selectedAll;

    AllSelectListen selectListen;

    public ZYBatchDownFooterVH(AllSelectListen selectListen) {
        this.selectListen = selectListen;
    }

    @Override
    public void updateView(Object data, int position) {

    }

    @Override
    public void attachTo(ViewGroup viewGroup) {
        View view  = LayoutInflater.from(viewGroup.getContext()).inflate(getLayoutResId(), viewGroup, false);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ZYScreenUtils.dp2px(viewGroup.getContext(),50));
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        view.setLayoutParams(layoutParams);
        bindView(view);
        viewGroup.addView(getItemView());
    }

    @Override
    public int getLayoutResId() {
        return R.layout.gd_batch_down_ft_item;
    }

    @OnClick({R.id.select_all,R.id.all_down})
    public void OnClick(View view){
        switch (view.getId()){
            case R.id.select_all:
                if(selectedAll.isSelected()){
                    selectedAll.setSelected(false);
                    selectListen.onSelectAll(false);
                }else {
                    selectedAll.setSelected(true);
                    selectListen.onSelectAll(true);
                }
                break;
            case R.id.all_down:
                selectListen.onAllDown();
                break;

        }

    }

    public void isSelectAll(boolean isAll){
        selectedAll.setSelected(isAll);
    }

    public interface AllSelectListen{
        void onSelectAll(boolean isSelectAll);
        void onAllDown();
    }
}
