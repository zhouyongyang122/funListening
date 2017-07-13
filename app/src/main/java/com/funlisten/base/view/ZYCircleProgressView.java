package com.funlisten.base.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.funlisten.R;

/**
 * Created by ZY on 17/3/28.
 */

public class ZYCircleProgressView extends View {

    private static final String TAG = "ZYCircleProgressView";

    private final RectF progressRect;
    private final RectF middleRect;
    private final Paint progressPaint;
    private final Paint middlePaint;

    private final Context mContext;

    private int max_progress;

    private int cur_progress;

    private int bg_color;

    private float stroke_width;

    private int progress_bg_color;

    private int progress_color;


    public ZYCircleProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        progressRect = new RectF();
        middleRect = new RectF();
        progressPaint = new Paint();
        middlePaint = new Paint();

        if (attrs != null) {
            try {
//                TypedArray typedArray = context.obtainStyledAttributes(attrs,
//                        R.styleable.ZYCircleProgressView);
//                max_progress = typedArray.getInt(R.styleable.ZYCircleProgressView_max_progress, 100);
//                cur_progress = typedArray.getInt(R.styleable.ZYCircleProgressView_cur_progress, 0);
//                stroke_width = typedArray.getDimension(
//                        R.styleable.ZYCircleProgressView_stroke_width,
//                        0);
//                bg_color = typedArray.getResourceId(
//                        R.styleable.ZYCircleProgressView_bg_color, -1);
//                progress_bg_color = typedArray.getResourceId(
//                        R.styleable.ZYCircleProgressView_progress_bg_color, -1);
//                progress_color = typedArray.getResourceId(
//                        R.styleable.ZYCircleProgressView_progress_color, -1);
            } catch (Exception e) {

            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = this.getWidth();
        int height = this.getHeight();

        if (width != height) {
            int min = Math.min(width, height);
            width = min;
            height = min;
        }

        // 设置画笔相关属性
        progressPaint.setAntiAlias(true);
        middlePaint.setAntiAlias(true);
        canvas.drawColor(Color.TRANSPARENT);
        progressPaint.setStrokeWidth(stroke_width);
        progressPaint.setStyle(Paint.Style.STROKE);
        middlePaint.setStyle(Paint.Style.FILL);


        progressRect.left = stroke_width / 2; // 左上角x
        progressRect.top = stroke_width / 2; // 左上角y
        progressRect.right = width - stroke_width / 2; // 左下角x
        progressRect.bottom = height - stroke_width / 2; // 右下角y

        middleRect.left = progressRect.left - stroke_width; // 左上角x
        middleRect.top = progressRect.top - stroke_width; // 左上角y
        middleRect.right = progressRect.right - stroke_width; // 左下角x
        middleRect.bottom = progressRect.bottom - stroke_width; // 右下角y

        progressPaint.setColor(mContext.getResources().getColor(progress_color));
        canvas.drawArc(progressRect, -90, 360, false, progressPaint);
        progressPaint.setColor(mContext.getResources().getColor(progress_bg_color));
        canvas.drawArc(progressRect, -90, ((float) cur_progress / max_progress) * 360, false, progressPaint);
        middlePaint.setColor(mContext.getResources().getColor(bg_color));
        canvas.drawArc(progressRect, -90, 360, false, middlePaint);
    }

    public void setProgress(int progress) {
        this.cur_progress = progress;
        this.invalidate();
    }

    public void setProgressNotInUiThread(int progress) {
        this.cur_progress = progress;
        this.postInvalidate();
    }

    public void setMax_progress(int max_progress) {
        this.max_progress = max_progress;
    }

    public void setCur_progress(int cur_progress) {
        this.cur_progress = cur_progress;
    }

    public void setBg_color(int bg_color) {
        this.bg_color = bg_color;
    }

    public void setStroke_width(float stroke_width) {
        this.stroke_width = stroke_width;
    }

    public void setProgress_bg_color(int progress_bg_color) {
        this.progress_bg_color = progress_bg_color;
    }

    public void setProgress_color(int progress_color) {
        this.progress_color = progress_color;
    }
}
