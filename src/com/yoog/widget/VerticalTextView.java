package com.yoog.widget;

import com.yoog.widget.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.TextView;

public class VerticalTextView extends TextView {
    public final static int ORIENTATION_UP_TO_DOWN = 0;
    public final static int ORIENTATION_DOWN_TO_UP = 1;
    public final static int ORIENTATION_LEFT_TO_RIGHT = 2;
    public final static int ORIENTATION_RIGHT_TO_LEFT = 3;

    private Rect text_bounds = new Rect();
    private int text_width = 0, text_height = 0;
    private Path path = new Path();
    private int direction;

    public VerticalTextView(Context context) {
        super(context);
    }

    public VerticalTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.VerticalTextView);
        direction = a.getInt(R.styleable.VerticalTextView_direction, 0);
        a.recycle();

        requestLayout();
        invalidate();
    }

    public void setDirection(int direction) {
        this.direction = direction;

        requestLayout();
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        getPaint().getTextBounds(getText().toString(), 0, getText().length(), text_bounds);
        text_width = (int) getPaint().measureText(getText().toString());
        text_height = text_bounds.height() + Math.abs(text_width - text_bounds.width());
        if (direction == ORIENTATION_LEFT_TO_RIGHT || direction == ORIENTATION_RIGHT_TO_LEFT) {
            setMeasuredDimension(measureHeight(widthMeasureSpec), measureWidth(heightMeasureSpec));
        } else if (direction == ORIENTATION_UP_TO_DOWN || direction == ORIENTATION_DOWN_TO_UP) {
            setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
        }
    }

    private int measureWidth(int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = text_height + getPaddingTop() + getPaddingBottom();
            // result = text_bounds.height();
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    private int measureHeight(int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = text_width + getPaddingLeft() + getPaddingRight();
            // result = text_bounds.width();
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    private int startX = 0, startY = 0;
    private int stopX = 0, stopY = 0;
    private int hOffset = 0, vOffset = 0;

    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);

        canvas.save();

        switch (direction) {
            case ORIENTATION_UP_TO_DOWN:
                startX = (getWidth() - text_height >> 1);
                startY = (getHeight() - text_width >> 1);
                stopX = (getWidth() - text_height >> 1);
                stopY = (getHeight() + text_width >> 1);
                break;
            case ORIENTATION_DOWN_TO_UP:
                startX = (getWidth() + text_height >> 1);
                startY = (getHeight() + text_width >> 1);
                stopX = (getWidth() + text_height >> 1);
                stopY = (getHeight() - text_width >> 1);
                break;
            case ORIENTATION_LEFT_TO_RIGHT:
                startX = (getWidth() - text_width >> 1);
                startY = (getHeight() + text_height >> 1);
                stopX = (getWidth() + text_width >> 1);
                stopY = (getHeight() + text_height >> 1);
                break;
            case ORIENTATION_RIGHT_TO_LEFT:
                startX = (getWidth() + text_width >> 1);
                startY = (getHeight() - text_height >> 1);
                stopX = (getWidth() - text_width >> 1);
                stopY = (getHeight() - text_height >> 1);
                break;
        }
        if (text_height >= text_bounds.height()) {//adjust negative offset
            hOffset = (text_bounds.height() - text_height) >> 1;
        }
        if (text_width >= text_bounds.width()) {//adjust negative offset
            vOffset = (text_bounds.width() - text_width) >> 1;
        }
        path.moveTo(startX, startY);
        path.lineTo(stopX, stopY);

        this.getPaint().setColor(this.getCurrentTextColor());
        //canvas.drawLine(startX, startY, stopX, stopY, this.getPaint());
        canvas.drawTextOnPath(getText().toString(), path, hOffset, vOffset, this.getPaint());

        canvas.restore();
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);

        //https://github.com/yoog568/VerticalTextView/issues/3
        requestLayout();
        // redraw view with new layout
        invalidate();
    }
}
