package de.fh_zwickau.tm.custom_view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

public class VerticalHourLine extends View {
    private int height;
    private int width;
    private Paint paint;
    private Rect rect;
    public VerticalHourLine(Context context, int width, int height) {
        super(context);
        this.height = height;
        this.width = width;
        this.paint = new Paint();
        paint.setColor(Color.GREEN);
        rect = new Rect(0, 0, width, height);
    }

    @Override
    protected void onDraw(Canvas canvas){
        canvas.drawRect(rect,paint);
    }

}
