package com.brainup.readbyapp.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.util.AttributeSet;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.brainup.readbyapp.R;


public class DashedLineView extends View {
    private float density;
    private Paint paint;
    private Path path;
    private PathEffect effects;

    public DashedLineView(Context context) {
        super(context);
        init(context);
    }

    public DashedLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DashedLineView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        // density = DisplayUtil.getDisplayDensity(context);
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3);
        //set your own color
        paint.setColor(ContextCompat.getColor(context, R.color.black66));
        path = new Path();
        //array is ON and OFF distances in px (4px line then 2px space)
        effects = new DashPathEffect(new float[]{4, 2, 4, 2}, 0);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        paint.setPathEffect(effects);
        int measuredHeight = getMeasuredHeight();
        int measuredWidth = getMeasuredWidth();
        path.moveTo(0, 0);
        if (measuredHeight <= measuredWidth) {
            // horizontal
            path.lineTo(measuredWidth, 0);
        } else {
            // vertical
            path.lineTo(0, measuredHeight);
        }
        canvas.drawPath(path, paint);

    }
}