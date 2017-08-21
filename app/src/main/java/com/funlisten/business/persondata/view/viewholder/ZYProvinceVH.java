package com.funlisten.business.persondata.view.viewholder;

import android.widget.TextView;

import com.funlisten.R;
import com.funlisten.base.viewHolder.ZYBaseViewHolder;
import com.funlisten.business.set.model.bean.ZYProvince;

import butterknife.Bind;

/**
 * Created by Administrator on 2017/7/23.
 */

public class ZYProvinceVH  extends ZYBaseViewHolder<ZYProvince>{
    @Bind(R.id.textName)
    TextView textName;
    @Override
    public void updateView(ZYProvince data, int position) {
        textName.setText(data.provinceName);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.gd_province_city_item;
    }
}
