package de.fh_zwickau.tm.custom_view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import de.fh_zwickau.tm.domain.Task;

public class TaskRectangle extends View {

    private Rect rectangle;
    private Paint paint;
    private Task task;
    public TaskRectangle(Context context, int w, int h, Task task, int color) {
        super(context);
        int x = 0;
        int y = 0;
        this.task = task;
        rectangle = new Rect(x, y, w, h );
        paint = new Paint();
        paint.setColor(color);
    }

    public Task getTask(){
        return task;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.BLUE);
        canvas.drawRect(rectangle, paint);
    }

}