package com.funlisten.base.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.funlisten.R;
import com.funlisten.utils.ZYScreenUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouyongyang on 16/10/12.
 */

public class ZYTopTabBar extends RelativeLayout implements ViewTreeObserver.OnGlobalLayoutListener, View.OnClickListener {

    private Context context;

    private LinearLayout tabLinear;

    private View moveLine;

    private RelativeLayout layoutMoveLine;

    //tabItems集合
    private ArrayList<RelativeLayout> tabItems;

    //tab名称颜色 正常情况
    private int tabNameNormalColor;

    //tab名称颜色 选择情况
    private int tabNameSelectColor;

    //tab名称字体大小
    private int tabNameTextSize;

    //item之间的间距
    private int itemSpace;

    //item的宽度
    private int itemWidth;

    //滑动横线的宽度
    private int lineWidth;

    //当前选择的tab
    private int curTab = 0;

    private OnTopTabBarChangeListener onTopTabBarChangeListener;

    private int defSelected;

    public ZYTopTabBar(Context context) {
        super(context);
        init(context);
    }

    public ZYTopTabBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ZYTopTabBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ZYTopTabBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        this.context = context;

        tabLinear = new LinearLayout(context);
        tabLinear.setOrientation(LinearLayout.HORIZONTAL);
        LayoutParams tabLinearLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        tabLinear.setLayoutParams(tabLinearLayoutParams);
        addView(tabLinear);

        layoutMoveLine = new RelativeLayout(context);
        LayoutParams moveLineLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        moveLineLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        layoutMoveLine.setLayoutParams(moveLineLayoutParams);
        addView(layoutMoveLine);

