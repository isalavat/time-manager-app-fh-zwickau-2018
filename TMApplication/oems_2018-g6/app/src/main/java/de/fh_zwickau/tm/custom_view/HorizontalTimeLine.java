package de.fh_zwickau.tm.custom_view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

public class HorizontalTimeLine extends View {
    private int width;
    private int height;

    public HorizontalTimeLine(Context context, int width, int height) {
        super(context);
        this.width = width;
        this.height = height;
    }

    @Override
    protected void onDraw(Canvas canvas){
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        Rect rect = new Rect(0,0,width,height);
        canvas.drawRect(rect,paint);
    }
}
