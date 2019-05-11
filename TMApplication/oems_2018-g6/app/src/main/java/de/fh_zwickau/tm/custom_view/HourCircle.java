package de.fh_zwickau.tm.custom_view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class HourCircle extends View {
    private Paint paint;

    public HourCircle(Context context) {
        super(context);
        this.paint = new Paint();
        paint.setColor(Color.GREEN);
    }

    @Override
    protected void onDraw(Canvas canvas){
        canvas.drawCircle(30,35,35, paint);
    }

}