        moveLine = new View(context);
        moveLineLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        moveLineLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        moveLine.setLayoutParams(moveLineLayoutParams);
        layoutMoveLine.addView(moveLine);
    }

    /**
     * 默认样式
     *
     * @param tabNames tab名称集合
     */
    public void addTabItems(List<String> tabNames) {
        addTabItems(tabNames, 2, R.color.c1, R.color.c4, R.color.c1, 17);
    }

    /**
     * 默认样式 加间距
     *
     * @param tabNames  tab名称集合
     * @param itemSpace tab名称之间的间隙 dp
     */
    public void addTabItems(List<String> tabNames, int itemSpace) {
        addTabItems(tabNames, 2, R.color.c1, R.color.c4, R.color.c1, 17, itemSpace);
    }

    /**
     * 默认样式 加间距,字体颜色
     *
     * @param tabNames  tab名称集合
     * @param itemSpace tab名称之间的间隙 dp
     */
    public void addTabItems(List<String> tabNames, int itemSpace, int tabNameNormalColor, int tabNameSelectColor) {
        addTabItems(tabNames, 2, R.color.c1, tabNameNormalColor, tabNameSelectColor, 17, itemSpace);
    }

    /**
     * 默认样式 加间距/字体大小
     *
     * @param tabNames        tab名称集合
     * @param itemSpace       tab名称之间的间隙 dp
     * @param tabNameTextSize 字体大小
     */
    public void addTabItems(List<String> tabNames, int itemSpace, int tabNameTextSize) {
        addTabItems(tabNames, 2, R.color.c1, R.color.c4, R.color.c1, tabNameTextSize, itemSpace);
    }

    /**
     * @param tabNames           tab名称集合
     * @param moveLineHeight     底部可移动横线高度
     * @param moveLineColor      底部可移动横线颜色
     * @param tabNameSelectColor tab名称选择后的颜色
     * @param tabNameNormalColor tab名称正常情况下的颜色
     * @param tabNameTextSize    tab名称大小
     */
    public void addTabItems(List<String> tabNames, int moveLineHeight, int moveLineColor, int tabNameNormalColor, int tabNameSelectColor, int tabNameTextSize) {
        addTabItems(tabNames, moveLineHeight, moveLineColor, tabNameNormalColor, tabNameSelectColor, tabNameTextSize, 0);
    }

    /**
     * @param tabNames           tab名称集合
     * @param moveLineHeight     底部可移动横线高度
     * @param moveLineColor      底部可移动横线颜色
     * @param tabNameSelectColor tab名称选择后的颜色
     * @param tabNameNormalColor tab名称正常情况下的颜色
     * @param itemSpace          tab名称之间的间隙 dp
     * @param tabNameTextSize    tab名称大小
     */
    public void addTabItems(List<String> tabNames, int moveLineHeight, int moveLineColor, int tabNameNormalColor, int tabNameSelectColor, int tabNameTextSize, int itemSpace) {
        if (tabNames != null && tabNames.size() > 0) {
            setMoveLineColor(moveLineColor);
            setMoveLineHeight(moveLineHeight);
            this.tabNameNormalColor = tabNameNormalColor;
            this.tabNameSelectColor = tabNameSelectColor;
            this.itemSpace = ZYScreenUtils.dp2px(context, itemSpace);
            this.tabNameTextSize = tabNameTextSize;
            int position = 0;
            for (String tabName : tabNames) {
                addTabItem(tabName, position);
                position++;
            }

            RelativeLayout itemView = tabItems.get(tabItems.size() - 1);
            itemView.getViewTreeObserver().addOnGlobalLayoutListener(this);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * @param name
     * @param position 当前添加的位置 -1表示从最后添加
     */
    private void addTabItem(String name, int position) {
        if (tabItems == null) {
            tabItems = new ArrayList<RelativeLayout>();
        }
        RelativeLayout itemView = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.zy_item_topbar, null);
        itemView.setTag(position);
        itemView.setOnClickListener(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        layoutParams.weight = 1;
        if (position != 0) {
            layoutParams.leftMargin = itemSpace;
        }
        itemView.setLayoutParams(layoutParams);
        TextView textView = (TextView) itemView.findViewById(R.id.tv_title);
        textView.setText(name);
        textView.setTextSize(tabNameTextSize);
        if (curTab == position) {
            textView.setTextColor(context.getResources().getColor(tabNameSelectColor));
        } else {
            textView.setTextColor(context.getResources().getColor(tabNameNormalColor));
        }
        tabLinear.addView(itemView);
        tabItems.add(itemView);
    }

    /**
     * viewPager的滚动回调时面调用此方法
     *
     * @param position
     * @param positionOffset
     * @param positionOffsetPixels
     */
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        LayoutParams layoutParams = (LayoutParams) layoutMoveLine.getLayoutParams();
        layoutParams.leftMargin = (int) (positionOffsetPixels * ((float) (itemWidth + itemSpace) / (float) ZYScreenUtils.getScreenWidth(context))) + position * (itemWidth + itemSpace);
        layoutMoveLine.setLayoutParams(layoutParams);
    }

    /**
     * viewPager选中时调用此方法
     *
     * @param position
     */
    public void onPageSelected(int position) {
        curTab = position;

        RelativeLayout itemView;
        for (int i = 0; i < tabItems.size(); i++) {
            itemView = tabItems.get(i);
            TextView textView = (TextView) itemView.findViewById(R.id.tv_title);
            if (i == position) {
                textView.setTextColor(context.getResources().getColor(tabNameSelectColor));
            } else {
                textView.setTextColor(context.getResources().getColor(tabNameNormalColor));
            }
        }
    }

    /**
     * @param moveLineHeight dp
     */
    public void setMoveLineHeight(int moveLineHeight) {
        if (layoutMoveLine != null) {
            LayoutParams layoutParams = (LayoutParams) layoutMoveLine.getLayoutParams();
            layoutParams.height = ZYScreenUtils.dp2px(context, moveLineHeight);
            layoutMoveLine.setLayoutParams(layoutParams);
        }
    }

    /**
     * @param moveLineWidth px
     */
    private void setMoveLineWidth(int moveLineWidth) {
        if (layoutMoveLine != null) {
            LayoutParams layoutParams = (LayoutParams) layoutMoveLine.getLayoutParams();
            layoutParams.width = moveLineWidth;
            layoutMoveLine.setLayoutParams(layoutParams);

            if (lineWidth > 0) {
                layoutParams = (LayoutParams) moveLine.getLayoutParams();
                layoutParams.width = lineWidth;
                moveLine.setLayoutParams(layoutParams);
            }
        }
    }

    public void setMoveLineColor(@ColorRes int moveLineColor) {
        if (moveLine != null) {
            moveLine.setBackgroundColor(context.getResources().getColor(moveLineColor));
        }
    }

    public void setTextColor(@ColorRes int normalColor, @ColorRes int selectColor) {
        tabNameNormalColor = normalColor;
        tabNameSelectColor = selectColor;
        for (int i = 0; i < tabItems.size(); i++) {
            View itemView = tabItems.get(i);
            TextView textView = (TextView) itemView.findViewById(R.id.tv_title);
            if (i == curTab) {
                textView.setTextColor(context.getResources().getColor(tabNameSelectColor));
            } else {
                textView.setTextColor(context.getResources().getColor(tabNameNormalColor));
            }
        }
    }

    @Override
    public void onGlobalLayout() {

        RelativeLayout itemView = tabItems.get(tabItems.size() - 1);
        itemWidth = itemView.getWidth();
        setMoveLineWidth(itemWidth);

        if (defSelected > 0) {
            try {
                LayoutParams layoutParams = (LayoutParams) layoutMoveLine.getLayoutParams();
                layoutParams.leftMargin = defSelected * (itemWidth + itemSpace);
                layoutMoveLine.setLayoutParams(layoutParams);
            } catch (Exception e) {

            }
        }

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                itemView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            } else {
                itemView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        } catch (Exception e) {

        }
    }

    public void setDefSelected(int defSelected) {
        this.defSelected = defSelected;
    }

    @Override
    public void onClick(View v) {
        int position = 0;
        try {
            position = Integer.valueOf(v.getTag().toString());
        } catch (Exception e) {

        }
        if (curTab == position) {
            return;
        }
        curTab = position;
        if (onTopTabBarChangeListener != null) {
            onTopTabBarChangeListener.onChange(position);
        }
    }

    public void setOnTopTabBarChangeListener(OnTopTabBarChangeListener onTopTabBarChangeListener) {
        this.onTopTabBarChangeListener = onTopTabBarChangeListener;
    }

    public interface OnTopTabBarChangeListener {
        void onChange(int position);
    }

    public int getCurTab() {
        return curTab;
    }

    public void setPointVisible(int position, int visible) {
        ImageView poinitView = (ImageView) tabItems.get(position).findViewById(R.id.img_point);
        if (poinitView != null) {
            poinitView.setVisibility(visible);
        }
    }

    /**
     * 设置滑动横线的宽度px
     *
     * @param lineWidth
     */
    public void setLineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
    }
}
