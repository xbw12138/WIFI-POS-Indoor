package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.graphics.Bitmap.Config;

import java.lang.Runnable;
/**
 * Created by xubowen on 2017/5/10.
 */
public class MapView extends View {
    Paint paint = new Paint();
    Point point = new Point();
    private Context context;
    private Activity activity;
    public MapView(Context context, Activity activity) {
        super(context);
        this.context=context;
        this.activity=activity;
        this.setBackground(getResources().getDrawable(R.drawable.map));
        WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        int wb=getWidth()/width;
        int hb=getHeight()/height;
        paint.setColor(Color.RED);
        paint.setStrokeWidth(15);
        paint.setStyle(Paint.Style.STROKE);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        Drawable d = getResources().getDrawable(R.drawable.ic_directions_walk_black_24dp);
        Bitmap b= drawableToBitmap(d);
        if(b!=null){
            canvas.drawBitmap(b, point.x, point.y, paint);
        }
        //canvas.drawCircle(point.x, point.y, 5, paint);
    }
    public void change(float x, float y) {
        point.x = x;
        point.y = y;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        });
    }
    //矢量图转位图
    public static Bitmap drawableToBitmap (Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable)drawable).getBitmap();
        }
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }
}
class Point {
    float x, y;
}