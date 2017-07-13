package com.funlisten.base.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.funlisten.R;

/**
 * Created by ZY on 17/3/30.
 */

public class ZYRoudCornerRelativeLayout extends RelativeLayout {

    private float roudCorner;

    private float round_corner_top_left;

    private float round_corner_top_right;

    private float round_corner_bottom_left;

    private float round_corner_bottom_right;

    public ZYRoudCornerRelativeLayout(Context context) {
        super(context);
        init(context, null);
    }

    public ZYRoudCornerRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ZYRoudCornerRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            try {
//                TypedArray typedArray = context.obtainStyledAttributes(attrs,
//                        R.styleable.ZYRoudCornerView);
//                roudCorner = typedArray.getDimension(
//                        R.styleable.ZYRoudCornerView_round_corner,
//                        -1);
//                round_corner_top_left = typedArray.getDimension(
//                        R.styleable.ZYRoudCornerView_round_corner_top_left,
//                        0);
//                round_corner_top_right = typedArray.getDimension(
//                        R.styleable.ZYRoudCornerView_round_corner_top_right,
//                        0);
//                round_corner_bottom_left = typedArray.getDimension(
//                        R.styleable.ZYRoudCornerView_round_corner_bottom_left,
//                        0);
//                round_corner_bottom_right = typedArray.getDimension(
//                        R.styleable.ZYRoudCornerView_round_corner_bottom_right,
//                        0);
            } catch (Exception e) {

            }
        }
    }

    @Override
    public boolean isInEditMode() {
        return true;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        Path path = new Path();
        path.addRoundRect(new RectF(0, 0, getMeasuredWidth(), getMeasuredHeight()), roudCorner, roudCorner, Path.Direction.CW);
        canvas.clipPath(path, Region.Op.REPLACE);
        super.dispatchDraw(canvas);

    }
}
