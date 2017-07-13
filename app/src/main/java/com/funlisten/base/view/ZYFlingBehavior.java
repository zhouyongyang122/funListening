package com.funlisten.base.view;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by ZY on 17/7/4.
 * http://stackoverflow.com/questions/30923889/flinging-with-recyclerview-appbarlayout/32174067#32174067
 * 解决RecyclerView在CollapsingToolbarLayout中向下滚动不流畅问题
 */

public final class ZYFlingBehavior extends AppBarLayout.Behavior {

    private static final int TOP_CHILD_FLING_THRESHOLD = 3;
    private boolean isPositive;

    public ZYFlingBehavior() {
    }

    public ZYFlingBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onNestedFling(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, float velocityX, float velocityY, boolean consumed) {
        if (velocityY > 0 && !isPositive || velocityY < 0 && isPositive) {
            velocityY = velocityY * -1;
        }
//        if (target instanceof SwipeRefreshLayout && velocityY < 0) {
//            target = ((SwipeRefreshLayout) target).getChildAt(0);
//        }

        if (target instanceof RecyclerView && velocityY < 0) {
            final RecyclerView recyclerView = (RecyclerView) target;
            final View firstChild = recyclerView.getChildAt(0);
            final int childAdapterPosition = recyclerView.getChildAdapterPosition(firstChild);
            consumed = childAdapterPosition > TOP_CHILD_FLING_THRESHOLD;
        }
        System.out.println(consumed + "-----------");
        return super.onNestedFling(coordinatorLayout, child, target, velocityX, velocityY, consumed);
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, int dx, int dy, int[] consumed) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed);
        isPositive = dy > 0;
    }
}
