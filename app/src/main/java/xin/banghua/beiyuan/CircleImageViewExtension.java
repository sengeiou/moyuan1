package xin.banghua.beiyuan;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

public class CircleImageViewExtension extends de.hdodenhof.circleimageview.CircleImageView {
    private boolean isVIP = false;
    private static Bitmap gifbmp = null;
    //private static Paint paint;



    public CircleImageViewExtension(Context context) {
        super(context);
    }

    public CircleImageViewExtension(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CircleImageViewExtension(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void isVIP(boolean isVIP,Resources resources) {
        this.isVIP = isVIP;
        //图片不能是矢量图
        gifbmp = BitmapFactory.decodeResource(resources, R.drawable.vipicon);

//        paint = new Paint();
//        paint.setColor(Color.parseColor("#469de6"));
//        paint.setStyle(Paint.Style.FILL);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (isVIP) {
            //画个长方形
            //canvas.drawRect(0, 0, 15, 15, paint);
            //画个bit图
            canvas.drawBitmap(gifbmp, canvas.getWidth()-100, canvas.getHeight()-100, null);
        }
    }

}
