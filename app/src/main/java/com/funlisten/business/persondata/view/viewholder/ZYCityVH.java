package com.funlisten.business.persondata.view.viewholder;

import android.widget.TextView;

import com.funlisten.R;
import com.funlisten.base.viewHolder.ZYBaseViewHolder;
import com.funlisten.business.user.model.ZYProvince;

import butterknife.Bind;

/**
 * Created by gd on 2017/7/23.
 */

public class ZYCityVH extends ZYBaseViewHolder<ZYProvince.City>{

    @Bind(R.id.textName)
    TextView textName;

    @Override
    public void updateView(ZYProvince.City data, int position) {
        textName.setText(data.cityName);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.gd_province_city_item;
    }
}
